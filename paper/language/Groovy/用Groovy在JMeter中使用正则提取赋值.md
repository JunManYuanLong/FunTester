# 用Groovy在JMeter中使用正则提取赋值

[原文地址](https://www.blazemeter.com/blog/using-regular-expressions-and-groovy-for-testing-in-jmeter/)

之前写过一些文章讲了Groovy如何在JMeter中协助测试：

- [用Groovy处理JMeter断言和日志](https://mp.weixin.qq.com/s/Q4yPA4p8dZYAARZ60ZDh9w)
- [用Groovy处理JMeter变量](https://mp.weixin.qq.com/s/BxtweLrBUptM8r3LxmeM_Q)
- [用Groovy在JMeter中执行命令行](https://mp.weixin.qq.com/s/VTip7tiLpwBOr1gUoZ0n8A)
- [用Groovy处理JMeter中的请求参数](https://mp.weixin.qq.com/s/9pCUOXWpMwXR5ynvCMYJ7A)
- [Java和Groovy正则使用](https://mp.weixin.qq.com/s/DT3BKE3ZcCKf6TLzGc5wbg)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)

​这次来看看Groovy正则表达式在JMeter中的应用​。

[正则表达式](https://mp.weixin.qq.com/s/DT3BKE3ZcCKf6TLzGc5wbg)是特殊的文本字符串，用作查找与之匹配的其他字符串的模板。它们是从字符串中检索数据（子字符串）的非常强大的机制。在Apache JMeter™中，可以从内置组件正则表达式提取器中使用正则表达式，也可以用Groovy编写它们。

将正则表达式与Groovy一起使用可提供更大的灵活性并节省时间。例如，如果您需要提取几个不同的参数，则可以只编写一个脚本，而不是为每个请求添加一个正则表达式提取器。

在本文中，我将向您展示当使用JMeter对API响应进行性能测试时，如何在Groovy中使用正则表达式。


* 首先新建一个简单的线程组和一个简单的请求：

![](http://pic.automancloud.com/1583240306684.jpg)

* 添加JSR223 后置处理程序

![](http://pic.automancloud.com/QQ20200306-201144.png)


这里先看一下接口的响应，如下：


```json
{
    "success": 1, 
    "gt": "3c73c021ac3bfea7b5df8d461b5573c5", 
    "challenge": "60e86734e0dfb7db48c5661ff9c5c935", 
    "new_captcha": true
}
```

这里我的需求是获取`challenge`这个字段的值，当然这个需要用解析json的方式更好，具体参考文章：[用Groovy处理JMeter断言和日志](https://mp.weixin.qq.com/s/Q4yPA4p8dZYAARZ60ZDh9w)。本期我采用正则提取的方式进行提取，并赋值到某个线程私有变量中，赋值变量部分可以参考文章：[用Groovy处理JMeter变量](https://mp.weixin.qq.com/s/BxtweLrBUptM8r3LxmeM_Q)。

脚本如下：


```Groovy
def response = prev.getResponseDataAsString()
log.info("响应内容:"+ response)
def re = response =~ /challenge":.+?"/
log.info("提取结果:"+ re[0])
def a = re[0] - "challenge\":\"" - "\""

vars.put("MY1",a)

log.warn("修改后的MY1参数是:${vars.get("MY1")}")
```

控制台输出：


```shell
2020-03-08 17:45:20,035 INFO o.a.j.e.StandardJMeterEngine: Running the test!
2020-03-08 17:45:20,038 INFO o.a.j.s.SampleEvent: List of sample_variables: []
2020-03-08 17:45:20,040 INFO o.a.j.g.u.JMeterMenuBar: setRunning(true, *local*)
2020-03-08 17:45:20,233 INFO o.a.j.e.StandardJMeterEngine: Starting ThreadGroup: 1 : 线程组
2020-03-08 17:45:20,233 INFO o.a.j.e.StandardJMeterEngine: Starting 1 threads for group 线程组.
2020-03-08 17:45:20,233 INFO o.a.j.e.StandardJMeterEngine: Thread will continue on error
2020-03-08 17:45:20,233 INFO o.a.j.t.ThreadGroup: Starting thread group... number=1 threads=1 ramp-up=1 perThread=1000.0 delayedStart=false
2020-03-08 17:45:20,233 INFO o.a.j.t.ThreadGroup: Started thread group number 1
2020-03-08 17:45:20,233 INFO o.a.j.e.StandardJMeterEngine: All thread groups have been started
2020-03-08 17:45:20,234 INFO o.a.j.t.JMeterThread: Thread started: 线程组 1-1
2020-03-08 17:45:20,495 INFO o.a.j.a.J.JSR223 正则提取: 响应内容:{"success":1,"gt":"3c73c021ac3bfea7b5df8d461b5573c5","challenge":"b001a7188421320f77fb4631dbd72116","new_captcha":true}
2020-03-08 17:45:20,495 INFO o.a.j.a.J.JSR223 正则提取: 提取结果:challenge":"b001a7188421320f77fb4631dbd72116"
2020-03-08 17:45:20,496 WARN o.a.j.a.J.JSR223 正则提取: 修改后的MY1参数是:b001a7188421320f77fb4631dbd72116
2020-03-08 17:45:20,496 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-1
2020-03-08 17:45:20,496 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-1
2020-03-08 17:45:20,496 INFO o.a.j.e.StandardJMeterEngine: Notifying test listeners of end of test
2020-03-08 17:45:20,497 INFO o.a.j.g.u.JMeterMenuBar: setRunning(false, *local*)

```

然后当前线程下一次请求时，变量`MY1`的值已经变成了我们修改过的内容。


---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [压测中测量异步写入接口的延迟](https://mp.weixin.qq.com/s/odvK1iYgg4eRVtOOPbq15w)
- [多种登录方式定量性能测试方案](https://mp.weixin.qq.com/s/WuZ2h2rr0rNBgEvQVioacA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)

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