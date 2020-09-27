# Selenium 4 Python的最佳测试框架

[原文地址](https://www.lambdatest.com/blog/top-5-python-frameworks-for-test-automation-in-2019/)

随着Python语言的使用越来越流行，基于Python的测试自动化框架也越来越流行。在项目选择最佳框架时，开发人员和测试人员会有些无法下手。做出选择是应该判断很多事情，框架的脚本质量，测试用例的简单性以及运行模块并找出其缺点的技术。这篇文章总结了测试自动化领域，适合Web端自动化框架（基于Python语言）以及它们相对于其他方面的优缺点。因此，可以帮助一些人根据需要和实际情况选择合适的的Python框架进行测试自动化。

## Robot Framework
Robot Framework主要用于测试驱动的开发以及验收测试，它是最好的测试框架之一。虽然是用Python开发的，它也可以运行的`IronPython`，这是`.NET`为基础的和基于`Java`的`Jython`的。Robot Framework作为Python框架可在所有平台上兼容：Windows，MacOS或Linux。

### 前提

* 首先，只有安装了Python 2.7.14或更高版本的Python，您才能使用Robot Framework（RF）。
* 您还需要安装“pip”或其他python软件包管理器。
* 最后，必须下载一个IDE。在开发人员中流行的工具是PyCharm社区版。

Robot Framework的优缺点是什么？让我们看一下与其他Python框架相比，作为测试自动化框架的Robot的优缺点是什么。

### 优点

* 通过使用关键字驱动的测试方法，它可以帮助测试人员轻松创建可读性很高的测试用例，从而使自动化流程更加简单。
* 测试数据语法可以轻松使用并组合。
* 它由通用工具和测试库组成，具有完整的生态系统，可以在单独的项目中使用各个功能。
* 该框架具有许多API，具有很高的可扩展性。
* Robot Framework框架可通过Selenium Grid运行并行测试，但需要自行开发相关功能。


### 缺点

* 虽然Robot Framework听起来很方便，但是在创建自定义`HTML`报告时却很不方便。
* Robot Framework框架的另一个缺陷是自身并行测试能力不足。

### 机器人是最适合您的Python测试框架吗？

如果您是自动化领域的初学者，并且在开发方面经验较少，那么将Robot Framework用作顶级Python测试框架比pytest或pyunit更容易使用，因为它具有丰富的内置库并且使用更容易的面向测试的DSL。但是，如果要开发复杂的自动化框架，最好切换到pytest或任何其他涉及Python代码的框架。

## pytest
pytest用于各种软件测试，是测试自动化的另一个顶级Python测试框架。该工具是开源的，易于学习，可以被质量保证团队、开发团队以及个人实践或者开源项目使用。由于其“断言重写”等有优秀的功能。相当一部分项目已经从unittest（Pyunit）切换为pytest。

### 前提
除了具有Python的使用知识外，pytest不需要任何复杂的东西。您所需要的只是一个具有命令行界面，python软件包管理器和开发IDE的工作桌面。

pytest的优缺点是什么？

### 优点

* 在pytest到来之前，大多数人将其测试内容包含在大型类中。随着pytest带来了革命，使得以更紧凑的方式编写测试用例成为现实。
* Pytest将所有值存储在测试用例中，测试完成之后通知哪个值断言失败和哪个值被断言。
* 由于不需要太多模板代码，因此测试用例更易于编写和理解。
* 在pytest中，可以通过讲功能模块化帮助覆盖所有参数组合而无需重写测试用例。
* pytest的丰富实用的插件，使该框架可扩展性极高。例如，pytest-xdist可以用于执行并行测试，而无需使用其他测试运行程序。单元测试也可以参数化，而无需重复任何代码。

### 缺点

* pytest使用规范和语法意味着开发者必须考虑兼容性。你方便地编写测试用例，但比较难将这些测试用例与任何其他测试框架一起使用。

### Pytest是最适合您的Python测试框架吗？

必须首先学习一种成熟的语言，但是一旦掌握了这种语言，您将获得所有技能点，例如静态代码分析，对多个IDE的支持以及最重要的是编写有效的测试用例。对于编写功能测试用例和开发复杂的框架，它比unittest更好，但是如果您的目标是开发简单的框架，则它的优势与Robot Framework有点相似。

## UnitTest也称为PyUnit
Unittest或PyUnit是Python自带的用于单元测试的标准测试自动化框架。它受到JUnit的极大启发。基类TestCase提供断言方法以及所有设置规范。TestCase子类中每个方法的名称均以“test”开头。这使它们可以作为测试用例运行。您可以将加载方法和TestSuite类用于该组并加载测试。您可以一起使用它们来构建自定义的测试运行器。就像使用JUnit进行Selenium测试一样，unittest也可以使用unittest-sml-reporting并生成XML报告。

### 前提
几乎没有前提条件，因为Python默认情况下自带unittest。要使用它，您将需要python框架的标准知识，并且如果您想安装其他模块，则需要安装pip以及一个IDE进行开发。

PyUnit的优点和缺点是什么？

### 优点

作为Python标准库的一部分，使用Unittest有多个优点。

* 开发人员不需要安装任何其他模块，因为该模块随附了该模块。
* Unittest是xUnit的派生产品，其工作原理类似于其他xUnit框架。熟悉的语法和规范会让初学者觉得轻松一些。
* 可以以更简单的方式运行各个测试用例，需要做的就是在终端上指定名称。输出也很简洁，在执行测试用例时更加灵活。
* 测试报告在毫秒内生成。

### 缺点

* 通常，snake_case用于命名python代码。但是由于该框架从Junit中获得了很多启发，因此传统的camelCase命名方法仍然存在。这可能会令人感到困惑和混乱。
* 测试代码的会变得难以阅读，因为它过多地支持抽象。

### PyUnit是您的最佳Python测试框架吗？

pytest引入了某些惯用作法，使测试人员可以以非常紧凑的方式编写更好的自动化代码。尽管unittest是默认的测试自动化框架，但是它的工作原理和命名约定与标准Python代码略有不同，这使它不是Python自动化测试的首选框架。

## 总结
在上面的内容中，我们讨论了基于不同测试过程的Python框架。pytest，Robot框架，单元测试用于功能和单元测试。我们可以得出结论，对于功能测试，pytest是最好的。但是，如果您不熟悉基于python的自动化测试，那么Robot Framework是入门的绝佳工具。尽管功能有限，但是它将使您轻松地走上正轨，快速产出效果明显。

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