package com.winston.controller;

import com.winston.entity.User;
import com.winston.service.IUserService;
import com.winston.utils.result.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Winston
 * @title: UserController
 * @projectName shiroDemo
 * @description:
 * @date 2019/7/24 15:53
 */
@RestController
@RequestMapping("/app/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequiresPermissions("/app/users")
    @GetMapping
    public Result query(){
        List<User> users = userService.queryAll();
        return Result.success(users);
    }

    @RequiresPermissions("/app/users/queryByUser")
    @GetMapping("/queryByUser")
    public Result queryByUser(User user){
        User user1 = userService.queryByUser(user);
        return Result.success(user1);
    }


}
