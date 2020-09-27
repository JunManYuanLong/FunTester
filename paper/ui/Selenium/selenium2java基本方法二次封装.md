# selenium2java基本方法二次封装

本人在使用selenium做测试的时候，封装了很多方法，由于工作原因估计很长时间不会更新方法库了，中间关于js的部分还差一些没有完善，其中设计接口的部分暂时就先不发了，以后有机会在更新。


```
package soucrce;
 
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.*;
//import org.openqa.selenium.ie.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
 
public class SelLibrary extends SourceCode{
	private static SelLibrary library = new SelLibrary();
	public static SelLibrary getInstance() {
		return library;
	}
	public static WebDriver driver = getDrive();
//	public static WebDriver driverH5 = getDriveH5();
	public static WebDriver getDrive() {
		/* 谷歌浏览器
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("http://www.baidu.com/");
		*/
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.manager.showWhenStarting", false);//是否显示下载进度框
		profile.setPreference("browser.offline-apps.notify", false);//网站保存离线数据时不通知我
		profile.setPreference("browser.helperApps.alwaysAsk.force", false);//应用程序设置不询问
		profile.setPreference("browser.download.folderList", 0);//设置下载地址0是桌面；1是“我的下载”；2是自定义
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream, application/vnd.ms-excel, text/csv, application/zip, application/msword");
		profile.setPreference("dom.webnotifications.enabled", false);//允许通知			
		WebDriver driver = new FirefoxDriver(profile);//启动火狐浏览器
		driver.manage().window().maximize();//设置窗口大小
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);//设置页面加载超时
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);//设置查询组件等待时间
		return driver;
	}
	/*
	 *此为H5页面方法，暂不使用
	public static WebDriver getDriveH5() {
		// 谷歌浏览器
//		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
//		WebDriver driver = new ChromeDriver();
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.manager.showWhenStarting", false);//是否显示下载进度框
		profile.setPreference("browser.offline-apps.notify", false);//网站保存离线数据时不通知我
		profile.setPreference("browser.helperApps.alwaysAsk.force", false);//应用程序设置不询问
		profile.setPreference("browser.download.folderList", 0);//设置下载地址0是桌面；1是“我的下载”；2是自定义
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/octet-stream, application/vnd.ms-excel, text/csv, application/zip, application/msword");
		profile.setPreference("dom.webnotifications.enabled", false);//允许通知	
		WebDriver driver = new FirefoxDriver(profile);//启动火狐浏览器
		driver.manage().window().setSize(new Dimension(435, 773));//设置窗口大小
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);//设置页面加载超时
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);//设置查询组件等待时间
		return driver;
	}
	*/
	public final String LINE = "\r\n";
	public final String testAppURL = "api.dev.chaojizhiyuan.com";
	public final String appURL = "api.gaotu100.com";
	public final String testWebURL = "beta-web.gaotu100.com";
	public final String smile = "^_^";
	public final String sad = "*o*";
	//截图命名为当前时间保存桌面
	public void takeScreenshotByNow() throws IOException {
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String file = "C:\\Users\\fankaiqiang\\Desktop\\888\\picture\\321\\"+getNow()+".png";
		FileUtils.copyFile(srcFile,new File(file));
	}
	//截图重命名保存至桌面
	public void takeScreenshotByName(String name) throws IOException {
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String file = "C:\\Users\\fankaiqiang\\Desktop\\888\\picture\\321\\"+name+".png";
		FileUtils.copyFile(srcFile,new File(file));
	}
	//通过id获取元素并点击
	public void findElementByIdAndClick(String id) {
		driver.findElement(By.id(id)).click();
	}
	public void findElementByNameAndClick(String name) {
		findElementByName(name).click();
	}
	//根据文本获取元素并点击
	public void findElementByTextAndClick(String text) {
		driver.findElement(By.linkText(text)).click();
	}
	//根据文本模糊查找
	public void findElementByPartiaTextAndClick(String text) {
		driver.findElement(By.partialLinkText(text)).click();
	}
	//根据xpath获取元素
	public WebElement findElementByXpath(String xpath) {
		return driver.findElement(By.xpath(xpath));
	}
	public WebElement findElementByTag(String tag) {
		return driver.findElement(By.tagName(tag));
	}
	//根据id获取元素
	public WebElement findElementById(String id) {
		return driver.findElement(By.id(id));
	}
	//根据id获取元素清除文本写入文本
	public void findElementByIdAndClearSendkeys(String id1 , String id2, String text) {
		driver.findElement(By.id(id1)).clear();
		driver.findElement(By.id(id2)).sendKeys(text);
	}
	public void findElementByIdAndClearSendkeys(String id, String text) {
		driver.findElement(By.id(id)).clear();
		driver.findElement(By.id(id)).sendKeys(text);
	}
	public void findElementByIdAndClearSendkeys(String id, int num) {
		driver.findElement(By.id(id)).clear();
		driver.findElement(By.id(id)).sendKeys(num+"");
	}
	public void findElementByNameAndClearSendkeys(String name, String text) {
		findElementByName(name).clear();
		findElementByName(name).sendKeys(text);
	}
	public void findElementByNameAndClearSendkeys(String name, int num) {
		findElementByName(name).clear();
		findElementByName(name).sendKeys(num+"");
	}
	//通过xpath获取元素点击
	public void findElementByXpathAndClick(String xpath) {
		driver.findElement(By.xpath(xpath)).click();
	}
	//通过class获取元素并点击
	public void findElementByClassNameAndClick(String name) {
		driver.findElement(By.className(name)).click();
	}
	public WebElement findElementByClassName(String name){
		return driver.findElement(By.className(name));
	}
	//获取一组元素
	public List<WebElement> findElementsByClassName(String className) {
		return driver.findElements(By.className(className));
	}
	//根据text获取一组页面元素
	public List<WebElement> findElementsByText(String text) {
		return driver.findElements(By.linkText(text));
	}
	public List<WebElement> findElementsByPartialText(String text) {
		return driver.findElements(By.partialLinkText(text));
	}
	//根据id获取一组页面元素
	public List<WebElement> findElementsById(String id) {
		return driver.findElements(By.id(id));
	}
	//根据tagName获取一组页面元素
	public List<WebElement> findElementsByTag(String tag) {
		return driver.findElements(By.tagName(tag));
	}
	public WebElement findElementByText(String text){
		return driver.findElement(By.linkText(text));
	}
	public WebElement findElementByPartialText(String text){
		return driver.findElement(By.partialLinkText(text));
	}
	public WebElement findElementByName(String name) {
		return driver.findElement(By.name(name));
	}
	//输出cookies信息
	public void outputCookie() {
		Set<Cookie> cookie = driver.manage().getCookies();
		System.out.println(cookie);
//		Cookie abc = new Cookie("", "");
//		driver.manage().addCookie(abc);
	}
	public void addCookie(Map<String, String> args) {
		Set<String> keys = args.keySet();
		for(String key : keys){
			driver.manage().addCookie(new Cookie(key, args.get(key)));
		}
	}
	//测试开始
	public void testBegin() {
		outputBegin();
		outputNow();
	}
	//测试结束
	public void testOver(boolean key) {
		if (key) {
		exit();
		}
		outputNow();
		outputOver();
		driver.quit();
	}
	//退出登录
	public void exit() {
		output("暂未实现退出方法！");
	}
	//判断元素是否存在
	public boolean exists(By selector) {
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);//设置查询组件等待时间
		try {
			driver.findElement(selector);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);//设置查询组件等待时间
			return true;
		} catch (Exception e) {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);//设置查询组件等待时间
			return false;
		}
	}
	//通过js点击
	public void clickByJs(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element);
		//第二种点击方法
//		((JavascriptExecutor) driver).executeScript("arguments[0].click()", question);
	}
	//通过xpath获取元素用js点击
	public void clickByJsByXpath(String xpath) {
		clickByJs(driver.findElement(By.xpath(xpath)));
	}
	public void clickByJsByText(String text) {
		clickByJs(findElementByText(text));
	}
	public void clickByJsById(String id) {
		clickByJs(findElementById(id));
	}
	public void clickByJsByClassName(String name) {
		clickByJs(findElementByClassName(name));
	}
	public void clickByJsByName(String name) {
		clickByJs(findElementByName(name));
	}
	//按物理按键
	public void pressKeyEvent(int keycode) throws AWTException {
		Robot robot = new Robot();
//		robot.keyPress(KeyEvent.VK_ENTER);//按下enter键
		robot.keyPress(keycode);
	}
	//通过xpath获取元素清除文本并写入
	public void findElementByXpathAndClearSendkeys(String xpath, String text) {
		findElementByXpath(xpath).clear();
		findElementByXpath(xpath).sendKeys(text);
	}
	public void findElementByXpathAndClearSendkeys(String xpath, int num) {
		findElementByXpath(xpath).clear();
		findElementByXpath(xpath).sendKeys(num+"");
	}
	//判断是否有警告框
	public boolean judgeAlert(WebDriver driver) {
		try {
			driver.switchTo().alert();
			return true;
			} catch (Exception e) {
				output("没有发现警告框！");
				return false;
				}
		}
	//获取文本
	public String getTextByXpath(String xpath) {
		return findElementByXpath(xpath).getText();
	}
	public String getTextByClassName(String name) {
		return findElementByClassName(name).getText();
	}
	public String getTextById(String id) {
		return findElementById(id).getText();
	}
	public String getTextByName(String name) {
		return findElementByName(name).getText();
	}
	//获取数量
	public int getNumByXpath(String xpath) {
		String num = getTextByXpath(xpath);
		return changeStringToInt(num);
	}
	public int getNumByClassName(String name) {
		String num = getTextByClassName(name);
		return changeStringToInt(num);
	}
	//通过xpath获取classname
	public String getClassNameByXpath(String xpath) {
		return findElementByXpath(xpath).getAttribute("class");
	}
	//通过id获取classname
	public String getClassNameById(String id) {
		return findElementById(id).getAttribute("class");
		}
	//强制刷新
	public void refresh() {
		Actions ctrl = new Actions(driver);
		ctrl.keyDown(Keys.CONTROL).perform();
		try {
			pressKeyEvent(KeyEvent.VK_F5);
		} catch (AWTException e) {
			output(sad+getNow());
			e.printStackTrace();
		}
		ctrl.keyUp(Keys.CONTROL).perform();
//		driver.navigate().refresh();
	}
//	//显式等待
//	public void waitForWebElementByXpathAndClick(String xpath) {
//		new WebDriverWait(5).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
//		findElementByXpathAndClick(xpath);
//	}
 
	//等待元素可用再点击
	public void waitForEnabledByXpathAndClick(String xpath) throws InterruptedException {
		boolean key = true;
		while(key){
			if (findElementByXpath(xpath).isEnabled() && findElementByXpath(xpath).isDisplayed()) {
				output(123);
				clickByJsByXpath(xpath);
//				findElementByXpathAndClick(xpath);
				key = false;
			}else{
				sleep(0);
			}
		}
	}
	//右键点击
	public void	RightClickWebElement(String id) {
		Actions actions = new Actions(driver);
		actions.contextClick(findElementById(id)).perform();;
	}
	//根据classname获取元素清除并输入内容
	public void findElementByClassnameAndClearSendkeys(String classname, String text) {
		driver.findElement(By.className(classname)).clear();
		driver.findElement(By.className(classname)).sendKeys(text);
	}
	public void findElementByClassnameAndClearSendkeys(String classname, int num) {
		driver.findElement(By.className(classname)).clear();
		driver.findElement(By.className(classname)).sendKeys(num+"");
	}
	//根据id获取下拉框，根据index选择选项
	public void findSelectByIdAndSelectByIndex(String id, int index) {
		Select select = new Select(findElementById(id));
		select.selectByIndex(index);
	}
	//根据id获取下拉框，根据value选择选项
	public void findSelectByIdAndSelectByValue(String id, String value) {
		Select select = new Select(findElementById(id));
		select.selectByValue(value);
	}
	//根据id获取下拉框，根据text选择选项
	public void findSelectByIdAndSelectByText(String id, String text) {
		Select select = new Select(findElementById(id));
		select.selectByVisibleText(text);;
	}
	//根据classname获取下拉框，根据text选择选项
	public void findSelectByClassNameAndSelectByText(String name, String text) {
		Select select = new Select(findElementByClassName(name));
		select.selectByVisibleText(text);
	}
	//根据classname获取下拉框，根据Value选择选项
	public void findSelectByClassNameAndSelectByValue(String name, String value) {
		Select select = new Select(findElementByClassName(name));
		select.selectByValue(value);
		}
	//根据classname获取下拉框，根据index选择选项
	public void findSelectByClassNameAndSelectByIndex(String name, int index) {
		Select select = new Select(findElementByClassName(name));
		select.selectByIndex(index);
		}
	//根据name获取下拉框，根据text选择选项
	public void findSelectByNameAndSelectByText(String name, String text) {
		Select select = new Select(findElementByName(name));
		select.selectByVisibleText(text);
	}
	//根据name获取下拉框，根据Value选择选项
	public void findSelectByNameAndSelectByValue(String name, String value) {
		Select select = new Select(findElementByName(name));
		select.selectByValue(value);
		}
	//根据name获取下拉框，根据index选择选项
	public void findSelectByNameAndSelectByIndex(String name, int index) {
		Select select = new Select(findElementByName(name));
		select.selectByIndex(index);
		}
	//鼠标悬停
	public void moveToElementById(String id) {
		Actions actions = new Actions(driver);
		actions.moveToElement(findElementById(id));
		}
	public void moveToElementByClassName(String name) {
		Actions actions = new Actions(driver);
		actions.moveToElement(findElementByClassName(name));
	}
	//滚动到最上方
	public void scrollToTop() {
		((JavascriptExecutor)driver).executeScript("window.scrollTo(0,0);");
//		JavascriptExecutor js = (JavascriptExecutor) driver;
//		js.executeScript("window.scrollTo(0,0);");
	}
	//滚动到页面底部
	public void scrollToBottom(String id) {
		((JavascriptExecutor)driver).executeScript("window.scrollTo(0,10000);");
	}
	//滚动到某个元素
	public void scrollToElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
	}
	//js滚动页面内div
	public void scrollToBottomById(String id) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollTo(0, 3000);", findElementById(id));
	}
	//使用js使元素隐藏元素显示
	public void makeDisplayById(String id) {
		JavascriptExecutor  js = (JavascriptExecutor)driver;
		js.executeScript("document.getElementById(id).style.display='block';");
	}
	public void makeElementDisplay(WebElement element) {
		JavascriptExecutor  js = (JavascriptExecutor)driver;
//		WebElement element = driver.findElement(By.xxx);
		js.executeScript("arguments[0].style=arguments[1]", element, "display: block;");
	}
	//js输入文本
	public void inputTextByJsById(String text, String id) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].value=\"2016-08-20\"",driver.findElement(By.id(id)));
	}
	//js输入文本
	public void inputTextByJs(String text, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].value=" + text + "\"", element);
	}
	//返回
	public void BrowserBack() {
		driver.navigate().back();
	}
	//前进
	public void BrowserForward() {
		driver.navigate().forward();
	}
}
```

### 技术类文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
8. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
9. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
10. [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
11. [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)
12. [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)

### 非技术文章精选
1. [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
2. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
3. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
4. [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
5. [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
6. [测试之《代码不朽》脑图](https://mp.weixin.qq.com/s/2aGLK3knUiiSoex-kmi0GA)

# [点击查看公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>