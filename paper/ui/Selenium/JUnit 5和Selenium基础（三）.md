# JUnit 5和Selenium基础（三）


[原文地址](https://www.javacodegeeks.com/2019/12/junit-5-and-selenium-improving-project-configuration.html)

在这一部分教程中，将介绍JUnit 5的其他功能，这些功能将通过并行运行测试，配置测试顺序和创建参数化测试来帮助减少测试的执行时间。还将介绍如何利用Selenium Jupiter功能，例如通过系统属性进行测试执行配置，单个浏览器会话测试以加快测试执行速度或捕获测试中的屏幕截图，AssertJ库的基本Demo。

## 使用JUnit 5并行测试执行

JUnit 5带有内置的并行测试执行支持。下面的命令将并行运行`TodoMvcTests`的测试方法：

`./gradlew clean test --tests *TodoMvcTests -Djunit.jupiter.execution.parallel.enabled=true -Djunit.jupiter.execution.parallel.mode.default=concurrent`

构建成功，在执行过程中，注意到两个Chrome浏览器实例正在运行。在此运行中，所有测试的执行时间减少到原来的几分之一：

```
> Task :test
 
demos.selenium.todomvc.TodoMvcTests > createsTodo() PASSED
 
demos.selenium.todomvc.TodoMvcTests > createsTodosWithSameName() PASSED
 
demos.selenium.todomvc.TodoMvcTests > togglesAllTodosCompleted() PASSED
 
demos.selenium.todomvc.TodoMvcTests > togglesTodoCompleted() PASSED
 
demos.selenium.todomvc.TodoMvcTests > clearsCompletedTodos() PASSED
 
demos.selenium.todomvc.TodoMvcTests > editsTodo() PASSED
 
demos.selenium.todomvc.TodoMvcTests > removesTodo() PASSED
 
BUILD SUCCESSFUL in 10s
4 actionable tasks: 4 executed
```

## 使用JUnit 5测试执行顺序

一般来讲，自动化测试应该能够独立运行并且没有特定的顺序，并且测试结果不应依赖于先前测试的结果。但是在某些情况下测试执行需要依赖特定顺序。

默认情况下，在JUnit 5中，测试方法的执行在构建之间是无序的，因此非确定性的。但是可以使用内置方法定购器或通过创建自定义定购器来调整执行顺序以满足测试的需求。我们将使用`@Order`批注来提供测试方法的排序，并使用注释类，`@TestMethodOrder`以指示`JUnit 5`方法已排序。


```Java
@ExtendWith(SeleniumExtension.class)
@SingleSession
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Managing Todos")
class TodoMvcTests {
 
    @Test
    @Order(1)
    @DisplayName("test001")
    void createsTodo() {
 
    }
 
    @Test
    @Order(2)
    @DisplayName("test002")
    void createsTodosWithSameName() {
 
    }
  
}
```

## 使用Selenium Jupiter的单个浏览器会话

对于`TodoMvcTests`类中的每个测试，都会启动一个新的`Chrome`浏览器实例，并在每个测试之后将其关闭。此行为导致整个套件的执行花费了相当多的时间。`Selenium Jupiter`附带了一个简单的类级别注释，可以修改这项功能。`@SingleSession`批注会更改行为，以便在所有测试之前初始化浏览器实例一次，并在所有测试之后关闭浏览器实例。

要应用`@SingleSession`需要稍微修改测试类，然后将驱动程序对象注入构造函数中而不是`@BeforeEach`方法中。我们还需要注意每次测试的正确状态。这可以通过清除`@AfterEach`方法中存储待办事项的本地存储来完成。我还创建了一个字段`driver`，该字段保留所有测试中使用的驱动程序对象实例。

```Java
private final ChromeDriver driver;
 
public TodoMvcTests(ChromeDriver driver) {
    this.driver = driver;
    this.todoMvc = PageFactory.initElements(driver, TodoMvcPage.class);
    this.todoMvc.navigateTo();
}
 
@AfterEach
void storageCleanup() {
    driver.getLocalStorage().clear();
}
```

当执行测试时，我们可以观察到执行所有测试的时间大大减少了：


```
./gradlew clean test
 
> Task :test
 
demos.selenium.todomvc.TodoMvcTests > editsTodo() PASSED
 
demos.selenium.todomvc.TodoMvcTests > togglesTodoCompleted() PASSED
 
demos.selenium.todomvc.TodoMvcTests > createsTodo() PASSED
 
demos.selenium.todomvc.TodoMvcTests > removesTodo() PASSED
 
demos.selenium.todomvc.TodoMvcTests > togglesAllTodosCompleted() PASSED
 
demos.selenium.todomvc.TodoMvcTests > createsTodosWithSameName() PASSED
 
demos.selenium.todomvc.TodoMvcTests > clearsCompletedTodos() PASSED
 
demos.selenium.todomvc.SeleniumTest > projectIsConfigured(ChromeDriver) PASSED
 
BUILD SUCCESSFUL in 9s
3 actionable tasks: 3 executed
```

提示：如果您希望从选定的类中运行测试，则可以使用Gradle测试任务随附的测试过滤。例如，此命令将仅运行来自`TodoMvcTests`类的测试：`./gradlew clean test --tests *.todomvc.TodoMvcTests`


## 但浏览器实例并行测试

如果你现在尝试使用`JUnit 5`并行执行测试，在并行执行中，每种方法都需要单独的驱动程序实例，并且`@SingleSession`启用后，我们将为所有测试共享一个实例。为了解决这个问题，需要运行测试配置并行执行，为了让顶级类并行运行，但方法在同一线程中。

只需复制`TodoMvcTests`类，然后尝试以下命令：

`./gradlew clean test --tests *TodoMvcTests -Djunit.jupiter.execution.parallel.enabled=true -Djunit.jupiter.execution.parallel.mode.default=same_thread -Djunit.jupiter.execution.parallel.mode.classes.default=concurrent`

在执行过程中，应该看到正在运行并在终端中输出以下内容：

```shell
<===========--> 87% EXECUTING [3s]
> :test > 0 tests completed
> :test > Executing test demos.selenium.todomvc.MoreTodoMvcTests
> :test > Executing test demos.selenium.todomvc.EvenMoreTodoMvcTests
> :test > Executing test demos.selenium.todomvc.TodoMvcTests
```

## Selenium Jupiter的驱动程序配置

在当前测试中，我们将`ChromeDriver`直接注入测试类。但是在某些情况下，我们希望对注入的驱动程序有更多的控制，而我们宁愿注入`WebDriver`（接口）并稍后决定应该注入哪个驱动程序实例。我们还需要更改`storageCleanup()`方法，因为通用`WebDriver`不提供直接的`localStorage`访问：


```java
public TodoMvcTests(WebDriver driver) {
    this.driver = driver;
    this.todoMvc = PageFactory.initElements(driver, TodoMvcPage.class);
    this.todoMvc.navigateTo();
}
 
@AfterEach
void storageCleanup() {
    ((JavascriptExecutor) driver).executeScript("window.localStorage.clear()");
}
```

现在，要在运行时更改浏览器类型，我们需要调整`sel.jup.default.browserconfig`属性。

配置`JUnit 5`和`Selenium Jupiter`的常用方法之一是通过Java系统属性。可以使用属性文件以编程方式完成此操作，也可以使用`-Dswitch` 将属性直接传递给JVM 。为了确保在执行`Gradle`时传递给JVM的属性在测试中可用，我们需要进行`build.gradle`如下修改：

```Groovy
test {
    systemProperties System.getProperties()
 
    useJUnitPlatform()
    testLogging {
        events "passed", "skipped", "failed"
    }
}

```

当运行命令时`./gradlew clean test -Dprop=value`，该属性将在测试中可用。通过上述更改，我们可以选择浏览器类型来运行测试：

`./gradlew clean test --tests *TodoMvcTests -Dsel.jup.default.browser=firefox`

* `Selenium Jupiter`允许在测试结束时保存屏幕截图-始终或仅在失败时保存。您还可以自定义输出目录和格式。
* `./gradlew clean test --tests *TodoMvcTests -Dsel.jup.default.browser=firefox -Dsel.jup.screenshot.at.the.end.of.tests=true -Dsel.jup.screenshot.format=png -Dsel.jup.output.folder=/tmp`

## 使用JUnit 5进行参数化测试

参数化单元测试的总体思路是针对不同的测试数据运行相同的测试方法。要在JUnit 5中创建参数化测试，请使用注释测试方法，@ParameterizedTest并提供该测试方法的参数源。有几种可用的参数来源，包括：

* @ValueSource –提供对文字值数组（例如，int，字符串等）的访问。
* @MethodSource –提供对从工厂方法返回的值的访问
* @CsvSource –从一个或多个提供的CSV行中读取逗号分隔值（CSV）
* @CsvFileSource –用于加载逗号分隔值（CSV）文件

为了在测试中使用上述CSV文件，我们需要在测试中加上@ParameterizedTest注释（而不是@Test），然后在@CsvFileSource注释中指向文件：


```Java
@ParameterizedTest
@CsvFileSource(resources = "/todos.csv", numLinesToSkip = 1, delimiter = ';')
@DisplayName("Creates Todo with given name")
void createsTodo(String todo) {
    todoMvc.createTodo(todo);
    assertSingleTodoShown(todo);
}
```

在下一个示例中，我们将使用以下CSV格式存储在`src/test/resources`目录中：

```
todo;done
Buy the milk;false
Clean up the room;true
Read the book;false
```

CSV文件中的每个记录都有两个字段：`name`和`done`。在上述测试中，仅使用待办事项的名称。但是我们当然可以使测试复杂一点，并同时使用这两个属性：

```java
@ParameterizedTest(name = "{index} - {0}, done = {1}" )
@CsvFileSource(resources = "/todos.csv", numLinesToSkip = 1, delimiter = ';')
@DisplayName("test003")
void createsAndRemovesTodo(String todo, boolean done) {
 
    todoMvc.createTodo(todo);
    assertSingleTodoShown(todo);
 
    todoMvc.showActive();
    assertSingleTodoShown(todo);
 
    if (done) {
        todoMvc.completeTodo(todo);
        assertNoTodoShown(todo);
 
        todoMvc.showCompleted();
        assertSingleTodoShown(todo);
    }
 
    todoMvc.removeTodo(todo);
    assertNoTodoShown(todo);
}
```

## 使用AssertJ更好的断言

`JUnit 5`具有许多内置的断言，在实际工作中，可能需要的超出`JUnit 5`所能提供的。在这种情况下，建议使用AssertJ库。`AssertJ`是一个Java库，提供了一组丰富的断言，真正有用的错误消息，提高了测试代码的可读性，并且设计为IDE中容易使用。

AssertJ的一些功能：

* 对许多Java类型的流利断言，包括日期，集合，文件等。
* SoftAssertions（类似于JUnit 5的assertAll）
* 复杂领域比较
* 可以轻松扩展–自定义条件和自定义断言

要在项目中使用AssertJ，我们需要向中添加单个依赖项`build.gradle`：

`testCompile('org.assertj:assertj-core:3.13.2')`

首先，我们需要静态导入`org.assertj.core.api.Assertions.*`并使用以下assertThat方法完成代码：`assertThat(objectUnderTest)`.

例如将`assertThat(todoMvc.getTodosLeft()).isEqualTo(3);`使用`AssertJ`而不是`assertEquals(3, todoMvc.getTodosLeft());`纯`JUnit 5`或`assertThat(todoMvc.todoExists(readTheBook)).isTrue()`来编写`assertTrue(todoMvc.todoExists(readTheBook))`。

使用复杂类型甚至更好：

```
todoMvc.createTodos(buyTheMilk, cleanupTheRoom, readTheBook);
 
assertThat(todoMvc.getTodos())
        .hasSize(3)
        .containsSequence(buyTheMilk, cleanupTheRoom, readTheBook);
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