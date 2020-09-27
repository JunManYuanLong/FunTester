# 创建Java守护线程

[原文地址](https://dzone.com/articles/creating-daemon-thread-in-java)

# Java中的守护程序线程是什么

`Java`运行时利用特殊类型的线程来执行后台任务，这称为守护程序线程。这些支持线程管理诸如垃圾收集之类的后后任务。守护程序线程是特殊的，因为如果`JVM`中运行的唯一线程是守护程序线程，则Java运行时将关闭或退出。

* 在Java中使用守护程序线程时，请特别小心地记住，运行时关闭时，守护程序线程的任务可能在执行过程中意外终止。

在Java中将线程创建为守护进程就像调用`setDaemon()`方法一样简单。设置`true`意味着线程是一个守护进程；`false`表示不是。默认情况下，所有线程的初始值均为`false`，如下：

```Java API
 /**
     * Marks this thread as either a {@linkplain #isDaemon daemon} thread
     * or a user thread. The Java Virtual Machine exits when the only
     * threads running are all daemon threads.
     *
     * <p> This method must be invoked before the thread is started.
     *
     * @param  on
     *         if {@code true}, marks this thread as a daemon thread
     *
     * @throws  IllegalThreadStateException
     *          if this thread is {@linkplain #isAlive alive}
     *
     * @throws  SecurityException
     *          if {@link #checkAccess} determines that the current
     *          thread cannot modify this thread
     */
    public final void setDaemon(boolean on) {
        checkAccess();
        if (isAlive()) {
            throw new IllegalThreadStateException();
        }
        daemon = on;
    }
```

# 创建守护程序线程Demo

主线程创建一个守护程序线程，该线程每1秒显示打印一条消息。然后，主线程休眠5秒钟。当守护程序线程仍在执行时，程序结束，因为当前唯一正在执行的线程是守护程序线程，所以守护进程也会结束。


```Java
import org.slf4j.Logger

class TSSS extends FanLibrary {

    public static Logger logger = getLogger(TSSS.class)

    public static void main(String[] args) {
        Thread daemonThread = new Thread(new Runnable() {

            public void run() {
                // Repeat forever
                while (true) {
                    logger.info("守护线程正在运行!" + TAB + Time.getDate());
                    sleep(1000)
                }
            }
        });
        daemonThread.setDaemon(true);
        daemonThread.start();
        sleep(5000)
        logger.info("主线程结束!")
    }

}
```

# 控制台输出


```Java
INFO-> 当前用户：fv，IP：192.168.0.102，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.4
INFO-> 守护线程正在运行!	2020-05-24 22:02:36
INFO-> 守护线程正在运行!	2020-05-24 22:02:37
INFO-> 守护线程正在运行!	2020-05-24 22:02:38
INFO-> 守护线程正在运行!	2020-05-24 22:02:39
INFO-> 守护线程正在运行!	2020-05-24 22:02:40
INFO-> 主线程结束!

Process finished with exit code 0
```

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester十八张原创专辑](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**，合作请联系`Fhaohaizi@163.com`。

### 热文精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [删除List中null的N种方法--最后放大招](https://mp.weixin.qq.com/s/4mfskN781dybyL59dbSbeQ)
- [测试如何处理Java异常](https://mp.weixin.qq.com/s/H00GWiATOD8QHJu3UewrBw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [Selenium并行测试基础](https://mp.weixin.qq.com/s/OfXipd7YtqL2AdGAQ5cIMw)
- [Selenium并行测试最佳实践](https://mp.weixin.qq.com/s/-RsQZaT5pH8DHPvm0L8Hjw)
- [性能测试、压力测试和负载测试](https://mp.weixin.qq.com/s/g26lpd7d7EtpN7pkiqkkjg)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)