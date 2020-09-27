# Selenium并行测试最佳实践

[原文地址](https://www.lambdatest.com/blog/what-is-parallel-testing-and-why-to-adopt-it/)

前文讲到[Selenium并行测试基础](https://mp.weixin.qq.com/s/OfXipd7YtqL2AdGAQ5cIMw)，本文将分享一些并行测试实践相关内容。主要以理论为主，各位如何像了解代码和项目实践细节的可参考之前的文章：

- [JUnit中用于Selenium测试的中实践](https://mp.weixin.qq.com/s/KG4sltQMCfH2MGXkRdtnwA)
- [JUnit 5和Selenium基础（一）](https://mp.weixin.qq.com/s/ehBRf7st-OxeuvI_0yW3OQ)
- [JUnit 5和Selenium基础（二）](https://mp.weixin.qq.com/s/Gt82cPmS2iX-DhKXTXiy8g)
- [JUnit 5和Selenium基础（三）](https://mp.weixin.qq.com/s/8YkonXTYgAV5-pLs9yEAVw)
- [如何在跨浏览器测试中提高效率](https://mp.weixin.qq.com/s/MB_Wv7yQ6i9BztAZtL4grA)

并行测试方法只有在使用最佳实践来实现时才能成功。以下是一些可用于在`Selenium`中实施并行测试的有效方法。

# Selenium中并行测试执行的最佳实践

即使使用`Selenium Grid`，并行运行自动化浏览器测试也不是一件容易的事，这是由于您在`Selenium`中执行并行测试所使用的非结构化自动化框架所致。以下是一些最佳实践，可以帮助您成功并行并行执行`Selenium`测试自动化。

## 生成独立的测试用例

如果项目生成可以独立运行的独立测试，则并行执行它们会更容易。简而言之，测试必须是独立的。因此，在运行任何测试时，不必担心运行测试套件的顺序问题。

有时并行执行的测试会表现出误报的行为，例如误报失败或误报成功，这就是脆弱性。独立测试可以通过减少测试中可能的断点数量来减少自动浏览器测试过程中的脆弱性。使用独立测试的另一个重要优点是，如果一个测试失败，则不会阻止您测试其他测试功能。

## 基于云的Selenium网格

在本地`Selenium Grid`上执行自动浏览器测试可能会比较麻烦，因为不仅必须管理和维护所有机器，而且还必须设置它们的基本属性和运行环境。在本地`Selenium`网格上进行并行测试会遇到一些可伸缩性问题，因为将难以涵盖所有​​主要的浏览器，不同的版本和操作系统的组合。

使用基于云的`Selenium Grid`，无需花费过多精力在硬件和运行环境的维护上。不仅可以在建立基础结构上节省很多钱，而且还可以有足够的时间来完成重要的任务。


## 防止自动化用例之间的依赖

由于各种测试用例之间的依赖性，许多测试人员经常发现并行执行测试具有极大的挑战性。当测试相互依赖时，它们需要以特定的顺序运行，这通常会影响并行测试策略。因此，并行测试应该专注于创建可以独立执行的独立测试和原子测试。

## 高效地管理测试数据

成功进行并行`Selenium`测试自动化的主要关键是有效地处理测试数据。但是要实现这一点，整个团队需要一个统一有效的策略，该策略可以在需要时创建测试数据，并在必要时进行清理。以下是一些非常有效的基本数据管理策略。

* 持续刷新数据：这种方法可以在测试执行期间重置数据
* 使用RESTful API：这是在运行时创建和销毁数据的好方法
* 自私的数据生成：这种方法具有创建策略，但是不提供任何数据清理功能。

这些只是可用于有效管理测试数据的几种方法。可以组合两种方法来获得所需的数据维护策略。

## 创建并行测试用例

如果团队打算在`Selenium`中采用并行测试，那么肯定希望自己能够使效率得到成倍的提升。因为很多测试用例的编写都需要建立在测试环境发布的产品基础上，很难创建在产品发布之前并行运行的测试用例。因此，从一开始就要考虑并行化来开发`Selenium`测试自动化案例。

从头开始进行计划不仅可以使您免于`最后一小时`的灾难，而且还可以有效地测试所有组合场景中的应用程序。这听起来很复杂，但是编写并行运行的测试用例更容易，更小巧，更快捷。

# 总结

`Selenium`中的并行测试是一种快速交付`Web应用`而又不影响质量的绝佳方法，特别是在涉及到[如何在跨浏览器测试](https://mp.weixin.qq.com/s/MB_Wv7yQ6i9BztAZtL4grA)时。通过并行执行`Selenium`测试自动化，可以节省质量检查费用，高精度运行测试用例，优化连续集成/交付过程以及不断改进测试脚本。但是实施并行化需要从一开始就采取有效的策略。

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