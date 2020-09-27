# mock延迟响应的接口

在使用[moco API](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1320663294239260672&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)做接口模拟的过程中，遇到一个模拟接口响应时间的问题。有些情况下是需要进行延迟响应的，比如我想**mock**一个响应时间超过5s的接口，以观察端上会如何处理这种情况。

[moco API](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1320663294239260672&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)本身是提供一个延迟的**API**，但是经过尝试之后发现，这个**API**只是用来异步请求接口的，并不能支持延迟响应这个需求，所以我又拓展了这个功能。

# 使用Demo


```Groovy
        HttpServer server = getServer(8088)

        server.get(urlOnly("/aba")).response(delay(textRes("faun"), 5000))

        server.response("haha")

        MocoServer drive = run(server)


        waitForKey("fan")

        drive.stop()
```

# 封装方法


```Groovy

/**
 * 延迟响应
 * @param handler
 * @param time 时间,单位ms,存在理论BUG,不能低于50ms
 * @return
 */
    static ResponseHandler delay(ResponseHandler handler, int time) {
        DelayHandler.newSeq(handler, time)
    }

/**
 * 延迟响应,默认1000ms
 * @param handler
 * @return
 */
    static ResponseHandler delay(ResponseHandler handler) {
        DelayHandler.newSeq(handler, 1000)
    }

```

# ResponseHandler实现类


```Groovy
package com.fun.moco.support


import com.github.dreamhead.moco.ResponseHandler
import com.github.dreamhead.moco.handler.AbstractResponseHandler
import com.github.dreamhead.moco.internal.SessionContext

import java.util.concurrent.TimeUnit

import static com.google.common.base.Preconditions.checkArgument

/**
 * 延迟响应API拓展ResponseHandler
 */
class DelayHandler extends AbstractResponseHandler {

    /**
     * 延迟时间
     */
    private final int time

    private final ResponseHandler handler

    private DelayHandler(ResponseHandler handler, int time) {
        this.time = time
        this.handler = handler
    }

    public static ResponseHandler newSeq(final ResponseHandler handler, int time) {
        checkArgument(handler != null, "responsehandler 不能为空!");
        return new DelayHandler(handler, time);
    }

/**
 *
 * @param context
 */
    @Override
    void writeToResponse(SessionContext context) {
        com.github.dreamhead.moco.util.Idles.idle(time, TimeUnit.MILLISECONDS)
        handler.writeToResponse(context)
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
- [如何mock固定QPS的接口](https://mp.weixin.qq.com/s/yogj9Fni0KJkyQuKuDYlbA)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [Selenium并行测试最佳实践](https://mp.weixin.qq.com/s/-RsQZaT5pH8DHPvm0L8Hjw)
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)
- [有关UI测试计划](https://mp.weixin.qq.com/s/D0fMXwJF754a7Mr5ARY5tQ)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)