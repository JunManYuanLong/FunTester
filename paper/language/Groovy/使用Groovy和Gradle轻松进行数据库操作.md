# 使用Groovy和Gradle轻松进行数据库操作



## Groovy：“时髦”语言

并非所有人都认为Java 编程语言很性感。但是，从最保守的企业到最古怪的初创企业，Java 虚拟机都是无处不在的主导力量。如今，有许多可替代的语言可编译为Java字节码。有基于JVM的Python，Ruby版本和JavaScript的多种实现。有全新的语言，例如  JetBrains的Kotlin和RedHat的Ceylon。Clojure最近重新唤起了对Lisp和Scala的兴趣很大程度上是2000年服务器端向功能编程的转变的原因。

Groovy是所有人的祖父，今天几乎无处不在。当它在13年前首次出现时，Groovy立刻受到欢迎。该语言和相关的Grails Web框架将Ruby on Rails的新兴流行与Java开发人员的极浅学习曲线结合在一起。在几乎一夜之间，Groovy完全取代了以前的JVM脚本替代品BeanShell。

对Rails模型的热情最终减弱了，强类型的语言再次成为趋势。坦率地说，许多仅仅因为它是“新的”而蜂拥至Groovy的人仍在继续开发新事物。但是，Groovy并没有消失。相反，它已经成为“企业时髦”语言的成熟角色。随处可见。JVM上几乎所有公开脚本接口的应用程序都以Groovy为头等公民而这样做。Groovy是与QA非常流行的自动化测试空间，被深深植入到Spring框架，并且是快速增长的基础摇篮构建系统。

我们没有像以前那样大肆宣传Groovy，但是它在Java生态系统中已经根深蒂固，并且还在不断扩展。这是一个稳定，安全的选择，为此，很容易找到人才（或快速在职培训）。尽管今天有更多时髦的流行语要放在您的简历上，但是Groovy很快就消失out尽的风险似乎很小。Groovy“行之有效”，是每个Java开发人员都应该在其工具箱中使用的非常方便的工具。

## Gradle作为Groovy App Server

除了历史，让我们谈论一个最近的用例，它使我无法使用Groovy技能。我需要为在多种环境中运行的许多应用程序快速建立一个“键值”配置参数注册表。我想在源代码管理中将这些参数捕获为属性文件的集合。每个应用程序一个文件，嵌套在每个环境的子目录中：


```
…
    qa-env/
        application-a.properties
        application-b.properties
…
    staging-env/
        application-a.properties
        application-b.properties
…
```

每当在源代码管理中提交对这些属性文件的更改时，我都希望Jenkins（或其他连续集成服务器）将其值与运行时“注册表”同步。该注册表最终可能会变成etcd或Consul和Vault之类的东西，但是我们可以使用传统的MySQL数据库快速开始工作。

由于这些天我们的大多数持续集成构建作业都是基于Gradle的，并且由于Gradle是Groovy本机的，因此我们可以将这种“同步”作业烘焙到Gradle构建中。通过基于JavaExec的任务（指向Groovy脚本），您可以将Gradle用作Groovy应用服务器！

* build.gradle

```
apply plugin: 'groovy'
 
repositories {
    mavenCentral()
    mavenLocal()
}
 
// [1] Declare a localGroovy() dependency, to use 
//     the Groovy library that ships with Gradle.
dependencies { 
    compile localGroovy() 
    compile("mysql:mysql-connector-java:5.1.35") 
    compile("com.h2database:h2:1.4.187") 
    testCompile("junit:junit:4.12") 
} 
 
// [2] Create a task of type 'JavaExec', referencing 
//     a Groovy script and any input arguments. 
task runScript(type: JavaExec) { 
    description 'Run a Groovy script to sync the environment config registry with the properties files in source control'
    classpath = sourceSets.main.runtimeClasspath 
    main 'com.mypackage.SyncScript'
    args Arrays.asList('jdbc:mysql://registry/db', 'com.mysql.jdbc.Driver', 'user', 'password').toArray() 
} 
 
// [3] Tell Gradle to invoke your Groovy script task. 
defaultTasks 'runScript'
```

编写执行某些任意Groovy代码的Gradle构建脚本相当简单。由于如今运行Gradle的首选方法是通过精简包装器脚本，因此无需安装Gradle，就可以直接从源代码控制存储库将此解决方案传递到任何地方。

换句话说，只要提交了源代码控制存储库，就可以使  Jenkins运行Groovy脚本。

## Groovy SQL
现在，对于真正整洁的部分，Groovy“同步”脚本本身。该脚本扫描任意数量的每个环境目录，扫描每个目录中的任意数量的每个应用程序属性文件，并将这些属性与MySQL数据库表同步。


```
// Iterate through each per-environment directory
new File('config').eachDir { File environmentDirectory ->
 
    // Iterate through each per-application properties file
    environmentDirectory.eachFileMatch FileType.FILES, ~/.+\.properties/, { File applicationFile ->
 
        def environment = environmentDirectory.name
        def application = applicationFile.name.replace('.properties', '')
        println "Processing properties for env: '$environment', application: '$application'"
 
        // Parse the file into a java.util.Properties object
        def properties = new Properties()
        applicationFile.withInputStream { stream -> properties.load(stream) }
 
        ...
         
    }
}
```

Java 8 Streams使这种事情在纯Java领域变得更加友好和易读，但是它仍然无法触及Groovy对 File等类的扩展的简单性  。该eachDir（）和eachFileMatch（）附加的方法可以很容易地通过迭代所有的目录，并扫描具有扩展名“properties“文件的。所述withInputStream（）方法可以帮助我们加载每个文件的内容到一个java.util.Properties与单行对象。

除了对java.io.File的扩展之外，Groovy还提供了自己的groovy.sql.Sql类来促进JDBC操作。这减少了构造数据库查询所需的许多样板，并允许我们在闭包内处理其ResultSet：


```
database = groovy.sql.Sql.newInstance(jdbcUrl, jdbcUsername, jdbcPassword, jdbcDriver)
database.resultSetConcurrency = ResultSet.CONCUR_UPDATABLE
 
// Iterate through the properties, and sync MySQL
properties.entrySet().each {
    def name = it.key
    def value = it.value
    def existingRecordQuery = '''
SELECT environment, service, property_name, property_value FROM environment_properties
WHERE environment = ? AND service = ? AND property_name = ?
'''
    database.query(existingRecordQuery, [environment, service, name]) { ResultSet rs ->
        if (rs.next()) {
            def existingValue = rs.getString('property_value')
            if (existingValue.equals(value)) {
                // Existing property value is unchanged.  No-op.
            } else {
                // Existing property value has changed.  Update.
                rs.updateString('property_value', value)
                rs.updateRow()
            }
        } else {
            // New property.  Insert.
            rs.moveToInsertRow()
            rs.updateString('environment', environment)
            rs.updateString('service', service)
            rs.updateString('property_name', name)
            rs.updateString('property_value', value)
            rs.insertRow()
        }
    }
}
 
// TODO: Remove from the database properties that have 
//       been removed from the properties file.
```
这里发生了一些有趣的事情：

* 在第2行，我们将并发设置更改为ResultSet.CONCUR_UPDATABLE。许多Java开发人员都不知道Java甚至支持它！
* 此设置使您可以更新，插入或删除ResultSet对象中的行，而不必构造其他JDBC语句。请参阅第20和29行上发生的示例。ORM的许多便利之处在于原始JDBC的简单性！
* 正如您在第8-11行看到的那样，Groovy允许带有三引号的多行字符串文字。这使得在源代码中包含较长的SQL字符串更具可读性。
* 在第12行，我们看到groovy.sql.Sql允许您执行语句并在闭包内处理其结果。一种便利是底层的JDBC语句将在最后自动关闭。

## 结论

这个特定的用例非常具体，但是它展示了多个概念，这些概念在隔离中广泛有用。Groovy是一种非常强大的语言，在没有其他替代方法的环境中可能会受到欢迎。它是Gradle的本机，后者已迅速成为Java生态系统中最主要的构建工具，因此Groovy易于通过您的持续集成服务器加以利用。最后，Groovy提供了完整的类库以及对核心Java类的扩展，这些真正地消除了许多常见任务的样板和复杂性。

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