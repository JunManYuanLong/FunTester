# Selenium 4.0 Alpha更新实践

上期讲到了[Selenium 4.0 Alpha更新日志](https://mp.weixin.qq.com/s/tU7sm-pcbpRNwDU9D3OVTQ)，这次来实践一下更新内容。

* DevTools操作更加方便。
* Chrome录制更加方便。（未实践）
* 窗口管理不在依赖一个driver。
* 相对定位器功能丰富。
* 补充全屏快照的功能。

## ChromiumDriver和DevTools：

在Selenium 3中，EdgeDriver和ChromeDriver具有从RemoteWebDriver类继承的自己的实现。在Selenium 4中，Chromedriver和EdgeDriver继承自ChromiumDriver。ChromiumDriver类具有预定义的方法来访问开发工具。考虑下面的代码片段

<!--![](http://pic.automancloud.com/ChromeDriver-Script.png)-->

```
       //创建驱动drive
        WebDriver driver = new ChromeDriver();
        Connection connection = null;
        DevTools devtools = new DevTools(connection);
        devtools.createSession();
        String message = "chrome 浏览器测试Demo!";
        driver.get("https:www.bing.cn");
        //JS打印信息
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("console.log('" + message + " ' ) ;");
```
上面的代码创建到给定URL的会话，并执行javascript打印消息。DevTools是一个类，具有用于获取开发人员选项的方法的类。

DevTools还可以用于性能评估并获取页面加载时间。

## 更好的窗口和标签管理

Selenium 4现在具有可以同时在两个不同的窗口上工作的功能。当我们要导航到新窗口（或选项卡）并在那里打开另一个URL并执行某些操作时，此功能特别有用。

<!--![](http://pic.automancloud.com/Tab-Management-Script.png)-->


```
        WebDriver window2 = driver.switchTo().newWindow(WindowType.TAB);
        window2.get("url");
        WebDriver window1 = driver.switchTo().newWindow(WindowType.WINDOW);
        window1.get("url");
```

`newWindow()`方法根据其参数中指定的WindowType打开一个新窗口或选项卡。

## 相对定位器
在Selenium 4 alpha版本中，我们还可以获得相对于任何其他定位器的定位器。

`toLeftOf()`：位于指定元素左侧的元素。
`toRightOf()`：位于指定元素右侧的元素。
`above()`：相对于指定元素位于上方的元素。
`below()`：相对于指定元素位于下方的元素。
`near()`：元素距离指定元素最多50个像素。像素值可以修改。

## 全屏快照

现在，我们可以在Firefox中使用`getFullPageScreenshotAs()`方法获取完整的屏幕截图。但是，我们无需将其类型转换为`TakesScreenshot`界面，而是需要将其类型转换为FirefoxDriver实例。

`File src = ((FirefoxDriver) driver).getFullPageScreenshotAs(OutputType.FILE);`

可能会有一些更有趣的功能和改进，因此继续探索！

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