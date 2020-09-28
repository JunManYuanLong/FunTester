# Selenium等待：sleep、隐式、显式和Fluent



`Selenium`等待页面加载在`Selenium`自动化测试中起着重要的作用。它们有助于使测试用例更加稳定，增强健壮性。`Selenium`提供多种等待，根据某些条件在脚本执行相应的等待，从而确保`Selenium`执行自动化测试时不会导致脚本失败。

在本文中，我们将介绍`Selenium`等待和睡眠的类型，并提供演示`Demo`以及对它们的比较分析。

# 为什么需要等待

大多数应用程序的前端都是基于`JavaScript`或`Ajax`构建的，使用诸如`React`、`Angular`、`Vue`之类的框架，都是需要花费一定时间才能在页面上加载或刷新`Web元素`。因此，如果测试用例在脚本中找到尚未加载到页面上的元素，则`Selenium`会向抛出`ElementNotVisibleException`的异常。

下面的代码片段将展示与使用`Selenium`执行自动化测试时的问题。在此代码段中，使用的是某一航空订票网站的示例，在该示例中，`post`用户选择行程日期的`From`和`To`目的地，`Web应用程序`需要花费一些时间来加载所需的航班详细信息。在正常用户使用情况下，可以从列表中预订某一班航班。现在，由于页面尚未完全加载，测试脚本无法找到**立即预订**按钮。结果抛出*NoSuchElementException*异常。下面的代码段和控制台输出：


```Java
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;

public class NoWaitImplemented {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", ".\\Driver\\chromedriver.exe");
        WebDriver driver=new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.***.com/");
        driver.findElement(By.id("FromSector_show")).sendKeys("北京", Keys.ENTER);
        driver.findElement(By.id("Editbox13_show")).sendKeys("上海", Keys.ENTER);
        driver.findElement(By.id("ddate")).click();
        driver.findElement(By.id("snd_4_08/08/2020")).click();
        driver.findElement(By.className("src_btn")).click();
        driver.findElement(By.xpath("//button[text()='立即预定']")).click();
   }

}
```
控制台输出：


```shell

*** Element info: (Us ing=xpath, value=//button(text()='立即预定']
at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
at sun.reflect.NativeConstructorAccessorImpl.newInstance (Unknown Source )
at sun.reflect.DelegatingConstructorAcces sorImp1.newInstance (Unknown Source )
at java.lang.reflect.Constructor.newInstance (Unknown Source )
at org.openqa.selenium.remote.http.W3CHttpResponseCodec.createException(W3CHttpResponseCodec.java:187)at org.openqa.selenium.remote.http.W3CHttpResponseCodec.decode (W3CHttpResponseCodec.java:122)
at org.openqa.selenium.remote.http.W3CHttpResponseCodec.decode (W3CHttpResponseCodec.java:49)
at org.openqa.selenium.remote.HttpC ommandExecutor.execute (HttpC ommandExecutor.java:158)
at org.openqa.selenium.remote.service.DriverCommandExecutor.execute(DriverCommandExecutor.java:83)at org.openqa.selenium.remote.RemoteWebDriver.execute ( RemoteWebDriver.java:

```

`Selenium`等待页面加载有助于解决此问题。`Selenium`等待有不同类型，例如**隐式等待**和**显式等待**，可确保在`Selenium`脚本执行元素定位之前，页面元素加载到页面中以进行进一步的操作。

# Selenium等待

在使用`Selenium`执行自动化测试时，在编写`Selenium`脚本时，我们使用以下类型的等待：

* Thread.Sleep()方法
* 隐式等待
* 显式等待
* Fluent等待


## Thread.Sleep()方法

`Thread.Sleep()`是属于线程类的静态方法。可以使用类名（即`Thread`）的引用来调用此方法。如果在使用`Selenium`执行自动化测试时使用`Thread.Sleep()`，则此方法将在指定的时间段内停止执行脚本，而不管是否在网页上找到了该元素。`Thread.Sleep()`方法中时间参数的单位是毫秒。相同的语法是：

`Thread.sleep(3000);`

睡眠函数抛出`InterruptedException`，因此应使用`try-catch`块进行处理，如下所示


```Java
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error("error", e);
        }
```

## `Thread.Sleep()`不是好主意

下面我将重点介绍使用`Thread.Sleep()`的一些缺点。

使用`Thread.Sleep()`方法`Selenium Webdriver`等待指定的时间，无论是否找到对应元素。如果在指定的持续时间之前找到元素，脚本将仍然等待持续的时间，从而增加了脚本的执行时间。如果花费的时间超过了定义的时间，脚本将抛出错误。这就是为什么使用`Selenium`处理动态元素，那么最好不要使用`Thread.Sleep()`。

下面的代码片段突出显示了`Thread.Sleep()`在`Selenium`自动化测试中的用法。


```Java
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;

public class ThreadWait {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", ".\\Driver\\chromedriver.exe");
        WebDriver driver=new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.***.com/");
        
        driver.findElement(By.id("FromSector_show")).sendKeys("北京", Keys.ENTER);
        driver.findElement(By.id("Editbox13_show")).sendKeys("上海", Keys.ENTER);
        driver.findElement(By.id("ddate")).click();
        driver.findElement(By.id("snd_4_08/08/2020")).click();
        driver.findElement(By.className("src_btn")).click();
        Thread.sleep(5000);
        driver.findElement(By.xpath("//button[text()='立即预定']")).click();

    }

}
```


如果不使用`Thread.sleep()`，那么哪个`Selenium`等待页面加载就足以满足测试要求？在这种情况下，这就需要**隐式等待**来处理。

## 隐式等待

`Selenium`解决了`Thread.Sleep()`存在的问题，并提出了两个`Selenium`等待页面加载的方法。其中之一是**隐式等待**，它允许您将`WebDriver`暂停特定的时间，直到`WebDriver`在网页上找到所需的元素为止。

这里要注意的关键点是，与`Thread.Sleep()`不同，它不需要等待整个时间段。如果在指定的持续时间之前找到元素，将继续执行下一行代码，从而减少了脚本执行的时间。这就是为什么**隐式等待**也称为动态等待的原因。如果在指定的持续时间内未找到该元素，则抛出`ElementNotVisibleException`。

关于**隐式等待**的另一件值得注意的事情是，它是全局应用的，这使其比`Thread.Sleep()`更好。这意味着测试人员只需编写一次即可，它适用于整个`WebDriver`实例中脚本上指定的所有`Web元素`。是不是特别方便？实现相同的语法是：

`driver.manage().timeouts().implicitlyWait(Time Interval to wait for, TimeUnit.SECONDS);`

隐式等待的默认时间为*零*，并且每隔*500毫秒*会不断轮询所需的元素。让我们看下面的代码片段，展示隐式等待的用法。在此示例中，我使用了相同的订票网站示例。在这种情况下，我们将进行预订过程，在此过程中页面需要花费更多的时间来加载。在这里，存在两个页面的页面加载问题，我们使用`Thread.Sleep()`而不是多次使用`Thread.Sleep()`来处理一行代码。


```Java
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;

public class ImplicitWait {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", ".\\Driver\\chromedriver.exe");
        WebDriver driver=new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get("https://www.***.com/");
        
        driver.findElement(By.id("FromSector_show")).sendKeys("北京", Keys.ENTER);
        driver.findElement(By.id("Editbox13_show")).sendKeys("上海", Keys.ENTER);
        driver.findElement(By.id("ddate")).click();
        driver.findElement(By.id("snd_4_08/08/2020")).click();
        driver.findElement(By.className("src_btn")).click();
       driver.findElement(By.xpath("//button[text()='立即预定']")).click();
        
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,750)");
        
        driver.findElement(By.xpath("//input[@type='email']")).sendKeys("***@FunTester.com");
        driver.findElement(By.xpath("//span[text()='继续']")).click();
        WebElement title=driver.findElement(By.id("titleAdult0"));
        Select titleTraveller=new Select(title);
        titleTraveller.selectByVisibleText("MS");
        driver.findElement(By.xpath("//input[@placeholder='Enter First Name']")).sendKeys("FunTester");
        driver.findElement(By.xpath("//input[@placeholder='Enter Last Name']")).sendKeys("FunTester");
        driver.findElement(By.xpath("//input[@placeholder='Mobile Number']")).sendKeys("*********");
        driver.findElement(By.xpath("//div[@class='con1']/span[@class='co1']")).click();
        }

}
```

我们知道了一个事实，即应该在一定的持续时间内加载页面，但是如果我们不知道在加载时该元素是*可见/可点击*的，该怎么办？正如它出现的时候一样，元素是动态的，并且可能会不时地变化。在这种情况下，显式等待将帮助解决此问题。让我们看一下显示等待的细节。

## 显示等待

**显式等待**是动态`Selenium`等待的另外一种类型。**显式等待**帮助可在特定时间段内根据特定条件停止脚本的执行。时间到了以后，脚本将抛出`ElementNotVisibleException`异常。在测试人员不确定要等待的时间的情况下，显式等待会派上大用场。使用`elementToBeClickable()`或`textToBePresentInElement()`之类的条件，可以等待指定的持续时间。可以结合使用`WebDriverWait`和`ExpectedConditions`类来使用这些预定义方法。为了使用这种情况，请在代码中导入以下软件包：

```Java
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
```

添加该代码后，需要为`WebDriverWait`类创建一个引用变量，并使用`WebDriver`实例实例化该变量，并提供可能需要的`Selenium`等待页面加载的数量。时间单位是秒。可以如下定义它：

`WebDriverWait wait = new WebDriverWait(driver,30);`

为了使用`ExpectedCondition`类的预定义方法，我们将使用如下的`wait`引用变量：

`wait.until(ExpectedConditions.visibilityOfElementLocated());`

### 预期条件的类型

以下是在使用Selenium执行自动化测试时通常使用的几种预期条件。

* visibleOfElementLocated()：验证给定元素是否存在
* alertIsPresent()：验证是否存在警报。
* elementToBeClickable()：验证给定元素是否在屏幕上存在/可单击
* textToBePresentInElement()：验证给定元素是否具有必需的文本
* titlels()：验证条件，等待具有给定标题的页面

还有更多可用的预期条件，您可以通过`Selenium`官方`GitHub`页面进行引用。与**隐式等待**一样，显式等待也会在每*500毫秒*后继续轮询。

下面是**显示等待**在`Selenium`中用法的代码段。在此示例中，我们使用的是订票网站，其中的模式在动态时间显示在主页上。使用**显式等待**，基于元素的可见性，我们将等待元素并关闭弹出窗口。

参考代码：


```Java
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ExplicitWait {

    public static void main(String[] args) {
                System.setProperty("webdriver.chrome.driver", ".\\Driver\\chromedriver.exe");
                WebDriver driver=new ChromeDriver();
                driver.manage().window().maximize();
                
                driver.get("https://www.*****.com/");
                
                driver.findElement(By.xpath("//span[@class='rm-city__selectorBoxLoca'][contains(text(),'北京')]")).click();
                WebDriverWait wait=new WebDriverWait(driver, 120);
                wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class='Campaign__innerWrapper']/button"))));
                driver.findElement(By.xpath("//div[@class='Campaign__innerWrapper']/button")).click();

    }

}
```

* 注意：当同时使用**隐式等待**和**显式等待**时，它们等待的时间是累计的，而不是在单个等待条件下工作。例如，如果给定隐式等待*30秒*，给定显式等待*10秒*，那么它正在寻找的显式元素将等待*40秒*。

## 显式等待与隐式等待

现在各位已经知道**隐式等待**和**显式等待**的用法，因此让我们看一下一下这两个`Selenium`等待之间的区别：

|隐式等待|显式等待|
|-----|----|
|默认情况下应用于脚本中的所有元素。|仅适用于特定条件的特定元素。|
|不能基于指定条件（例如元素选择/可点击）而不是显式地等待。|可以根据特定条件指定等待时间。|
|确定该元素在特定时间内可能可见时，通常使用它|不知道元素可见性的时间时，通常使用它。它具有动态性质。|

## Fluent等待

就其本身功能而言，**Fluent**等待类似于**显式等待**。在**Fluent**等待中，当测试人员不知道某个元素可见或单击所需的时间时，而需要对其执行`Selenium`等待。**Fluent**等待提供的一些差异因素：

* 轮询频率：在显式等待的情况下，默认情况下此轮询频率为*500毫秒*。使用`Fluent wait`，测试工程师可以根据需要更改此轮询频率。
* 忽略异常：在轮询期间，如果找不到元素，则可以忽略任何异常，例如`NoSuchElement`异常等。
* 除了这些差异因素（例如显式等待或隐式等待）之外，**Fluent**还可以定义等待元素可见或可操作的时间。以下语法或代码行用于定义`Selenium`中的`Fluent`等待：


```Java
        Wait<WebDriver> fluentWait = new FluentWait<WebDriver>(driver)
                .withTimeout(60, SECONDS) // 自定义等待的总时间
                .pollingEvery(2, SECONDS) // 自定义轮询频率
                .ignoring(NoSuchElementException.class); //  自定义要忽略的异常
        WebElement foo = fluentWait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver)  // 自定义等待条件
            {
                return driver.findElement(By.id("FunTester"));
            }
        });
```

咋一看语法似乎很复杂，但是一旦开始学习使用，熟练之后，**Fluent**会变得很方便。对于初级的自动化工程师来讲，复杂的语法是测试人员选择显式等待而不是**Fluent**等待的最大原因之一。另外，**显式等待**和`Fluent`等待之间的主要区别在于**显式等待**提供了预定义的条件，这些条件适用于我们需要等待的元素，而对于`Fluent Selenium`等待，则可以自定义适用方法中的条件。

----
公众号**FunTester**首发，原创分享爱好者，腾讯云和掘金社区首页推荐，知乎七级原创作者，欢迎关注、交流，禁止第三方擅自转载。

FunTester热文精选
=

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)
- [测试开发工程师工作技巧](https://mp.weixin.qq.com/s/TvrUCisja5Zbq-NIwy_2fQ)
- [Selenium4 IDE，它终于来了](https://mp.weixin.qq.com/s/XNotlZvFpmBmBQy1pYifOw)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [为什么测试覆盖率如此重要](https://mp.weixin.qq.com/s/0evyuiU2kdXDgMDnDKjORg)
- [为什么测试覆盖率如此重要](https://mp.weixin.qq.com/s/0evyuiU2kdXDgMDnDKjORg)
- [吐个槽，非测误入。](https://mp.weixin.qq.com/s/BBFzUZVFMmU7a6qfLKas2w)
- [自动化测试框架](https://mp.weixin.qq.com/s/vu6p_rQd3vFKDYu8JDJ0Rg)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDnHxttBoq6jhgic4jJF8icbAMdOvlR0xXUX9a3tupYYib3ibYyIHicNtefS3Jo7yefLKlQWgLK7bCgCLA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)