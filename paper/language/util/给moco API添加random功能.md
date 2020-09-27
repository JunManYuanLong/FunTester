# 给moco API添加random功能

<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

使用moco API快速搭建测试挡板服务的时候，有一些接口需求是随机返回固定的几个响应体，但是moco API提供并未提供此功能，幸好有先前增加limit功能的经验，这次很顺利解决了这个问题。

handle代码如下：


```
package com.fun.moco.support;


import com.fun.frame.SourceCode;
import com.github.dreamhead.moco.MocoConfig;
import com.github.dreamhead.moco.ResponseHandler;
import com.github.dreamhead.moco.handler.AbstractResponseHandler;
import com.github.dreamhead.moco.internal.SessionContext;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.ImmutableList.copyOf;


/**
 * 随机的responsehandle
 */
public class RandomHandler extends AbstractResponseHandler {

    private final ImmutableList<ResponseHandler> handlers;

    private RandomHandler(final Iterable<ResponseHandler> handlers) {
        this.handlers = copyOf(handlers);
    }

    public static ResponseHandler newSeq(final Iterable<ResponseHandler> handlers) {
        checkArgument(Iterables.size(handlers) > 0, "Sequence contents should not be null");
        return new RandomHandler(handlers);
    }

    /**
     * getrandom随机获取一个响应
     *
     * @param context
     */
    @Override
    public void writeToResponse(final SessionContext context) {
        handlers.get(SourceCode.getRandomInt(handlers.size()) - 1).writeToResponse(context);
    }

    @Override
    public ResponseHandler apply(final MocoConfig config) {
        if (config.isFor(MocoConfig.RESPONSE_ID)) {
            return super.apply(config);
        }

        FluentIterable<ResponseHandler> transformedResources = from(copyOf(handlers)).transform(applyConfig(config));
        return new RandomHandler(transformedResources.toList());
    }

    private Function<ResponseHandler, ResponseHandler> applyConfig(final MocoConfig config) {
        return new Function<ResponseHandler, ResponseHandler>() {
            @Override
            public ResponseHandler apply(final ResponseHandler input) {
                return input.apply(config);
            }
        };
    }
}

```

封装好的使用方法：


```
/**
 * 随机
 * @param content
 * @param contents
 * @return
 */
    static ResponseHandler random(String content, String... contents) {
        RandomHandler.newSeq(FluentIterable.from(asIterable(content, contents)).transform(textToResource()))
    }

/**
 * 随机返回
 * @param json
 * @param jsons
 * @return
 */
    static ResponseHandler random(JSONObject json, JSONObject... jsons) {
        RandomHandler.newSeq(FluentIterable.from(asIterable(json.toString(), jsons.toList().stream().map { x -> x.toString() }.toArray() as String[])).transform(textToResource()))
    }

/**
 * 随机
 * @param handler
 * @param handlers
 * @return
 */
    static ResponseHandler random(ResponseHandler handler, ResponseHandler... handlers) {
        RandomHandler.newSeq(asIterable(handler, handlers))
    }
```

使用方法如下：


```
        server.get(urlOnly("/find")).response(random("43242=342","e343=fdsf","3242"))
        
```
### 往期文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
8. [测试之JVM命令脑图](https://mp.weixin.qq.com/s/qprqyv0j3SCvGw1HMjbaMQ)
9. [将json数据格式化输出到控制台](https://mp.weixin.qq.com/s/2IPwvh-33Ov2jBh0_L8shA)
10. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
11. [“双花”BUG的测试分享](https://mp.weixin.qq.com/s/0dsBsssNfg-seJ_tu9zFaQ)

## [公众号地图](https://mp.weixin.qq.com/s/36RbP20beZ8oWJ9nLAxG3g) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)



> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)


<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---


<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>