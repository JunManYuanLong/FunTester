# 如何使用“dd MM”解析日期

[原文地址](https://mkyong.com/java8/java-8-how-to-parse-date-with-dd-mmm-02-jan-without-year/)

## 错误示范

本示例说明如何在不指定年份的情况下解析日期，如下：

```Java

package com.fun

import com.fun.frame.SourceCode

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TSSS extends SourceCode {

    public static void main(String[] args) {
        public static void main(String[] args) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM", Locale.CHINA);

            String date = "02 12";

            LocalDate localDate = LocalDate.parse(date, formatter);

            System.out.println(localDate);

            System.out.println(formatter.format(localDate));

        }

    }
}
```

## 输出


```Java
INFO-> 当前用户：fv，IP：192.168.0.100，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.3
Exception in thread "main" java.time.format.DateTimeParseException: Text '02 Jan' could not be parsed at index 3
	at java.time.format.DateTimeFormatter.parseResolved0(DateTimeFormatter.java:1947)
	at java.time.format.DateTimeFormatter.parse(DateTimeFormatter.java:1849)
	at java.time.LocalDate.parse(LocalDate.java:400)
	at java_time_LocalDate$parse.call(Unknown Source)
	at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCall(CallSiteArray.java:47)
	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:115)
	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.call(AbstractCallSite.java:135)
	at com.fun.TSSS.main(TSSS.groovy:16)

Process finished with exit code 1
```

## 正解

模式`dd MMM`还不够；我们需要`DateTimeFormatterBuilder`为日期解析提供默认年份。


```Java

package com.fun

import com.fun.frame.SourceCode

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

class TSSS extends SourceCode {

    public static void main(String[] args) {

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("dd MM")
                .parseDefaulting(ChronoField.YEAR, 2020)
                .toFormatter(Locale.CHINA);

        String date = "02 11";

        LocalDate localDate = LocalDate.parse(date, formatter);

        System.out.println(localDate);

        System.out.println(formatter.format(localDate));

    }
}
```

## 输出

```Java
INFO-> 当前用户：fv，IP：192.168.0.100，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.3
2020-11-02
02 11

Process finished with exit code 0
```

---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [Java并发BUG基础篇](https://mp.weixin.qq.com/s/NR4vYx81HtgAEqH2Q93k2Q)
- [Java并发BUG提升篇](https://mp.weixin.qq.com/s/GCRRe8hJpe1QJtxq9VBEhg)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)


## 非技术文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)