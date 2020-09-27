# Selenium处理下拉列表

[原文地址](https://www.lambdatest.com/blog/webdriverio-tutorial-for-handling-dropdown-in-selenium/)

在执行`Selenium`自动浏览器测试时，很多时候需要处理下拉菜单。下拉菜单通常用于表单中，在节省空间和防止用户在表单中选择错误的选项时非常有用。因此在测试任何网站或访问表单时，如何使用`Selenium`处理下拉列表显得尤为重要。

为了对下拉菜单执行操作，可以在`Selenium WebdriverIO`中使用`Select`类。在本文中，演示如何使用`Select`来处理下拉菜单。

# 下拉菜单的不同类型

通常会在网站上找到两种主要的下拉菜单。

* 正常下拉菜单
* 自定义下拉菜单

正常的下拉菜单是我们在`Selenium`中处理访问表单时经常遇到的下拉菜单。识别正常的下拉菜单很容易，只需在浏览器中打开`element`标签，然后查看该下拉`HTML`标签即可。HTML标记应为`<selcet>`，`id`应为`dropdown`。

## 正常下拉列表

```HTML
<select id="dropdown">
    <option value="" disabled="disabled" selected="selected">Please select an option</option>
    <option value="1">Option 1</option>
    <option value="2">Option 2</option>
  </select>
```

## 自定义下拉菜单

由于使用`<selcet>`的样式选项不多，因此开发人员可以使用自定义下拉菜单。正如我们所讨论的，自定义下拉列表不是使用`<selcet>`标记开发的，而是使用`<div>`标记或基于前端框架的其他一些自定义标记开发的。


```HTML
<div class="fsw_inputBox travelFor inactiveWidget ">
   <label data-cy="travellingFor" for="travelFor">
      <span class="lbl_input latoBold appendBottom10">Travelling For</span><input data-cy="travelFor" id="travelFor" type="text" class="hsw_inputField font20" readonly="" value="">
      <div class="code latoBold font14 blackText makeRelative">
         <p></p>
         <p class="font14 greyText">Select a Reason (optional)</p>
      </div>
   </label>
</div>
```

现在，了解了这两个下拉菜单之间的区别。在`Selenium`测试自动化中，自定义下拉列表是根据开发人员定义的事件进行处理的，而常规下拉列表则由称为`Select`类的特殊`Selenium`类对象进行处理。

# 处理下拉菜单

处理`WebDriverIO`中的下拉菜单非常简单！没有像`Java`或任何其他编程语言这样的单独的类对象。在这里，`WebDriverIO`下拉列表也可以通过简单的选择器访问。

在正常下拉菜单中使用给定的HTML示例，您可以使用以下使用ID选择器的语法查找下拉菜单对象。

`Const drp = $("#dropdown");`

下拉菜单有两个选项。

* 单值下拉
* 多值下拉

访问单个或多个值下拉菜单没有区别，只是多个值下拉菜单允许用户从下拉选项中选择多个值。

WebDriverIO在下拉菜单上提供以下操作。

* `selectByIndex()`
* `selectByVisibleText()`
* `selectByAttribute()`

## selectByIndex

可以通过提供值的索引来选择值的下拉列表。索引不过是下拉值的位置。索引始终从**0**开始。因此，第一个值被视为第**0**个索引。

句法：

`$("selector").selectByIndex(index)`

如果要选择选项**1**，则可以使用以下代码。

`$("#dropdown").selectByIndex(0)`


注意：当下拉列表值随着值索引的频繁变化而动态变化时，避免使用`selectByIndex()`。

## selectByVisibleText

另一个选项是`selectByVisibleText()`。使用此选项非常安全，因为我们需要使用下拉值中显示的下拉可见文本。

我们可以使用选项1或选项2作为选择

句法：

`$("Selector").selectByVisibleText(text)`

如果要使用`selectByVisibleText()`选择选项2，则使用下面的代码；

`$("#dropdown").selectByVisibleText("Option 2")`

注意：使用`selectByVisibleText()`时，请保持可见文本不变，否则该元素将无法识别。

## selectByAttribute

与其他用于`Selenium`测试自动化的框架相比，`selectByAttribute()`是非常灵活的东西。通常，在其他`Selenium`测试自动化框架中，您将使用`selectByValue()`选项，该选项允许用户仅使用`value`属性选择下拉列表。但是，`WebDriverIO`提供了使用任何属性的功能，并且其值存在于下拉列表中。

句法：

`$("Selector").selectByAttribute(attribute, value)`

在这里，属性参数可以是<option>标记中的任何属性，而value参数是所提供属性的值。

`$("#dropdown").selectByAttribute("value", "1")`

如果考虑普通的`HTML`下拉代码，则只能看到一个`value`属性。如果提供了任何其他属性，那么也可以使用它。

## 多值下拉

如果您看到`<select>`标签具有`multiple="true"`属性，则此下拉列表具有选择多个选项的功能。当您使多个值下拉列表自动化时，必须多次调用上述方法。当然也可以自定义方法实现这些功能，很可能需要借助`JavaScript`，这个有机会再讲​。​

--- 
* 公众号**FunTester**首发，更多原创文章：[FunTester420+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/7VG7gHx7FUvsuNtBTJpjWA)
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)
- [有关UI测试计划](https://mp.weixin.qq.com/s/D0fMXwJF754a7Mr5ARY5tQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)