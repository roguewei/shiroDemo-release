package com.winston.service;

import com.winston.entity.UserRoleKey;

import java.util.List;

public interface IUserRoleService {

    List<UserRoleKey> queryByUserId(int userId);

}
