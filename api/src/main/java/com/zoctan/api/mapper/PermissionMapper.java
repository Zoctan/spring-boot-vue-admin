package com.zoctan.api.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.entity.Permission;
import com.zoctan.api.entity.Resource;

import java.util.List;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
public interface PermissionMapper extends MyMapper<Permission> {
  /**
   * 找到所有权限可控资源
   *
   * @return 资源列表
   */
  List<Resource> listResourceWithHandle();

  /**
   * 找到所有权限可控资源
   *
   * @param roleId 角色id
   * @return 资源列表
   */
  List<Resource> listRoleWithResourceByRoleId(@Param("roleId") Long roleId);

  /**
   * 获取所有权限代码
   *
   * @return 代码列表
   */
  @Select("SELECT p.code FROM `permission` p")
  List<String> listAllCode();
}
