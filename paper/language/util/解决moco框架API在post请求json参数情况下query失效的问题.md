# 解决moco框架API在post请求json参数情况下query失效的问题
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

在使用moco API做接口虚拟化的过程中遇到一个比较棘手的问题，就是根据官方文档提供的案例，并不能跑通post请求在处理json传参格式的虚拟化。经过查询源码，发现了一个问题：
源码：



```
public class ParamRequestExtractor extends HttpRequestExtractor<String[]> {
    private final String param;

    public ParamRequestExtractor(final String param) {
        this.param = param;
    }

    @Override
    protected Optional<String[]> doExtract(final HttpRequest request) {
        String[] reference = request.getQueries().get(this.param);
        return fromNullable(reference);
    }
}
```

在获取请求的内容时，发现该方法不能获取到正确的请求参数，后来索性自己重写了一个Extractor类，内容如下：

```
package com.fun.moco.support;

import com.github.dreamhead.moco.HttpRequest;
import com.github.dreamhead.moco.HttpRequestExtractor;
import com.github.dreamhead.moco.RequestExtractor;
import com.google.common.base.Optional;
import net.sf.json.JSONObject;

import static com.github.dreamhead.moco.util.Preconditions.checkNotNullOrEmpty;
import static com.google.common.base.Optional.fromNullable;

/**
 * json数据格式参数值的获取
 */
@SuppressWarnings("all")
public class JsonExtractor extends HttpRequestExtractor<String[]> {

    private final String param;

    public JsonExtractor(final String param) {
        this.param = param;
    }

    @Override
    protected Optional<String[]> doExtract(HttpRequest request) {
        try {
            String s = request.getContent().toString();
            String value = JSONObject.fromObject(s).getString(param);
            return fromNullable(new String[]{value});
        } catch (Exception e) {
            return fromNullable(new String[]{""});
        }
    }

    /**
     * 获取参数的value
     *
     * @param param
     * @return
     */
    public static RequestExtractor<String[]> queryJson(final String param) {
        return new JsonExtractor(checkNotNullOrEmpty(param, "参数不能为空！"));
    }
}
```

groovy使用方法如下：

```
/**
 * get请求参数是否相等
 * @param key
 * @param value
 * @return
 */
    static RequestMatcher eqArgs(String key, String value) {
        eq query(key), value
    }

/**
 * post请求json数据参数是否相等
 * @param key
 * @param value
 * @return
 */
    static RequestMatcher eqParams(String key, String value) {
        eq queryJson(key), value
    }

```
>  groovy是一种基于JVM的动态语言，我觉得最大的优势有两点，第一：于java兼容性非常好，大部分时候吧groovy的文件后缀改成java直接可以用，反之亦然。java的绝大部分库，groovy都是可以直接拿来就用的。这还带来了另外一个优点，学习成本低，非常低，直接上手没问题，可以慢慢学习groovy不同于Java的语法；第二：编译器支持变得更好，现在用的intellij的ide，总体来说已经比较好的支持groovy语言了，写起代码来也是比较顺滑了，各种基于groovy的框架工具也比较溜，特别是Gradle构建工具，比Maven爽很多。----此段文字为了撑字数强加的，与内容无关。


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)


<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---


<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>