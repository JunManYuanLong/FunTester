# Gradle如何在任务失败后继续构建



如果我们运行Gradle构建并且其中一项任务失败，则整个构建将立即停止。因此，我们可以快速反馈构建状态。如果我们不想这样做，并且希望Gradle执行所有任务，即使某些任务可能失败了，我们也可以使用命令行选项`--continue`。当我们使用`--continue`命令行选项时，Gradle将执行从属任务没有失败的所有任务。这在多模块项目中也很有用，即使在某些项目中测试可能失败，我们也可能希望构建所有项目，因此我们可以全面了解所有模块的失败测试。

在下面的Gradle构建文件中，我们有两个任务。任务failTask抛出TaskExecutionException故意使任务失败。该successTask不会失败：


```
task failTask << { task ->
    println "Running ${task.name}"
 
    throw new TaskExecutionException(
            task, 
            new Exception('Fail task on purpose')) 
}
 
task successTask << {
    println "Running ${it.name}"
}
```

让我们从命令行运行这两个任务并查看输出：


```
$ gradle failTask successTask
:failTask
Running failTask
:failTask FAILED
 
FAILURE: Build failed with an exception.
 
* Where:
Build file '/Users/mrhaki/samples/gradle/continue/build.gradle' line: 4
 
* What went wrong:
Execution failed for task ':failTask'.
> Fail task on purpose
 
* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output.
 
BUILD FAILED
 
Total time: 4.148 secs
$
```
我们看到构建失败，仅failTask执行任务。现在我们运行相同的两个任务，但是我们使用命令行选项`--continue`：

```
$ gradle --continue failTask successTask
:failTask
Running failTask
:failTask FAILED
:successTask
Running successTask
 
FAILURE: Build failed with an exception.
 
* Where:
Build file '/Users/mrhaki/samples/gradle/continue/build.gradle' line: 4
 
* What went wrong:
Execution failed for task ':failTask'.
> Fail task on purpose
 
* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output.
 
BUILD FAILED
 
Total time: 6.918 secs
$
```

这次，successTask即使failTask再次失败，也会执行。Gradle将跟踪所有失败的任务，并显示所有失败任务的摘要。

* 郑重声明：文章禁止第三方（腾讯云除外）转载、发表，事情原委[测试窝，首页抄我七篇原创还拉黑，你们的良心不会痛吗？](https://mp.weixin.qq.com/s/ke5avkknkDMCLMAOGT7wiQ)。

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
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
![](https://mmbiz.qpic.cn/mmbiz_png/BuV4gXrNvFrQnPz6hPuyeNCH9BXB4Ufc0lbWyTGjcWrpSwFJOWqFtL0jIYWeqa093ibQcZCu7UMpSVZsFwKbicHQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)