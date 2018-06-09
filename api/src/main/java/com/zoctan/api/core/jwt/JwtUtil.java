package com.zoctan.api.core.jwt;

import com.zoctan.api.util.RSAUtil;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Json web token 工具
 * 验证、生成token
 *
 * @author Zoctan
 * @date 2018/06/09
 */
@Component
public class JwtUtil {
    private final static Logger log = LoggerFactory.getLogger(JwtUtil.class);
    @Resource
    private RSAUtil rsaUtil;
    @Resource
    private JWTSetting jwtSetting;

    private Claims getClaims(final String token) {
        final Jws<Claims> jws = this.parseToken(token);
        return jws == null ? null : jws.getBody();
    }

    /**
     * 根据token得到用户名
     */
    public String getUsername(final String token) {
        final Claims claims = this.getClaims(token);
        return claims == null ? null : claims.getSubject();
    }


    /**
     * 从请求头或请求参数中获取token
     */
    public String getTokenFromRequest(final HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader(this.jwtSetting.getHeader());
        if (StringUtils.isEmpty(token)) {
            token = httpRequest.getParameter(this.jwtSetting.getHeader());
        }
        return token;
    }

    /**
     * 返回用户认证
     *
     * @param username 用户名
     * @param token    token
     * @return UsernamePasswordAuthenticationToken
     */
    public UsernamePasswordAuthenticationToken getAuthentication(final String username, final String token) {
        // 解析 token 的 payload
        final Claims claims = this.getClaims(token);

        // 获取用户角色字符串
        // 将元素转换为 GrantedAuthority 接口集合
        //noinspection ConstantConditions
        final Collection<? extends GrantedAuthority> authorities =
                // 因为 JwtAuthenticationFilter 拦截器已经检查过 token 有效，所以可以忽略 get 空指针提示
                Arrays.stream(claims.get(this.jwtSetting.getAuthoritiesKey()).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(
                new User(username, "", authorities),
                null,
                authorities);
    }

    /**
     * 签发token
     *
     * @param username           用户名
     * @param grantedAuthorities 用户权限信息[ROLE_ADMIN, xx, ...]
     * @return token
     */
    public String sign(final String username, final Collection<? extends GrantedAuthority> grantedAuthorities) {
        // 获取用户的权限字符串，如 user:delete, role:add
        final String authorities = grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        log.info("User<{}> : authorities => {}", username, authorities);

        final Date date = new Date(System.currentTimeMillis() + this.jwtSetting.getExpirationTime() * 1000);
        // 加载私钥
        final PrivateKey privateKey = this.rsaUtil.loadPemPrivateKey(this.jwtSetting.getPrivateKey());
        // 创建 token
        return this.jwtSetting.getTokenPrefix() + " " +
                Jwts.builder()
                        // 设置用户名
                        .setSubject(username)
                        // 添加权限属性
                        .claim(this.jwtSetting.getAuthoritiesKey(), authorities)
                        // 设置失效时间
                        .setExpiration(date)
                        // 512位的私钥加密生成签名
                        .signWith(SignatureAlgorithm.RS256, privateKey)
                        .compact();
    }

    /**
     * 解析token
     */
    private Jws<Claims> parseToken(final String token) {
        try {
            // 加载公钥
            final PublicKey publicKey = this.rsaUtil.loadPemPublicKey(this.jwtSetting.getPublicKey());
            return Jwts
                    .parser()
                    // 公钥解密
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token.replace(this.jwtSetting.getTokenPrefix(), ""));
        } catch (final SignatureException e) {
            // 签名异常
            log.error("Invalid JWT signature");
        } catch (final MalformedJwtException e) {
            // JWT 格式错误
            log.error("Invalid JWT token");
        } catch (final ExpiredJwtException e) {
            // JWT 过期
            log.error("Expired JWT token");
        } catch (final UnsupportedJwtException e) {
            // 不支持该 JWT
            log.error("Unsupported JWT token");
        } catch (final IllegalArgumentException e) {
            // 参数错误异常
            log.error("JWT token compact of handler are invalid");
        }
        return null;
    }
}