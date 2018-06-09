package com.zoctan.api.mapper;

import com.alibaba.fastjson.JSONObject;
import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.model.Permission;

import java.util.List;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
public interface PermissionMapper extends MyMapper<Permission> {
    List<JSONObject> findAllResourcePermission();

    List<String> findAllCode();
}