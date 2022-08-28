package com.lyz.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyz.common.dto.LoginDto;
import com.lyz.common.lang.Result;
import com.lyz.entity.User;
import com.lyz.service.UserService;
import com.lyz.shiro.JwtToken;
import com.lyz.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;

@RestController
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response){

        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        Assert.notNull(user, "Account is not exist");

        if(!user.getPassword().equals((loginDto.getPassword()))){ // 密码没有加密
            return Result.fail("The password is incorrect");
        }
        String jwt = jwtUtils.generateToken(user.getId());

        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");

        return Result.succ(MapUtil.builder().put("token", jwt).map());
    }

    @GetMapping("/info")
    public Result getInfo (@Validated String token){
        User user = userService.getById(jwtUtils.getClaimByToken(token).getSubject());
        ArrayList<String> roleArray = new ArrayList<>();
        roleArray.add(user.getRole());
        return Result.succ(MapUtil.builder()
                .put("roles", roleArray)
                .put("introduction", "I am a super administrator")
                .put("avatar", user.getAvatar())
                .put("name", user.getUsername())
                .put("id", user.getId())
                .map()
        );
    }

//    @RequiresAuthentication
    @PostMapping("/logout")
    public Result logout(){
//        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }

}
