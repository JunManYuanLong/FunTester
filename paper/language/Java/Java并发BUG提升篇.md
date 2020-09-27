# Java并发BUG提升篇

书接上文：[Java并发BUG基础篇](https://mp.weixin.qq.com/s/NR4vYx81HtgAEqH2Q93k2Q)


## 内存一致性问题

当多个线程访问为相同数据的结果不一致时，将发生内存一致性问题。

根据Java内存模型，除`主内存（RAM）`外，每个CPU都有自己的缓存。因此，任何线程都可以缓存变量，因为与主内存相比，它提供了更快的访问速度。

### 问题

让我们回想一下我们的Counter示例：


```Java
class Counter {
    private int counter = 0;
 
    public void increment() {
        counter++;
    }
 
    public int getValue() {
        return counter;
    }
}
```

让我们考虑以下情形：线程1递增计数器，然后线程2读取其值。可能会发生以下事件序列：

* thread1从其自己的缓存中读取计数器值；计数器为0
* thread1递增计数器并将其写回到其自己的缓存中；计数器是1
* thread2从其自己的缓存中读取计数器值；计数器为0

当然也可能不会发生这样的错误，`thread2`将读取正确的值`（1）`，但不能保证一个线程所做的更改每次都会对其他线程可见。

### 解决方案

为了避免内存一致性错误，我们需要建立一个事前发生的关系。这种关系只是对一个特定语句的内存更新对另一特定语句可见的保证。

有几种策略可以创建事前发生的关系。其中之一是同步，已经介绍过了。同步可确保互斥和内存一致性。但是，这会带来性能成本。

我们也可以通过使用`volatile`关键字来避免内存一致性问题。简而言之，对`volatile`变量的每次更改始终对其他线程可见。

让我们使用`volatile`重写`Counter`示例：

```Java
class SyncronizedCounter {
    private volatile int counter = 0;
 
    public synchronized void increment() {
        counter++;
    }
 
    public int getValue() {
        return counter;
    }
}
```

我们应该注意，我们仍然需要同步增量操作，因为`volatile`不能确保我们相互排斥。使用简单的原子变量访问比通过同步代码访问这些变量更有效。

## 滥用同步

同步机制是一个强大的工具来实现线程安全。它依赖于内部和外部锁的使用。我们还记得以下事实：每个对象都有一个不同的锁，一次只能有一个线程获得一个锁。

但是，如果我们不注意并为关键代码仔细选择正确的锁，则可能会发生意外行为。

### 引用同步

方法级同步是许多并发问题的解决方案。但是，如果使用过多，它也可能导致其他并发问题。这种同步方法依赖于此引用作为锁定，也称为固有锁定。

在以下示例中，我们可以看到如何使用引用作为锁，将方法级同步转换为块级同步。

这些方法是等效的：


```Java
public synchronized void foo() {
    //dosomething()
}

public void foo() {
    synchronized(this) {
      //dosomething()
    }
}
```

当线程调用这种方法时，其他线程无法同时访问该对象。由于所有操作最终都以单线程运行，因此这可能会降低并发性能。当读取的对象多于更新的对象时，此方法特别糟糕。

此外，我们代码的客户端也可能会获得此锁。在最坏的情况下，此操作可能导致死锁。

### 死锁

死锁描述了两个或多个线程相互阻塞，每个线程等待获取某个其他线程持有的资源的情况。

让我们考虑示例：


```Java
public class DeadlockExample {
 
    public static Object lock1 = new Object();
    public static Object lock2 = new Object();
 
    public static void main(String args[]) {
        Thread threadA = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("ThreadA: Holding lock 1...");
                sleep();
                System.out.println("ThreadA: Waiting for lock 2...");
 
                synchronized (lock2) {
                    System.out.println("ThreadA: Holding lock 1 & 2...");
                }
            }
        });
        Thread threadB = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("ThreadB: Holding lock 2...");
                sleep();
                System.out.println("ThreadB: Waiting for lock 1...");
 
                synchronized (lock1) {
                    System.out.println("ThreadB: Holding lock 1 & 2...");
                }
            }
        });
        threadA.start();
        threadB.start();
    }
}

```
在上面的代码中，我们可以清楚地看到第一个`ThreadA`获取`lock1`，而`ThreadB`获取`lock2`。然后，`ThreadA`中尝试获取`lock2`，其已经被`threadB`获取而`threadB`尝试获取`lock1`，其已经被`ThreadA`获取。因此，他们两个都不会继续运行，这意味着他们陷入了死锁。

我们可以通过更改其中一个线程的锁定顺序来轻松解决此问题。

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