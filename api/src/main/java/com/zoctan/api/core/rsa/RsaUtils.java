package com.zoctan.api.core.rsa;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 工具
 *
 * <p>用openssl生成512位RSA：
 *
 * <p>生成私钥： openssl genrsa -out key.pem 512
 *
 * <p>从私钥中导出公钥： openssl rsa -in key.pem -pubout -out public-key.pem
 *
 * <p>公钥加密： openssl rsautl -encrypt -in xx.file -inkey public-key.pem -pubin -out xx.en
 *
 * <p>私钥解密： openssl rsautl -decrypt -in xx.en -inkey key.pem -out xx.de
 *
 * <p>pkcs8编码（Java）： openssl pkcs8 -topk8 -inform PEM -in key.pem -outform PEM -out private-key.pem
 * -nocrypt
 *
 * <p>最后将公私玥放在/resources/rsa/：private-key.pem public-key.pem
 *
 * @author Zoctan
 * @date 2018/07/20
 */
@Slf4j
@Component
public class RsaUtils {
  @Resource private RsaConfigurationProperties rsaProperties;
  private static final String ALGORITHM = "RSA";
  private PrivateKey privateKey;
  private PublicKey publicKey;

  public RsaUtils() {
    if (this.rsaProperties == null) {
      this.rsaProperties = new RsaConfigurationProperties();
    }
  }

  /**
   * 生成密钥对
   *
   * @param keyLength 密钥长度(最少512位)
   * @return 密钥对 公钥 keyPair.getPublic() 私钥 keyPair.getPrivate()
   * @throws Exception e
   */
  public KeyPair genKeyPair(final int keyLength) throws Exception {
    final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
    keyPairGenerator.initialize(keyLength);
    return keyPairGenerator.generateKeyPair();
  }

  /**
   * 公钥加密
   *
   * @param content 待加密数据
   * @param publicKey 公钥
   * @return 加密内容
   * @throws Exception e
   */
  public byte[] encrypt(final byte[] content, final PublicKey publicKey) throws Exception {
    final Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    return cipher.doFinal(content);
  }

  /**
   * 公钥加密
   *
   * @param content 待加密数据
   * @return 加密内容
   * @throws Exception e
   */
  public byte[] encrypt(final byte[] content) throws Exception {
    return this.encrypt(content, this.publicKey != null ? this.publicKey : this.loadPublicKey());
  }

  /**
   * 私钥解密
   *
   * @param content 加密数据
   * @param privateKey 私钥
   * @return 解密内容
   * @throws Exception e
   */
  public byte[] decrypt(final byte[] content, final PrivateKey privateKey) throws Exception {
    final Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    return cipher.doFinal(content);
  }

  /**
   * 私钥解密
   *
   * @param content 加密数据
   * @return 解密内容
   * @throws Exception e
   */
  public byte[] decrypt(final byte[] content) throws Exception {
    return this.decrypt(content, this.privateKey != null ? this.privateKey : this.loadPrivateKey());
  }

  /**
   * 加载pem格式的公钥
   *
   * @param decoded 二进制公钥
   * @return 公钥
   */
  public PublicKey loadPublicKey(final byte[] decoded) {
    try {
      final X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
      final KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
      this.publicKey = keyFactory.generatePublic(spec);
      return this.publicKey;
    } catch (final Exception e) {
      log.error("==> RSA Utils Exception: {}", e.getMessage());
      return null;
    }
  }

  /**
   * 加载配置文件中设置的公钥
   *
   * @return 公钥
   */
  public PublicKey loadPublicKey() {
    try {
      byte[] decoded;
      if (this.rsaProperties.isUseFile()) {
        decoded =
            this.replaceAndBase64Decode(
                this.rsaProperties.getPublicKeyPath(),
                this.rsaProperties.getPublicKeyHead(),
                this.rsaProperties.getPublicKeyTail());
      } else {
        decoded = Base64.decodeBase64(this.rsaProperties.getPublicKey());
      }
      return this.loadPublicKey(decoded);
    } catch (final Exception e) {
      log.error("==> RSA Utils Exception: {}", e.getMessage());
      return null;
    }
  }

  /**
   * 加载pem格式PKCS8编码的私钥
   *
   * @param decoded 二进制私钥
   * @return 私钥
   */
  public PrivateKey loadPrivateKey(final byte[] decoded) {
    try {
      final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
      final KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
      this.privateKey = keyFactory.generatePrivate(spec);
      return this.privateKey;
    } catch (final Exception e) {
      log.error("==> RSA Utils Exception: {}", e.getMessage());
      return null;
    }
  }

  /**
   * 加载配置文件中设置的私钥
   *
   * @return 私钥
   */
  public PrivateKey loadPrivateKey() {
    try {
      byte[] decoded;
      if (this.rsaProperties.isUseFile()) {
        decoded =
            this.replaceAndBase64Decode(
                this.rsaProperties.getPrivateKeyPath(),
                this.rsaProperties.getPrivateKeyHead(),
                this.rsaProperties.getPrivateKeyTail());
      } else {
        decoded = Base64.decodeBase64(this.rsaProperties.getPrivateKey());
      }
      return this.loadPrivateKey(decoded);
    } catch (final Exception e) {
      log.error("==> RSA Utils Exception: {}", e.getMessage());
      return null;
    }
  }

  /**
   * 加载文件后替换头和尾并解密
   *
   * @return 文件字节
   */
  private byte[] replaceAndBase64Decode(
      final String filePath, final String headReplace, final String tailReplace) throws Exception {
    // 从 classpath:resources/ 中加载资源
    final ClassPathResource resource = new ClassPathResource(filePath);
    if (!resource.exists()) {
      throw new Exception("公私钥文件找不到");
    }
    final byte[] keyBytes = new byte[(int) resource.getFile().length()];
    final FileInputStream in = new FileInputStream(resource.getFile());
    in.read(keyBytes);
    in.close();

    final String keyPEM =
        new String(keyBytes).replace(headReplace, "").trim().replace(tailReplace, "").trim();

    return Base64.decodeBase64(keyPEM);
  }
}
