package com.fun.frame;


import com.fun.utils.Regex;
import com.fun.utils.Time;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class SourceCode extends Output {

    private static Logger logger = LoggerFactory.getLogger(SourceCode.class);

    /**
     * 获取当前时间戳10位int 类型的数据
     *
     * @return
     */
    public static int getMark() {
        return (int) (Time.getTimeStamp() / 1000);
    }

    /**
     * 获取纳秒的时间标记
     *
     * @return
     */
    public static long getNanoMark() {
        return System.nanoTime();
    }

    /**
     * 等待方法，用sacnner类，控制台输出字符key时会跳出循环
     *
     * @param key
     */
    public static void waitForKey(Object key) {
        logger.warn("请输入“{}”继续运行！", key.toString());
        long start = Time.getTimeStamp();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            if (next.equals(key.toString())) break;
        }
        scanner.close();
        long end = Time.getTimeStamp();
        double timeDiffer = Time.getTimeDiffer(start, end);
        logger.info("本次共等待：" + timeDiffer + "秒！");
    }

    /**
     * 获取屏幕输入内容
     *
     * @return
     */
    public static String getInput() {
        Scanner scanner = new Scanner(System.in);
        String next = scanner.next();
        scanner.close();
        return next;
    }

    /**
     * 将数组变成json对象，使用split方法
     *
     * @param objects
     * @param regex
     * @return
     */
    public static JSONObject changeArraysToJson(Object[] objects, String regex) {
        JSONObject args = new JSONObject();
        Arrays.stream(objects).forEach(x -> args.put(x.toString().split(regex, 2)[0], x.toString().split(regex, 2)[1]));
        return args;
    }

    /**
     * 获取一个简单的json对象
     *
     * @param content
     * @return
     */
    public static JSONObject getJson(Object... content) {
        JSONObject json = new JSONObject();
        for (int i = 0; i < content.length; i++) {
            String[] split = content[i].toString().split("=", 2);
            json.put(split[0], split[1]);
        }
        return json;
    }

    /**
     * 获取text复制拼接的string
     *
     * @param text
     * @param time 重复次数
     * @return
     */
    public static String getManyString(String text, int time) {
        String str = text;
        for (int i = 0; i < time - 1; i++) {
            str = str + text;
        }
        return str;
    }

    /**
     * 获取一个百分比，两位小数
     *
     * @param total 总数
     * @param piece 成功数
     * @return 百分比
     */
    public static double getPercent(int total, int piece) {
        if (total == 0) return 0.00;
        int s = (int) (piece * (1.0) / total * 10000);
        double result = s * 1.0 / 100;
        return result;
    }

    /**
     * 删除字符，只删除第一个，不匹配正则
     *
     * @param delChar 要删除的字符
     * @param text    被删除的字符串
     * @return 返回删除后的字符串
     */
    public static String deleteCharFromString(String delChar, String text) {
        int index = text.indexOf(delChar);
        int length = delChar.length();
        int total = text.length();
        text = text.substring(0, index) + text.substring(0 + length, total);
        return text;
    }

    /**
     * 格式化数字格式，使用千分号
     *
     * @param number
     * @return
     */
    public static String getFormatNumber(Number number) {
        DecimalFormat format = new DecimalFormat("#,###");
        return format.format(number);
    }

    /**
     * 获取随机IP地址
     *
     * @return
     */
    public static String getRandomIP() {
        return getRandomInt(255) + "." + getRandomInt(255) + "." + getRandomInt(255) + "." + getRandomInt(255);
    }

    /**
     * 把string类型转化为int
     *
     * @param text 需要转化的文本
     * @return
     */
    public static int changeStringToInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            logger.warn("转化int类型失败！", e);
            return TEST_ERROR_CODE;
        }
    }

    public static boolean changeStringToBoolean(String text) {
        return text == null ? null : text.equalsIgnoreCase("true") ? true : text.equalsIgnoreCase("false") ? false : null;
    }


    /**
     * 把字符串每个字符用分隔器连接起来
     *
     * @param text
     * @param separator
     * @return
     */
    public static String join(String text, String separator) {
        return StringUtils.join(ArrayUtils.toObject(text.toCharArray()), separator);
    }

    /**
     * 把list用分隔器连接起来
     *
     * @param list
     * @param separator
     * @param prefix
     * @param suffix
     * @return
     */
    public static String join(List list, String separator, String prefix, String suffix) {
        return list.stream().map(x -> x.toString()).collect(Collectors.joining(separator, prefix, suffix)).toString();
    }

    public static String join(List list, String separator) {
        return join(list, separator, "", "");
    }

    /**
     * string转化为double
     *
     * @param text
     * @return
     */
    public static Double changeStringToDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            logger.warn("转化double类型失败！", e);
            return TEST_ERROR_CODE * 1.0;
        }
    }

    /**
     * 是否是数字，000不算
     *
     * @param text
     * @return
     */
    public static boolean isNumber(String text) {
        if (StringUtils.isEmpty(text)) return false;
        if (text.equals("0")) return true;
        return Regex.isRegex(text, "^[1-9][0-9]*$");
    }

    /**
     * 线程休眠，超过30，单位是毫秒，小于等于30，单位是秒,此方法不适用于groovy脚本，groovy默认单位统一ms
     *
     * @param second 秒，可以是小数
     */
    public static void sleep(int second) {
        try {
            if (second > 30) Thread.sleep(second);
            if (second <= 30) Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            logger.warn("sleep发生错误！", e);
        }
    }

    /**
     * 获取随机数，获取1~num 的数字，包含 num
     *
     * @param num 随机数上限
     * @return 随机数
     */
    public static int getRandomInt(int num) {
        return new Random().nextInt(num) + 1;
    }

    /**
     * 获取一定范围内的随机值
     *
     * @param start 初始值
     * @param range 随机范围
     * @return
     */
    public static double getRandomRange(double start, double range) {
        return start - range + getRandomDouble() * range * 2;
    }

    /**
     * 获取随机数，获取0-1 的数字
     *
     * @return 随机数
     */
    public static double getRandomDouble() {
        return new Random().nextDouble();
    }

    /**
     * 获取一个intsteam
     *
     * @param start
     * @param end
     * @return
     */
    public static IntStream range(int start, int end) {
        return IntStream.range(start, end);
    }

    /**
     * 获取一个intsteam，默认从0开始
     *
     * @param num
     * @return
     */
    public static IntStream range(int num) {
        return IntStream.range(0, num);
    }
}