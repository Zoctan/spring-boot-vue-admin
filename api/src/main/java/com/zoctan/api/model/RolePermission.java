package com.zoctan.api.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
@Table(name = "role_permission")
public class RolePermission {
    /**
     * 角色Id
     */
    @Id
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 权限Id
     */
    @Column(name = "permission_id")
    private Long permissionId;

    public Long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(final Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return this.permissionId;
    }

    public void setPermissionId(final Long permissionId) {
        this.permissionId = permissionId;
    }
}