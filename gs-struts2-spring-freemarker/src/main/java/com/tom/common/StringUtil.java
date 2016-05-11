package com.tom.common;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by tom on 2016/5/11.
 * 常用的字符串处理包
 */
public class StringUtil {
    /**
     * Base64编码.
     */
    public static String base64Encode(String input) {
        return new String(Base64.encodeBase64(input.getBytes()));
    }
    /**
     * Base64编码, URL安全(将Base64中的URL非法字符�?,/=转为其他字符, 见RFC3548).
     */
    public static String base64UrlSafeEncode(byte[] input) {
        return Base64.encodeBase64URLSafeString(input);
    }

    /**
     * Base64解码.
     */
    public static String base64Decode(String input) {
        return new String(Base64.decodeBase64(input));
    }
}
