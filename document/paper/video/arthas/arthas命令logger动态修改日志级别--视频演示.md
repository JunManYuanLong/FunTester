# arthas命令logger动态修改日志级别--视频演示

 > 可以动态修改`logger`的级别。

arthas命令`logger`主要是用来处理日志记录相关的功能，包括查看`logger`和`appenders`信息，包括`classloader`，日志`level`，`codeSource`，日志文件名等等。其中最有用的就是修改`logger`的级别，这个功能可以在不重启`JVM`的情况下，打开`debug`调试日常，在完成排查之后，再修改为正常的`info`级别，对于线上排查问题来讲，非常有帮助。当然有用的前提是，日志规范执行到位，如果开发压根没有输出`debug`日志，那就无能为力了，不过这难不倒`arthas`，还会有更牛的命令来实现这个功能，以后我会继续分享演示视频。

我项目使用的是`log4j2`，在`xml`配置上跟官方Demo有些区别，所以教程基于`log4j2`，如果使用`log4j`的童鞋也是可以使用的。

## arthas命令logger命令

- [点击观看视频](https://mp.weixin.qq.com/s/w724P9B12eTC9rMbavwsMA)

## log4j2配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--日志级别以及优先级排序: OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL -->
<!--Configuration后面的status，这个用于设置log4j2自身内部的信息输出，可以不设置，当设置成trace时，你会看到log4j2内部各种详细输出-->
<!--monitorInterval：Log4j能够自动检测修改配置 文件和重新配置本身，设置间隔秒数-->
<configuration status="WARN" monitorInterval="30">
    <!--先定义所有的appender-->
    <appenders>
        <!--这个输出控制台的配置-->
        <console name="Console" target="SYSTEM_OUT">
            <!--输出日志的格式-->
            <PatternLayout pattern="%-4p-> %m%n"/>
            <!--            <PatternLayout pattern="[%-4p] %d - %t %L:%M %m %n"/>-->
            <!--            <PatternLayout pattern="%d %-4p (%F:%L) - %m%n"/>-->
        </console>
        <!--文件会打印出所有信息，这个log每次运行程序会自动清空，由append属性决定，这个也挺有用的，适合临时测试用-->
        <!--        <File name="log" fileName="log/fun.log" append="false">-->
        <!--            <PatternLayout pattern="%d{HH:mm:ss.SSS} %-5level %class{36} %L %M - %msg%xEx%n"/>-->
        <!--        </File>-->
        <!-- 这个会打印出所有的info及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档-->
        <RollingFile name="RollingFileInfo"
                     fileName="log/info.log"
                     filePattern="log/$${date:yyyy-MM}/info-%d{yyyy-MM-dd}-%i.log">
            <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
            <ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="%d{HH:mm:ss} %-4level %l - %m%n"/>
            <Policies>
                <TimeBasedTriggeringPolicy/>
                <SizeBasedTriggeringPolicy size="2 MB"/>
            </Policies>
            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件，这里设置了20 -->
            <DefaultRolloverStrategy max="5"/>
        </RollingFile>


        <RollingFile name="RollingFileWarn" fileName="log/warn.log"
                     filePattern="log/$${date:yyyy-MM}/warn-%d{yyyy-MM-dd}-%i.log">
            <ThresholdFilter level="warn" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="%d{HH:mm:ss} %-4level %l - %m%n"/>
            <Policies>
                <TimeBasedTriggeringPolicy/>
                <SizeBasedTriggeringPolicy size="2 MB"/>
            </Policies>
            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件，这里设置了20 -->
            <DefaultRolloverStrategy max="5"/>
        </RollingFile>
    </appenders>
    <!--然后定义logger，只有定义了logger并引入的appender，appender才会生效-->
    <loggers>
        <root level="info">
            <appender-ref ref="Console"/>
            <appender-ref ref="RollingFileInfo"/>
            <appender-ref ref="RollingFileWarn"/>
        </root>
    </loggers>
<!--    异步日志记录的配置-->
<!--    <Appenders>-->
<!--        &lt;!&ndash; Async Loggers will auto-flush in batches, so switch off immediateFlush. &ndash;&gt;-->
<!--        <RandomAccessFile name="RandomAccessFile" fileName="asyncWithLocation.log"-->
<!--                          immediateFlush="false" append="false">-->
<!--            <PatternLayout>-->
<!--                <Pattern>%d %p %class{1.} [%t] %location %m %ex%n</Pattern>-->
<!--            </PatternLayout>-->
<!--        </RandomAccessFile>-->
<!--    </Appenders>-->
<!--    <Loggers>-->
<!--        &lt;!&ndash; pattern layout actually uses location, so we need to include it &ndash;&gt;-->
<!--        <AsyncLogger name="com.foo.Bar" level="trace" includeLocation="true">-->
<!--            <AppenderRef ref="RandomAccessFile"/>-->
<!--        </AsyncLogger>-->
<!--        <Root level="info" includeLocation="true">-->
<!--            <AppenderRef ref="RandomAccessFile"/>-->
<!--        </Root>-->
<!--    </Loggers>-->
</configuration>
```

## Demo代码


```Java
package com.fun

import com.fun.frame.httpclient.FanLibrary
import org.slf4j.Logger

class TSSS extends FanLibrary {

    public static Logger logger = getLogger(TSSS.class)

    public static void main(String[] args) {
        while (true) {
            logger.debug("我是debug信息!")
            logger.info("我是info信息!")
            logger.warn("我是warn信息!")
            logger.error("我是error信息!")
            sleep(3000)
        }

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