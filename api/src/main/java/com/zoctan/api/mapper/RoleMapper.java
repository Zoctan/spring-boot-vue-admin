package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.model.Resource;
import com.zoctan.api.model.Role;
import com.zoctan.api.model.RoleWithResource;

import java.util.List;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
public interface RoleMapper extends MyMapper<Role> {
    /**
     * 获取所有角色以及对应的权限
     *
     * @return 角色可控资源列表
     */
    List<RoleWithResource> findRoleWithPermission();

    /**
     * 获取所有角色以及对应的权限
     *
     * @return 角色可控资源列表
     */
    List<RoleWithResource> findRoles();
}