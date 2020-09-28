# JUnit中用于Selenium测试的中实践



自动化测试通过允许他们自动化重复性的任务来帮助简化软件测试人员的生活，而开源测试自动化框架（如Selenium）使用户能够大规模自动化Web测试体验。 但是，如果您无法验证测试用例是否通过，则自动化测试有什么用？

这是断言的体现，因此您可以跟踪执行Selenium测试的自动化脚本后遇到了多少测试失败或成功。今天，我要告诉你如何在JUnit的断言，不同类型的断言在JUnit是由例子。

# 什么是断言？为什么要使用它们？

断言，不论放在硒测试使用的工具和框架的自动化测试的一个组成部分。在测试中使用断言来验证或检查操作/功能的结果是否与执行测试后预期的结果相同。简而言之，它们用于验证测试案例通过或失败的状态。

当我们运行要自动化的测试用例/场景时，找出通过或失败的场景对于了解自动化脚本的执行是否符合预期至关重要。

为此，我们必须提供某种断言，因此，在操作结束时，我们的代码将在JUnit或任何其他测试自动化框架中进行比较和断言，以评估我们得到的结果是否符合预期。

如果实际结果与预期结果相同，则可以将该断言标记为通过，如果不满足，则可以将该断言标记为失败。

当满足测试脚本中的所有断言时，仅将一个测试用例视为通过。可以使用JUnit框架的预定义方法来处理Selenium Java中的断言。

硒测试有2种主要的断言类型，即硬断言和软断言。

* 硬断言–如果断言条件与预期结果不匹配，当我们希望测试脚本立即停止时，将使用硬断言。由于断言条件未能达到预期的结果，因此将遇到断言错误，并且正在执行的测试用例将标记为“失败”。

* 软断言–即使不满足断言条件，测试脚本的执行也不会停止。同样，在软断言的情况下，当断言条件将无法满足预期结果时，也不会引发任何错误，并且测试脚本的执行将继续到下一个测试用例步骤。

话虽如此，现在该通过示例深入研究JUnit中的各种断言了。
# JUnit中用于硒测试的断言类型
JUnit中的声明方法由类“ org.junit.Assert ” 提供，该类扩展了“ java.lang.Object ”类。现在，我们将通过示例研究在JUnit中声明的不同方法。

## assertEquals（）

JUnit assertEquals（）方法将预期结果与实际结果的相等性进行比较。当我们提供的预期结果与执行操作后得到的Selenium测试脚本的实际结果不匹配时，它将引发断言错误。这导致在该行本身终止测试脚本的执行。

句法：


```
Assert.assertEquals(String expected, String actual);
Assert.assertEquals(String message, String expected, String actual);
```
这是一个JUnit assertEquals示例，可以帮助您更好地理解过程。


```
package com.test;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AssertionDemoClass {


    public static WebDriver driver;
    
    @BeforeClass
    public static void openBrowser()
    {
        System. setProperty("webdriver.chrome.driver", "D:\\AutomationPractice\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    
    @Test
    public void assertURL() {
        // TODO Auto-generated method stub
        driver.get("https://www.lambdatest.com/");
        
        String actualURL = driver.getCurrentUrl();
        System.out.println(actualURL);
        
        Assert.assertEquals("https://www.lambdatest.com/",actualURL);
        System.out.println("Test Passed");
    }
    
    @AfterClass
    public static void closeBrowser()
    {
        driver.close();
    }

}
```
在上面的代码中，我们可以看到我们在JUnit assertEquals（）方法中提供了两个参数，它们是预期结果和实际结果。如果实际URL的值与Selenium测试脚本中提到的预期URL不匹配，则将引发断言错误，并且程序的执行将在同一行（即断言语句本身）处终止。

我们还可以将断言错误消息作为参数传递，如语法所示。

### assertEquals为浮点声明

如果我们需要比较浮点类型（例如double或float），则在这种情况下，为了避免舍入错误，我们必须提供可以称为delta的其他参数。
增量值可以评估为：

`Math.abs（预期–实际）=增量`

如果由于四舍五入而导致期望值和实际值之间存在边际差异，则可以认为这些边际差异相同，并且断言应标记为合格。因此，用户给出的增量值决定了哪个裕度值应被认为可以通过该声明。

可以与float和double数据类型一起使用，请参考下面的语法

句法：

```
public static void assertEquals(float expected,float actual,float delta)
public static void assertEquals(double expected,double actual,double delta)
```
声明用于浮点声明的JUnit示例


```
@Test
    public void assertURL() {
        // TODO Auto-generated method stub      
        
        double actualDoubleValue= 2.999;
        double expectedDoubleValue = 3.000;
        
        Assert.assertEquals(expectedDoubleValue, actualDoubleValue, 0.001);
        
        System.out.println("Test Passed");      
        
    }
```

## assertTrue（）

如果您希望为方法中调用的特定条件将参数值传递为True，则可以使用.JUnit assertTrue（）。您可以在两种实际情况下使用JUnit assertTrue（）。

* 通过使用assertTrue方法将condition作为布尔值参数传递给JUnit进行断言。如果方法中给出的条件不是True，则抛出AssertionError（无消息）。
句法：

`Assert.assertTrue(boolean assertCondition);`

* 通过在assertTrue（）方法中同时传递两个参数。其中，一个参数用于断言错误消息，第二个参数用于指定需要应用断言方法为True的特定条件。如果方法中给定的条件不是True，则抛出AssertionError（带有消息）。
句法：

`Assert.assertTrue(String message, boolean assertCondition);`

让我们看一下assertTrue（）的assert JUnit示例Selenium测试脚本：


```
package com.assertions.junit 1;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AssertionDemo1 {

    public static WebDriver driver;
    
    @BeforeClass
    public static void openBrowser()
    {
        System. setProperty("webdriver.chrome.driver", "D:\\AutomationPractice\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    
    @Test
    public void assertURL() {
        driver.get("https://www.spicejet.com/");
        
        String actualURL = driver.getCurrentUrl();
        System.out.println(actualURL);
        
        String expectedURL="https://www.spicejet.com/";
        
        Assert.assertTrue("URL does not match",expectedURL.equals(actualURL));
        System.out.println("Test Passed");  
        
    }

    
    @AfterClass
    public static void closeBrowser()
    {
        driver.close();
    }
}
```

在上面的代码中，我们可以看到在assertTrue（）方法中提供了两个参数，分别是断言错误消息和布尔条件。如果条件不匹配或不成立，则将引发断言错误，并且程序的执行将在同一行（即断言语句本身）处终止。

如果我们不想提供断言错误消息，那么我们只需提供条件即可，如我们在上述语法中所见。

## assertFalse（）
与JUnit assertTrue相反，我们可以使用assertFalse（）方法来验证给定条件是否为False。您也可以通过两种方式声明JUnit assertFalse。

* 它以条件作为参数，需要对其施加断言。如果方法中给出的条件不为False，则会引发AssertionError（无消息）。
语法：
`Assert.assertFalse(boolean condition);`

* 与assertTrue相似，您也可以为assertFalse传递两个参数。一个确定断言错误消息，另一个确定应用assertFalse的条件。如果方法中给定的条件不为False，则会引发AssertionError（带有消息）。
句法：
`Assert.assertFalse(String message, boolean condition);`

让我们看一下assertFalse（）的一个断言JUnit示例Selenium测试脚本：


```
package com.assertions.junit 1;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AssertionDemo1 {

    public static WebDriver driver;
    
    @BeforeClass
    public static void openBrowser()
    {
        System. setProperty("webdriver.chrome.driver", "D:\\AutomationPractice\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    
    @Test
    public void assertURL() {
        // TODO Auto-generated method stub
        driver.get("https://www.spicejet.com/");
        
        String actualURL = driver.getCurrentUrl();
        System.out.println(actualURL);
        
        String expectedURL="https://www.google.com/";
        
        Assert.assertFalse("URL does match",expectedURL.equals(actualURL));
        System.out.println("Test Passed");          
    }
    
    @AfterClass
    public static void closeBrowser()
    {
        driver.close();
    }
}
```

在上面的Selenium测试脚本中，我们可以看到在assertFalse（）方法中提供了两个参数，分别是断言错误消息和布尔条件。如果条件确实匹配或不为假，则将引发断言错误，并且程序的执行将在同一行（即断言语句本身）处终止。

如果我们不想提供断言错误消息，那么我们只需提供条件即可，如我们在上述语法中所见。

## assertNull（）

为了验证传递的对象是否包含null值，我们使用assertNull（）方法，该方法有助于在对象不是null值的情况下显示断言错误。

句法：

```
Assert.assertNull(Object obj);
Assert.assertNull(String msg, Object obj);
```
让我们看一下用于JUnit assertNull（）的示例Selenium测试脚本：


```
package com.assertions.junit 1;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AssertionDemo1 {

    public static WebDriver driver;
    
    
    //To open the browser before running test
    @BeforeClass
    public static void openBrowser()
    {
        System. setProperty("webdriver.chrome.driver", "D:\\AutomationPractice\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    
    @Test
    public void assertURL() {
        
        //Redirect to the URL 
        driver.get("https://www.spicejet.com/");
        
        String actualURL = driver.getCurrentUrl();
        System.out.println(actualURL);
        
        String expectedURL=null;
        
        //Assertion     
        //Assert.assertNull(“Not Null”,actualURL);
        //OR
        Assert.assertNull(“Not Null”,expectedURL);

        System.out.println("Test Passed");  
        
    }
    
    //To close the browser after running test
    @AfterClass
    public static void closeBrowser()
    {
        driver.close();
    }

}
```
在上面的代码中，我们可以看到在assertNull（）方法中提供了两个参数，分别是断言错误消息和对象。如果提供的对象不为null，则将引发断言错误，并且程序的执行将在同一行（即断言语句本身）处终止。
如果我们不想提供断言错误消息，那么我们就可以提供一个对象，正如我们在上述语法中看到的那样。

## assertNotNull（）

assertNotNull（）方法检查提供的对象是否不包含空值。我们可以在此方法中将对象作为参数传递，如果传递的对象确实包含NULL值以及提供的断言错误消息，则将获得断言错误。

句法：

```
Assert.assertNotNull(Object obj);
Assert.assertNotNull(String msg, Object obj);
```
让我们来看一个assertNotNull（）的assert JUnit示例Selenium测试脚本：


```
package com.assertions.junit 1;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AssertionDemo1 {

    public static WebDriver driver;
    
    
    //To open the browser before running test
    @BeforeClass
    public static void openBrowser()
    {
        System. setProperty("webdriver.chrome.driver", "D:\\AutomationPractice\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    
    @Test
    public void assertURL() {
        
        //Redirect to the URL 
        driver.get("https://www.spicejet.com/");
        
        String actualURL = driver.getCurrentUrl();
        System.out.println(actualURL);
        
        String expectedURL="https://www.google.com/";
        
        //Assertion     
        Assert.assertNotNull("Null Object", actualURL);;
        System.out.println("Test Passed");  
        
    }
    
    //To close the browser after running test
    @AfterClass
    public static void closeBrowser()
    {
        driver.close();
    }

}
```

在上面的代码中，我们可以看到在assertNotNull（）方法中提供了两个参数，分别是断言错误消息和对象。如果提供的对象为null，则仅引发断言错误，并且程序的执行将在同一行（即断言语句本身）处终止。

如果我们不想提供断言错误消息，那么我们只需提供一个对象即可，正如我们在上述语法中看到的那样。

## assertSame（）

在执行Selenium测试时，您可能经常会遇到一种情况，您需要比较在方法中作为参数传递的两个不同对象，以评估它们是否引用了同一对象。在这里可以使用JUnit assertSame（）。如果两个对象未引用同一对象，则会显示断言错误。另外，如果提供了错误消息，则我们将收到断言错误消息，如以下语法所示。

Selenium测试脚本中JUnit assertSame（）的语法：


```
Assert.assertSame(Object expected, Object actual);
Assert.assertSame(String message, Object expected, Object actual);
```

## assertNotSame（）
assertNotSame（）方法验证是否作为参数传递的两个对象不相等。如果两个对象具有相同的引用，则将与我们提供的消息（如果有）一起引发断言错误。
此方法要注意的另一件事是，它比较对象的引用而不是这些对象的值。

Selenium测试脚本中JUnit assertNotSame（）的语法：


```
Assert.assertNotSame(Object expected, Object actual);
Assert.assertNotSame(String message, Object expected, Object actual);
```

## assertArrayEquals（）
assertArrayEquals（）方法验证我们作为参数传递的两个对象数组是否相等。如果两个对象数组的值都为null，则将它们视为相等。
如果我们在方法中作为参数传递的两个对象数组都不相等，则此方法将引发声明错误并提供消息。

Selenium测试脚本中JUnit assertArrayEquals（）的语法：

```
Assert.assertArrayEquals(Object[] expected, Object[] actual);
Assert.assertArrayEquals(String message, Object[] expected, Object[] actual);
```

## JUnit5与JUnit4之间的JUnit断言之间的差异

JUnit Jupiter附带了许多JUnit 4中已经存在的断言方法，并且它添加了更多断言方法，使其很适合与Java 8 Lambdas一起使用。在JUnit Jupiter中，断言是类中的静态方法org.junit.jupiter.api.Assertions

在Junit 4中，org.junit.Assert具有不同的断言方法来验证预期结果和结果。同样，我们可以为断言错误消息提供额外的参数，作为方法签名中的FIRST参数。您可以使用以下语法或上面讨论的每种方法来引用它们。

例：


```
public static void assertEquals(long expected, long actual);
public static void assertEquals(String message, long expected, long actual);
```
在JUnit 5中，org.junit.jupiter.Assertions包含大多数assert方法，包括其他assertThrows（）和assertAll（）方法。

JUnit 5断言方法还具有重载方法，以支持在测试失败的情况下传递要打印的错误消息

| Junit 4 | Junit 5 |
| ---- | ---- |
| 使用的类是'org.junit.Assert'	 | 使用的类是'org.junit.jupiter.api.Assertions' |
| 断言错误消息是第一个参数，尽管它是可选的 | 断言错误消息可以作为最后一个参数传递，它也是可选的 |
| 新方法：无 | 新方法：assertAll（）和assertThrows（） |

# 断言JUnit5的新方法

现在，我们已经清楚地了解了在JUnit5与JUnit 4中声明的方式的区别。我们现在将深入研究在JUnit5中声明的最新方法。

## assertAll（）

将执行新添加的方法assertAll（）来检查所有断言是否为分组断言。它有一个可选的标题参数，允许使用该方法assertAll（）来识别一组断言。在失败时，断言错误消息会显示有关该组中使用的每个字段断言的详细信息。

让我们来看一个带有断言的assertAll的assert JUnit示例：


```
package com.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AssertionDemoClass {

    public static WebDriver driver;
    
    @BeforeClass
    public static void openBrowser()
    {
        System. setProperty("webdriver.chrome.driver", "D:\\AutomationPractice\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    
    @Test
    public void assertURL() {
        // TODO Auto-generated method stub
        driver.get("https://www.spicejet.com/");
        
        
        String actualURL = driver.getCurrentUrl();
        System.out.println(actualURL);
        String expectedURL="https://www.spicejet.com/";
            
        String actualTitle =  driver.getTitle();
        System.out.println(actualTitle);
        String expectedTitle = "SpiceJet - Flight Booking for Domestic and International, Cheap Air Tickets";
        
        Assertions.assertAll( 
                () -> assertEquals(expectedURL, actualURL),
                () -> assertEquals(expectedTitle, actualTitle)
                );
        
        System.out.println("Test Passed");
    }
    
    @AfterClass
    public static void closeBrowser()
    {
        driver.close();
    }
}
```
## assertThrows（）

JUnit 5中另一个新添加的方法是替换JUnit 4中的ExpectedException Rule。现在，可以针对返回的Throwable类实例进行所有声明，这将使测试脚本更具可读性。作为可执行文件，我们可以使用lambda或方法引用。

# JUnit中的第三方断言

JUnit Jupiter为大多数测试方案提供了足够的断言功能，但是可能存在一些需要附加功能的方案，除了JUnit Jupiter所提供的功能之外，例如需要或需要匹配器。

对于这种情况，JUnit团队建议使用第三方断言库，例如Hamcrest，AssertJ，Truth等。用户可以在需要时使用这些第三方库。

仅就一个断言JUnit示例而言，为了使断言更具描述性和可读性，我们可以使用匹配器和流利的API的组合。

而JUnit Jupiter（JUnit 5）的org.junit.jupiter.api.Assertions类不提供方法assertThat（），该方法可在JUnit 4的org.junit.Assert类中使用，该方法接受Hamcrest Matcher。这样，您可以利用第三方断言库为匹配器提供的内置支持。

# 总结一下
如果您要通过Selenium测试执行自动化，则断言将扮演不可或缺的角色。它们通过评估通过Selenium测试脚本传递给对象的参数来帮助我们确定测试用例是否通过。

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

## 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [功能测试与非功能测试](https://mp.weixin.qq.com/s/oJ6PJs1zO0LOQSTRF6M6WA)
- [自动化和手动测试，保持平衡！](https://mp.weixin.qq.com/s/mMr_4C98W_FOkks2i2TiCg)
- [自动化测试生命周期](https://mp.weixin.qq.com/s/SH-vb2RagYQ3sfCY8QM5ew)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)

## 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
- [JVM的对象和堆](https://mp.weixin.qq.com/s/iNDpTz3gBK3By_bvUnrWOA)
- [JVM虚拟机面试大全](https://mp.weixin.qq.com/s/WPll-3ZvYrS7J7Cl8MuzhA)

![长按关注](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzBEASPySoVdOFmP12QUIWAQms664L0b82nic8BRIlufg0QibzXNnoibZp8yqhU9Pv0hXjKtqrGof8kMA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)