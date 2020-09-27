# FunTester测试框架视频讲解（序）

最近有些空闲，有点时间，想录个视频，给粉丝看看。[点击跳转原文观看视频](https://mp.weixin.qq.com/s/CJrHAAniDMyr5oDXYHpPcQ)

本人使用Java语言基于httpclient做了一个测试框架，包括功能、自动化和性能测试的能力，之前都是写一些文章分享一些案例，打算做一些新的尝试，希望效果能比文字更好一些。本系列视频内容也是多基于这个框架来讲，番外的视频另说。

**gitee地址：https://gitee.com/fanapi/tester**

首先录了一个HTTP接口扫盲的视频，主要分享了一些HTTP基础，力推一本书《图解HTTP》，可以从之前的文章获取[图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)。讲的比较快，有个朋友说太笼统了，很多知识点都没有讲清楚，这个如果有需求的话，以后针对某个知识点再录个小视频。目前收到一个需求就是`json`使用。有其他需求的欢迎留言。

- [点击观看视频](https://mp.weixin.qq.com/s/CJrHAAniDMyr5oDXYHpPcQ)

下面是介绍测试框架中接口功能测试最重要的两个类`ClientManage`和`FanLibrary`中的`ClientManage`，这个类主要是处理通用的设置以及极少量的初始化方法。这个类里面的内容是不用更改的，只要几个配置还有一个初始化方法（性能测试专用），性能测试使用的以后会专门讲解。如果你想直接上手框架做测试，请等下期视频。

- [点击观看视频](https://mp.weixin.qq.com/s/CJrHAAniDMyr5oDXYHpPcQ)

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

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDkiawpL3o8umv1EgHOc2OE1iaib1vR7Q6DH1FSpP4HVuibsibicftEqUqfXZpE2FyN7nIPvHwhWQdG6n0g/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzBiaBZzt2rchWvBn0pztDTcYwUrHyWvCCIxiaHORQ1xe1vID42zWVicABw6dHibFChrlbFqVR5vO96eVQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
