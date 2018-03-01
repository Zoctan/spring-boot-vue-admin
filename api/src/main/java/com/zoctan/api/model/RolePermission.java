package com.zoctan.api.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
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
}