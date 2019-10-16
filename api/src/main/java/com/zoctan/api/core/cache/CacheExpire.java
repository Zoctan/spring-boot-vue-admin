package com.zoctan.api.core.cache;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 缓存过期注解
 *
 * @author Zoctan
 * @date 2018/07/11
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CacheExpire {
  /** 过期时间，默认 60s */
  @AliasFor("expire")
  long value() default 60L;

  /** 过期时间，默认 60s */
  @AliasFor("value")
  long expire() default 60L;
}
