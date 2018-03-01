package com.zoctan.api.core.exception;

/**
 * 工具类异常
 */
public class UtilException extends RuntimeException {
    public UtilException(final String message) {
        super(message);
    }

    public UtilException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
