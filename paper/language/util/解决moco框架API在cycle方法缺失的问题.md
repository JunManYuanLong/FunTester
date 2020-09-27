# 解决moco框架API在cycle方法缺失的问题
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
我在使用moco框架过程中，遇到一个问题，在官方文档中给出了cycle的方法，表示循环返回一个数组里面的response，但是在查看API的时候并没有发现这个cycle()方法，所以觉得自己写了一个responsehandle，并且重写了cycle()方法。

cycle方法主要用在请求次数相关的内容，比如订单提交、资源删除等场景。


```

package com.fun.moco.support;

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
 * 循环的responsehandle
 */
@SuppressWarnings("all")
public class CycleHandle extends AbstractResponseHandler {

    private final ImmutableList<ResponseHandler> handlers;

    private int index;

    private CycleHandle(final Iterable<ResponseHandler> handlers) {
        this.handlers = copyOf(handlers);
    }

    public static ResponseHandler newSeq(final Iterable<ResponseHandler> handlers) {
        checkArgument(Iterables.size(handlers) > 0, "Sequence contents should not be null");
        return new CycleHandle(handlers);
    }

    @Override
    public void writeToResponse(final SessionContext context) {
        handlers.get((index++) % handlers.size()).writeToResponse(context);
    }

    @Override
    public ResponseHandler apply(final MocoConfig config) {
        if (config.isFor(MocoConfig.RESPONSE_ID)) {
            return super.apply(config);
        }

        FluentIterable<ResponseHandler> transformedResources = from(copyOf(handlers)).transform(applyConfig(config));
        return new CycleHandle(transformedResources.toList());
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
使用方法如下（groovy，有兴趣可以转成java）：

/**
 * 循环返回
 * @param content
 * @param contents
 * @return
 */
    static ResponseHandler cycle(String content, String... contents) {
        CycleHandle.newSeq(FluentIterable.from(asIterable(content, contents)).transform(textToResource()))
    }

/**
 * 循环返回
 * @param handler
 * @param handlers
 * @return
 */
    static ResponseHandler cycle(final ResponseHandler handler, final ResponseHandler... handlers) {
        CycleHandle.newSeq(asIterable(handler, handlers))
    }
```
> groovy是一种基于JVM的动态语言，我觉得最大的优势有两点，第一：于java兼容性非常好，大部分时候吧groovy的文件后缀改成java直接可以用，反之亦然。java的绝大部分库，groovy都是可以直接拿来就用的。这还带来了另外一个优点，学习成本低，非常低，直接上手没问题，可以慢慢学习groovy不同于Java的语法；第二：编译器支持变得更好，现在用的intellij的ide，总体来说已经比较好的支持groovy语言了，写起代码来也是比较顺滑了，各种基于groovy的框架工具也比较溜，特别是Gradle构建工具，比Maven爽很多。----此段文字为了撑字数强加的，与内容无关。

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)


<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---


<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>