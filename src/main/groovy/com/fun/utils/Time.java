package com.fun.utils;

import com.fun.config.Constant;
import com.fun.frame.SourceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Time extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(Time.class);

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
        long utc = timestamp - Calendar.getInstance().getTimeZone().getRawOffset();
        return utc;
    }

    /**
     * 获取UTC时间戳
     *
     * @param time 纯数字日期
     * @return
     */
    public static long getUtcTimestamp(long time) {
        return getUtcTimestamp(time + EMPTY);
    }

    /**
     * 获取当前星期数（按年）
     *
     * @return
     */
    public static int getWeeksNum() {
        return getCalendar().get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取月份
     *
     * @return
     */
    public static int getMonth() {
        return getCalendar().get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前是当月的第几天
     *
     * @return
     */
    public static int getDay() {
        return getCalendar().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取年份
     *
     * @return
     */
    public static int getYear() {
        return getCalendar().get(Calendar.YEAR);
    }


    public static int getHour() {
        return getCalendar().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return getCalendar().get(Calendar.MINUTE);
    }

    public static int getSecond() {
        return getCalendar().get(Calendar.SECOND);
    }

    /**
     * 获取calendar类对象，默认UTC时间
     *
     * @return
     */
    public static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(getTimeStamp()));
        return calendar;
    }

    /**
     * 获取当前时间
     *
     * @return 返回当前时间
     */
    public static String getNow() {
        return getNow("yyyyMMddHHmmss");
    }

    public static String markDate() {
        return getNow("ddHHmm");
    }

    public static String getNow(String format) {
        Date time = new Date();
        SimpleDateFormat now = new SimpleDateFormat(format);
        return now.format(time);
    }


    /**
     * 获取时间戳,会替换掉所有非数字的字符
     * 默认返回{@link Constant#DEFAULT_LONG}
     *
     * @param time 传入时间，纯数字组成的时间
     * @return 返回时间戳，毫秒
     */
    public static long getTimeStamp(String time) {
        time = time.replaceAll("\\D*", EMPTY);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            logger.warn("时间格式错误！", e);
        }
        return DEFAULT_LONG;
    }

    /**
     * 根据时间戳返回对应的时间，并且输出
     *
     * @param time long 时间戳
     * @return 返回时间
     */
    public static String getTimeByTimestamp(long time) {
        Date now = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        String nowTime = format.format(now);
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
     * 获取当前时间，返回date类型
     *
     * @return
     */
    public static String getDate() {
        return getNow(DATE_FORMAT);
    }


}
