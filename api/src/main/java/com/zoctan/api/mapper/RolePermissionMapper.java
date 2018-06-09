package com.zoctan.api.mapper;

import com.zoctan.api.core.mapper.MyMapper;
import com.zoctan.api.model.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
public interface RolePermissionMapper extends MyMapper<RolePermission> {
    void saveRolePermission(@Param("roleId") Object roleId, @Param("permissionIdList") List<Integer> permissionIdList);
}