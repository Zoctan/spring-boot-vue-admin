package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.dto.RoleWithPermission;
import com.zoctan.api.model.Role;
import com.zoctan.api.model.RoleWithResource;

import java.util.List;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
public interface RoleService extends Service<Role> {

    /**
     * 新建角色
     *
     * @param role 带权限列表的角色
     */
    void save(RoleWithPermission role);

    /**
     * 更新角色
     *
     * @param role 带权限列表的角色
     */
    void update(RoleWithPermission role);

    /**
     * 获取所有角色以及对应的权限
     *
     * @return 角色可控资源列表
     */
    List<RoleWithResource> findRoleWithPermission();
}
