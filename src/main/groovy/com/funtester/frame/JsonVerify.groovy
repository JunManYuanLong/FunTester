package com.funtester.frame

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONException
import com.alibaba.fastjson.JSONObject
import com.funtester.base.exception.ParamException
import com.funtester.utils.JsonUtil
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * 操作符重写类,用于匹配JSonpath验证语法,基本重载的方法以及各种比较方法,每个方法重载三次,参数为double,String,verify
 * 数字统一采用double类型,无法操作的String对象的方法返回empty
 * 操作符现在支持['>', '<', '=']三种,暂无增加计划
 */
class JsonVerify extends SourceCode implements Comparable {

    private static Logger logger = LogManager.getLogger(JsonVerify.class)

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
    private JsonVerify(JSONObject json, String path) {
        this(JsonUtil.getInstance(json).getString(path))
        if (isNumber()) num = changeStringToDouble(extra)
    }

    private JsonVerify(String value) {
        extra = value
        if (isNumber()) num = changeStringToDouble(extra)
    }

    /**
     * 获取实例方法
     * @param json
     * @param path
     * @return
     */
    static JsonVerify getInstance(JSONObject json, String path) {
        new JsonVerify(json, path)
    }

    static JsonVerify getInstance(String str) {
        new JsonVerify(str)
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
    def plus(JsonVerify v) {
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
     * 减法重载,string类型
     * @param s
     * @return
     */
    def minus(String s) {
        extra - s
    }
    /**
     * 减法重载
     * @param v
     * @return
     */
    def minus(JsonVerify v) {
        if (isNumber() && v.isNumber()) this - v.num
        else extra - v.extra
    }

    /**
     * extra * i 这里会去强转double为int,调用intvalue()方法
     * @param i
     * @return
     */
    def multiply(double i) {
        if (isNumber()) num * i
        else extra * i
    }

    def multiply(String s) {
        isNumber() ? isNumber(s) ? num * changeStringToDouble(s) : s * num : isNumber(s) ? extra * changeStringToDouble(s) : EMPTY
    }

    def multiply(JsonVerify v) {
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

    def div(JsonVerify v) {
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
            isNumber(s) ? extra.charAt(changeStringToInt(s)) : null
        }
    }

    /**
     * if (a implements Comparable) { a.compareTo(b) == 0 } else { a.equals(b) }* @param a
     * @return
     */
    boolean equals(JsonVerify verify) {
        extra == verify.extra
    }

    boolean equals(Number n) {
        num.toString() == n.toString()
    }

    /**
     * 此方法存在缺陷,在其他项目引入jar时,调用==会直接调用Java的
     * @param s
     * @return
     */
    boolean equals(String s) {
        extra == s
    }

    @Override
    boolean equals(Object o) {
        this == o.toString()
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
        logger.debug("强转类型:{}", tClass.toString())
        if (tClass == Integer) num.intValue()
        else if (tClass == Double) num
        else if (tClass == Long) num.longValue()
        else if (tClass == String) extra
        else if (tClass == JsonVerify) new JsonVerify(extra)
        else if (tClass == Boolean) changeStringToBoolean(extra)
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


    /**
     * 使用与switch-case方法判断
     * @param o
     * @return
     */
    boolean isCase(Object o) {
        this == o
    }

    /**
     * 判断是否符合期望
     * @param str
     * @return
     */
    boolean fit(String str) {
        logger.info("verify对象: {},匹配的字符串: {}", extra, str)
        OPS o = OPS.getInstance(str.charAt(0))
        def res = str.substring(1)
        switch (o) {
            case OPS.GREATER:
                return this > res
            case OPS.LESS:
                return this < res
            case OPS.EQUAL:
                return extra == res
            case OPS.REGEX:
                return extra ==~ res
            default:
                ParamException.fail("判断字符串参数错误!")
        }
    }

    /**
     * 判断是否符合操作后期望,通过{@link com.funtester.config.Constant#REG_PART}分隔
     * @param str
     * @return
     */
    boolean fitFun(String str) {
        def split = str.split(REG_PART, 2)
        def handle = split[0]
        def ops = split[1]
        HPS h = HPS.getInstance(handle.charAt(0))
        def hr = handle.substring(1)
        switch (h) {
            case HPS.PLUS:
                def n = getInstance((this + hr) as String)
                return n.fit(ops)
            case HPS.MINUS:
                def n = getInstance((this - hr) as String)
                return n.fit(ops)
            case HPS.MUL:
                def n = getInstance((this * hr) as String)
                return n.fit(ops)
            case HPS.DIV:
                def n = getInstance((this / hr) as String)
                return n.fit(ops)
            default:
                ParamException.fail("运算操作字符串参数错误!")
        }

    }

    /**
     * 支持的判断类型的操作符枚举类
     */
    static enum OPS {

        GREATER((char) '>'), LESS((char) '<'), EQUAL((char) '='), REGEX((char) '~')

        char name

        OPS(char name) {
            this.name = name
        }

        static OPS getInstance(char c) {
            switch (c) {
                case GREATER.getName():
                    return GREATER;
                case LESS.getName():
                    return LESS;
                case EQUAL.getName():
                    return EQUAL;
                case REGEX.getName():
                    return REGEX
                default:
                    ParamException.fail("判断操作符参数错误!")
            }
        }
    }

    /**
     * 支持的运算类型的操作符枚举类
     */
    static enum HPS {

        PLUS((char) '+'), MINUS((char) '-'), MUL((char) '*'), DIV((char) '/')

        char name

        HPS(char name) {
            this.name = name
        }

        static HPS getInstance(char c) {
            switch (c) {
                case PLUS.getName():
                    return PLUS
                case MINUS.getName():
                    return MINUS
                case MUL.getName():
                    return MUL
                case DIV.getName():
                    return DIV
                default:
                    ParamException.fail("运算操作符参数错误!")
            }
        }
    }


}
