# 用Groovy处理JMeter中的请求参数

之前写过一些文章讲了Groovy如何在JMeter中协助测试：

- [用Groovy处理JMeter断言和日志](https://mp.weixin.qq.com/s/Q4yPA4p8dZYAARZ60ZDh9w)
- [用Groovy处理JMeter变量](https://mp.weixin.qq.com/s/BxtweLrBUptM8r3LxmeM_Q)
- [用Groovy在JMeter中执行命令行](https://mp.weixin.qq.com/s/VTip7tiLpwBOr1gUoZ0n8A)

下面分享一下Groovy如何在JMeter修改请求参数，这个在正常测试中用处还是很广的，跟设置变量不一样，很多参数可能需要校验签名，而且每一次请求的参数也不尽相同。需要在设置完请求参数后，然后用程序统一处理一下。比如计算参数签名、加密明文、从数据库中取值等等。

* 首先新建一个简单的线程组和一个简单的请求：

![](http://pic.automancloud.com/1583240306684.jpg)

* 添加JSR223 预处理程序（后置处理程序需要下一次请求）

![](http://pic.automancloud.com/QQ20200304-152436.png)

脚本内容：

```Groovy
sampler.addArgument("name","data");
log.info(sampler.getArguments().toString())
```

**经过测试这个方法对于get和post请求均有效，包括post请求的不同参数类型。**

* 控制台输出

这里只发get请求的控制台输出，多余的输出已经删除了。


```Java
2020-03-04 23:24:23,239 INFO o.a.j.e.StandardJMeterEngine: Running the test!
2020-03-04 23:24:23,239 INFO o.a.j.s.SampleEvent: List of sample_variables: []
2020-03-04 23:24:23,240 INFO o.a.j.g.u.JMeterMenuBar: setRunning(true, *local*)
2020-03-04 23:24:23,358 INFO o.a.j.e.StandardJMeterEngine: Starting ThreadGroup: 1 : 线程组
2020-03-04 23:24:23,358 INFO o.a.j.e.StandardJMeterEngine: Starting 1 threads for group 线程组.
2020-03-04 23:24:23,358 INFO o.a.j.e.StandardJMeterEngine: Thread will continue on error
2020-03-04 23:24:23,359 INFO o.a.j.t.ThreadGroup: Starting thread group... number=1 threads=1 ramp-up=1 perThread=1000.0 delayedStart=false
2020-03-04 23:24:23,361 INFO o.a.j.t.ThreadGroup: Started thread group number 1
2020-03-04 23:24:23,361 INFO o.a.j.e.StandardJMeterEngine: All thread groups have been started
2020-03-04 23:24:23,361 INFO o.a.j.t.JMeterThread: Thread started: 线程组 1-1
2020-03-04 23:24:23,456 INFO o.a.j.m.J.JSR223 预处理程序: t=FunTester()&s=funt3est1583335463413()&name=data()
2020-03-04 23:24:23,628 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-1
2020-03-04 23:24:23,630 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-1
2020-03-04 23:24:23,630 INFO o.a.j.e.StandardJMeterEngine: Notifying test listeners of end of test
2020-03-04 23:24:23,631 INFO o.a.j.g.u.JMeterMenuBar: setRunning(false, *local*)

```

* 查看结果树

下面展示三种情况get请求、post请求json参数、post请求表单参数的请求体信息：

![](http://pic.automancloud.com/QQ20200304-232713.png)

![](http://pic.automancloud.com/QQ20200304-232723.png)

![](http://pic.automancloud.com/QQ20200304-232755.png)

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

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCxr0Sa2MXpNKicZE024zJm7vIAFRC09bPV9iaMer9Ncq8xppcYF73sDHbrG2iaBtRqCFibdckDTcojKg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)