# Selenium 4.0 Alpha更新日志

[原文地址](https://www.javacodegeeks.com/2019/12/selenium-4-alpha-what-to-expect.html)

早在2018年8月，整个测试自动化社区就发生了一件重大新闻：Selenium的创始成员Simon Stewart在班加罗尔Selenium会议上正式确认了Selenium 4的发布日期和一些重要更新。

Selenium 4.0 Alpha版本已经发布了，可以从Selenium官方网站下载。让我们回顾一下Selenium会议上宣布的功能以及此版本中提供的一些改进和附加功能。

## 为什么Selenium 4.0很重要
如果您认为测试自动化工程师是唯一应该关注Selenium重大更新的人员，那么您是错的。Selenium已经成为实现自定义自动测试的行业标准，并且被认为是每个Web应用程序自动化测试的首选解决方案，而该解决方案已经超出了手动功能测试可以解决问题的方法。
但是经常被遗忘的是，严重依赖Selenium的企业不仅是拥有自动化QA工程师团队的企业，而且还有很多是集成了基于Selenium的无代码自动化测试框架的企业。

基于Selenium的无代码测试已成为一种趋势。此类工具不仅使对Web浏览器有基本了解的人都可以进行部署自动化测试，而且还使运行回归测试，进行综合监视和负载测试更加容易，而无需任何Selenium框架知识。

此类无代码自动化软件的完美示例是CloudQA，有兴趣的童鞋可以自行搜索了解。

## Selenium 4.0的重大变化

让我们来看看Selenium 4.0 Alpha版本的主要变化：

### W3C WebDriver标准化
首先，Selenium 4 WebDriver是完全W3C标准化的。WebDriver API在Selenium之外已变得越来越重要，并已在多种自动化工具中使用。例如，诸如Appium和iOS驱动程序之类的移动测试工具在很大程度上依赖于它。W3C标准还将鼓励WebDriver API的不同软件实现之间的兼容性。

这是Selenium Grid与早期版本中的Driver可执行文件进行通信的方式：

![](http://pic.automancloud.com/W3C-WebDriver-Protocol.png)

Selenium 3.x中的测试通过本地端的有线协议与节点处的浏览器通信。这种方法需要对API进行编码和解码。

随着我们期望在Selenium 4中看到的更新，该测试将直接进行通信，而无需通过W3C协议对API请求进行任何编码和解码。尽管JAVA绑定将向后兼容，但重点将更多地放在W3C协议上。


### Selenium 4 IDE TNG

![](http://pic.automancloud.com/Selenium-4-IDE-TNG.png)

Chrome的Selenium IDE支持现已可用。您可以从以下网址下载它：https://selenium.dev/selenium-ide/ 

![](http://pic.automancloud.com/QQ20191213-172646.png)

众所周知，Selenium IDE是一种记录和回放工具。现在它将具有以下更丰富和高级的功能：

* 新的插件系统。任何浏览器都可以轻松插入新的Selenium IDE。您将能够拥有自己的定位器策略和Selenium IDE插件。
新的CLI运行器。它将完全基于NodeJS，而不是基于HTML的旧运行器，并将具有以下功能：
* WebDriver播放。新的Selenium IDE运行程序将完全基于WebDriver。
* 并行执行。新的CLI运行器还将支持并行测试用例执行，并将提供有用的信息，例如花费的时间，通过和失败的测试用例。

### 改进的Selenium网格

使用过Selenium Grid的人都知道设置和配置有多困难。Selenium Grid支持在具有并行执行功能的不同浏览器，操作系统和机器上执行测试用例。

Selenium Grid有两个主要元素：集线器和节点。

集线器充当服务器，是控制网络中所有测试机的中心点。在Selenium Grid中，只有一个集线器根据功能匹配将测试执行分配给特定节点。

简而言之，Node是实际运行测试用例的测试机器。

![](http://pic.automancloud.com/Selenium-Node-Container.png)

到目前为止，Selenium Grid的设置过程通常会导致测试人员难以将连接节点连接到集线器。

在Selenium 4中，由于不再需要分别设置和启动集线器和节点，因此使用体验变得流畅而轻松。启动Selenium服务器后，网格将同时充当集线器和节点。

硒提供三种类型的网格-

* 独立模式
* 集线器和节点
* 完全分布式

新的硒服务器jar包含运行网格所需的所有内容。它具有所有依赖性。新的版本还带有Docker支持。

### 更好的可观察性

现在，可观察性，日志记录和调试不再局限于DevOps。作为即将发布的版本的一部分，将改进带有钩子的请求跟踪和日志记录，以使自动化工程师可以进行调试。

### 更新文档

文档在任何项目的成功中都起着关键作用。自Selenium 2.0发行以来，Selenium文档尚未更新。这意味着，过去几年中尝试学习Selenium的任何人都必须使用旧的教程。

因此，自然而然地，SeleniumHQ承诺将与4.0版本一起提供给我们的最新文档已经成为测试自动化社区中最受期待的Selenium更新之一。

下一期将继续分享Selenium 4 Alpha版本的实践。

---
* **郑重声明**：文章禁止第三方（腾讯云除外）转载、发表，事情原委[测试窝，首页抄我七篇原创还拉黑，你们的良心不会痛吗？](https://mp.weixin.qq.com/s/ke5avkknkDMCLMAOGT7wiQ)

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)

## 非技术文章精选

- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [自动化测试生命周期](https://mp.weixin.qq.com/s/SH-vb2RagYQ3sfCY8QM5ew)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [测试人员如何成为变革的推动者](https://mp.weixin.qq.com/s/0nTZHBOuKG0rewKAeyIqwA)
- [编写测试用例的技巧](https://mp.weixin.qq.com/s/zZAh_XXXGOyhlm6ebzs06Q)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)