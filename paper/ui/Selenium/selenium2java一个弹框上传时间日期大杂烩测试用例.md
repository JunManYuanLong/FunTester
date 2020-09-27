# selenium2java一个弹框上传时间日期大杂烩测试用例


本人在学习selenium2java的时候，遇到过一个弹框上传大杂烩的用例，特别是有一个选时间的，得先选时，再选分。费死老劲了重要写完，没啥大坑，就是比较复杂。


```
//生成班课
	public static void createFormalCourseAndSale(WebDriver driver, String grade, String subject) throws InterruptedException {
		clickCourse(driver);
		clickFormalCourse(driver);
		clickAddCourse(driver);
		findElementByIdAndClearSendkeys(driver, "input-title", "测试班课"+grade+subject);//输入课程标题
		findElementByIdAndClick(driver, "button-toggle-grade");//选择年级
		findElementByTextAndClick(driver, grade);
		findElementByIdAndClick(driver, "button-toggle-subject");//选择科目
		findElementByTextAndClick(driver, subject);
		findElementByIdAndClearSendkeys(driver, "input-description", "测试招生人数1人");//输入知识点
		findElementByIdAndClick(driver, "button-toggle-teacher_id");//选择主讲老师
		findElementByTextAndClick(driver, "李");
		findElementByIdAndClick(driver, "input-enroll_deadline_date");//选择截止日期
		findElementByTextAndClick(driver, "30");
		sleep(0);
		findElementByIdAndClick(driver, "input-enroll_deadline_time");//选择截止时间
		sleep(0);
		findElementByXpathAndClick(driver, "html/body/div[8]/div[3]/div/div[2]/div[1]");
		sleep(0);
		findElementByXpathAndClick(driver, "html/body/div[8]/div[3]/div/div[3]/div[1]");
		findElementByIdAndClearSendkeys(driver, "input-time_des", "测试招生人数1人");//输入时间描述
		findElementByIdAndClearSendkeys(driver, "input-orig_price", 230);//输入价格
		findElementByIdAndClearSendkeys(driver, "input-totalEnrollLimit", 1);//输入人数
		findElementByIdAndClearSendkeys(driver, "input-video_url", "http://cdn.gaotu100.com/video/mingdoudou0505.mp4");//输入介绍视频视频
		findElementByIdAndClearSendkeys(driver, "input-detail_url", "http://www.gaotu100.com/course/XE4007.html");//输入班课详情
		findElementByIdAndClick(driver, "input-start_time_date");//选择开始日期
		findElementByTextAndClick(driver, "29");
		sleep(0);
		findElementByIdAndClick(driver, "input-start_time_time");//选择开始时间
		sleep(0);
		findElementByXpathAndClick(driver, "html/body/div[9]/div[3]/div/div[2]/div[1]");
		sleep(0);
		findElementByXpathAndClick(driver, "html/body/div[9]/div[3]/div/div[3]/div[1]");
		findElementByIdAndClick(driver, "input-end_time_date");//选择截止日期
		findElementByTextAndClick(driver, "30");
		sleep(0);
		findElementByIdAndClick(driver, "input-end_time_time");//选择截止时间
		sleep(0);
		findElementByXpathAndClick(driver, "html/body/div[10]/div[3]/div/div[2]/div[1]");
		sleep(0);
		findElementByXpathAndClick(driver, "html/body/div[10]/div[3]/div/div[3]/div[1]");
		findElementByIdAndClearSendkeys(driver, "input-deliver_cost", 20);//输入运费
		scrollToTop(driver);//滚动最上方
		findElementByIdAndClick(driver, "button-toggle-teacher_list");//选择辅导老师
		findElementByXpathAndClick(driver, ".//*[@id='choice-container-teacher_list']/div[6]/div/label/i");
		findElementByIdAndClick(driver, "btn-ok-teacher_list");//确定
		sleep(0);
		findElementByIdAndClick(driver, "button-toggle-tag_list");//选择课程特色
		findElementByXpathAndClick(driver, ".//*[@id='choice-container-tag_list']/div[1]/div/label/i");
		findElementByXpathAndClick(driver, ".//*[@id='choice-container-tag_list']/div[2]/div/label/i");
		findElementByXpathAndClick(driver, ".//*[@id='choice-container-tag_list']/div[3]/div/label/i");
		findElementByXpathAndClick(driver, ".//*[@id='choice-container-tag_list']/div[4]/div/label/i");
		findElementByIdAndClick(driver, "btn-ok-tag_list");//确定
		sleep(0);
		findElementByIdAndClick(driver, "button-toggle-recommend_list");//选择推荐课程
		findElementByXpathAndClick(driver, ".//*[@id='choice-container-recommend_list']/div[7]/div/label/i");
		sleep(0);
		findElementByXpathAndClick(driver, ".//*[@id='choice-container-recommend_list']/div[8]/div/label/i");
		findElementByIdAndClick(driver, "btn-ok-recommend_list");//确定
		sleep(0);
		findElementByIdAndClearSendkeys(driver, "input-file-cover", "C:\\Users\\fankaiqiang\\Desktop\\123.JPG");//上传图片
		clickSave(driver);
		sleep(0);
		clickSure(driver);
		boolean key1 = true;
		while(key1){//等待保存完毕，再次确认
			if (exists(driver, By.xpath("html/body/div[12]/h2")) & getTextByXpath(driver, "html/body/div[12]/h2").equals("保存成功")) {
				key1 = false;
				break;
			}
		}
		clickSure(driver);
		boolean key = true;
		while(key){//等待直播课页面出现
			if (exists(driver, By.id("input-begin_time_date"))) {
				key = false;
				break;
			}
		}
		findElementByIdAndClearSendkeys(driver, "input-title", "直播课1");//输入直播课名称
		findElementByIdAndClick(driver, "input-begin_time_date");//选择开始日期
		findElementByTextAndClick(driver, "30");
		sleep(0);
		findElementByIdAndClick(driver, "input-begin_time_time");//选择开始时间
		sleep(0);
		findElementByXpathAndClick(driver, "html/body/div[3]/div[3]/div/div[2]/div[2]");//选择1点开始
		sleep(0);
		findElementByXpathAndClick(driver, "html/body/div[3]/div[3]/div/div[3]/div[1]");
		findElementByIdAndClick(driver, "input-end_time_time");//选择结束时间
		sleep(0);
		findElementByXpathAndClick(driver, "html/body/div[4]/div[3]/div/div[2]/div[3]");//选择两点结束
		sleep(0);
		findElementByXpathAndClick(driver, "html/body/div[4]/div[3]/div/div[3]/div[1]");
		findElementByIdAndClick(driver, "button-toggle-live_address_id");//选择直播间
		findElementByXpath(driver, ".//*[@id='dropdown-live_address_id']/li["+getRandomInt(5)+"]/a");//此处随机设置直播间避免时间冲突
		findElementByIdAndClick(driver, "input-exercise_deadline_date");//选择作业截止日期
		findElementByTextAndClick(driver, "30");
		sleep(0);
		findElementByIdAndClick(driver, "input-exercise_deadline_time");//选择作业截止时间
		sleep(0);
		findElementByXpathAndClick(driver, "html/body/div[5]/div[3]/div/div[2]/div[7]");//选择六点截止
		sleep(0);
		findElementByXpathAndClick(driver, "html/body/div[5]/div[3]/div/div[3]/div[1]");
		findElementByIdAndClick(driver, "input-revise_deadline_date");//选择作业订正日期
		findElementByTextAndClick(driver, "30");
		sleep(0);
		findElementByIdAndClick(driver, "input-revise_deadline_time");//选择作业订正时间
		sleep(0);
		findElementByXpathAndClick(driver, "html/body/div[6]/div[3]/div/div[2]/div[8]");//选择七点截止
		sleep(0);
		findElementByXpathAndClick(driver, "html/body/div[6]/div[3]/div/div[3]/div[1]");
		findElementByIdAndClearSendkeys(driver, "input-exercise_number", 5);//输入试题数目
		sleep(0);
		clickSave(driver);
		sleep(0);
		clickSure(driver);
		boolean key2 = true;
		while(key2){//等待保存完毕，再次确认
			if (exists(driver, By.xpath("html/body/div[8]/h2")) & getTextByXpath(driver, "html/body/div[8]/h2").equals("保存成功")) {
				key2 = false;
				break;
			}
		}
		clickSure(driver);
		sleep(1);
		findElementByTextAndClick(driver, "返回");
		clickCourse(driver);
		clickFormalCourse(driver);
		findElementByXpathAndClick(driver, ".//*[@id='btnContainer0']/div[1]/button[2]");//点击审核
		findElementByXpathAndClick(driver, ".//*[@id='btnContainer0']/div[1]/ul/li[2]/a");//点击在售
		sleep(1);
		clickSure(driver);
	}
```
这里着重说一下中间这个地方的几行代码，判断条件必须是&链接，不能用&&，不然会报错的。

```
boolean key2 = true;
		while(key2){//等待保存完毕，再次确认
			if (exists(driver, By.xpath("html/body/div[8]/h2")) & getTextByXpath(driver, "html/body/div[8]/h2").equals("保存成功")) {
				key2 = false;
				break;
			}
		}
```

发几张弹框的图片

![](/blog/pic/20170526095758324.png)

![](/blog/pic/20170526095804820.png)

![](/blog/pic/20170526095812418.png)

![](/blog/pic/20170526095824371.png)

掘金备份：
![](https://user-gold-cdn.xitu.io/2019/9/10/16d191057bc65055?w=199&h=269&f=png&s=6760)
![](https://user-gold-cdn.xitu.io/2019/9/10/16d19106fee10b79?w=598&h=493&f=png&s=26930)
![](https://user-gold-cdn.xitu.io/2019/9/10/16d19108f48d5a7a?w=436&h=331&f=png&s=20040)
![](https://user-gold-cdn.xitu.io/2019/9/10/16d1910a770a6fc9?w=441&h=253&f=png&s=15716


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
7. [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)

# [点击查看公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ)



> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>