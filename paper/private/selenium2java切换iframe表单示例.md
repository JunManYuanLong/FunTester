# selenium2java切换iframe表单示例
本人在学习selenium2java的期间，遇到了网页中使用iframe内嵌页面的问题，查看了相关资料，终于成功了，分享如下：


```
		//进入广告配置
		findElementByXpathAndClick(driver, ".//*[@id='certclient-01-02']/span");
		findElementByXpathAndClick(driver, ".//*[@id='certclient-01-02-05']");
		sleep(2);
		//切换iframe
		driver.switchTo().frame(findElementByXpath(driver, ".//*[@id='gdca-main-contents']/li[2]/iframe"));
		//点击删除
		findElementByXpathAndClick(driver, ".//*[@id='ctTsAdvertisement']/tbody/tr[1]/td[5]/button[3]");
		sleep(1);
		//切换到原来的表单
		driver.switchTo().defaultContent();
		//点击删除
		findElementByXpathAndClick(driver, "html/body/div[7]/div/div/div[2]/button[2]");
//		clickByJs(driver, findElementByXpath(driver, "html/body/div[7]/div/div/div[2]/button[2]"));
```
其中最后有一个弹框，一直无法定位，后来发现原来已经不再当前iframe中，费了不少时间才发现问题的源头。

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>