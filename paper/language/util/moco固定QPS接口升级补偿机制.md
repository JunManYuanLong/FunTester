# moco固定QPS接口升级补偿机制

之前写过一篇[如何mock固定QPS的接口](https://mp.weixin.qq.com/s/yogj9Fni0KJkyQuKuDYlbA)，中间用到了流量控制类`Semaphore`和线程安全的知识。当时在测试过程中，多线程并发请求，`QPS`误差率在**5%**以内，觉得算是在可接受范围内的，所以并没有做进一步优化。

最近看了一篇有关*开源*代码文章，有点感触，就在原来的基础上做了优化。主要思路是新建一个线程，通过计算理论值和实际值的差距，通过一个线程安全的对象完成这个补偿修正。

核心代码如下：


```Groovy
package com.fun.moco.support

import com.fun.frame.SourceCode
import com.github.dreamhead.moco.ResponseHandler
import com.github.dreamhead.moco.handler.AbstractResponseHandler
import com.github.dreamhead.moco.internal.SessionContext
import com.github.dreamhead.moco.util.Idles

import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

import static com.google.common.base.Preconditions.checkArgument

/**
 * 固定QPS的接口实现类
 */
class QPSHandler extends AbstractResponseHandler {


    private static final Semaphore semaphore = new Semaphore(1, true);
    /**
     * 访问间隔,使用微秒控制
     */
    private final int gap

    private final ResponseHandler handler

    /**
     * 用于记录收到第一个请求的时间
     */
    private long start

    /**
     * 用于统计已处理请求的总次数,因为用了流量控制,所以不适用安全类
     */
    private int times = 0

    /**
     * 用于统计实际的请求数和预期请求数直接的差距,由于在真实场景下预期的QPS总是大于实际QPS,所以只处理diff为正值的情况
     */
    private AtomicInteger diff = new AtomicInteger(0)

    private QPSHandler(ResponseHandler handler, int gap) {
        this.gap = gap * 1000
        this.handler = handler
    }

    public static ResponseHandler newSeq(final ResponseHandler handler, int gap) {
        checkArgument(handler != null, "responsehandler 不能为空!");
        def handler1 = new QPSHandler(handler, gap)
        handler1.thread.start()
        return handler1;
    }


    /**
     * 具体实现,这里采用微秒,实验证明微秒更准确
     * @param context
     */
    @Override
    void writeToResponse(SessionContext context) {
        if (start == 0) start = SourceCode.getNanoMark()
        semaphore.acquire()
        if (diff.getAndIncrement() <= 0) Idles.idle(gap, TimeUnit.MICROSECONDS)
        times++
        semaphore.release()
        handler.writeToResponse(context)
    }

    /**
     * 用于定时计算实际处理请求与预期处理请求数量差距,补偿缺失部分的多线程
     */
    private Thread thread = new Thread(new Runnable() {

        @Override
        void run() {
            while (true) {
                SourceCode.sleep(30_000)
                long present = SourceCode.getNanoMark()
                def t0 = present - start
                def t1 = times * gap
                if (t0 - t1 > gap) diff.getAndSet((t0 - t1) / gap)
            }
        }
    })

}

```

* `times`属性我并没有使用线程安全类，因为执行`times++`的时候已经是单线程执行了，过多使用线程安全类会使`QPS`误差更大。
* `diff`属性的正负值问题。在实际使用时发现`diff`的值总是正值，也就是期望`QPS`总是大于实际的`QPS`，这个跟响应的代码执行和`Idles.idle()`方法的误差有关系。

----
**公众号[FunTester](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)首发，原创分享爱好者，腾讯云和掘金社区首页推荐，知乎七级原创作者，欢迎关注、交流，禁止第三方擅自转载。**

FunTester热文精选
=

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [开源测试服务](https://mp.weixin.qq.com/s/ZOs0cp_vt6_iiundHaKk4g)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)
- [测试开发工程师工作技巧](https://mp.weixin.qq.com/s/TvrUCisja5Zbq-NIwy_2fQ)
- [Fiddler Everywhere工具答疑](https://mp.weixin.qq.com/s/2peWMJ-rgDlVjs3STNeS1Q)
- [Selenium4 IDE，它终于来了](https://mp.weixin.qq.com/s/XNotlZvFpmBmBQy1pYifOw)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)