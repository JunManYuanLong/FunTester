# 如何选择API测试工具



> 没有最好，只有最合适。

如今，越来越多的公司正在向DevOps的方向左转，以实现持续集成和持续部署开发。这意味着我们的反馈需要比以往更快，以便确定我们的应用程序是否准备好交付。这就是API测试如此重要的原因，以及为什么应将其作为整体自动化策略重要的一部分。

分享一下我认为不错的五种API测试工具，无论哪种方式，它们都是不错的选择。

## Rest-Assured

如果您使用的是Java，则Rest-Assured将是实现API自动化的首选。

Rest-assured是一个流行的Java库，可用于测试基于HTTP的REST服务。它在设计时就考虑到了测试，并且与任何现有的基于Java的自动化框架集成在一起。它提供了一个类似于BDD的DSL，从而使用Java创建API测试变得简单。它还具有许多内置功能，这意味着不必从头开始编写代码。Rest-assured可以和很多测试框架无缝集成，这意味着可以将UI和API测试全部结合在一个框架中，从而生成全面出色的报告。与动态语言（例如Ruby和Groovy）相比，用Java测试和验证REST服务要困难得多。这是使用REST-Assured的另一个原因，因为它将Java语言中使用这些语言的简便性带给了您。

如果团队主要由Java开发人员组成，对API测试来说Rest-Assured是非常不错的选项。

## Postman

接口测试不一定要使用与开发人员相同的语言来进行必要的测试工作。如果刚好跟开发语言不通，则需要考虑Postman进行一些快速而简单的API测试，而不必担心其他的开销。Postman还是探索型API测试的不错选择。但是它也足够强大，可以根据需要创建更多集成的解决方案。

Postman是一个易于使用的Rest客户端，您可以利用其Chrome插件快速入门。Mac和Windows也都有响应的版本支持。它具有许多Rest客户都没有的非常丰富的界面，使其易于使用。它还使您可以轻松地与同事共享知识，因为您可以打包所有请求和期望的响应，然后将其发送给其他人，以便他们也可以查看。

如果您的团队不仅要测试API，而且要有一个工具来帮助自动化还有一些探索性API测试工作，那么Postman是一个不错的选择。

## SoapUI

SoapUI已经存在了一段时间。如果您的团队仅进行API测试，并且主要由质量检查工程师（而非开发人员）组成，那么SoapUI可能是您团队的最佳选择。SoapUI是专用于API测试的功能齐全的测试工具。API无需从头开始创建解决方案，而是使您能够利用功能齐全的工具严格针对API测试。如果出于某种原因需要创建自定义功能，则可以使用Groovy在SoapUI中编写解决方案的代码。

如果您的团队具有复杂的API测试方案，并且由更多的质量检查/测试工程师组成，重点是不缺钱，那么SoapUI是首先的工具。

## JMeter

尽管JMeter是为进行负载测试而创建的，但许多人也将其用于功能API功能和自动化测试。JMeter包括帮助您测试API所需的所有功能，以及一些可用来增强API测试工作的额外功能。例如，JMeter可以自动使用CSV文件，这使团队可以快速为API测试创建唯一的参数值。它还与Jenkins集成，这意味着您可以将API测试包含在持续集成管道中。

如果您打算创建API功能测试，并且还要在性能测试中加以利用，JMeter无疑是最佳的测试解决方案。

## Fiddler
 
Fiddler是一个工具，经常用来抓包，它还可以捕获、操纵和重发HTTP请求。Fiddler可以做很多事情来调试网站问题，并且通过它的众多扩展之一，您可以完成更多工作。其中之一-APITest扩展-极大地增强了Fiddler以验证Web API的行为。（验证者提供了一种轻量级的方法来判断测试的成功或失败）

对于更多的核心API测试开发，您可以使用`FiddlerCore.NET`类库来构建其API测试基础结构。对于使用`.NET`语言的团队来说，这是一个不错的选择，因为您可以使用所需的任何`.NET`语言来开发测试。

# 选哪个
没有完美的工具。每个小组有不同的要求。实际上，所有API测试工具都可以很好地工作，并且是不错的选择，具体取决于团队的需求以及团队的人员配置，不选最好，选最合适的那个工具。

---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [Selenium 4.0 Alpha更新日志](https://mp.weixin.qq.com/s/tU7sm-pcbpRNwDU9D3OVTQ)
- [Selenium 4.0 Alpha更新实践](https://mp.weixin.qq.com/s/yT9wpO5o5aWBUus494TIHw)
- [如何统一接口测试的功能、自动化和性能测试用例](https://mp.weixin.qq.com/s/1xqtXNVw7BdUa03nVcsMTg)

## 非技术文章精选

- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)
- [2019年浏览器市场份额排行榜](https://mp.weixin.qq.com/s/4NmJ_ZCPD5UwaRCtaCfjEg)
- [API测试基础](https://mp.weixin.qq.com/s/bkbUEa9CF21xMYSlhPcULw)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)