# Selenium编写自动化用例的8种技巧


[原文地址](https://www.lambdatest.com/blog/8-actionable-insights-to-write-better-automation-code/)


在开始自动化时，您可能会遇到各种可能包含在自动化代码中的方法，技术，框架和工具。有时，与提供更好的灵活性或解决问题的更好方法相比，这种多功能性导致代码更加复杂。在编写自动化代码时，重要的是我们能够清楚地描述自动化测试的目标以及我们如何实现它。话虽如此，编写“干净的代码”以提供更好的可维护性和可读性很重要。编写干净的代码也不是一件容易的事，您需要牢记许多最佳实践。以下主题突出显示了编写更好的自动化代码应获得的8条银线。

## 1.命名约定
当我们从手动转向自动化或实际上以任何编程语言编写代码时，这确实是要牢记的经验法则之一。遵循正确的命名约定有助于更轻松地理解代码和维护。此命名约定暗含变量，方法，类和包。例如，您的方法名称应特定于其用途。“ Register_User（）”方法描述了在该方法中显示用户注册的方法。明确定义的方法名称增加了脚本的易于维护和可读性。这同样适用于变量命名。我注意到许多人提到变量为a，b，c等，甚至将Web元素称为Weblelement1，Webelement2等。这样一来，用户看不到变量名与预期的一样。

以下是显示命名错误的示例：



```
public void Register_User() throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.get("https://www.lambdatest.com/ ");
		driver.manage().window().maximize();
		WebElement web1= driver.findElement(By.xpath("//a[text()='Free Sign Up']"));
		web1.click();
		WebElement web2=driver.findElement(By.xpath("//input[@name='organization']"));
		web2.sendKeys("LambdaTest");
		WebElement web3=driver.findElement(By.xpath("//input[@name='first_name']"));
		web3.sendKeys("Test");
		WebElement web4=driver.findElement(By.xpath("//input[@name='last_name']"));
		web4.sendKeys("User");
		WebElement web5=driver.findElement(By.xpath("//input[@name='email']"));
		web5.sendKeys("sadhvi.singh@navyuginfo.com");
		WebElement web6=driver.findElement(By.xpath("//input[@name='password']"));
		web6.sendKeys("TestUser123");
		WebElement web7=driver.findElement(By.xpath("//input[@name='phone']"));
		web7.sendKeys("9412262090");
		WebElement web8=driver.findElement(By.xpath("//button[text()='SIGN UP']"));
		web8.click();
		Thread.sleep(3500);
		
	}	
```

上面的代码显示了“ method1”如何不向用户提供任何线索，就像该方法的确切作用一样。另外，所有的web元素都通过web1，web2等表示。用户无法识别哪个Web元素捕获了哪个字段。

对于上述相同的代码，可以如下标记正确的表示方式：


```
public void Register_User() throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.get("https://www.lambdatest.com/ ");
		driver.manage().window().maximize();
		WebElement link= driver.findElement(By.xpath("//a[text()='Free Sign Up']"));
		link.click();
		WebElement organization=driver.findElement(By.xpath("//input[@name='organization']"));
		organization.sendKeys("LambdaTest");
		WebElement first_name=driver.findElement(By.xpath("//input[@name='first_name']"));
		first_name.sendKeys("Test");
		WebElement last_name=driver.findElement(By.xpath("//input[@name='last_name']"));
		last_name.sendKeys("User");
		WebElement email=driver.findElement(By.xpath("//input[@name='email']"));
		email.sendKeys("sadhvi.singh@navyuginfo.com");
		WebElement password=driver.findElement(By.xpath("//input[@name='password']"));
		password.sendKeys("TestUser123");
		WebElement phone_number=driver.findElement(By.xpath("//input[@name='phone']"));
		phone_number.sendKeys("9412262090");
		WebElement button=driver.findElement(By.xpath("//button[text()='SIGN UP']"));
		button.click();
		Thread.sleep(3500);
		String url= driver.getCurrentUrl();
		assertEquals("fail- unable to register", url, "https://accounts.lambdatest.com/user/email-verification");
		
	}
```

在这里，方法名称'Register_User'通过名称明确定义了用户，指示该方法包含与用户注册相关的代码。同样，所有Web元素或变量都具有与用于定义意图的捕获字段相关的名称。

通常，通常鼓励使用驼峰式大小写来记录方法或变量，因为它在可读性和维护脚本方面更加清晰。

## 2.减少，重用和回收
确保将您的方法分解到用户场景的最小块上非常重要。它们应涵盖简单和单一的流程。不要让您的方法与单一方法涵盖的多个功能过于复杂。例如，登录功能需要在应用程序上注册用户。将您的注册功能保留在另一个方法中，如果需要，请在登录方法中调用该方法。降低方法的复杂度可简化代码的可维护性。

另外，在需要的地方重复使用您的方法，请勿将相同的代码复制粘贴到不同的方法中。这导致代码中不必要的重复和冗余。增加代码行并不意味着您已经编写了不错的代码。重构和优化代码是编写稳定，健壮和更好的自动化代码的关键。

回收也是编写更好的自动化代码的另一个有用技巧。我有经验丰富的人员可以自动化遗留系统，不倾向于在自动化框架中更改现有方法，而不会在现有功能发生变化时重写另一种方法。这只是使框架变得脆弱。每当流程改变时，总是要更新现有方法，尽管它有其自身的挑战，即新用户可能不知道该方法可能具有的依赖性，但是我认为我们应该始终以长远的眼光来看待问题，而不是实现那些较短的目标。 。

下面是一个示例，说明如何将登录代码简化为一小部分功能，并使用了另一种注册方法来简化整个过程。


```
@Test
	public void Register_User() throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.get("https://www.lambdatest.com/ ");
		driver.manage().window().maximize();
		WebElement link= driver.findElement(By.xpath("//a[text()='Free Sign Up']"));
		link.click();
		WebElement organization=driver.findElement(By.xpath("//input[@name='organization']"));
		organization.sendKeys("LambdaTest");
		WebElement first_name=driver.findElement(By.xpath("//input[@name='first_name']"));
		first_name.sendKeys("Test");
		WebElement last_name=driver.findElement(By.xpath("//input[@name='last_name']"));
		last_name.sendKeys("User");
		WebElement email=driver.findElement(By.xpath("//input[@name='email']"));
		email.sendKeys("sadhvi.singh@navyuginfo.com");
		WebElement password=driver.findElement(By.xpath("//input[@name='password']"));
		password.sendKeys("TestUser123");
		WebElement phone_number=driver.findElement(By.xpath("//input[@name='phone']"));
		phone_number.sendKeys("9412262090");
		WebElement button=driver.findElement(By.xpath("//button[text()='SIGN UP']"));
		button.click();
	}	
	
	@Test
	public void Login_User()
	{

		  driver.get("https://accounts.lambdatest.com/login");
		  driver.findElement(By.xpath("//input[@name='email']")).sendKeys("User2@gmail.com");
		  driver.findElement(By.xpath("//input[@name='password']")).sendKeys("TestUser123");
		  driver.findElement(By.xpath("//button[@class='sign-up-btn']")).click();
		
	}

	@AfterClass
	public static void BrowserClose()
	{
		
		driver.quit();
	}
	

	}
```

## 3.合理地组织测试
好的，这确实是确保更好的自动化代码的主要可操作见解之一。它不仅易于理解，而且在维护上无需花费太多精力。从长远来看，借助框架来构建测试可以增加工作价值，并减少维护工作。您可以通过使用由JUnit和TestNG之类的框架提供的注释来控制应用程序的流程。例如，使用@BeforeClass之类的注释可以帮助您指导耗时的活动，例如连接到数据库，设置浏览器等与此方法相关的代码以及与此相关联的@BeforeClass注释。这可以帮助自动化测试仪立即知道该方法的确切功能以及何时调用该方法。试想一下，您的设置过程很清楚，并且已从代码的其他部分中整理出来。

下面的示例突出显示了通过TestNG框架展示了一种更好的结构化方法：


```
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Lamdatest {

	static WebDriver driver;
	
	@BeforeClass
	public static void BrowserOpen()
	{
		System.setProperty("webdriver.chrome.driver", "chromepath"); 
	    driver= new ChromeDriver() ;
	    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
	
	@Test(priority=1)
	public void Register_User() throws InterruptedException
	{
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.get("https://www.lambdatest.com/ ");
		driver.manage().window().maximize();
		WebElement link= driver.findElement(By.xpath("//a[text()='Free Sign Up']"));
		link.click();
		WebElement organization=driver.findElement(By.xpath("//input[@name='organization']"));
		organization.sendKeys("LambdaTest");
		WebElement first_name=driver.findElement(By.xpath("//input[@name='first_name']"));
		first_name.sendKeys("Test");
		WebElement last_name=driver.findElement(By.xpath("//input[@name='last_name']"));
		last_name.sendKeys("User");
		WebElement email=driver.findElement(By.xpath("//input[@name='email']"));
		email.sendKeys("sadhvi.singh@navyuginfo.com");
		WebElement password=driver.findElement(By.xpath("//input[@name='password']"));
		password.sendKeys("TestUser123");
		WebElement phone_number=driver.findElement(By.xpath("//input[@name='phone']"));
		phone_number.sendKeys("9412262090");
		WebElement button=driver.findElement(By.xpath("//button[text()='SIGN UP']"));
		button.click();
		String url= driver.getCurrentUrl();
		assertEquals("fail- unable to register", url, "https://accounts.lambdatest.com/user/email-verification");
		
	}	
	
	@Test(dependsOnMethods="Register_User")	
	public void Login_User()
	{

		  driver.get("https://accounts.lambdatest.com/login");
		  driver.findElement(By.xpath("//input[@name='email']")).sendKeys("User2@gmail.com");
		  driver.findElement(By.xpath("//input[@name='password']")).sendKeys("TestUser123");
		  driver.findElement(By.xpath("//button[@class='sign-up-btn']")).click();
		
	}
	
		
	
	@AfterClass
	public static void BrowserClose()
	{
		
		driver.quit();
	}
	

	}
```

确定哪些注释应该与哪种测试方法相关联是很重要的。通过明确的依赖关系和优先级，可以根据应用程序的流程来构造测试和代码。

## 4.全面验证您的测试

作为质量检查人员，您要做的就是验证您的预期和实际满足情况，这与您的自动化代码相同。如果您的脚本不符合验证要求，那么创建一个脚本将毫无意义，也没有任何意义。理想情况下，每个用户操作都应该像测试用例步骤一样进行验证，无论它是在验证元素的可见性，还是要记住版式提示，文本表示形式，页面重定向或任何形式的视觉验证，甚至是关于评估数据库的结果。

即使您的验证无法确定，也会显示失败消息，以便您可以找出问题所在。我们在验证代码方面犯的最大错误是从确保验证通过的角度编写。我们从未考虑过如果代码失败或未达到预期效果会发生什么，那么继续下去将需要什么。

如果您希望在验证失败后立即中断测试并跳至另一测试，则可以使用硬断言，而如果您希望在同一页面上验证多个检查，则可以选择软断言。决定完全使用哪个断言取决于用例。

以下是在登录页面上执行的断言示例。在此方法中，将创建一种方法，其中使用有效凭据登录用户，然后使用另一种方法确保用户不会使用无效凭据登录并显示错误消息。


```
//validate user able to login with valid credentials
	    @Test
		public void Login_User() throws IOException
		{
	    	
		    driver.get("https://accounts.lambdatest.com/login");
            driver.findElement(By.xpath("//input[@name='email']")).sendKeys("User2@gmail.com");
			driver.findElement(By.xpath("//input[@name='password']")).sendKeys("TetsUser123");
	    	driver.findElement(By.xpath("//button[@class='sign-up-btn']")).click();
			  WebDriverWait wait= new WebDriverWait(driver, 15);
			  wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[@class='user-profile dropdown-toggle']"))));
			  String Current_url= driver.getCurrentUrl();
			  Assert.assertEquals("https://accounts.lambdatest.com/user/email-verification", Current_url);
			  System.out.println("user logged in sucesfully");
			  driver.findElement(By.xpath("//a[@class='user-profile dropdown-toggle']")).click();
			  driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
	    	}
		
		
			
		//validate user is unable to login with invalid credentials
		        @Test
				public void Login_invalid_User() throws IOException
				{

					  driver.get("https://accounts.lambdatest.com/login");
					  driver.findElement(By.xpath("//input[@name='email']")).sendKeys("User21@gmail.com");
					  driver.findElement(By.xpath("//input[@name='password']")).sendKeys("TestUser123");
					  driver.findElement(By.xpath("//button[@class='sign-up-btn']")).click();
					  WebDriverWait wait= new WebDriverWait(driver, 15);
					  String str= driver.findElement(By.xpath("//p[@class='error-mass']")).getText();
					  String Current_url= driver.getCurrentUrl();
					  Assert.assertEquals("https://accounts.lambdatest.com/login", Current_url);
					  System.out.println(str);
				}
```

覆盖多个验证检查的方法可能有所不同，或者您可以像我上面所做的那样为每个验证选择不同的方法，或者可以选择在try-catch块下的单个方法中进行所有验证。

## 5.sleep不能改善稳定性
我们倾向于相信的最大神话，尤其是当我们刚接触自动化领域时，是通过为脚本提供足够的等待量，必要或不必要的等待会导致脚本顺利执行。相反，它使脚本不稳定，并增加了总体执行时间。这种静态睡眠的主要问题是，我们不了解运行测试的机器的负载，因此可能导致超时。因此，应避免使用thread.sleep来维护更好的自动化代码。对脚本使用等待的一种更好的方法是通过条件绑定，其中脚本可以像人类一样等待直到满足特定条件。例如，等待直到某个元素可见或不可见。

作为开发更好的自动化代码的一种选择，显式和流畅的等待更加适应。

## 6.进行测试，数据驱动

在对多种形式的数据进行测试时，测试变得更加有效，当编写更好的自动化代码以测试Web应用程序或任何其他软件时，测试也是如此。在自动化中，关键是通过多种形式的数据测试测试代码，而不是为每个数据编写不同的测试脚本。这可以通过数据驱动的测试框架轻松实现。它有助于将测试数据输入存储到外部数据库中，例如CSV文件，excel文件，文本文件，XML文件甚至是ODBC存储库。此数据被调用到脚本中，并一次又一次地运行在相同的测试代码中。与手动工作相比，这有助于减少冗余并加快执行速度。发现新的bug。这种方法的另一个好处是，它减少了您可能必须添加的测试脚本的数量，从而加快了测试周期。

与之保持同步，它还有助于简化脚本的可维护性。如果应用程序发生任何更改，代码中的所有硬编码值都可能会中断。实现此目的的一种更简单的方法是将所有硬编码组件设置为变量驱动。例如，通过将它们各自的值存储在excel工作表中并在脚本中调用它们，可以使所有定位器都不受代码限制。万一您的任何定位器损坏了，您只需要在excel中更改定位器的值即可，而根本不需要触摸脚本。

数据驱动测试的一个基本示例是：


```@Test
		public void Login_User() throws IOException
		{
	    	
	    	File f1= new File("C://Users//navyug//Desktop//Test.xlsx");
	    	FileInputStream scr= new FileInputStream(f1);
	    	XSSFWorkbook book= new XSSFWorkbook(scr);
	    	XSSFSheet sheet=book.getSheetAt(0);
	    	for(int i=0; i<=sheet.getLastRowNum(); i++ )
	    	{
	    		//XSSFCell cell= sheet.getRow(i).getCell(1);
			 Row row = sheet.getRow(i);
			 Cell cell = row.getCell(0);

			  driver.findElement(By.xpath("//input[@name='email']")).sendKeys(cell.toString());
			  cell= row.getCell(1);
			  
			  driver.findElement(By.xpath("//input[@name='password']")).sendKeys(cell.toString());
			    
	    	
	    	driver.findElement(By.xpath("//button[@class='sign-up-btn']")).click();
			  WebDriverWait wait= new WebDriverWait(driver, 15);
			  wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[@class='user-profile dropdown-toggle']"))));
			  String Current_url= driver.getCurrentUrl();
			  Assert.assertEquals("https://accounts.lambdatest.com/user/email-verification", Current_url);
			  System.out.println("user logged in sucesfully");
			  takescreenshot();
			  driver.findElement(By.xpath("//a[@class='user-profile dropdown-toggle']")).click();
			  driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
	    	}
		}
```

上面的代码显示了从Excel获取的用于不同登录凭据的数据。对于Xpath也可以扩展同样的功能，其中XPath值也可以从excel中提取。在这里，通过数据驱动方法解决的关键点是从我们的代码中删除硬编码的值，使其成为面向变量，并使其在多组输入中运行同一段代码。

## 7.不要错过报告！

如果自动化代码没有向您报告结果，则该代码将无法正常工作。为了优化您作为自动化工程师的工作，重要的是要知道哪些测试代码通过了，哪些失败并附带了屏幕截图。您可以向利益相关者展示的最佳投资回报是通过报告。共享这些详细的报告可提供可见性，并减少您验证测试执行脚本的时间。您可以通过TestNG HTML报告生成，JUnit报告生成等各种技术来实现报告，也可以使用扩展库来实现报告。

下面的代码显示了一个示例，其中登录功能的完成后已截取了屏幕截图作为验证通过的证明，而下面是执行后生成的TestNG报告的示例：



```
//validate user able to login with valid credentials
	    @Test
		public void Login_User() throws IOException
		{
	    	
		    driver.get("https://accounts.lambdatest.com/login");
            driver.findElement(By.xpath("//input[@name='email']")).sendKeys("User2@gmail.com");
			driver.findElement(By.xpath("//input[@name='password']")).sendKeys("TetsUser123");
	    	driver.findElement(By.xpath("//button[@class='sign-up-btn']")).click();
			  WebDriverWait wait= new WebDriverWait(driver, 15);
			  wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[@class='user-profile dropdown-toggle']"))));
			  String Current_url= driver.getCurrentUrl();
			  Assert.assertEquals("https://accounts.lambdatest.com/user/email-verification", Current_url);
			  System.out.println("user logged in sucesfully");
			  takescreenshot();
			  driver.findElement(By.xpath("//a[@class='user-profile dropdown-toggle']")).click();
			  driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
	    	}
	    
	    public void takescreenshot() throws IOException
		{
			TakesScreenshot scr= ((TakesScreenshot)driver);
		    File file1= scr.getScreenshotAs(OutputType.FILE);
		    
		   FileUtils.copyFile(file1, new File("C:\\Users\\navyug\\Desktop\\Login_user.PNG")); 
		}
```
 
[](unnamed-10-1.png)

## 8.不要忘记跨浏览器测试！

如今，所有Web应用程序都支持多种浏览器和版本。重要的是，您的代码应针对多个浏览器，而不是针对特定的浏览器。在特定的浏览器上运行代码会失去应用程序的跨浏览器兼容性。执行跨浏览器测试，以确保您的应用程序在所有主要浏览器上都能提供无缝的用户体验，我们可以扩展此测试的自动化范围。诸如TestNG之类的框架有助于轻松地在各种浏览器中执行测试。

下面的代码显示了如何通过TestNG在多个浏览器上运行自动化代码


```
public class crowssbrowser {
	
	static WebDriver driver;


	@Parameters("browser")
	@BeforeClass
	public static void Browser_Select(String browser)
	{
		if(browser.equalsIgnoreCase("firefox")) {

			System.setProperty("webdriver.firefox.marionette", "geckodriverpath");
			  driver = new FirefoxDriver();

		  // If browser is IE, then do this	  

		  }else if (browser.equalsIgnoreCase("chrome")) { 

			  // Here I am setting up the path for my IEDriver

			  System.setProperty("webdriver.chrome.driver", "chromedriverpath"); 
			    driver= new ChromeDriver() ;
		  }
		
	    driver.get("https://accounts.lambdatest.com/login");
		  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
```

XML代码：



```
<?xml ve
rsion="1.0" encoding="UTF-8"?>
 
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
 
<suite name="Suite" parallel="none">
 
 <test name="FirefoxTest">
 
 <parameter name="browser" value="firefox" />
 
 <classes>
 
 <class name="crowssbrowser" />
 
 </classes>
 
 </test>
 
 <test name="chrometest">
 
 <parameter name="browser" value="chrome" />
 
 <classes>
 
 <class name="crowssbrowser" />
 
 </classes>
 
 </test>
 
</suite>
```

上面的代码显示了一种以浏览器为参数的方法，其中设置了不同的浏览器驱动程序。使用TestNG XML文件，我们已将参数传递为不同的浏览器，在这些浏览器上将运行用于Firefox和chrome上的登录功能的代码。


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
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [功能测试与非功能测试](https://mp.weixin.qq.com/s/oJ6PJs1zO0LOQSTRF6M6WA)
- [自动化和手动测试，保持平衡！](https://mp.weixin.qq.com/s/mMr_4C98W_FOkks2i2TiCg)

## 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
- [JVM的对象和堆](https://mp.weixin.qq.com/s/iNDpTz3gBK3By_bvUnrWOA)
