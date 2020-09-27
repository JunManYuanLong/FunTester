# JMeter吞吐量误差分析

> JMeter吞吐量可能是个假数据，包含了本机处理时间。

首先我本身并不用JMeter进行压测，故事的缘起是因为看到了同事适用JMeter进行测试的测试报告，偶然间发现一个问题，JMeter报告中的吞吐量误差较大。结果如图：

![](http://pic.automancloud.com/QQ20200311-211247.png)

按照经典理论模型计算吞吐量TPS或者QPS应该是等于并发线程数除以平均响应时间：

**tps =Thread / AVG(t)**

或者 **tps = COUNT(request) / T**

大家看第一个案例：平均响应时间593ms，100并发，计算得到的吞吐量为：168.63，JMeter给出的吞吐量为166.4，误差几乎可以忽略。
再看第三个案例：100并发，平均响应时间791ms，计算得到的吞吐量为126.422，JMeter给出的吞吐量为92.3，误差已经很大了。

到底是什么原因导致误差如此之大呢，经过研究同事的压测过程，发现了在第三个案例中，他使用了较多的正则匹配来校验响应返回值。那么是不是JMeter在处理返回值消耗的时间较多导致了计算吞吐量误差的呢？不由让我想起之前的文章：[利用微基准测试修正压测结果](https://mp.weixin.qq.com/s/dmO33qhOBrTByw_NshS-uA)、[性能测试如何减少本机误差](https://mp.weixin.qq.com/s/S6b_wwSowVolp1Uu6sEIOA)。

那么我们通过一个实验验证一下：首先写一个脚本，我用了单线程的脚本，请求10次看结果：

![](http://pic.automancloud.com/QQ20200311-213548.png)

看结果，平均响应时间207ms，一个并发，计算得到的结果为4.83，JMeter给出的结果4.8，符合预期。

然后我用一个Groovy后置处理器，让线程休眠500ms，然后还是单线程并发，请求10次的结果：

![](http://pic.automancloud.com/QQ20200311-213943.png)

看结果，平均响应时间193ms，跟第一次结果差不多，JMeter给出的吞吐量值为1.5，误差巨大。

那么1.5的吞吐量是怎么来的呢？我们给193ms加上我们的等待500ms（这里是应该加上500 * 9 / 10），计算结果为1.54，跟JMeter给出的1.5符合，基本可以断定JMeter在计算吞吐量时候，把本机处理的过程也是计算在内的。

如果JMeter在整个请求过程中平均响应时间是正常统计请求发出到接收到响应的时间，但是吞吐量缺失用本机的整个线程一次循环的时间作为吞吐量计算的依据。

如果你在线程中做了别的事情，比如正则提取，参数校验，变量赋值等等都会导致吞吐量会变小。而一旦本机处理时间增加，那么压测过程中给服务端的实际压力也是比配置的要小，如果本机性能消耗过大或者某些地方发生等待，那么得到的吞吐量就可以当做一个假数据处理了。

这里我给出一个方案[利用微基准测试修正压测结果](https://mp.weixin.qq.com/s/dmO33qhOBrTByw_NshS-uA)、[性能测试如何减少本机误差](https://mp.weixin.qq.com/s/S6b_wwSowVolp1Uu6sEIOA)。

如果发现误差较大，一定要进行结果修正，避免假数据。


--- 
* 公众号**FunTester**首发，更多原创文章：[450+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [开源测试服务](https://mp.weixin.qq.com/s/ZOs0cp_vt6_iiundHaKk4g)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)
- [测试开发工程师工作技巧](https://mp.weixin.qq.com/s/TvrUCisja5Zbq-NIwy_2fQ)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)