# JSON基础

本文整理自《JSON必知必会》一书，主要是章节的简介，把前三个章的内容简单叙述了，算是需要JSON的基础知识。以后有机会看情况再写一些Java的jsonobject的实践文章。

正文开始：

在深入讨论 JSON 之前，先让我们对它有一个感性的认识。宏观上看， JSON 是一种轻量的数据格式，在各种地方传递数据。直观上看， JSON里的数据是被保存在花括号({})中的，而如果从用途上进一步分析，最终得出结论：JSON 是一种数据交换格式。
 
 
## 1.1 JSON是一种数据交换格式

数据交换格式是一种在不同平台间传递数据的文本格式。除`JSON`外，你也可能听说过`XML`这种数据交换格式。像`XML`和 `JSON`这样的数据交换格式非常重要，我们需要它们来实现不同系统间的数据交换。

## 1.2 JSON独立于编程语言

JSON 的全称是 JavaScript Object Notation(JavaScript 对象表示法)。这个 名字可能会让人误以为要想理解和使用 JSON，得先学习 JavaScript。诚然， 在学习 JSON 前学一点 JavaScript 肯定会有帮助，毕竟 JSON 源于 JavaScript 的一个子集。但如果你以后用不到 JavaScript，那也没有必要去学习它，因 为数据交换格式是独立于语言的。你仍可以在你自己的系统中使用你自己 的语言。

## 2.1 JSON基于JavaScript对象

JSON 是基于 JavaScript 对象字面量的。注意是“基于”。在 JavaScript(以及大多数包含对象概念的编程语言)中，对象里面常常包含函数。数据交换格式的核心是数据，所以JSON 中并不会涉及JavaScript对象字面量中的函数。JSON 所基于的 JavaScript 对象字面量单纯指对象字面量及其属性的语法表示。

## 2.2 名称—值对

在计算机界，名称—值对的概念非常流行。它们也有别的名字，像键—值对、属性—值对或字段—值对等。如果你对名称—值对这一概念已经很熟悉了，那么JSON 看上去也会很亲切。在名称—值对中，你首先要声明一个名称，例如 "animal"。然后把它凑成 一对：一个名称加一个值。我们来给这个名称(本例中的 "animal")一个 值。在JSON 中，名称—值 对的值还可以是数字、布尔值、null、数组或对象。

## 2.3 正确的JSON语法

名称，也就是我们示例中的 "animal"，始终需要被双引号包裹。双引号中的名称可以是任何有效的字符 串，所以你的名称即使看起来像下面这样，在 JSON 中也是完全合法的:
"My animal": "cat"
你甚至可以在名称中使用单引号:
"Lindsay's animal": "cat"
这是因为，JSON 中的名称—值对是一种对许多系统都十分友好的数据结构，而使用空格和特殊字符(即 a~z、0~9 除外的其他字符)忽略了可移植性。

## 2.4 语法验证

和机器不同，对我们这些敲键盘的人来说，只要少敲个字就能酿成错误。 我们没有创造比想象中更多的错误，真的是很神奇。所以当你在工作中使 用 JSON 时，很重要的一点就是验证。你使用的集成开发环境(integrated development environment，IDE)也许会内置JSON的验证。

## 2.5 JSON文件

你可能会觉得在今后使用 JSON 时，仅能在代码中创建它并传输到一个仅 可通过开发者工具来查看的不可见的世界。然而，JSON 这种数据交换格 式是可以作为独立的文件存在于文件系统中的。它的文件扩展名非常好 记:.json。
因此，我可以将“animal/cat”保存到计算机中的一个 JSON 文件中，比如 C:/animals.json。

## 2.6 JSON的媒体类型

当你在传递数据时，需要提前告知接收方数据是什么类型，这就会涉及媒 体类型。媒体类型也有一些你可能听过的其他称呼，如“互联网媒体类型”“内容类型”或“MIME 类型”。它使用“类型/子类型”这种格式来表 示，比如你可能见过的text/html。
JSON 的 MIME 类型是application/json。

## 3.1 数据类型简介

在计算机中，我们需要知道正在处理什么类型的数据，因为不同类型的数 据有着不同的操作途径。可以让两个数相乘，但是不能让一个单词和一个数相乘。如果我有一个单词表，可以按字母顺序给它们排序。但是数字5可没有字母顺序。所以在编程中，当一个方法(或函数)说“请给我传递 一个数字”时，如果我们知道什么是数字的话，就不会错把单词“ketchup”传给它。

在计算机科学中，有一种数据类型被称为原始数据类型。这里所指的数据类型可不是像原始人那样粗陋的数据，确切地说，它们指的是数据中最基本的一种类型。

## 3.2 JSON中的数据类型

虽说对于复合数据类型，乃至于一小部分原始类型来说，它们的编程语言存在许多差异，但我最开始提到的原始类型，大多数语言中都是涵盖的:
* 数字(如 5 或 5.09)：整型、浮点数、定点数
* 字符和字符串(如“a”“A”或“apple”)
* 布尔类型(即真或假)

对象数据类型是在大多数编程语言中都很常见且流行的数据类型，如 Java或C#，不过不是全部。数据交换格式是以让不同的两个系统间能够进行交流为目标的，这一格式所表达的必须是共有的部分。复合数据类型对象的数据结构可以被解构为原始数据类型。JSON 中的数据类型包括:
* 对象
* 字符串
* 数字
* 布尔值
* null
* 数组

## 3.3 JSON中的对象数据类型

JSON 中的对象类型非常简单。追根溯源，JSON 本身就是对象，也就是一个被花括号包裹的名称—值对的列表。如果你希望在作为对象的JSON中创建一个名称—值对，那就需要用到嵌套。

## 3.4 JSON中的字符串类型

前面我们曾通过“animal/cat”这个示例简单讨论了JSON中的字符串类型：`{ "animal" : "cat" }`。这里的 "cat" 就是一个字符串类型的值。JSON 中的字符串可以由任何`Unicode`字符构成，因此上面的例子中的所有字符都是可以使用的。字符串的两边必须被双引号包裹。

## 3.5 JSON中的数字类型

数字是一种常见的用于传递数据的信息片段。库存数目、金额、经度/纬度以及地球的质量等均可以用数字来表示，JSON中的数字可以是整数、小数、负数或者指数。


## 3.6 JSON中的布尔类型

在口语中，对问题最简单的回答莫过于肯定或否定。在计算机编程中，布尔类型是很简单的。它不是真就是假。如果你问你
的电脑一个疑问句，它就会回答“真”(true)或 “假”(false)。在一些编程语言中，`true`的字面值可能用1来表示，`false`用0来表示。

## 3.7 JSON中的null类型

对于一无所有的东西，你可能觉得用0来描述比较合适。比如，我有 0 个手表。但事实是，0是一个数字。这意味着本质上是在计数。不要把`null`和`undefined`混淆，尤其是在使用 JavaScript 时。在JavaScript中，`undefined`与那些声明的名称和值都不存在的对象或变量有关，而null则仅与对象或变量的值 有关。null是一个表示“没有值”的值。在 JSON中，null必须使用小写形式。

## 3.8 JSON中的数组类型

现在探讨一下数组数据类型。如果你对数组不熟悉也没关系，我们先来简单介绍一下。想象一个装着一打鸡蛋的容器。容器里有12个位置用来放鸡蛋。我刚买下这些鸡蛋时，数量是12个。这就相当于有一个大小为12的数组，包含12个鸡蛋。

有需要PDF的同学可以添加微信：

![](http://pic.automancloud.com/WechatIMG24.jpeg)

---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [Selenium 4.0 Alpha更新日志](https://mp.weixin.qq.com/s/tU7sm-pcbpRNwDU9D3OVTQ)
- [Selenium 4.0 Alpha更新实践](https://mp.weixin.qq.com/s/yT9wpO5o5aWBUus494TIHw)
- [如何统一接口测试的功能、自动化和性能测试用例](https://mp.weixin.qq.com/s/1xqtXNVw7BdUa03nVcsMTg)

## 非技术文章精选

- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)
- [API测试基础](https://mp.weixin.qq.com/s/bkbUEa9CF21xMYSlhPcULw)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)