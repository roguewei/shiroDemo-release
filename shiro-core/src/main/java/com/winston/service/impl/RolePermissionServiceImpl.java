package com.winston.service.impl;

import com.winston.entity.RolePermissionExample;
import com.winston.entity.RolePermissionKey;
import com.winston.mapper.RolePermissionMapper;
import com.winston.service.IRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Winston
 * @title: RolePermissionServiceImpl
 * @projectName shiroDemo
 * @description:
 * @date 2019/7/24 14:32
 */
@Service
public class RolePermissionServiceImpl implements IRolePermissionService {

    @Autowired
    private RolePermissionMapper mapper;

    @Override
    public List<RolePermissionKey> queryByRoleIds(List<Integer> roleIds) {
        RolePermissionExample example = new RolePermissionExample();
        example.createCriteria().andRoleIdIn(roleIds);
        List<RolePermissionKey> rolePermissionKeys = mapper.selectByExample(example);
        return rolePermissionKeys;
    }
}
