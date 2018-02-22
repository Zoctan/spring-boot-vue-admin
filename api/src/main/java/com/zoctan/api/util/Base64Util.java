package com.zoctan.api.util;

import com.zoctan.api.core.exception.UtilException;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Util {
    /**
     * base64加密方法
     *
     * @param plainText 明文
     * @return 密文
     */
    public static String encode(final String plainText) {
        try {
            final byte[] bytes = plainText.getBytes("UTF-8");
            return Base64.getEncoder().encodeToString(bytes);
        } catch (final UnsupportedEncodingException e) {
            throw new UtilException(e.getMessage());
        }
    }

    /**
     * base64解密方法
     *
     * @param cipher 密文
     * @return 明文
     */
    public static byte[] decode(final String cipher) {
        try {
            final byte[] bytes = cipher.getBytes("UTF-8");
            return Base64.getDecoder().decode(bytes);
        } catch (final Exception e) {
            throw new UtilException(e.getMessage());
        }
    }
}
