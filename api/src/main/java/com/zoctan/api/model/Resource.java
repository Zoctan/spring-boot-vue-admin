package com.zoctan.api.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Transient;
import java.util.List;

/**
 * @author Zoctan
 * @date 2018/10/16
 */
public class Resource {
    @Transient
    private String resource;

    @Transient
    private List<Handle> handleList;

    public List<Handle> getHandleList() {
        return this.handleList;
    }

    public void setHandleList(final List<Handle> handleList) {
        this.handleList = handleList;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}