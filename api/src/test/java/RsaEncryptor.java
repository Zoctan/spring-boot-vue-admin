import com.zoctan.api.core.rsa.RsaUtils;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * RSA工具测试
 *
 * @author Zoctan
 * @date 2018/05/27
 */
public class RsaEncryptor {
  private final RsaUtils rsaUtil = new RsaUtils();

  /** 加载公私钥pem格式文件测试 */
  @Test
  public void test1() throws Exception {
    final PublicKey publicKey =
        this.rsaUtil.loadPublicKey(
            Base64.decodeBase64(
                "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANl43G/i7U86D2pWh6UQ7whrddH9QDqTBoZjbySk3sIS2W/AoZnCwJAhYYfQtY6qZ4p9oWwH9OQC7Z/8S3W6M58CAwEAAQ=="));
    final PrivateKey privateKey =
        this.rsaUtil.loadPrivateKey(
            Base64.decodeBase64(
                "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEA2Xjcb+LtTzoPalaHpRDvCGt10f1AOpMGhmNvJKTewhLZb8ChmcLAkCFhh9C1jqpnin2hbAf05ALtn/xLdboznwIDAQABAkEAhc3iO4kxH9UGVRQmY352xARyOqCKWz/I/PjDEpXKZTdWs1oqTM2lpYBlpnV/fJ3Sf2es6Sv6reLCLP7MZP1KGQIhAP0+wZ0c1YBVJvkkHHmhnGyaU1Bsb1alPY4A2sM97Q0lAiEA29Z7rVhw4Fgx201uhnwb0dqiuXfCZM1+fVDyTSg0XHMCIBZencGgE2fjna6yNuWzldquAx/+hBM2Q2qwvqIybScVAiEAlFhnnNHRWZIqEpJtwtJ8819V71GhG+SPNoEpAGfg7YECIHPReHdJdfBehhD1o63xH+gTZqliK4n6XvBhkcyWNYdS"));
    Assert.assertNotNull(publicKey);
    Assert.assertNotNull(privateKey);
    System.out.println("公钥：" + publicKey);
    System.out.println("私钥：" + privateKey);

    final String data = "zoctan";
    // 公钥加密
    final byte[] encrypted = this.rsaUtil.encrypt(data.getBytes());
    System.out.println("加密后：" + Base64.encodeBase64String(encrypted));

    // 私钥解密
    final byte[] decrypted = this.rsaUtil.decrypt(encrypted);
    System.out.println("解密后：" + new String(decrypted));
  }

  /** 生成RSA密钥对并进行加解密测试 */
  @Test
  public void test2() throws Exception {
    final String data = "hello word";
    final KeyPair keyPair = this.rsaUtil.genKeyPair(512);

    // 获取公钥，并以base64格式打印出来
    final PublicKey publicKey = keyPair.getPublic();
    System.out.println("公钥：" + new String(Base64.encodeBase64(publicKey.getEncoded())));

    // 获取私钥，并以base64格式打印出来
    final PrivateKey privateKey = keyPair.getPrivate();
    System.out.println("私钥：" + new String(Base64.encodeBase64(privateKey.getEncoded())));

    // 公钥加密
    final byte[] encrypted = this.rsaUtil.encrypt(data.getBytes(), publicKey);
    System.out.println("加密后：" + new String(encrypted));

    // 私钥解密
    final byte[] decrypted = this.rsaUtil.decrypt(encrypted, privateKey);
    System.out.println("解密后：" + new String(decrypted));
  }
}
