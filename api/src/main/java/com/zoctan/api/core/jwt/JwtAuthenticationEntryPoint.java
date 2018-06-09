package com.zoctan.api.core.jwt;

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
 * Json web token 入口点
 *
 * @author Zoctan
 * @date 2018/06/09
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    /**
     * 当访问的资源没有权限时被调用
     */
    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("Content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.getWriter().print(ResultGenerator.genUnauthorizedResult().toString());
        response.getWriter().close();
    }
}
