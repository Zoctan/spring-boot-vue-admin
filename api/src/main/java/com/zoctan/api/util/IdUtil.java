package com.zoctan.api.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * ID工具类
 */
public class IdUtil {
    private static final SimpleDateFormat timeSdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public static String uuid() {
        return uuid16();
    }

    public static String uuid64() {
        final UUID uuid = UUID.randomUUID();
        final ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return Base64.encodeBase64URLSafeString(bb.array());
    }

    public static String uuid16() {
        final UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "").toLowerCase();
    }

    public static String randomNumeric(final int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    public static String randomAlphabetic(final int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static String randomAlphanumeric(final int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    synchronized public static String timeId() {
        return timeSdf.format(System.currentTimeMillis()) +
                randomNumeric(5);
    }
}
