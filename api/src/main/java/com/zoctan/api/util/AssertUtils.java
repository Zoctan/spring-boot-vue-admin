package com.zoctan.api.util;

import com.zoctan.api.core.exception.ServiceException;
import com.zoctan.api.core.response.ResultCode;

/**
 * 断言工具
 *
 * @author Zoctan
 * @date 2018/11/29
 */
public class AssertUtils {
  public static void throwIf(
      final boolean statement, final ResultCode resultCode, final String message) {
    if (statement) {
      throw toThrow(resultCode, message);
    }
  }

  public static void throwIf(
      final boolean statement, final ResultCode resultCode, final Object... messages) {
    throwIf(statement, resultCode, resultCode.format(messages));
  }

  public static RuntimeException toThrow(final ResultCode resultCode, final Object... messages) {
    return new ServiceException(resultCode, resultCode.format(messages));
  }

  public static void asserts(
      final boolean statement, final ResultCode resultCode, final Object... messages) {
    throwIf(!statement, resultCode, messages);
  }
}
