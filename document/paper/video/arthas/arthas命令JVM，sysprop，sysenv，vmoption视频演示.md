# arthas命令JVM，sysprop，sysenv，vmoption视频演示

arthas视频教程合集：

- [arthas快速入门视频演示](https://mp.weixin.qq.com/s/Wl5QMD52isGTRuAP4Cpo-A)
- [arthas进阶thread命令视频演示](https://mp.weixin.qq.com/s/XuF7Nr1sGC3diIn50zlDDQ)

本期分享阿尔萨斯的四个命令：JVM，sysprop，sysenv，option。因为这四个命令都是比较简单，是一些基础啊，配置啊，环境变量啊，还有一些参数的查询，很少涉及到修改。所以也没有什么可讲的，就索性把这四个命令做成一个视频了。其中`JVM`这个命令，是查看Java虚拟机当前信息的。但是这个信息跟`JVM`自带的命令差别还是挺大的。主要体现在信息的准确性和丰富程度上。如果是对`JVM`的信息有严格的要求的话，我个人建议还是用`JVM`自带的命令。剩下两个命令system property和system environment都是属于查看命令（其中system property能改的地方非常少，修改的意义对于测试来说也不是很大。）。最后一个`vmoption`命令能查看一些`JVM`启动参数，但是，参数信息有限，比如对内存的设置和内存的分配配置都是看不到的。其中。比较有用的信息就是设置`OOM`的参数，还有`Java gc`的参数。这个动态修改还是非常有用的。因为虽然说项目需要一个严格的规范，一般都会将这些参数呃进行一个比较合理的配置。但是总有例外情况，很多时候我们去检查这些配置的时候就会发现，事实跟我们规范并不一样。这个时候。命令的作用就体现了我们可以随时的动态修改这些配置。

* jvm：查看当前JVM信息
* sysprop：查看当前JVM的系统属性(System Property)
* sysenv：查看当前JVM的环境属性(System Environment Variables)
* vmoption：查看，更新VM诊断相关的参数

## arthas命令JVM，sysprop，sysenv，vmoption

- [点击观看视频](https://mp.weixin.qq.com/s/87BsTYqnTCnVdG3a_kBcng)

由于本期视频并没有涉及到代码的内容，修改`JVM`的参数，比如`OOM`和`gc`的一些设置，我在视频里面并没有去演示。修改系统配置和环境变量等也没有涉及到代码。如果大家想多了解`JVM`启动参数的使用，大家可以参考一下我之前的文章：

- [测试之JVM命令脑图](https://mp.weixin.qq.com/s/qprqyv0j3SCvGw1HMjbaMQ)
- [6个重要的JVM性能参数](https://mp.weixin.qq.com/s/b1QnapiAVn0HD5DQU9JrIw)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)

这里特别推荐《Java性能权威指南》这本书，我现在大概能看懂里面的1/3的内容，也是收获满满。


---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [多项目登录互踢测试用例](https://mp.weixin.qq.com/s/Nn_CUy_j7j6bUwHSkO0pCQ)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [绑定手机号性能测试](https://mp.weixin.qq.com/s/K5x1t1dKtIT2NKV6k4v5mw)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [简化测试用例](https://mp.weixin.qq.com/s/BhwfDqhN9yoa3Iul_Eu5TA)
- [生产环境中进行自动化测试](https://mp.weixin.qq.com/s/JKEGRLOlgpINUxs-6mohzA)
- [所谓UI测试](https://mp.weixin.qq.com/s/wDvUy_BhQZCSCqrlC2j1qA)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)