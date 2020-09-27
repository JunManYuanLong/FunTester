# android UiAutomator长按实现控制按住控件时间的方法
本人在使用UiAutomator做测试的时候，遇到一些控件需要长按一会儿，比如录音功能，需要按住几秒，官方api不太好用，所以自己写了一个长按的方法。分享如下，供大家参考。


```
    /*
    * 根据resourceid获取控件并长按
    * /
	public void longclickUiObectByResourceId(String id) throws UiObjectNotFoundException {
		int x = getUiObjectByResourceId(id).getBounds().centerX();
		int y = getUiObjectByResourceId(id).getBounds().centerY();
		UiDevice.getInstance().swipe(x, y, x, y, 300);//最后一个参数单位是5ms
	}
	 /*
    * 根据文本获取控件并长按
    * /
	public void longclickUiObectByText(String text) throws UiObjectNotFoundException {
		int x = getUiObjectByText(text).getBounds().centerX();
		int y = getUiObjectByText(text).getBounds().centerY();
		UiDevice.getInstance().swipe(x, y, x, y, 300);//最后一个参数单位是5ms
	}
	 /*
    * 根据坐标并长按
    * /
		public void longclickUiObectByText(int x, int y) throws UiObjectNotFoundException {
		UiDevice.getInstance().swipe(x, y, x, y, 300);//最后一个参数单位是5ms
	}
```

文章写作时间较早了，UiAutomator1基础的API进行封装的，还可以封装几个根据classname、index、或者description都可以，这里就不一一写了，最重要的最后一个，不常用但是很有用，根据坐标点进行长按。还有一个根据坐标的数组进行轨迹的描绘，请参考[android uiautomator一个画心形图案的方法--代码的浪漫](https://mp.weixin.qq.com/s/byfAKHxD2i83VHnuaNgIZA)

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

## [公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>