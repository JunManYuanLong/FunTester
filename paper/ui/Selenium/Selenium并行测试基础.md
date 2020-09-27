# Selenium并行测试基础

[原文地址](https://www.lambdatest.com/blog/what-is-parallel-testing-and-why-to-adopt-it/)

随着技术的进步，组织从人工测试转向[Selenium](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319034944479510528&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)测试自动化，测试解决方案变得比以往更具可扩展性。但是，摆在大多数团队面前的还有一座山：并行测试的可伸缩性。许多公司仍在使用顺序测试方法来提供质量保证，这会消耗大量的时间，资源和精力。
e
本人是一些简单的尝试，可以展示一个发行周期中并行测试的重要性。将讨论并行测试，从定义到`Selenium`中并行测试的最佳实践，以帮助扩展测试工作。

# 并行测试是什么？

`Selenium`中的并行测试是一个过程，可以在不同的环境中同时运行相同的测试。并行执行测试的主要目的是减少总体时间以提高测试效率，同时通过使用`Selenium Grid`来确保高质量的产品。
让我们来测试一下顺序执行的场景。

![](http://pic.automancloud.com/parallel-testing1.png)

假设我要测试提交表单的功能，然后为该表单编写了自动化测试脚本。如果要对60种不同的浏览器和操作系统组合执行此测试，并且假设单个测试在1分钟内运行，则总共需要60分钟，即1个小时。这只是一个用例，如果要在其他模块上运行自动化脚本，想想都觉得可怕。

在上述情况下，如果同时运行3个并行测试会发生：

![](http://pic.automancloud.com/testing-series.png)

总执行时间将从60分钟缩短到20分钟。同样，如果您运行了4个并行测试，则总时间将仅减少到15分钟，依此类推，效率翻倍再翻倍。

# 为什么要并行运行[Selenium](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319034944479510528&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)测试？

有多种原因使测试人员在[Selenium](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319034944479510528&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)中采用并行测试作为他们的第一个自动浏览器测试方案。一些最受欢迎的原因如下：

## 更广泛的测试范围

与顺序测试相比，并行执行测试是一种更快的方法，因为它为测试人员提供了更广的测试兼容性，且跨度更短。例如，如果您要测试新`Web应用程序`的用户界面功能，则不必针对不同的`OS`和`浏览器`组合反复运行测试，则可以通过运行测试来同时测试所有组合在平行下。

## 减少测试时间

顺序测试的确为`Web应用`提供了全面而彻底的自动化浏览器测试，但这非常耗时。另一方面，并​​行测试可以通过在多台计算机上并行运行测试来减少总体测试时间。例如，如果您要运行100个运行环境，则可以将自动浏览器测试效率提升100倍，从而帮助您更快地交付产品。

## 成本效益

顺序测试需要开发，维护和保持测试环境为最新，这可能会影响总体成本。但是`Selenium`中的并行测试是在云上运行的自动化过程，因此无需维护。此外，您不必担心更新，因为云基础架构始终处于更新状态。

## 持续集成和交付

为了持续集成和持续交付，需要频繁且快速地运行功能测试。而且并行运行测试，它不仅可以为您节省更多时间，同时还能获得详细的测试数据报告。开发团队以后可以使用这些报告在代码中查找问题并快速修复它们，以优化`CI/CD`。

## 连续测试 
在尽可能短的时间内发布高质量产品的好方法是使用持续集成和交付方法。连续测试需要更快的测试周期，这对于顺序自动浏览器测试是不可行的。但是，如果在`Selenium`中使用并行测试，从而允许团队利用云技术和[软件测试中的虚拟化](https://mp.weixin.qq.com/s/zHyJiNFgHIo2ZaPFXsxQMg)以更快的速度执行更多测试，则可以更快地向市场发布产品。

# 如何处理并行测试用例？

我们认为并行执行测试的最佳方法是创建用于浏览器兼容性测试的不同项目，以测试`Web应用`程序的各个部分，并创建用于测试这些不同项目的主项目。通常，在硒中有两个级别的并行测试标准。第一个是入门级条件，第二个是退出条件。

在入门级标准中，定义了在成功并行执行测试之前应满足的特定任务，例如：

* 在`Selenium`中开始并行测试之前需要测试环境设置
* 在开始自动浏览器测试过程之前，必须先定义前提条件和方案
* 新数据和旧数据必须成功迁移

退出级别标准描述了成功执行并行测试的步骤，包括：

* 针对新开发的系统运行旧系统
* 了解两个系统之间的区别
* 使用相同的输入进行完整的自动浏览器测试周期
* 与旧系统相比，测量新开发系统的输出
* 向开发团队报告错误（如果发现）

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester十八张原创专辑](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**，合作请联系`Fhaohaizi@163.com`。

### 热文精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [删除List中null的N种方法--最后放大招](https://mp.weixin.qq.com/s/4mfskN781dybyL59dbSbeQ)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [自动化策略六步走](https://mp.weixin.qq.com/s/He69k8iCKhTKD1j-yV6M5g)
- [功能自动化测试策略](https://mp.weixin.qq.com/s/qHmcblN4cD4JK6jT7oU4fQ)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)