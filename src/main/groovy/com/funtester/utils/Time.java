package com.funtester.utils;

import com.funtester.base.exception.FailException;
import com.funtester.config.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间相关功能工具类
 */
public class Time {

    /**
     * 默认的日志显示格式
     */
    private static ThreadLocal<SimpleDateFormat> DEFAULT_FORMAT = new ThreadLocal() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 纯数字的日期格式
     */
    private static ThreadLocal<SimpleDateFormat> NUM_FORMAT = new ThreadLocal() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };

    /**
     * 标记日期格式,选用ddHHmm
     */
    private static ThreadLocal<SimpleDateFormat> MARK_FORMAT = new ThreadLocal() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("ddHHmm");
        }
    };

    /**
     * 获取calendar类对象，默认UTC时间
     *
     * @return
     */
    public static Calendar calendarInit() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        return instance;
    }

    /**
     * 获取时间戳，13位long类型
     *
     * @return
     */
    public static long getTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * 获取一天开始，utc
     *
     * @return
     */
    public static String getStartOfDay() {
        return getUtcDate() + " 00:00:00";
    }

    /**
     * 获取一天结束，utc
     *
     * @return
     */
    public static String getEndOfDay() {
        return getUtcDate() + " 23:55:55";
    }

    /**
     * 获取当天日期，utc
     *
     * @return
     */
    public static String getUtcDate() {
        int month = getMonth();
        int day = getDay();
        return getYear() + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
    }

    /**
     * 获取时间戳
     *
     * @param time 传入时间，纯数字
     * @return 返回时间戳，毫秒
     */
    public static long getUtcTimestamp(String time) {
        long timestamp = getTimeStamp(time);
        long utc = timestamp + Calendar.getInstance().getTimeZone().getRawOffset();
        return utc;
    }

    /**
     * 获取UTC时间戳
     *
     * @param time 纯数字日期
     * @return
     */
    public static long getUtcTimestamp(long time) {
        return getUtcTimestamp(time + Constant.EMPTY);
    }

    /**
     * 获取当前星期数（按年）
     *
     * @return
     */
    public static int getWeeksNum() {
        return calendarInit().get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取月份,获取值+1,索引从0开始的
     *
     * @return
     */
    public static int getMonth() {
        return calendarInit().get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前是当月的第几天
     *
     * @return
     */
    public static int getDay() {
        return calendarInit().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前是当周第几天
     *
     * @return
     */
    public static int getDayOfWeek() {
        return calendarInit().get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取年份
     *
     * @return
     */
    public static int getYear() {
        return calendarInit().get(Calendar.YEAR);
    }

    public static int getHour() {
        return calendarInit().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return calendarInit().get(Calendar.MINUTE);
    }

    public static int getSecond() {
        return calendarInit().get(Calendar.SECOND);
    }

    /**
     * 获取当前时间
     *
     * @return 返回当前时间
     */
    public static String getNow() {
        return getNow(NUM_FORMAT.get());
    }

    public static String markDate() {
        return getNow(MARK_FORMAT.get());
    }

    public static String getNow(String format) {
        return getNow(new SimpleDateFormat(format));
    }

    public static String getNow(SimpleDateFormat now) {
        return now.format(new Date());
    }

    /**
     * 获取时间戳,会替换掉所有非数字的字符
     * 默认返回{@link Constant#DEFAULT_LONG}
     *
     * @param time 传入时间，纯数字组成的时间
     * @return 返回时间戳，毫秒
     */
    public static long getTimeStamp(String time) {
        time = time.replaceAll("\\D*", Constant.EMPTY).substring(0, 14);
        try {
            return NUM_FORMAT.get().parse(time).getTime();
        } catch (ParseException e) {
            FailException.fail(e);
        }
        return Constant.DEFAULT_LONG;
    }

    /**
     * 根据时间戳返回对应的时间，并且输出
     *
     * @param time long 时间戳
     * @return 返回时间
     */
    public static String getTimeByTimestamp(long time) {
        Date now = new Date(time);
        String nowTime = DEFAULT_FORMAT.get().format(now);
        return nowTime;
    }

    /**
     * 获取时间差，以秒为单位
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return
     */
    @Deprecated
    public static double getTimeDiffer(Date start, Date end) {
        return getTimeDiffer(start.getTime(), end.getTime());
    }

    /**
     * 重载，用long类型取代date
     *
     * @param start
     * @param end
     * @return
     */
    public static double getTimeDiffer(long start, long end) {
        return (end - start) / 1000.0;
    }

    /**
     * 获取当前时间,返回默认格式时间
     *
     * @return
     */
    public static String getDate() {
        return getNow(DEFAULT_FORMAT.get());
    }

    /**
     * 将时间s转换成hms形式
     *
     * @param time
     * @return
     */
    public static String convert(int time) {
        int second = time % 60;
        int ms = time / 60;
        int minute = ms % 60;
        int hour = ms / 60;
        return (hour > 0 ? hour + " h," : "") + (minute > 0 ? minute + " m," : "") + second + " s";
    }

    /**
     * 获取今天零点的时间戳
     * @return
     */
    public static long getZeroTimestamp() {
        long timeStamp = getTimeStamp();
        return timeStamp - timeStamp % Constant.DAY - Calendar.getInstance().getTimeZone().getRawOffset();
    }
}
