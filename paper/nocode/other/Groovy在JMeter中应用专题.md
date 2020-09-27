# Groovy在JMeter中应用专题

以前我并不会熟练使用JMeter，偶然间看到博文将JMeter支持了Groovy（其实一直都是支持的），心血来潮写了一个Demo，分享了一篇文章，效果还是挺不错的。然后一发不可收拾，目前我感觉除了`ctx`和脚本文件的应该以外，其他功能的Demo我已经完成了。

它有助于访问上下文。通过`ctx`获取诸如`SampleResult`或`prev`更高级别的信息，我看官方的文档中是支持线程调度的，用于中断线程或者中断循环等等，个人感觉用处不大，还是使用`JMeter`自带的线程管理功能比较好。故而`Groovy`在`JMeter`中的尝试也就结束了。

还有一个偶然发现的`JMeter`在计算吞吐量的时候存在的理论值差异，算是无心插柳，如果是想在`JMeter`使用更上一层楼，个人给两个建议：**多查阅官方或社区文档**，还有**多和他人交流探索**。

以下是`JMeter`专题全部文章内容：

- [用Groovy处理JMeter断言和日志](https://mp.weixin.qq.com/s/Q4yPA4p8dZYAARZ60ZDh9w)
- [用Groovy处理JMeter变量](https://mp.weixin.qq.com/s/BxtweLrBUptM8r3LxmeM_Q)
- [用Groovy在JMeter中执行命令行](https://mp.weixin.qq.com/s/VTip7tiLpwBOr1gUoZ0n8A)
- [用Groovy处理JMeter中的请求参数](https://mp.weixin.qq.com/s/9pCUOXWpMwXR5ynvCMYJ7A)
- [用Groovy在JMeter中使用正则提取赋值](https://mp.weixin.qq.com/s/9riPpnQZCfKGscuzOOpYmQ)
- [Groovy在JMeter中处理cookie](https://mp.weixin.qq.com/s/DCnDjWaj2aiKv5HVw3-n6A)
- [Groovy在JMeter中处理header](https://mp.weixin.qq.com/s/juY-1jEWODJ5HHiEsxhIEw)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)

--- 
* 公众号**FunTester**首发，更多原创文章：[FunTester420+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)
- [有关UI测试计划](https://mp.weixin.qq.com/s/D0fMXwJF754a7Mr5ARY5tQ)
- [Selenium自动化测试技巧](https://mp.weixin.qq.com/s/EzrpFaBSVITO2Y2UvYvw0w)
- [敏捷测试中面临的挑战](https://mp.weixin.qq.com/s/vmsW56r1J7jWXHSZdcwbPg)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)