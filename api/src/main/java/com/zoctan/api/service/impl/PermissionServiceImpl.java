package com.zoctan.api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zoctan.api.core.service.AbstractService;
import com.zoctan.api.mapper.PermissionMapper;
import com.zoctan.api.entity.Permission;
import com.zoctan.api.service.PermissionService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl extends AbstractService<Permission>
    implements PermissionService {
  @Resource private PermissionMapper permissionMapper;

  @Override
  public List<com.zoctan.api.entity.Resource> listResourceWithHandle() {
    return this.permissionMapper.listResourceWithHandle();
  }

  @Override
  public List<com.zoctan.api.entity.Resource> listRoleWithResourceByRoleId(Long roleId) {
    return this.permissionMapper.listRoleWithResourceByRoleId(roleId);
  }
}
