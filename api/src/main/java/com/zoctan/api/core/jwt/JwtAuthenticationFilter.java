package com.zoctan.api.core.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证请求的Token
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {

        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Content-Length, Authorization, Accept, X-Requested-With");
        // 明确允许通过的方法，不建议使用*
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Expose-Headers", "*");
        // web axios.js send
        if (request.getMethod().equals("OPTIONS")) {
            return;
        }

        final String token = this.jwtUtil.getTokenFromRequest(request);
        if (token != null) {
            final String username = this.jwtUtil.getUsername(token);
            log.info("JwtFilter => user<{}> token : {}", username, token);
            log.info("request URL<{}> Method<{}>", request.getRequestURL(), request.getMethod());

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (this.jwtUtil.validateToken(token)) {
                    final UsernamePasswordAuthenticationToken authentication = this.jwtUtil.getAuthentication(username, token);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("JwtFilter => user<{}> is authorized, set security context", username);
                }
            }
        } else {
            log.info("Anonymous request URL<{}> Method<{}>", request.getRequestURL(), request.getMethod());
        }
        filterChain.doFilter(request, response);
    }
}
