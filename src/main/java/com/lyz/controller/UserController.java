package com.lyz.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyz.common.dto.GroupDto;
import com.lyz.common.lang.Result;
import com.lyz.entity.*;
import com.lyz.service.GroupMemberService;
import com.lyz.service.GroupService;
import com.lyz.service.UserService;
import com.lyz.util.JwtUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyz
 * @since 2021-12-26
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private GroupMemberService groupMemberService;
    @Autowired
    private GroupService groupService;

    @GetMapping("info")
    public Result fetchUserName(int userId){
        User user = userService.getById(userId);
        return Result.succ(MapUtil.builder().put("user", user).map());
    }

    @PostMapping("updateInfo")
    public Result updateUserInfo(@RequestBody User user) {
        userService.saveOrUpdate(user);
        return Result.succ("update successfully");
    }

    @PostMapping("updatePassword")
    public Result updatePassword(@RequestHeader("X-Token") String token, String password) {
        int userId = Integer.parseInt(jwtUtils.getClaimByToken(token).getSubject());
        User user = userService.getById(userId);
        user.setPassword(password);
        userService.saveOrUpdate(user);
        return Result.succ("update successfully");
    }

    @GetMapping("list")
    public Result fetchList(String username){
        List<User> userInfo;
        userInfo = userService.list(new QueryWrapper<User>().like("username", username));
        int total = userInfo.size();
        return Result.succ(MapUtil.builder()
                .put("total", total)
                .put("items", userInfo)
                .map());
    }

    @PostMapping("delete")
    public Result deleteDocument(@RequestBody User user){
        userService.removeById(user.getId());
        groupMemberService.remove(new QueryWrapper<GroupMember>().eq("memberId", user.getId()).or().eq("supervisorId", user.getId()));
        groupService.remove(new QueryWrapper<Group>().eq("creator", user.getUsername()));
        return Result.succ(user);
    }

    @PostMapping("create")
    public Result createGroup(@RequestBody User user){
        int count = userService.count(new QueryWrapper<User>().eq("username", user.getUsername()));
        if (count > 0) {
            return Result.fail("The user has existed");
        } else {
            userService.save(user);
            return Result.succ(user);
        }
    }

    @PostMapping("update")
    public Result updateDocument(@RequestBody User user){
        userService.updateById(user);
        return Result.succ(user);
    }
}

