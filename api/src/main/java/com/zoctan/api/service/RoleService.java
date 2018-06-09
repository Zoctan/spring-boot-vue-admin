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
    List<Resource> findAllRoleWithPermission();

    @Override
    void save(final Role role);

    @Override
    void update(Role role);
}
