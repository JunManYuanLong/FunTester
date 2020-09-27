# Selenium4 IDE特性：弹性测试、循环和逻辑判断

书接上文和上上文：
- [Selenium4 IDE，它终于来了](https://mp.weixin.qq.com/s/XNotlZvFpmBmBQy1pYifOw)
- [Selenium4 IDE特性：无代码趋势和SIDE Runner](https://mp.weixin.qq.com/s/G0S9K0jHsN0P_jxdMME-cg)

## Selenium IDE现在提供更多的弹性测试

在自动化测试过程中，会遇到以下情况：上一个软件版本中通过的自动化测试用例在新版中失败。这种情况在`Selenium`测试自动化中很常见。

大概率是由于`UI`代码的更改，对其执行操作的**Web定位器**（自动化测试的一部分）可能已更改。随着产品的迭代，测试变得更加复杂，由于某些元素定位器的**可访问性**问题而导致的某些测试失败会降低测试效率。

**Selenium4 IDE**在自动化测试中建立了弹性测试，因为它为与之交互的每个元素记录了多个定位符。在重放或者执行过程中，如果**Selenium4 IDE**无法找到特定的定位器，它将尝试与网页上的所有其他定位器一起使用，直到其中一个成功。

下面显示的是**Selenium4 IDE**演示Demo，其中介绍了**Selenium4 IDE**如何构建测试弹性。除了基于*CSS*的定位器之外，**Selenium4 IDE**还捕获`Click`命令的`XPath`表达式或者其他定位方式。

![](http://pic.automancloud.com/WX20200917-150016@2x.png)

## 支持循环和条件逻辑

在`Selenium`测试自动化过程中，测试人员会遇到仅在满足某些条件时才必须执行一组命令的场景。例如，在使用任何cookie，本地权限等之前，都会有收到需用户确认的弹框或者通知。

**Selenium4 IDE**可以通过条件分支实现条件逻辑（或控制流程），从而可以更改测试中的行为。它还支持循环执行测试，在测试中，可以根据预定义的标准重复执行一组命令。

下面是一些有助于**Selenium4 IDE**中的条件分支和循环的流行控制流命令包括：

* if、else if、else、end
* times、end
* do、repeat if
* while、end

下面显示的是此**Selenium4 IDE**教程中的条件分支示例：

![](http://pic.automancloud.com/fdsaaafdsaggds546.png)

* 这里除了`execute script`意外，还很很多`command`可选，由于本身水平有点，这里我判断应该使用的是`JavaScript`脚本和语法。有需要的同学可以去翻一翻`JavaScript`这门语言的基础，这个在**Selenium4 IDE**其他特性中也有体现其巨大价值的地方。

----
公众号**FunTester**首发，原创分享爱好者，腾讯云和掘金社区首页推荐，知乎七级原创作者，欢迎关注、交流，禁止第三方擅自转载。

FunTester热文精选
=

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)
- [测试开发工程师工作技巧](https://mp.weixin.qq.com/s/TvrUCisja5Zbq-NIwy_2fQ)
- [Selenium4 IDE，它终于来了](https://mp.weixin.qq.com/s/XNotlZvFpmBmBQy1pYifOw)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [为什么测试覆盖率如此重要](https://mp.weixin.qq.com/s/0evyuiU2kdXDgMDnDKjORg)
- [为什么测试覆盖率如此重要](https://mp.weixin.qq.com/s/0evyuiU2kdXDgMDnDKjORg)
- [吐个槽，非测误入。](https://mp.weixin.qq.com/s/BBFzUZVFMmU7a6qfLKas2w)
- [自动化测试框架](https://mp.weixin.qq.com/s/vu6p_rQd3vFKDYu8JDJ0Rg)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDnHxttBoq6jhgic4jJF8icbAMdOvlR0xXUX9a3tupYYib3ibYyIHicNtefS3Jo7yefLKlQWgLK7bCgCLA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)