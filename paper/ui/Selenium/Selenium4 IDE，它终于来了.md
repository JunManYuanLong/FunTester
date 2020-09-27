# Selenium4 IDE，它终于来了

> 新版的Selenium4 IDE，更强，更全，更好用。

在之前的文章我介绍了，Selenium4.0的更新路线，其中提到了Selenium IDE的发展。

- [Selenium 4 Java的最佳测试框架](https://mp.weixin.qq.com/s/MlNyv-kb03gRTcYllxUreA)
- [Selenium 4.0 Alpha更新日志](https://mp.weixin.qq.com/s/tU7sm-pcbpRNwDU9D3OVTQ)
- [Selenium 4.0 Alpha更新实践](https://mp.weixin.qq.com/s/yT9wpO5o5aWBUus494TIHw)
- [JUnit 5和Selenium基础（一）](https://mp.weixin.qq.com/s/ehBRf7st-OxeuvI_0yW3OQ)
- [JUnit 5和Selenium基础（二）](https://mp.weixin.qq.com/s/Gt82cPmS2iX-DhKXTXiy8g)
- [JUnit 5和Selenium基础（三）](https://mp.weixin.qq.com/s/8YkonXTYgAV5-pLs9yEAVw)

![](http://pic.automancloud.com/Selenium-4-IDE-TNG.png)

*Chrome*的**Selenium IDE**支持现已可用。可以从以下网址下载它：*https://selenium.dev/selenium-ide/*

![](http://pic.automancloud.com/QQ20191213-172646.png)

众所周知，**Selenium IDE**是一种记录和回放工具。现在它将具有以下更丰富和高级的功能：

* 新的插件系统。任何浏览器都可以轻松插入新的`Selenium4 IDE`。您将能够拥有自己的定位器策略和Selenium IDE插件。
新的`CLI`运行器。它将完全基于`NodeJS`，而不是基于`HTML`的旧运行器，并将具有以下功能：
* `WebDriver`播放。新的`Selenium4 IDE`运行程序将完全基于`WebDriver`。
* 并行执行。新的CLI运行器还将支持并行测试用例执行，并将提供有用的信息，例如花费的时间，通过和失败的测试用例。

# 主角Selenium4 IDE

早期的测试人员通常会避免使用**Selenium IDE**等记录和重播工具进行自动化测试，而宁愿选择使用`Selenium Webdriver`，`WebDriverIO`，`Cypress`等脚本框架。毕竟，为什么不使用这些工具，事实证明它们是更有效，对测试的帮助更多！

尽管**Selenium IDE**提供了易用性，但它仍然存在很多问题，它不支持跨浏览器测试，运行并行测试等等。这就是原因，当**Selenium IDE**的开发于2017年停止更新时，许多测试人员都对**Selenium IDE**死亡并不感到惋惜。

但是，随着**Selenium4 IDE**再次受到关注并且其使用率逐渐增加，这种情况最近有所改变。原因是**Selenium4 IDE**克服了以前的局限性，现在支持`跨浏览器测试`、`并行测试`、`脚本注入`、`debug调试`、`CI/CD`等等！

# Selenium IDE历史回顾

**Selenium IDE**于2006年推出，是一种用于开发*Selenium*测试用例的记录和重播工具。`Selenium IDE`易于上手，因为不需要任何特殊设置和基础。要开始使用**Selenium IDE**进行`Selenium`测试自动化，只需要为相应的浏览器安装扩展（或附加组件）。`Selenium IDE`提供了一个*GUI*，用于记录与网站的交互。

尽管**Selenium IDE**先前仅适用于*Firefox*，但现在也适用于*Chrome*。**Selenium IDE**的最新版本是*3.17.0*。可以使用下面的链接下载Selenium IDE的Chrome和Firefox扩展。

* Selenium IDE的Chrome扩展：https://chrome.google.com/webstore/detail/selenium-ide/mooikfkahbdckldjjndioackbalphokd?hl=en
* Selenium IDE的Firefox插件：https://addons.mozilla.org/en-US/firefox/addon/selenium-ide/

安装扩展后，就可以开始记录测试。与网站的交互被记录并归类为以下几类：命令、目标对象、值、说明（可选）。

![欢迎页](http://pic.automancloud.com/WX20200830-102817@2x.png)

![主页面](http://pic.automancloud.com/WX20200830-104414@2x.png)

## Selenium4 IDE的主要功能

**Selenium4 IDE**是一个易于使用的工具，可提供即时反馈，并可以使用**Selenium IDE**的`SIDE文件`（或命令行）运行器在任何浏览器+操作系统组合上运行测试。

以下是一些关键功能：

* 执行测试（和测试套件）：*Selenium4 IDE*可以执行特定的测试或包含一组测试的完整测试套件。
* 跨浏览器测试：它可以用于自动浏览器测试，因为可以使用`SIDE文件`运行器执行跨浏览器测试。
* 调试：设置断点并暂停异常，可以很容易地从`IDE`本身调试测试。
* 丰富的命令集：`Selenium4 IDE`支持许多命令，这些命令可用于处理断言、插入脚本、创建循环等。
* 逻辑控制：**Selenium4 IDE**具有广泛的命令集，可启用控制流结构。`if`、`if..else`、`while`等命令有助于从`IDE`执行条件判断的测试。
* 测试用例重用：运行命令使您可以在另一个测试用例（或测试套件）中重用特定的测试用例。
* `Selenese`命令的分组：**Selenium4 IDE**支持`Selenese`命令（或`Selenium`命令集）的分组，可以运行测试，一系列`Selenese`命令构成一个测试脚本。
* 轻松修改和比较测试用例–使用**Selenium4 IDE**创建的测试脚本存储为`JSON`文件。这样可以轻松比较，修改和检查测试。

下面显示了我们的**Selenium4 IDE**教程录制了一个使用必应搜索**FunTester**的教程，随手点击一个链接进去。

```json

{
  "id": "ab514091-68ca-4b4c-a232-587a0b82fcaa",
  "version": "2.0",
  "name": "FunTester",
  "url": "https://cn.bing.com",
  "tests": [{
    "id": "e34fd206-9ab8-4a6c-a543-0f80d1ba3fe9",
    "name": "FunTester",
    "commands": [{
      "id": "92e8a427-e37e-4c6b-88c4-718d4e6c606e",
      "comment": "",
      "command": "open",
      "target": "/",
      "targets": [],
      "value": ""
    }, {
      "id": "780adf28-4694-4c22-bfd1-74e09bf80775",
      "comment": "",
      "command": "setWindowSize",
      "target": "1680x1027",
      "targets": [],
      "value": ""
    }, {
      "id": "2d8284e8-8475-4d75-9740-b407d6317b95",
      "comment": "",
      "command": "click",
      "target": "id=sb_form_q",
      "targets": [
        ["id=sb_form_q", "id"],
        ["name=q", "name"],
        ["css=#sb_form_q", "css:finder"],
        ["xpath=//input[@id='sb_form_q']", "xpath:attributes"],
        ["xpath=//form[@id='sb_form']/div/input", "xpath:idRelative"],
        ["xpath=//input", "xpath:position"]
      ],
      "value": ""
    }, {
      "id": "ddd0e97d-6474-4a3e-a260-02f3a573f84f",
      "comment": "",
      "command": "type",
      "target": "id=sb_form_q",
      "targets": [
        ["id=sb_form_q", "id"],
        ["name=q", "name"],
        ["css=#sb_form_q", "css:finder"],
        ["xpath=//input[@id='sb_form_q']", "xpath:attributes"],
        ["xpath=//form[@id='sb_form']/div/input", "xpath:idRelative"],
        ["xpath=//input", "xpath:position"]
      ],
      "value": "FunTester"
    }, {
      "id": "959e1353-de22-4997-9b26-3ed02a55d3d6",
      "comment": "",
      "command": "sendKeys",
      "target": "id=sb_form_q",
      "targets": [
        ["id=sb_form_q", "id"],
        ["name=q", "name"],
        ["css=#sb_form_q", "css:finder"],
        ["xpath=//input[@id='sb_form_q']", "xpath:attributes"],
        ["xpath=//form[@id='sb_form']/div/input", "xpath:idRelative"],
        ["xpath=//input", "xpath:position"]
      ],
      "value": "${KEY_ENTER}"
    }, {
      "id": "d964cc49-1d76-41e2-b2aa-e0ad34da2999",
      "comment": "",
      "command": "click",
      "target": "linkText=FunTester - 知乎",
      "targets": [
        ["linkText=FunTester - 知乎", "linkText"],
        ["css=.b_algo:nth-child(5) > h2 > a", "css:finder"],
        ["xpath=//a[contains(text(),'FunTester - 知乎')]", "xpath:link"],
        ["xpath=//ol[@id='b_results']/li[5]/h2/a", "xpath:idRelative"],
        ["xpath=//a[contains(@href, 'https://www.zhihu.com/people/ba-yin-xian/posts')]", "xpath:href"],
        ["xpath=//li[5]/h2/a", "xpath:position"],
        ["xpath=//a[contains(.,'FunTester - 知乎')]", "xpath:innerText"]
      ],
      "value": "",
      "opensWindow": true,
      "windowHandleName": "win2584",
      "windowTimeout": 2000
    }]
  }],
  "suites": [{
    "id": "5915a8a1-cce3-42f9-ad68-5022d7fcfc1f",
    "name": "Default Suite",
    "persistSession": false,
    "parallel": false,
    "timeout": 300,
    "tests": ["e34fd206-9ab8-4a6c-a543-0f80d1ba3fe9"]
  }],
  "urls": ["https://cn.bing.com/"],
  "plugins": []
}
```


对于需要进行自动浏览器测试的测试人员，**Selenium4 IDE**是一个非常值得探索的选择。

关于**Selenium4 IDE**的新特性，如果各位有需求的话，我会逐个更新简单的入门讲解，由于本人现在主要从事服务端的测试工作，对于**Selenium4 IDE**后面的高阶功能，如：并行测试、分布式兼容性测试等等略显有心无力，可能更新会比较慢。

--- 
* 公众号**FunTester**首发，更多原创文章：[450+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [开源测试服务](https://mp.weixin.qq.com/s/ZOs0cp_vt6_iiundHaKk4g)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)
- [测试开发工程师工作技巧](https://mp.weixin.qq.com/s/TvrUCisja5Zbq-NIwy_2fQ)
- [Fiddler Everywhere工具答疑](https://mp.weixin.qq.com/s/2peWMJ-rgDlVjs3STNeS1Q)
- [Selenium4 IDE，它终于来了](https://mp.weixin.qq.com/s/XNotlZvFpmBmBQy1pYifOw)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)