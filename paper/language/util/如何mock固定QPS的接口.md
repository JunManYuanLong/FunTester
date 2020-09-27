# 如何mock固定QPS的接口

在做接口测试的时候，经常会遇到一些接口去调用其他服务接口，或者调用第三方接口。在进行压测的时候就会遇到问题，因为很难隔离掉其他服务和第三方接口的性能变化情况，虽然单独维护一套压测环境可以解决服务调用的问题，但是这需要很多资源和精力投入，并不一定适合每个团队。至于第三方就更难掌握，能够提供一些性能数据就很不错了。

为此我们需要用到`mock`一个固定`QPS`的接口这样的功能。我的解决方案是基于[moco API](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1320663294239260672&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)，利用本身提供的功能做一些尝试，很不幸失败了，在花费一个小时左右翻阅官方文档和实现Demo以及自己尝试发现这条路走不通。

只能无奈放弃，然后自己拓展这个功能了。采取的方案是`JDK`中的`Semaphore`类控制流量，然后通过创建自定义`ResponseHandler`来完成接口的限流，思路是拿到令牌的请求线程休眠一段时间再去释放令牌完成响应。

经过我的测试误差都在10%以内，如果是测试方案设计得好，误差应该是5%以内，这里有几条规律：

* 请求线程越多，误差越小
* 请求次数越多，误差越小
* 系统充分预热，误差越小

测试过程，改天录个视频给大家分享。

# 使用Demo


```Groovy
        HttpServer server = getServer(8088)

        server.get(urlOnly("/aaa")).response(qps(textRes("faun"), 10))

        server.response("haha")

        MocoServer drive = run(server)


        waitForKey("fan")

        drive.stop()
```

# 封装方法


```Groovy

/**
 * 创建固定QPS的ResponseHandler,默认QPS=1
 * @param handler
 * @return
 */
    static ResponseHandler qps(ResponseHandler handler) {
        QPSHandler.newSeq(handler, 1000)
    }

/**
 * 创建固定QPS的ResponseHandler
 * @param handler
 * @param gap
 * @return
 */
    static ResponseHandler qps(ResponseHandler handler,int gap) {
        QPSHandler.newSeq(handler, gap)
    }
    
```

# ResponseHandler实现类


```Groovy

package com.fun.moco.support


import com.github.dreamhead.moco.ResponseHandler
import com.github.dreamhead.moco.handler.AbstractResponseHandler
import com.github.dreamhead.moco.internal.SessionContext
import com.github.dreamhead.moco.util.Idles

import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

import static com.google.common.base.Preconditions.checkArgument 
/**
 * 固定QPS的接口实现类
 */
class QPSHandler extends AbstractResponseHandler {


    private static final Semaphore semaphore = new Semaphore(1, true);
    /**
     * 访问间隔
     */
    private final int gap

    private final ResponseHandler handler

    private QPSHandler(ResponseHandler handler, int gap) {
        this.gap = gap
        this.handler = handler
    }

    public static ResponseHandler newSeq(final ResponseHandler handler, int gap) {
        checkArgument(handler != null, "responsehandler 不能为空!");
        return new QPSHandler(handler, gap);
    }


/**
 * 具体实现,这里采用微秒,实验证明微秒更准确
 * @param context
 */
    @Override
    void writeToResponse(SessionContext context) {
        semaphore.acquire()
        Idles.idle(gap * 1000, TimeUnit.MICROSECONDS)
        handler.writeToResponse(context)
        semaphore.release()
    }

}

```


---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester十八张原创专辑](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**，合作请联系`Fhaohaizi@163.com`。

### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [测试如何处理Java异常](https://mp.weixin.qq.com/s/H00GWiATOD8QHJu3UewrBw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [Selenium并行测试最佳实践](https://mp.weixin.qq.com/s/-RsQZaT5pH8DHPvm0L8Hjw)
- [性能测试、压力测试和负载测试](https://mp.weixin.qq.com/s/g26lpd7d7EtpN7pkiqkkjg)
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)