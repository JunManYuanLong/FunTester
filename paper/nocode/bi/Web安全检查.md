# Web安全检查

[原文地址](https://www.softwaretestingclass.com/top-10-web-security-checks-how-to-test-for-a-secure-website/)

在使用成熟的框架编写Web应用程序时，有时候开发会处于永无止境的修改=>测试=>修改=>测试的状态。尽管如此，开发人员更专注于更改的功能和可视输出，而在安全性方面花费的时间却少得多。但是，当他们确实专注于安全性时，通常会想到的就是典型的事情，例如防止SQL注入或访问控制错误，但是对安全性的关注应该远远超过这些。

在测试Web应用程序时，这个列表提供一个常见的几种导致安全漏洞的原因和预防办法。虽然这还远远不够全面，但是可以为测试提供一定参考。

## 单元测试

前面我们提到了修改=>测试=>修改=>测试的循环。与此有关的一个问题是，所做的更改会影响到测试内容之外的其他功能，例如在代码的多个模块复用了这个功能。测试框架允许通过使用带有某些参数的函数并断言预期的结果来确保功能与预期一致，从而防止发生安全事件（例如，防止`isAdmin()`函数错误地允许管理员权限）。

## 访问控制

我们还提到了访问控制错误，这些错误在定制开发的应用程序中至关重要。这就像是用户提升自己的特权或访问未经授权访问的内容的能力。应当采取严格访问控制限制来验证执行受限操作和内容的高权限。

## 变更跟踪/版本控制


跟踪代码或配置文件的更改对于许多安全问题至关重要：功能可靠性，跟踪修改记录，确保黑客没有进行任何篡改等等。跟踪配置文件的更改，并使用源代码管理（Git，SVN）可确保在何时何地更改了哪些内容。

## 管理权限

当在较大的团队中工作时，拥有管理员级别权限的人数可能会增加。不仅是服务器上的root权限，还包括对Git存储库的写访问或云服务器账户中的实例访问权限。审核日志有助于跟踪谁做了什么，但是更重要的是确保合适的人员拥有所需的访问权限。

## 最小特权

从上面的权限内容继续，应该始终明确人员都采用最小特权的概念。在授予访问权限时，要考虑的一个重要问题是，所需的最少特权是多少？Web应用程序不需要访问整个文件系统，需要不需要访问数据库，依此类推。

## 异地冗余

前面提到的大多数要点都需要一定程度的日志记录才能完全有效。将日志存储在本地而不是远程存储，从而有可能篡改跟踪记录。此外，异地备份和多地冗余可实现更好的稳定性，正常运行时间和灾难恢复。

## 监控

冗余有利于恢复，但还可以采取监控措施提高安全性。强制性监控可以及时发现问题发生的时间，而不是找出发生问题的时间，因此可以在重大故障发生之前进行防范措施。良好的监控还会寻找级联效应的可能性，例如，一项服务中断会使依赖它的其他服务的整个集群瘫痪。

## 加密

太多的Web应用程序仍然允许通过非SSL连接和其他各种未加密流量进行纯文本身份验证。数据存储也不总是安全的，例如使用易破解的MD5或SHA1密码加密哈希算法存储的密码。确保使用加密安全的TLS证书和哈希算法（建议使用加盐的SHA512），可以大大减少未经授权的数据访问的脆弱性。

## Web安全扫描程序


将每个功能和每个用户操作组合起来，安全风险的可能性呈指数增长。甚至安全团队都不可能手动验证和测试所有内容，但是好的自动Web安全扫描程序不仅可以测试漏洞，而且可以发现一些开发人员可能不会考虑的问题。

## SQL注入

当然，SQL注入是最常见的安全问题。近年来，SQL注入仍然是OWASP前10名中的第一名。使用准备好的SQL语句和处理用户输入内容是防止中招的两种重要方法。

# 总结

正如之前所说，该列表不是全面的。确实，一个真正全面的列表会是数以千计，其本身也拥有大量的书籍资料。安全性是要了解每个潜在的极端情况，超越常规的思维方式。

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