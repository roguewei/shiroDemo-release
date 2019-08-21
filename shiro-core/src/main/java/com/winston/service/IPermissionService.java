package com.winston.service;

import com.winston.entity.Permission;

import java.util.List;

public interface IPermissionService {

    List<Permission> queryAll();

    List<Permission> queryByUserName(String username);

}
