# 利用ThreadLocal解决线程同步问题

线程安全是[Java性能测试](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)中绕不过去的一个坎，想要其测试必需对其有所了解，所谓知己知彼百战不殆。之前我也写过一些性能测试中线程安全和线程同步的文章：

- [服务端性能优化之双重检查锁](https://mp.weixin.qq.com/s/-bOyHBcqFlJY3c0PEZaWgQ)
- [Java并发BUG基础篇](https://mp.weixin.qq.com/s/NR4vYx81HtgAEqH2Q93k2Q)
- [Java并发BUG提升篇](https://mp.weixin.qq.com/s/GCRRe8hJpe1QJtxq9VBEhg)
- [如何在匿名thread子类中保证线程安全](https://mp.weixin.qq.com/s/GCXx_-ummi0JfZQ7GTIxig)
- [Java服务端两个常见的并发错误](https://mp.weixin.qq.com/s/5VvCox3eY6sQDsuaKB4ZIw)
- [线程安全类在性能测试中应用](https://mp.weixin.qq.com/s/0-Y63wXqIugVC8RiKldHvg)

但是就运行效能而言，加锁同步又会带来更多的性能消耗，有些得不偿失。在某些并发场景下加锁同步并不是唯一解决线程安全的方法，还有两种，其中一种是基于`CAS`的替代方案，我已经之前文章[线程安全类在性能测试中应用](https://mp.weixin.qq.com/s/0-Y63wXqIugVC8RiKldHvg)中使用的就是这个方案，包括[性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)中也多次使用到这个方案，有兴趣的同学可以再看一看这两篇文章。

下面将另外一个避免同步的方案：避免同步发生的一个方法就是在每个线程中使用不同的对象，这样访问对象时就不存在竞争了。为保证线程安全，很多Java对象是同步的，但是它们未必需要共享。另一方面，很多Java对象创建的成本很高，或者是会占用大量内存。`java.lang.ThreadLocal`这个方法就可以很好解决这个问题。每次有一个线程访问这个对象，就会得到一个新的对象，避免了线程竞争同一个对象，也就用不到同步，可以很大程度提升性能。


下面是Demo：


```Groovy

package com.fun;

import com.fun.frame.SourceCode;

public class AR extends SourceCode {

    static ThreadLocal<String> local = new ThreadLocal<String>() {
        public String initialValue() {
            String s = new String(getNanoMark() + EMPTY);
            output(s.hashCode());
            return s;
        }
    };

    public static void main(String[] args) throws InterruptedException {
        AR ar = new AR();
        ar.ss();
        ar.ss();
        Thread thread = new Thread(() -> ar.ss());
        Thread thread2 = new Thread(() -> ar.ss());
        thread.start();
        thread2.start();
        thread.join();
        thread2.join();
    }

    public void ss() {
        local.get();
    }
    
}
```

控制台打印如下：


```
INFO-> 当前用户：fv，IP：192.168.0.104，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.3
INFO-> 851397249
INFO-> 1851572413
INFO-> 1875674350

Process finished with exit code 0

```

---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [Selenium Python使用技巧（一）](https://mp.weixin.qq.com/s/39v8tXG3xig63d-ioEAi8Q)
- [Selenium Python使用技巧（二）](https://mp.weixin.qq.com/s/uDM3y9zoVjaRmJJJTNs6Vw)
- [Selenium Python使用技巧（三）](https://mp.weixin.qq.com/s/J7-CO-UDspUGSpB8isjsmQ)


## 非技术文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)