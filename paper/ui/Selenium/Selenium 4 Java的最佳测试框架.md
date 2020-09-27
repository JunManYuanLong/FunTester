# Selenium 4 Java的最佳测试框架

[原文地址](https://www.lambdatest.com/blog/top-5-java-test-frameworks-for-automation-in-2019/)

几十年来，Java一直是开发应用程序服务器端的首选编程语言。尽管JUnit一直在与开发人员一起帮助他们进行自动化的单元测试，但随着时间的推移和测试行业的发展，特别是伴随着自动化测试的兴起，已经开发了许多基于Java的开源框架，它们在验证和业务逻辑方面与JUnit有所不同。在这里，我将讨论用于使用Selenium WebDriver执行测试自动化的顶级Java测试框架，还将重点介绍这些顶级Java测试框架的优缺点和独到之处。

## JUnit

Junit是开发人员基于xUnit基础上开发的一个实用案例。其最初主要目的是使Java开发人员能够编写脚本并执行可重复的测试用例。它通常用于测试一小段代码。您还可以通过将JUnit与用于测试自动化的Selenium集成来执行网站的自动化测试。每当添加任何新代码需要发版时，都需要重新执行整个测试用例，并确保没有不影响原有功能。

### 有哪些先决条件？
该框架与Selenium WebDriver for Java高度兼容，因此，JUnit和Selenium WebDriver也是完全兼容的，作为某些先决条件，您需要

* 在工作项目中使用较新版本的JDK。
* 下载最新版本的JUnit并设置环境。
* 对面向对象的编程语言（Java）的应用程序开发有很好的使用经验。

### 使用JUnit的优缺点？

JUnit有几个优点：

* 在受测试驱动的环境中工作的开发人员发现它非常有好处，因为他们被迫阅读代码并查找是否存在BUG。
* 尽早检测到错误，从而使代码可靠性大大提高。
* 开发更具可读性且无错误的代码可以增强可信度。
* 使用最新版本的JUnit（版本5），可以轻松识别异常，也可以执行用旧版JUnit编写的测试用例。
* 您也可以将其与Java 5以及更高版本一起使用。

JUnit的唯一缺点是：

* 该框架无法执行依赖性测试。那就是我们需要TestNG的地方。

### JUnit是您的最佳Java测试框架吗？
JUnit和TestNG都执行相同的工作。它们的功能几乎相同，只是在两个框架中，JUnit无法进行依赖测试，并且参数化测试的实现过程不同。另外，由于JUnit长期使用，因此有更好的社区支持，它已被定义为使用Selenium WebDriver for Java的应用程序进行单元测试的标准。尽管TestNG的用户很少，但社区仍然很大，并且每天都在增长。因此，我们可以得出结论，对于Java测试框架，在TestNG或JUnit之间进行选择完全取决于应用程序的性质和要求。

## JBehave

我们都知道行为驱动开发（BDD)。这种测试类型以对业务用户透明的方式描述了验收测试。JBehave是用于BDD测试的另一个Java测试框架，主要与Selenium WebDriver for Java一起使用。使用JBehave的主要目的是使新手可以轻松理解和熟悉BDD。这是一种设计理念，它使应用程序的测试阶段更多地基于其行为。

### 有哪些先决条件？
使用JBehave的理想方法是与IDE集成。为此，除了必要的运行环境搭建配置，您还需要几个jar文件，例如

`Junit-4.1.0.jar，Jbehave-core-3.8.jar，Commons-lang-2.4.jar，Paranamer-2.5.jar，Freemarker-2.3.9.jar，Org.apacje.commons.io.jar，Org.apache.commons.collections.jar，Plexus-utils-1.1.jar`

### JBehave的优缺点

像所有其他BDD测试框架一样，JBehave在许多方面也具有优势。

* 使规范相似的不同项目的不同开发团队之间更好地协调，可以达到行为驱动开发的最重要目的。
* 由于规范相似，因此项目经理和利益相关者可以更好地了解开发团队和质量检查团队的输出。
* 由于JBehave具有详细的逻辑推理和思考功能，因此产品具有更好的可靠性。
* JBehave使用半正式语言，并且还具助于在团队结构中保持一致的行为的功能。

跟其他任何BDD测试工具一样，JBehave只有一个缺点。

* BDD测试工具的成功主要取决于项目中不同成员，利益相关者，开发人员，测试人员以及组织管理层之间的沟通。缺乏沟通可能会导致无法及时发现和快速解决的问题，进而可能导致导致应用程序发生错误或者与实际业务需求相悖，最终导致所有各方互相指责（甩锅）。

### JBehave是适合您的最佳Java测试框架吗？
JBehave的工作方式与Serenity相同。但是，如果您打算提高自动验收测试的效率，则最好将Serenity与JBehave集成在一起，以利用更好的测试体验。这是因为Serenity的核心概念基于BDD开发，并且还使用户能够编写功能强大且丰富的测试报告。

## Selenide
Selenide基于Selenium的测试框架，并由Selenium提供技术支持，是一种流行的工具，用于精确和更加直观的UI测试用例。对现代Web技术（如Ajax）进行测试具有一定的复杂性，例如超时、等待和断言等等。WebDriver是用于UI测试的流行工具，但缺少处理超时的功能。Selenide以简单的方式处理所有这些问题。另外，它更容易掌握和学习。只需要做的只是专注于业务逻辑，仅需执行几行简单的代码即可完成您的工作。

### Selenide的前提条件是什么？

Selenide的安装非常简单。如果使用的是Maven，则需要在`pom.xml`文件中添加以下几行。
```
<dependency>
    <groupId>com.codeborne</groupId>
    <artifactId>selenide</artifactId>
    <version>5.1.0</version>
    <scope>test</scope>
</dependency>
```
### Selenide的优缺点

在服务器端使用Java的应用程序的前端层中，测试人员面临的最常见问题是超时。您编写的测试用例目前可能工作正常，但是几天后，也许一些Ajax请求将比现在花费更多的时间，或者某些Javascript的运行速度会变慢。另外，您的系统可能同时运行另一个进程，从而导致测试用例失败。更不幸的是，您可能需要花费数天的时间才能找出这些问题的根源。硒化物可以帮助您

* 简洁的测试用例编写过程消除了超时问题。
* 支持使用AngularJS开发的应用程序的测试
* 减少了传统Selenium工具使用的大多数调用函数。

到目前为止，我们还没有发现使用Selenide明显缺点，大概唯一勉强称作缺点的就是语法中各种符号。

下面放一个Demo大家就懂了：

```
@Test
public void userCanLoginByUsername() {
    open("/login");
    $(By.name("user.name")).setValue("johny");
    $("#submit").click();
    $(".loading_progress").should(disappear); // Waits until element disappears
    $("#username").shouldHave(text("Hello, Johny!")); // Waits until element gets text
}
```

网上有人发现这个Demo跑不起来，其实是缺少了必要的初始化步骤，如下：

```
        Configuration.browser = "Chrome";
        Configuration.baseUrl="https://www.baidu.com";
```


### Selenide是最适合您的Java测试框架吗？
对于UI测试，除了Selenide的父框架Selenium WebDriver之外，没有更好的基于Java的框架。显然，WebDriver无法解决由Ajax超时，JavaScript运行缓慢或任何需要花费时间加载的动态内容引起的问题。为了克服问题，我们先前在测试案例中使用了`wait_until`或`sleep`方法。使用Selenide，我们不再需要考虑那些问题。仅关注业务逻辑，测试用例就可以很好地实现其目的。

## Spock

Spock是一个从JUnit派生的测试自动化框架，用Groovy编写，允许您在JVM（Java虚拟机）上执行DDT（数据驱动测试）。它提供了对JVM支持的所有语言的兼容性。Spock提供的UI与任何其他Java测试框架相比都非常出色。代码的可读性和文档简洁明了，并且可以解释简单的句子，使其成为非常方便的Java测试框架。

### Spock作为Java测试框架的优势

Spock的优点：

* 出色的可读性，提供与普通英语句子的兼容性。
* Spock使模拟和存根比以往任何时候都容易。而且，两者都是内置的。
* 参数化更短，更清晰。
* 提供周围的环境，使查找故障原因更加容易。
* 富有表现力和简单的DSL（特定于域的语言）。

Spock的缺点：

* 您需要对Groovy有基本的了解。
* 如果您不熟悉Spock，则可能会觉得该框架有点不方便，但是一旦精通它，就不会使用其他Java测试框架。
* 对于其他基于Java的测试框架的高级语法兼容性较差（比如Mockito）

### Spock是最适合您的Java测试框架吗？

Spock拥有如此多的优点，相信Spock会成为BDD（行为驱动开发）的最佳Java测试框架，因为它使用简单，直观的UI和强大的DSL。如果碰巧你的项目是基于JVM的应用程序那他绝对是你的不二选择。


下面是之前写过的三篇spock的文章，点击查看详情
- [Maven和Gradle中配置单元测试框架Spock](https://mp.weixin.qq.com/s/kL5keijAAZwmq_DO1NDBtw)
- [Groovy单元测试框架spock基础功能Demo](https://mp.weixin.qq.com/s/fQCyIyeQANbu2YP2ML6_8Q)
- [Groovy单元测试框架spock数据驱动Demo](https://mp.weixin.qq.com/s/uCAB7Mxt1JZW229aKp-uVQ)

# 总结

在当前的敏捷时代，开发人员还需要参与测试。不必人人都是专家，但是至少应该具备编写自动验证代码的测试用例的基本知识。本文介绍了几种工具，这些工具为应用程序的UI测试，单元测试和BDD测试提供了开发人员的首选。如果真的想在工作表现突出，并希望成为更专业开发人员和测试人员，那么上面提到的工具将对日常工作绝对有所帮助。

---
* **郑重声明**：文章禁止第三方（腾讯云除外）转载、发表，事情原委[测试窝，首页抄我七篇原创还拉黑，你们的良心不会痛吗？](https://mp.weixin.qq.com/s/ke5avkknkDMCLMAOGT7wiQ)

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)

## 非技术文章精选

- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [自动化测试生命周期](https://mp.weixin.qq.com/s/SH-vb2RagYQ3sfCY8QM5ew)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [测试人员如何成为变革的推动者](https://mp.weixin.qq.com/s/0nTZHBOuKG0rewKAeyIqwA)
- [编写测试用例的技巧](https://mp.weixin.qq.com/s/zZAh_XXXGOyhlm6ebzs06Q)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)