# Java删除空字符：Java8 & Java11

[原文地址](https://www.javacodegeeks.com/2020/08/different-ways-to-remove-spaces-from-string-in-java.html)

操作字符串是编程时经常遇到的，常用的比如在字符串中处理空格。到目前为止，`Java`提供了很多从字符串中删除空格的不同方法，即`trim`，`replaceAll`。但是，`Java 11`通过诸如`strip`，`stripLeading`和`stripTrailing`之类的方法对这些方法进行了一些功能性的拓展。

在大多数情况下，我们只使用`trim()`方法删除空格。有时候不禁停下来想一想是否有更好的方法来满足我们的需求？当然，`trim()`在大多数情况下都能很好地工作，但是`java`中有许多不同的方法。每种都有自己的优点和缺点。

在本文中，将详细介绍在`Java`中从字符串中删除空格的不同方法

* `trim()`：从字符串中删除前缀和后缀空格
* `strip()`：删除字符串开头和结尾的空格。`strip()`方法支持`Unicode`字符集
* `trim vs strip`：`trim`和`strip`方法之间的差异
* `stripLeading()`：仅从字符串开头删除空格
* `stripTrailing()`：仅从字符串末尾删除空格
* `replace()`：用新字符替换所有目标字符
* `replaceAll()`：将所有正则匹配的字符替换为新字符
* `replaceFirst()`：使用新替换字符串替换第一次匹配成功的子字符串


* 需要注意的最重要一点是，在`Java`中，字符串对象是不可变的。这意味着我们无法修改字符串，因此所有方法都将通过所有转换返回新字符串。

# trim()方法

`trim()`是`Java`开发人员最常用的删除前导和尾随空格的方法。对于`trim()`方法，空格字符是指*ASCII值小于或等于32('U + 0020')*的任何字符。

```Java
public class FunTester {
 
    public static void main(String[] args) {
        String string = "    one    two    three    ";
        System.out.println("原始字符串: \"" + string +"\"");
        System.out.println("处理结果: \"" + string.trim() +"\"");
   }
}
```

控制台输出：


```shell
原始字符串: "    one    two    three    "
处理结果: "one    two    three"
```


# strip()方法

在`Java 11`发行版中，添加了新的`strip()`方法以从`String`中删除前缀和后缀空格。

添加此方法的原因是，根据`Unicode`标准，存在各种空格字符，其*ASCII值大于32('U + 0020')*。例如：*8193(U + 2001)*。为了识别这些空格字符，`Java 1.5`从`Character`类中添加了新方法`isWhitespace(int)`。此方法使用`unicode`识别空格字符。`strip()`方法使用此`Character.isWhitespace(int)`方法覆盖广泛的空白字符并将其删除。


```Java
public class StringStripTest {
    public static void main(String[] args) {
        String string = "    one    two    three    ";
        System.out.println("原始字符串: \"" + string+"\"");
        System.out.println("处理结果: \"" + string.strip()+"\"");
    }
}
```

控制台输出：


```shell
原始字符串: "    String    with    space    "
处理结果: "one    two    three"
```

# Java中trim和strip方法之间的区别

|trim()		| strip()|
|----|----|
|从Java 1 |	从Java 11|
|使用ASCII值 |	使用Unicode值|
|删除前缀和后缀字符(空格)	| 删除前缀和后缀字符(空格)|
|删除ASCII值小于或等于'U+0020'或'32'的字符|根据Unicode删除所有空格字符|

* 让我们看一下使用大于`32('U+0020')`的`unicode`的空白字符。


```Java
public class StringTrimVsStripTest {
    public static void main(String[] args) {
        String string = '\u2001'+"one    two    three"+ '\u2001';
        System.out.println("原始字符串: \"" + string+"\"");
        System.out.println("处理结果: \"" + string.trim()+"\"");
        System.out.println("处理结果: \"" + string.strip()+"\"");
   }
}
```

控制台输出：

```shell
原始字符串: "  one    two    three  "
处理结果: " one    two    three "
处理结果: "one    two    three"
```

在上面的示例中，我们可以看到`trim()`方法无法删除由**'\u2001'Unicode**字符添加的空格字符。

* 注意：如果在Windows计算机上运行，​​则由于限制了`unicode`设置，可能看不到类似的输出。

# stripLeading()方法

`Java 11`中添加了`stripLeading()`方法，可从`String`中删除所有前缀空格。与`strip()`方法类似，`stripLeading()`也使用`Character.isWhitespace(int)`识别空白字符。


```Java
public class StringStripLeadingTest {
    public static void main(String[] args) {
        String string = "    one    two    three    ";
        System.out.println("原始字符串: \"" + string+"\"");
        System.out.println("处理结果 : \"" + string.stripLeading()+"\"");
    }
}
```

控制台输出：


```shell
原始字符串: "    one    two    three    "
处理结果 : "one    two    three    "
```

# stripTrailing()方法


`Java 11`中增加了`stripTrailing()`方法，可从`String`中删除所有后缀空格。与`stripLeading()`方法类似，`stripTrailing()`也使用`Character.isWhitespace(int)`识别空白。



```shell
public class StringStripTrailingTest {
 
    public static void main(String[] args) {
      String string = "    one    two    three    ";
      System.out.println("原始字符串: \"" + string+"\"");
        System.out.println("处理结果 : \"" + string.stripTrailing()+"\"");
    }
}
```

控制台输出：


```shell
原始字符串:"    one    two    three    "
处理结果 :"    one    two    three"
```

# replace(CharSequence target, CharSequence replacement):


从`Java 1.5`中添加，此方法用于将每个目标子字符串替换为指定的替换字符串。此方法替换所有匹配的目标字符。

* 注意： `java`中的`String`类中提供了另一种方法`replace(char oldChar，char newChar)`。区别在于该方法参数是字符，而不是字符串。

```Java
public class StringReplaceTest {
  
    public static void main(String[] args) {
        String string = "    one    two    three    ";
        System.out.println("原始字符串 : \"" + string + "\"");
        System.out.println("处理结果: \"" + string.replace(" ", "") + "\"");
    }
}

```

控制台输出：


```shell
原始字符串  : "    one    two    three    "
处理结果 : "onetwothree"
```

# replaceAll(String regex, String replacement)

在`Java 1.4`中添加，这是最强大的字符串处理方法之一。使用`replaceAll()`方法，我们可以使用给定的替换字符串替换每个匹配的正则表达式子字符串。例如，删除所有空格，删除前导空格，删除尾随空格等等。我们只需要创建带有正确替换参数的正确正则表达式即可。参考：[Java和Groovy正则使用](https://mp.weixin.qq.com/s/DT3BKE3ZcCKf6TLzGc5wbg)。

* 在Java中添加'/'，我们必须使用转义字符，因此对于`\s+`，必须使用`\\s+`


```Java
public class StringReplaceAllTest {
    public static void main(String[] args) {
        String string = "    one    two    three    ";
        System.out.println("原始字符串 : \"" + string+"\"");
        System.out.println("处理结果 : \"" + string.replaceAll(" ", "") + "\"");
        System.out.println("处理结果 : \"" + string.replaceAll("\\s+", "") + "\"");
        System.out.println("处理结果  : \"" + string.replaceAll("^\\s+", "") + "\""); 
        System.out.println("处理结果 : \"" + string.replaceAll("\\s+$", "") + "\"");
    }
}
```

控制台输出：

```shell
原始字符串 : "    one    with    three    "
处理结果 : "onetwothree"
处理结果 : "onetwothree"
处理结果   : "one    two    three    "
处理结果  : "    one    two    three"
```

# replaceFirst(String regex, String replacement)


在`Java 1.4`中添加了`replaceFirst()`方法，只用替换字符串替换给定正则表达式的第一个匹配项，用于替换一个第一次出现的位置。例如，如果我们只需要删除前缀空格，则可以使用`\\s+`或`^\\s+`。还可以使用此方法通过使用`\\s+$`正则表达式删除后缀空格。


```Java
public class StringReplaceFistTest {
      public static void main(String[] args) {
      String string = "    one    two    three    ";
      System.out.println("原始字符串   : \"" + string+"\"");
        System.out.println("处理结果  : \"" + string.replaceFirst("three", "four") + "\"");
        System.out.println("处理结果  : \"" + string.replaceFirst("\\s+", "") + "\"");
        System.out.println("处理结果 : \"" + string.replaceFirst("\\s+$", "") + "\"");    }
}
```

控制台输出：


```shell
原始字符串   : "    one    two    three    "
处理结果  : "    one    two    four    "
处理结果  : "one    two    three    "
处理结果 : "    one    two    three"
```

----
**公众号[FunTester](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)首发，原创分享爱好者，腾讯云和掘金社区首页推荐，知乎七级原创作者，欢迎关注、交流，禁止第三方擅自转载。**

FunTester热文精选
=

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)
- [测试开发工程师工作技巧](https://mp.weixin.qq.com/s/TvrUCisja5Zbq-NIwy_2fQ)
- [Fiddler Everywhere工具答疑](https://mp.weixin.qq.com/s/2peWMJ-rgDlVjs3STNeS1Q)
- [Selenium4 IDE，它终于来了](https://mp.weixin.qq.com/s/XNotlZvFpmBmBQy1pYifOw)
- [质量管理计划的基本要素](https://mp.weixin.qq.com/s/v8lOioYn01S1F0ex4mmljA)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)