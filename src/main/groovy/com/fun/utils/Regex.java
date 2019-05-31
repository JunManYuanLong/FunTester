package com.fun.utils;

import com.fun.frame.SourceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则验证的封装
 */
public class Regex extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(Regex.class);

    /**
     * 正则校验文本是否匹配
     *
     * @param text  需要匹配的文本
     * @param regex 正则表达式
     * @return
     */
    public static boolean isRegex(String text, String regex) {
        return Pattern.compile(regex).matcher(text).find();
    }

    /**
     * 正则校验文本是否完全匹配，不包含其他杂项，相当于加上了^和$
     *
     * @param text  需要匹配的文本
     * @param regex 正则表达式
     * @return
     */
    public static boolean isMatch(String text, String regex) {
        return Pattern.compile(regex).matcher(text).matches();
    }

    /**
     * 返回所有匹配项
     *
     * @param text  需要匹配的文本
     * @param regex 正则表达式
     * @return
     */
    public static List<String> regexAll(String text, String regex) {
        List<String> result = new ArrayList<>();
        Matcher matcher = Pattern.compile(regex).matcher(text);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    /**
     * 获取匹配项，不包含文字信息，会删除regex的内容
     * <p>不保证完全正确</p>
     *
     * @param text
     * @param regex
     * @return
     */
    public static String getRegex(String text, String regex) {
        String result = EMPTY;
        try {
            result = regexAll(text, regex).get(0);
            String[] split = regex.split("(\\.|\\+|\\*|\\?)");
            for (int i = 0; i < split.length; i++) {
                String s1 = split[i];
                if (!s1.isEmpty())
                    result = result.replaceAll(s1, EMPTY);
            }
        } catch (Exception e) {
            logger.warn("获取匹配对象失败！", e);
        } finally {
            return result;
        }
    }

}
