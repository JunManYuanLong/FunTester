# Java并发BUG基础篇



## 使用线程安全对象

### 共享对象

线程主要通过共享对相同对象的访问进行通信。因此，在对象变化时读取可能会产生意外的结果。同样，同时更改对象可能会使它处于不一致的状态。

避免此类并发问题编写可靠代码的主要方法是使用不可变对象，因为它们的状态无法通过多线程的干扰进行修改。

但是，我们不能总是使用不可变的对象。在这些情况下，我们必须找到使可变对象成为线程安全的方法。

### 集合类线程安全

像任何其他对象一样，集合在内部维护状态。这可以通过多个线程同时更改集合来更改。因此，我们可以在多线程环境中安全使用集合的一种方法是同步它们：

```Java
Map<String, String> map = Collections.synchronizedMap(new HashMap<>());
List<Integer> list = Collections.synchronizedList(new ArrayList<>());
```
一般来说，同步有助于我们实现互斥。更具体地说，一次只能由一个线程访问这些集合。因此，我们可以避免使集合处于不一致状态。

### 多线程集合

现在让我们考虑一个场景，我们需要更多的读取而不是写入。通过使用同步集合，应用程序可能会因此导致性能下降。如果两个线程要同时读取集合，则一个线程必须等待另一个线程完成。

因此，Java提供了并发集合，例如`CopyOnWriteArrayList`  和  `ConcurrentHashMap`，可以由多个线程同时访问它们：

```Java
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
Map<String, String> map = new ConcurrentHashMap<>();
```

上述的`CopyOnWriteArrayList`通过为像添加或删除这样的可变操作，创建底层单独的副本实现线程安全。尽管它的写操作性能比Collections.synchronizedList差，但是当我们需要的读操作比写操作多时，它为我们提供了更好的性能。

`ConcurrentHashMap`从根本上讲是线程安全的，并且比围绕非线程安全`Map`的`Collections.synchronizedMap`包装器性能更高。它是真正的线程安全map实现类，允许在其子映射中同时发生不同的操作。

### 使用非线程安全类型

我们经常使用诸如`SimpleDateFormat`之类的内置对象来解析和格式化日期对象。`SimpleDateFormat`类在执行操作时会更改其内部状态。

我们需要非常小心，因为它们不是线程安全的。由于竞争条件等原因，它们的状态在多线程应用程序中可能变得不一致，从而导致BUG的发生。

那么，如何安全地使用`SimpleDateFormat`？我们有几种选择：

* 每次使用时创建一个SimpleDateFormat的新实例
* 通过使用`ThreadLocal<SimpleDateFormat>`限制对象创建的对象数。它保证每个线程都有自己的SimpleDateFormat实例
* 将多个线程的并发访问与synced关键字或锁进行同步

`SimpleDateFormat`只是其中的一个示例。我们可以将这些技术用于任何非线程安全类型。

## 竞争条件

当两个或多个线程访问共享数据并且它们试图同时更改它们时，就会发生竞争状态。因此，竞争条件可能导致运行时错误或意外结果。

### 竞争条件示例

让我们考虑以下代码：


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
该计数器类的设计使得增量方法的每次调用将增加1。但是，如果从多个线程引用了Counter对象，则线程之间的干扰可能会破坏此事件按预期发生。[快看，i++真的不安全](https://mp.weixin.qq.com/s/-CdWdROKSEq_ZiLX2kWxzA)

我们可以将`counter ++`语句分解为3个步骤：

* 检索计数器的当前值
* 将检索到的值增加1
* 将增加的值存回计数器

现在，让我们假设两个线程，线程1和线程2，调用在同一时间的增量方法。他们交错的动作可能遵循以下顺序：

* thread1读取计数器的当前值; 0
* thread2读取计数器的当前值; 0
* thread1增加检索到的值；结果是1
* thread2增加检索到的值；结果是1
* thread1将结果存储在计数器中 ; 现在的结果是1
* thread2将结果存储在计数器中；现在的结果是1

我们预计该计数器的值为2，但值为1。

### 基于同步的解决方案

我们可以通过同步关键代码来解决不一致问题：


```Java
class SynchronizedCounter {
    private int counter = 0;
 
    public synchronized void increment() {
        counter++;
    }
 
    public synchronized int getValue() {
        return counter;
    }
}

```
任何时候都只允许一个线程使用对象的同步方法，因此这在计数器的读写中强制了一致性。但是这个方案也存在问题，无论怎样都会有获取锁和释放锁的过程，会降低性能。

### 解决方案

我们可以将上述代码替换为内置的`AtomicInteger`对象。此类提供除其他外的原子方法，用于增加整数，是比编写自己的代码更好的解决方案。因此，我们可以直接调用其方法而无需同步：


```Java
AtomicInteger atomicInteger = new AtomicInteger(3);
atomicInteger.incrementAndGet();
```
在这种情况下，`SDK`可以为我们解决问题。否则，我们得可以编写自己的代码，将关键部分封装在自定义线程安全的类中。这种方法有助于我们最大程度地减少代码的复杂性并最大程度地提高代码的可重用性。

## Collections的竞争条件

### 问题

我们可以陷入的另一个陷阱是，认为同步集合提供的保护是完全可以信赖的。

让我们阅读下面的代码：

```Java
List<String> list = Collections.synchronizedList(new ArrayList<>());
if(!list.contains("FunTester")) {
    list.add("FunTester");
}
```

我们列表的每个操作都是同步的，但是多个方法调用的任何组合都不会同步。更具体地说，在两个操作之间，另一个线程可以修改我们的集合，从而导致不良结果。

例如，两个线程可以同时进入`if`块，然后更新列表，每个线程将`FunTester`值添加到列表中。

### 解决方案

我们可以使用同步保护代码避免一次被多个线程访问：


```Java
synchronized (list) {
    if (!list.contains("FunTester")) {
        list.add("FunTester");
    }
}
```
我们没有在函数中添加`synchronized`关键字，而是创建了一个与`list`有关的关键部分，该部分一次只允许一个线程执行此操作。我们可以对列表对象的其他操作使用 `synchronized (list)`，以保证一次只有一个线程可以对此对象执行任何操作。

### `ConcurrentHashMap`的内置解决方案

在`ConcurrentHashMap`中提供了这种类型的问题更好的解决方案。我们可以使用其原子的`putIfAbsent`方法：


```Java
Map<String, String> map = new ConcurrentHashMap<>();
map.putIfAbsent("foo", "bar");
```

或者，如果我们想计算该值，则使用其原子的`computeIfAbsent`方法：

`map.computeIfAbsent("foo", key -> key + "bar");`

我们应该注意，这些方法是`Map`接口的一部分  ，它们提供了一种便捷的方法来避免围绕插入编写条件逻辑。当尝试进行多线程调用时，它们确实可以帮助我们。

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