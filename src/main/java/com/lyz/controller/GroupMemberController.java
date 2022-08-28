package com.lyz.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyz.common.lang.Result;
import com.lyz.entity.Group;
import com.lyz.entity.GroupMember;
import com.lyz.entity.User;
import com.lyz.service.GroupMemberService;
import com.lyz.service.GroupService;
import com.lyz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyz
 * @since 2022-01-31
 */
@RestController
@RequestMapping("/group-member")
public class GroupMemberController {

    @Autowired
    GroupService groupService;
    @Autowired
    GroupMemberService groupMemberService;
    @Autowired
    UserService userService;

    @GetMapping("people")
    public Result getPeople(int groupId) {
        List<GroupMember> groupMember = groupMemberService.list(new QueryWrapper<GroupMember>().eq("groupId", groupId));
        List<Integer> supervisorIdList = groupMember.stream().map(GroupMember::getSupervisorId).collect(Collectors.toList());
        List<User> supervisorUser = userService.list(new QueryWrapper<User>().in("id", supervisorIdList));
        List<Integer> memberIdList = groupMember.stream().map(GroupMember::getMemberId).collect(Collectors.toList());
        List<User> memberUser = userService.list(new QueryWrapper<User>().in("id", memberIdList));
        return Result.succ(MapUtil.builder()
                .put("groupMember", memberUser)
                .put("supervisor", supervisorUser)
                .map());
    }

    @GetMapping("personInfo")
    public Result getPersonInfo(int personId) {
        User user = userService.getById(personId);
        return Result.succ(MapUtil.builder()
                .put("person", user)
                .map());
    }
}
