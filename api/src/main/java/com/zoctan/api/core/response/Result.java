package com.zoctan.api.core.response;

import com.alibaba.fastjson.JSON;

/**
 * 统一API响应结果封装
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Result {
    private int status;
    private String message;
    private Object data;

    public Result setStatus(final int status) {
        this.status = status;
        return this;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public Result setMessage(final String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return this.data;
    }

    public Result setData(final Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
