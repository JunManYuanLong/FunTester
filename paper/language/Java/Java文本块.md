# Java文本块

[Java文本块](https://www.javacodegeeks.com/2020/06/java-text-blocks-2.html)

文本块是`JDK`增强建议 k**（JEP 355）**，可以在**JDK13**和**JDK14**中作为预览语言功能使用。它计划在**JDK15**中成为永久性功能。文本块是一个字符串文字，它跨越多行，并且不需要大多数转义字符。

# 动机

在标准Java字符串中嵌入`XML`、`JSON`或`SQL`之类的格式会变得很烦人。例如，由于需要转义，因此只有两个键的简单`JSON`代码片段在`Java`中几乎无法流畅阅读：

```Java
String json =
        "{\n" +
            "\"name\": \"FunTester\",\n" +
            "\"age\": 30\n" +
        "}";
```

# 文本块来拯救

使用新的文本块功能，我们可以将代码重写为：

```Java
String text = """
        {
            "name": "FunTester",
            "age": "30"
        }
        """;
```

使用三引号**"""**打开和关闭文本块。文本从下一行开始。打开文本块后，该行的其余部分需要保持空白。

如果我们将此字符串打印到控制台，我们将看到：

```Java
{
    "name": "FunTester",
    "age": "30"
}
```

可能你已经注意到，左侧的缩进已被去除。这是因为文本块的处理分为三个步骤：

* 行终止符被标准化为`LF`字符。这样可以避免不同平台（例如`Windows`和`Unix`）之间的兼容性问题。
* 附带的前置空格和所有尾随空格均被删除。偶然的前导空格是通过找到所有行的前导空格的公共数量来确定的。
* 转义序列被解释。文本块可以包含与标准字符串相同的转义序列（例如`\t`或`\n`）。请注意，已经添加了两个新的转义序列：`\s`用于显式空间，`\<eol>`作为连续指示符（稍后在`\<eol>`上有更多介绍）。

# 前置空格

如果我们明确需要前置空格，则可以使用`indent()`方法：


```Java
String text = """
        {
            "name": "FunTester",
            "age": "30"
        }
        """.indent(4);
```

这会将4个额外的前导空格添加到我们的`JSON`代码段中。控制台输出看起来像这样：


```Java
    {
        "name": "FunTester",
        "age": "30"
    }
```

或者，我们可以从封闭的三引号中删除4个前导空格以产生相同的结果：


```Java
String text = """
        {
            "name": "FunTester",
            "age": "30"
        }
    """; // 将这4个空格向左移动会产生4个额外的前导空格
```

# 新的转义序列

使用新的转义序列，我们可以将单行的内容拆分为多行，而无需创建实际的行终止符。


```Java
String text = """
        1
        2 \
        3 \
        4
        5
        """;
```

控制台输出：

```Java
1
2 3 4
5
```

# 转义三引号

如果我们需要在文本块中写入三引号，则只需要转义第一引号即可：


```Java
String text = """
        测试文本 \"""
        """;
```

控制台输出：

`测试文本 """`

--- 
* 公众号**FunTester**首发，更多原创文章：[FunTester410+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [Selenium并行测试最佳实践](https://mp.weixin.qq.com/s/-RsQZaT5pH8DHPvm0L8Hjw)
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)
- [有关UI测试计划](https://mp.weixin.qq.com/s/D0fMXwJF754a7Mr5ARY5tQ)
- [Selenium自动化测试技巧](https://mp.weixin.qq.com/s/EzrpFaBSVITO2Y2UvYvw0w)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)