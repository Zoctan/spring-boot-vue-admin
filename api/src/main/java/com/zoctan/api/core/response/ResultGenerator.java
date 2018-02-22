package com.zoctan.api.core.response;

import org.springframework.http.HttpStatus;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    private static final String DEFAULT_OK_MESSAGE = "OK";
    private static final String DEFAULT_UNAUTHORIZED_MESSAGE = "需要认证";
    private static final String DEFAULT_METHOD_NOT_ALLOWED_MESSAGE = "请求方式出错";

    public static Result genOkResult() {
        return new Result()
                .setStatus(HttpStatus.OK.value())
                .setMessage(DEFAULT_OK_MESSAGE);
    }

    public static Result genOkResult(final Object data) {
        return new Result()
                .setStatus(HttpStatus.OK.value())
                .setMessage(DEFAULT_OK_MESSAGE)
                .setData(data);
    }

    public static Result genFailedResult(final String message) {
        return new Result()
                .setStatus(HttpStatus.BAD_REQUEST.value())
                .setMessage(message);
    }

    public static Result genMethodErrorResult() {
        return new Result()
                .setStatus(HttpStatus.METHOD_NOT_ALLOWED.value())
                .setMessage(DEFAULT_METHOD_NOT_ALLOWED_MESSAGE);
    }

    public static Result genUnauthorizedResult() {
        return new Result()
                .setStatus(HttpStatus.UNAUTHORIZED.value())
                .setMessage(DEFAULT_UNAUTHORIZED_MESSAGE);
    }

    public static Result genUnauthorizedResult(final String message) {
        return new Result()
                .setStatus(HttpStatus.UNAUTHORIZED.value())
                .setMessage(message);
    }

    public static Result genInternalServerErrorResult(final String url) {
        return new Result()
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMessage("接口 [" + url + "] 内部出错");
    }
}
