# JsonPath验证类既Groovy重载操作符实践

在使用[JsonPath工具类封装](https://mp.weixin.qq.com/s/KyuCuG5fVEExxBdGJO2LdA)进行接口响应的验证过程中，由于使用原生的`JsonPath`的`API`获取到的值默认是`object`，如果需要转成其他类型需要多写一些代码。

这一点对于将要提供给前端的工具类来讲，着实有点缺憾。因为用户在前端都在用文本标记语法来编写用例的，如果可以的话，更需要用**>**、**+**、**/**、**=**这样的语法标记就会更容易一些。

基于这样的需求，再结合[Groovy重载操作符（终极版）](https://mp.weixin.qq.com/s/4oYGJ2B2Y1AqxsIj8v5nZA)中学到的技能点，所以写了一个用于`JsonPath`验证功能的`verify`验证类。

# 代码如下

```Groovy

package com.fun.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONException
import com.alibaba.fastjson.JSONObject
import com.fun.frame.SourceCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 操作符重写类,用于匹配JSonpath验证语法,基本重载的方法以及各种比较方法,每个方法重载三次,参数为double,String,verify
 * 数字统一采用double类型,无法操作的String对象的方法返回empty
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
        this(JsonUtil.getInstance(json).getString(path))
        if (isNumber()) num = changeStringToDouble(extra)
    }

    private Verify(String value) {
        extra = value
        logger.info("构建verify对象:{}",extra)
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
        logger.info("强转类型:{}", tClass.toString())
        if (tClass == Integer) num.intValue()
        else if (tClass == Double) num
        else if (tClass == Long) num.longValue()
        else if (tClass == String) extra
        else if (tClass == Verify) new Verify(extra)
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
}

```

# 演示代码


```Groovy
package com.fun.ztest.groovy

import com.fun.frame.httpclient.FanLibrary
import com.fun.utils.Verify

class Ft extends FanLibrary {

    public static void main(String[] args) {

        def instance1 = Verify.getInstance("fdsafds")
        def instance2 = Verify.getInstance("fdsa")
        def instance3 = Verify.getInstance("0.2365")
        def instance4 = Verify.getInstance("5.0")
        def instance5 = Verify.getInstance("5")
        println instance1 + instance2
        println instance1 - instance2
        println instance1 * instance2 as boolean
        println instance3 > instance4
        println instance3 * instance4
        println instance1 * instance4
        println instance5 == instance4
        println instance5 / instance4

        println instance4 as Integer

        testOver()
    }


}

```

# 控制台输出


```shell
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 构建verify对象:fdsafds
INFO-> 构建verify对象:fdsa
INFO-> 构建verify对象:0.2365
INFO-> 构建verify对象:5.0
INFO-> 构建verify对象:5
fdsafdsfdsa
fds
false
false
1.1824999999999999
fdsafdsfdsafdsfdsafdsfdsafdsfdsafds
true
1.0
INFO-> 强转类型:class java.lang.Integer
5

Process finished with exit code 0

```

----
公众号**FunTester**首发，原创分享爱好者，腾讯云和掘金社区首页推荐，知乎七级原创作者，欢迎关注、交流，禁止第三方擅自转载。

FunTester热文精选
=

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)
- [测试开发工程师工作技巧](https://mp.weixin.qq.com/s/TvrUCisja5Zbq-NIwy_2fQ)
- [Selenium4 IDE，它终于来了](https://mp.weixin.qq.com/s/XNotlZvFpmBmBQy1pYifOw)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [什么阻碍手动测试发挥价值](https://mp.weixin.qq.com/s/t0VAVyA3ywQsHzaqzSILOw)
- [压测中测量异步写入接口的延迟](https://mp.weixin.qq.com/s/odvK1iYgg4eRVtOOPbq15w)
- [“双花”BUG的测试分享](https://mp.weixin.qq.com/s/0dsBsssNfg-seJ_tu9zFaQ)
- [为什么测试覆盖率如此重要](https://mp.weixin.qq.com/s/0evyuiU2kdXDgMDnDKjORg)
- [吐个槽，非测误入。](https://mp.weixin.qq.com/s/BBFzUZVFMmU7a6qfLKas2w)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDnHxttBoq6jhgic4jJF8icbAMdOvlR0xXUX9a3tupYYib3ibYyIHicNtefS3Jo7yefLKlQWgLK7bCgCLA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)