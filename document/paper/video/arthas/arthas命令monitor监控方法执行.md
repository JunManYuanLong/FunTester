# arthas命令monitor监控方法执行

> `arthas`是一个`Java`开源诊断神器。

今天分享一个非常重要的命令`monitor`，主要作用是监控`Java`方法执行的。

# 监控的维度说明

| 监控项 | 说明 |
| --- | --- |
| timestamp | 时间戳 |
|class| Java类|
|method| 方法（构造方法、普通方法）|
|total |调用次数|
|success| 成功次数|
|fail| 失败次数|
|rt |平均RT|
|fail-rate |失败率|

这里面`fail`的标准是抛出异常，其中`rt`这个参数比较有意义，可以查看一下方法调用的耗时情况，比如不同参数场景下的方法耗时，这个统计仅供参考。原因下面会讲。

方法调用比较简单，只有一个参数`-c`表示统计周期的，默认是`120s`。文档中表示执行只命令会导致`JVM`运行改方法变慢，这里我做了实验，大概会提高`50%`的`rt`，结束`arthas`之后恢复原来的性能。

# arthas命令monitor监控方法执行

- [点击观看视频](https://mp.weixin.qq.com/s/7-oe3UoTY8bzpi89tIKvQQ)

# 代码


```Java
package com.fun;


import com.fun.frame.httpclient.FanLibrary;
import com.fun.utils.Time;

public class AR extends FanLibrary {

    public static void main(String[] args) {

        while (true) {
            sleep(1000);
            long mark = Time.getTimeStamp();
            for (int i = 0; i < 1000; i++) {
                output(DEFAULT_STRING);
            }
            long mark1 = Time.getTimeStamp();
            System.out.println(mark1 - mark);

        }

    }

    static String test() {
        sleep(100);
        return DEFAULT_STRING;
    }

    String fun() {
        sleep(100);
        int randomInt = getRandomInt(2);
        if (randomInt == 1) fail();
        return DEFAULT_STRING;
    }


}

```

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester原创合集](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**，合作请联系`Fhaohaizi@163.com`。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [接口测试视频专题](https://mp.weixin.qq.com/s/4mKpW3QiVRee3kcVOSraog)
- [删除List中null的N种方法--最后放大招](https://mp.weixin.qq.com/s/4mfskN781dybyL59dbSbeQ)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [自动化策略六步走](https://mp.weixin.qq.com/s/He69k8iCKhTKD1j-yV6M5g)
- [功能自动化测试策略](https://mp.weixin.qq.com/s/qHmcblN4cD4JK6jT7oU4fQ)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)