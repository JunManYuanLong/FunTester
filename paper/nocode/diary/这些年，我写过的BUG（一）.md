# 这些年，我写过的BUG（一）

最近测试项目开发完，本来自测了几天直接上线（基本都是冒烟用例和正向的功能用例），因为是个内部项目，所以没想做很严格的测试。但这周刚好腾出来测试人手了，专门安排了一个`Web端`测试童鞋来测试。还是发现了一些**BUG**的，下面分享一下自己写过的BUG。

# 字符长度

在创建和编辑测试用例的时候，用例名称会有一些长度的限制，本来预计是32个长度就足够了，太长了列表不好展示，所以一直也是按照一般名称32个长度开发的。结果测试今天发现产品文档上明确写着**1-60**个长度，只好进行修改。又由于新建库表的时候没考虑周全，所以也得修改数据库的设置，着实麻烦。

总结起来，还是因为开发过程中，过于注重功能的实现，缺少对产品细节的了解，包括一些提示语等等。在之前的产品会议上也没太注意提示语这块，导致了一些**边界值**测试用例无法通过。

**个人总结**：项目开发，如果时间比较紧，优先实现功能，的确会不太关注这些细节，再加上这些的限制条件并没有统一规范，基本上都需要查看产品文档的细节说明才能得到结果，很难不遗漏。这类问题在自测过程中也很难去发现，只能通过**专业人员**的**专业测试**才会发现，改起来也比较容易。

# 技术文档

项目里面有几个筛选框，例如用例筛选，筛选项：**环境**、**服务**、**模块**、**接口**、**是否本人**、**是否可用**、**关联项目**等等，每个选项都不是必选，但是都需要一个默认值，大部分基础数据来源于服务端。关于这个默认值有两个状态:1、页面默认空，接口默认传空字符串；2、页面默认`全部`，接口默认传一个`int`值（目前为0）。在和前端商定接口文档时，发现了两种状态处理起来比较麻烦，所以统一都采用了**1**这个方案，由此与产品文档有出入，在**专业测试**的坚持下，决定以*产品文档*为准进行修改，就是这两种筛选状态同时存在。

总结来讲，这类问题其实应该采取统一的方案，对于后端和前端来讲都比较方便，省去很多工作量。特别是后端在进行参数校验的时候，会省去非常大的维护成本。现在都是使用`validation`注解来完成参数验证，同样的一个参数，不同接口校验规则不一样，对我来讲，会让人干到头大。

**个人总结**：第一次开发完整的后端项目，很多事情不如想象的顺利。给我的经验就是：**统一**，**统一**，**再统一**，规范越早越详尽越好，文档越详尽越好，技术文档越直观越好。人的精力有限，很多限制和规则不一定都会记得清楚，再加上这些规范和规则很可能会一改再改。最好的办法还是制定一些**凌驾于产品文档**的通用规则，包括技术文档。

--- 
* 公众号**FunTester**首发，更多原创文章：[FunTester420+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/7VG7gHx7FUvsuNtBTJpjWA)
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)
- [有关UI测试计划](https://mp.weixin.qq.com/s/D0fMXwJF754a7Mr5ARY5tQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)