# 用Groovy处理JMeter断言和日志

## 闲扯一会jmeter和Groovy

首先说明一下，我并不擅长`jmeter`，因为我基本不用这个，关于`jmeter`设置的疑问可以多去搜索引擎求助。本篇文章内容主要是自己在协助同事的时候一点点实践，分享一下`jmeter`除`BeanShell`之外的另外一种脚本语言`Groovy`。本来想弄个断言就好了，没想到日志模块比较简单，也就顺手写个Demo。

查阅完官方文档得出结论，脚本语言如：`Groovy`，在`jmeter`体系中基本说是全能的，例如：处理请求、响应、参数、变量以及收集器和监听器（这些以后有机会再写文章，有需求的请异步官方Demo）。总的来讲，`jmeter`中`Groovy`语法兼容性一般，还是得老老实实按照`Java`的语法来写比较稳妥，主要还是需要用`jmeter`自己的`API`，需要一点时间学习和实践。

## 序言

`Apache JMeter`断言是测试人员能够设置标准的组件，这些标准确定将响应视为“**通过**”还是“**失败**”。您可以使用断言来确保样本和子样本的返回值与预期结果匹配，也可以将其应用于`JMeter`变量。它们将在同一范围内的每个采样器之后执行。

`JMeter`包含许多断言元素，用于验证采样器的响应。

![](http://pic.automancloud.com/QQ20200302-210802.png)

但是，有时需要验证决定可能遵循复杂和高级的逻辑，并且无法使用开箱即用的`JMeter`断言进行配置。例如，确认`JSON`响应的有效性，然后评估响应的值，并具有调试问题的自定义失败消息。

`Groovy`脚本语言非常适合编写简洁且可维护的测试，以及所有构建和自动化任务。`Groovy`与Java和任何第三方库无缝且透明地集成在一起，从而使使用`JMeter`的`Java`开发人员易于使用。与其他可用的脚本语言（例如`BeanShell`）相比，诸如`Power Assertion`之类的功能使`Groovy`中的测试和断言变得轻松简洁。

## 进入正题

> 本Demo使用**jmeter5.12**版本，本人对其他版本不兼容性问题研究甚少。

在以下Demo中，我们要测试服务器响应是否包含结构良好的JSON。我们将通过我们的应用程序服务器运行一个请求，并且我们期望收到一个结构化的JSON，校验结构化的JSON包含固定字段的值。

由于我们知道期望在响应中接收哪些键，以及应该分配给它们的值，因此我们可以使用`Groovy`断言来断言响应。

### 1.简单的开始

该脚本包括一个线程组和一个采样器。

### 2.添加断言

使能够使用Groovy作为脚本语言：JSR223。

右键单击`采样器-`>`添加`->`断言`->`JSR223断言`

![](http://pic.automancloud.com/QQ20200302-210728.png)

### 3.元素配置

设置以下属性：脚本语言：`Groovy 2.xx`，如下图：

![](http://pic.automancloud.com/QQ20200302-211138.png)

JSR223断言字段说明：

* 名称：元素名称
* 语言：要使用的脚本语言（`Groovy`，`BeanShell`，`JS`等）
* 参数：传递给脚本的参数。参数存储在以下变量中：参数，参数
* 文件名：预制脚本文件的路径。将覆盖主脚本字段中编写的所有脚本
* 脚本编译缓存：启用此选项时，`JSR223`断言（或与此相关的任何其他JSR223元素）可以预编译代码并将其缓存。这将大大提高性能。“如果可用”表示仅适用于JSR223兼容脚本。`Java`，`JavaScript`和`Beanshell`可以在`JSR223`断言中使用，但是与`Groovy`相反，它们与`JSR223`的接口不兼容。
* 与`Beanshell`元素相比，脚本编译缓存是使用`JSR223`元素的主要优点。

### 4.编写脚本

脚本内容：

```Groovy
import groovy.json.*

log.info("线程组名字 " + prev.getThreadName())
def end_time = prev.getEndTime()
log.info("结束时间 " + (new Date(end_time).toString()))
log.info("响应结果: " + prev.getTime().toString())
log.info("Connect Time is: " + prev.getConnectTime().toString())
log.info("Latency is: " + prev.getLatency().toString())
log.info("响应大小" + prev.getBytesAsLong().toString())
log.info("请求url " + prev.getURL())
log.info("测试结果是 " + prev.isSuccessful().toString())

def response = prev.getResponseDataAsString()
log.info("响应内容是:" + response)

def json = new JsonSlurper().parseText(response)
log.info("响应code" + json.success)

assert 2 == json.success

log.info("响应头响应行是 " + prev.getResponseHeaders())

```


该脚本除了简单验证了响应结果中`success`字段值意外，还验证了`prev`的几个基本的`API`以及`jmeter`日志使用。其他的常用的`API`以后有机会我会写一点，毕竟我不用 `jmeter`，有需求的移步官方文档和Demo。


该脚本从导入`JSON Slurper`开始。`JSON Slurper`将`JSON`文本或阅读器内容解析为`Groovy`数据结构。这里说明一下，`import`使用的是jmeter自带的`Groovy`库，并不是本地的`Groovy`环境配置下面`libs`里面的库，如果想增加功能，比如去修改`jmeter`里面的`Groovy`依赖，不过我并不建议，太麻烦了。而且自带的已经够用了。

### 5.运行脚本

响应失败：

![](http://pic.automancloud.com/QQ20200302-213227.png)


在此响应中，`json.success`的值为1，而不是2。

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