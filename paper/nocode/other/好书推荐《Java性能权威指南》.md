# 好书推荐《Java性能权威指南》

> Java性能调优覆盖这两个领域：`编译器`和`垃圾收集器`等的调优参数，以及`API`的最佳实践。

最近看完了《Java性能权威指南》对于自己做性能测试有很大的启发，其中关于微基准测试、介基准测试、宏基准测试已经在工作用使用了，包括`GC收集器`的优劣和故障排查也有了新的认识。其中讲了`Java线程同步`的几种方案和优劣也有很不错的`Demo`，虽然不能全懂，但寥寥草草也知道个大概，比如`编译器优化`和`其他JVM参数`，以后等我懂了会写文章分享一下。

有文为证：

- [利用微基准测试修正压测结果](https://mp.weixin.qq.com/s/dmO33qhOBrTByw_NshS-uA)
- [性能测试如何减少本机误差](https://mp.weixin.qq.com/s/S6b_wwSowVolp1Uu6sEIOA)
- [超大对象导致Full GC超高的BUG分享](https://mp.weixin.qq.com/s/L15-0JW9WK-E005GeOG9WQ)
- [服务端性能优化之双重检查锁](https://mp.weixin.qq.com/s/-bOyHBcqFlJY3c0PEZaWgQ)
- [线程安全类在性能测试中应用](https://mp.weixin.qq.com/s/0-Y63wXqIugVC8RiKldHvg)
- [利用ThreadLocal解决线程同步问题](https://mp.weixin.qq.com/s/VEm8jt3ZUEUdyyeXPC8VvQ)
- [线程安全集合类中的对象是安全的么？](https://mp.weixin.qq.com/s/WKSuPEfzZCVwjVTcoD0Dyg)

## 适用范围

本书适合那些渴望深入了解`JVM`和`Java API`性能各个方面的性能调优工程师和开发者。假如你想快速修复性能问题，那么本书可能不适合你。

要想让`Java`运行得飞快，就得深入理解JVM (以及`Java API`)的实际工作原理。本书的目的是提供更为详尽的`JVM`和`API`工作原理，以便理解它们如何工作的原理之后，排除那些性能低下行为就会变成简单的任务。

Java性能调优工作还有一个有趣的方面，就是开发人员的背景和性能调优或测试工程师的背景常常有很大差别。有些开发人员，他们可以记住大量令人费解的很少使用的`Java API`方法签名，但他们对`-Xmn`的含义却没有什么概念。也有些测试工程师，他们可以通过设置各种标志来榨取最后一滴性能，但却很少能用Java写出像样的`Hello, World ！`。

## 建议

如果你的主要兴趣是JVM自身的性能调优。意思是不用更改任何代码而改变JVM的行为，那么本书的大量章节都对你有用。可以随意跳过代码部分，而关注你所感兴趣的领域。也许你会顺便为Java应用如何影响JVM性能提出一些真知灼见，并向开发人员提出更改建议，以便让你的性能调优测试工作更加如鱼得水。

下面是本书的脑图：

![](http://pic.automancloud.com/Java性能权威指南.png)

PDF格式的文件和xmind格式的发不上来，有兴趣的朋友可以添加微信号索取。

![](http://pic.automancloud.com/WechatIMG34.jpeg)


---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [Java并发BUG基础篇](https://mp.weixin.qq.com/s/NR4vYx81HtgAEqH2Q93k2Q)
- [Java并发BUG提升篇](https://mp.weixin.qq.com/s/GCRRe8hJpe1QJtxq9VBEhg)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)


## 非技术文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)