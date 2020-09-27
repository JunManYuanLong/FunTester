# JUnit 5和Selenium基础（一）

[原文地址](https://www.javacodegeeks.com/2019/09/junit-selenium-setup-project-gradle-jupiter-selenium.html)

> Gradle、JUnit 5和Jupiter Selenium

[原文地址](https://www.javacodegeeks.com/2019/09/junit-selenium-setup-project-gradle-jupiter-selenium.html)

Selenium是一组支持浏览器自动化的工具，主要用于Web应用程序测试。Selenium的组件之一是Selenium WebDriver，它提供客户端库，JSON有线协议（与浏览器驱动程序进行通信的协议）和浏览器驱动程序。Selenium WebDriver的主要优点之一是，它几乎支持所有主要编程语言，并且可以在所有主流操作系统上运行。


## 测试准备

首先，Java JDK是必需的，并且必须将其安装在系统中。我建议安装OpenJDK而不是Oracle JDK。您还需要`Gradle`来启动一个新项目和您喜欢的Java IDE –建议使用IntelliJ IDEA Community或Professional，当然也少不了git。

开始之前，确保已安装以下工具并可供您使用：

* Java JDK：建议使用最新的Java JDK版本
* Gradle：仅在设置项目时需要，建议使用Gradle 5.6+
* Java IDE：IntelliJ IDEA
* Chrome浏览器：用于运行Selenium测试
* 终端：用于执行shell命令，至少对Unix命令具有基本支持
* Git：跟踪源代码历史记录


## 从头开始

要创建一个基于Gradle的空项目，请打开终端并输入：

```
mkdir demo
cd demo
gradle init --type basic --dsl groovy
```

生成的项目是一个空的DIY项目：没有插件，也没有依赖项。它带有settings.gradle可以删除的冗余：

`rm settings.gradle`

## Java和JUnit 5

对于使用JUnit 5的基本Java项目配置，将以下内容添加到中build.gradle：

```
plugins {
 id 'java'
}
 
repositories {
  mavenCentral()
}
 
dependencies {
 testImplementation('org.junit.jupiter:junit-jupiter:5.5.1')
}
 
test {
 useJUnitPlatform()
 testLogging {
  events "passed", "skipped", "failed"
 }
}

```
上面的DSL配置了`Gradle`的Java插件`（plugins）`，该插件为我们提供了使用Gradle构建基于Java的项目的功能。该项目使用Maven存储库`（repositories）`下载在项目`dependencies`中声明的项目依赖项`（）`。将项目的测试实现依赖项设置为`JUnit 5（testImplementation）`，并调整任务`（test）`，以确保在使用`Gradle`执行测试时使用`JUnit 5`。

可以通过在终端中执行Gradle构建来验证配置：

`./gradlew build`

构建成功：

```
BUILD SUCCESSFUL in 0s
1 actionable task: 1 executed
```

`./gradlew`命令运行Gradle Wrapper而不是全局Gradle发行版。该项目是由Gradle Wrapper生成的，因此根本不需要执行全局Gradle分发即可执行任务和使用该项目。

## JUnit Jupiter

为了简化项目中`Selenium WebDriver`的配置，我将使用`Selenium Jupiter`，它是JUnit 5扩展，旨在简化`JUnit 5`测试中对Selenium（`WebDriver`和`Grid`）的使用。它是一个单独的依赖项，需要添加到以下项的依赖项列表中`build.gradle`：

```Groovy
dependencies {
  testCompile('io.github.bonigarcia:selenium-jupiter:3.3.0')
}
```

`Selenium Jupiter`库提供了与`Selenium`和`Appium`的集成。`Selenium Jupiter`支持本地和远程浏览器，Docker容器中的浏览器（需要Docker引擎）以及基于Selenide的浏览器配置。它在内部使用`WebDriverManager`来管理浏览器驱动程序。

* 注意：不要惊讶于项目中有很多库，Selnium Jupiter有很多依赖性。要查看所有项目依赖项（包括传递性依赖项），请执行以下命令：`./gradlew dependencies`。

## 目录和项目文件

该项目创建时没有Java源文件。要创建初始目录和第一个测试，可以执行以下命令：

```
mkdir -p src/test/java/demo/selenium/todomvc
touch src/test/java/demo/selenium/todomvc/SeleniumTest.java
```

该`SeleniumTest.java`文件包含非常基本的测试，确认项目已正确配置。该测试使用`Selenium Jupiter`提供的`JUnit 5`扩展，并且只有一个测试，没有断言：

```Java
package pl.codeleak.demos.selenium.todomvc;
 
import io.github.bonigarcia.seljup.SeleniumExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeDriver;
 
@ExtendWith(SeleniumExtension.class)
class SeleniumTest {
 
    @Test
    void projectIsConfigured(ChromeDriver driver) {}
}
```
## 运行测试

执行Gradle构建应确认测试通过：

```
./gradlew build
 
demo.selenium.todomvc.SeleniumTest > projectIsConfigured() PASSED
 
BUILD SUCCESSFUL in 1s
3 actionable tasks: 2 executed, 1 up-to-date
```

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

