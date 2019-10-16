package com.zoctan.api.filter;

import com.zoctan.api.core.response.ResultCode;
import com.zoctan.api.core.response.ResultGenerator;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * 认证入口点 因为 RESTFul 没有登录界面所以只显示未登录提示
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
  @Override
  public void commence(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final AuthenticationException authException)
      throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setHeader("Content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
    response
        .getWriter()
        .println(ResultGenerator.genFailedResult(ResultCode.UNAUTHORIZED_EXCEPTION).toString());
    response.getWriter().close();
  }
}
