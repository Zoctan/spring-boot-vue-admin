package com.zoctan.api.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "user_role")
public class UserRole {
    /**
     * 用户Id
     */
    @Id
    @Column(name = "user_id")
    private Long userId;

    /**
     * 角色Id
     */
    @Column(name = "role_id")
    private Long roleId;

    public UserRole setRoleId(final Long roleId) {
        this.roleId = roleId;
        return this;
    }

    public UserRole setUserId(final Long userId) {
        this.userId = userId;
        return this;
    }
}