# 用Groovy处理JMeter变量


本来没打算写这个系列的，又看了看JMeter的文档，发现Groovy在JMeter中能发挥很多非常有趣的功能，虽然语法和API比较隐晦，但好得不多。

前面已经写过文章介绍了Groovy断言：[用Groovy处理JMeter断言和日志](https://mp.weixin.qq.com/s/Q4yPA4p8dZYAARZ60ZDh9w)。

下面介绍下一个题目：Groovy处理JMeter变量，这里的变量分为线程私有（局部）和线程共享（全局）。

## 线程私有

* 首先新建一个简单的线程组和一个简单的请求：

![](http://pic.automancloud.com/1583240306684.jpg)

**请注意参数`t`去的是局部变量`MY1`的值。**


* 添加JSR223 预处理程序（后置处理程序需要下一次次请求）

![](http://pic.automancloud.com/QQ20200303-210125.png)


脚本内容如下：


```Groovy
OUT. println 'FunTester'
log.error '输出JMeter控制台错误'
vars.put("MY1","FunTester")
def my_var = vars.get("MY");
log.warn "输出参数-------- ${vars} console"
log.info("222222 " + my_var);
```

* 控制台输出


```Java
2020-03-03 21:02:30,499 INFO o.a.j.t.JMeterThread: Thread started: 线程组 1-1
2020-03-03 21:02:30,517 ERROR o.a.j.m.J.JSR223 预处理程序: 输出JMeter控制台错误
2020-03-03 21:02:30,518 WARN o.a.j.m.J.JSR223 预处理程序: 输出参数-------- org.apache.jmeter.threads.JMeterVariables@14a52b54 console
2020-03-03 21:02:30,518 INFO o.a.j.m.J.JSR223 预处理程序: 222222 FunTester
```

* 查看结果树

![](http://pic.automancloud.com/QQ20200303-210350.png)

至此已经完美搞定私有变量。

## 线程共享

线程共享变量的处理方法跟上一个基本一模一样，除了对象名和方法名意外。

脚本内容如下：


```Groovy
OUT. println 'FunTester'
log.error '输出JMeter控制台错误'
vars.put("MY1","FunTester")
def my_var = vars.get("MY1");
log.warn "输出参数-------- ${vars} console"
log.info("222222 " + my_var);


props.put("MY",test())
log.info(props.get("MY"))

def test(){
	"funtest" + fan()
}

def fan(){
	System.currentTimeMillis()
}

```

**这里我稍微写了两个方法，验证了一下Groovy的方法是否可行**

* 参数设置

![](http://pic.automancloud.com/QQ20200303-210651.png)

* 查看结果树

![](http://pic.automancloud.com/QQ20200303-210809.png)

Groovy如何处理JMeter的变量的Demo到此结束了。


---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [Java并发BUG基础篇](https://mp.weixin.qq.com/s/NR4vYx81HtgAEqH2Q93k2Q)
- [Java并发BUG提升篇](https://mp.weixin.qq.com/s/GCRRe8hJpe1QJtxq9VBEhg)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [压测中测量异步写入接口的延迟](https://mp.weixin.qq.com/s/odvK1iYgg4eRVtOOPbq15w)
- [多种登录方式定量性能测试方案](https://mp.weixin.qq.com/s/WuZ2h2rr0rNBgEvQVioacA)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [左移测试](https://mp.weixin.qq.com/s/8zXkWV4ils17hUqlXIpXSw)
- [选择手动测试还是自动化测试？](https://mp.weixin.qq.com/s/4haRrfSIp5Plgm_GN98lRA)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1)