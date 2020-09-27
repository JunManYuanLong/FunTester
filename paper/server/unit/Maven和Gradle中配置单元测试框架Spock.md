# Maven和Gradle中配置单元测试框架Spock

[原文链接](https://www.javacodegeeks.com/2015/03/spock-1-0-with-groovy-2-4-configuration-comparison-in-maven-and-gradle.html)

## Maven

Maven本身不支持其他JVM语言（例如Groovy或Scala）。要在Maven项目中使用它，需要使用第三方插件。对于Groovy而言，最好的选择似乎是GMavenPlus（重写不再维护的GMaven插件）。另一种选择是允许使用Groovy-Eclipse编译器和Maven 的插件，但是它没有使用官方的插件，groovyc并且在过去，使用Groovy 的新发行版/功能存在一些问题。

GMavenPlus插件的示例配置如下所示：


```
<plugin>
    <groupId>org.codehaus.gmavenplus</groupId>
    <artifactId>gmavenplus-plugin</artifactId>
    <version>1.4</version>
    <executions>
        <execution>
            <goals>
                <goal>compile</goal>
                <goal>testCompile</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

由于我们要用Spock编写测试，因此建议使用Spec附加后缀（从规范中命名）来命名文件，因此需要告诉Surefire 在这些文件中也查找测试：


```
<plugin>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>${surefire.version}</version>
    <configuration>
        <includes>
            <include>**/*Spec.java</include> <!-- Yes, .java extension -->
            <include>**/*Test.java</include> <!-- Just in case of having also "normal" JUnit tests -->
        </includes>
    </configuration>
</plugin>
```

值得留意的是，我们需要包括`**/*Spec.java不**/*Spec.groovy`让它工作。

最后必须添加依赖项：


```
<dependencies>
        <dependency>
            <groupId>org.codehaus.groovy</groupId>
            <artifactId>groovy-all</artifactId>
            <version>2.4.1</version>
        </dependency>
        <dependency>
            <groupId>org.spockframework</groupId>
            <artifactId>spock-core</artifactId>
            <version>1.0-groovy-2.4</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

正确使用Spock版本非常重要。对于Groovy 2.4，需要版本1.0-groovy-2.4。对于Groovy 2.3版本1.0-groovy-2.3。如果发生错误，Spock会发出清晰的错误消息以示抗议：


```
Could not instantiate global transform class
org.spockframework.compiler.SpockTransform specified at
jar:file:/home/foo/.../spock-core-1.0-groovy-2.3.jar!/META-INF/services/org.codehaus.groovy.transform.ASTTransformation
because of exception
org.spockframework.util.IncompatibleGroovyVersionException:
The Spock compiler plugin cannot execute because Spock 1.0.0-groovy-2.3 is
not compatible with Groovy 2.4.0. For more information, see
 
http://versioninfo.spockframework.org
```
连同其他必需的pom.xml元素，文件大小增加到了50行以上的XML。仅对于Groovy和Spock而言。让我们看看Gradle中有多么复杂。

## Gradle

Gradle具有对Groovy和Scala的内置支持。事不宜迟，Groovy插件只需要应用即可。

`apply plugin: 'groovy'`

接下来必须添加依赖项：


```
compile 'org.codehaus.groovy:groovy-all:2.4.1'
testCompile 'org.spockframework:spock-core:1.0-groovy-2.4'
```

以及Gradle在哪里寻找他们的信息：


```
repositories {
    mavenCentral()
}
```

连同定义包组和版本一起，在基于Groovy的DSL中花费了15行代码。

* 顺便说一句，在Gradle的情况下，匹配Spock和Groovy版本也很重要，例如Groovy 2.4.1和Spock 1.0-groovy-2.4。

## 我现在的配置

> 我用的Gradle

```
    testCompile group: 'org.spockframework', name: 'spock-core', version: '1.3-groovy-2.5'
    testCompile group: 'org.spockframework', name: 'spock-spring', version: '1.3-groovy-2.5'
    testCompile group: 'org.springframework', name: 'spring-test', version: '5.1.9.RELEASE'
```

## 总结

得益于对Groovy的嵌入式支持和紧凑的DSL Gradle，它是开始使用Spock（通常是Groovy）的首选解决方案。但是，如果您在GMavenPlus（和XML）的帮助下更喜欢Apache Maven，也可以构建使用Spock测试的项目。

![Maven & Gradle](/Users/fv/Documents/fan/blogPic/spock-groovy-maven-gradle.png)


## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [JUnit中用于Selenium测试的中实践](https://mp.weixin.qq.com/s/KG4sltQMCfH2MGXkRdtnwA)

## 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [自动化和手动测试，保持平衡！](https://mp.weixin.qq.com/s/mMr_4C98W_FOkks2i2TiCg)
- [自动化测试生命周期](https://mp.weixin.qq.com/s/SH-vb2RagYQ3sfCY8QM5ew)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)

## 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
- [JVM的对象和堆](https://mp.weixin.qq.com/s/iNDpTz3gBK3By_bvUnrWOA)

![长按关注](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)