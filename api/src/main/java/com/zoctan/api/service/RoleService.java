package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.model.Resource;
import com.zoctan.api.model.Role;

import java.util.List;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
public interface RoleService extends Service<Role> {
    /**
     * 获取所有角色以及对应的权限
     *
     * @return 角色可控资源列表
     */
    List<Resource> findAllRoleWithPermission();
}
