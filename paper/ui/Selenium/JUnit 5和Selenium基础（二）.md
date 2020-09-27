# JUnit 5和Selenium基础（二）

[原文地址](https://www.javacodegeeks.com/2019/10/using-pagefactory-implement-page-object-pattern.html)

> 使用Selenium内置的`PageFactory`实现页面对象模式

- [JUnit 5和Selenium基础（一）](https://mp.weixin.qq.com/s/ehBRf7st-OxeuvI_0yW3OQ)

在这一部分中，将通过Selenium的内置`PageFactory`支持类来介绍`Page Object`模式的实现。`PageFactory`提供一种机制来初始化任何声明`WebElement`或`List<WebElement>`带有`@FindBy`注释的字段的`Page Object`。

由于不可描述的原因，我已经将测试网页打包，需要的请留意文末信息。

## 介绍页面对象模式

页面对象模式的目标是从实际测试中抽象出应用程序页面和功能。页面对象模式提高了代码在测试和固定装置之间的可重用性，但也使代码易于维护。

## 页面API或页面对象
我们将从将TodoMVC页面建模为Page Object 的项目开始。该对象将表示将在测试中使用的页面API。可以使用接口对API本身进行建模。如果查看以下界面的方法，则会注意到这些方法只是页面上可用的用户功能。用户可以创建待办事项，用户可以重命名待办事项，也可以删除待办事项：


```Java
public interface TodoMvc {

    void navigateTo();
    
    void createTodo(String todoName);
    
    void createTodos(String... todoNames);
    
    int getTodosLeft();
    
    boolean todoExists(String todoName);
    
    int getTodoCount();
    
    List<String> getTodos();
    
    void renameTodo(String todoName, String newTodoName);
    
    void removeTodo(String todoName);
    
    void completeTodo(String todoName);
    
    void completeAllTodos();
    
    void showActive();
    
    void showCompleted();
    
    void clearCompleted();
}

```

上面的接口隐藏了所有实现细节。实际上，它与Selenium WebDriver无关。因此，从理论上讲，我们可以针对不同的设备（例如移动本机应用程序，桌面应用程序和Web应用程序）使用此页面的不同实现。

## 创建测试

定义了页面API后，可以直接跳转到创建测试方法。在确认API可用于创建测试之后，再进行页面实现。这种设计模式使测试人员可以专注于应用程序的实际使用，而不必太早掉进细节的坑里。

创建了以下测试：

```java
@ExtendWith(SeleniumExtension.class)
@DisplayName("Managing Todos")
class TodoMvcTests {
 
    private TodoMvc todoMvc;
 
    private final String buyTheMilk = "Buy the milk";
    private final String cleanupTheRoom = "Clean up the room";
    private final String readTheBook = "Read the book";
 
    @BeforeEach
    void beforeEach(ChromeDriver driver) {
        this.todoMvc = null;
        this.todoMvc.navigateTo();
    }
 
    @Test
    @DisplayName("Creates Todo with given name")
    void createsTodo() {
 
        todoMvc.createTodo(buyTheMilk);
 
        assertAll(
                () -> assertEquals(1, todoMvc.getTodosLeft()),
                () -> assertTrue(todoMvc.todoExists(buyTheMilk))
        );
    }
 
    @Test
    @DisplayName("Creates Todos all with the same name")
    void createsTodosWithSameName() {
 
        todoMvc.createTodos(buyTheMilk, buyTheMilk, buyTheMilk);
 
        assertEquals(3, todoMvc.getTodosLeft());
 
 
        todoMvc.showActive();
 
        assertEquals(3, todoMvc.getTodoCount());
    }
 
    @Test
    @DisplayName("Edits inline double-clicked Todo")
    void editsTodo() {
 
        todoMvc.createTodos(buyTheMilk, cleanupTheRoom);
 
        todoMvc.renameTodo(buyTheMilk, readTheBook);
 
        assertAll(
                () -> assertFalse(todoMvc.todoExists(buyTheMilk)),
                () -> assertTrue(todoMvc.todoExists(readTheBook)),
                () -> assertTrue(todoMvc.todoExists(cleanupTheRoom))
        );
    }
 
    @Test
    @DisplayName("Removes selected Todo")
    void removesTodo() {
 
        todoMvc.createTodos(buyTheMilk, cleanupTheRoom, readTheBook);
 
        todoMvc.removeTodo(buyTheMilk);
 
        assertAll(
                () -> assertFalse(todoMvc.todoExists(buyTheMilk)),
                () -> assertTrue(todoMvc.todoExists(cleanupTheRoom)),
                () -> assertTrue(todoMvc.todoExists(readTheBook))
        );
    }
 
    @Test
    @DisplayName("Toggles selected Todo as completed")
    void togglesTodoCompleted() {
        todoMvc.createTodos(buyTheMilk, cleanupTheRoom, readTheBook);
 
        todoMvc.completeTodo(buyTheMilk);
        assertEquals(2, todoMvc.getTodosLeft());
 
        todoMvc.showCompleted();
        assertEquals(1, todoMvc.getTodoCount());
 
        todoMvc.showActive();
        assertEquals(2, todoMvc.getTodoCount());
    }
 
    @Test
    @DisplayName("Toggles all Todos as completed")
    void togglesAllTodosCompleted() {
        todoMvc.createTodos(buyTheMilk, cleanupTheRoom, readTheBook);
 
        todoMvc.completeAllTodos();
        assertEquals(0, todoMvc.getTodosLeft());
 
        todoMvc.showCompleted();
        assertEquals(3, todoMvc.getTodoCount());
 
        todoMvc.showActive();
        assertEquals(0, todoMvc.getTodoCount());
    }
 
    @Test
    @DisplayName("Clears all completed Todos")
    void clearsCompletedTodos() {
        todoMvc.createTodos(buyTheMilk, cleanupTheRoom);
        todoMvc.completeAllTodos();
        todoMvc.createTodo(readTheBook);
 
        todoMvc.clearCompleted();
        assertEquals(1, todoMvc.getTodosLeft());
 
        todoMvc.showCompleted();
        assertEquals(0, todoMvc.getTodoCount());
 
        todoMvc.showActive();
        assertEquals(1, todoMvc.getTodoCount());
    }
}

```

在上述测试类中，我们看到在每次测试之前，ChromeDriver均已`@BeforeEach`通过`Selenium Jupiter`扩展名（`@ExtendWith(SeleniumExtension.class)`）初始化并注入到设置方法中。驱动程序对象将用于初始化页面对象。

页面对象模式很大程度上取决于项目的特征。你可能要经常使用接口，但这不是必需的。你可能要考虑在较低的抽象水平，其中API是暴露的更详细的方法，例如`setTodoInput(String value)`，`clickSubmitButton()`。


## 使用Selenium内置的`PageFactory`实现`Page Object Pattern`

我们已经有一个接口可以对`TodoMVC`页面的行为进行建模，并且我们有使用API​​的失败测试。下一步是实际实现页面对象。为此，我们将使用Selenium内置`PageFactory`类及其实用程序。

`PageFactory`类简化了页面对象模式的实现。该类提供了一种机制来初始化任何声明`WebElement`或`List<WebElement>`带有`@FindBy`注释的字段的`Page Object`。`PageFactory`中提供了支持`Page Object`模式实现的和其他注释。

下面的TodoMvcPage类实现了我们之前创建的接口。它声明了几个带有`@FindBy`注解的字段。它还声明一个构造函数，该构造`WebDriver`函数采用工厂使用的用于初始化字段的参数：


```
public class TodoMvcPage implements TodoMvc {
 
    private final WebDriver driver;
 
    private static final By byTodoEdit = By.cssSelector("input.edit");
    private static final By byTodoRemove = By.cssSelector("button.destroy");
    private static final By byTodoComplete = By.cssSelector("input.toggle");
 
    @FindBy(className = "new-todo")
    private WebElement newTodoInput;
 
    @FindBy(css = ".todo-count > strong")
    private WebElement todoCount;
 
    @FindBy(css = ".todo-list li")
    private List<WebElement> todos;
 
    @FindBy(className = "toggle-all")
    private WebElement toggleAll;
 
    @FindBy(css = "a[href='#/active']")
    private WebElement showActive;
 
    @FindBy(css = "a[href='#/completed']")
    private WebElement showCompleted;
 
    @FindBy(className = "clear-completed")
    private WebElement clearCompleted;
 
    public TodoMvcPage(WebDriver driver) {
        this.driver = driver;
    }
 
    @Override
    public void navigateTo() {
        driver.get("***");
    }
 
    public void createTodo(String todoName) {
        newTodoInput.sendKeys(todoName + Keys.ENTER);
    }
 
    public void createTodos(String... todoNames) {
        for (String todoName : todoNames) {
            createTodo(todoName);
        }
    }
 
    public int getTodosLeft() {
        return Integer.parseInt(todoCount.getText());
    }
 
    public boolean todoExists(String todoName) {
        return getTodos().stream().anyMatch(todoName::equals);
    }
 
    public int getTodoCount() {
        return todos.size();
    }
 
    public List<String> getTodos() {
        return todos
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }
 
    public void renameTodo(String todoName, String newTodoName) {
        WebElement todoToEdit = getTodoElementByName(todoName);
        doubleClick(todoToEdit);
 
        WebElement todoEditInput = find(byTodoEdit, todoToEdit);
        executeScript("arguments[0].value = ''", todoEditInput);
 
        todoEditInput.sendKeys(newTodoName + Keys.ENTER);
    }
 
    public void removeTodo(String todoName) {
        WebElement todoToRemove = getTodoElementByName(todoName);
        moveToElement(todoToRemove);
        click(byTodoRemove, todoToRemove);
    }
 
    public void completeTodo(String todoName) {
        WebElement todoToComplete = getTodoElementByName(todoName);
        click(byTodoComplete, todoToComplete);
    }
 
    public void completeAllTodos() {
        toggleAll.click();
    }
 
    public void showActive() {
        showActive.click();
    }
 
    public void showCompleted() {
        showCompleted.click();
    }
 
    public void clearCompleted() {
        clearCompleted.click();
    }
 
    private WebElement getTodoElementByName(String todoName) {
        return todos
                .stream()
                .filter(el -> todoName.equals(el.getText()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Todo with name " + todoName + " not found!"));
    }
 
    private WebElement find(By by, SearchContext searchContext) {
        return searchContext.findElement(by);
    }
 
    private void click(By by, SearchContext searchContext) {
        WebElement element = searchContext.findElement(by);
        element.click();
    }
 
    private void moveToElement(WebElement element) {
        new Actions(driver).moveToElement(element).perform();
    }
 
    private void doubleClick(WebElement element) {
        new Actions(driver).doubleClick(element).perform();
    }
 
    private void executeScript(String script, Object... arguments) {
        ((JavascriptExecutor) driver).executeScript(script, arguments);
    }
}
```

`@FindBy`不是用于在Page Object中查找元素的唯一注释。也有`@FindBys`和`@FindAll`。

### @FindBys
@FindBys批注用于标记Page Object上的字段，以指示查找应使用一系列@FindBy标签。在这个例子中，硒将搜索元件与class = "button"是内与元件id = "menu"：


```
@FindBys({
  @FindBy(id = "menu"),
  @FindBy(className = "button")
})
private WebElement element;
```

### @FindAll
@FindAll批注用于标记Page Object上的字段，以指示查找应使用一系列@FindBy标记。在此示例中，Selenium将搜索带有class = "button" 和的所有元素id = "menu"。不保证元素按文档顺序排列：


```
FindAll({
  @FindBy(id = "menu"),
  @FindBy(className = "button")
})
private List<WebElement> webElements;
```

## PageFactory初始化Page对象

`PageFactory`提供了几种静态方法来初始化Page Objects。在我们的测试中，在`beforeEach()`方法中，我们需要初始化`TodoMvcPage`对象：

```Java
@BeforeEach
void beforeEach(ChromeDriver driver) {
    this.todoMvc = PageFactory.initElements(driver, TodoMvcPage.class);
    this.todoMvc.navigateTo();
}
```

在`PageFactory`使用反射初始化对象，然后将其初始化所有`WebElement`或`List<WebElement>`标有字段`@FindBy`注释。使用此方法要求`Page Object`具有单个参数构造函数接受`WebDriver`对象。

## 定位元素

那么元素何时定位？每次访问该字段都会进行查找。例如，当我们执行代码：`new TodoInput.sendKeys(todoName + Keys.ENTER);`在`in createTodo()`方法时，实际执行的指令是：`driver.findElement(By.className('new-todo')).sendKeys(todoName + Keys.ENTER)`。不是在对象初始化期间而是在第一个元素查找期间引发未找到元素的潜在异常。Selenium使用代理模式来实现所描述的行为。

### @CacheLookup
在某些情况下，每次访问带注释的字段时都不需要查找元素。在这种情况下，我们可以使用`@CacheLookup`注释。在示例中，输入字段在页面上没有更改，因此可以缓存查找结果：

```
@FindBy(className = "new-todo")
@CacheLookup
private WebElement newTodoInput;
```

## 运行测试

现在是执行测试的时候了。可以从IDE或使用终端来完成：

`./gradlew clean test --tests *TodoMvcTests`

通过所有测试，构建成功：


```
> Task :test
 
demos.selenium.todomvc.TodoMvcTests > editsTodo() PASSED
 
demos.selenium.todomvc.TodoMvcTests > togglesTodoCompleted() PASSED
 
demos.selenium.todomvc.TodoMvcTests > createsTodo() PASSED
 
demos.selenium.todomvc.TodoMvcTests > removesTodo() PASSED
 
demos.selenium.todomvc.TodoMvcTests > togglesAllTodosCompleted() PASSED
 
demos.selenium.todomvc.TodoMvcTests > createsTodosWithSameName() PASSED
 
demos.selenium.todomvc.TodoMvcTests > clearsCompletedTodos() PASSED
 
BUILD SUCCESSFUL in 27s
3 actionable tasks: 3 executed

```

微信公众号后台回复“测试网页”，获取测试网页下载地址。


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
