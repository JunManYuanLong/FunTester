# Phaser类在性能测试中应用

`Phaser`的功能与`CountDownLatch`和`CyclicBarrier`有部分重叠，同时提供了更丰富的语义和更灵活的用法。`Phaser`比较适合这样一种场景，一种任务可以分为多个阶段，现希望多个线程去处理该批任务，对于每个阶段，多个线程可以并发进行，但是希望保证只有前面一个阶段的任务完成之后才能开始后面的任务。这种场景可以使用多个`CyclicBarrier`来实现，每个`CyclicBarrier`负责等待一个阶段的任务全部完成。但是使用`CyclicBarrier`的缺点在于，需要明确知道总共有多少个阶段，同时并行的任务数需要提前预定义好，且无法动态修改。而`Phaser`可同时解决这两个问题，可以随时在任务过程中*增加*、*删除*需要等待的个数。

比如下面这个[性能测试](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)场景：**N多个**老师有**N**个班级（为空），一次性并发**N多个**学生请求加入的消息，老师并发处理，然后清空班级学生，重新进入循环。这里面涉及**N**个循环，也就是多个任务阶段。但是在任务阶段可能会遇到老师处理完消息，清空班级学生信息的期间，学生和班级的关联关系同步延迟的情况，导致学生无法正常请求加入班级，这可能会导致该线程的某次任务进入了其他**支线业务逻辑**，这跟期望是不相符的，所以要判断，如果该学生请求加入班级接口响应非期望响应的时候，可以注销当前线程，退出循环。等下下一次循环开始重新注册加入，然后继续测试。

这个场景使用`CyclicBarrier`也是可以实现的，就是略微麻烦，而且进入**支线业务逻辑**的线程很大可能会干扰到其他正常测试的线程，会把异常线程的测试数据记录到结果中，导致测试结果不够准确。

## 基本介绍

`Phaser`类常用的构造方法有1个：只有一个`int`类型的参数，表示参加等待的线程数，这一点跟`CountDownLatch`类一样。这里还有几个构造方法，都是涉及`parent`的，太复杂了，测试用不到，所以就不讲了。

```Java

    /**
     * Creates a new phaser with the given number of registered
     * unarrived parties, no parent, and initial phase number 0.
     *
     * @param parties the number of parties required to advance to the
     * next phase
     * @throws IllegalArgumentException if parties less than zero
     * or greater than the maximum number of parties supported
     */
    public Phaser(int parties) {
        this(null, parties);
    } 
```

## 重要方法

使用方法比较简单，构造方法完成后，之后一个方法`await()`，这个方法用来表示到达节点后开始等待其他线程到达，同样的，还有一个重载方法，增加了超时设置，两个参数：1、时间；2、时间单位。如果该方法报了超时异常，那么其他等待线程到达这个方法后会报`BrokenBarrierException`这个异常。由于`CyclicBarrier`对象的`await()`方法在同一线程中是可以多次调用的，相当于任务分成了很多阶段，一旦某一个线程的某一个任务阶段报错，会导致其他线程同样的任务阶段都报错，进而可能导致所有现成任务报错失败。

`arriveAndAwaitAdvance()`当前线程当前阶段执行完毕，等待其它线程完成当前阶段。如果当前线程是该阶段最后一个未到达的，则该方法直接返回下一个阶段的序号（阶段序号从0开始），同时其它线程的该方法也返回下一个阶段的序号。

`arrive()`该方法不作任何等待，直接返回下一阶段的序号。
`awaitAdvance(int phase) `该方法等待某一阶段执行完毕。如果当前阶段不等于指定的阶段或者该Phaser已经被终止，则立即返回。该阶段数一般由`arrive()`方法或者`arriveAndDeregister()`方法返回。返回下一阶段的序号，或者返回参数指定的值（如果该参数为负数），或者直接返回当前阶段序号（如果当前`Phaser`已经被终止）。

* 还有两个带超时功能的方法：
* `awaitAdvanceInterruptibly(int phase) `效果与`awaitAdvance(int phase)`相当，唯一的不同在于若该线程在该方法等待时被中断，则该方法抛出`InterruptedException`。
* `awaitAdvanceInterruptibly(int phase, long timeout, TimeUnit unit) `效果与`awaitAdvanceInterruptibly(int phase)`相当，区别在于如果超时则抛出`TimeoutException`。


```Java
     public int register() {
        return doRegister(1);
    }
    
       public int arriveAndDeregister() {
        return doArrive(ONE_DEREGISTER);
    }
    
       public int arriveAndAwaitAdvance() {
        // Specialization of doArrive+awaitAdvance eliminating some reads/paths
        final Phaser root = this.root;
        for (;;) {
            long s = (root == this) ? state : reconcileState();
            int phase = (int)(s >>> PHASE_SHIFT);
            if (phase < 0)
                return phase;
            int counts = (int)s;
            int unarrived = (counts == EMPTY) ? 0 : (counts & UNARRIVED_MASK);
            if (unarrived <= 0)
                throw new IllegalStateException(badArrive(s));
            if (UNSAFE.compareAndSwapLong(this, stateOffset, s,
                                          s -= ONE_ARRIVAL)) {
                if (unarrived > 1)
                    return root.internalAwaitAdvance(phase, null);
                if (root != this)
                    return parent.arriveAndAwaitAdvance();
                long n = s & PARTIES_MASK;  // base of next state
                int nextUnarrived = (int)n >>> PARTIES_SHIFT;
                if (onAdvance(phase, nextUnarrived))
                    n |= TERMINATION_BIT;
                else if (nextUnarrived == 0)
                    n |= EMPTY;
                else
                    n |= nextUnarrived;
                int nextPhase = (phase + 1) & MAX_PHASE;
                n |= (long)nextPhase << PHASE_SHIFT;
                if (!UNSAFE.compareAndSwapLong(this, stateOffset, s, n))
                    return (int)(state >>> PHASE_SHIFT); // terminated
                releaseWaiters(phase);
                return nextPhase;
            }
        }
    }
    
      public int awaitAdvance(int phase) {
        final Phaser root = this.root;
        long s = (root == this) ? state : reconcileState();
        int p = (int)(s >>> PHASE_SHIFT);
        if (phase < 0)
            return phase;
        if (p == phase)
            return root.internalAwaitAdvance(phase, null);
        return p;
    }
     
    public boolean isTerminated() {
        return root.state < 0L;
    }
     

```

## 实践

下面是我写的一个测试`Demo`，基本是按照赛跑的逻辑思路写的。在创建`Phaser`对象的时候，可以重写`onAdvance()`，这个方法主要是线程都到达等待节点的方法，重写可以增加日志记录。

```Java
public class AR extends FanLibrary {

    public static void main(String[] args) throws InterruptedException {

        final Phaser phaser = new Phaser(3) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("====== " + phase + " ======");
                return registeredParties == 0;
            }
        };
        for (int index = 0; index < 3; index++) {
            new Thread(new player(phaser), "player" + index).start();
        }
        phaser.register();
        System.out.println("Game Start");
        //注销当前线程,比赛开始
        sleep(2);
        phaser.arriveAndDeregister();
        //是否非终止态一直等待
//        sleep(3);
        int i = phaser.awaitAdvance(2);
        output(i);

        while (!phaser.isTerminated()) {
            sleep(1000);
            output(111111111111L);
        }
        System.out.println("Game Over");
    }


    static class player implements Runnable {

        private final Phaser phaser;

        player(Phaser phaser) {
            this.phaser = phaser;
        }

        @Override
        public void run() {
//            try {
            // 第一阶段——等待创建好所有线程再开始
            phaser.arriveAndAwaitAdvance();
            sleep(1000);
            // 第二阶段——等待所有选手准备好再开始
//                Thread.sleep((long) (Math.random() * 1000));
            System.out.println(Thread.currentThread().getName() + " ready");
//            phaser.arriveAndAwaitAdvance();

            phaser.arriveAndAwaitAdvance();

            phaser.arriveAndAwaitAdvance();
            sleep(1000);
            phaser.arriveAndAwaitAdvance();
            sleep(1000);
            phaser.arriveAndAwaitAdvance();
            sleep(1000);

            // 第三阶段——等待所有选手准备好到达，到达后，该线程从phaser中注销，不在进行下面的阶段。
//                Thread.sleep((long) (Math.random() * 1000));
            System.out.println(Thread.currentThread().getName() + " arrived");
            phaser.arriveAndDeregister();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }


    }
```

--- 
* 公众号**FunTester**首发，更多原创文章：[FunTester430+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/7VG7gHx7FUvsuNtBTJpjWA)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [运行越来越快的Java热点代码](https://mp.weixin.qq.com/s/AP0BcDEjDuaouaB0RXJOoQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)