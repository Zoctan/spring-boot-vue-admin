package com.zoctan.api.service;

import com.alibaba.fastjson.JSONObject;
import com.zoctan.api.core.service.Service;
import com.zoctan.api.model.Permission;

import java.util.List;

/**
 * @author Zoctan
 * @date 2018/05/17
 */
public interface PermissionService extends Service<Permission> {
    /**
     * 找到所有权限可控资源
     *
     * @return Json对象列表
     */
    List<JSONObject> findAllResourcePermission();
}
