package com.zoctan.api.core.response;

import org.springframework.http.HttpStatus;

/**
 * 响应结果生成工具
 *
 * @author Zoctan
 * @date 2018/06/09
 */
public class ResultGenerator {
  /**
   * 成功响应结果
   *
   * @param data 内容
   * @return 响应结果
   */
  public static <T> Result<T> genOkResult(final T data) {
    return new Result<T>().setCode(HttpStatus.OK.value()).setData(data);
  }

  /**
   * 成功响应结果
   *
   * @return 响应结果
   */
  public static <T> Result<T> genOkResult() {
    return genOkResult(null);
  }

  /**
   * 失败响应结果
   *
   * @param code 状态码
   * @param message 消息
   * @return 响应结果
   */
  public static <T> Result<T> genFailedResult(final int code, final String message) {
    return new Result<T>().setCode(code).setMessage(message);
  }

  /**
   * 失败响应结果
   *
   * @param resultCode 状态码枚举
   * @param message 消息
   * @return 响应结果
   */
  public static <T> Result<T> genFailedResult(final ResultCode resultCode, final String message) {
    return genFailedResult(resultCode.getValue(), message);
  }

  /**
   * 失败响应结果
   *
   * @param resultCode 状态码枚举
   * @return 响应结果
   */
  public static <T> Result<T> genFailedResult(final ResultCode resultCode) {
    return genFailedResult(resultCode.getValue(), resultCode.getReason());
  }

  /**
   * 失败响应结果
   *
   * @param message 消息
   * @return 响应结果
   */
  public static <T> Result<T> genFailedResult(final String message) {
    return genFailedResult(ResultCode.SUCCEED_REQUEST_FAILED_RESULT.getValue(), message);
  }

  /**
   * 失败响应结果
   *
   * @return 响应结果
   */
  public static <T> Result<T> genFailedResult() {
    return genFailedResult(ResultCode.SUCCEED_REQUEST_FAILED_RESULT);
  }
}
