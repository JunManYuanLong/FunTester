# 用groovy仿stopwatch写一个时间计数器timewatch辅助性能测试
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
本人在做性能测试的时候，经常需要去记录一些方法的执行时间，这期间用到了stopwatch的类，但是有些功能自定义起来不太好，比如自定义标记的名称一类，而且split方法也有点不顺手，所以产生了自己写一个简单的时间计数器。

语言使用groovy写的，java框架可以使用。

v1.0已经完成，目前只有一些简单的功能，下一步打算弄一下多线程使用兼容问题。

```

package com.fun.utils

import com.fun.frame.SourceCode
import org.slf4j.LoggerFactory

import java.text.DecimalFormat

/**
 * 时间观察者类，用于简单记录执行时间
 */
class TimeWatch extends SourceCode {
    static def logger = LoggerFactory.getLogger(TimeWatch.class)
/**
 * 默认的名称
 */
    def name = "default"
/**
 * 纳秒
 */
    def startTime
    /**
     * 标记集合
     */
    def marks = new HashMap<String, Mark>()
    /**
     * 毫秒
     */
    def startTimeMillis

/**
 * 无参创建方法，默认名称
 * @return
 */
    public static TimeWatch create() {
        final TimeWatch timeWatch = new TimeWatch()
        timeWatch.start()
        return timeWatch
    }

/**
 * 创建方法
 * @param name
 * @return
 */
    public static TimeWatch create(def name) {
        final TimeWatch timeWatch = new TimeWatch()
        timeWatch.start()
        return timeWatch
    }


    private TimeWatch() {
    }

/**
 * 开始记录
 * @return
 */
    def start() {
       reset()
    }

/**
 * 重置
 */
    def reset() {
        startTime = getNanoMark()
        startTimeMillis = Time.getTimeStamp()
    }
/**
 * 标记
 * @param name
 * @return
 */
    def mark(def name) {
        marks.put(name, new Mark(name))
    }

/**
 * 标记
 * @return
 */
    def mark() {
        marks.put(name, new Mark(name))
    }

/**
 * 获取标记时间
 * @return
 */
    def getMarkTime() {
        if (marks.containsKey(name)) {
            def diff = Time.getTimeStamp() - marks.get(name).getStartTimeMillis()
            logger.info(LINE + "观察者：{}的标记：{}记录时间：{} ms", name, name, diff)
        } else {
            logger.warn("没有默认标记！")
        }
    }

/**
 * 获取标记时间
 * @return
 */
    def getMarkNanoTime() {
        if (marks.containsKey(name)) {
            def diff = getNanoMark() - marks.get(name).getStartTime()
            logger.info(LINE + "观察者：{}的标记：{}记录时间：{} ns", name, name, diff)
        } else {
            logger.warn("没有默认标记！")
        }
    }


/**
 * 获取某个标记的记录时间
 * @param name
 * @return
 */
    def getMarkTime(String name) {
        if (marks.containsKey(name)) {
            def diff = Time.getTimeStamp() - marks.get(name).getStartTimeMillis()
            logger.info(LINE + "观察者：{}的标记：{}记录时间：{} ms", name, name, diff)
        } else {
            logger.warn("没有{}标记！", name)
        }
    }

/**
 * 获取某个标记的记录时间
 * @param name
 * @return
 */
    def getMarkNanoTime(String name) {
        if (marks.containsKey(name)) {
            def diff = getNanoMark() - marks.get(name).getStartTime()
            logger.info(LINE + "观察者：{}的标记：{}记录时间：{} ns", name, name, diff)
        } else {
            logger.warn("没有{}标记！", name)
        }
    }


/**
 * 获取记录时间
 * @return
 */
    def getTime() {
        def diff = Time.getTimeStamp() - startTimeMillis
        logger.info(LINE + "观察者：{}，记录时间：{} ms", getName(), diff)
        return diff
    }

/**
 * 获取记录时间纳秒
 * @return
 */
    def getNanoTime() {
        long diff = getNanoMark() - startTime
        DecimalFormat format = new DecimalFormat("#,###")
        logger.info(LINE + "观察者：{}，记录时间：{} ns", getName(), format.format(diff))
        return diff
    }

    @Override
    public String toString() {
        return "时间观察者：" + this.name
    }

    @Override
    public TimeWatch clone() {
        def watch = new TimeWatch()
        watch.name = getName() + "_c"
        watch.startTime = getStartTime()
        watch.startTimeMillis = getStartTimeMillis()
        return watch
    }
/**
 * 标记类
 */
    class Mark {

        public Mark(def name) {
            this.name = name
            reset()
        }

        def name

        def startTime

        def startTimeMillis
        def lastTime
        def l

        def reset() {
            this.startTime = getNanoMark()
            this.startTimeMillis = Time.getTimeStamp()
        }
    }
}
```

> groovy是一种基于JVM的动态语言，我觉得最大的优势有两点，第一：于java兼容性非常好，大部分时候吧groovy的文件后缀改成java直接可以用，反之亦然。java的绝大部分库，groovy都是可以直接拿来就用的。这还带来了另外一个优点，学习成本低，非常低，直接上手没问题，可以慢慢学习groovy不同于Java的语法；第二：编译器支持变得更好，现在用的intellij的ide，总体来说已经比较好的支持groovy语言了，写起代码来也是比较顺滑了，各种基于groovy的框架工具也比较溜，特别是Gradle构建工具，比Maven爽很多。----此段文字为了撑字数强加的，与内容无关。


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)


<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---


<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>