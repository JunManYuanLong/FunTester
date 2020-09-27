# Selenium Python使用技巧（三）

书接上文和上上文：
- [Selenium Python使用技巧（一）](https://mp.weixin.qq.com/s/39v8tXG3xig63d-ioEAi8Q)
- [Selenium Python使用技巧（二）](https://mp.weixin.qq.com/s/uDM3y9zoVjaRmJJJTNs6Vw)

## 处理不同情况的等待

在Selenium自动化测试中网页可能需要花费一些时间来加载，或者希望在触发测试代码之前可以看到页面上的特定Web元素。在这种情况下，需要执行“显式等待”，这是一段代码，通过它可以定义要发生的条件，然后再继续执行代码。

Selenium具有`WebDriverWait`，可以将其应用于任何具有条件和持续时间的Web元素。如果不存在执行等待的元素或发生超时，则可能引发异常。

在下面的示例中，我们等待`link_text=Sitemap`加载到页面上，并在`WebDriverWait`方法中指定了超时。如果在超时时间内未加载该元素，则抛出异常。


```Python
from selenium import webdriver
from selenium.common.exceptions import TimeoutException
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By
from pip._vendor.distlib import resources
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

driver = webdriver.Firefox()
driver.get("https://www.***.com/")
timeout = 10

try:
    element_present = EC.presence_of_element_located((By.LINK_TEXT, 'Sitemap'))
    WebDriverWait(driver, timeout).until(element_present)
except TimeoutException:
    print("查找用户超时！")
driver.quit()
```

## 网页中的滚动操作

在使用Selenium执行测试自动化时，您可能需要在页面上执行`上滚/下滚`操作的要求。您可以将`execute_script()`与`window.scrollTo(JS)`代码用作参数来实现相同的效果。在下面的示例中，加载被测网站后，我们滚动到页面的末尾。

```Python
from selenium import webdriver
from time import sleep

driver = webdriver.Firefox()
driver.get("https://www.***.com/")
timeout = 10

''' 滚动到页尾'''
driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")

sleep(10)

''' 滚动到页首'''
driver.execute_script("window.scroll(0, 0);")

sleep(10)
driver.quit()
```

## 使用Selenium放大和缩小

为了在进行Selenium自动化测试时放大或缩小，应使用`transform`CSS属性（适用于相应的浏览器），该属性可让您在页面上执行放大，缩小，旋转，倾斜等操作。不同类型的浏览器的CSS参数如下

![](http://pic.automancloud.com/Untitled32432.png)

在下面的示例中，我们将浏览器中加载的网页缩小200％，然后再放大100％（即恢复正常）。由于我们使用的是Firefox浏览器，因此我们使用了`MozTransform` CSS属性。


```Python
from selenium import webdriver
from time import sleep

driver = webdriver.Firefox()
driver.get("https://www.***.com/")
timeout = 10

''' 放大 200% '''
driver.execute_script('document.body.style.MozTransform = "scale(2.0)";')
driver.execute_script('document.body.style.MozTransformOrigin = "0 0";')

sleep(10)

''' 恢复 100% '''

driver.execute_script('document.body.style.MozTransform = "scale(1.0)";')
driver.execute_script('document.body.style.MozTransformOrigin = "0 0";')

sleep(10)

driver.quit()
```

## 查找元素的大小

必须首先通过ID搜索元素，然后使用`.size`属性来计算搜索到的元素的大小。在下面的示例中，我们在页面中计算按钮`create_programmatic_menu(ID = createDestoryButton)`的大小。


```Python
from selenium import webdriver
from time import sleep

driver = webdriver.Firefox()
driver.get("http://demos.***/test_Menu.html")
timeout = 10

search_element = driver.find_element_by_id("createDestroyButton")

print(search_element.size)

driver.quit()
```

## 获取元素的X和Y坐标

您必须遵循用于计算元素大小的类似方法。您必须首先通过ID搜索元素，然后使用`.location`属性来计算搜索到的元素的X和Y坐标。计算按钮`create_programmatic_menu(ID = createDestoryButton)`的X和Y坐标。


```Python
from selenium import webdriver
from time import sleep

driver = webdriver.Firefox()
driver.get("http://demos.***/test_Menu.html")
timeout = 10

search_element = driver.find_element_by_id("createDestroyButton")

print(search_element.location)

''' Release all the resources '''
driver.quit()
```

## 使用自定义配置文件禁用JavaScript

如果要禁用浏览器的JavaScript支持以验证自动跨浏览器与Selenium自动化测试的兼容性，则需要更改被测浏览器的配置文件设置（在本例中为Firefox），并将更改应用于配置文件。我们使用`DEFAULT_PREFERENCES ['frozen'] ['javascript.enabled'] = False`禁用浏览器的JavaScript支持。

执行代码后，您应该通过在地址栏中输入`about：config`并搜索`javascript.enabled`属性的值来验证配置文件的更改。

```Python
from selenium import webdriver

ff_profile = webdriver.FirefoxProfile()

ff_profile.DEFAULT_PREFERENCES['frozen']['javascript.enabled'] = False
ff_profile.set_preference("app.update.auto", False)
ff_profile.set_preference("app.update.enabled", False)

''' 更新配置 '''
ff_profile.update_preferences()

''' 加载配置文件 '''
driver = webdriver.Firefox(ff_profile)

''' 验证是否生效 '''
driver.get("about:config")
```

## 设置手动代理设置

在某些情况下，您可能需要更改代理设置才能执行测试。要更改代理设置，需要首先导入模块`selenium.webdriver.common.proxy`。您必须将代理类型设置为`MANUAL`，然后更改代理设置，然后将新设置应用到被测浏览器（在我们的示例中为Firefox）。需要用计划用于测试的IP地址和端口号替换`ip_address`和`port_number`。


```Python
from selenium import webdriver
from selenium.webdriver.common.proxy import Proxy, ProxyType

proxy_settings = Proxy()

''' 修改配置 '''
proxy_settings.proxy_type = ProxyType.MANUAL

proxy_settings.http_proxy = "ip_address:port_number"
proxy_settings.socks_proxy = "ip_address:port_number"
proxy_settings.ssl_proxy = "ip_address:port_number"

''' 添加证书 '''
capabilities = webdriver.DesiredCapabilities.FIREFOX
proxy_settings.add_to_capabilities(capabilities)

driver = webdriver.Firefox(desired_capabilities=capabilities)
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