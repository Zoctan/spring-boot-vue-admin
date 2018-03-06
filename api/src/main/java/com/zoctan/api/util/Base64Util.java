package com.zoctan.api.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@SuppressWarnings("unused")
class Base64Util {
    /**
     * base64加密方法
     *
     * @param plainText 明文
     * @return 密文
     */
    public static String encode(final String plainText) throws UnsupportedEncodingException {
        final byte[] bytes = plainText.getBytes("UTF-8");
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * base64解密方法
     *
     * @param cipher 密文
     * @return 明文
     */
    public static byte[] decode(final String cipher) throws UnsupportedEncodingException {
        final byte[] bytes = cipher.getBytes("UTF-8");
        return Base64.getDecoder().decode(bytes);
    }
}
