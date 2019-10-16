package com.zoctan.api.filter;

import com.zoctan.api.core.jwt.JwtUtil;
import com.zoctan.api.util.IpUtils;
import com.zoctan.api.util.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 身份认证过滤器
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@Slf4j
@Component
public class AuthenticationFilter implements Filter {
  @Resource private JwtUtil jwtUtil;

  @Override
  public void init(final FilterConfig filterConfig) {
    AuthenticationFilter.log.debug("==> AuthenticationFilter init");
  }

  @Override
  public void doFilter(
      final ServletRequest servletRequest,
      final ServletResponse servletResponse,
      final FilterChain filterChain)
      throws IOException, ServletException {
    final HttpServletRequest request = (HttpServletRequest) servletRequest;
    final HttpServletResponse response = (HttpServletResponse) servletResponse;

    AuthenticationFilter.log.debug(
        "==> IP<{}> Request: [{}] {}",
        IpUtils.getIpAddress(),
        request.getMethod(),
        UrlUtils.getMappingUrl(request));

    // 设置允许多个域名请求
    String[] allowDomains = {"http://localhost:9999", "http://localhost:8080"};
    Set<String> allowOrigins = new HashSet<>(Arrays.asList(allowDomains));
    String origin = request.getHeader("Origin");
    if (allowOrigins.contains(origin)) {
      // 设置允许跨域的配置
      response.setHeader("Access-Control-Allow-Origin", origin);
    }
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader(
        "Access-Control-Allow-Headers", "Content-Type, Content-Length, Authorization");
    // 明确允许通过的方法，不建议使用 *
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, OPTIONS");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Expose-Headers", "*");

    // 预请求后，直接返回
    // 返回码必须为 200 否则视为请求失败
    if (HttpMethod.OPTIONS.matches(request.getMethod())) {
      return;
    }

    final String token = this.jwtUtil.getTokenFromRequest(request);
    if (!StringUtils.isEmpty(token)) {
      final String name = this.jwtUtil.getName(token);
      AuthenticationFilter.log.debug("==> Account<{}> token: {}", name, token);

      if (name != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        if (this.jwtUtil.validateToken(token)) {
          final UsernamePasswordAuthenticationToken authentication =
              this.jwtUtil.getAuthentication(name, token);

          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          // 向 security 上下文中注入已认证的账户
          // 之后可以直接在控制器 controller 的入参获得 Principal 或 Authentication
          SecurityContextHolder.getContext().setAuthentication(authentication);
          AuthenticationFilter.log.debug(
              "==> Account<{}> is authorized, set security context", name);
        }
      }
    } else {
      AuthenticationFilter.log.debug(
          "==> IP<{}> Without token, Request: [{}] {}",
          IpUtils.getIpAddress(),
          request.getMethod(),
          UrlUtils.getMappingUrl(request));
    }
    filterChain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    AuthenticationFilter.log.debug("==> AuthenticationFilter destroy");
  }
}
