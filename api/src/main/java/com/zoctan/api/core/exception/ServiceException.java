package com.zoctan.api.core.exception;

import com.zoctan.api.core.response.ResultCode;

/**
 * Service 异常
 *
 * @author Zoctan
 * @date 2018/05/27
 */
public class ServiceException extends RuntimeException {
  private ResultCode resultCode;

  public ServiceException(final String message) {
    super(message);
  }

  public ServiceException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ServiceException(final ResultCode resultCode, final String message) {
    super(message);
    this.resultCode = resultCode;
  }

  public ServiceException(final ResultCode resultCode) {
    super(resultCode.getReason());
    this.resultCode = resultCode;
  }

  public ResultCode getResultCode() {
    return this.resultCode;
  }

  public void setResultCode(final ResultCode resultCode) {
    this.resultCode = resultCode;
  }
}
