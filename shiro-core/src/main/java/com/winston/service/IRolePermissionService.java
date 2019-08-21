package com.winston.service;

import com.winston.entity.RolePermissionKey;

import java.util.List;

public interface IRolePermissionService {

    List<RolePermissionKey> queryByRoleIds(List<Integer> roleIds);

}
