# Java 8，Jenkins，Jacoco和Sonar进行持续集成

## 技术环境

在以安全与质量为主要驱动力的项目中，CI至关重要。

因此，我从我的团队开始进行“概念验证”，以表明以下技术已准备好协同工作：
* Java 8, NetBeans 8.0 & Ant
* JUnit 4 & Jacoco 0.7.1
* Jenkins & Sonar 4.2

本文的范围是解释安装和设置必要工具的所有步骤，以使Java 8的CI服务器完全正常运行。请注意，该证明已在Windows 7的开发人员机器上完成，但很容易做到。在Linux服务器中也是如此。

下图高层次显示了将在帖子中描述的体系结构。

![](http://q0zm42rte.bkt.clouddn.com/kjdslfjs.png)

### Java 8 & NetBeans 8.0 & Ant
我们正在创建模块化应用程序。该应用程序具有多层体系结构，其中每个层都是模块套件，而最终的可执行文件只是一组集成套件。

我们正在使用Ant  来构建我们的项目，但是如果您使用的是Maven，则甚至可以简化该过程，因为Jenkins中的Sonar集成可以通过使用Maven的插件来完成。

### JUnit 4 & Jacoco 0.7.1
自然，我们正在进行单元测试，因此，我们使用JUnit4。它在任何地方都可以很好地集成，尤其是在NetBeans中。

Jacoco  是生成代码覆盖率的绝佳工具，并且自0.7.1版起，它完全支持Java 8。

### Jenkins & Sonar 4.2

Jenkins是我们CI服务器的引擎，它将与上述所有技术集成在一起，没有任何问题。测试的版本是1.554。

声纳正在对代码进行所有质量分析。4.2版与Java 8完全兼容。

将Sonar与Ant一起使用需要一个小型库，其中包含要集成到Jenkins中的目标。如果您使用的是Maven，则可以只安装Maven插件。

## 项目配置

### 1、安装Java 8
### 2、创建一个包含几个模块，几个类和几个jUnit测试的模块套件
### 3、将代码提交到您的源代码版本管理服务器中
### 4、在名为“ jacoco-0.7.1”的线束中创建一个文件夹，其中包含下载的jacoco jars
### 5、在名为“ sonar-ant-task”的线束中创建一个文件夹，并将其放入下载的sonar文件夹
### 6、在名为sonar-jacoco-module.xml的工具中创建一个文件，并将以下代码粘贴到其中：


```
<?xml version="1.0" encoding="UTF-8"?>
<!--
 
-->
<project name="sonar-jacoco-module" basedir="." xmlns:jacoco="antlib:org.jacoco.ant" xmlns:sonar="antlib:org.sonar.ant">
    <description>Builds the module suite otherSuite.</description>

    <property name="jacoco.dir" location="${nbplatform.default.harness.dir}/jacoco-0.7.1"/>
    <property name="result.exec.file" location="${jacoco.dir}/jacoco.exec"/>
    <property name="build.test.results.dir" location="build/test/unit/results"/>

    <property file="nbproject/project.properties"/>

    <!-- Step 1: Import JaCoCo Ant tasks -->
    <taskdef uri="antlib:org.jacoco.ant" resource="org/jacoco/ant/antlib.xml">
        <classpath path="${jacoco.dir}/jacocoant.jar"/>
    </taskdef>

    <!-- Target at the level of modules -->
    <target name="-do-junit" depends="test-init">
        <echo message="Doing testing for jacoco"/>
        <macrodef name="junit-impl">
            <attribute name="test.type"/>
            <attribute name="disable.apple.ui" default="false"/>
            <sequential>
                <jacoco:coverage destfile="${build.test.results.dir}/${code.name.base}_jacoco.exec">
                    <junit showoutput="true" fork="true" failureproperty="tests.failed" errorproperty="tests.failed"
                           filtertrace="${test.filter.trace}" tempdir="${build.test.@{test.type}.results.dir}"
                           timeout="${test.timeout}">
                        <batchtest todir="${build.test.@{test.type}.results.dir}">
                            <fileset dir="${build.test.@{test.type}.classes.dir}" includes="${test.includes}"
                                     excludes="${test.excludes}"/>
                        </batchtest>
                        <classpath refid="test.@{test.type}.run.cp"/>
                        <syspropertyset refid="test.@{test.type}.properties"/>
                        <jvmarg value="${test.bootclasspath.prepend.args}"/>
                        <jvmarg line="${test.run.args}"/>
                        <!--needed to have tests NOT to steal focus when running, works in latest apple jdk update only.-->
                        <sysproperty key="apple.awt.UIElement" value="@{disable.apple.ui}"/>
                        <formatter type="brief" usefile="false"/>
                        <formatter type="xml"/>
                    </junit>
                </jacoco:coverage>
                <copy file="${build.test.results.dir}/${code.name.base}_jacoco.exec"
                      todir="${suite.dir}/build/coverage"/>
                <!--
                Copy the result of all the unit tests of all the modules into one common
                folder at the level of the suite, so that sonar could find those files to
                generate associated reports
                -->
                <copy todir="${suite.dir}/build/test-results">
                    <fileset dir="${build.test.results.dir}">
                        <include name="**/TEST*.xml"/>
                    </fileset>
                </copy>
                <fail if="tests.failed" unless="continue.after.failing.tests">Some tests failed; see details above.
                </fail>
            </sequential>
        </macrodef>
        <junit-impl test.type="${run.test.type}" disable.apple.ui="${disable.apple.ui}"/>
    </target>

</project>
```

该文件的范围是覆盖添加jacoco覆盖范围的do-junit任务，并复制套件构建中每个模块的单元测试结果，以便声纳将找到所有这些元素一起进行分析。

### 7、在名为sonar-jacoco-suite.xml的线束中创建一个文件，并将以下代码粘贴到其中

```
<?xml version="1.0" encoding="UTF-8"?>
<project name="sonar-jacoco-suite" basedir="." xmlns:jacoco="antlib:org.jacoco.ant" xmlns:sonar="antlib:org.sonar.ant">
    <description>Builds the module suite otherSuite.</description>

    <property name="jacoco.dir" location="${nbplatform.default.harness.dir}/jacoco-0.7.1"/>
    <property name="result.exec.file" location="build/coverage"/>

    <!-- Define the SonarQube global properties (the most usual way is to pass these properties via the command line) -->
    <property name="sonar.jdbc.url" value="jdbc:mysql://localhost:3306/sonar?useUnicode=true&characterEncoding=utf8" />
    <property name="sonar.jdbc.username" value="sonar" />
    <property name="sonar.jdbc.password" value="sonar" />
    <!-- Define the SonarQube project properties -->
    <property name="sonar.projectKey" value="org.codehaus.sonar:example-java-ant" />
    <property name="sonar.projectName" value="Simple Java Project analyzed with the SonarQube Ant Task" />
    <property name="sonar.projectVersion" value="1.0" />
    <property name="sonar.language" value="java" />
    <!-- Load the project properties file for retrieving the modules of the suite -->
    <property file="nbproject/project.properties"/>

    <!-- Using Javascript functions to build the paths of the data source for sonar configuration -->
    <script language="javascript">
        <![CDATA[

// getting the value
modulesName = project.getProperty("modules");
modulesName = modulesName.replace(":",",");
res = modulesName.split(",");
srcModules = "";
binariesModules = "";
testModules = "";
//Build the paths
for (var i=0; i<res.length; i++)
{
srcModules += res[i]+"/src,";
binariesModules += res[i]+"/build/classes,";
testModules += res[i]+"/test,";
}
//Remove the last comma
srcModules = srcModules.substring(0, srcModules.length - 1);
binariesModules = binariesModules.substring(0, binariesModules.length - 1);
testModules = testModules.substring(0, testModules.length - 1);
// store the result in a new properties
project.setProperty("srcModulesPath",srcModules);
project.setProperty("binariesModulesPath",binariesModules);
project.setProperty("testModulesPath",testModules);
]]>
    </script>
    <!-- Display the values -->
    <property name="sonar.sources" value="${srcModulesPath}"/>
    <property name="sonar.binaries" value="${binariesModulesPath}" />
    <property name="sonar.tests" value="${testModulesPath}" />
    <!-- Define where the coverage reports are located -->
    <!-- Tells SonarQube to reuse existing reports for unit tests execution and coverage reports -->
    <property name="sonar.dynamicAnalysis" value="reuseReports" />
    <!-- Tells SonarQube where the unit tests execution reports are -->
    <property name="sonar.junit.reportsPath" value="build/test-results" />
    <!-- Tells SonarQube that the code coverage tool by unit tests is JaCoCo -->
    <property name="sonar.java.coveragePlugin" value="jacoco" />
    <!-- Tells SonarQube where the unit tests code coverage report is -->
    <property name="sonar.jacoco.reportPath" value="${result.exec.file}/merged.exec" />
    <!--  Step 1: Import JaCoCo Ant tasks  -->
    <taskdef uri="antlib:org.jacoco.ant" resource="org/jacoco/ant/antlib.xml">
        <classpath path="${jacoco.dir}/jacocoant.jar"/>
    </taskdef>
    <target name="merge-coverage">
        <jacoco:merge destfile="${result.exec.file}/merged.exec">
            <fileset dir="${result.exec.file}" includes="*.exec"/>
        </jacoco:merge>
    </target>

    <target name="sonar">
        <taskdef uri="antlib:org.sonar.ant" resource="org/sonar/ant/antlib.xml">
            <!-- Update the following line, or put the "sonar-ant-task-*.jar" file in your "$HOME/.ant/lib" folder -->
            <classpath path="${harness.dir}/sonar-ant-task-2.1/sonar-ant-task-2.1.jar" />
        </taskdef>

        <!-- Execute the SonarQube analysis -->
        <sonar:sonar />
    </target>

</project>
```

该文件的范围是在套件级别定义声纳配置和声纳任务。如果您使用声纳，则某些特殊的数据库或特殊的用户必须在此处更改配置。

定义的另一项任务是jacoco合并，该合并实际上将获取每个模块的所有生成的exec，并将它们合并到套件构建中的单个exec中，以允许声纳进行分析。

### 8、用以下内容替换每个模块的build.xml的内容：

```
<description>Builds, tests, and runs the project com.infrabel.jacoco.</description>
<property file="nbproject/suite.properties"/>
<property file="${suite.dir}/nbproject/private/platform-private.properties"/>
<property file="${user.properties.file}"/>
<import file="${nbplatform.default.harness.dir}/sonar-jacoco-module.xml"/>
<import file="nbproject/build-impl.xml"/>
```

### 9、用以下内容替换每个套件的build.xml的内容：


```
<description>Builds the module suite otherSuite.</description>
<property file="nbproject/private/platform-private.properties"/>
<property file="${user.properties.file}"/>
<import file="${nbplatform.default.harness.dir}/sonar-jacoco-suite.xml"/>
<import file="nbproject/build-impl.xml"/>
```
## Jenkins

### 在“管理Jenkins->管理插件”中，进入可用列表并安装（如果尚未存在）以下插件：

* JaCoCo
* Mercurial or Subversion
* Sonar

> 如果您在防火墙或代理后面，并且在配置网络设置时遇到问题，可以随时从此处手动下载并安装它们。在这种情况下，请记住还要先下载每个插件的依赖项。

### 在“管理Jenkins->配置系统”中，检查是否正确安装了所有插件，请参见以下屏幕截图以获取示例（将文件夹替换为适合您的文件夹）：

![](http://q0zm42rte.bkt.clouddn.com/jenkins-jdk1.png)
![](http://q0zm42rte.bkt.clouddn.com/jenkins-jdk.png)
![](http://q0zm42rte.bkt.clouddn.com/jenkins-sonar-1.png)
![](http://q0zm42rte.bkt.clouddn.com/jenkins-sonar-2.png)



### 创建一个新的自由样式项目，配置您的首选项的版本控制，然后在“构建”面板中添加以下三个“ Invoce Ant”任务：
![](http://q0zm42rte.bkt.clouddn.com/jenkins-configure.png)

### 在“构建后操作”面板中添加新的“记录Jacoco覆盖率报告”，其配置如下：
![](http://q0zm42rte.bkt.clouddn.com/jenkins-jacoco.png)


## Sonar

按照此脚本创建数据库，并选择运行此查询以使连接正常工作：
`GRANT ALL PRIVILEGES ON 'sonar'.* TO 'sonar'@'localhost';`

进入声纳的配置文件（sonar.properties）并启用MySQL，该文件位于安装的conf文件夹中：


```
sonar.jdbc.username=sonar
sonar.jdbc.password=sonar
sonar.jdbc.url=jdbc:mysql://localhost:3306/sonar?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true
```

> 在声纳的配置中，如果需要与Java 8兼容，请更新Java插件

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

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
![](https://mmbiz.qpic.cn/mmbiz_png/BuV4gXrNvFrQnPz6hPuyeNCH9BXB4Ufc0lbWyTGjcWrpSwFJOWqFtL0jIYWeqa093ibQcZCu7UMpSVZsFwKbicHQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)