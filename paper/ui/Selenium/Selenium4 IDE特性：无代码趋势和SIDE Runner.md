# Selenium4 IDE特性：无代码趋势和SIDE Runner

书接上文：[Selenium4 IDE，它终于来了](https://mp.weixin.qq.com/s/XNotlZvFpmBmBQy1pYifOw)

# Selenium4 IDE的趋势

**Selenium4 IDE**并不完美，但其优点在于，它不仅仅是功能强大的记录和回放工具。尽管不能认为它是广泛用于*Selenium*测试自动化的`Selenium WebDriver`的替代品，但它无疑为自动化浏览器测试增加了价值。这也是让我继续探索**Selenium4 IDE**新特性的的原因。

# 无代码自动化测试工具

由于**Selenium4 IDE**等工具有助于加快测试用例的创建，对进行无代码自动化测试的工具的需求将会更加强烈。测试人员不需要过多学习编码知识来编写测试用例，因此降低了自动化测试的进入门槛。它还可以最大程度地减少重复测试用例上花费的时间。

本**Selenium4 IDE**新特性探索系列文章中的`Selenium`测试自动化入门仅需要安装附加组件（或扩展名）。**Selenium4 IDE**允许用户通过录制操作作为测试一部分的内容来从**IDE**中直接生成测试用例。**无代码**工具变得越来越流行，**Selenium4 IDE**的*开发时间表*和*产品路线图*使无代码自动化测试的前景变得更加透亮！

# 积极地开发

旧的**Selenium IDE**项目的开发于2017年停止。该功能仅在*Firefox*浏览器中可用，并且宣布从*Firefox 55*起，将不再支持*Selenium IDE*。

后来，使用更先进*API*和*Selenium*测试自动化的重要功能（例如**并行测试**，**跨浏览器支持**，**弹性测试**）对**Selenium4 IDE**进行了重新构建，现在**Selenium4 IDE**中已经包含了需求中的大部分功能，未来还将添加更多有趣的功能。

下图一张`Selenium`团队`commit`活跃程度的图表：

![](http://pic.automancloud.com/Selenium-IDE-1.png)

新的**Selenium4 IDE**是开源的，大家可以在`GitHub`上找到代码。与`Selenium`测试套件中的其他工具一样，它也由`Selenium`社区管理。

# SIDE Runner

多浏览器的可用性使跨浏览器测试变得极为重要，因为*Web应用程序*需要在不同的浏览器之间无缝运行。如本[Selenium4 IDE](https://mp.weixin.qq.com/s/XNotlZvFpmBmBQy1pYifOw)文章中之前提到的，可以在不同的浏览器和`Selenium WebDriver`服务器上执行并行测试执行，而不需要编写任何代码！

**SIDE Runner**是可以在**Selenium4 IDE**中使用命令行运行测试用例的工具。在**Selenium4 IDE**中进行自动浏览器测试，如果在本地执行测试，则只需安装**SIDE Runner**并获取必要的浏览器驱动程序。

可以通过从终端触发以下命令来安装**SIDE Runner**运行程序（安装前必需安装*Node.js*包管理工具`npm`）：

`npm install -g selenium-side-runner`


```shell
# 安装Chrome驱动
npm install -g chromedriver
# 安装Edge驱动
npm install -g edgedriver
# 安装Geckodriver驱动
npm install -g geckodriver
# 安装IE驱动
npm install -g iedriver
```

使用**Selenium4 IDE**记录的测试用例必须另存为**.side**后缀的文件。

```shell
selenium-side-runner -c "browserName=chrome" <test.side>
selenium-side-runner -c "browserName='internet explorer'" <test.side>
selenium-side-runner -c "browserName=edge" <test.side>
selenium-side-runner -c "browserName=firefox" <test.side>
selenium-side-runner -c "browserName=safari"  <test.side>
```

## 通过SIDE运行器并行执行脚本

旧的**Selenium IDE**只能以串行方式执行测试用例（或测试套件）。对于自动浏览器测试，并行测试非常重要，因为它可以加速测试过程，极大减少测试运行时间。

[Selenium4 IDE](https://mp.weixin.qq.com/s/XNotlZvFpmBmBQy1pYifOw)使您可以并行执行测试。通过更改**Selenium4 IDE**中的相关设置，可以在测试套件中进行测试并行化。**SIDE Runner**运行程序还允许用户通过输入执行的并行进程数来控制并行执行测试。该`-w`选项用于控制正在运行的并行处理的数量。


----
**公众号[FunTester](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)首发，原创分享爱好者，腾讯云和掘金社区首页推荐，知乎七级原创作者，欢迎关注、交流，禁止第三方擅自转载。**

FunTester热文精选
=

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [开源测试服务](https://mp.weixin.qq.com/s/ZOs0cp_vt6_iiundHaKk4g)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)
- [测试开发工程师工作技巧](https://mp.weixin.qq.com/s/TvrUCisja5Zbq-NIwy_2fQ)
- [Fiddler Everywhere工具答疑](https://mp.weixin.qq.com/s/2peWMJ-rgDlVjs3STNeS1Q)
- [Selenium4 IDE，它终于来了](https://mp.weixin.qq.com/s/XNotlZvFpmBmBQy1pYifOw)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)