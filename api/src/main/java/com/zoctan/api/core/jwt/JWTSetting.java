package com.zoctan.api.core.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Json web token配置
 *
 * @author Zoctan
 * @date 2018/06/09
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTSetting {
    private String authoritiesKey;
    /**
     * RSA 私钥
     */
    private String privateKey;
    /**
     * RSA 公钥
     */
    private String publicKey;
    /**
     * token 前缀
     */
    private String tokenPrefix;
    /**
     * 存放 token 的 Header Key
     */
    private String header;
    /**
     * 有效期
     */
    private long expirationTime;

    public String getAuthoritiesKey() {
        return this.authoritiesKey;
    }

    public void setAuthoritiesKey(final String authoritiesKey) {
        this.authoritiesKey = authoritiesKey;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(final String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(final String publicKey) {
        this.publicKey = publicKey;
    }

    public String getTokenPrefix() {
        return this.tokenPrefix;
    }

    public void setTokenPrefix(final String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public String getHeader() {
        return this.header;
    }

    public void setHeader(final String header) {
        this.header = header;
    }

    public long getExpirationTime() {
        return this.expirationTime;
    }

    public void setExpirationTime(final long expirationTime) {
        this.expirationTime = expirationTime;
    }
}
