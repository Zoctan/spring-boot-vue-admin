package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.model.Resource;
import com.zoctan.api.model.Role;

import java.util.List;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
public interface RoleMapper extends MyMapper<Role> {
    List<Resource> findAllRoleWithPermission();
}