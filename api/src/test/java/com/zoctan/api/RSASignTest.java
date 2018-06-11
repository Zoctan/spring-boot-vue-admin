package com.zoctan.api;

import com.zoctan.api.util.RSAUtil;
import org.junit.Assert;
import org.junit.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSASignTest {
    private final RSAUtil rsaUtil = new RSAUtil();

    /**
     * 加载公私钥pem格式文件测试
     */
    @Test
    public void test1() {
        final PublicKey publicKey = this.rsaUtil.loadPemPublicKey("rsa/public-key.pem");
        final PrivateKey privateKey = this.rsaUtil.loadPemPrivateKey("rsa/private-key.pem");
        Assert.assertNotNull(publicKey);
        Assert.assertNotNull(privateKey);
        System.out.println("公钥：" + publicKey);
        System.out.println("私钥：" + privateKey);

    }

    /**
     * 生成RSA密钥对并进行加解密测试
     */
    @Test
    public void test2() throws Exception {
        final String data = "hello word";
        final KeyPair keyPair = this.rsaUtil.genKeyPair(512);

        // 获取公钥，并以base64格式打印出来
        final PublicKey publicKey = keyPair.getPublic();
        System.out.println("公钥：" + new String(Base64.getEncoder().encode(publicKey.getEncoded())));

        // 获取私钥，并以base64格式打印出来
        final PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("私钥：" + new String(Base64.getEncoder().encode(privateKey.getEncoded())));

        // 公钥加密
        final byte[] encryptedBytes = this.rsaUtil.encrypt(data.getBytes(), publicKey);
        System.out.println("加密后：" + new String(encryptedBytes));

        // 私钥解密
        final byte[] decryptedBytes = this.rsaUtil.decrypt(encryptedBytes, privateKey);
        System.out.println("解密后：" + new String(decryptedBytes));
    }
}