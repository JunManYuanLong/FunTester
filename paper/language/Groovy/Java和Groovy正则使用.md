# Java和Groovy正则使用

相信很多人都对正则有很深的交情，毕竟这玩意功能太强了，几乎无处不在。我最长用的正则还是爬虫。爬虫分两类，一种是接口返回`json`数据的，一种是返回`HTML`数据的。

对于第一种返回`json`数据的可以直接用`jsonobject`解析。而第二种往往用`HTML`解析类做起来比较麻烦，特别是提取表单信息的时候，所以我直接当做`string`信息，通过正则表达式提取想要的信息。

下面分享几个案例：

- [电子书网站爬虫实践](https://mp.weixin.qq.com/s/KGW0dIS5NTLgxyhSjxDiOw)
- [groovy爬虫实例——历史上的今天](https://mp.weixin.qq.com/s/5LDUvpU6t_GZ09uhZr224A)
- [爬取720万条城市历史天气数据](https://mp.weixin.qq.com/s/vOyKpeGlJSJp9bQ8NIMe2A)
- [记一次失败的爬虫](https://mp.weixin.qq.com/s/SMylrZLXDGw5f1xKI9ObnA)

## Java正则

里面用到了一个`Java`的正则工具类，算是写了`Java`的正则`Demo`，代码如下：

```Java
package com.fun.utils;

import com.fun.frame.SourceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则验证的封装
 */
public class Regex extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(Regex.class);

    /**
     * 正则校验文本是否匹配
     *
     * @param text  需要匹配的文本
     * @param regex 正则表达式
     * @return
     */
    public static boolean isRegex(String text, String regex) {
        return Pattern.compile(regex).matcher(text).find();
    }

    /**
     * 正则校验文本是否完全匹配，不包含其他杂项，相当于加上了^和$
     *
     * @param text  需要匹配的文本
     * @param regex 正则表达式
     * @return
     */
    public static boolean isMatch(String text, String regex) {
        return Pattern.compile(regex).matcher(text).matches();
    }

    /**
     * 返回所有匹配项
     *
     * @param text  需要匹配的文本
     * @param regex 正则表达式
     * @return
     */
    public static List<String> regexAll(String text, String regex) {
        List<String> result = new ArrayList<>();
        Matcher matcher = Pattern.compile(regex).matcher(text);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    /**
     * 获取匹配项，不包含文字信息，会删除regex的内容
     * <p>不保证完全正确</p>
     *
     * @param text
     * @param regex
     * @return
     */
    public static String getRegex(String text, String regex) {
        String result = EMPTY;
        try {
            result = regexAll(text, regex).get(0);
            String[] split = regex.split("(\\.|\\+|\\*|\\?)");
            for (int i = 0; i < split.length; i++) {
                String s1 = split[i];
                if (!s1.isEmpty())
                    result = result.replaceAll(s1, EMPTY);
            }
        } catch (Exception e) {
            logger.warn("获取匹配对象失败！", e);
        } finally {
            return result;
        }
    }

}
```

## Groovy正则

首先来讲，`Groovy`完全可以使用Java的正则语法，上面的正则工具类完全适用于`Groovy`脚本，我的爬虫Demo里面基本上也都是在`Groovy`脚本里面直接使用的这个工具类。

下面分享一下`Groovy`语言自己的正则表达式。其中最重要的三个符号`=~`相当于`Java`里面的`Pattern.compile(regex).matcher(text)`，然后`==~`相当于`Pattern.compile(regex).matcher(text).match()`，这里不是`find()`，两者区别请自行搜索，还有一个写法`def stra = /.*test\w+/`，专指正则表达式，使用收尾都加上`/`而不是`"`。

下面是我的Demo：


```Groovy
public static void main(String[] args) {
        def str = "fantester"
        def matcher = str =~ "\\wt"
        println matcher.find()
        println matcher[0]
        println matcher.size()
        matcher.each {println it}
        def b = str ==~ ".*er"
        output b

        def stra = /.*test\w+/

        println str ==~ stra

        ("fanfanfanfan" =~ "\\wf").each {println it}

        "fanfanfanfan".eachMatch(/\wa/) {println it}

}
```

控制台输出如下：


```Java
INFO-> 当前用户：fv，IP：192.168.0.100，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.3
true
nt
2
nt
st
INFO-> true
true
nf
nf
nf
fa
fa
fa
fa

Process finished with exit code 0

```

`Groovy`语法还是有很强的可玩性的，虽然基本兼容`Java`语法，但是深入了解`Groovy`之后，是可以写出不逊于`Python`的简洁语法。

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

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [左移测试](https://mp.weixin.qq.com/s/8zXkWV4ils17hUqlXIpXSw)
- [选择手动测试还是自动化测试？](https://mp.weixin.qq.com/s/4haRrfSIp5Plgm_GN98lRA)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDkiawpL3o8umv1EgHOc2OE1iaib1vR7Q6DH1FSpP4HVuibsibicftEqUqfXZpE2FyN7nIPvHwhWQdG6n0g/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)