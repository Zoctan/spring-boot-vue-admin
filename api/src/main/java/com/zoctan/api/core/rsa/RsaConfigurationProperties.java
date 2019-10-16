package com.zoctan.api.core.rsa;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * RSA 配置
 *
 * @author Zoctan
 * @date 2018/07/20
 */
@Data
@Component
@ConfigurationProperties(prefix = "rsa")
public class RsaConfigurationProperties {
  /** 私钥位置 */
  private String privateKeyPath;
  /** 公钥位置 */
  private String publicKeyPath;
  /** 使用文件还是直接使用字符串 */
  private boolean useFile;
  /** 私钥 */
  private String privateKey;
  /** 公钥 */
  private String publicKey;

  private String publicKeyHead = "-----BEGIN PUBLIC KEY-----";
  private String publicKeyTail = "-----END PUBLIC KEY-----";
  private String privateKeyHead = "-----BEGIN PRIVATE KEY-----";
  private String privateKeyTail = "-----END PRIVATE KEY-----";
}
