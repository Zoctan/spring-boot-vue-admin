package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.mapper.PermissionMapper;
import com.zoctan.api.model.Permission;
import com.zoctan.api.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl extends AbstractService<Permission> implements PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<com.zoctan.api.model.Resource> findResourceWithHandle() {
        return this.permissionMapper.findResourceWithHandle();
    }

    @Override
    public List<com.zoctan.api.model.Resource> findRoleWithResourceByRoleId(Long roleId) {
        return this.permissionMapper.findRoleWithResourceByRoleId(roleId);
    }
}
