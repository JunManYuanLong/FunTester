package com.funtester.utils;

import com.funtester.base.exception.FailException;
import com.funtester.config.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

/**
 * 编码格式转码解码类
 */
public class DecodeEncode extends Constant{

    private static Logger logger = LogManager.getLogger(DecodeEncode.class);

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
     * 对本文进行base64解码，方法默认UTF_8
     *
     * @param text
     * @return
     */
    public static String base64Decode(String text) {
        return base64Decode(text, UTF_8);
    }

    /**
     * 对字符串进行解码,使用编码格式参数
     *
     * @param text
     * @param charset
     * @return
     */
    public static String base64Decode(String text, Charset charset) {
        return new String(base64Byte(text.getBytes(charset)));
    }

    /**
     * 转换
     *
     * @param text
     * @return
     */
    public static byte[] base64Byte(byte[] text) {
        return Base64.getDecoder().decode(text);
    }

    /**
     * 获取字符串的字节数组
     *
     * @param text
     * @return
     */
    public static byte[] base64Byte(String text) {
        return base64Byte(text.getBytes());
    }

    /**
     * 压缩字符串,默认梳utf-8
     *
     * @param text
     * @return
     */
    public static String zipBase64(String text) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(out)) {
                deflaterOutputStream.write(text.getBytes(Constant.UTF_8));
            }
            return DecodeEncode.base64Encode(out.toByteArray());
        } catch (IOException e) {
            logger.error("压缩文本失败:{}", text, e);
        }
        return EMPTY;
    }

    /**
     * 解压字符串,默认utf-8
     *
     * @param text
     * @return
     */
    public static String unzipBase64(String text) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            try (OutputStream outputStream = new InflaterOutputStream(os)) {
                outputStream.write(DecodeEncode.base64Byte(text));
            }
            return new String(os.toByteArray(), Constant.UTF_8);
        } catch (IOException e) {
            logger.error("解压文本失败:{}", text, e);
        }
        return EMPTY;
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
            FailException.fail("utf-8格式错误！" + e.getMessage());
        }
        MessageDigest message = null;
        try {
            message = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            FailException.fail("md5加密失败！" + e.getMessage());
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
     * 处理Unicode码转(\u6210\u529f)
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
            str = str.replace(group1, ch + EMPTY);
        }
        return str;
    }

    /**
     * 处理Unicode码转成(\xe6\x88\x90\xe5\x8a\x9f")
     *
     * @param str
     * @return
     */
    public static String unicodeToStringX(String str) {
        str = str.replaceAll("\\\\x", "%");
        return urlDecoderText(str, DEFAULT_CHARSET);
    }


}
