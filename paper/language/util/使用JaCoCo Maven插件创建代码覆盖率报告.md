# 使用JaCoCo Maven插件创建代码覆盖率报告

[原文地址](https://www.javacodegeeks.com/2013/08/creating-code-coverage-reports-for-unit-and-integration-tests-with-the-jacoco-maven-plugin.html)

这篇博客文章描述了我们如何使用JaCoCo Maven插件为单元和集成测试创建代码覆盖率报告。

我们的构建要求如下：

运行测试时，我们的构建必须为单元测试和集成测试创建代码覆盖率报告。
代码覆盖率报告必须在单独的目录中创建。换句话说，必须将用于单元测试的代码覆盖率报告创建到与用于集成测试的代码覆盖率报告不同的目录中。
让我们开始吧。

## 配置JaCoCo Maven插件

我们使用JaCoCo Maven插件有两个目的：

* 它使我们可以访问JaCoCo运行时代理，该代理记录了执行覆盖率数据。
* 它根据JaCo​​Co运行时代理记录的执行数据创建代码覆盖率报告。
* 我们可以按照以下步骤配置JaCoCo Maven插件：

将JaCoCo Maven插件添加到我们的POM文件的插件部分。
* 为单元测试配置代码覆盖率报告。
* 配置代码覆盖率报告以进行集成测试。
下面将更详细地描述这些步骤。

## 将JaCoCo Maven插件添加到POM文件

通过将以下插件声明添加到其“ 插件”部分，我们可以将JaCoCo Maven插件添加到我们的POM文件中：


```
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.6.3.201306030806</version>
</plugin>
```
## 配置单元测试的代码覆盖率报告
我们可以通过将两个执行添加到插件声明中来为单元测试配置代码覆盖率报告。这些执行方式如下所述：

* 第一次执行将创建一个指向JaCoCo运行时代理的属性。确保执行数据已写入文件target / coverage-reports / jacoco-ut.exec。将该属性的名称设置为surefireArgLine。运行单元测试时，此属性的值作为VM参数传递。
* 运行单元测试后，第二次执行将为单元测试创建代码覆盖率报告。确保从文件target / coverage-reports / jacoco-ut.exec中读取执行数据，并将代码覆盖率报告写入目录target / site / jacoco-ut中。

我们的插件配置的相关部分如下所示：


```
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.6.3.201306030806</version>
    <executions>
        <!--
           Prepares the property pointing to the JaCoCo runtime agent which
           is passed as VM argument when Maven the Surefire plugin is executed.
       -->
        <execution>
            <id>pre-unit-test</id>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
            <configuration>
                <!-- Sets the path to the file which contains the execution data. -->
                <destFile>${project.build.directory}/coverage-reports/jacoco-ut.exec</destFile>
                <!--
                   Sets the name of the property containing the settings
                   for JaCoCo runtime agent.
               -->
                <propertyName>surefireArgLine</propertyName>
            </configuration>
        </execution>
        <!--
           Ensures that the code coverage report for unit tests is created after
           unit tests have been run.
       -->
        <execution>
            <id>post-unit-test</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
            <configuration>
                <!-- Sets the path to the file which contains the execution data. -->
                <dataFile>${project.build.directory}/coverage-reports/jacoco-ut.exec</dataFile>
                <!-- Sets the output directory for the code coverage report. -->
                <outputDirectory>${project.reporting.outputDirectory}/jacoco-ut</outputDirectory>
            </configuration>
        </execution>
    </executions>
</plugin>
```
让我们找出如何为集成测试配置代码覆盖率报告。

## 配置集成测试的代码覆盖率报告
我们可以通过在插件声明中添加两个执行来为集成测试配置代码覆盖率报告。这些执行方式如下所述：

* 第一次执行将创建一个指向JaCoCo运行时代理的属性。确保将执行数据写入文件target / coverage-reports / jacoco-it.exec。将该属性的名称设置为failsafeArgLine。运行我们的集成测试时，此属性的值作为VM参数传递。
* 创建一个执行，该执行在集成测试运行后为集成测试创建代码覆盖率报告。确保从文件target / coverage-reports / jacoco-it.exec中读取执行数据，并将代码覆盖率报告写入目录target / site / jacoco-it。

我们的插件配置的相关部分如下所示：

```
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.6.3.201306030806</version>
    <executions>
        <!-- The Executions required by unit tests are omitted. -->
        <!--
           Prepares the property pointing to the JaCoCo runtime agent which
           is passed as VM argument when Maven the Failsafe plugin is executed.
       -->
        <execution>
            <id>pre-integration-test</id>
            <phase>pre-integration-test</phase>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
            <configuration>
                <!-- Sets the path to the file which contains the execution data. -->
                <destFile>${project.build.directory}/coverage-reports/jacoco-it.exec</destFile>
                <!--
                   Sets the name of the property containing the settings
                   for JaCoCo runtime agent.
               -->
                <propertyName>failsafeArgLine</propertyName>
            </configuration>
        </execution>
        <!--
           Ensures that the code coverage report for integration tests after
           integration tests have been run.
       -->
        <execution>
            <id>post-integration-test</id>
            <phase>post-integration-test</phase>
            <goals>
                <goal>report</goal>
            </goals>
            <configuration>
                <!-- Sets the path to the file which contains the execution data. -->
                <dataFile>${project.build.directory}/coverage-reports/jacoco-it.exec</dataFile>
                <!-- Sets the output directory for the code coverage report. -->
                <outputDirectory>${project.reporting.outputDirectory}/jacoco-it</outputDirectory>
            </configuration>
        </execution>
    </executions>
</plugin>
```
现在，我们已经配置了JaCoCo Maven插件。下一步是配置Maven Surefire插件。让我们找出如何做到这一点。

## 配置Maven Surefire插件

我们使用Maven Surefire插件运行示例应用程序的单元测试。因为我们要为单元测试创​​建代码覆盖率报告，所以我们必须确保在运行单元测试时JaCoCo代理正在运行。我们可以通过添加的价值保证本surefireArgLine财产作为价值argLine配置参数。

Maven Surefire插件的配置如下所示（突出显示了所需的更改）：


```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>2.15</version>
    <configuration>
        <!-- Sets the VM argument line used when unit tests are run. -->
        <argLine>${surefireArgLine}</argLine>
        <!-- Skips unit tests if the value of skip.unit.tests property is true -->
        <skipTests>${skip.unit.tests}</skipTests>
        <!-- Excludes integration tests when unit tests are run. -->
        <excludes>
            <exclude>**/IT*.java</exclude>
        </excludes>
    </configuration>
</plugin>
```

我们快完成了。剩下要做的就是配置Maven Failsafe插件。让我们找出如何做到这一点。

## 配置Maven故障安全插件
我们的示例应用程序的集成测试由Maven Failsafe插件运行。因为我们要为集成测试创建代码覆盖率报告，所以我们必须确保在运行集成测试时JaCoCo代理正在运行。我们可以通过将failsafeArgLine属性的值添加为argLine配置参数的值来实现。

Maven Failsafe插件的配置如下所示（突出显示了所需的更改）：

```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>2.15</version>
    <executions>
        <!--
            Ensures that both integration-test and verify goals of the Failsafe Maven
            plugin are executed.
        -->
        <execution>
            <id>integration-tests</id>
            <goals>
                <goal>integration-test</goal>
                <goal>verify</goal>
            </goals>
            <configuration>
                <!-- Sets the VM argument line used when integration tests are run. -->
                <argLine>${failsafeArgLine}</argLine>
                <!--
                    Skips integration tests if the value of skip.integration.tests property
                    is true
                -->
                <skipTests>${skip.integration.tests}</skipTests>
            </configuration>
        </execution>
    </executions>
</plugin>
```
## 创建代码覆盖率报告
现在，我们已成功完成所需的配置。让我们看看如何为单元测试和集成测试创建代码覆盖率报告。

此博客文章的示例应用程序具有三个构建配置文件，下面对此进行了描述：

* 在开发配置文件开发过程中使用，这是我们构建的默认配置文件。当此配置文件处于活动状态时，仅运行单元测试。
* 在集成测试配置文件用于运行集成测试。
* 在所有的测试配置文件用于为运行单元测试和集成测试。
我们可以通过在命令提示符处运行以下命令来创建不同的代码覆盖率报告：

* 命令mvn clean test运行单元测试，并为目录target / site / jacoco-ut创建单元测试的代码覆盖率报告。
* 命令mvn clean verify -P integration-test运行集成测试，并为目录target / site / jacoco-it创建用于集成测试的代码覆盖率报告。
* 命令mvn clean verify -P all-tests运行单元测试和集成测试，并为单元测试和集成测试创建代码覆盖率报告。

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

![长按关注](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)