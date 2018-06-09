package com.zoctan.api.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
public class Permission {
    /**
     * 权限Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 权限对应的资源
     */
    private String resource;

    /**
     * 权限的代码/通配符,对应代码中@hasAuthority(xx)
     */
    private String code;

    /**
     * 对应的资源操作
     */
    private String handle;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getResource() {
        return this.resource;
    }

    public void setResource(final String resource) {
        this.resource = resource;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getHandle() {
        return this.handle;
    }

    public void setHandle(final String handle) {
        this.handle = handle;
    }
}