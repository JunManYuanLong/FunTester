# Selenium Python使用技巧（二）

书接上文：[Selenium Python使用技巧（一）](https://mp.weixin.qq.com/s/39v8tXG3xig63d-ioEAi8Q)。


## 进行自动跨浏览器测试

您可能需要在多种情况下针对不同的浏览器（例如Firefox，Chrome，Internet Explorer，Edge）测试代码。跨不同浏览器测试网站的做法称为自动浏览器测试。要使用Selenium自动化测试执行自动浏览器测试，您应该在单元测试代码或`pytest`代码中合并对这些浏览器的选择性处理。下面显示了一个代码片段（利用`pytest`）来处理多个浏览器：


```Python
''' 导入必要的包和类 '''

import pytest
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.keys import Keys
from time import sleep

@pytest.fixture(params=["chrome", "firefox"],scope="class")
def driver_init(request):
    if request.param == "chrome":
        # 搞点事情
    if request.param == "firefox":
        # 搞定事情
    yield
    web_driver.close()
    ...........
    ...........
```

## 使用CSS定位器

使用Selenium执行测试自动化时，在页面上定位Web元素是自动化脚本的基础。如果您想基于特定种类的Web元素（如Tag，Class，ID等）的存在来执行条件执行，则可以使用find_elements _ *** API。下面提到其中一些

* find_elements_by_class_name()：按类名称查找元素
* find_elements()：按策略和定位器查找元素
* find_element_by_link_text()：通过链接文本查找元素
* find_element_by_partial_link_text()：通过链接文本的部分匹配来查找元素


下面显示的是`find_element_by_partial_link_text()`和`find_elements_by_class_name()`的用法，其中在受测试的URL页面上搜索了元素。

```Python
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.keys import Keys
from time import sleep
from selenium.common.exceptions import NoSuchElementException

driver = webdriver.Firefox()
driver.get("https://www.***.com")

try:
    element = driver.find_element_by_partial_link_text("START TESTING")
    print("元素找到了")
    element = driver.find_elements_by_class_name('home-btn-2')
    print(“按钮找到了”)
except NoSuchElementException:
    print("元素没找到")
    
sleep(10)
driver.close()
```

## WebElement的HTML源代码

`innerHTML`属性可用于捕获`WebPage`的源代码。自页面首次由网络浏览器加载以来，`innerHTML`还用于检查页面中的任何更改。您可以将整个源代码编写为`.html`文件，以备将来参考。


```Python
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.keys import Keys
from time import sleep
import io

driver = webdriver.Firefox()
driver.get("https://www.***.com")

elem = driver.find_element_by_xpath("//*")
source_code = elem.get_attribute("innerHTML")

filename = open('page_source.html', 'w')
filename.write(source_code)
filename.close()
    
sleep(10)

driver.close()
```

## 鼠标悬停

在某些情况下，您可能需要单击作为菜单一部分的项目或作为多级菜单一部分的项目。首先，我们找到菜单项，然后在所需的菜单项上执行单击操作。

在下面的示例中，在导航到主页上的“Automation”选项卡。第一个任务是Menu中找到某个元素 。通过使用检查工具，我们可以获得正确的`element-id`，详细信息如快照中所示：

我们使用`move_to_element()`操作移动到菜单，该操作是`action_chains`模块的一部分。下一个任务是找到包含文本`Automation`的菜单项，我们将使用`find_element_by_xpath(“//a[contains(text()，'Automation')]”)))`进行单击操作。


```Python
from selenium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
from time import sleep
    
driver = webdriver.Firefox()
driver.get("https://www.***.com")

action = ActionChains(driver);

parent_level_menu = driver.find_element_by_id("bs-example-navbar-collapse-1")
action.move_to_element(parent_level_menu).perform()

child_level_menu = driver.find_element_by_xpath("//a[contains(text(),'Automation')]")
child_level_menu.click();

sleep(10)

driver.close()
```
## 关闭标签而不是浏览器

对于任何测试自动化Selenium脚本，最基本但必不可少的技巧之一是实现如何在不关闭整个浏览器的情况下关闭选项卡。`driver.close()`关闭当前选项卡，`driver.quit()`将关闭（浏览器的）所有选项卡，并退出驱动程序。如果需要保持浏览器窗口打开（并退出所有其他选项卡），则可以使用`switch_to.window()`方法，该方法的输入参数为`window handle-id`。

* 注：还有其他方法可以解决此问题。`window.open()`方法可以与适当的选项一起使用（例如，打开新窗口，打开新选项卡等）。可以使用使用`send_keys()`发送正确的组合键，但是该行为取决于`geckodriver`版本（对于Firefox），`chromedriver`版本等。因此，`send_keys()`方法不是可取的，因为输出会根据WebDriver版本而有所不同。

在下面的示例中，我们打开一个包含测试URL的新窗口，然后关闭其他窗口。我们仅使用`window_handles`来达到要求。


```Python
from selenium import webdriver
import time
 
driver = webdriver.Firefox()
driver.get('https://www.google.com')
# 打开新窗口
driver.execute_script("window.open('');")
time.sleep(5)
# 切换窗口
driver.switch_to.window(driver.window_handles[1])
driver.get("https://***.com")
time.sleep(5)
# 关闭
driver.close()
time.sleep(5)
# 切换回旧窗口
driver.switch_to.window(driver.window_handles[0])
driver.get("https://www.***.com")
time.sleep(5)
# 关闭窗口
#driver.close()
```

## 处理下拉菜单

有一个需求，必须从网页上的下拉菜单中选择一个特定的选项。您可以通过多种方式从下拉菜单中选择所需的选项。

* select_by_index(期望的索引值)
* select_by_visible_text(“文本信息”)
* select_by_value(值)

我们从下拉菜单中选择所需元素之前，获取被测元素的ID非常重要。我们使用`find_element_by_xpath()`方法来定位该元素，并且一旦找到该元素（使用ID），便从下拉菜单中选择该值。

在下面的示例中，我们显示了可以从菜单中选择元素的不同方法（`@ aria-label ='select'`）


```Python
from selenium import webdriver
from selenium.webdriver.support.ui import Select
from time import sleep
from selenium.common.exceptions import NoSuchElementException
from pip._vendor.distlib import resources

driver = webdriver.Firefox()
driver.get("http://demos.*****/test_Menu.html")

sleep(5)

try:
    select_element = Select(driver.find_element_by_xpath("//select[@aria-label='select']"))
    select_element.select_by_visible_text("bleed through")
    sleep(5)
    select_element.select_by_index(0)
    sleep(5)
except NoSuchElementException:
    print("元素查找失败")

sleep(5)

driver.quit()
```

## 复选框处理

复选框是网页中的常见元素，用于您必须从多个选项中仅选择一个选项的情况下。像下拉菜单处理一样，我们使用`find_element_by_xpath()`方法找到所需的复选框，一旦找到该复选框，就会执行单击操作。

我们将使用Selenium自动化测试，并且选中的复选框。使用`driver.find_elements_by_xpath(“//*[contains(text()，'文本')]”)`完成操作。


```Python
from selenium import webdriver
from selenium.webdriver.support.ui import Select
from time import sleep
from selenium.common.exceptions import NoSuchElementException
from pip._vendor.distlib import resources

driver = webdriver.Firefox()
driver.get("http://demos.***test_CheckBox.html")

sleep(5)

try:
    driver.find_element_by_xpath("//*[contains(text(), 'cb7: normal checkbox')]").click()
except NoSuchElementException:
    print("元素查找失败")

sleep(5)

driver.quit()
```

## 通过CSS选择器选择元素

在使用Selenium执行测试自动化时，可以使用CSS定位器来定位网页上的元素。`find_elements_by_css_selector()`可以用于定位必须将要定位的元素详细信息（标签，链接，ID等）作为输入参数传递的元素。它通过`CSS Selector`在该元素的子元素中找到元素列表。

目的是使用`find_elements_by_css_selector()`在https://***.com/上找到“登录”按钮并执行单击操作。与登录相关的代码如下。代码检查工具快照还提供了所需的信息。


```HTML
<html>
........
<li class="login">
<a href="https://accounts.***.com/register">Free Sign Up</a>
</li>
.....
</html>
```

因此，我们将`li.login`作为参数传递给`find_elements_by_css_selector()`，一旦找到元素，就执行`Click`操作。


```Python
from selenium import webdriver
from selenium.webdriver.support.ui import Select
from time import sleep
from selenium.common.exceptions import NoSuchElementException
from pip._vendor.distlib import resources

driver = webdriver.Firefox()
driver.get("https://www.***.com/")

sleep(5)

try:
    driver.find_element_by_css_selector("li.login").click()
except NoSuchElementException:
    print("Element not found")

sleep(5)

driver.quit()
```


---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
 - [Selenium 4.0 Alpha更新日志](https://mp.weixin.qq.com/s/tU7sm-pcbpRNwDU9D3OVTQ)
- [Selenium 4.0 Alpha更新实践](https://mp.weixin.qq.com/s/yT9wpO5o5aWBUus494TIHw)
- [如何统一接口测试的功能、自动化和性能测试用例](https://mp.weixin.qq.com/s/1xqtXNVw7BdUa03nVcsMTg)

## 非技术文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)
- [API测试基础](https://mp.weixin.qq.com/s/bkbUEa9CF21xMYSlhPcULw)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)