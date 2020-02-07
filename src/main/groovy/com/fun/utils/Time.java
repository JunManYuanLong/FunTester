package com.fun.utils;

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
        return getYearNum() + "-" + (getMonthNum() < 10 ? "0" + getMonthNum() : getMonthNum()) + "-" + (getDayNum() < 10 ? "0" + getDayNum() : getDayNum());
    }

    /**
     * 获取时间戳
     *
     * @param time 传入时间，纯数字
     * @return 返回时间戳，毫秒
     */
    public static long getUtcTimestamp(String time) {
        long timestamp = getTimestamp(time);
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
    public static int getMonthNum() {
        return getCalendar().get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前是当月的第几天
     *
     * @return
     */
    public static int getDayNum() {
        return getCalendar().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取年份
     *
     * @return
     */
    public static int getYearNum() {
        return getCalendar().get(Calendar.YEAR);
    }

    /**
     * 获取calendar类对象，默认UTC时间
     *
     * @return
     */
    public static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(getTimeStamp() - calendar.getTimeZone().getRawOffset()));
        return calendar;
    }

    /**
     * 获取当前时间
     *
     * @return 返回当前时间
     */
    public static String getNow() {
        Date time = new Date();
        SimpleDateFormat now = new SimpleDateFormat("yyyyMMddHHmmss");
        String c = now.format(time);
        return c;
    }

    public static String getHour() {
        return getNow().substring(0, 10);
    }

    /**
     * 获取时间戳
     *
     * @param time 传入时间，纯数字组成的时间
     * @return 返回时间戳，毫秒
     */
    public static long getTimestamp(String time) {
        time = time.replaceAll("\\D*", EMPTY);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            logger.warn("时间格式错误！", e);
        }
        return TEST_ERROR_CODE;
    }

    public static long getTimestamp(long time) {
        return getTimestamp(time + EMPTY);
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
        long time = end.getTime() - start.getTime();
        double differ = (double) time / 1000;
        return differ;
    }

    /**
     * 重载，用long类型取代date
     *
     * @param start
     * @param end
     * @return
     */
    public static double getTimeDiffer(long start, long end) {
        return (end - start) * 1.0 / 1000;
    }

    /**
     * 获取当前时间，返回date类型
     *
     * @return
     */
    public static String getDate() {
        Date time = new Date();
        SimpleDateFormat now = new SimpleDateFormat(DATE_FORMAT);
        String c = now.format(time);
        return c;
    }


}
