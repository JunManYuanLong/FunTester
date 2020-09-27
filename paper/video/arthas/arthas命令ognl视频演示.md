# arthas命令ognl视频演示

> `arthas`是一个`Java`开源诊断神器。

今天分享一个非常重要的命令`ognl`，比较神奇的命令。这个命令很多高级用法，这里就不演示了，后面会慢慢补上，这里只分享一些基础用法。

个人使用过程中主要两个场景：1、查看静态字段的值；2、调用静态方法。

第二个场景的使用更多，调用静态方法，最简单的就是`dump`内存快照、重新初始化配置，也可以调用静态方法去修改一些配置，相比查看静态字段的值，有用很多。

系列文章：

- [arthas快速入门视频演示](https://mp.weixin.qq.com/s/Wl5QMD52isGTRuAP4Cpo-A)
- [arthas进阶thread命令视频演示](https://mp.weixin.qq.com/s/XuF7Nr1sGC3diIn50zlDDQ)
- [arthas命令jvm,sysprop,sysenv,vmoption视频演示](https://mp.weixin.qq.com/s/87BsTYqnTCnVdG3a_kBcng)
- [arthas命令logger动态修改日志级别--视频演示](https://mp.weixin.qq.com/s/w724P9B12eTC9rMbavwsMA)
- [arthas命令sc和sm视频演示](https://mp.weixin.qq.com/s/Ga63sjW_bOKQqfnA5LTb9w)

# arthas命令ognl

- [点击观看视频](https://mp.weixin.qq.com/s/cMCaXFwjp6QHFq40TvP4bQ)

# 代码


```Groovy
package com.fun

import com.fun.frame.SourceCode

class DeleteNull extends SourceCode {

    public static String name = DEFAULT_STRING;

    public int age;

    public DeleteNull(int i) {
        this.age = i
    }

    public static void main(String[] args) {
        int num = 0
        while (true) {
            sleep(5000)
            new DeleteNull(num++)
        }
    }

/**
 * 测试
 * @return FunTester
 */
    static String test() {
        logger.warn(DEFAULT_STRING)
        DEFAULT_STRING
    }


}

```

* 后台回复`arthas`可以观看最近的`arthas`视频教程。

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester原创合集](https://gitee.com/fanapi/tester/blob/okay/document/directory.markdown)**。

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