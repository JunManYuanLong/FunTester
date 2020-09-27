# Gradle+Groovy基础篇

[原文地址](https://www.javacodegeeks.com/2019/11/get-groovy-with-gradle.html)

在Java项目中，有两个主要的构建系统：Gradle和Maven。构建系统主要管理潜在的复杂依赖关系并正确编译项目。还可以将已编译的项目以及所有资源和源文件打包到`.war`或`.jar`文件中。对于简单的构建，Maven和Gradle之间的选择几乎是个人喜好之一，或者也许是公司CTO或技术经理的偏好。他们俩都是非常好的构建工具。但是，对于更复杂的项目，Gradle比Maven更胜一筹。

## Gradle构建的利与弊
个人喜欢Gradle；我讨厌XML，复杂的Java/Groovy项目，如果没有Gradle，几乎是寸步难行的。除了没有复杂的XML以外，Gradle还使用Groovy或Kotlin编写的构建脚本提供了灵活性和更快的构建速度。借助Kotlin或Groovy的全部功能以及Gradle API库，您可以创建功能强大且复杂的构建脚本。这肯定是提升效率的工具。

对于DSL（特定于域的语言）需要一些时间来适应，并且Gradle以难以学习而著称。但是，我认为这主要是因为人们已经习惯了Maven。使用Gradle，您实质上可以学习一种构建语言，而不只是简单地学习XML。与仅在Maven中添加依赖项相比，充分利用Gradle无疑具有更陡峭的学习曲线。但是向Gradle文件添加依赖项实际上并不比在Maven中困难。扩展和自定义Gradle构建比编写Maven插件和自定义构建步骤要简单得多。

Gradle还极大地缩短了构建时间，尤其是在大型项目中，因为Gradle仅处理已更改的任务和文件就可以很好地完成工作。此外，它提供了构建缓存和构建守护进程，使重复构建的性能更高。而且，像Maven一样，它使用并行线程进行依赖关系解析和项目构建。同样，对于小型，简单的构建，这种性能提升可能并不明显。但是对于较大的项目，这种性能提升是巨大的。

因此，总结一下。Gradle是：

* 大型项目更快
* 无限制可定制`==`更陡峭的学习曲线
* 使用Groovy或Kotlin代替XML


而Maven是：

* 普遍采用
* 对于较小项目更简单
* 带有XML和尖括号

## Groovy的优点

简要介绍一下Groovy。Groovy是一种JVM语言，它可以编译为与Java相同的字节码，并且可以与Java类无缝地互操作。Groovy是Java的向后兼容超集，这意味着Groovy可以透明地与Java库和代码交互。但是，它还增加了许多新功能：可选的键入，函数式编程，运行时灵活性以及许多元编程内容。它还极大地清理了Java中许多冗长的代码格式。Groovy尚未成为主流的开发语言，但是它已经在测试（由于其简化的语法和元编程功能）和构建系统中占据了一席之地。

## 依存关系
您需要为本教程安装一些内容：

Java：您可能已经安装了Java。本教程至少需要Java 1.8。如果不是，请转到官网下载并安装它。

Gradle：但是，由于本教程是有关Gradle的教程，因此在本教程中，您可以继续进行安装。

## 认识`build.gradle`

`build.gradle`文件是Gradle项目的核心，是构建配置必不可少的一项。就比如`pom.xml`对于Maven来说，这是等效的（没有所有令人讨厌的尖括号）

让我们来看一个。

```
// 配置运行构建脚本的要求
buildscript { 
    // 设置自定义属性
    ext {  
       springBootVersion = '2.1.6.RELEASE' 
    }  
    // 解决buildscript块中的依赖项时，检查Maven Central中的依赖项
    repositories {  
       mavenCentral()  
    }  
    // 我们需要spring boot插件来运行构建脚本
    dependencies {  
       classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")  
    }  
}  
   
// 添加构建插件
apply plugin: 'java' 
apply plugin: 'org.springframework.boot' 
apply plugin: 'io.spring.dependency-management' 
   
// 设置全局变量
group = 'com.okta.springboottokenauth' 
version = '0.0.1-SNAPSHOT' 
sourceCompatibility = 1.8 
   
// 用于搜索以解决项目依赖关系的仓库地址
repositories {  
    mavenCentral()  
}  
 
// 项目依赖
dependencies {  
    implementation( 'com.okta.spring:okta-spring-boot-starter:1.2.1' )  
    implementation('org.springframework.boot:spring-boot-starter-security')  
    implementation('org.springframework.boot:spring-boot-starter-web')  
    testImplementation('org.springframework.boot:spring-boot-starter-test')  
    testImplementation('org.springframework.security:spring-security-test')  
}
```

理解Gradle构建文件的关键是要意识到它是一个脚本，内置在Groovy DSL中。粗略地讲，它是一个配置脚本，它调用定义了配置选项的一系列闭包（考虑函数）。它看起来像JSON或propertiy文件，尽管从技术上来说这是错误的。

但是，真正的有趣的来自`build.gradle` Groovy脚本。因为它可以执行任意代码并访问任何Java库，特定于构建的Gradle DSL和Gradle API。

## Gradle`buildscript`

让我们从上至下查看脚本：

* `buildscript`闭包配置构建脚本本身（与应用程序相对）所需的属性，依赖项和源仓库。

* 接下来，`apply plugin`以非常好友的方式应用了插件。这些扩展了Gradle-Groovy DSL框架的基本功能：将该java插件与Spring Boot和Spring依赖项管理一起应用。Java插件提供配置Gradle的期望标准的Java项目的目录结构：`src/main/java`，`src/main/resources`，`src/test/java`等，这些可以被配置为改变默认的目录或添加新的目录。

* 接下来，将一些标准属性应用于构建。

* `repositories`块定义了构建脚本将在哪里寻找依赖关系。Maven Central是最常见的（`mavenCentral()`），但也可以配置其他仓库，包括自定义仓库和本地仓库。可以使用来将本地Maven缓存配置为仓库`mavenLocal()`。如果团队希望协调项目之间的构建，但又不想将项目构建文件实际捆绑在一起，这将很有帮助。

* 最后，定义项目依赖项。

其中每个模块定义闭包的顺序无关紧要，因为大多数`build.gradle`文件仅定义依赖项，设置项目属性并使用预定义的任务，因此文件中元素的顺序无关紧要。例如，没有理由`repositories`块必须走在该`dependencies`块之前。您可以将`build.gradle`文件视为Gradle在执行调用它的shell命令分配的任何任务之前读取的配置文件。

但是，当您开始使用Gradle的功能来定义自定义任务并执行任意代码时，它将变得更加复杂。Gradle将以`build.gradle`自上而下的方式读取文件，并执行在其中找到的所有代码块；根据此代码的作用，它可以在脚本中创建强制排序。此外，当您定义自定义任务和属性（在Gradle API中找不到）时，排序很重要，因为这些符号不会被预先定义，因此必须在构建脚本中定义它们才能使用它们。

## 什么是闭包

回到Groovy刚问世时，函数式编程是相当小众的领域，将诸如闭包之类的东西带入JVM感觉很疯狂。如今，它变得更加普遍：Javascript中的每个函数都是闭包。一般来说，闭包是具有范围的一流函数。

这意味着两件事：

* 闭包是可以在运行时作为变量传递的函数
* 闭包保留对定义它们的变量范围的访问


Java版本的闭包称为lambda。这些是在1.8版中引入Java的，顺便说一句，这并不是在Groovy获得最初的流行和函数式编程开始发展的同时发生的。

为了演示lambda，请看一下名为的JUnit测试`LambdaTest.java`。

`src/test/java/com/okta/springboottokenauth/LambdaTest.java`


```
interface SimpleLambda {  
    public int sum(int x, int y);  
}  
   
public class LambdaTest {  
   
    // 创建一个lambda函数 
    public SimpleLambda getTheLambda(int offset) {  
        int scopedVar = offset;  
        return (int x, int y) -> x + y + scopedVar;  
    }  
   
    @Test 
    public void testClosure() {  
        // 测试lambda方法，当offset=1
        SimpleLambda lambda1 = getTheLambda(1);  
        assertEquals(lambda1.sum(2,2), 5);  
   
        //  测试lambda方法，当offset=2
        SimpleLambda lambda2 = getTheLambda(2);  
        assertEquals(lambda2.sum(2,2), 6);  
    }
}
```

这个示例很有代表性，演示了lambda的两个基本属性。在闭包或lambda函数中，实现是在`getTheLambda(int offset)`方法中定义的。创建lambda时，将offset变量封装在闭包范围中并返回。该lambda被分配给变量。可以重复调用它，并且它将引用相同的作用域。此外，可以使用封装在单独作用域中并分配给其他变量的新变量来创建新的lambda。

来自强大的面向对象的背景，封闭最初感觉就像虫洞在严格的对象范围连续体上打穿透孔一样，奇怪地将对象的各个部分在空间和时间上连接在一起。

## Gradle只是闭包

采取`build.gradle`文件的依赖项部分：


```
dependencies {  
    implementation( 'com.okta.spring:okta-spring-boot-starter:1.2.1' )  
    implementation('org.springframework.boot:spring-boot-starter-security')  
    ...
}
```

没有Groovy DSL速记，实际上是：

```
project.dependencies({
    implementation( 'com.okta.spring:okta-spring-boot-starter:1.2.1' )  
    implementation('org.springframework.boot:spring-boot-starter-security')  
    ... 
})
```

括号中的所有内容实际上都是传递给该`project.dependencies()`方法的闭包。该`project`对象是`Project`该类的实例，该类是构建的主要API父类。

如您所见，这些函数将一系列依赖项作为字符串传递。那么，为什么不使用更传统的静态数据结构（如JSON，属性或XML）呢？原因是这些重载函数也可以使用闭包代码块，因此可以进行深度自定义。

## 探索Gradle依赖项配置

依赖关系块内部是一系列配置和名称。

```
dependencies {
    configurationName dependencyNotation
}
```

我们的`build.gradle`文件使用两种配置：`implementation`和`testImplementation`。

`implementation()`定义编译时所需的依赖项。此配置方法称为`compile`。`testImplementation()`并定义了仅用于测试（旧`testCompile`）所需的依赖项。

您可能会看到的另一个依赖项配置是`runtimeOnly`和`testRuntimeOnly`。这声明了运行时提供的不需要对其进行编译的依赖项。

定义依赖关系的方法比对本文的范围有用的方法更多。几乎可以说任何东西都可以是依赖项：本地文件，jar的目录，另一个Gradle项目等等，并且可以将依赖项配置为执行某些操作，例如排除某些子依赖项。

值得注意的是：Gradle和Maven以完全相同的方式解决依赖关系。例如，假设我们想从`Spring Boot Starter`中排除`Log4j`依赖关系，我们可以这样做：

```
dependencies {  
    implementation( 'com.okta.spring:okta-spring-boot-starter:1.2.1' ) {
        exclude group: 'org.apache.logging.log4j', module: 'log4j-api'
    }
}
```
或者说我们想将目录中的所有文件都包含`libs`为依赖项：

```
dependencies {  
    implementation fileTree('libs')
}
```
## 打包Gradle版本

关于Gradle的一件很棒的事情是Gradle包装器。Gradle命令行为`gradle`。但是，您会注意到在网上的许多地方，您都会看到`./gradlew`或`gradlew.bat`。这些是调用包装程序的命令。

包装器允许项目捆绑在项目本身内部构建项目所需的Gradle版本。这样可以确保对Gradle的更改不会中断构建。它还可以确保即使没有安装Gradle的人也可以运行构建。

它将以下文件添加到您的项目：

```
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
└── gradlew.bat
```

`gradlew`和`gradlew.bat`是用于Linux/OSX和Window（分别）执行脚本。他们运行`build.gradle`使用捆绑的摇篮文件`.jar`的`gradle/wrapper`子目录。

## 任务

任务是Gradle的核心。Java插件增加了十几个任务，包括：`clean`，`compile`，`test`，`jar`，和`uploadArchives`。Spring Boot插件添加了`bootRun`任务，该任务运行Spring Boot应用程序。

通常，任务是这样运行的：`gradle taskName otherTaskName`，或使用包装器：`./gradlew taskName otherTaskName`。

如果打开终端并cd进入示例项目的基本目录，则可以使用`gradle tasks`列出`build.gradle`文件定义的所有任务。`tasks`当然，它本身是由基本Gradle API定义的任务。


```
> Task :tasks

------------------------------------------------------------
Tasks runnable from root project
------------------------------------------------------------

Build tasks
-----------
assemble - Assembles the outputs of this project.
build - Assembles and tests this project.
buildDependents - Assembles and tests this project and all projects that depend on it.
buildNeeded - Assembles and tests this project and all projects it depends on.
classes - Assembles main classes.
clean - Deletes the build directory.
jar - Assembles a jar archive containing the main classes.
testClasses - Assembles test classes.

Build Setup tasks
-----------------
init - Initializes a new Gradle build.
wrapper - Generates Gradle wrapper files.

Distribution tasks
------------------
assembleDist - Assembles the main distributions
assembleMonitorDist - Assembles the monitor distributions
distTar - Bundles the project as a distribution.
distZip - Bundles the project as a distribution.
installDist - Installs the project as a distribution as-is.
installMonitorDist - Installs the project as a distribution as-is.
monitorDistTar - Bundles the project as a distribution.
monitorDistZip - Bundles the project as a distribution.

Documentation tasks
-------------------
groovydoc - Generates Groovydoc API documentation for the main source code.
javadoc - Generates Javadoc API documentation for the main source code.

Help tasks
----------
buildEnvironment - Displays all buildscript dependencies declared in root project 'fun'.
components - Displays the components produced by root project 'fun'. [incubating]
dependencies - Displays all dependencies declared in root project 'fun'.
dependencyInsight - Displays the insight into a specific dependency in root project 'fun'.
dependentComponents - Displays the dependent components of components in root project 'fun'. [incubating]
help - Displays a help message.
model - Displays the configuration model of root project 'fun'. [incubating]
projects - Displays the sub-projects of root project 'fun'.
properties - Displays the properties of root project 'fun'.
tasks - Displays the tasks runnable from root project 'fun'.

IDE tasks
---------
cleanIdea - Cleans IDEA project files (IML, IPR)
idea - Generates IDEA project files (IML, IPR, IWS)
openIdea - Opens the IDEA project

Verification tasks
------------------
check - Runs all checks.
test - Runs the unit tests.

Rules
-----
Pattern: clean<TaskName>: Cleans the output files of a task.
Pattern: build<ConfigurationName>: Assembles the artifacts of a configuration.
Pattern: upload<ConfigurationName>: Assembles and uploads the artifacts belonging to a configuration.

To see all tasks and more detail, run gradle tasks --all

To see more detail about a task, run gradle help --task <task>
```
我想指出`dependencies`任务。它将列出一棵树，其中包含项目所需的所有依赖关系（包括子依赖关系）。尝试`gradle dependencies`在项目根目录中运行。您可以使用该`dependencyInsight`任务来深入了解特定的子依赖项。

另一个有助于解决问题的`properties`任务是该任务，该任务列出了在根项目对象实例上定义的所有属性。

当然，在开发Spring Boot项目时，可以使用命令：`./gradlew bootJar`，该任务将项目及其依赖项打包在一个jar文件中。

到此，基础篇完事儿，提高篇中将会实践一下自定义任务和Groovy闭包在Gradle配置文件`build.gradle`文件中如何使用。


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