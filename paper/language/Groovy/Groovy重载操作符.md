# Groovy重载操作符

> 重载一时爽，一直重载一直爽。

最近在读《Groovy in action》一本书重新复习了Groovy的一些语法特性，迷恋上这个重载操作符的功能，爽的不要要的。分享一个Demo。

由于Groovy语法跟Java差别略大但又基本完全兼容Java语法，这个Demo依然以Java语法写出来，方便大家理解。


```java
package com.FunTester.demo

import com.fun.frame.SourceCode

class demo1 extends SourceCode {

    public static void main(String[] args) {
       def s = "fun" * 3 << "fan"

        println s

        Demo demo = new Demo()
        Demo a = new Demo()

        Demo demo1 = demo + a

        Demo sssss = demo + 3

        Demo fsa = demo * 3

        Demo demo2 = demo / 9

        Demo demo3 = demo << a

        Demo demo4 = demo >> a

        Demo demo5 = demo++


        def i = 2 >>> 1

    }

    static class Demo {

        def plus(Demo demo) {
            output("加法对象")
            this
        }

        def plus(int s) {
            output("加法")
            this
        }

        def multiply(int a) {
            output("乘法")
            this
        }

        def div(int a) {
            output("除法")
            this
        }

        def leftShift(Demo demo) {
            output("<<操作")
            this
        }

        def rightShift(Demo demo) {
            output(">>操作")
            this
        }

        def next() {
            output("++操作")
            this
        }
    }
}

```

控制台输出

![](http://pic.automancloud.com/QQ20200106-171035.png)

惊不惊喜意不意外！

下面结合性能测试框架的thread类写一个：

```
 RequestThreadTimes requestThreadTimes = new RequestThreadTimes(FanLibrary.getHttpGet(""), 100);
        List<RequestThreadTimes> threads = requestThreadTimes * 100;
        new Concurrent(threads).start()
```

乘法重载如下：

```
    /**
     * 乘法
     *
     * @param i
     * @return
     */
    public List<RequestThreadTimes> multiply(int i) {
        ArrayList<RequestThreadTimes> threads = new ArrayList<>(i);
        i.times {
            threads << this.clone();
        }
        threads
    }
```

哈，哈，哈！

* 还有一个大秘密：Groovy连操作符“.”也能重写。


---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [Selenium 4.0 Alpha更新日志](https://mp.weixin.qq.com/s/tU7sm-pcbpRNwDU9D3OVTQ)
- [Selenium 4.0 Alpha更新实践](https://mp.weixin.qq.com/s/yT9wpO5o5aWBUus494TIHw)
- [如何统一接口测试的功能、自动化和性能测试用例](https://mp.weixin.qq.com/s/1xqtXNVw7BdUa03nVcsMTg)

## 非技术文章精选

- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)
- [2019年浏览器市场份额排行榜](https://mp.weixin.qq.com/s/4NmJ_ZCPD5UwaRCtaCfjEg)
- [API测试基础](https://mp.weixin.qq.com/s/bkbUEa9CF21xMYSlhPcULw)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)