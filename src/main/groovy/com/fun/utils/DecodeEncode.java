package com.fun.utils;

import com.fun.config.Constant;
import com.fun.frame.SourceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 编码格式转码解码类
 */
public class DecodeEncode extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(DecodeEncode.class);

    /**
     * url进行转码，常用于网络请求
     *
     * @param text 需要转码的文本
     * @return 返回转码后的文本
     */
    public static String urlEncoderText(String text) {
        return urlEncoderText(text, UTF_8);
    }

    /**
     * url进行转码，常用于网络请求
     *
     * @param text 需要转码的文本
     * @return 返回转码后的文本
     */
    public static String urlEncoderText(String text, Charset charset) {
        String result = EMPTY;
        try {
            result = java.net.URLEncoder.encode(text, charset.toString());
        } catch (UnsupportedEncodingException e) {
            logger.warn("数据格式错误！", e);
        }
        return result;
    }

    /**
     * url进行解码，常用于解析响应，默认是UTF-8字符集
     *
     * @param text 需要解码的文本
     * @return 解码后的文本
     */
    public static String urlDecoderText(String text, Charset charset) {
        String result = EMPTY;
        try {
            result = java.net.URLDecoder.decode(text, charset.toString());
        } catch (UnsupportedEncodingException e) {
            logger.warn("数据格式错误！", e);
        }
        return result;
    }

    /**
     * url进行解码，常用于解析响应，默认是UTF-8字符集
     *
     * @param text 需要解码的文本
     * @return 解码后的文本
     */
    public static String urlDecoderText(String text) {
        return urlDecoderText(text, UTF_8);
    }

    /**
     * 对本文进行base64解码，方法默认ISO_8859_1
     *
     * @param text
     * @return
     */
    public static String base64Decode(String text) {
        try {
            return new String(Base64.getDecoder().decode(text));
        } catch (Exception e) {
            logger.warn("base64解码失败！", e);
            return EMPTY;
        }
    }

    /**
     * 对本文进行base64转码，方法默认了utf8
     *
     * @param text
     * @return
     */
    public static String base64Encode(String text) {
        return base64Encode(text, UTF_8);
    }

    /**
     * 对本文进行base64转码，编码格式自定义
     *
     * @param text
     * @param charset
     * @return
     */
    public static String base64Encode(String text, Charset charset) {
        try {
            return new String(Base64.getEncoder().encode(text.getBytes(charset)));
        } catch (Exception e) {
            logger.warn("base64转码失败！", e);
            return EMPTY;
        }
    }

    public static String base64Encode(byte[] data) {
        try {
            return new String(Base64.getEncoder().encode(data));
        } catch (Exception e) {
            logger.warn("base64转码失败！", e);
            return EMPTY;
        }
    }

    /**
     * 使用md5加密数据
     *
     * @param text
     * @return
     */
    public static String encodeByMd5(String text) {
        byte[] date = null;
        try {
            date = text.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.warn("utf-8格式错误！", e);
        }
        MessageDigest message = null;
        try {
            message = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            logger.warn("md5加密失败！", e);
        }
        message.update(date);
        byte[] result = message.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (int offset = 0; offset < result.length; offset++) {
            int i = result[offset];
            if (i < 0)// 如果负数
                i += 256;// 变成正数，0xff & i 也可以
            if (i < 16)// 如果小于16，则加上0小于16，转换之后就是一位缺少一位故要加0
                stringBuffer.append("0");
            stringBuffer.append(Integer.toHexString(i));
        }
        return stringBuffer.toString();
    }

    /**
     * MD5加盐加密
     *
     * @param text
     * @param salt
     * @return
     */
    public static String encodeByMd5(String text, String salt) {
        return encodeByMd5(text + salt);
    }

    /**
     * 处理Unicode码转成utf-8
     *
     * @param str
     * @return
     */
    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            String group = matcher.group(2);
            ch = (char) Integer.parseInt(group, 16);
            String group1 = matcher.group(1);
            str = str.replace(group1, ch + Constant.EMPTY);
        }
        return str;
    }


}
