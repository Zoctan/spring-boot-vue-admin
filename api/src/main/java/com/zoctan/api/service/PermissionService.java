package com.zoctan.api.service;

import com.zoctan.api.core.service.Service;
import com.zoctan.api.entity.Permission;
import com.zoctan.api.entity.Resource;

import java.util.List;

/**
 * @author Zoctan
 * @date 2018/05/17
 */
public interface PermissionService extends Service<Permission> {
  /**
   * 找到所有权限可控资源
   *
   * @return 资源列表
   */
  List<Resource> listResourceWithHandle();

  /**
   * 找到角色权限可控资源
   *
   * @param roleId 角色id
   * @return 资源列表
   */
  List<Resource> listRoleWithResourceByRoleId(Long roleId);
}
