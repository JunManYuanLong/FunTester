# 如何正确执行功能API测试



测试曾经在GUI级别进行，但开发人员已经意识到它是多么脆弱。本文将讲述更多API测试以及如何使其最佳运行。

API或应用程序接口是一种通信方法系统，它使开发人员和非开发人员能够访问程序，过程，函数和服务。API中使用的最常见协议是HTTP以及REST架构。使用REST编程的开发人员可以轻松理解他们的代码。他们和其他人知道他们将使用哪种语言，功能如何工作，可以使用哪些参数等。

开发API的流行框架包括Swagger，WADL和RAML。理想情况下，在编程时，开发人员会形成一个“API契约”，它描述了如何使用API​​中开发的服务。

在此标准化之前，编程就像狂野西部的草原放飞自我。开发人员以他们认为合适的方式访问他们的代码，并且很难开发公共服务并使其可用，因为有许多方法可以编写代码。SOAP是标准化的第一次尝试，但现在REST是主导者。

API测试可创建更可靠的代码。但从历史上看，测试更多在在GUI级别进行。当开发人员完成他们的工作时，他们会将其交给QA工程师。测试工程师的时间有限，因此他们会在最高级别的GUI上测试代码。测试工作将涵盖前端和后端开发。

这适用于手动测试和自动化测试的开始，但不适合敏捷和连续测试的时代。GUI测试过于脆弱，GUI自动化脚本很容易奔溃不稳定。此外，团队不能等待整个系统更新，并且在测试发生之前准备好GUI。

在敏捷时代，测试必须在较低级别进行，即在API级别进行。开发人员甚至可以自己完成。由于“API契约”，API测试甚至可以在开发完成之前测试准备阶段。这意味着开发人员可以根据预先编写的测试（又称测试驱动开发）验证他们的代码。

但尽管已经知道API测试的重要性，但并不总是这样做。敏捷开发人员没有时间。平均而言，开发人员每周只有很少的时间写代码，剩下的时间用于测试，文档，验证和会议。所以他们更倾向于强行冲刺，进行手动测试，但这只需要太长时间。在两周内完成功能性API测试非常困难，还需要开发，测试，验证并完成文档编写。

自动化API测试可以加快开发速度，并节省开发人员做其他事情的时间，比如编写代码。自动化还可以更轻松地覆盖整个测试范围：正面，负面，边缘情况，SQL注入等。这样可以确保没有任何机会，所有参数和排列都经过测试。试图测试其API的敏捷开发小组可能会测试一个或两个正面测试流程，或者一个正面测试流程和一个负面测试流程，并称之为成功。但这不是彻底的API测试，并且为不必要的发布风险打开了大门，因为错过了许多变体并且未实现完全验证。

例如，假设API采用作者姓名和图书发布日期。将测试名称和日期，看看它们是否有效。一旦正确收到响应，API就可以运行。

但是负面和边缘情况呢？例如，插入一个正确的日期但没有书，或更改日期格式，或一年中不存在的正确日期格式，或长名称，或插入向数据库授予数据的SQL代码等。这些仅是需要测试的许多变体中的一些示例，即使它们未在合同中涵盖。

开发人员和测试人员需要一种简单的方法来创建涵盖所有这些方面的测试。我们建议您寻找可以获取Swagger或其他框架文件的解决方案，根据您的API合同对其进行全面测试，并将其作为持续集成流程的一部分进行运行。这可确保您专注于开发强大而耐用的代码。


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