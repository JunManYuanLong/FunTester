# CountDownLatch类在性能测试中应用


`CountDownLatch`类位于`java.util.concurrent`包下，利用它可以实现计数器的功能。比如有一堆任务需要多线程去执行，需要在所有任务执行完之后才能进行下一步这个场景，此时就可以利用`CountDownLatch`来实现这种功能了。

## 基本介绍

`CountDownLatch`类只提供了一个构造器，只有一个`int`类型的参数，表示计数器的初始值。

```Java
    /**
     * Constructs a {@code CountDownLatch} initialized with the given count.
     *
     * @param count the number of times {@link #countDown} must be invoked
     *        before threads can pass through {@link #await}
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public CountDownLatch(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.sync = new Sync(count);
    }
```

## 重要方法

前两个是等待所有任务结束的方法，第二个方法有一个超时控制，第一个参数是时间，第二个参数是时间的单位，一般是秒或者毫秒。第三个方法就是计数器减一的方法。性能测试中常用的也就这三个。

```Java
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }
    
    public boolean await(long timeout, TimeUnit unit)
        throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    public void countDown() {
        sync.releaseShared(1);
    }

```

## 实践

下面是我写的性能框架用到的地方，首先是构建任务：


```Java
   /**
     * 执行多线程任务
     */
    public PerformanceResultBean start() {
        startTime = Time.getTimeStamp();
        for (int i = 0; i < threadNum; i++) {
            ThreadBase thread = getThread(i);
            thread.setCountDownLatch(countDownLatch);
            executorService.execute(thread);
        }
        shutdownService(executorService, countDownLatch);
        endTime = Time.getTimeStamp();
        threads.forEach(x -> {
            if (x.status()) failTotal++;
            errorTotal += x.errorNum;
            excuteTotal += x.excuteNum;
        });
        logger.info("总计{}个线程，共用时：{} s,执行总数:{},错误数:{},失败数:{}", threadNum, Time.getTimeDiffer(startTime, endTime), excuteTotal, errorTotal, failTotal);
        return over();
    }
```


下面是多线程基类`run()`用到的地方：


```Java
    @Override
    public void run() {
        try {
            before();
            List<Long> t = new ArrayList<>();
            long ss = Time.getTimeStamp();
            long et = ss;
            while (true) {
                try {
                    threadmark = mark == null ? EMPTY : this.mark.mark(this);
                    long s = Time.getTimeStamp();
                    doing();
                    et = Time.getTimeStamp();
                    excuteNum++;
                    long diff = et - s;
                    t.add(diff);
                    if (diff > HttpClientConstant.MAX_ACCEPT_TIME) marks.add(diff + CONNECTOR + threadmark);
                    if ((et - ss) > time || status() || key) break;
                } catch (Exception e) {
                    logger.warn("执行任务失败！", e);
                    logger.warn("执行失败对象的标记:{}", threadmark);
                    errorNum++;
                }
            }
            long ee = Time.getTimeStamp();
            logger.info("执行次数：{}, 失败次数: {},总耗时: {} s", excuteNum, errorNum, (ee - ss) / 1000 + 1);
            Concurrent.allTimes.addAll(t);
            Concurrent.requestMark.addAll(marks);
        } catch (Exception e) {
            logger.warn("执行任务失败！", e);
        } finally {
            after();
        }

    }

```

下面是`after()`方法的内容：


```Java
    @Override
    protected void after() {
        super.after();
        marks = new ArrayList<>();
        GCThread.stop();
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
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)
- [有关UI测试计划](https://mp.weixin.qq.com/s/D0fMXwJF754a7Mr5ARY5tQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)