# arthas命令redefine实现Java热更新


> `arthas`是一个`Java`开源诊断神器。

今天分享一个非常重要的命令`redefine`，主要作用是加载外部的`.class`文件，用来替换`JVM`已经加载的类，总结起来就是实现了`Java`的热更新。

`redefine`在一下几种情况中会失败：1、增加了`field`；2、增加了`method`；3、替换正在运行的方法。

前两个比较好理解，第三个意思就是这个方法必须结束之后才会被替换，如果有个方法开始运行之后就不会跳出，那么这个方法所在的类是无法被替换的，类似无限循环的方法。

中间提到了将本地的`.class`文件上传到服务器的技巧，个人没有采用，如果是要热更新，完全可以利用`mc`这个命令在服务端编译更新后的代码，然后进行本地替换。

# arthas命令redefine实现Java热更新

- [点击观看视频](https://mp.weixin.qq.com/s/2HUXfJhoUfg4yMzSoRHK9w)

# 代码


```Java
package com.fun;

import com.fun.frame.SourceCode;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Fun extends SourceCode {

    int[] ss = new int[1024];

    public static Logger logger = getLogger(Fun.class);

    public static void main(String[] args) {
        List<Fun> funs = new ArrayList<>();
        while (true) {
            Fun fun = new Fun();
            funs.add(fun);
            sleep(3000);
            test();
            output(funs.size());
        }

    }

    public static void test() {
        logger.info("成功!!!");
    }


}

```

* 调用`test()`方法会打印日志的，如果是该方法被其他地方调用，修改之后也会生效。

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester原创合集](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [接口测试视频专题](https://mp.weixin.qq.com/s/4mKpW3QiVRee3kcVOSraog)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [简化测试用例](https://mp.weixin.qq.com/s/BhwfDqhN9yoa3Iul_Eu5TA)
- [自动化策略六步走](https://mp.weixin.qq.com/s/He69k8iCKhTKD1j-yV6M5g)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)