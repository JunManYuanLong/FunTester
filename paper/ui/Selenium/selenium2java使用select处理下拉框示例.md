# selenium2java使用select处理下拉框示例


在学习selenium2java的时候，在写收货地址相关用例的时候碰到了下拉框，刚好练习了一下select的使用，现在分享，供大家参考。


```
	//删除添加收货地址
	public static void deleteAndAddUserAdress(WebDriver driver) throws InterruptedException {
		clickUser(driver);
		findElementByTextAndClick(driver, "个人信息");
		findElementByTextAndClick(driver, "收货地址");
		clickDeleteAdress(driver);
		sleep(0);
		clickSure(driver);
		AddAddress(driver);
		String name = getTextByXpath(driver, ".//*[@id='main']/div[2]/div/div/div[1]/p[1]");
		assertTrue("添加收获地址失败！", name.equals("收货人:测试收货人"));
	}
```

下面是具体的添加收货地址的方法：

```
//添加收货地址
	public static void AddAdress(WebDriver driver) {
		findElementByIdAndClick(driver, "add-address-btn");//点击添加地址
		findElementByXpathAndClearSendkeys(driver, ".//*[@id='LAY_layuipro1a']/div/div[1]/table/tbody/tr[1]/td[2]/div/input", "测试收货人");//添加收货人
		findElementByXpathAndClearSendkeys(driver, ".//*[@id='LAY_layuipro1a']/div/div[1]/table/tbody/tr[2]/td[2]/div/input", "13120454218");//输入手机号
		//选择省市县，以及详细地址
		Select province = new Select(findElementByid(driver, "province-select"));
		province.selectByIndex(1);
		Select city = new Select(findElementByid(driver, "city-select"));
		city.selectByIndex(1);			
		Select area = new Select(findElementByid(driver, "area-select"));
		area.selectByIndex(1);
		findElementByClassnameAndClearSendkeys(driver, "textarea", "我是测试地址。");
		clickSave(driver);
	}
```

### 往期文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
8. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
9. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
10. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
11. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
12. [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
13. [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)

## [公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>