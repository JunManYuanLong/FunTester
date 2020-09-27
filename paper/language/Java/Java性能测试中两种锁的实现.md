# Java性能测试中两种锁的实现

[原文地址](https://howtodoinjava.com/java/multi-threading/object-vs-class-level-locking/)

在使用`Java`进行性能测试过程中，经常会遇到线程同步代码锁的使用，同步内容的对象、方法、代码块。

同步是使所有并发线程在执行中保持同步的过程。同步避免了由于共享内存视图不一致而导致的内存一致性错误。当方法被声明为同步时，该线程持有监视器或锁定对象为这个方法的对象。如果另一个线程正在执行同步方法，则该线程将被阻塞，直到该线程释放这个锁。

* 可以通过`synchronized`在类中的已定义方法或块上使用。

# 对象级别锁

对象级锁是机制，当我们要同步非静态方法或者非静态代码块，使得只有一个线程就可以在类的给定实例执行的代码块，以确保实例级数据线程安全。对象级别锁定可以通过以下方式完成：


```Java
public class DemoClass {

    public synchronized void demoMethod() {
	   	 //搞点事情()
    }
}


public class DemoClass {

    public void demoMethod() {
        synchronized (this) {
            //搞点事情()
        }
    }
}


public class DemoClass {

    private final Object lock = new Object();

    public void demoMethod() {
        synchronized (lock) {
            //搞点事情()
        }
    }
}
```

# 类级别锁

类级别锁可防止多个线程`synchronized`在运行时进入该类的所有可用实例中的任何一个块中。这意味着，如果在运行时有100个实例`DemoClass`，则一次只能在一个实例中的一个线程上执行一个线程`demoMethod()`，而所有其他实例将被其他线程锁定。

应该始终执行类级别锁定，以使静态数据线程安全。我们知道`static`关键字将方法的数据关联到类级别，因此在静态字段或方法上使用锁定使其成为类级别。


```Java
public class DemoClass {

    public synchronized static void demoMethod() {
            //搞点事情()
    }
}

public class DemoClass {

    public void demoMethod() {
        synchronized (DemoClass.class) {
            //搞点事情()
        }
    }
}

public class DemoClass {

    private final static Object lock = new Object();

    public void demoMethod() {
        synchronized (lock) {
            //搞点事情()
        }
    }
}
```

# 重要说明

* `Java`中的同步保证了没有两个线程可以同时或并发执行需要相同锁的同步方法。
* `synchronized`关键字只能与方法和代码块一起使用。这些方法或块可以是静态的还是非静态两种。
* 每当线程进入`Java synchronized`方法或块时，它都会获得一个锁，而每当它离开同步方法或块时，它将释放该锁。即使线程在完成后或由于任何错误或异常而离开同步方法时，也会释放锁定。
* `Java synchronized`关键字本质上是可重入的，这意味着如果一个同步方法调用了另一个需要相同锁的同步方法，则持有锁的当前线程可以进入该方法而无需获取锁。
* `NullPointerException`如果在同步块中使用的对象为`null`，则将引发`NullPointerException`。例如，在上面的代码示例中，如果将锁初始化为`null`，则`synchronized (lock)`将抛出`NullPointerException`。
* `Java`中的同步方法使您的应用程序性能降低。
* 静态同步和非静态同步方法都可能同运行，因为它们锁定在不同的对象上。
* 根据`Java`语言规范，不能`synchronized`在构造函数中使用关键字。
* 不要在`Java`中的同步块上的非`final`字段上进行同步。因为非最终字段的引用可能随时更改，然后不同的线程可能会在不同的对象上进行同步，即完全没有同步。
* 不要使用`String`文字，因为它们可能在应用程序中的其他地方被引用，并且可能导致死锁。使用`new`关键字创建的字符串对象可以安全使用。但是，作为最佳实践，请在我们要保护的共享变量本身上创建一个新的私有作用域`Object`实例锁。


---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester十八张原创专辑](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**，合作请联系`Fhaohaizi@163.com`。


## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [接口测试视频专题](https://mp.weixin.qq.com/s/4mKpW3QiVRee3kcVOSraog)
- [删除List中null的N种方法--最后放大招](https://mp.weixin.qq.com/s/4mfskN781dybyL59dbSbeQ)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [自动化策略六步走](https://mp.weixin.qq.com/s/He69k8iCKhTKD1j-yV6M5g)
- [功能自动化测试策略](https://mp.weixin.qq.com/s/qHmcblN4cD4JK6jT7oU4fQ)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
