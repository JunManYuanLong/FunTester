# arthas命令watch观察方法调用（下）

> `arthas`是一个`Java`开源诊断神器。

上期讲过了[arthas命令watch观察方法调用（上）](https://mp.weixin.qq.com/s/6fMKP7H4Q7ll_0v-wyN19g)中官方文档和官方的Demo演示，本期讲一下`watch`命令的实践，主要内容是针对`Demo`里面用到的命令进行演示和一些想法的尝试。其中用到了部分`ognl`的语法，这方面我比较菜，只能照猫画虎，目前来说足够我用了。还有一部分监控方法性能细节的功能将会在接下来`trace`这个命令中讲解。

此方法可以非常好地观察方法的入参返回信息，很适合线上调试，对性能影响偏大，可以错开高峰期进行`debug`。


# arthas命令watch观察方法调用（下）

- [点击观看视频](https://mp.weixin.qq.com/s/-r2kufxdOjRb2TgF2HPskg)

# Demo代码


```Java
package com.fun;

import com.alibaba.fastjson.JSONObject;
import com.fun.frame.SourceCode;
import com.fun.utils.RString;
import org.slf4j.Logger;

public class Fun extends SourceCode {

    int count;

    public static Logger logger = getLogger(Fun.class);

    public static void main(String[] args) {
        Fun fun = new Fun();
        while (true) {
            fun.test();
            sleep(1000);
            output(RString.getStringWithoutNum(12));
            sleep(1000);
            JSONObject json = getJson("242=4324", "3242432=32423", "32432=dsdfdsf");
            json.put("fun", json.clone());
            json.put("fun", json.clone());
            json.put("fun", json.clone());
            json.put("fun", json.clone());
            output(json);
            try {
                fun.fun();
            } catch (Exception e) {

            }
        }


    }

    public void test() {
        count++;
    }

    public String fun() {
        sleep(getRandomInt(100));
        int randomInt = getRandomInt(2);
        if (randomInt == 1) fail();
        return DEFAULT_STRING;
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
- [自动化策略六步走](https://mp.weixin.qq.com/s/He69k8iCKhTKD1j-yV6M5g)
- [功能自动化测试策略](https://mp.weixin.qq.com/s/qHmcblN4cD4JK6jT7oU4fQ)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)