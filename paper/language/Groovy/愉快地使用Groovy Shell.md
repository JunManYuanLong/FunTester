# 愉快地使用Groovy Shell

这是一篇有关Groovy Shell的帖子，以及它如何在日常工作中为您提供帮助（只要您是软件开发人员）。无论您使用哪种编程语言或技术，都可以从Groovy Shell中受益。唯一真正的要求是您能够编写（和阅读）小段Groovy代码。

## 入门
我认为Groovy shell的目的最好由官方文档来描述：

> Groovy Shell，又名。groovysh是一个命令行应用程序，可轻松访问以评估Groovy表达式，定义类并运行简单的实验。

Groovy Shell包含在Groovy编程语言的发行版中，可以在<groovy home> / bin中找到。要启动Groovy Shell，只需从命令行运行groovysh即可：


```
GROOVY_HOME\bin>groovysh
Groovy Shell (2.2.2, JVM: 1.7.0)
Type 'help' or '\h' for help.
--------------------------------------------------------------------
groovy:000>
```

现在，您可以在外壳中运行Groovy命令：


```
groovy:000> println("hu?")
hu?
===> null
groovy:000>
```
它支持变量和多行语句：


```
groovy:000> foo = 42
===> 42
groovy:000> baz = {
groovy:001> return 42 * 2
groovy:002> }
===> groovysh_evaluate$_run_closure1@3c661f99
groovy:000> baz(foo)
===> 84
groovy:000>
```

* （请注意，您必须跳过def关键字，以便以后使用变量和闭包）


## Windows用户须知
我可以清楚地推荐Console（2），它是笨拙的cmd窗口的小包装。它提供了Tab支持，更好的文本选择和其他有用的功能。

不幸的是，在某些地区（包括德语）中，Groovy 2.2.0 Shell 在Windows 7/8上的箭头键存在问题。但是，您可以使用CTRL-P和CTRL-N代替UP和DOWN。作为替代方案，您可以使用旧的Groovy版本的外壳（来自Groovy 2.1.9的groovysh可以正常工作）。

## 那么，我们可以使用它吗？
我们可以做的最明显的事情是评估Groovy代码。如果您正在使用Groovy的应用程序上工作，这将特别有用。

也许您知道可以使用<<运算符将元素添加到列表中，但是不确定该运算符是否适用于地图？在这种情况下，您可以开始谷歌搜索或在文档中查找。或者，您可以将其键入Groovy Shell并查看其是否有效：

```
groovy:000> [a:1] << [b:2]
===> {a=1, b=2}
```
有用！

您不确定是否可以遍历枚举值？


```
groovy:000> enum Day { Mo, Tu, We }
===> true
groovy:000> Day.each { println it }
Mo
Tu
We
===> class Day
```
## 这是一个计算器！
Groovy Shell可用于简单的数学计算：


```
groovy:000> 40 + 2
===> 42
groovy:000>
groovy:000> 123456789123456789 * 123456789123456789123456789
===> 15241578780673678530864199515622620750190521
groovy:000>
groovy:000> 2 ** 1024
===> 179769313486231590772930519078902473361797697894230657273430081157732675805500963132708477322407536021120113879871393357658789768814416622492847430639474124377767893424865485276302219601246094119453082952085005768838150682342462881473913110540827237163350510684586298239947245938479716304835356329624224137216
groovy:000>
```

如您所见，Groovy可以很好地处理可能导致其他编程语言溢出的数字。Groovy使用BigInteger和BigDecimal进行这些计算。顺便说一句，您可以很快自己验证一下：


```
groovy:000> (2 ** 1024).getClass()
===> class java.math.BigInteger
```

## 更多可能

也许您需要某个网页的内容？使用Groovy可以轻松实现：


```
groovy:000> "http://groovy.codehaus.org".toURL().text<font></font>
===> <!DOCTYPE html><font></font>
<html><font></font>
<head><font></font>
    <meta charset="utf-8"/><font></font>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/><font></font>
    <meta name="description" content="Groovy Wiki"/><font></font>
    ...
```
也许出于某些原因，您只想要<meta>标签？


```
groovy:000> "http://groovy.codehaus.org".toURL().eachLine { if (it.contains('<meta')) println it }
    <meta charset="utf-8"/>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="description" content="Groovy Wiki"/>
    <meta name="keywords"
    <meta name="author" content="Codehaus Groovy Community"/>
===> null
```
我确定您处于需要某些文本的url编码版本的情况：


```
groovy:000> URLEncoder.encode("foo=bar")
===> foo%3Dbar
```
当然，您无需记住确切的类和方法名称。只需输入前几个字符，然后按Tab键即可获得可能的选项：


```
groovy:000> URL
URL                       URLClassLoader            URLConnection             URLDecoder                URLEncoder
URLStreamHandler          URLStreamHandlerFactory
```
它也适用于方法：


```
groovy:000> URLEncoder.e
each(            eachWithIndex(   encode(          every(           every()
```

## 结论
在切换到Groovy Shell之前，我出于几乎相同的原因使用了Python Shell（即使我根本没有使用Python）。在过去的一年中，我使用了很多Groovy，很快我发现Groovy Web Console是用于测试和原型制作的非常有价值的工具。对我来说，Groovy Shell替换了这两个工具。显然，这是我不想错过的开发工具。

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [JUnit中用于Selenium测试的中实践](https://mp.weixin.qq.com/s/KG4sltQMCfH2MGXkRdtnwA)

## 非技术文章精选

- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [自动化和手动测试，保持平衡！](https://mp.weixin.qq.com/s/mMr_4C98W_FOkks2i2TiCg)
- [自动化测试生命周期](https://mp.weixin.qq.com/s/SH-vb2RagYQ3sfCY8QM5ew)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
![](https://mmbiz.qpic.cn/mmbiz_png/BuV4gXrNvFrQnPz6hPuyeNCH9BXB4Ufc0lbWyTGjcWrpSwFJOWqFtL0jIYWeqa093ibQcZCu7UMpSVZsFwKbicHQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)