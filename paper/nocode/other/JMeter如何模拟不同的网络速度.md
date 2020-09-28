# JMeter如何模拟不同的网络速度



* **如何以不同的网络连接速度测试移动应用程序和网站？**

在大多数情况下，移动设备用户通过其蜂窝运营商网络访问互联网。覆盖范围将根据其位置而有所不同，这意味着连接速度将有所不同。确保您的网站或应用程序能够完全处理移动设备和平板电脑，即使它们具有不同的互联网连接速度，也至关重要。

在今天的文章中，将展示如何通过在`JMeter`负载测试中控制模拟虚拟用户的带宽来做到这一点。

默认情况下，JMeter将尽快发送其采样器定义的请求。这对于产生负载非常有用，但不是很现实，因为实际用户不会不停地访问服务器，因此他们需要一些时间在两次操作之间进行人生思考。最重要的是，移动用户受到网络带宽的限制，这可能会进一步降低他们的速度。

# 限制输出带宽以模拟不同的网络速度

`JMeter`确实提供了限制输出带宽以模拟不同网络速度的选项。可以通过以下两个属性来控制带宽：

```Java
httpclient.socket.http.cps = 0

httpclient.socket.https.cps = 0
```

这些分别用于`HTTP`和HTTPS协议，[图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)。该属性默认为零，这意味着没有限制。首字母缩写`cps`代表“每秒字符数”。当您将属性设置为零以上时，带宽将根据您的设置进行调节。

这是计算“cps”的公式：

**`cps=（目标带宽（以kbps为单位*1024）/8`**

例如：为了模拟`GPRS`蜂窝网络速度（下行速度为171Kbits/秒），相关的`CPS`值为：`21888（171*1024/8）`

因此，这是通过这些属性限制带宽的方法：

# 将这两行添加到`user.properties`文件中（可以在`JMeter`安装的`bin`文件夹中找到此行）

```Java
httpclient.socket.http.cps = 21888
httpclient.socket.https.cps = 21888
```

* 重新启动JMeter来使配置生效

# 通过-J命令行参数传递属性的值，如下所示：

`jmeter -Jhttpclient.socket.http.cps=21888 -Jhttpclient.socket.https.cps=21888 -t /path/to/your/testplan.jmx`

* 以下是一些流行的带宽预设：
 
|带宽 |cps值|
|---|---|                          
|GPRS|	21888|
|3G	|2688000|
|4G|	19200000|
|WIFI 802.11a/g	|6912000|
|ADSL|	1024000|
|100 Mb局域网 |	12800000 |
|千兆网卡	|128000000|

## JMeter专题：

- [用Groovy处理JMeter断言和日志](https://mp.weixin.qq.com/s/Q4yPA4p8dZYAARZ60ZDh9w)
- [用Groovy处理JMeter变量](https://mp.weixin.qq.com/s/BxtweLrBUptM8r3LxmeM_Q)
- [用Groovy在JMeter中执行命令行](https://mp.weixin.qq.com/s/VTip7tiLpwBOr1gUoZ0n8A)
- [用Groovy处理JMeter中的请求参数](https://mp.weixin.qq.com/s/9pCUOXWpMwXR5ynvCMYJ7A)
- [用Groovy在JMeter中使用正则提取赋值](https://mp.weixin.qq.com/s/9riPpnQZCfKGscuzOOpYmQ)
- [Groovy在JMeter中处理cookie](https://mp.weixin.qq.com/s/DCnDjWaj2aiKv5HVw3-n6A)
- [Groovy在JMeter中处理header](https://mp.weixin.qq.com/s/juY-1jEWODJ5HHiEsxhIEw)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)

---
* **郑重声明**：公众号“FunTester”首发，欢迎关注交流，禁止第三方转载。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [压测中测量异步写入接口的延迟](https://mp.weixin.qq.com/s/odvK1iYgg4eRVtOOPbq15w)
- [多种登录方式定量性能测试方案](https://mp.weixin.qq.com/s/WuZ2h2rr0rNBgEvQVioacA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [多项目登录互踢测试用例](https://mp.weixin.qq.com/s/Nn_CUy_j7j6bUwHSkO0pCQ)
- [从Java到Groovy的八级进化论](https://mp.weixin.qq.com/s/QTrRHsD3w-zLGbn79y8yUg)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [简化测试用例](https://mp.weixin.qq.com/s/BhwfDqhN9yoa3Iul_Eu5TA)
- [软件测试中的虚拟化](https://mp.weixin.qq.com/s/zHyJiNFgHIo2ZaPFXsxQMg)
