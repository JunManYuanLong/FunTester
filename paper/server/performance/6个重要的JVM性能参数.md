# 6个重要的JVM性能参数



围绕垃圾收集和内存，您可以将600多个参数传递给`JVM`。如果包括其他方面，则JVM参数总数将很容易超过1000+。任何人都无法消化和理解太多的论据。在本文中，重点介绍了六个重要的`JVM`参数，在`Java性能测试`中起着非常重要的作用。

## -Xmx和-XX：MaxMetaspaceSize

`-Xmx`可能是最重要的`JVM`参数。`-Xmx`定义要分配给应用程序的最大堆大小。。您可以这样定义应用程序的堆大小：`-Xmx2g`。

堆大小在影响应用性能和所需物理硬件需求。这带来了一个问题，我的应用程序正确的堆大小是多少？我应该为应用程序分配大堆大小还是小堆大小？答案是：取决于需求和预算。

**将`-Xms`和`-Xmx`设置为相同值的会提高JVM性能**

元空间是将存储`JVM`的元数据定义（例如类定义，方法定义）的区域。默认情况下，可用于存储此元数据信息的内存量是无限的（即受您的容器或计算机的RAM大小的限制）。您需要使用`-XX：MaxMetaspaceSize`参数来指定可用于存储元数据信息的内存量的上限。

`-XX:MaxMetaspaceSize=256m`

## GC算法

OpenJDK中有7种不同的GC算法：

* Serial GC
* Parallel GC
* Concurrent Mark & Sweep GC
* G1 GC
* Shenandoah GC
* Z GC
* Epsilon GC


如果您未明确指定GC算法，那么JVM将选择默认算法。在Java 8之前，`Parallel GC`是默认的GC算法。从Java 9开始，`G1 GC`是默认的GC算法。

GC算法的选择对于确定应用程序的性能起着至关重要的作用。根据我们的研究，我们正在使用Z GC算法观察到出色的性能结果。如果使用`JVM 11+`，则可以考虑使用`Z GC`算法（即`-XX：+ UseZGC`）。

下表总结了激活每种垃圾收集算法所需传递的JVM参数。


| GC算法 |	JVM参数 |
| ---- | ---- |
|Serial GC	 |	-XX：+ UseSerialGC|
|Parallel GC		|-XX：+ UseParallelGC|
|Concurrent Market & Sweep (CMS) GC |	-XX：+ UseConcMarkSweepGC|
|G1 GC |	-XX：+ UseG1GC|
|Shenandoah GC	 |	-XX：+使用ShenandoahGC|
|Z GC	 |	-XX：+ UseZGC|
|Epsilon GC	| GC	-XX：+ UseEpsilonGC|

## 启用GC日志记录

垃圾收集日志包含有关垃圾收集事件，回收的内存，暂停时间段等信息，可以通过传递以下JVM参数来启用垃圾收集日志：

从JDK 1到JDK 8：

`-XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:{file-path}`

从JDK 9及更高版本开始：

`-Xlog:gc*:file={file-path}`

Demo：

```Java
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:/opt/workspace/myAppgc.log
-Xlog:gc*:file=/opt/workspace/myAppgc.log
```

通常，GC日志用于调整垃圾回收性能。但是，GC日志包含重要的微观指标。这些指标可用于预测应用程序的可用性和性能特征。在本文中将重点介绍一种这样的标尺：GC吞吐量。GC吞吐量是您的应用程序在处理客户交易中花费的时间与它在处理GC活动中花费的时间之比。假设您的应用程序的GC吞吐量为98％，则意味着应用程序将其98％的时间用于处理客户活动，其余2％用于GC活动。

现在，让我们看一个健康的JVM的堆使用情况图：

![](http://pic.automancloud.com/healthy-jvm-heap.png)


您会看到一个完美的锯齿图案。您会注意到，当运行Full GC（红色三角形）时，内存利用率将一直下降到最低。

现在，让我们看一下有问题的JVM的堆使用情况图：

![](http://pic.automancloud.com/sick-jvm-heap-1.png)

您可以注意到，在图表的右端，即使GC反复运行，内存利用率也没有下降。这是一个典型的内存泄漏迹象，表明该应用程序正在存在某种内存问题。

如果您仔细观察一下该图，您会发现重复的完整GC开始在上午8点左右开始。但是，该应用程序仅在上午8:45左右开始获取OutOfMemoryError。到上午8点，该应用程序的GC吞吐量约为99％。但是，在上午8点之后，GC吞吐量开始下降到60％。因为当重复的GC运行时，该应用程序将不会处理任何客户交易，而只会进行GC活动。

## -XX：+ HeapDumpOnOutOfMemoryError，-XX：HeapDumpPath

`OutOfMemoryError`是一个严重的问题，它将影响您的应用程序的可用性和性能。要诊断`OutOfMemoryError`或任何与内存相关的问题，必须在应用程序开始遇到`OutOfMemoryError`的那一刻或一瞬间捕获堆转储。由于我们不知道何时会抛出`OutOfMemoryError`，因此很难在抛出时左右的正确时间手动捕获堆转储。但是，可以通过传递以下JVM参数来自动化捕获堆转储：

`-XX：+ HeapDumpOnOutOfMemoryError和-XX：HeapDumpPath = {HEAP-DUMP-FILE-PATH}`

在`-XX：HeapDumpPath`中，需要指定堆转储所在的文件路径。传递这两个JVM参数时，将在抛出`OutOfMemoryError`时自动捕获堆转储并将其写入定义的文件路径。例：

`-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/crashes/my-heap-dump.hprof`

一旦捕获了堆转储，就可以使用`HeapHero`和`EclipseMAT`之类的工具来分析堆转储。

## -Xss

每个应用程序将具有数十，数百，数千个线程。每个线程都有自己的堆栈。在每个线程的堆栈中，存储以下信息：

* 当前执行的方法/功能
* 原始数据类型
* 变量
* 对象指针
* 返回值。

他们每个都消耗内存。如果它们的使用量超出某个限制，则会引发`StackOverflowError`。可以通过传递-Xss参数来增加线程的堆栈大小限制。例：

`-Xss256k`

如果将此`-Xss`值设置为一个很大的数字，则内存将被阻塞并浪费。假设您将`-Xss`值指定为`2mb`，而只需要`256kb`，那么您将浪费大量的内存。

假设您的应用程序有500个进程，然后`-Xss`值为`2Mb`，则您的线程将消耗`1000Mb`的内存。另一方面，如果您仅将`-Xss`分配为`256kb`，那么您的线程将仅消耗`125Mb`的内存。每个JVM将节省`875Mb`内存。

注意：线程是在堆（即`-Xmx`）之外创建的，因此这`1000Mb`将是您已经分配的-Xmx值的补充。

## -Dsun.net.client.defaultConnectTimeout和-Dsun.net.client.defaultReadTimeout

现代应用程序使用多种协议（即SOAP，REST，HTTP，HTTPS，JDBC，RMI）与远程应用程序连接。有时远程应用程序可能需要很长时间才能做出响应，有时它可能根本不响应。

如果没有正确的超时设置，并且远程应用程序的响应速度不够快，则您的应用程序线程/资源将被卡住。远程应用程序无响应可能会影响您的应用程序的可用性。它可以使您的应用程序停止磨削。为了保护应用程序的高可用性，应配置适当的超时设置。

您可以在JVM级别传递这两个强大的超时网络属性，这些属性可以全局适用于所有使用`java.net.URLConnection`的协议处理程序：

`sun.net.client.defaultConnectTimeout`：指定建立到主机的连接的超时（以毫秒为单位）。例如，对于HTTP连接，它是与HTTP服务器建立连接时的超时。当建立与资源的连接时，`sun.net.client.defaultReadTimeout`指定从输入流读取时的超时（以毫秒为单位）。
例如，如果您要将这些属性设置为2秒：


```Java
-Dsun.net.client.defaultConnectTimeout=2000
-Dsun.net.client.defaultReadTimeout=2000
```
注意，默认情况下，这两个属性的值为-1，这表示未设置超时。

---
* **郑重声明**：文章首发于公众号“FunTester”，欢迎关注交流，禁止第三方转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [压测中测量异步写入接口的延迟](https://mp.weixin.qq.com/s/odvK1iYgg4eRVtOOPbq15w)
- [多种登录方式定量性能测试方案](https://mp.weixin.qq.com/s/WuZ2h2rr0rNBgEvQVioacA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [多项目登录互踢测试用例](https://mp.weixin.qq.com/s/Nn_CUy_j7j6bUwHSkO0pCQ)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [选择手动测试还是自动化测试？](https://mp.weixin.qq.com/s/4haRrfSIp5Plgm_GN98lRA)
- [自动化测试项目为何失败](https://mp.weixin.qq.com/s/KFJXuLjjs1hii47C1BH8PA)
- [简化测试用例](https://mp.weixin.qq.com/s/BhwfDqhN9yoa3Iul_Eu5TA)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDkiawpL3o8umv1EgHOc2OE1iaib1vR7Q6DH1FSpP4HVuibsibicftEqUqfXZpE2FyN7nIPvHwhWQdG6n0g/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)