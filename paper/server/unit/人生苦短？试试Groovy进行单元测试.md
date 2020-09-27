# 人生苦短？试试Groovy进行单元测试

[原文链接](https://www.javacodegeeks.com/2015/04/short-on-time-switch-to-groovy-for-unit-testing.html)

如果您今天正在编程，那么您很可能听说过单元测试或测试驱动的开发过程。我还没有遇到一个既没有听说过又没有听说过单元测试并不重要的程序员。在随意的讨论中，大多数程序员似乎认为单元测试非常重要。

但是，当我开始使用代码并问“单元测试在哪里？”时，我得到了一个完全不同的故事。我最近在网上问我的程序员朋友为什么不这样做，以及为什么其他程序员不这样做呢？不要编写单元测试。当我问程序员或IT经理同样的问题时，我经常听到的第一答案是：“我没有时间”或类似的问题。通常会出现这样的论点，即使用单元测试编写应用程序要比不使用单元测试编写时间长20％，并且“我们受到时间限制”。

我的建议–当我们尝试解决时间不足的问题时，也许我们可以在娱乐性上做出一些贡献。

## 在实践中

我正在为一个应用程序设计原型，该应用程序将允许用户输入有关房屋装修项目的信息，然后与朋友共享该项目的材料和工具信息。然后，朋友可以承诺贷款或购买项目中所需的一些材料或工具。基本上是用于家庭装修项目的“登记处”。

测试将在采用Project对象的方法上进行，遍历该项目的工具列表以查看该工具是否已经被承诺，并创建一个未被承诺的工具列表。然后，它将把该列表传递给将查询每个工具当前价格的服务。

原型是用Grails完成的，但是我们将用Java编写此方法：


```
public List<Tool> neededToolList(Project project) {
        final List<Tool> retList = new ArrayList<>();
 
        if (project.getTools() == null || project.getTools().isEmpty()) {
            return retList;
        }
 
        for (Tool tool : project.getTools()) {
            if (!tool.getPromise().isPromised()) {
                retList.add(tool);
            }
        }
 
        List<Tool> tools = lookupService.updateToolList(retList);
        return tools;
    }
```


单个单元测试可能类似于：


```
@Test
    public void testNeededToolList() {
        Tools _instance = new Tools();
 
        Project project = new Project();
 
        Promise promise = new Promise();
        promise.setProject(project);
        promise.setPromised(false);
        Promise promise2 = new Promise();
        promise2.setProject(project);
        promise2.setPromised(true);
 
        List<Tool> tools = new ArrayList<>();
        List<Tool> lookupTools = new ArrayList<>();
        Tool tool1 = new Tool();
        tool1.setName("table saw");
        tool1.setStoreId("T001");
        tool1.setPromise(promise);
        tools.add(tool1);
        lookupTools.add(tool1);
        Tool tool2 = new Tool();
        tool2.setName("pneumatic nail guns");
        tool2.setStoreId("T027");
        tool2.setPromise(promise2);
        tools.add(tool2);
        project.setTools(tools);
 
        List<Tool> mockedTools = new ArrayList<>();
        Tool mockedTool1 = new Tool();
        mockedTool1.setPromise(promise);
        mockedTool1.setName("table saw");
        mockedTool1.setStoreId("T001");
        mockedTool1.setPrice(129.0);
        mockedTools.add(mockedTool1);
 
        lookupService = Mockito.mock(LookupServiceImpl.class);
        Mockito.when(lookupService.updateToolList(lookupTools)).thenReturn(mockedTools);
        _instance.setLookupService(lookupService);
 
        List<Tool> returnedTools  = _instance.neededToolList(project);
 
        assertTrue(returnedTools.size() == 1);
        for(Tool tool : returnedTools) {
            assertEquals(129.0, tool.getPrice(), 0.01);
        }
    }
```

这是一个简单的测试，并且只有一个。需要针对几种情况编写测试，例如空值。例如，如果StoreID不存在怎么办？

## 输入Groovy

在之前的文章中，我已经介绍了我的好朋友Groovy编程语言。让我们看看是否可以进行Groovy测试。

Groovy带来了许多语法上的捷径，这些捷径有助于加快编写代码（包括测试）的速度。让我们看一下在Groovy中重写该测试的可能方法。


```
class GroovyToolsTest extends GroovyTestCase {
    def lookupService = [
        updateToolList : {List<Tool> toolList ->
            toolList.each {
                if(it.storeId == "T001") {
                    it.price = 129.0
                }
            }
            return toolList
        }
    ] as LookupService
 
    void testNeededToolList() {
        def _instance = new Tools()
        def project = new Project()
        project.tools = [
            new Tool(name: "table saw", storeId: "T001", promise: new Promise(project: project, promised: false)),
            new Tool(name: "pneumatic nail guns", storeId: "T027", promise: new Promise(project: project, promised: true))
        ]
 
        _instance.lookupService = lookupService
 
        def returnedList = _instance.neededToolList(project)
        returnedList.size() == 1
        returnedList.each {
            if(it.storeId == "T001") {
                assert it.price == 129.0
            }
        }
        println "done"
    }
}
```

我们看到的第一件事是Groovy为我们提供了一种很棒的Mocking代码机制，它使我们能够做的比我在Mocking框架中所能做的还要多。在模拟框架中，我通常为期望返回的数据创建一个新对象。在这里，我实际上是将数据更改为服务应该返回的内容。

切记：我不是在测试服务，所以模拟服务应该返回我期望服务返回的值。

我还发现可以在一个调用中创建对象并加载数据的功能（与创建Bean和调用每个setter相对）更容易编写，读取和复制为模板，以创建更多内容。Groovy提供了几种处理列表的方法，使之成为快速开发和维护测试的出色语言。

如果您想对单元测试有所不同，那么还有Spock测试框架。它具有更广泛的语言，使其更具行为驱动的外观，但仍使用上一示例中的所有Groovy Goodness。


```
class ToolsSpec extends Specification {
    def lookupService = [
        updateToolList : {List<Tool> toolList ->
            println "mocked service"
            toolList.each { tool ->
                if(tool.storeId == "T001")
                    tool.price = 129.0
            }
            return toolList
        }
    ] as LookupService
 
    def "Lookup needed tool list"() {
        given:"Create instance"
            def _instance = new Tools()
            def project = new Project()
            project.tools = [
                [name: "table saw", storeId: "T001", promise: [project: project, promised: false] as Promise] as Tool,
                [name: "pneumatic nail guns", storeId: "T027", promise: [project: project, promised: true] as Promise] as Tool,
                ] as List<Tool>;
 
        _instance.lookupService = lookupService
 
        expect:"Tool List"
            def returnedList = _instance.neededToolList(project)
            returnedList.size() == 1
            returnedList.each {
                if(it.storeId == "T001") {
                    assert it.price == 129.0
                }
            }
 
    }
 
}
```

请注意，我使用了一种不同的语法为Tool创建测试数据对象。这是标准的Groovy功能，它允许程序员将映射转换为具体的类，并且在先前的示例中也可以使用。当您习惯阅读Groovy时，这可能比新的Object语法更容易阅读。

在这两个示例中，语法“糖”更紧密的代码并不是唯一的好处。测试失败的输出也会有所不同，并且会更有帮助

在第一个示例中，测试失败的输出为：


```
java.lang.AssertionError: expected:<128.0> but was:<129.0>
    at org.junit.Assert.fail(Assert.java:88)
    at org.junit.Assert.failNotEquals(Assert.java:834)
    at org.junit.Assert.assertEquals(Assert.java:553)
    at org.junit.Assert.assertEquals(Assert.java:683)
    at org.projectregistry.services.ToolsTest.testNeededToolList(ToolsTest.java:93)
....
```

Groovy和Spock测试的输出如下所示：


```
Assertion failed: 
 
assert it.price == 128.0
       |  |     |
       |  129.0 false
       org.projectregistry.model.Tool@5e59238b
 
    at org.codehaus.groovy.runtime.InvokerHelper.assertFailed(InvokerHelper.java:399)
    at org.codehaus.groovy.runtime.ScriptBytecodeAdapter.assertFailed(ScriptBytecodeAdapter.java:648)
    at org.projectregistry.services.GroovyToolsTest$_testNeededToolList_closure2.doCall(GroovyToolsTest.groovy:34)
...
```
Groovy输出中提供了更多信息，这反过来又使您可以更快地进行修复。

## 代码项目
因此，随着可以节省语法和输出的时间，并希望通过一种新的和不同的语言来增加编程乐趣，我希望每个人都可以尝试Groovy和/或Spock来克服惯性，这种惯性会阻止程序员进行单元测试。

学习如何简单。Groovy和Spock都有据可查的文档，仅通过搜索即可获得许多资源。在各种社交媒体上也有一个非常活跃和乐于助人的社区，我相信很乐意提供帮助。

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