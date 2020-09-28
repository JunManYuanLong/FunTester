# Gradle+Groovy提高篇



## 创建自定义任务
打开`build.gradle`文件，并在末尾添加以下内容：

```
println "1" 
   
task howdy {  
    println "2" 
    doLast {  
        println "Howdy" 
    }  
}  
   
println "3"
```
这将演示有关Gradle脚本如何工作的一些信息。使用以下命令运行它：

`./gradlew howdy`

您将看到（省略了一些多余的行）：


```
> Configure project :
1
2
3
 
> Task :howdy
Howdy
```

在这里，`Configure project`任务将生成并运行生成脚本。在Gradle执行`Configure project`任务时，它会执行以下操作：

它打第一个`println`并打印“1”
它找到要执行的`howdy`任务定义块，一个闭包，并显示“2”。请注意，它不会执行`doLast`关闭操作，因此尚未打印“Howdy”。
它继续执行脚本，直到第四个`println`，然后打印“3”。
至此，构建脚本本身已完成对构建环境的配置。下一步是执行命令行中指定的所有任务，在本例中为`howdy`任务。

这是`task.doLast{}`执行块的地方，因此您会在输出中看到“Howdy”字样。

`doLast`是该块的别称；它的真正含义是类似于“任务操作”，而外部块是任务配置。


```
task howdy {  
    // 始终在初始构建脚本配置期间执行
    doLast {  
        // 仅在任务本身被调用时执行 
    }
    // 始终在初始构建脚本配置期间执行
} 
```

使用Graovy DSL根据Gradle文档定义任务的各种方法如下：

```
task taskName
task taskName { configure closure }
task taskName(type: SomeType)
task taskName(type: SomeType) { configure closure }
```


只是为了锤炼，在运行构建脚本时立即执行“配置闭包”，而在`doLast`专门执行任务时执行在配置闭包中定义的闭包。

将第二个自定义任务添加到`build.gradle`文件：


```
task partner {  
    println "4" 
    doLast {  
        println "Partner" 
    }  
}  
println "5"
```

如果您`./gradlew partner`看到的是：

```
> Configure project :
1
2
3
4
5
 
> Task :partner
Partner
```
如果您希望一个自定义任务依赖另一个任务怎么办？这简单。`build.gradle` 在定义两个自定义任务之后，将以下行添加到文件中的某处。

`partner.dependsOn howdy`

并运行：`./gradlew partner`


```
...
> Task :howdy
Howdy
 
> Task :partner
Partner
```

您也可以使用task属性表示类似的关系finalizedBy。如果将dependsOn行替换为：

`howdy.finalizedBy partner`

并运行：`/gradlew howdy`。


```
...
> Task :howdy
Howdy
 
> Task :partner
Partner
```

您得到相同的输出。当然，他们表达不同的关系。

关于任务的最后一点：在实践中，您很少编写自定义任务来说诸如“Howdy Partner”之类的东西（我很难相信，我知道）。实际上，通常您会覆盖已经定义的任务类型。例如，Gradle定义了Copy一个将文件从一个位置复制到另一个位置的任务。

这是一个将文档复制到构建目标的示例：


```
task copyDocs(type: Copy) {
    from 'src/main/doc'
    into 'build/target/doc'
}
```
当您意识到`build.gradle`文件实际上是一个Groovy脚本时，就可以使用Groovy和Gradle的真正功能，如果需要，您可以执行任意代码来过滤和转换这些文件。

下面的任务转换每个副本文件并排除`.DS_Store`文件。DSL非常灵活。您可以使用`from`和多个块`excludes`，也可以执行诸如重命名文件或专门包含文件之类的操作。再次查看“复制”任务的文档以获取更完整的想法。


```
task copyDocs(type: Copy) {
    from 'src/main/doc'
    into 'build/target/doc'
    eachFile { file ->
        doSomething(file);
    }
    exclude '**/.DS_Store'
}
```

我在Gradle Jar或中最重视War的任务是负责打包`.jar`和`.war`文件以进行最终分发的任务。像`Copy`任务一样，他们具有定制过程的非常开放的能力，这对于需要定制最终产品的项目可能是巨大的帮助。实际上，您可以使用Gradle DSL来完全控制打包过程的各个方面。

Spring Boot插件的`bootJar`和`bootWar`任务继承自`Jar`和`War`任务，因此它们包括所有配置选项，包括复制，过滤和修改文件的能力以及自定义清单的能力。

提高篇到此结束，大家要是有兴趣欢迎去Gradle官网查看API文档，非常有帮助。

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