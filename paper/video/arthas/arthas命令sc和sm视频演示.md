# arthas命令sc和sm视频演示


> arthas是一个Java开源诊断神器。

今天分享两个命令：`sc`和`sm`。

* `sc`：“Search-Class” 的简写，这个命令能搜索出所有已经加载到 JVM 中的 Class 信息。
* `sm`：“Search-Method” 的简写，这个命令能搜索出所有已经加载了 Class 信息的方法信息。

一个是查看加载类的信息，一个是查看加载类的方法信息，这两个方法都是侧重于查看信息，比较简单，测试过程中实用性不算很高，如果有权限去查看源码的话，几乎可以忽略掉这两个命令的。如果是没有源码的话，可以执行`jda`反编译命令即可查看源码（反编译后）信息。所以我对这两个命令的观点就是，了解，知道即可。

系列文章：

- [arthas快速入门视频演示](https://mp.weixin.qq.com/s/Wl5QMD52isGTRuAP4Cpo-A)
- [arthas进阶thread命令视频演示](https://mp.weixin.qq.com/s/XuF7Nr1sGC3diIn50zlDDQ)
- [arthas命令jvm,sysprop,sysenv,vmoption视频演示](https://mp.weixin.qq.com/s/87BsTYqnTCnVdG3a_kBcng)
- [arthas命令logger动态修改日志级别--视频演示](https://mp.weixin.qq.com/s/w724P9B12eTC9rMbavwsMA)


# arthas命令sc和sm

- [点击观看视频](https://mp.weixin.qq.com/s/Ga63sjW_bOKQqfnA5LTb9w)

本期不涉及源码，如果想练习这两个命令的，自己随便写一个就行，这里我就不分享我自己的Demo了。感兴趣的同学，后台回复`arthas`即可查看所有录制的arthas教程视频。

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [接口测试视频专题](https://mp.weixin.qq.com/s/4mKpW3QiVRee3kcVOSraog)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [简化测试用例](https://mp.weixin.qq.com/s/BhwfDqhN9yoa3Iul_Eu5TA)
- [自动化策略六步走](https://mp.weixin.qq.com/s/He69k8iCKhTKD1j-yV6M5g)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)