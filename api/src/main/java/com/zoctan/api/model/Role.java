package com.zoctan.api.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

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
}