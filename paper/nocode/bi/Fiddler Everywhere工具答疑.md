# Fiddler Everywhere工具答疑

之前写过一篇文章介绍了`fiddler Everywhere`工具：[未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)，然后我就发到了平台上，收到了一些反馈，主要是功能咨询和对工具的看法交流。这两天特意去官方网站看了看，得到了一些新信息，所以分享一下。

> 先说我的看法，大概率不会流行起来。

一开始看到这个软件的`GUI`，非常惊喜，感觉*就是我要的*软件，帮助我在*无代码*环境中更好地完成一些工作。但是当我看完官网的介绍之后，发现其实软件的定位和我的期望相差巨大。

首先，`fiddler Everywhere`定位是一个商业协作软件，其次，定位是*调试工具*和*代理工具*。

# 收费

`fiddler Everywhere`的功能在免费版和专业版都可用。但是，`fiddler Everywhere Pro`版本扩展了免费版本的共享和协作选项。免费版本具有与共享配额相关的一些限制。有关免费版与专业版的详细比较，请参阅下表。

|特征	|免费|	专业版|
|----|----|-----|
|捕获和检查流量|无限|无限|
|创建作曲家请求|无限|无限  |使用自动响应规则|最多 5 个已启用的规则|无限|
|创建已保存的会话条目 |最多 5 个会话|无限|
|共享已保存的会话|最多 5 个会话（最多一个用户）|1000 会话|
|共享会话大小|最大 5 MB|最大 50 MB|
|电子邮件支持|-|无限|

主要限制还是在团队协作方面，单机版应该够用了，只能说是勉强拓展了一些`fiddler`功能，如果想作为主要测试工具，目前来看还是不行。但是如果抛开`费用`不谈，还是比较吸引人的。现在很多团队都在开发测试协作工具或者通过流程解决协作问题，`fiddler Everywhere`的确给出了一个非常不错的解决方案。

# 功能缺失

`fiddler Everywhere`看名字很像`fiddler`，起初我以为会直接继承`fiddler`的功能，然后做拓展。没想到功能阉割的比较厉害，包括*网络模拟*、*环境变量*、*关联执行*和*脚本支持*等等。

为此我查了一下官网的产品路线图，发现因为定位的不同，这些软件功能可能在未来很长一段时间不会出现在`fiddler Everywhere`未来版本中。

* 2020年产品路线图：

## 调试和修改流量

在2019年的下一个主要版本中，我们将能够添加断点并修改服务器的响应。

## 导入/导出会话

我们将优化导入/导出会话，以实现将流量导入和导出到文件。

## 检查流量

我们将介绍一种更方便的方法，以通过`Cookie`，`JSON`，`XML`等不同的检查器检查会话。

## request

我们将使您能够向服务器编写复杂的请求。

## 过滤流量

我们将提供过滤您的流量的功能。

> 个人看法：2020年的话，还是针对现有的功能进行强化，没有补充偏小众的功能。我个人比较期待脚本的支持，因为我就可以自定义功能了。


# 更新频率

下面是2020年的更新记录：

* Fiddler Everywhere v1.0.1 August 07, 2020
* Fiddler Everywhere v1.0.0 July 27, 2020
* Fiddler Everywhere v0.11.0 July 08, 2020
* Fiddler Everywhere v0.10.0 June 01, 2020

更新频率上还算可以，从6月份以后基本月更，但是功能上实在是看不到太多让人`虎躯一震`的亮点，如果就目前的路线图来讲，其实我并不推荐使用这个工具，即使全部功能免费。

# 其他工具

`telerik`公司还有一整套的解决方案及其相关工具，我简单看了看，真心觉得牛。

分享一个`fiddler  core`的功能介绍
![](http://pic.automancloud.com/WX20200826-150656@2x.png)

可以说非常好了，这种级别的商业工具做的真就是好，可我一看价格，瞬间就凉了……

![](http://pic.automancloud.com/WX20200826-150852@2x.png)

还有一些其他`CI/CD`的工具，结论都一样，真香惜穷。

> 重复我的观点：**大概率不会流行起来**。


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