package com.zoctan.api;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 访问控制器时以某用户已登录状态操作
 *
 * @author Zoctan
 * @date 2018/11/29
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomSecurityContextFactory.class)
public @interface WithCustomUser {
  String name() default "admin";
}
