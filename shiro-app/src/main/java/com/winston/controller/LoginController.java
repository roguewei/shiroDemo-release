package com.winston.controller;

import com.winston.entity.User;
import com.winston.service.IUserService;
import com.winston.utils.result.CodeMsg;
import com.winston.utils.result.Result;
import com.winston.utils.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Winston
 * @title: LoginController
 * @projectName shiroDemo
 * @description:
 * @date 2019/7/24 16:24
 */
@RestController
public class LoginController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public Result login(){
        return Result.error(CodeMsg.IS_NOT_LOGIN);
    }

    @PostMapping("/login")
    public Result login(User user){
        User user1 = userService.queryByUser(user);
        String token = tokenService.getToken(user1);
        return Result.success(token);
    }

    @GetMapping("/unauthorized")
    public Result unauthorized(){
        return Result.success("未登录，请重新登录");
    }

}
