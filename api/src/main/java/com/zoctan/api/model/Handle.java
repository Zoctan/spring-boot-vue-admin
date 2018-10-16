package com.zoctan.api.model;

import javax.persistence.Transient;

/**
 * @author Zoctan
 * @date 2018/10/16
 */
public class Handle {
    /**
     * 对应权限id
     */
    @Transient
    private Long id;

    /**
     * 操作名称
     */
    @Transient
    private String handle;

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getHandle() {
        return this.handle;
    }

    public void setHandle(final String handle) {
        this.handle = handle;
    }
}