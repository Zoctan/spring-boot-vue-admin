package com.zoctan.api.core.exception;

/**
 * 资源没找到异常（更新和删除都需先确认存在才操作）
 *
 * @author Zoctan
 * @date 2018/07/20
 */
public class ResourcesNotFoundException extends RuntimeException {
  private static final String DEFAULT_MESSAGE = "资源不存在";

  public ResourcesNotFoundException() {
    super(DEFAULT_MESSAGE);
  }

  public ResourcesNotFoundException(final String message) {
    super(message);
  }

  public ResourcesNotFoundException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
