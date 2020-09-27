# 用Groovy记录JMeter请求和响应


之前写过一些文章讲了Groovy如何在JMeter中协助测试：

- [用Groovy处理JMeter断言和日志](https://mp.weixin.qq.com/s/Q4yPA4p8dZYAARZ60ZDh9w)
- [用Groovy处理JMeter变量](https://mp.weixin.qq.com/s/BxtweLrBUptM8r3LxmeM_Q)
- [用Groovy在JMeter中执行命令行](https://mp.weixin.qq.com/s/VTip7tiLpwBOr1gUoZ0n8A)
- [用Groovy处理JMeter中的请求参数](https://mp.weixin.qq.com/s/9pCUOXWpMwXR5ynvCMYJ7A)

下面分享一下Groovy如何在JMeter文件操作，我选了一个保存超时请求和响应的脚本，抛砖引玉，各位可以依需拓展。如果将这个功能在拓展一些，比如分别记录某些响应错误的请求，超时请求，以及根据业务码不同分别记录请求和响应，对于BUG的追溯将会有很大的帮助。亦可以在请求中进行时间控制，对于某些查询结果响应时间较长的请求，尝试修改类似`pageSize`的参数，或者请求分页列表，`page`递增，遇到没有内容的就回头从`page=1`继续开始等等。

* 首先新建一个简单的线程组和一个简单的请求：

![](http://pic.automancloud.com/1583240306684.jpg)

* 添加JSR223 后置处理程序

![](http://pic.automancloud.com/QQ20200306-201144.png)

脚本内容：


```Groovy
def file = new File("fan.log")
if (!file.exists()) file.createNewFile()
log.info("工作目录地址:" + file.getAbsoluteFile())
def end_time = prev.getEndTime()
def start_time = prev.getStartTime()
def response = prev.getResponseDataAsString()
if(end_time - start_time > 100)
file << "FunTester${System.currentTimeMillis()} 参数${sampler.getArguments()} 响应${response}\n"
log.info("响应时间${end_time - start_time}")
file.eachLine{
    log.info("文件内容:${it}")
}
```

* 控制台输出：

```Java
2020-03-06 20:08:52,898 INFO o.a.j.e.StandardJMeterEngine: Running the test!
2020-03-06 20:08:52,898 INFO o.a.j.s.SampleEvent: List of sample_variables: []
2020-03-06 20:08:52,899 INFO o.a.j.g.u.JMeterMenuBar: setRunning(true, *local*)
2020-03-06 20:08:53,026 INFO o.a.j.e.StandardJMeterEngine: Starting ThreadGroup: 1 : 线程组
2020-03-06 20:08:53,026 INFO o.a.j.e.StandardJMeterEngine: Starting 1 threads for group 线程组.
2020-03-06 20:08:53,026 INFO o.a.j.e.StandardJMeterEngine: Thread will continue on error
2020-03-06 20:08:53,026 INFO o.a.j.t.ThreadGroup: Starting thread group... number=1 threads=1 ramp-up=1 perThread=1000.0 delayedStart=false
2020-03-06 20:08:53,026 INFO o.a.j.t.ThreadGroup: Started thread group number 1
2020-03-06 20:08:53,026 INFO o.a.j.e.StandardJMeterEngine: All thread groups have been started
2020-03-06 20:08:53,027 INFO o.a.j.t.JMeterThread: Thread started: 线程组 1-1
2020-03-06 20:08:53,406 INFO o.a.j.a.J.JSR223 文件处理: 工作目录地址:/Users/fv/Applications/apache-jmeter-5.12/bin/fan.log
2020-03-06 20:08:53,408 INFO o.a.j.a.J.JSR223 文件处理: 响应时间356
2020-03-06 20:08:53,409 INFO o.a.j.a.J.JSR223 文件处理: 文件内容:FunTester1583495038269 响应{"success":1,"gt":"3c73c021ac3bfea7b5df8d461b5573c5","challenge":"90c62da9d345de77d32964fe1be7585f","new_captcha":true}
2020-03-06 20:08:53,410 INFO o.a.j.a.J.JSR223 文件处理: 文件内容:FunTester1583495685724 响应{"success":1,"gt":"3c73c021ac3bfea7b5df8d461b5573c5","challenge":"8bf74ba0a80e40b1c6cef999083283bf","new_captcha":true}
2020-03-06 20:08:53,410 INFO o.a.j.a.J.JSR223 文件处理: 文件内容:FunTester1583495714929 响应{"success":1,"gt":"3c73c021ac3bfea7b5df8d461b5573c5","challenge":"c4a1cb70e26a652e89d016e4616e519a","new_captcha":true}
2020-03-06 20:08:53,410 INFO o.a.j.a.J.JSR223 文件处理: 文件内容:FunTester1583495928753 参数t=FunTester()&s=funt3est1583495928504() 响应{"success":1,"gt":"3c73c021ac3bfea7b5df8d461b5573c5","challenge":"c633d6001a6beb89afb336e24c7d3a78","new_captcha":true}
2020-03-06 20:08:53,410 INFO o.a.j.a.J.JSR223 文件处理: 文件内容:FunTester1583495960480 参数t=FunTester()&s=funt3est1583495960178() 响应{"success":1,"gt":"3c73c021ac3bfea7b5df8d461b5573c5","challenge":"abd36baf721b3f6f8a038357dacfb353","new_captcha":true}
2020-03-06 20:08:53,410 INFO o.a.j.a.J.JSR223 文件处理: 文件内容:FunTester1583496456193 参数t=FunTestppper()&s=funt3est1583496455866() 响应{"success":1,"gt":"3c73c021ac3bfea7b5df8d461b5573c5","challenge":"c225843a7b3f30663173bc70ec7a0033","new_captcha":true}
2020-03-06 20:08:53,410 INFO o.a.j.a.J.JSR223 文件处理: 文件内容:FunTester1583496533406 参数t=FunTester()&s=funt3est1583496533035() 响应{"success":1,"gt":"3c73c021ac3bfea7b5df8d461b5573c5","challenge":"ae7322b76361ea308574c704315872ed","new_captcha":true}
2020-03-06 20:08:53,410 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-1
2020-03-06 20:08:53,410 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-1
2020-03-06 20:08:53,411 INFO o.a.j.e.StandardJMeterEngine: Notifying test listeners of end of test
2020-03-06 20:08:53,411 INFO o.a.j.g.u.JMeterMenuBar: setRunning(false, *local*)

```

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

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDkiawpL3o8umv1EgHOc2OE1iaib1vR7Q6DH1FSpP4HVuibsibicftEqUqfXZpE2FyN7nIPvHwhWQdG6n0g/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)