# Java服务端两个常见的并发错误

[原文地址](https://codingcraftsman.wordpress.com/2020/01/15/two-common-concurrency-bugs/)

理想情况来讲，开发在开始编写代码之前就应该讲并发情况考虑进去，但是大多数实际情况确是，开发压根不会考虑高并发情况下的业务问题。主要原因还是因为业务极难遇到高并发的情况。

下面列举两个比较常见的后端编码中常见的并发BUG：

## Bean中的请求状态

在Java应用程序中，server，controller，处理程序和存储库通常是单例的。它们是在应用启动时创建的，然后请求通常通过多个线程传递给它们。

代码如下：


```Java
public void handleOrder(Order order) {
   ...
   currentLineItem = order.getLine(0);
   processLineItem();
}
 
private void processLineItem() {
   myService.store(currentLineItem);
}
```

这违反了两个原则：线程安全和有意义的对象状态。这里处理一个`order`对象的时候只是处理了其中一个的`currentLineItem`，先是赋值给了当前类对象的属性，然后去处理这个`currentLineItem`对象，但是如果多个线程同时请求到当前的类单例对象，那么赋值和处理对象中间会发生第二次赋值，此时再去处理`currentLineItem`对象很可能已经不是之前`order`对象的`currentLineItem`。

如果将请求的每个属性放入该请求的接收者中，那么将有两个风险：

* 在多线程执行中的请求之间出错
* 如果事情没有完全处理完，则在单线程的请求之间出错


## 对象初始化错误

延迟初始化允许：

由于以下原因，启动速度更快
* 必要时及时加载资源
* 如果不需要，则不加载资源（例如，无服务器Lambda，在其生命周期中可能永远不会被要求执行特定的代码路径）
* 加载优先活动资源

虽然如此，但是，如下代码可能会发生错误：


```Java
private LazyService getLazyService() {
   if (lazyService != null) {
      return lazyService;
   }
   LazyService newLazyService = connectToLazyService();
   registerWithServiceRegistry(newLazyService);
   lazyService = newLazyService;
   return newLazyService;
}
```

尽管它可以工作，但并发调用很可能出错。在示例中：

* 在并发调用中，发生了多个延迟加载
* 如果发生多个延迟加载，则可能两个对象在内存中的停留时间超长或者永远存在
* 如果这是单例，初始化过程中的多余对象可能会获取到唯一的资源导致无法正常工作

为了正确进行单例初始化，您应该使用双重检查锁定或使用框架，甚至使用基于static字段的简单Java单例初始化，如下：


```Java
    private volatile static Singleton singleton;
    public static Singleton getSingleton() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
```

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