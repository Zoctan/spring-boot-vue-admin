package com.zoctan.api.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "role_auth")
public class RoleAuth {
    /**
     * 角色Id
     */
    @Id
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 权限Id
     */
    @Column(name = "auth_id")
    private Long authId;
}