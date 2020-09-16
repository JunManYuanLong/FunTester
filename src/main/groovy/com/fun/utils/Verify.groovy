package com.fun.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONException
import com.alibaba.fastjson.JSONObject
import com.fun.frame.SourceCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 操作符重写类,用于匹配JSonpath验证语法
 */
class Verify extends SourceCode implements Comparable {

    public static Logger logger = LoggerFactory.getLogger(Verify.class)

    /**
     * 验证文本
     */
    String extra

    /**
     * 验证数字格式
     */
    double num

    /**
     * 构造方法,暂时写着,尽量使用jsonutil创造verify对象
     *
     * @param json
     * @param path
     */
    private Verify(JSONObject json, String path) {
        extra = JsonUtil.getInstance(json).getString(path)
        if (isNumber()) num = changeStringToDouble(extra)
    }

    private Verify(String value) {
        extra = value
        if (isNumber()) num = changeStringToDouble(extra)
    }

    /**
     * 获取实例方法
     * @param json
     * @param path
     * @return
     */
    static Verify getInstance(JSONObject json, String path) {
        new Verify(json, path)
    }

    static Verify getInstance(String str) {
        new Verify(str)
    }

    /**
     * 加法重载
     * @param i
     * @return
     */
    def plus(double i) {
        isNumber() ? num + i : extra + i.toString()
    }

    /**
     * 加法重载,string类型
     * @param s
     * @return
     */
    def plus(String s) {
        isNumber() && isNumber(s) ? num + changeStringToDouble(s) : extra + s
    }

    /**
     * 加法重载,verify类型
     * @param s
     * @return
     */
    def plus(Verify v) {
        isNumber() && v.isNumber() ? this + (v.num) : extra + v.extra
    }

    /**
     * 减法重载
     * @param i
     * @return
     */
    def minus(double i) {
        isNumber() ? num - i : extra - i.toString()
    }

    /**
     * 加法重载,string类型
     * @param s
     * @return
     */
    def minus(String s) {
        extra - s
    }

    def minus(Verify v) {
        if (isNumber() && v.isNumber()) this - v.num
        extra - v.extra
    }

    /**
     * extra * i 这里会去强转double为int,调用intvalue()方法
     * @param i
     * @return
     */
    def multiply(double i) {
        if (isNumber()) num * i
        extra * i
    }

    def multiply(String s) {
        isNumber() ? isNumber(s) ? num * changeStringToDouble(s) : s * num : isNumber(s) ? extra * changeStringToDouble(s) : EMPTY
    }

    def multiply(Verify v) {
        this * v.extra
    }

    /**
     * 除法重载
     * @param i
     * @return
     */
    def div(int i) {
        if (isNumber()) num / i
    }

    def div(String s) {
        if (isNumber() && isNumber(s)) num / changeStringToDouble(s)
    }

    def div(Verify v) {
        if (isNumber() && v.isNumber()) num / v.num
    }

    def mod(int i) {
        if (isNumber()) (int) (num % i * 10000) * 1.0 / 10000
    }

    /**
     * 直接取值,用于数组类型
     * @param i
     * @return
     */
    def getAt(int i) {
        try {
            JSONArray.parseArray(extra)[i]
        } catch (JSONException e) {
            i >= extra.length() ? EMPTY : extra[i]
        }
    }

    /**
     * 直接取值,用户json类型
     * @param i
     * @return
     */
    def getAt(String s) {
        try {
            JSON.parseObject(extra)[s]
        } catch (JSONException e) {
            extra.indexOf(s)
        }
    }

    /**
     * if (a implements Comparable) { a.compareTo(b) == 0 } else { a.equals(b) }* @param a
     * @return
     */
    boolean equals(Verify verify) {
        extra == verify.extra
    }

    boolean equals(Number n) {
        num == n.doubleValue()
    }

    boolean equals(String s) {
        extra == s
    }

    @Override
    boolean equals(Object o) {
        extra == o.toString()
    }

    /**
     * a <=> b  a.compareTo(b)
     * a>b      a.compareTo(b) > 0
     * a>=b     a.compareTo(b) >= 0
     * a<b      a.compareTo(b) < 0
     * a<=b     a.compareTo(b) <= 0
     * @param o
     * @return
     */
    @Override
    int compareTo(Object o) {
        if (isNumber() && (o instanceof Number || isNumber(o.toString()))) {
            return num.compareTo(o.toString() as Double)
        } else {
            extra.length().compareTo(o.toString().length())
        }
    }

    /**
     * 类型转换,用于as关键字
     * @param tClass
     * @return
     */
    def <T> T asType(Class<T> tClass) {
        logger.info(tClass.toString())
        if (tClass == Integer) num.intValue()
        if (tClass == Double) num
        if (tClass == Long) num.longValue()
        if (tClass == String) extra
        if (tClass == Verify) new Verify(extra)
        if (tClass == Boolean) changeStringToBoolean(extra)
    }

    /**
     * 用户正则匹配
     * @param regex
     * @return
     */
    def regex(String regex) {
        extra ==~ regex
    }

    /**
     * 是否是数字
     * @return
     */
    def isNumber() {
        isNumber(extra)
    }

    /**
     * 是否为boolean类型
     * @return
     */
    def isBoolean() {
        extra ==~ ("false|true")
    }

    @Override
    String toString() {
        extra
    }
}
