# CyclicBarrier类在性能测试中应用


`CyclicBarrier`也叫同步屏障，在`JDK1.5`被引入，可以让一组线程达到一个屏障时被阻塞，直到最后一个线程达到屏障时，所以被阻塞的线程才能继续执行。在执行很多个任务，但是这些任务中间某个节点需要等到其他任务都执行到固定的节点才能继续进行，先到达的线程会一直等待所有线程到达这个节点。在性能测试中，经常会遇到**N多个**用户同时在线的场景，一般处理起来都是先让这**N多个**用户登录，然后保持登录状态，然后去并发请求。这个场景下`CyclicBarrier`就能完美解决我们的需求。

## 基本介绍

`CyclicBarrier`类常用的构造方法有两个：1、只有一个`int`类型的参数，表示参加等待的线程数，这一点跟`CountDownLatch`类一样；2、构造方法多了一个`Runnable`参数，这个表示所有线程都到达等待节点后执行的线程任务，网上大多数用赛跑的发令枪做比喻，很形象。当所有线程都到达准备好之后，发令枪就响了。


```Java

    /**
     * Creates a new {@code CyclicBarrier} that will trip when the
     * given number of parties (threads) are waiting upon it, and
     * does not perform a predefined action when the barrier is tripped.
     *
     * @param parties the number of threads that must invoke {@link #await}
     *        before the barrier is tripped
     * @throws IllegalArgumentException if {@code parties} is less than 1
     */
    public CyclicBarrier(int parties) {
        this(parties, null);
    }


       /**
     * Creates a new {@code CyclicBarrier} that will trip when the
     * given number of parties (threads) are waiting upon it, and which
     * will execute the given barrier action when the barrier is tripped,
     * performed by the last thread entering the barrier.
     *
     * @param parties the number of threads that must invoke {@link #await}
     *        before the barrier is tripped
     * @param barrierAction the command to execute when the barrier is
     *        tripped, or {@code null} if there is no action
     * @throws IllegalArgumentException if {@code parties} is less than 1
     */
    public CyclicBarrier(int parties, Runnable barrierAction) {
        if (parties <= 0) throw new IllegalArgumentException();
        this.parties = parties;
        this.count = parties;
        this.barrierCommand = barrierAction;
    }

```

## 重要方法

使用方法比较简单，构造方法完成后，之后一个方法`await()`，这个方法用来表示到达节点后开始等待其他线程到达，同样的，还有一个重载方法，增加了超时设置，两个参数：1、时间；2、时间单位。如果该方法报了超时异常，那么其他等待线程到达这个方法后会报`BrokenBarrierException`这个异常。由于`CyclicBarrier`对象的`await()`方法在同一线程中是可以多次调用的，相当于任务分成了很多阶段，一旦某一个线程的某一个任务阶段报错，会导致其他线程同样的任务阶段都报错，进而可能导致所有现成任务报错失败。

如果当前调用是最后一个调用，则唤醒所有其它的线程的等待并且如果在构造`CyclicBarrier`时指定了`action`，当前线程会去执行该`action`，然后该方法返回该线程调用`await`的次序（getParties()-1说明该线程是第一个调用await的，0说明该线程是最后一个执行await的），接着该线程继续执行`await`后的代码；如果该调用不是最后一个调用，则阻塞等待；如果等待过程中，当前线程被中断，则抛出`InterruptedException`；如果等待过程中，其它等待的线程被中断，或者其它线程等待超时，或者该`barrier`被`reset`，或者当前线程在执行`barrier`构造时注册的`action`时因为抛出异常而失败，则抛出`BrokenBarrierException`。

`reset() `该方法会将该`barrier`重置为它的初始状态，并使得所有对该`barrier`的`await`调用抛出`BrokenBarrierException`。


```Java
       public int await() throws InterruptedException, BrokenBarrierException {
        try {
            return dowait(false, 0L);
        } catch (TimeoutException toe) {
            throw new Error(toe); // cannot happen
        }
    }

    public int await(long timeout, TimeUnit unit)
        throws InterruptedException,
               BrokenBarrierException,
               TimeoutException {
        return dowait(true, unit.toNanos(timeout));
    }

    public void reset() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            breakBarrier();   // break the current generation
            nextGeneration(); // start a new generation
        } finally {
            lock.unlock();
        }
    }
    
```

## 实践

下面是我写的一个测试`Demo`，第一个线程我估计写了1秒的等待，出发超时报错的。

```Java
  public static void main(String[] args) {
        CyclicBarrier very_good = new CyclicBarrier(2, new Runnable() {
            @Override
            public void run() {
                logger.warn("very good");
            }
        });

        new Thread(() -> {
            logger.info("111111111");
            try {
                very_good.await(1, TimeUnit.SECONDS);
                sleep(1);
                very_good.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            sleep(2);
            logger.info("222222222");
            try {
                very_good.await();
                very_good.await();
//                very_good.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();

        testOver();
    }
```

如果想实验`Runnable`参数的调用的话，可以放开超时，控制台输出如下：


```Java
INFO-> 当前用户：fv，IP：192.168.0.107，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 111111111
INFO-> 222222222
WARN-> very good
WARN-> very good

Process finished with exit code 0

```

--- 
* 公众号**FunTester**首发，更多原创文章：[FunTester430+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/7VG7gHx7FUvsuNtBTJpjWA)
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)
- [有关UI测试计划](https://mp.weixin.qq.com/s/D0fMXwJF754a7Mr5ARY5tQ)
- [Java服务端两个常见的并发错误](https://mp.weixin.qq.com/s/5VvCox3eY6sQDsuaKB4ZIw)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)
- [JMeter参数签名——Groovy脚本形式](https://mp.weixin.qq.com/s/wQN9-xAUQofSqiAVFXdqug)
- [微软Zune闰年BUG分析](https://mp.weixin.qq.com/s/zpqAUcNcHaZjWUdUYH_loQ)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)