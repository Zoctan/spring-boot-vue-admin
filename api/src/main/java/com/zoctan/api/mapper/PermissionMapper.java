package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.model.Permission;

import java.util.List;

public interface PermissionMapper extends MyMapper<Permission> {
    List<String> findAllCode();
}