# arthas命令tt方法时空隧道

> `arthas`是一个`Java`开源诊断神器。

今天分享一个非常重要的命令`tt`，全称是`TimeTunnel`，记录下指定方法每次调用的入参和返回信息，并能对这些不同的时间下调用进行观测。这个命令与之前讲到的[arthas命令watch观察方法调用（上）](https://mp.weixin.qq.com/s/6fMKP7H4Q7ll_0v-wyN19g)、[arthas命令watch观察方法调用（下）](https://mp.weixin.qq.com/s/-r2kufxdOjRb2TgF2HPskg)从大概功能上比较相似，区别在于`watch`如果想发挥足够的排查作用，必需熟练掌握`ognl`语法，特别是高级语法这样才能从大量的请求中筛选出来自己想要的，`tt`命令相对简单，而且还支持`录制、重放`功能，可以说非常强大。在[arthas](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1318979101297950721&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)运行的过程中，经过`tt`命令保存的方法都是可以重放的。

# 字段说明

|表格字段	|字段解释|
|----|-----|
|INDEX	|时间片段记录编号|
|TIMESTAMP	|方法执行的本机时间|
|COST(ms)	|方法执行的耗时|
|IS-RET	|方法是否以正常返回的形式结束|
|IS-EXP	|方法是否以抛异常的形式结束|
|OBJECT	|执行对象的hashCode()|
|CLASS	|执行的类名|
|METHOD	|执行的方法名|

* 这里有个问题视频中未讲明白，`hashcode()`方法得到的是对象在`JVM`中内存地址的映射。

# arthas命令tt方法时空隧道

- [点击观看视频](https://mp.weixin.qq.com/s/mDczYmVdSmL5ZbK7bb8i0A)

# Demo代码


```Java
package com.fun;


import com.alibaba.fastjson.JSONObject;
import com.fun.frame.httpclient.FanLibrary;
import org.apache.http.client.methods.HttpGet;

import java.util.Arrays;
import java.util.List;

public class AR extends FanLibrary {

    public static int times = 0;

    public static void main(String[] args) {

        while (true) {
            String url = "https://api.apiopen.top/getAllUrl";
            HttpGet get = getHttpGet(url);
            JSONObject response = getHttpResponse(get);
//            output(response);
            output(test());
            sleep(5000);
        }

    }

    static String test() {
        times++;
        for (int i = 0; i < 5; i++) {
            getRandomInt(100);
        }
        List list = Arrays.asList(32, 432, 432, 423, 423, 32);
        list.stream().forEach(x -> aaa(x));
        return DEFAULT_STRING + times;
    }

    static Integer aaa(Object a) {
        sleep(100);
        return 915 * 516;
    }


}
```


---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester十八张原创专辑](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**，合作请联系`Fhaohaizi@163.com`。

### 热文精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [删除List中null的N种方法--最后放大招](https://mp.weixin.qq.com/s/4mfskN781dybyL59dbSbeQ)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [Selenium并行测试基础](https://mp.weixin.qq.com/s/OfXipd7YtqL2AdGAQ5cIMw)
- [Selenium并行测试最佳实践](https://mp.weixin.qq.com/s/-RsQZaT5pH8DHPvm0L8Hjw)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)