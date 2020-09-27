# Selenium自动化常见问题

`Selenium`是最流行的web端自动化测试框架之一，用于自动执行用户对被测产品的操作。`Selenium`是开源的，`Selenium`框架的核心组件是`Selenium WebDriver`。 `Selenium WebDriver`允许使用者在不同的浏览器（例如Chrome，Firefox，Internet Explorer，Microsoft Edge等）上执行测试用例。使用`Selenium WebDriver`的主要优点是它支持`.NET`，`Java`，`C＃`，`Python`等。可以参考有关`Selenium WebDriver`体系结构的官方文档以了解更多信息。

尽管`Selenium`简化了Web网站或`Web`应用程序的测试，但测试开发人员在使用框架时面临着许多`Selenium`自动化挑战。让我们看一下`Selenium Automation`中面临的一些最常见挑战及其比较不错的解决方案。

# 误报成功和误报失败

误报成功也是测试结果成功的一种情况，即使实际情况并非如此。反之亦然，误报失败是测试失败一种情况，即使一切都按预期进行，测试结果也会报告脚本执行过程中出现错误。误报对自动化测试一直是最大的挑战，当然`Selenium`也不例外。

当测试工程师通过`Selenium`脚本运行成百上千的测试用例时，可能会遇到一些不稳定的测试，这些测试显示误报。如果长时间不处理，可能会导致整个自动化测试项目失去价值，从而使测试人员的自动化测试脚本沦为“废物”。

测试脚本的稳定性无疑是`Selenium`自动化中最常见的挑战之一。目前通用的解决办法依然缺少，但从过往工作经验来看，测试左移，独立测试环境，统计脚本误报率等等从流程上来解决这个难题是一个不错的思路。

# 等待网页加载JavaScript

现在很多网站包含需要`JS`异步加载`Web`元素，例如基于用户选择的下拉列表。则`Selenium`脚本在运行时可能会在这些`Web`元素时突然失效。发生这种情况是因为`WebDriver`没有处理网页完全加载所花费的时间。为了处理页面加载的`Selenium`自动化中的异步加载的问题，需要使`WebDriver`等到该页面的完整`JavaScript`加载完成之后再进行操作。在任何网页上执行测试之前，您应确保该网页（尤其是带有很多`JavaScript`代码的网页）的加载已完成。您可以使用`readyState`属性，该属性描述文档/网页的加载状态。`document.readyState`状态为`complete`表示页面/文档的解析已完成。


```
''' 在此示例中，我们将pytest框架与Selenium一起使用 '''
import pytest
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.keys import Keys
from time import sleep
from contextlib import contextmanager
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support.expected_conditions import staleness_of

@pytest.fixture(params=["chrome"],scope="class")
def driver_init(request):
    if request.param == "chrome":
        web_driver = webdriver.Chrome()
    request.cls.driver = web_driver
    yield
    web_driver.close()

@pytest.mark.usefixtures("driver_init")
class BasicTest:
    pass
class Test_URL(BasicTest):
        def test_open_url(self):
            self.driver.get("https://www.*****.com/")
            print(self.driver.title)
            sleep(5)
  def wait_for_page_load(self, timeout=30):
            old_page = self.driver.find_element_by_id('owl-example')  
            yield
            WebDriverWait(self.driver, timeout).until(
                staleness_of(old_page)
            )
        
        def test_click_operation(self):
            # 等待10秒钟的超时时间，然后执行CLICK操作
            with self.wait_for_page_load(timeout=10):
                self.driver.find_element_by_link_text('FREE SIGN UP').click()
                print(self.driver.execute_script("return document.readyState"))
```

# 无法扩展的方法

`Selenium`是一种出色的自动化测试工具，它是开源的，它使世界各地的Web测试人员的生活变得更加轻松。但是，`Selenium`自动化的主要挑战之一是无法扩展。

执行自动化测试的最终目标是在更短的时间内覆盖更多的测试范围。最初可能会进行简短的测试构建，但是产品势必会随着每个迭代版本发生变化。这意味着您可能需要涵盖更多的测试用例。使用`Selenium WebDriver`，您只能以顺序的方式执行测试，并且效果不如您希望的自动化过程有效，测试脚本的执行速度也会变得越来越慢。

现在，`Selenium Grid`可以并行运行测试用例，但这也有一个缺点。无法跨浏览器和操作系统的多种组合全面测试网站或网络应用。因为`Selenium Grid`仅有助于在本地计算机上安装的特定浏览器上执行跨浏览器测试。您可以利用`Selenium`中的并行测试功能来代替线性测试，从而降低总体项目成本，并在并行执行自动化测试时加快产品/功能迭代交付。

# 处理动态内容

越来越多的网站使用了动态的内容。测试具有静态内容的网站对`Selenium`自动化来说相对轻松。在当今时代，大多数网站所包含的内容可能因一个访客而异。这意味着内容本质上是动态的（基于`AJAX`的应用程序）。

例如，电子商务网站可以根据用户登录的位置加载不同的产品、内容可能会有所不同，具体取决于用户从特定下拉菜单中选择的内容。由于新内容的加载需要时间，因此仅在加载完成后才触发测试非常重要。由于网页上的元素以不同的时间间隔加载，因此如果`DOM`中尚不存在某个元素，则可能会出现错误。这就是为什么处理动态内容一直是Selenium自动化中最常见的挑战之一的原因。

解决此问题的一个简单解决方案是使线程休眠几秒钟，这可能会提供足够的时间来加载内容。但是，这不是一个好习惯，因为，无论是否发生必需的事件，线程都会休眠那么长的时间。

```
# 并不完美的方案
from selenium import webdriver
import time
from time import sleep
 
driver = webdriver.Firefox()
driver.get("https://www.*****.com")

# 睡眠10秒，无论是否存在元素
time.sleep(10)

# 资源释放
driver.close()
```

使用`Selenium WebDriver`动态内容处理此挑战的更好方法是使用隐式等待或显式等待，这取决于各自的需求。

## 显式等待处理动态内容

使用显式等待，您可以使`Selenium WebDriver`停止执行并等待直到满足特定条件。如果您希望设置条件以等待到确切的时间段，则可以将它与`thread.sleep()`函数一起使用。有多种方法可以实现显式等待，带有`ExpectedCondition`的`WebDriver`是最受欢迎的选项。

```
from selenium import webdriver
from time import sleep
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.support.ui import WebDriverWait

driver = webdriver.Firefox()
driver.get("https://www.*****.com")

try:
    myElem_1 = WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.CLASS_NAME, 'home-btn')))
    print("Element 1 found")
    myElem_2 = WebDriverWait(driver, 10).until(EC.element_to_be_clickable((By.CLASS_NAME, 'login')))
    print("Element 2 found")
    myElem_2.click()
    sleep(10)
#异常处理
except TimeoutException:
    print("No element found")

sleep(10)

driver.close()
```

在上面的示例中，对受测URL内容进行了两次搜索。第一次搜索是通过`CLASS_NAME`将元素定位到元素`home-btn`的最长持续时间为10秒。

第二个搜索是可点击元素登录，最长持续时间为10秒。如果存在`clickable`元素，则执行`click()`操作。在两种情况下，都将`WebDriverWait`与`ExpectedCondition`一起使用。`WebDriverWait`触发`ExpectedCondition`的默认限制为500毫秒，直到收到成功的响应。

## 隐式等待处理动态内容

隐式等待通知WebDriver在特定时间段内轮询DOM，以获取页面上Web元素的存在。默认超时为0秒。隐式等待需要一次性设置，如果配置正确，它将在Selenium WebDriver对象的生存周期内可用。


```
from selenium import webdriver
import time
from time import sleep
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.support import expected_conditions as EC
from builtins import str

driver = webdriver.Firefox()
driver.get("https://www.*****.com")
driver.implicitly_wait(60)
curr_time = time.time()
try: 
    curr_element = driver.find_element_by_class_name("home-btn") 
except: 
    print("元素不存在！")
print("耗时 " + str(int(time.time()-curr_time))+'-secs')

driver.close()
```

## 处理弹出窗口

在某些情况下，您需要与弹出窗口进行自动交互，这是`Selenium`自动化中最常见的挑战之一。有不同类型的弹出窗口：

* 简单警报：显示一些消息的东西。
* 确认警报：要求用户确认操作。
* 提示警报：通知用户输入内容。


尽管`Selenium WebDriver`无法处理基于`Windows`的警报，但它确实具有处理基于Web的警报的功能。`switch_to`方法用于处理弹出窗口。为了演示，我们创建了一个简单的`HTML`文件，其中包含`Alert`弹出窗口实现。


```
<!DOCTYPE html>
<html>
<body>
<h2>
Demo for Alert</h3>


<button onclick="create_alert_dialogue()" name ="submit">Alert Creation</button>
<script>
function create_alert_dialogue() {
 alert("测试弹框，请点击继续！");
}
</script>
</body>
</html>
```

下面是我们在`switch_to`方法中的用法 `.switch_to.alert.alert`，以便切换到警报对话框。进入“警报框”后，可以使用`alert.accept()`方法接受警报。


```
from selenium import webdriver
import time
from time import sleep
from selenium.webdriver.support.ui import WebDriverWait
from selenium.common.exceptions import NoSuchElementException
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.support import expected_conditions as EC
from builtins import str

driver = webdriver.Firefox()
driver.get("file://<HTML File location>")

driver.find_element_by_name("submit").click()
sleep(5)

alert = driver.switch_to.alert
text_in_alert = alert.text
sleep(10)

alert.accept()

print("ok，跳过警告框")

driver.close()
```

如果页面显示输入警报，则可以使用`send_keys()`方法将文本发送到输入框。

```
from selenium import webdriver
import time
from time import sleep
from selenium.webdriver.support.ui import WebDriverWait
from selenium.common.exceptions import NoSuchElementException
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.support import expected_conditions as EC
from builtins import str

driver = webdriver.Firefox()
driver.get("file://<HTML File location>")

driver.find_element_by_name("***").send_keys("***")
sleep(5)

driver.find_element_by_name("submit").click()
sleep(5)

try:
    WebDriverWait(driver, 10).until(EC.alert_is_present(),
                '超时')
    alert = driver.switch_to.alert
    sleep(10)
    alert.accept()
    sleep(10)  
except TimeoutException:
    print("没有发现警告框")

driver.close()
```

# 切换浏览器窗口

多窗口测试无疑是`Selenium`自动化中的常见挑战之一。一种理想的情况是单击按钮会打开一个弹出窗口，该弹出窗口成为子窗口。一旦有关子窗口上的活动的活动完成，则应将控件移交给父级或者上一级窗口。这可以通过使用`switch_to.window()`方法（其中`window_handle`作为输入参数传递）来实现。

用户单击链接后，将打开一个新的弹出窗口，该窗口将称为子窗口。`switch_to.window`方法用于切换回父窗口。可以使用`driver.switch_to.window(window-handle-id)`或`driver.switch_to.default_content（）`切换到父窗口。


```
import unittest
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from time import sleep
 
class test_switch_windows(unittest.TestCase):
 
    def setUp(self):
        self.driver = webdriver.Firefox()
    
    def test_open_pop_up_window(self):
        driver = self.driver
        driver.get("http://www.****.com/html/codes/html_popup_window_code.cfm")
        title1 = driver.title
        print(title1)
    

        #获取当前窗口的窗口句柄
        parent_window = driver.window_handles[0]
        print(parent_window)
 
        #弹出式窗口是iframe
        driver.switch_to.frame(driver.find_element_by_name('result1'))
        driver.find_element_by_link_text('show_iframe').click()
        
        #获取子窗口的句柄
        child_window = driver.window_handles[1]
        
        #父窗口将在后台
        #子窗口来到前台
        driver.switch_to.window(child_window)
        title2 = driver.title
        print(title2)
        print(child_window)
        
        #断言主窗口和子窗口标题不匹配
        self.assertNotEqual(parent_window , child_window)
        
        sleep(5)
    
    def tearDown(self):
        self.driver.close()
 
if __name__ == "__main__":
    unittest.main()
```

## 无法测试移动设备

尽管`Selenium`框架已广泛用于跨浏览器和操作系统的不同组合测试网站或`Web`应用程序，但该测试仍仅限于非移动设备。因此，测试针对移动设备的网站或Web应用程序是`Selenium`自动化面临的重大挑战之一。

如果对移动应用程序执行测试自动化测试，那么最著名的开源框架将是`Appium`。

## 无法自动化一切

100％自动化是一个吹牛的命题。众所周知的是，并非所有测试方案都可以自动化，因为有些测试需要手动干预。您需要确定团队在自动化测试上相对于手动测试应花费的精力的优先级。尽管`Selenium`框架具有一些功能，可以通过这些功能来截取屏幕截图，记录视频（测试的整体执行情况）以及可视化测试的其他方面，但是将这些功能与可扩展的基于云的跨浏览器测试平台一起使用可能会具有很大的价值。

## 生成测试报告

任何测试活动的主要目的是发现BUG并改善整体产品。在跟踪正在执行的测试，生成的输出和测试结果方面，报告可以发挥主要作用。尽管有可以与pytest和Selenium一起使用的模块，例如`pytest_html`（对于`Python`），但是测试报告中的信息可能并不十分详尽。Selenium可以使用不同类型的编程语言（例如`Java`，`C＃`、`.Net`等）的类似模块/包，但是这些语言仍然存在相同的问题。收集测试报告是Selenium自动化中的关键挑战之一。

很多基于`Selenium`的第三方云测平台，还有很多公司机遇`Selenium`开发的自己的报告框架提取，一般来说从以下几个方面丰富报告信息：

* 检索构建信息，例如构建测试状态，单个测试状态，测试运行时间，错误和测试日志
* 通过命令获取屏幕截图
* 浏览器环境的详细信息

## 有所不能

上面提到的是`Selenium`自动化中的一些常见挑战，就`Selenium`而言存在一些限制。只能使用`Selenium`框架来测试`Web`应用程序，即不能用于测试基于本地`Windows`的应用程序。在某些情况下可能需要使用这些场景，出于安全目的，因此自动化测试很难甚至永远无法绕过一些严格身份验证。


--- 
* 公众号**FunTester**首发，更多原创文章：[FunTester440+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [开源测试服务](https://mp.weixin.qq.com/s/ZOs0cp_vt6_iiundHaKk4g)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [电子书网站爬虫实践](https://mp.weixin.qq.com/s/KGW0dIS5NTLgxyhSjxDiOw)
- [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)