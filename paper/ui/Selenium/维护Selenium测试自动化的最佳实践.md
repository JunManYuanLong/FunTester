# 维护Selenium测试自动化的最佳实践

[原文地址](https://www.lambdatest.com/blog/the-perfect-way-to-maintain-your-selenium-test-automation/)

自动化测试框架和基础组件需要及时、良好的维护。如果团队无法跟上与维护相关的需求，那么以后可能会付出更大代价，最终带来自动化项目的深陷泥潭。这里有一些减少[Selenium自动化测试](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319034944479510528&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)维护的最佳实践。


## 保持自动化用例设计简单

保持简单是简化测试维护过程的关键。现在，有些人可能会认为话说起来容易做起来难。但是实际情况就是应该尽量使用简单的`Selenium`测试自动化脚本。原因是可以快速执行**低级**测试。此外，它们还易于维护，因为它们减少了维护工作量。

不要地使测试套件复杂化会增加发生故障的机会。但是有时候，复杂的情况是不可避免的。在这种情况下，最好的解决方案是创建可重用的测试组件。

## 激励开发人员时刻注意测试

更新的UI设计是软件至关重要的过程，可为最终用户提供愉悦的体验。但是通常，**UI/UX**开发人员在编写代码时不会在心中考虑进行测试。在这种情况下，为每个对象制定命名约定可能会有所帮助。当您拥有稳定的**QA ID**时，`Selenium`测试自动化脚本将随着代码和**UI/UX**设计不断更新迭代，就会获得更加稳定的页面元素对象。

## 建立合适测试执行策略

> 软件线上BUG往往会造成巨大的灾难。编程时难免会发生错误，但测试通过之后时不应该发生。为了避免线上事故发生，我们常常会采取多种测试手段和测试方案，其中就包括自动化测试。要实现此目的，将需要准备合适的的测试自动化策略。

内容摘自：[自动化策略六步走](https://mp.weixin.qq.com/s/He69k8iCKhTKD1j-yV6M5g)

## 提高测试稳定性

在确定并尝试减少易碎测试用例之前，想说明一下什么是测试稳定性。假设同样的测试环境中，在相同的参数下运行测试用例，但是多次运行的结果仍然给出不一致。它们被称为易碎测试。换句话说，易碎测试是测试人员再次运行时通过的失败测试。通常情况在UI界面测试自动化中普遍存在，所以在设计测试用例和编写测试用例时需要额外注意。

不幸的是，即使做了很多工作来避免，无法完全避免使用它们，识别易碎的测试并快速对其做出响应非常重要。因此，减少不稳定的测试将极大降低维护测试套件的复杂度。

## 工作要追求高回报率

> 在开始自动化测试之前，需要考虑到在自动化测试上投入的时间、精力和资源后，看看自动化测试可以带来什么好处。以下是确定哪些手动测试应该或不应该自动化应该考虑的问题。俗话说，仅仅因为您可以使某些东西自动化并不一定意味着应该这样做。

内容摘自：[自动化如何选择用例](https://mp.weixin.qq.com/s/1hH5YIle4YQimJr4iGSWlA)

维护一套卓有成效的Selenium测试自动化系统并不容易。随着版本迭代，维护用例的成本会大于新建用例的成本。因此，为了最大程度地减少我们的测试自动化维护工作，需要考虑为哪些功能编写测试。因此，建议将自动化工作重点放在高回报率和不稳定的功能上。围绕关键业务价值案例进行自动化测试，而不是围绕**PPT**进行自动化测试。


## 借助云提升兼容性测试效率

> 建立可以兼容性设备，浏览器和操作系统组合的测试基础架构是一项昂贵的事情。例如，如果您必须在不同版本的Android上测试网站功能；您将需要具有这些Android版本的设备，并且还需要从不同的智能手机供应商处购买设备。因此，这种方法是不可行且不可持续的。理想的方法是在云测试服务上测试功能，以便您可以专注于测试而不必担心基础架构。也可以通过下载相应的`WebDriver for Selenium`使用`Selenium`编写自动测试脚本。

内容摘自：[如何在跨浏览器测试中提高效率](https://mp.weixin.qq.com/s/MB_Wv7yQ6i9BztAZtL4grA)

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester十八张原创专辑](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**，合作请联系`Fhaohaizi@163.com`。

### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [测试如何处理Java异常](https://mp.weixin.qq.com/s/H00GWiATOD8QHJu3UewrBw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [Selenium并行测试最佳实践](https://mp.weixin.qq.com/s/-RsQZaT5pH8DHPvm0L8Hjw)
- [性能测试、压力测试和负载测试](https://mp.weixin.qq.com/s/g26lpd7d7EtpN7pkiqkkjg)
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)