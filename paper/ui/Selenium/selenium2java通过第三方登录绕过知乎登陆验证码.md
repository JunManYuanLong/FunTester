# selenium2java通过第三方登录绕过知乎登陆验证码

本人在学习使用selenium2java的过程中，突然想把知乎首页的推荐内容放到本地或者把一个问题的优秀回答整理一下。可以知乎登录时那个选中倒立的汉字的验证码不胜其烦，后来想到用第三方登录绕过了知乎帐号登录。试了一下，感觉效果很不错，分享一下，供大家参考。


```
driver.get("https://www.zhihu.com/");
		findElementByTextAndClick(driver, "登录");//点击知乎登录
		findElementByXpathAndClick(driver, "html/body/div[1]/div/div[2]/div[2]/form/div[4]/span");//点击第三方帐号登录
		String homehandle = driver.getWindowHandle();//获取当前handle
		findElementByClassNameAndClick(driver, "sprite-index-icon-qq");//点击QQ登录
		Set<String> handles = driver.getWindowHandles();//获取当前全部handle
		for(String handle : handles){//遍历获取新窗口handle
			if (handle.equals(homehandle) == false){
			driver.switchTo().window(handle);//跳转新页面
			driver.manage().window().maximize();//最大化
			}
		}
		driver.switchTo().frame("ptlogin_iframe");//切入页面iframe
		findElementByIdAndClick(driver, "img_out_1009329307");//选择自己QQ登录
		for(String handle : handles){//回到知乎页面
			if (handle.equals(homehandle) == true){
			driver.switchTo().window(handle);//回到知乎
			driver.manage().window().maximize();//最大化
			}
		}
```

比较简单，中间有个iframe，没其他难点了。


## 往期文章精选

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

## [公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>