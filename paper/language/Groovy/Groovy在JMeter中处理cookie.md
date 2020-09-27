# Groovy在JMeter中处理cookie

突然发现JMeter系列写了不少文章，干脆整个全套的，把剩下的Demo也发一下，旧文如下：

- [用Groovy处理JMeter断言和日志](https://mp.weixin.qq.com/s/Q4yPA4p8dZYAARZ60ZDh9w)
- [用Groovy处理JMeter变量](https://mp.weixin.qq.com/s/BxtweLrBUptM8r3LxmeM_Q)
- [用Groovy在JMeter中执行命令行](https://mp.weixin.qq.com/s/VTip7tiLpwBOr1gUoZ0n8A)
- [用Groovy处理JMeter中的请求参数](https://mp.weixin.qq.com/s/9pCUOXWpMwXR5ynvCMYJ7A)
- [用Groovy在JMeter中使用正则提取赋值](https://mp.weixin.qq.com/s/9riPpnQZCfKGscuzOOpYmQ)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)

下面讲讲JMeter如何处理`cookie`，这里先讲一个事情，`cookie`只是HTTP请求`header`里面的一个字段，但是在JMeter里面是分开处理的，`HTTP信息头管理器`和`HTTP Cookie管理器`完全就是两个对象，分工不重复，在源码里面使用的是`HeaderManager`和`CookieManager`两个类。

首先讲一讲`CookieManager`的基本使用，添加`cookie`，获取`cookie`，修改`cookie`。

* 首先新建一个简单的线程组和一个简单的请求：

![](http://pic.automancloud.com/1583240306684.jpg)

* 然后创建一个`HTTP Cookie管理器`

![](http://pic.automancloud.com/QQ20200319-210157.png)

* 添加JSR223 预处理程序（后置处理程序需要下一次次请求）

![](http://pic.automancloud.com/QQ20200303-210125.png)

脚本内容：


```Groovy
import org.apache.jmeter.protocol.http.control.*

//Get cookie manager
import org.apache.jmeter.protocol.http.control.*

CookieManager cm = sampler.getCookieManager()
def a = new Cookie("FunTester", "FunTester323323", "account.cnblogs.com", "/", false, 1557598515)
cm.add(a)
Cookie c = cm.get(0)
log.info("-------Cookies : " + c.getName() + "----------- " + c.getValue())
c.setValue("FunTester")
log.info("-------Cookies : " + c.getName() + "----------- " + c.getValue())
```

这里注意一点，cookie有一个空参的构造方法，经过我验证，只设置`name`和`value`是不能被正确携带到请求里面去，必需把`domian`和`path`以及后面两个鬼参数都设置。

* 控制台输出：

```Java
2020-03-19 21:04:35,634 INFO o.a.j.e.StandardJMeterEngine: Running the test!
2020-03-19 21:04:35,635 INFO o.a.j.s.SampleEvent: List of sample_variables: []
2020-03-19 21:04:35,636 INFO o.a.j.g.u.JMeterMenuBar: setRunning(true, *local*)
2020-03-19 21:04:35,949 INFO o.a.j.e.StandardJMeterEngine: Starting ThreadGroup: 1 : 线程组
2020-03-19 21:04:35,949 INFO o.a.j.e.StandardJMeterEngine: Starting 1 threads for group 线程组.
2020-03-19 21:04:35,949 INFO o.a.j.e.StandardJMeterEngine: Thread will continue on error
2020-03-19 21:04:35,950 INFO o.a.j.t.ThreadGroup: Starting thread group... number=1 threads=1 ramp-up=0 perThread=0.0 delayedStart=false
2020-03-19 21:04:35,950 INFO o.a.j.t.ThreadGroup: Started thread group number 1
2020-03-19 21:04:35,950 INFO o.a.j.e.StandardJMeterEngine: All thread groups have been started
2020-03-19 21:04:35,951 INFO o.a.j.t.JMeterThread: Thread started: 线程组 1-1
2020-03-19 21:04:36,026 INFO o.a.j.m.J.处理cookie: -------Cookies : fds----------- 32423
2020-03-19 21:04:36,026 INFO o.a.j.m.J.处理cookie: -------Cookies : fds----------- FunTester
2020-03-19 21:04:36,302 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-1
2020-03-19 21:04:36,302 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-1
2020-03-19 21:04:36,303 INFO o.a.j.e.StandardJMeterEngine: Notifying test listeners of end of test
2020-03-19 21:04:36,303 INFO o.a.j.g.u.JMeterMenuBar: setRunning(false, *local*)

```

* 查看结果树

![](http://pic.automancloud.com/QQ20200320-091015.png)


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
- [自动化测试项目为何失败](https://mp.weixin.qq.com/s/KFJXuLjjs1hii47C1BH8PA)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)