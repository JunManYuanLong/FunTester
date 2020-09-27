# TDD测试驱动开发的基础

[原文地址](https://www.javacodegeeks.com/2019/03/essentials-test-driven-development.html)

> 如果您需要软件并且需要快速，那么测试驱动开发（TDD）可能是解决方案。TDD致力于快速将软件从计算机推向市场，是当今顶级软件开发和软件测试公司正在使用的最有效方法之一。

## 什么是测试驱动开发？
敏捷性和速度是赋予测试驱动开发运动力量的两个概念。但是什么是TDD，流程如何运作？

测试驱动的开发是一个软件开发过程，其重点是在开发人员编写实际代码之前为软件测试编写测试。目的是使开发人员专注于代码的用途并确保其功能。

运作方式如下：
* 每个测试驱动的开发周期都始于编写测试以查看软件是否可以运行。该测试基于软件的功能，要求和规格。
* 接下来，开发人员运行测试以确保其适当性和有效性。在此阶段，测试应该失败，这意味着它可以工作并且不会显示出假阳性结果。
* 一旦建立了足够的测试，开发人员便会继续编写代码。在此阶段，代码可能还不够完善，但必须通过测试才能继续前进。这就是为什么此测试阶段必不可少的原因。
* 一旦一段代码通过测试，就可以进行重构。这是代码清理阶段，其中删除重复项，正确命名所有代码元素（对象，类，模块，变量，方法等），并添加所有必需的新功能。
* 完成此过程后，开发人员可以重新启动该循环以进行编码改进，添加新功能或修复任何编码错误。
简而言之，测试驱动的开发关注于代码是否完成了应做的工作。如果有效，请转到下一个阶段，否则请重写。概念就是这么简单。

## TDD是如何发明的？

现代TDD的原型是在1960年代发明的。该技术的“重新发现”归功于一位肯特·贝克（Kent Beck）的美国软件工程师。贝克还是敏捷软件开发的创始人之一，也是《敏捷宣言》的签署人。

早在2002年，贝克（Beck）就在他的《测试驱动开发：范例》一书中向世界介绍了TDD的概念。

虽然一般来说不是一个新主意，但是Beck声称TDD是“有效的干净代码”，着眼于模型的简单性和消除了传统软件开发方法附带的代码不起作用的担忧。

## TDD与传统测试之间的差异
让我们比较一下。

| 传统测试 | TDD |
| --- | --- |
| 最后测试的方法，其中开发人员创建代码，但保留测试直到开发过程结束。| 一种测试优先的方法，其中开发人员或测试自动化工程师首先创建测试，然后开发人员进行编码以满足测试的要求。|
| 专注于代码正确性，但可能无法检测到所有编码缺陷。| 然后，测试将进行重构，直到代码通过测试为止；直到代码满足功能为止，然后继续进行测试，并减少系统中的错误数量。|
| 线性过程。（设计代码测试）| 循环过程。（测试代码重构）|

## 测试驱动开发的好处
测试驱动开发的支持者可以在快速开发代码时提高其速度，敏捷性和功能。但是，这些并不是唯一的优点。开发系统还：

* 保持代码简单，有用且切合实际，使所有相关人员的过程更加轻松。
* 有助于查明由于严格测试而导致的错误和其他代码缺陷，因此开发人员可以准确地知道问题出在哪里。这样可以减少（但不会否定）最终测试时间。
* 允许开发人员查看实际的代码，采用用户的观点并对最终用户产生同情。因此，代码可以更好地反映用户的需求。
* 巩固了项目的目的和目标，从抽象的想法到精确的目标，鼓励开发人员专注于他们真正需要做的事情。

## 测试驱动开发的缺点

但是，使用测试驱动的开发方法存在一些缺点。让我们来看看：

* 尽管声称TDD比传统编码过程快，但最初该过程可能很慢。但是，随着时间的推移，生产率将大大提高。
* 开发人员可能过于专注于一两个编码问题，而看不到全局。尝试修复错误时，这一点尤其重要。
* 开发足够的初始测试（尤其是对于创新软件）存在一些问题，因为测试开发人员应该几乎完全知道他们想要从代码中获得什么。
* 这种方法不允许在初始设计中进行大量更改，否则，这将增加TDD流程的执行时间。

## 您应该在软件开发中使用测试驱动的方法吗？
与所有业务决策一样，选择采用测试驱动的开发方法是公司特定的决策。如果您正在考虑使用测试驱动的方法，则应首先确保TDD适合您的业务。

首先，这将取决于您团队的需求和经验。由于TDD是一种快节奏的敏捷方法，因此您需要确保它们已准备好应对挑战。另外，您可以求助于质量保证咨询以帮助您采用这种方法。

也就是说，测试驱动的开发可能是将您的产品尽快从代码行转换为可用于市场的产品的绝佳方法。

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)

## 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [功能测试与非功能测试](https://mp.weixin.qq.com/s/oJ6PJs1zO0LOQSTRF6M6WA)
- [自动化和手动测试，保持平衡！](https://mp.weixin.qq.com/s/mMr_4C98W_FOkks2i2TiCg)
- [自动化测试生命周期](https://mp.weixin.qq.com/s/SH-vb2RagYQ3sfCY8QM5ew)

## 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
- [JVM的对象和堆](https://mp.weixin.qq.com/s/iNDpTz3gBK3By_bvUnrWOA)
- [JVM虚拟机面试大全](https://mp.weixin.qq.com/s/WPll-3ZvYrS7J7Cl8MuzhA)

![长按关注](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzBEASPySoVdOFmP12QUIWAQms664L0b82nic8BRIlufg0QibzXNnoibZp8yqhU9Pv0hXjKtqrGof8kMA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)