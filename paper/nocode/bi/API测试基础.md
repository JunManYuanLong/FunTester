# API测试基础



在进行API测试之前，我们先了解一下

## 什么是API？

API（全称Application Programming Interface）是两个单独的软件系统之间的通信和数据交换。实现API的软件系统包含可以由另一个软件系统执行的功能/子例程。

## 什么是API测试

API测试是一种用于验证API（应用程序编程接口）的软件测试类型。它与GUI测试非常不同，主要集中在软件体系结构的业务逻辑层。在API测试中，您无需使用标准的用户输入（键盘）和输出，而是使用软件将调用发送到API，获取输出并记下系统的响应。


API测试需要可以通过API进行交互的应用程序。为了测试API，您需要

* 使用测试工具调用API
* 编写自己的代码调用API

## API测试的测试用例：
API测试的测试用例基于

* 基于输入条件的返回值：相对容易测试，因为可以定义输入并可以验证结果
* 不返回任何内容：没有返回值时，将检查系统上的API行为
* 触发其他一些API /事件/中断：如果API的输出触发了某些事件或中断，则应跟踪这些事件和中断侦听器
* 更新数据结构：更新数据结构将对系统产生某些结果或影响，应进行身份验证
* 修改某些资源：如果API调用修改了某些资源，则应通过访问相应资源来对其进行验证


## API测试方法：
以下几点可帮助用户进行API测试：

* 了解API程序的功能并明确定义程序范围
* 应用诸如等效类，边界值分析和错误猜测之类的测试技术，并为API编写测试用例
* API的输入参数需要适当计划和定义
* 执行测试用例，并比较预期结果和实际结果。

* API测试和单元测试之间的区别

| 单元测试	| API测试 |
| ----- | ----- |
|开发人员执行它|测试人员执行它|
|单独的功能经过测试|端到端功能经过测试|
|开发人员可以访问源代码|测试人员无法访问源代码|
|还涉及UI测试|仅测试API函数|
|仅测试基本功能|所有功能问题均经过测试|
|范围有限|范围更广|
|通常在办理登机手续前运行|创建完成后运行|
## 如何进行API测试

API测试应至少涵盖除常规SDLC流程以外的以下测试方法：

* 发现测试：测试组应手动执行API中记录的一组调用，例如验证是否可以列出，创建和删除API公开的特定资源。
* 可用性测试：此测试可验证API是否功能正常且用户友好。API是否也可以与其他平台很好地集成
* 安全测试：此测试包括需要哪种身份验证以及是否通过HTTP加密敏感数据或同时通过这两种方法对敏感数据进行加密
* 自动化测试： API测试应以创建一组脚本或可用于定期执行API的工具为最终结果
* 文档：测试团队必须确保文档足够，并提供足够的信息来与API交互。文档应成为最终交付成果的一部分
## API测试的最佳做法：
* 测试用例应按测试类别分组
* 在每个测试的顶部，您应包括被调用的API的声明。
* 测试用例中应明确提及参数选择
* 确定API函数调用的优先级，以便测试人员轻松进行测试
* 每个测试用例应尽可能独立且独立于依赖项
* 在开发中避免“测试链”
* 处理诸如-Delete，CloseWindow等一次性调用函数时必须格外小心。
* 呼叫排序应执行且计划合理
* 为了确保完整的测试范围，请为API的所有可能的输入组合创建测试用例。
## API测试检测到的错误类型
* 无法优雅地处理错误情况
* 未使用的标志
* 功能缺失或重复
* 可靠性问题。难以连接API并从API获得响应。
* 安全问题
* 多线程问题
* 性能问题。API响应时间非常高。
* 错误的错误/警告呼叫者
* 对有效参数值的错误处理
* 响应数据的结构不正确（JSON或XML）
## API测试工具
由于API和单元测试都是目标源代码，因此可以使用工具/框架进行自动化。

* Parasoft SOAtest
* Runscope
* Postman
* Curl
* Cfix
* Check
* CTESK
* dotTEST
* Eclipse SDK tool- Automated API testing

## API测试的挑战
API测试的挑战包括：

* Web API测试中的主要挑战是参数组合，参数选择和调用排序
* 没有可用于测试应用程序的 GUI ，这很难提供输入值
* 对测试人员而言，在不同系统中验证和验证输出几乎没有困难
* 测试人员必须知道参数的选择和分类
* 异常处理功能需要测试
* 测试人员必须具备编码知识
# 结论：
API由代表业务逻辑层的一组类/函数/过程组成。如果未正确测试API，则可能不仅会导致API应用程序出现问题，还会导致调用应用程序出现问题。它是软件工程中必不可少的测试。


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

## 非技术文章精选

- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [测试人员如何成为变革的推动者](https://mp.weixin.qq.com/s/0nTZHBOuKG0rewKAeyIqwA)
- [编写测试用例的技巧](https://mp.weixin.qq.com/s/zZAh_XXXGOyhlm6ebzs06Q)
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)