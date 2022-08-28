package com.lyz.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyz.common.dto.GroupDto;
import com.lyz.common.lang.Result;
import com.lyz.entity.Group;
import com.lyz.entity.GroupMember;
import com.lyz.entity.User;
import com.lyz.service.GroupMemberService;
import com.lyz.service.GroupService;
import com.lyz.service.UserService;
import com.lyz.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyz
 * @since 2022-01-30
 */
@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private GroupMemberService groupMemberService;

    @GetMapping("memberList")
    public Result fetchUserName(){
        List<User> userList = userService.list(new QueryWrapper<User>().select("id", "username").eq("role", "student"));
        JSONArray memberList = new JSONArray();
        for (int i = 0; i < userList.size(); i++) {
            JSONObject member = new JSONObject();
            member.set("label", userList.get(i).getUsername());
            member.set("key", userList.get(i).getId());
            member.set("search", userList.get(i).getUsername());
            memberList.put(member);
        }
        return Result.succ(MapUtil.builder().put("memberList", memberList).map());
    }

    @GetMapping("groupList")
    public Result fetchGroup(@RequestHeader("X-Token") String token) {
        int userId = Integer.parseInt(jwtUtils.getClaimByToken(token).getSubject());
        List<GroupMember> groupList = groupMemberService.list(new QueryWrapper<GroupMember>().eq("memberId", userId).or().eq("supervisorId", userId));
        List<Integer> groupIdList = groupList.stream().map(GroupMember::getGroupId).collect(Collectors.toList());
        if(groupIdList.size() == 0) {
            return Result.succ(null);
        }
        List<Group> group = groupService.list(new QueryWrapper<Group>().in("id", groupIdList));
        return Result.succ(group);
    }

    @PostMapping("create")
    public Result createGroup(@RequestHeader("X-Token") String token, @RequestBody GroupDto groupDto){
        int userId = Integer.parseInt(jwtUtils.getClaimByToken(token).getSubject());
        User user = userService.getById(userId);
        Group group = new Group();
        group.setGroupName(groupDto.getGroupName());
        group.setCreator(user.getUsername());
        groupService.save(group);
        int groupId = group.getId();
        List<Integer> member = groupDto.getMemberList();
        for (int i = 0; i < member.size(); i++) {
            GroupMember groupMember = new GroupMember();
            groupMember.setGroupId(groupId);
            groupMember.setMemberId(member.get(i));
            groupMember.setSupervisorId(userId);
            groupMemberService.save(groupMember);
        }
        return Result.succ(group);
    }

    @PostMapping("delete")
    public Result deleteGroup(@RequestBody Group group) {
        groupService.removeById(group);
        groupMemberService.remove(new QueryWrapper<GroupMember>().eq("groupId", group.getId()));
        return Result.succ("delete successfully");
    }
}
