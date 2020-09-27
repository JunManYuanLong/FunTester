# selenium2java写一个设置秒杀价的脚本

本人在使用selenium做测试的时候，有时候需要去后台管理界面去设置一些测试数据，所以写了一些脚本，有一个比较有代表性的，因为这里的设置按钮和课程详情并不在一个页面，而是在列表页面。所以查找起来比较麻烦。先分享代码，供大家参考。

先放一下后台的页面和xml信息：
![](/blog/pic/20170906190455595.png)

下面是我的代码：


```
//设置秒杀价
	public void setSeckillPriceByCourseId(int courseId, int price) throws InterruptedException {
		clickCourse();//点击进去课程列表
		clickFormalCourse();//选择班课列表
		int status = findCourseByIdAndSetSeckillPrice(courseId, price);//根据courseid查找班课设置秒杀价
		if (status != 2) {
			outpu(status, "设置秒杀价失败！");
		}
	}
```
下面是具体的方法：

```
	/*
	 * 分页遍历课程，查找相应班课
	 * 返回值1：找到该课程，2：设置秒杀价完成，3：已经开始秒杀，4前四页未找到该课程
	 */
	public int findCourseByIdAndSetSeckillPrice(int courseId, int price) throws InterruptedException {
		int times = 0;//标记页面
		int mark = 0;//标记状态
		sleep(0);
		while(true) {
			times++;
			List<WebElement> seckills = findElementsByPartialText("秒杀");//获取所有秒杀状态按钮集合
			for (int i = 0; i < seckills.size(); i++) {//遍历集合中所有元素
				//获取date_course_id，来判断是否是等于参数id，然后点击
				String course = seckills.get(i).getAttribute("data-course_id");//获取该元素对应课程id
				if (course.equals(courseId+"")) {//比较id
					mark = 1;//更改标记，表示发现课程
					String status = seckills.get(i).getText();//获取秒杀状态
//					output(status);
					if (status.equals("开始秒杀")) {
						scrollToElement(seckills.get(i));//滚动到该元素
						clickByJs(seckills.get(i));//通过js点击
						sleep(0);
						findElementByIdAndClearSendkeys("input-modal_promotion_price", price);//设置秒杀价
						findElementByIdAndClick("btn-ok-product_promotion");//确定
						boolean key = true;
						while(key){//等待保存完毕，再次确认
							if (exists(By.xpath("/html/body/div[7]/h2")) && getTextByXpath("/html/body/div[7]/h2").equals("操作成功")) {
								key = false;
								break;
								}
							}
						clickSure();
						output("设置秒杀价成功！");
						mark = 2;//更改标记，表示已经完成秒杀
						} else if (status.equals("结束秒杀")) {
							output("该课程已经开始秒杀了！");
							mark = 3;//标记，表示已经在秒杀了
							output("已经开始秒杀！");
							}
					}
				}//遍历结束
			if (mark != 0) {
				return mark;
				}
			if (times > 2) {
				output("未找到该课程！");
				mark = 4;//更改标记，表示未找到
				return mark;
				}
			}
	}
```
中间js滚动到 某个元素的方法：

```
	//滚动到某个元素
	public void scrollToElement(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
	}
```

中间js点击某个元素的方法：

```
	//通过js点击
	public void clickByJs(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element);
		//第二种点击方法
//		((JavascriptExecutor) driver).executeScript("arguments[0].click()", question);
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


## [公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect) 



> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>