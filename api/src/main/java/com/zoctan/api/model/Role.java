package com.zoctan.api.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

@Data
public class Role {
    /**
     * 角色Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /* ---------- 以下字段来自联表查询 ------------*/

    /**
     * 角色对应的权限
     */
    @Transient
    private List<Permission> permissionList;
}