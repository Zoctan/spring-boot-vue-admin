package com.zoctan.api.core.jwt;

import com.zoctan.api.core.rsa.RsaUtils;
import com.zoctan.api.util.RedisUtils;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Json web token 工具 验证、生成 token
 *
 * @author Zoctan
 * @date 2018/05/27
 */
@Slf4j
@Component
public class JwtUtil {
  @Resource private JwtConfigurationProperties jwtProperties;
  @Resource private RedisUtils redisUtils;
  @Resource private RsaUtils rsaUtils;

  private Claims getClaims(final String token) {
    final Jws<Claims> jws = this.parseToken(token);
    return jws == null ? null : jws.getBody();
  }

  /** 根据 token 得到账户名 */
  public String getName(final String token) {
    final Claims claims = this.getClaims(token);
    return claims == null ? null : claims.getSubject();
  }

  /**
   * 签发 token
   *
   * @param name 账户名
   * @param grantedAuthorities 账户权限信息[ADMIN, TEST, ...]
   * @param isAdmin 是否为管理后台
   */
  public String sign(
      final String name,
      final Collection<? extends GrantedAuthority> grantedAuthorities,
      final boolean isAdmin) {
    // 函数式创建 token，避免重复书写
    final Supplier<String> createToken = () -> this.createToken(name, grantedAuthorities, isAdmin);
    // 看看缓存有没有账户 token
    final String token = (String) this.redisUtils.getValue(name);
    // 没有登录过
    if (StringUtils.isBlank(token)) {
      return createToken.get();
    }
    final boolean isValidate = (boolean) this.redisUtils.getValue(token);
    // token 仍有效
    if (isValidate) {
      // 删除，重新签发
      this.redisUtils.delete(name);
      this.redisUtils.delete(token);
      return createToken.get();
    }
    return createToken.get();
  }

  /**
   * 清除账户在 Redis 中缓存的 token
   *
   * @param name 账户名
   */
  public void invalidRedisToken(final String name) {
    // 将 token 设置为无效
    final String token = (String) this.redisUtils.getValue(name);
    Optional.ofNullable(token).ifPresent(_token -> this.redisUtils.setValue(_token, false));
  }

  /** 从请求头或请求参数中获取 token */
  public String getTokenFromRequest(final HttpServletRequest httpRequest) {
    final String header = this.jwtProperties.getHeader();
    final String token = httpRequest.getHeader(header);
    return StringUtils.isNotBlank(token) ? token : httpRequest.getParameter(header);
  }

  /** 返回账户认证 */
  public UsernamePasswordAuthenticationToken getAuthentication(
      final String name, final String token) {
    // 解析 token 的 payload
    final Claims claims = this.getClaims(token);
    // 因为 JwtAuthenticationFilter 拦截器已经检查过 token 有效，所以可以忽略 get 空指针提示
    assert claims != null;
    final String claimKeyAuth = this.jwtProperties.getClaimKeyAuth();
    // 账户角色列表
    final List<String> authList = Arrays.asList(claims.get(claimKeyAuth).toString().split(","));
    // 将元素转换为 GrantedAuthority 接口集合
    final Collection<? extends GrantedAuthority> authorities =
        authList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    final User user = new User(name, "", authorities);
    return new UsernamePasswordAuthenticationToken(user, null, authorities);
  }

  /** 验证 token 是否正确 */
  public boolean validateToken(final String token) {
    boolean isValidate = true;
    final Object redisTokenValidate = this.redisUtils.getValue(token);
    // 可能 redis 部署出现了问题
    // 或者清空了缓存导致 token 键不存在
    if (redisTokenValidate != null) {
      isValidate = (boolean) redisTokenValidate;
    }
    // 能正确解析 token，并且 redis 中缓存的 token 也是有效的
    return this.parseToken(token) != null && isValidate;
  }

  /** 生成 token */
  private String createToken(
      final String name,
      final Collection<? extends GrantedAuthority> grantedAuthorities,
      final boolean isAdmin) {
    // 获取账户的角色字符串，如 USER,ADMIN
    final String authorities =
        grantedAuthorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
    JwtUtil.log.debug("==> Account<{}> authorities: {}", name, authorities);

    // 过期时间
    final Duration expireTime;
    if (isAdmin) {
      expireTime = this.jwtProperties.getAdminExpireTime();
    } else {
      expireTime = this.jwtProperties.getWechatExpireTime();
    }
    // 当前时间 + 有效时长
    final Date expireDate = new Date(System.currentTimeMillis() + expireTime.toMillis());
    // 创建 token，比如 "Bearer abc1234"
    final String token =
        this.jwtProperties.getTokenType()
            + " "
            + Jwts.builder()
                // 设置账户名
                .setSubject(name)
                // 添加权限属性
                .claim(this.jwtProperties.getClaimKeyAuth(), authorities)
                // 设置失效时间
                .setExpiration(expireDate)
                // 私钥加密生成签名
                .signWith(SignatureAlgorithm.RS256, this.rsaUtils.loadPrivateKey())
                // 使用LZ77算法与哈夫曼编码结合的压缩算法进行压缩
                .compressWith(CompressionCodecs.DEFLATE)
                .compact();
    // 保存账户 token
    // 因为账户注销后 JWT 本身只要没过期就仍然有效，所以只能通过 redis 缓存来校验有无效
    // 校验时只要 redis 中的 token 无效即可（JWT 本身可以校验有无过期，而 redis 过期即被删除了）
    // true 有效
    this.redisUtils.setValue(token, true, expireTime);
    // redis 过期时间和 JWT 的一致
    this.redisUtils.setValue(name, token, expireTime);
    JwtUtil.log.debug("==> Redis set Account<{}> token: {}", name, token);
    return token;
  }

  /** 解析 token */
  private Jws<Claims> parseToken(final String token) {
    try {
      return Jwts.parser()
          // 公钥解密
          .setSigningKey(this.rsaUtils.loadPublicKey())
          .parseClaimsJws(token.replace(this.jwtProperties.getTokenType(), ""));
    } catch (final SignatureException e) {
      // 签名异常
      JwtUtil.log.debug("Invalid JWT signature");
    } catch (final MalformedJwtException e) {
      // 格式错误
      JwtUtil.log.debug("Invalid JWT token");
    } catch (final ExpiredJwtException e) {
      // 过期
      JwtUtil.log.debug("Expired JWT token");
    } catch (final UnsupportedJwtException e) {
      // 不支持该JWT
      JwtUtil.log.debug("Unsupported JWT token");
    } catch (final IllegalArgumentException e) {
      // 参数错误异常
      JwtUtil.log.debug("JWT token compact of handler are invalid");
    }
    return null;
  }
}
