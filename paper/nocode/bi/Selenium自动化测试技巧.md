# Selenium自动化测试技巧



与以前瀑布式开发模式不同，现在软件测试人员具有使用自动化工具执行测试用例套件的优势，而以前，测试人员习惯于通过测试脚本执行来完成测试。

但自动化测试的目的不是完全摆脱手动测试，而是最大程度地减少手动运行的测试。自动化测试使您可以快速测试多语言站点，还可以提高测试执行速度。

自动化测试的过程很简单，请参考：[自动化测试生命周期](https://mp.weixin.qq.com/s/SH-vb2RagYQ3sfCY8QM5ew)。

# Selenium自动化

由于开源工具和应用程序的成本效益，效率，可重复性，准确性和易用性，它们变得越来越重要。`Selenium`是开放源代码工具之一，它在应用程序测试方面提供了开放源代码工具的所有优点。

`Selenium`是用于测试的一套`Selenium`工具。它包含`Selenium IDE`，`Selenium RC`，`Selenium Webdriver`和`Selenium Grid`。它用于自动化`Web`交互和回归测试，并具有记录和回放功能。此外还可以将记录的脚本导出为其他语言，包括`Java`，`C＃`，`Python`，`Ruby`，`Javascript`和`PHP`。

# 跨浏览器测试中的Selenium

顾名思义，跨浏览器测试是一种用于在不同的Web浏览器和设备上测试Web应用程序以确保其在每个设备和浏览器上都能无缝运行的方法。


Selenium帮助在Safari，Google Chrome，Mozilla Firefox和IE中自动化测试案例。Selenium也可以同时在不同浏览器上的同一台计算机上执行测试用例。它还支持多种语言和操作系统。参考文章: [如何在跨浏览器测试中提高效率](https://mp.weixin.qq.com/s/MB_Wv7yQ6i9BztAZtL4grA)

让我们看一下`Selenium`的最佳实践，以在自动化测试过程中充分利用。

## 利用正确的定位器

`Selenium`框架的底部是与浏览器进行交互，从而可以使用文档**对象模型（DOM）**检查，输入和浏览多个对象。这是通过一组操作发生的，并使用了多个定位器，包括`CSS`选择器，`name`，`Xpath`，`ID`，`标记名`，`链接文本`和`classname`。

例如，当您不想在开发人员和测试人员不了解的情况下更改代码时，请使用`Class`和`ID定位器`。另一方面，当其他团队进行测试时，可以使用链接文本来动态处理情况。最后，可以采用`XPath`可用于定位。

## 数据驱动的测试 
如果要为不同的输入使用相同的测试和相同的代码，则可以依赖`Selenium`。它将允许开发人员和质量检查团队进行修改，这意味着您可以将其用于系统功能测试以及浏览器兼容性测试。

`Selenium`还允许客户从其框架中受益。客户可以利用专有的测试加速器并启动测试自动化。这将减少自动化周期时间。有很多个函数库，可让客户端启动自动化过程。

## 不要依赖特定的驱动程序 
永远不要依赖于一种特定的驱动程序实现。了解驱动程序在不同的浏览器中不是瞬时的。也就是说，不一定会有`IE`驱动程序、`FireFox`驱动程序等。

例如，在连续`Linux`构建过程中执行集成测试时，将收到`RemoteDriver`。您可以使用`LabelledParameterized`（`JUnit`具有，`@RunWith` 而`TestNG`为 `@Parameters`）在`Selenium`中快速创建小型框架。

和`ScreenShotWatchMan`（`JUnit` `@Rule`，`TestNG` `TestListenerAdapter`）。换句话说，使用参数注释来处理多种浏览器类型并准备好同时执行是不错的选择。

## 选择器顺序

选择选择器的顺序很重要，因为选择器（例如`XPath`和`CSS`）是基于位置的。与`ID`，`name`和`链接文本`相比，它们比较慢。`name`和`ID`是特别直接和直接的方式选择器。`CSS`通常是`ID`和`Name`的组合。相比之下，`XPath`应该是最后的解决方案。

健壮的解决方案如下所示：  `XPath` <`CSS` <`Links Text` <`Name` <`ID`。这意味着以ID开头，并使XPath为最后一个选择器。在3个没有数据的表中，XPath识别第二个表的速度最慢，并且可能不会返回正确的表。因此，最后选择了XPath，它们很脆弱。`CSS`始终与名称和`ID`结合在一起。

## 使用`PageObjects`设计模式

**PageObject**已作为测试自动化中的最佳设计模式而获得普及。它提升了测试的可维护维护性，还减少了代码重复量。此外，它是一个面向对象的类，它充当被测应用程序页面的接口。为简化起见，**PageObject**是一种面向对象的设计模式，并且将网页定义为类。页面上的不同元素将成为变量。用户交互被用具体的方法实现。

* 网页=类别
* 页面上的各种元素=变量
* 用户互动=方法

* **PageObject**的优点

* 通过较小的UI调整，它有助于建立一个健壮的框架。测试代码和页面代码是分开的。
* 它们可靠且易于维护。
* 该脚本是可读的。该代码是可重用的。
* 几乎完全消除重复。

## 提倡`wait`避免`sleep`

利用`wait`代替`sleep`。了解显式和隐式等待，还有`Thread.sleep()`逻辑。然后，为什么`wait`等待而不是`sleep`。

* `wait`
* 
显式–等待某种情况发生，而无需继续编写代码。

隐式–指示WebDriver轮询DOM，直到完成对元素的搜索为止。默认情况下，时间设置为0。

* `sleep`

`Thread.sleep()`无论工作页是否准备就绪，都会在括号内指定的秒数内等待。


## 关闭Firebug起始页

在启动`firefox`驱动程序时，可能已包含`firebug`。有时这可能导致无法工作正常。如果在启动浏览器时同时打开一个新的`firebug`选项卡使您感到烦恼，请按照以下提供的提示之一关闭firebug起始页。

* 在`showFirstRunPage`标志中将**False**设置，如下。

```Java
FirefoxProfile profile = new FirefoxProfile();
profile.setPreference("extensions.firebug.showFirstRunPage", false);
```

# 参考文章：

- [Selenium 4.0 Alpha更新日志](https://mp.weixin.qq.com/s/tU7sm-pcbpRNwDU9D3OVTQ)
- [Selenium 4.0 Alpha更新实践](https://mp.weixin.qq.com/s/yT9wpO5o5aWBUus494TIHw)
- [JUnit 5和Selenium基础（一）](https://mp.weixin.qq.com/s/ehBRf7st-OxeuvI_0yW3OQ)
- [JUnit 5和Selenium基础（二）](https://mp.weixin.qq.com/s/Gt82cPmS2iX-DhKXTXiy8g)
- [JUnit 5和Selenium基础（三）](https://mp.weixin.qq.com/s/8YkonXTYgAV5-pLs9yEAVw)
- [Selenium Python使用技巧（一）](https://mp.weixin.qq.com/s/39v8tXG3xig63d-ioEAi8Q)
- [Selenium Python使用技巧（二）](https://mp.weixin.qq.com/s/uDM3y9zoVjaRmJJJTNs6Vw)
- [Selenium Python使用技巧（三）](https://mp.weixin.qq.com/s/J7-CO-UDspUGSpB8isjsmQ)
- [Selenium并行测试基础](https://mp.weixin.qq.com/s/OfXipd7YtqL2AdGAQ5cIMw)
- [Selenium并行测试最佳实践](https://mp.weixin.qq.com/s/-RsQZaT5pH8DHPvm0L8Hjw)

--- 
* 公众号**FunTester**首发，欢迎关注，禁止第三方擅自转载。合作请联系`Fhaohaizi@163.com`。

### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)
- [有关UI测试计划](https://mp.weixin.qq.com/s/D0fMXwJF754a7Mr5ARY5tQ)
- [软件测试外包](https://mp.weixin.qq.com/s/sYQfb2PiQptcT0o_lLpBqQ)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)