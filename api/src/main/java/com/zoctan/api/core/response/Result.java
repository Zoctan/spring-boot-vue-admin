package com.zoctan.api.core.response;

import com.alibaba.fastjson.JSON;

/**
 * 统一API响应结果封装
 *
 * @author Zoctan
 * @date 2018/06/09
 */
public class Result {
    /**
     * 状态码
     */
    private final Integer code;
    /**
     * 消息
     */
    private final String msg;
    /**
     * 数据内容，比如列表，实体
     */
    private final Object data;

    private Result(final Builder builder) {
        this.code = builder.code;
        this.msg = builder.msg;
        this.data = builder.data;
    }

    public static class Builder {
        private final Integer code;
        private String msg;
        private Object data;

        public Builder(final Integer code) {
            this.code = code;
        }

        public Builder msg(final String msg) {
            this.msg = msg;
            return this;
        }

        public Builder data(final Object data) {
            this.data = data;
            return this;
        }

        public Result build() {
            return new Result(this);
        }
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public Object getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
