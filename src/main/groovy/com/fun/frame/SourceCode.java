package com.fun.frame;


import com.alibaba.fastjson.JSONObject;
import com.fun.base.exception.FailException;
import com.fun.base.exception.ParamException;
import com.fun.base.interfaces.IMessage;
import com.fun.utils.Regex;
import com.fun.utils.Time;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class SourceCode extends Output implements Cloneable {

    private static Logger logger = LoggerFactory.getLogger(SourceCode.class);

    private static Scanner scanner;

    private static IMessage iMessage;

    public static IMessage getiMessage() {
        return iMessage;
    }

    public static void setiMessage(IMessage iMessage) {
        SourceCode.iMessage = iMessage;
    }

    /**
     * 获取日志记录的logger
     *
     * @param name
     * @return
     */
    public static Logger getLogger(String name) {
        if (!StringUtils.isNoneEmpty(name)) return logger;
        return LoggerFactory.getLogger(name);
    }

    public static Logger getLogger(Class name) {
        return LoggerFactory.getLogger(name);
    }

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
     * <p>如何执行close方法，只能用一次</p>
     *
     * @param key
     */
    public static void waitForKey(Object key) {
        logger.warn("请输入“{}”继续运行！", key.toString());
        long start = Time.getTimeStamp();
        scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            if (next.equalsIgnoreCase(key.toString())) break;
            logger.warn("输入：{}错误！", next);
        }
        long end = Time.getTimeStamp();
        double timeDiffer = Time.getTimeDiffer(start, end);
        logger.info("本次共等待：" + timeDiffer + "秒！");
    }

    /**
     * 获取屏幕输入内容
     * <p>如何执行close方法，只能用一次</p>
     *
     * @return
     */
    public static String getInput() {
        scanner = new Scanner(System.in);
        String next = scanner.next();
        logger.debug("输、入内容：{}", next);
        return next;
    }

    /**
     * 关闭scanner，解决无法多次使用wait的BUG
     */
    public static void closeScanner() {
        scanner.close();
    }

    /**
     * 将数组变成json对象，使用split方法
     * <p>
     * split方法默认limit=2
     * </p>
     *
     * @param objects
     * @param regex   分隔的regex表达式
     * @return
     */
    public static JSONObject changeArraysToJson(Object[] objects, String regex) {
        JSONObject args = new JSONObject();
        Arrays.stream(objects).forEach(x -> {
            String[] split = x.toString().split(regex, 2);
            args.put(split[0], split[1]);
            logger.debug("key:[{}],value:[{}]", split[0], split[1]);
        });
        return args;
    }

    /**
     * 获取一个简单的json对象
     * <p>
     * 使用“=”号作为分隔符，limit=2
     * </p>
     *
     * @param content
     * @return
     */
    public static JSONObject getJson(String... content) {
        if (StringUtils.isAnyEmpty(content)) ParamException.fail("转换成json格式参数错误!");
        return changeArraysToJson(content, "=");
    }

    public static JSONObject getSimpleJson(String key, Object value) {
        if (StringUtils.isBlank(key)) return null;
        JSONObject result = new JSONObject(1) {{
            put(key, value);
        }};
        return result;
    }

    /**
     * 获取text复制拼接的string
     *
     * @param text
     * @param time 次数
     * @return
     */
    public static String getManyString(String text, int time) {
        return IntStream.range(0, time).mapToObj(x -> text).collect(Collectors.joining());
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
        logger.debug("需要转化成的文本：{}", text);
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            logger.warn("转化int类型失败！", e);
            return TEST_ERROR_CODE;
        }
    }

    /**
     * 把string类型转化为long
     *
     * @param text 需要转化的文本
     * @return
     */
    public static long changeStringToLong(String text) {
        logger.debug("需要转化成的文本：{}", text);
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            logger.warn("转化int类型失败！", e);
            return TEST_ERROR_CODE;
        }
    }

    /**
     * 将string转换成boolean，失败返回null，待修改
     *
     * @param text
     * @return
     */
    public static boolean changeStringToBoolean(String text) {
        logger.debug("需要转化成的文本：{}", text);
        return text == null ? null : text.equalsIgnoreCase("true") ? true : text.equalsIgnoreCase("false") ? false : null;
    }

    /**
     * string转化为double
     *
     * @param text
     * @return
     */
    public static Double changeStringToDouble(String text) {
        logger.debug("需要转化成的文本：{}", text);
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
        logger.debug("需要判断的文本：{}", text);
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
     * 随机范围int,取头不取尾
     *
     * @param start
     * @param end
     * @return
     */
    public static int getRandomIntRange(int start, int end) {
        if (end <= start) return TEST_ERROR_CODE;
        return new Random().nextInt(end - start) + start;
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

    /**
     * 通用的终止运行的方法,用于脚本调试等场景
     */
    public static void fail() {
        throw new FailException();
    }

    /**
     * 通过将对象序列化成数据流实现深层拷贝的方法
     * <p>
     * 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
     * </p>
     *
     * @param t   需要被拷贝的对象,必需实现 Serializable接口,不然会报错
     * @param <T> 需要拷贝对象的类型
     * @return
     */
    public static <T> T deepClone(T t) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(t);
            // 将流序列化成对象
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (IOException e) {
            logger.error("线程任务拷贝失败!", e);
        } catch (ClassNotFoundException e) {
            logger.error("未找到对应类!", e);
        }
        return null;
    }


}