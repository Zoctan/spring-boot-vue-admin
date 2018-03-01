package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.mapper.RolePermissionMapper;
import com.zoctan.api.model.RolePermission;
import com.zoctan.api.service.RolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Zoctan on 2018/02/17.
 */
@Service
@Transactional
public class RolePermissionServiceImpl extends AbstractService<RolePermission> implements RolePermissionService {
    @Resource
    private RolePermissionMapper rolePermissionMapper;

}
