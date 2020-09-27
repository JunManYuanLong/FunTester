# 初学者的API测试技巧

[原文地址](https://www.katalon.com/resources-center/blog/api-testing-tips/)

API（应用程序编程接口）测试是一种直接在API级别执行验证的软件测试。它是集成测试的一部分，它确认API是否满足测试人员对功能、可靠性、性能和安全性的期望。与UI测试不同，API测试是在没有GUI层执行操作的。

# API测试技巧

Web API有两大类Web服务：SOAP和REST。SOAP（简单对象访问协议）是W3C标准定义的一种标准协议，用于发送和接收Web服务请求和响应。REST（表示状态传输）是使用HTTP的基于Web标准的体系结构。与基于SOAP的Web服务不同，没有针对RESTful Web API的正式标准。

以下是API测试的10条基本技巧：


## 了解API要求

在测试API之前，需要回答以下问题以彻底了解API的要求：

* API的功能是什么？业务流程是什么？使用场景是什么？

通常，应用程序的API用于对资源进行操作。它们常用于读取，创建，更新。了解API的用途将为输入和输出准备好测试数据奠定坚实的基础。此步骤还可以帮助您定义验证方法。例如，对于某些API，您将针对数据库验证响应。对于其他一些，最好根据其他API来验证响应。

例如，“创建用户” API的输出将是“获取用户” API的输入以进行验证。“获取用户” API的输出可以用作“更新用户” API的输入，依此类推。

## 指定API输出状态

您需要在API测试中验证的最常见的API输出是响应状态代码。

新API测试人员熟悉验证响应代码是否等于200以确定API测试是通过还是失败。这不是错误的验证。但是，它并不反映API的所有测试方案。

在通用标准中，所有API响应状态代码均分为五类。状态码的第一位数字定义响应的类别。后两位没有任何类别或分类作用。

第一位数有五个值：

* 1xx（信息性）：收到请求并继续进行处理
* 2xx（成功）：成功接收，理解并接受了请求
* 3xx（重定向）：需要采取进一步的措施来完成请求
* 4xx（客户端错误）：请求包含错误的语法或无法实现
* 5xx（服务器错误）：服务器无法满足看似有效的请求

> API的实际响应状态代码由构建API的开发团队指定。

## 专注于小型功能性API

在测试项目中，总是有一些简单的API，只有一个或两个输入，例如登录API，获取身份令牌API，运行状况检查API等。但是，这些API是必需的，被视为进入其他业务的“门API”。首先关注这些API，将确保API服务器，环境和身份验证正常工作。

还应该避免在一个测试案例中测试多个API。如果发生错误，这是很痛苦的，因为您将不得不按顺序调试API生成的测试数据。保持测试尽可能简单。在某些情况下，如果需要调用一系列API来实现端到端测试流程，这些任务应该在所有API都经过单独测试之后完成。

## 分类API

一个测试项目可能有几个甚至数百个用于测试的API。强烈建议将它们分类，以更好地进行测试管理。它需要采取额外的步骤，但是将大大帮助您创建具有高覆盖率和集成度的测试方案。

同一类别的API共享一些公共信息，例如资源类型，路径等。以相同的结构组织测试将使您的测试在集成流程中可重复使用和扩展。

## 利用自动化功能进行API测试

尽可能早地利用自动化进行API测试。以下是自动化API测试的一些重要好处：

* 测试数据和执行历史记录可以与API信息一起保存。这使得以后重新运行测试变得更加容易。
* API测试稳定且较少更改。API反映了系统的业务规则。API的任何更改都需要明确的要求；因此，测试人员始终可以及时了解更改并进行调整。
* 与Web UI测试相比，测试执行速度要快得多
* API测试被视为灰盒测试，用户可以在其中发送输入数据并获取输出数据以进行验证。数据驱动方法的自动化（即在同一测试场景中应用不同的数据集）可以帮助增加API测试覆盖率
* 数据输入和输出遵循某些特定的模板或模型，因此您只能创建一次测试脚本。这些测试脚本也可以在整个测试项目中重复使用
* API测试可以在软件开发生命周期的早期进行。具有模拟技术的自动化方法可以帮助在开发实际的API之前验证API及其集成。因此，减少了团队内部的依赖性。


## 选择合适的自动化工具

* [如何选择正确的自动化测试工具](https://mp.weixin.qq.com/s/_Ee78UW9CxRpV5MoTrfgCQ)

利用API测试的自动化功能的另一步骤是从市场上的数百种选择中选择最合适的工具或一组合适的工具。选择API自动测试工具时，应考虑以下一些标准：

* 该工具是否支持测试您的AUT（被测应用程序）正在使用的API / Web服务类型？如果您在AUT使用SOAP服务时所选的工具支持测试RESTful服务，则没有任何意义。
* 该工具是否支持您的AUT服务所需的授权方法？以下是您的API可以使用的一些授权方法：No Auth、Bearer Token、Basic auth、Digest Auth、NTLM Authentication、OAuth 1.0、OAuth 2.0、Hawk Authentication、AWS Signature。这是一项必不可少的任务，因为你无法在未经授权的情况下开始测试API。
* 该工具是否支持从WSDL，Swagger，WADL和其他服务规范中导入API / Web服务端点？这是一项可选功能。但是，如果您要测试数百个API，这一点非常重要。
* 该工具是否支持数据驱动的方法？这也是一项可选功能。
* 最后但并非最不重要的一点是，除了API测试之外，您是否还需要执行其他类型的测试，例如WebUI或数据源？API测试在数据源和UI之间的业务层执行。所有这些层都必须进行测试是正常的。支持所有测试类型的工具将是理想的选择，这样您的测试对象和测试脚本可以在所有层之间共享。

## 选择合适的验证方法

当响应状态代码告诉请求状态时，响应主体内容就是API通过给定输入返回的内容。API响应内容因数据类型和大小而异。响应可以是纯文本，JSON数据结构，XML文档等。它们可以是简单的几个单词的字符串（甚至为空），也可以是一百页的JSON/XML文件。因此，必须为给定的API选择合适的验证方法。

通常，有一些验证API响应正文内容的基本方法：

* 将整个响应正文内容与预期信息进行比较，此方法适用于具有静态内容的简单响应。日期时间，增加的ID等动态信息会在断言中引起麻烦。
* 比较响应的每个属性值，对于JSON或XML格式的响应，很容易获得给定键或属性的值。因此，此方法在验证动态内容或单个值而不是整个内容时很有用。
* 比较匹配与正则表达式，与验证单个属性值一起，此方法用于验证具有特定模式的数据响应以处理复杂的动态数据。

每种验证方法都有其优点和缺点，并且没有“一刀切”的选项，需要选择最适合您的测试项目的解决方案。

## 创建正面和负面的测试

API测试需要正向测试和反向测试，以确保API正常运行。由于API测试被视为一种灰盒测试，因此两种类型的测试均由输入和输出数据驱动。

### 正向测试

* 验证API是否已接收输入并按要求中指定的那样返回预期的输出。
* 验证是否按要求指定返回了响应状态代码，无论它返回的是2xx还是错误代码。
* 用最小的必填字段和最大的字段指定输入。

### 反向测试

* 当预期的输出不存在时，请验证API是否返回了适当的响应。
* 执行异常输入验证测试。
* 使用不同的授权级别验证API的行为。

## 现场测试流程

建议在测试过程中安排每天的API测试执行。由于API测试执行快速，稳定且足够小，因此很容易以最小的风险将更多测试添加到当前测试过程中。这只有通过具有以下功能的自动API测试工具才能实现：

* 使用内置测试命令进行测试计划
* 与测试管理工具和缺陷跟踪工具集成
* 与各种领先的CI工具进行持续集成
* 可视日志报告生成

测试过程完成后，每天都可以得到这些测试的结果。如果发生失败的测试，则可以立即检查输出并验证问题以找到适当的解决方案。

## 不要小看API自动化测试

API测试流程非常简单，只需三个主要步骤：

* 发送带有必要输入数据的请求
* 获取具有输出数据的响应
* 验证响应是否按要求返回

API测试最重要的部分既不是发送请求也不是接收响应。它们是测试数据管理和验证。通常，测试一些第一个API（例如登录，查询一些资源等）非常简单。因此，API测试任务很容易被低估。在常规手段方法无法达到你的目的时，使用编程技能可以极大拓展API测试的边界。


---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
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
- [API测试基础](https://mp.weixin.qq.com/s/bkbUEa9CF21xMYSlhPcULw)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)