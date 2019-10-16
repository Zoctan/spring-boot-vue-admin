package com.zoctan.api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.mapper.RolePermissionMapper;
import com.zoctan.api.entity.RolePermission;
import com.zoctan.api.service.RolePermissionService;

import javax.annotation.Resource;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RolePermissionServiceImpl extends AbstractService<RolePermission>
    implements RolePermissionService {
  @Resource private RolePermissionMapper rolePermissionMapper;
}
