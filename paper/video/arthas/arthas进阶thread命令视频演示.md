# arthas进阶thread命令视频演示

之前分享过`arthas`的[快速入门视频演示](https://mp.weixin.qq.com/s/Wl5QMD52isGTRuAP4Cpo-A)，自己打算把这个系列继续做下去。工具还是非常强大的，适用范围也非常的广。在性能测试和性能分析以及故障诊断方面有着非常大的应用。然后和这个工具和`JVM`的一些工具搭配起来会非常非常地有用！如果是想做`Java`服务端的性能测试的话，我觉得这一定是一个绕不过去的一个神器。

你今天给大家分享啊，`arthas`进阶中，比较重要的命令`thread`。它主要负责去查看嗯Java虚拟机里面关于线程的信息。比如：当前`JVM`虚拟机的线程概况、线程的运行状态、线程的优先级等等。当然他也可以查看线程的具体信息，比如：线程的堆栈、线程使用CPU的情况线程的死锁，线程使用对象锁或者说是类锁的状态。还可以根据线程的运行状态进行分类统计，你也可以通过设置统计间隔来达到。对线程的运行消耗CPU的情况的一个监控。

官方文档地址如下：`https://alibaba.github.io/arthas/`

## arthas进阶thread命令

- [点击观看视频](https://mp.weixin.qq.com/s/XuF7Nr1sGC3diIn50zlDDQ)

由于我并没有使用官方的演示`Demo`，自己随手写了一个，下面是代码：


```Groovy
package com.fun

import com.fun.frame.httpclient.FanLibrary

class sdsync extends FanLibrary {

    public static void main(String[] args) {

        Thread thread1 = new Thread({
            1000.times {
                test(it + 10000)
            }
        })
        Thread thread2 = new Thread({
            1000.times {
                test(it + 20000)
            }
        })
        Thread thread3 = new Thread({
            1000.times {
                test(it + 30000)
            }
        })
        Thread thread4 = new Thread({
            1000.times {
                test(it + 40000)
            }
        })
        thread1.start()
        thread2.start()
        thread3.start()
        thread1.join()
        thread2.join()
        thread3.join()

        testOver()
    }

    static void test(int i) {
        output(Thread.currentThread().getName() + "开始!----$i")
        synchronized (sdsync.class) {
            sleep(1000)
        }
        output(Thread.currentThread().getName() + "结束!-----${i}")
    }
}

```

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [多项目登录互踢测试用例](https://mp.weixin.qq.com/s/Nn_CUy_j7j6bUwHSkO0pCQ)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [绑定手机号性能测试](https://mp.weixin.qq.com/s/K5x1t1dKtIT2NKV6k4v5mw)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [简化测试用例](https://mp.weixin.qq.com/s/BhwfDqhN9yoa3Iul_Eu5TA)
- [生产环境中进行自动化测试](https://mp.weixin.qq.com/s/JKEGRLOlgpINUxs-6mohzA)
- [所谓UI测试](https://mp.weixin.qq.com/s/wDvUy_BhQZCSCqrlC2j1qA)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)