# 超大对象导致Full GC超高的BUG分享

在某次测试的过程中，突然发现后端底层`user`服务突然就挂了，用户量并不大，几个人用着用着就不行了。中间层发现大量超时报错，后来去查看`user`服务的GC日志，发现了一个非常奇怪的现象：`Full GC`次数竟然比`Young GC`次数还高。下图是停止请求之后的`GC`统计：

![](http://pic.automancloud.com/QQ20200219-233914.png)

中间某个时刻抓到的一秒内两次`Full GC`异常情况：

![](http://pic.automancloud.com/1582122953847_7608AD8D-8757-4D72-8593-9D9C233EBEDF.png)

然后去翻看了`GC日志`，发现了很多次`GC`失败的信息：

![](http://pic.automancloud.com/QQ20200219-234150.png)

随后为了让服务正常跑起来，配置翻了倍，然后停止所有测试，只留一个账号用于调试，发现勉强还能撑住，最终才发现了问题所在：中间层服务调用后端查询接口时候，限制数量的参数没传进去，导致每次查询都查的全量信息，大概4万多条。

每一个中间层过来的请求，`user`一开始还能处理，很快就不行了，因为创建了非常大的`List`，再加上查询消耗资源较多，所以服务就挂了。

但是，为什么`Full GC`会比`Young GC`，我就想起来前两天看的书里面，关于Java分配大对象的知识，如下：

![](http://pic.automancloud.com/QQ20200219-234823.png)

原来超大对象可能会被直接在`老生代`里面，然后就导致了`Full GC`频繁，由于内存不足，就导致了`Full GC`失败。

---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [Selenium 4.0 Alpha更新日志](https://mp.weixin.qq.com/s/tU7sm-pcbpRNwDU9D3OVTQ)
- [Selenium 4.0 Alpha更新实践](https://mp.weixin.qq.com/s/yT9wpO5o5aWBUus494TIHw)
- [如何统一接口测试的功能、自动化和性能测试用例](https://mp.weixin.qq.com/s/1xqtXNVw7BdUa03nVcsMTg)

## 非技术文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)
- [API测试基础](https://mp.weixin.qq.com/s/bkbUEa9CF21xMYSlhPcULw)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCxr0Sa2MXpNKicZE024zJm7vIAFRC09bPV9iaMer9Ncq8xppcYF73sDHbrG2iaBtRqCFibdckDTcojKg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)