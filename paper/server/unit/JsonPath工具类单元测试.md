# JsonPath工具类单元测试

上期文章讲到[JsonPath工具类封装](https://mp.weixin.qq.com/s/KyuCuG5fVEExxBdGJO2LdA)，遗留了一个坑，就是关于工具类的[单元测试](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319036461240500224&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)，由于中午得空，所以使用[单元测试框架Spock](https://mp.weixin.qq.com/s/kL5keijAAZwmq_DO1NDBtw)写了一点点单元测试用例，分享出来，供大家参考。

* 使用`Groovy`语言，`spock`测试框架，如需了解请参考文章：

- [Maven和Gradle中配置单元测试框架Spock](https://mp.weixin.qq.com/s/kL5keijAAZwmq_DO1NDBtw)
- [Groovy单元测试框架spock基础功能Demo](https://mp.weixin.qq.com/s/fQCyIyeQANbu2YP2ML6_8Q)
- [Groovy单元测试框架spock数据驱动Demo](https://mp.weixin.qq.com/s/uCAB7Mxt1JZW229aKp-uVQ)
- [人生苦短？试试Groovy进行单元测试](https://mp.weixin.qq.com/s/ahyP-YQTzigeq_5N8byC4g)
- [Spock 2.0 M1版本初探](https://mp.weixin.qq.com/s/nyYh2QzER03kIk-w9P9GNw)
- [单元测试框架spock和Mockito应用](https://mp.weixin.qq.com/s/s21Lts1UnG9HwOEVvgj-uw)

* 中间用到了`Groovy`的文本块，有兴趣的可以看看[Java文本块](https://mp.weixin.qq.com/s/GwasvpJsd7uLngvCr6KlQw)。

# 单元测试用例


```Groovy
package com.FunTester.spock.utils_test

import com.alibaba.fastjson.JSON
import com.fun.utils.JsonUtil
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

import static com.fun.frame.SourceCode.getLogger

public class JsonUtilTest extends Specification {

    @Shared
    Logger logger = getLogger(this.getClass().getName())

    @Shared
    AtomicInteger times = new AtomicInteger(1)

    @Shared
    JsonUtil json = JsonUtil.getInstance(JSON.parseObject("{" +
            "    \"store\": {" +
            "        \"book\": [" +
            "            {" +
            "                \"category\": \"reference\"," +
            "                \"author\": \"Nigel Rees\"," +
            "                \"title\": \"Sayings of the Century\"," +
            "                \"page\": \"D\"," +
            "                \"pages\": [\"S\",\"X\",\"G\"]," +
            "                \"price\": 8.95" +
            "            }," +
            "            {" +
            "                \"category\": \"fiction\"," +
            "                \"author\": \"Evelyn Waugh\"," +
            "                \"title\": \"Sword of Honour\"," +
            "                \"page\": \"A\"," +
            "                \"pages\": [\"A\",\"B\"]," +
            "                \"price\": 12.99" +
            "            }," +
            "            {" +
            "                \"category\": \"fiction\"," +
            "                \"author\": \"Herman Melville\"," +
            "                \"title\": \"Moby Dick\"," +
            "                \"isbn\": \"0-553-21311-3\"," +
            "                \"page\": \"B\"," +
            "                \"pages\": [\"E\",\"F\"]," +
            "                \"price\": 8.99" +
            "            }," +
            "            {" +
            "                \"category\": \"fiction\"," +
            "                \"author\": \"J. R. R. Tolkien\"," +
            "                \"title\": \"The Lord of the Rings\"," +
            "                \"isbn\": \"0-395-19395-8\"," +
            "                \"page\": \"C\"," +
            "                \"pages\": [\"C\",\"D\"]," +
            "                \"price\": 22.99" +
            "            }" +
            "        ]," +
            "        \"bicycle\": {" +
            "            \"color\": \"red\"," +
            "            \"price\": 19.95" +
            "        }" +
            "    }," +
            "    \"expensive\": 10," +
            "    \"ss\": [32,32,4,23]" +
            "}"))

    def setupSpec() {
        logger.info "测试类开始! ${logger.getName()}"
    }

    def setup() {
        logger.info "第 ${times.get()} 次测试结束!"
    }

    def cleanup() {
        logger.info "第 ${times.getAndIncrement()} 次测试结束!"
    }

    def cleanupSpec() {
        logger.info "测试类结束! ${logger.getName()}"
    }

    def "验证取值效果"() {

        expect:
        value as String == json.getString(path)

        where:
        value                           | path
        10                              | "\$.expensive"
        "Sword of Honour"               | "\$.store.book[1].title"
        "0-395-19395-8"                 | "\$.store.book[3].isbn"
        19.95                           | "\$.store.bicycle.price"
        "[19.95,8.95,12.99,8.99,22.99]" | "\$..price"
        "[]"                            | "\$..fdsss"
        ""                              | "\$.fdsss"

    }

    def "验证数组相关功能"() {
        expect:
        value as String == json.getString(path)

        where:
        value           | path
        """["S","X"]""" | "\$..book[0].pages[0,1]"
        """["G"]"""     | "\$..book[0].pages[-1]"
        """["C"]"""     | "\$..book[?(@.price == 22.99)].page"
        """["C"]"""     | "\$..book[?(@.price in [22.99])].page"
        """["D"]"""     | "\$..book[?(@.price nin [22.99,8.99,12.99])].page"
        """["C"]"""     | "\$..book[?(@.title =~ /.*Lord.*/)].page"
        """["D","C"]""" | "\$..book[?(@.title =~ /.*the.*/)].page"
        """["B","C"]""" | "\$..book[?(@.pages subsetof ['D','C','E','F'])].page"
    }

    def "验证处理数组的函数"() {
        expect:
        value == json.getDouble(path)

        where:
        value   | path
        91      | "\$.ss.sum()"
        4       | "\$.ss.min()"
        32      | "\$.ss.max()"
        22.75   | "\$.ss.avg()"
        11.4318 | "\$.ss.stddev()"
        4       | "\$.ss.length()"

    }

}

```

* 最后一个用例里面，我特意留了一个**BUG**，就是在计算标准差的时候，我省去了后面的几位数字，导致一个用例失败。


# 控制台输出

* 这里只放了最后一个方法的输出，其他的都是成功的，所以就省去了。

```shell
INFO-> 第 16 次测试结束!
INFO-> 第 16 次测试结束!
INFO-> 第 17 次测试结束!
INFO-> 第 17 次测试结束!
INFO-> 第 18 次测试结束!
INFO-> 第 18 次测试结束!
INFO-> 第 19 次测试结束!
INFO-> 第 19 次测试结束!
INFO-> 第 20 次测试结束!
INFO-> 第 20 次测试结束!
INFO-> 第 21 次测试结束!
INFO-> 第 21 次测试结束!

Condition not satisfied:

value == json.getDouble(path)
|     |  |    |         |
|     |  |    |         $.ss.stddev()
|     |  |    11.431863365173676
|     |  <com.fun.utils.JsonUtil@192d74fb json=[ss:[32, 32, 4, 23], store:[bicycle:[color:red, price:19.95], book:[[pages:[S, X, G], author:Nigel Rees, price:8.95, page:D, category:reference, title:Sayings of the Century], [pages:[A, B], author:Evelyn Waugh, price:12.99, page:A, category:fiction, title:Sword of Honour], [pages:[E, F], author:Herman Melville, price:8.99, isbn:0-553-21311-3, page:B, category:fiction, title:Moby Dick], [pages:[C, D], author:J. R. R. Tolkien, price:22.99, isbn:0-395-19395-8, page:C, category:fiction, title:The Lord of the Rings]]], expensive:10]>
|     false
11.4318

<Click to see difference>


	at com.FunTester.spock.utils_test.JsonUtilTest.验证处理数组的函数(JsonUtilTest.groovy:120)


```


--- 
* 公众号**FunTester**首发，原创分享爱好者，腾讯云和掘金社区首页推荐，知乎七级原创作者，[更多原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

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