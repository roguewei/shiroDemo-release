package com.winston.service.impl;

import com.winston.entity.*;
import com.winston.mapper.PermissionMapper;
import com.winston.service.IPermissionService;
import com.winston.service.IRolePermissionService;
import com.winston.service.IUserRoleService;
import com.winston.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Winston
 * @title: PermissionServiceImpl
 * @projectName shiroDemo
 * @description:
 * @date 2019/7/24 14:32
 */
@Service
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private PermissionMapper mapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRolePermissionService rolePermissionService;

    @Override
    public List<Permission> queryAll() {
        return mapper.selectByExample(new PermissionExample());
    }

    @Override
    public List<Permission> queryByUserName(String username) {
        User query = new User();
        query.setUsername(username);
        User user = userService.queryByUser(query);
        if(user != null){
            List<UserRoleKey> userRoleKeys = userRoleService.queryByUserId(user.getId());
            if(userRoleKeys != null && userRoleKeys.size() > 0){
                List<Integer> roleIds = new ArrayList<>();
                for(UserRoleKey userRole : userRoleKeys){
                    roleIds.add(Integer.valueOf(userRole.getRoleId()));
                }
                List<RolePermissionKey> rolePermissionKeys = rolePermissionService.queryByRoleIds(roleIds);
                if(rolePermissionKeys != null && rolePermissionKeys.size() > 0){
                    List<Integer> perIds = new ArrayList<>();
                    for(RolePermissionKey rolePer : rolePermissionKeys){
                        perIds.add(Integer.valueOf(rolePer.getPerId()));
                    }
                    PermissionExample example = new PermissionExample();
                    example.createCriteria().andIdIn(perIds);
                    List<Permission> permissionList = mapper.selectByExample(example);
                    return permissionList;
                }
            }
        }
        return null;
    }

    @Override
    public void addAllUrl(Permission permission) {
        mapper.insert(permission);
    }
}
