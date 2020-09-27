# Selenium Python使用技巧（一）

使用Selenium进行测试自动化已使全球的网站测试人员能够轻松执行自动化的网站测试。Webdriver是Selenium框架的核心组件，您可以使用它执行自动跨浏览器测试针对不同类型的浏览器（例如Google Chrome，Mozilla Firefox，Safari，Opera，Internet Explorer，Microsoft Edge等）访问您的网站或Web应用程序。与其他Web自动化工具/框架相比，使用Selenium Webdriver执行测试自动化的主要优势是支持多种编程语言，例如Python，Java，C，Ruby，PHP，JavaScript，.Net，Perl，Groovy等。

想系统学习的可以找一找Selenium WebDriver自动化跨浏览器测试教程，在此我们讨论Selenium的基本功能及如何将该框架与流行的编程语言（Python）一起使用。在本文中，我将与您分享一些Selenium自动化测试的关键技巧，这些技巧涉及代码优化，性能改进，动态网页加载，处理CSS和HTML代码等方面。

* 这些用于Selenium WebDriver的自动化测试的编码技巧中的大多数都是通用的，并且可以与开发测试脚本所使用的编程语言通用。

## 设置Selenium Webdriver路径

为了与浏览器进行通信，需要首先从其官方网站下载相应的插件驱动webdriver 。该插件将负责与浏览器进行通信，并且该插件应存在于正在开发测试的计算机上。webdriver路径必须在Selenium Webdriver配置中设置。

尽管可以将插件Webdriver放置在任何位置，也可以在Selenium Webdriver配置中提供静态/相对路径，但是这种方法容易出错，并且配置跟踪文件路径（路径会收到当前系统的影响）。更可靠的方法是将相应的Selenium Webdriver放置在驱动程序可执行文件所在的位置，在这种情况下，无需在Selenium Webdriver配置中指定可执行文件路径。


如果`geckodriver`在浏览器启动程序所在目录中不存在，则需要在源代码中手动添加相同的路径。我们导入`selenium.webdriver.firefox.firefox_binary`模块以提供Firefox可执行文件的路径。


```Python
from selenium import webdriver
from selenium.webdriver.firefox.firefox_binary import FirefoxBinary

ff_binary = FirefoxBinary('path/to/gecko driver')
browser = webdriver.Firefox(firefox_binary=ff_binary)
```

如下面的代码片段所示，由于火狐驱动程序（Firefox Webdriver）放置在与Firefox浏览器相同的位置，因此我们未指定其位置。与前一种方法相比，这是一种更可靠的方法，可以帮助减少使用Selenium实现测试自动化时的基本错误。

```Python
''' 导入必要的包和类 '''
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from time import sleep

'''创建 Firefox 驱动 '''
driver = webdriver.Firefox()
driver.get("https://www.****.com/")
```

## 捕获测试自动化的屏幕截图

在执行测试时，经常会遇到一些特殊的验证需求，其中必须捕获屏幕快照以验证测试结果。Selenium WebDriver提供了三种API，可以通过它们获取网页的屏幕截图。

* save_screenshot('保存屏幕快照的路径/filename.png')
* get_screenshot_as_file('保存屏幕快照的路径/filename.png')
* get_screenshot_as_png()

前两个API可让您将当前窗口的屏幕保存为`.png`文件。如果存在`IOError`，则API返回`False`，否则返回`True`。仅当文件扩展名为`.png`时，这些API才有效，否则Python会引发错误并且保存的内容可能无法查看。如果您希望以二进制格式捕获当前窗口的屏幕，请使用`get_screenshot_as_png()`API。


```Python
''' 导入必要的包和类 '''
from selenium import webdriver
import StringIO
from PIL import Image

'''创建 Firefox 驱动 '''
driver = webdriver.Firefox()
driver.get("https://www.***.com/")

'''截图，保存在当前工作目录下 '''

driver.save_screenshot('screenshot_1.png');

driver.get_screenshot_as_file('screenshot_2.png');

screenshot = driver.get_screenshot_as_png();
    
screenshot_size = (20, 10, 480, 600)
image = Image.open (StringIO.StringIO(screen))
region = image.crop(screenshot_size)
region.save('screenshot_3.jpg', 'JPEG', optimize=True)
```

## 自动化测试时刷新网页

在某些情况下，可能需要刷新网页或者强制刷新，尤其是在等待特定条件时。使用Selenium Webdriver执行测试自动化时，有多种方法可以刷新网页，下面列出了一种流行的方法。

### driver.refresh()方法
顾名思义，`refresh()`方法用于刷新网页。因此，它本质上是异步的；您应该将此API与`document.readyState()`结合使用。


```Python
''' 导入必要的包和类 '''
from selenium import webdriver

'''创建 Firefox 驱动 '''
driver = webdriver.Firefox()
driver.get("https://www.***.com/")
driver.refresh()
```
### ActionChains()方法
`ActionChains()`是自动化与Selenium进行自动化测试的低级交互的另一种方式，例如按键，鼠标按钮动作等。为了刷新网页，我们使用了`CTRL + F5`组合。

```Python
import time
from selenium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.keys import Keys

'''创建 Firefox 驱动 '''
# driver = webdriver.Chrome()
driver = webdriver.Firefox()
driver.get("https://www.****.com/")

time.sleep(5)

print("开始刷新！")

ActionChains(driver) 
    .key_down(Keys.CONTROL) 
    .send_keys(Keys.F5) 
    .key_up(Keys.CONTROL) 
    .perform()

print("刷新完成！")

sleep(5)
driver.quit()
```

## 在新标签页中打开网页

`execute_script()`可用于在当前窗口/框架中同步执行JavaScript代码。将打开网页的参数（JavaScript）作为参数传递给`execute_script()`

```Python
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from time import sleep

driver = webdriver.Firefox()
driver.get("http://www.***.com/")

driver.implicitly_wait(10)

#打开新标签
driver.execute_script("window.open('https://www.***.com', 'new tab')")

sleep(5)
driver.quit()
```

## 保存网页截屏

在某些情况下，使用Selenium执行测试自动化时，可能需要截取网页的部分屏幕截图。在这种情况下，您可以使用`pillow`模块。需要先使用以下命令安装`Pillow/PIL`模块（注意权限）：

`pip install pillow`

使用`get_screenshot_as_png()`API 拍摄整个网页的屏幕截图。截图准备好后，将使用PIL库在内存中打开捕获的图像，然后裁剪图像（包含整个网页的屏幕截图）以获取结果图像。


```Python
from selenium import webdriver
''' 导入 Pillow module '''
from PIL import Image
from io import BytesIO

driver = webdriver.Firefox()
driver.get('http://***.com/')

# 用检查器发现元素id
element = driver.find_element_by_id('hplogo')
image_location = element.location
size = element.size

png = driver.get_screenshot_as_png()

''' 截图完成，退出浏览器'''
driver.quit()

''' 将图片读入内存中 '''
crop_image = Image.open(BytesIO(png))

''' 获取参数配置 ''' 

left = image_location['x']
top = image_location['y']
right = image_location['x'] + size['width']
bottom = image_location['y'] + size['height']

crop_image = crop_image.crop((left, top, right, bottom))
crop_image.save('logo-screenshot.png')
```

## 执行JavaScript代码

当使用Selenium WebDriver执行测试自动化时，`execute_script()`用于执行JavaScript代码。语法为`driver.execute_script(“js code”)`。

如下例所示，找到`classname`是`home-cta`的元素执行的`on_click()`操作。


```Python
from selenium import webdriver
from time import sleep

driver = webdriver.Firefox()
driver.get("https://www.***.com")

driver.execute_script("document.getElementsByClassName('home-cta')[0].click()")

sleep(10)

driver.close()
```

## 提取JavaScript代码的执行结果

调用JavaScript代码以使用Selenium进行自动化测试后，您需要提取这些JavaScript代码的结果。您可以使用`return`关键字来获取JavaScript代码的结果，如我们在解释JavaScript的扩展示例中所示。


```Python
from selenium import webdriver
from time import sleep

driver = webdriver.Firefox()
driver.get("https://www.***.com")

driver.execute_script("document.getElementsByClassName('home-cta')[0].click()")
    
result = driver.execute_script("return 0")

print(result)

sleep(10)

driver.close()
```

未完待续……

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