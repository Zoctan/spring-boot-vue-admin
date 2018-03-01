package com.zoctan.api.service.impl;

import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.mapper.PermissionMapper;
import com.zoctan.api.model.Permission;
import com.zoctan.api.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Zoctan on 2018/02/17.
 */
@Service
@Transactional
public class PermissionServiceImpl extends AbstractService<Permission> implements PermissionService {
    @Resource
    private PermissionMapper authMapper;

}
