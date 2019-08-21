package com.winston.service.impl;

import com.winston.entity.UserRoleExample;
import com.winston.entity.UserRoleKey;
import com.winston.mapper.UserRoleMapper;
import com.winston.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Winston
 * @title: UserRoleServiceImpl
 * @projectName shiroDemo
 * @description:
 * @date 2019/7/24 14:33
 */
@Service
public class UserRoleServiceImpl implements IUserRoleService {

    @Autowired
    private UserRoleMapper mapper;

    @Override
    public List<UserRoleKey> queryByUserId(int userId) {
        UserRoleExample example = new UserRoleExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<UserRoleKey> userRoleKeys = mapper.selectByExample(example);
        return userRoleKeys;
    }
}
