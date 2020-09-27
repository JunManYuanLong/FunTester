# java利用时间戳来获取UTC时间
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

由于项目原因，本人在做测试的时候发时间都是UTC时间，因此找了找解决方案，发现都是非常复杂，十几行代码实现此功能，其中主要都用在计算时间偏移量。我试了一下在calendar的getinstance方法参数修改并不能直接获取UTC时间，在尝试过之后终于找到一个简单的方法，通知data类直接获取时间戳，然后设置时间戳来达到转换时区的方法。分享代码，供大家参考。

本方法只适用于采用北京时间为标准时间的地区。


```
/**
	 * 获取calendar类对象，默认UTC时间
	 *
	 * @return
	 */
	public static Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(getDate().getTime() - 8 * 3600 * 1000));
		return calendar;
	}
```
在发一下几个关联的封装方法：


```
/**
	 * 获取当前星期数（按年）
	 *
	 * @return
	 */
	public static int getWeeksNum() {
		return getCalendar().get(Calendar.WEEK_OF_YEAR);
	}
 
	/**
	 * 获取月份
	 *
	 * @return
	 */
	public static int getMonthNum() {
		return getCalendar().get(Calendar.MONTH) + 1;
	}
 
	/**
	 * 获取当前是当月的第几天
	 *
	 * @return
	 */
	public static int getDayNum() {
		return getCalendar().get(Calendar.DAY_OF_MONTH);
	}
 
	/**
	 * 获取年份
	 *
	 * @return
	 */
	public static int getYearNum() {
		return getCalendar().get(Calendar.YEAR);
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
8. [测试之JVM命令脑图](https://mp.weixin.qq.com/s/qprqyv0j3SCvGw1HMjbaMQ)
9. [将json数据格式化输出到控制台](https://mp.weixin.qq.com/s/2IPwvh-33Ov2jBh0_L8shA)
10. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
11. [“双花”BUG的测试分享](https://mp.weixin.qq.com/s/0dsBsssNfg-seJ_tu9zFaQ)
12. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)

## [公众号地图](https://mp.weixin.qq.com/s/36RbP20beZ8oWJ9nLAxG3g) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)



> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>