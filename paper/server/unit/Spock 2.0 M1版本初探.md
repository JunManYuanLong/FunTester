# Spock 2.0 M1版本初探



> spock2进行了较大的升级，基于Junit5，基于Groovy3（Groovy3要求JDK9+）

重要说明：不建议将您的现实项目迁移到Spock 2.0 M1！这是2.x的第一个（预）发行版，未完成API，旨在收集与内部Spock迁移到JUnit Platform有关的用户反馈。

## 由`JUnit Platform`提供支持

`Spock 2.0 M1`的主要变化是向`JUnit 5`的迁移（确切地说，是使用`JUnit Platform 1.5`（是`JUnit 5`的一部分而不是`JUnit 4`运行器API 执行测试）。这非常方便，因为应该在支持JUnit平台的任何地方（IDE，构建工具，质量保障工具等）自动识别并执行`Spock`测试。另外，平台本身提供的功能也应该也适用于`Spock`。

要将Spock 2引入Gradle项目，需要修改Spock版本：

`testImplementation('org.spockframework:spock-core:2.0-M1-groovy-2.5')`

并通过JUnit平台激活测试执行：


```Groovy
test {
    useJUnitPlatform()
}
```
另一方面，对于Maven，仍然需要切换到Never Spock版本：

```
<dependency>
  <groupId>org.spockframework</groupId>
  <artifactId>spock-core</artifactId>
  <version>2.0-M1-groovy-2.5</version>
  <scope>test</scope>
</dependency>
```

但这就是全部。如果找到了`junit-platform-engine`（Spock 2的传递依赖项），则`Surefire`插件（如果使用版本3.0.0+）默认执行`JUnit Platform`测试。

## 其他变化

由于具有向`JUnit Platform`迁移的巨大变化，`Spock 2.0 M1`中的其他变化数量有限，从而使查找潜在的回归原因变得容易一些。作为迁移本身的副作用，目前所需的Java版本是8。

此外，所有参数化测试都会自动进行。但是，那太好了，目前还没有办法 “滚动”特定的测试，如Spock 1.x的`spock-global-unroll`所知。

`SpockReportingExtension`在发行说明中可以找到一些其他更改（例如暂时禁用）。


## JUnit 4 Rule问题

使用`JUnit 4 @Rule`的测试`@ClassRule`可能会失败，并显示错误消息，提示未在测试（例如`NullPointerException`或`IllegalStateException: the temporary folder has not yet been created`）之前创建/初始化所请求的对象，或者在测试之后未进行验证/清除（例如，来自`AssertJ`的软断言） 。`JUnit`平台不再支持`Rules API`。但是，为了使迁移更容易（`@TemporaryFolder`可能在基于`Spock`的集成测试中经常使用），有一个专用工具`spock-junit4`可以在内部将`JUnit 4`规则包装到`Spock`扩展中，并在`Spock`的生命周期中执行它。由于它是作为全局扩展实现的，因此唯一需要添加的就是另一个依赖项。在Gradle中：

`testImplementation 'org.spockframework:spock-junit4:2.0-M1-groovy-2.5'`

或在Maven中：

```
<dependency>
    <groupId>org.spockframework</groupId>
    <artifactId>spock-junit4</artifactId>
    <version>2.0-M1-groovy-2.5</version>
    <scope>test</scope>
</dependency>
```

## 其他问题

`Spock 2.0 M1`仅使用`Groovy 2.5.8`进行编译和测试。从`M1`开始，当前在运行时阻止使用`Groovy 3.0`执行。不幸的是，没有关于不兼容的Groovy版本的明确错误消息，只有一个非常隐秘的错误消息：

```
Could not instantiate global transform class org.spockframework.compiler.SpockTransform specified at
jar:file:/.../spock-core-2.0-M1-groovy-2.5.jar!/META-INF/services/org.codehaus.groovy.transform.ASTTransformation
because of exception java.lang.reflect.InvocationTargetException
```

令人遗憾的是，仅对`Groovy 2.5`的限制减少了使用`Groovy 3`工作的人们的潜在反馈，该反馈非常接近稳定版本（RC2）。由于许多`Spock`测试仅适用于`Groovy 3`（特别是某些极端情况），因此特别不方便。Spock 2在发行版之前可能会被调整为Groovy 3中的更改已提供兼容性，或者至少会取消上述硬性限制。

---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [Selenium 4.0 Alpha更新日志](https://mp.weixin.qq.com/s/tU7sm-pcbpRNwDU9D3OVTQ)
- [Selenium 4.0 Alpha更新实践](https://mp.weixin.qq.com/s/yT9wpO5o5aWBUus494TIHw)
- [如何统一接口测试的功能、自动化和性能测试用例](https://mp.weixin.qq.com/s/1xqtXNVw7BdUa03nVcsMTg)

## 非技术文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)
- [API测试基础](https://mp.weixin.qq.com/s/bkbUEa9CF21xMYSlhPcULw)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640)