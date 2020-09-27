# selenium2java写一个小小的爬虫程序
本人在学习selenium的过程中，本人偶然接触到爬虫获取网页信息，自己写了一个简单的获取课程信息的方法。


```
String xpath = "xpath";
			for(int i=1;i<getNumByXpath(driver, xpath);i++){
				getCourseInfoOnPage(driver);
				nextPage();
			}
```
下面是获取本页所有信息的方法：


```
public static void getCourseInfoOnPage(WebDriver driver) throws IOException {
		for(int i =1;i<13;i++){
			String price = getTextByXpath(driver, "html/body/div[2]/div[3]/div[1]/ul/li["+i+"]/a/div[1]/div[2]/span");
			String coursename = getTextByXpath(driver, "html/body/div[2]/div[3]/div[1]/ul/li["+i+"]/a/div[2]/p[1]");
			String starttime = getTextByXpath(driver, "html/body/div[2]/div[3]/div[1]/ul/li["+i+"]/a/div[2]/p[2]/span[1]");
			String teachername = getTextByXpath(driver, "html/body/div[2]/div[3]/div[1]/ul/li["+i+"]/a/div[2]/p[2]/span[2]");
			outputAndSave("课程名称："+coursename+"；主讲老师："+teachername+"；课程价格："+price+"；开课时间："+starttime+"。");
		}
	}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>