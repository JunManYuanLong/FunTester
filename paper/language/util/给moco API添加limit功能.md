# 给moco API添加limit功能
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

在使用moco API的时候，发现文档中的一些功能并不能满足构建测试服务的需求，需要自己开发一些功能。之前两篇主要讲了moco本身的补充，本篇说说moco文档之外的功能：limit。
主要是用于限制访问次数，并不针对某个session或者同一个用户（本人暂时没有这方面的需求，故没有开发）。
使用场景的话：小游戏的里面的抽奖，订单提交，耗时较长的功能等。在实际的业务逻辑中，很可能会有短时间内不允许提交多次，请求多次的需求。

下面上代码：

```
package com.fun.moco.support;

import com.fun.utils.Time;
import com.github.dreamhead.moco.HttpRequest;
import com.github.dreamhead.moco.MocoConfig;
import com.github.dreamhead.moco.ResponseHandler;
import com.github.dreamhead.moco.handler.AbstractResponseHandler;
import com.github.dreamhead.moco.internal.SessionContext;
import com.github.dreamhead.moco.model.MessageContent;
import com.google.common.base.Function;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 循环的responsehandle
 */
@SuppressWarnings("all")
public class LimitHandle extends AbstractResponseHandler {


    private final ResponseHandler limit;

    private final ResponseHandler unlimit;

    private Map<String, Long> tatal = new ConcurrentHashMap<>();

    private int interval;

    private LimitHandle(final ResponseHandler limit, final ResponseHandler unLimit, int interval) {
        this.limit = limit;
        this.unlimit = unLimit;
        this.interval = interval;
    }

    public static ResponseHandler newSeq(final ResponseHandler limit, final ResponseHandler unLimit, int interval) {
        return new LimitHandle(limit, unLimit, interval);
    }

    /**
     * 返回响应
     *
     * @param context
     */
    @Override
    public void writeToResponse(final SessionContext context) {
        HttpRequest request = (HttpRequest) context.getRequest();
        String uri = request.getUri();
        MessageContent content = request.getContent();
        (limited(uri + content) ? limit : unlimit).writeToResponse(context);
    }

    @Override
    public ResponseHandler apply(final MocoConfig config) {
        if (config.isFor(MocoConfig.RESPONSE_ID)) {
            return super.apply(config);
        }
        return new LimitHandle(limit, unlimit, interval);
    }

    private Function<ResponseHandler, ResponseHandler> applyConfig(final MocoConfig config) {
        return new Function<ResponseHandler, ResponseHandler>() {
            @Override
            public ResponseHandler apply(final ResponseHandler input) {
                return input.apply(config);
            }
        };
    }

    /**
     * 判断是否被限制
     * <p>
     * 通过记录每一次响应的时间戳，判断两次请求间隔达到limit目的
     * </p>
     *
     * @param info
     * @return
     */
    public boolean limited(String info) {
        long fresh = Time.getTimeStamp();
        long old = tatal.containsKey(info) ? tatal.get(info) : 0L;
        tatal.put(info, fresh);
        return fresh - old > interval;
    }
}
```
使用方法如下：


```
/**
 * 限制请求频次，默认1000ms
 * @param limit
 * @param unlimit
 * @return
 */
    static ResponseHandler limit(String limited, String unlimited) {
        limit contentResponse(limited), contentResponse(unlimited)
    }

    static ResponseHandler limit(JSONObject limited, JSONObject unlimited) {
        limit jsonResponse(limited), jsonResponse(unlimited)
    }

    static ResponseHandler limit(ResponseHandler limited, ResponseHandler unlimited) {
        limit limited, unlimited, 1000
    }

/**
 * 限制请求频次
 * @param limit
 * @param unlimit
 * @param interval 单位ms
 * @return
 */
    static ResponseHandler limit(String limited, String unlimited, int interval) {
        limit contentResponse(limited), contentResponse(unlimited), interval
    }

    static ResponseHandler limit(JSONObject limited, JSONObject unlimited, int interval) {
        limit limited.toString(), unlimited.toString(), interval
    }

    static ResponseHandler limit(ResponseHandler limit, ResponseHandler unlimit, int interval) {
        LimitHandle.newSeq(limit, unlimit, interval)
    }
```
* groovy是一种基于JVM的动态语言，我觉得最大的优势有两点，第一：于java兼容性非常好，大部分时候吧groovy的文件后缀改成java直接可以用，反之亦然。java的绝大部分库，groovy都是可以直接拿来就用的。这还带来了另外一个有点，学习成本低，非常低，直接上手没问题，可以慢慢学习groovy不同于Java的语法；第二：编译器支持变得更好，现在用的intellij的ide，总体来说已经比较好的支持groovy语言了，写起代码来也是比较顺滑了，各种基于groovy的框架工具也比较溜，特别是Gradle构建工具，比Maven爽很多。----此段文字为了撑字数强加的，与内容无关。



> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)


<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---


<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>