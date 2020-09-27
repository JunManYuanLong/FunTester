# JsonPath工具类封装

书接上文和上上文：

- [JsonPath实践（一）](https://mp.weixin.qq.com/s/Cq0_v_ptbGd4f5y8HIsq7w)
- [JsonPath实践（二）](https://mp.weixin.qq.com/s/w_iJTiuQahIw6U00CJVJZg)
- [JsonPath实践（三）](https://mp.weixin.qq.com/s/58A3k0T6dbOkBJ5nRYKDqA)
- [JsonPath实践（四）](https://mp.weixin.qq.com/s/8ER61qrkMj8bdBpyuq9r6w)
- [JsonPath实践（五）](https://mp.weixin.qq.com/s/knVLW960WXnckGLstdrOVQ)
- [JsonPath实践（六）](https://mp.weixin.qq.com/s/ckBCK3t1w68FLBhaw5a7Jw)

在经历过一些波折之后，总算是把`JsonPath`工具类的封装类写好了，时间仓促。没有太严格的测试，等有机会我再用[Groovy进行单元测试框架spock](https://mp.weixin.qq.com/s/ahyP-YQTzigeq_5N8byC4g)写一些单测来验证一下。

工具类的语言`Groovy`，有点不必多说了，相信使用`Java`技术栈的同学读起来应该不会有障碍。另外我把官方的`API`当做类注释写出来了。

有两个关于`verify`类的方法，这个主要是为了验证用的，涉及到[Groovy重载操作符](https://mp.weixin.qq.com/s/4oYGJ2B2Y1AqxsIj8v5nZA)，是专门写的一个提供给`Groovy`脚本的验证功能类，还有就是为[开源测试服务](https://mp.weixin.qq.com/s/ZOs0cp_vt6_iiundHaKk4g)增加功能储备。

代码如下：


```Groovy
package com.fun.utils

import com.alibaba.fastjson.JSONObject
import com.fun.base.exception.ParamException
import com.fun.frame.SourceCode
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.JsonPathException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**下面是例子,官方文档地址:https://github.com/json-path/JsonPath/blob/master/README.md
 * $.store.book[*].author	The authors of all books
 * $..author	All authors
 * $.store.*	All things, both books and bicycles
 * $.store..price	The price of everything
 * $..book[2]	The third book
 * $..book[-2]	The second to last book
 * $..book[0,1]	The first two books
 * $..book[:2]	All books from index 0 (inclusive) until index 2 (exclusive)
 * $..book[1:2]	All books from index 1 (inclusive) until index 2 (exclusive)
 * $..book[-2:]	Last two books
 * $..book[2:]	Book number two from tail
 * $..book[?(@.isbn)]	All books with an ISBN number
 * $.store.book[?(@.price < 10)]	All books in store cheaper than 10
 * $..book[?(@.price <= $['expensive'])]	All books in store that are not "expensive"
 * $..book[?(@.author =~ /.*REES/i)]	All books matching regex (ignore case)
 * $..*	Give me every thing
 * $..book.length()	The number of books
 *
 *
 * min()	Provides the min value of an array of numbers	Double
 * max()	Provides the max value of an array of numbers	Double
 * avg()	Provides the average value of an array of numbers	Double
 * stddev()	Provides the standard deviation value of an array of numbers	Double
 * length()	Provides the length of an array	Integer
 * sum()	Provides the sum value of an array of numbers	Double
 * min()	最小值	Double
 * max()	最大值	Double
 * avg()	平均值	Double
 * stddev()	标准差	Double
 * length()	数组长度	Integer
 * sum()	数组之和	Double
 * ==	left is equal to right (note that 1 is not equal to '1')
 * !=	left is not equal to right
 * <	left is less than right
 * <=	left is less or equal to right
 * >	left is greater than right
 * >=	left is greater than or equal to right
 * =~	left matches regular expression [?(@.name =~ /foo.*?/i)]
 * in	left exists in right [?(@.size in ['S', 'M'])]
 * nin	left does not exists in right
 * subsetof	子集 [?(@.sizes subsetof ['S', 'M', 'L'])]
 * anyof	left has an intersection with right [?(@.sizes anyof ['M', 'L'])]
 * noneof	left has no intersection with right [?(@.sizes noneof ['M', 'L'])]
 * size	size of left (array or string) should match right
 * empty	left (array or string) should be empty
 */
class JsonUtil extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class)

    /**
     * 用户构建对象,获取verify对象
     */
    private JSONObject json

    private JsonUtil(JSONObject json) {
        this.json = json
    }

    static JsonUtil getInstance(JSONObject json) {
        new JsonUtil(json)
    }

    Verify getVerify(String path) {
        Verify.getInstance(this.json, path)
    }

    /**
     * 获取string对象
     * @param path
     * @return
     */
    String getString(String path) {
        def object = get(path)
        object == null ? EMPTY : object.toString()
    }


    /**
     * 获取int类型
     * @param path
     * @return
     */
    int getInt(String path) {
        changeStringToInt(getString(path))
    }

    /**
     * 获取boolean类型
     * @param path
     * @return
     */
    int getBoolean(String path) {
        changeStringToBoolean(getString(path))
    }

    /**
     * 获取long类型
     * @param path
     * @return
     */
    int getLong(String path) {
        changeStringToLong(getString(path))
    }
    
    /**
     * 获取double类型
     * @param path
     * @return
     */
    double getDouble(String path) {
        changeStringToDouble(getString(path))
    }

    /**
     * 获取list对象
     * @param path
     * @return
     */
    List getList(String path) {
        get(path) as List
    }

    /**
     * 获取匹配对象,类型传参
     * 这里不加public  IDE会报错
     * @param path
     * @param tClass
     * @return
     */
    public <T> T getT(String path, Class<T> tClass) {
        try {
            get(path) as T
        } catch (ClassCastException e) {
            logger.warn("类型转换失败!", e)
            null
        }
    }

    /**
     * 获取匹配对象
     * @param path
     * @return
     */
    Object get(String path) {
        logger.debug("匹配对象:{},表达式:{}", json.toString(), path)
        if (json == null || json.isEmpty()) ParamException.fail("json为空或者null,参数错误!")
        try {
            JsonPath.read(this.json, path)
        } catch (JsonPathException e) {
            logger.warn("jsonpath:{}解析失败,json值", json.toString(), path, e)
            null
        }
    }


}

```


--- 
* 公众号**FunTester**首发，更多原创文章：[460+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [开源测试服务](https://mp.weixin.qq.com/s/ZOs0cp_vt6_iiundHaKk4g)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)
- [测试开发工程师工作技巧](https://mp.weixin.qq.com/s/TvrUCisja5Zbq-NIwy_2fQ)
- [Fiddler Everywhere工具答疑](https://mp.weixin.qq.com/s/2peWMJ-rgDlVjs3STNeS1Q)
- [Selenium4 IDE，它终于来了](https://mp.weixin.qq.com/s/XNotlZvFpmBmBQy1pYifOw)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)