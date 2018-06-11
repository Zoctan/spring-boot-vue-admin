package com.zoctan.api.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
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
    @NotEmpty(message = "角色名不能为空")
    private String name;

    /* ---------- 以下字段来自联表查询 ------------*/

    /**
     * 角色对应的权限
     */
    @Transient
    private List<Resource> resourceList;

    /* ---------- 以下字段来自请求的Json ------------*/

    /**
     * 角色对应的权限Id
     */
    @JSONField(serialize = false)
    @Transient
    private List<Integer> permissionIdList;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<Resource> getResourceList() {
        return this.resourceList;
    }

    public void setResourceList(final List<Resource> resourceList) {
        this.resourceList = resourceList;
    }

    public List<Integer> getPermissionIdList() {
        return this.permissionIdList;
    }

    public void setPermissionIdList(final List<Integer> permissionIdList) {
        this.permissionIdList = permissionIdList;
    }
}