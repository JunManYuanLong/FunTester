package com.funtester.utils;

import com.funtester.base.exception.ParamException;
import com.funtester.frame.SourceCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则验证的封装
 */
public class Regex extends SourceCode {

    private static Logger logger = LogManager.getLogger(Regex.class);

    /**
     * 正则校验文本是否匹配
     *
     * @param text  需要匹配的文本
     * @param regex 正则表达式
     * @return
     */
    public static boolean isRegex(String text, String regex) {
        return matcher(text, regex).find();
    }

    /**
     * 正则校验文本是否完全匹配，不包含其他杂项，相当于加上了^和$
     *
     * @param text  需要匹配的文本
     * @param regex 正则表达式
     * @return
     */
    public static boolean isMatch(String text, String regex) {
        return matcher(text, regex).matches();
    }

    /**
     * 获取匹配对象
     *
     * @param text
     * @param regex
     * @return
     */
    private static Matcher matcher(String text, String regex) {
        if (StringUtils.isAnyBlank(text, regex)) ParamException.fail("正则参数错误!");
        return Pattern.compile(regex).matcher(text);
    }

    /**
     * 返回所有匹配项
     *
     * @param text  需要匹配的文本
     * @param regex 正则表达式
     * @return
     */
    public static List<String> regexAll(String text, String regex) {
        Matcher matcher = matcher(text, regex);
        List<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    /**
     * 获取第一个匹配对象
     *
     * @param text
     * @param regex
     * @return
     */
    public static String findFirst(String text, String regex) {
        Matcher matcher = matcher(text, regex);
        if (matcher.find()) return matcher.group();
        return EMPTY;
    }

    /**
     * 获取匹配项，不包含文字信息，会删除regex的内容
     * <p>不保证完全正确</p>
     *
     * @param text
     * @param regex
     * @return
     */
    @Deprecated
    public static String getRegex(String text, String regex) {
        if (StringUtils.isAnyBlank(text, regex)) ParamException.fail("正则参数错误!");
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
