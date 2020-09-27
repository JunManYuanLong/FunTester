# 使用httpclient中EntityUtils类解析entity遇到socket closed错误的原因


本人在使用httpclient做接口测试的时候，最近程序偶然报socket closed错误，上周经过排查发现是request.releaseConnection()这个方法搞得鬼，也是自己学艺不精，没有真正理解方法的含义，改掉之后其他接口就没有出现过这个问题，今天又遇到了，又重新排查了自己的方法，发现还有一种导致socket closed的原因，因为我的响应对象创建时用的是CloseableHttpResponse类，所以需要关闭，在某些时候response太大可能导致使用EntityUtils.toString(entity)解析实体的时候出错，个人理解是由于response的并未完全解析到entity里面时已经执行了close()方法导致的，试着把close()方法后置，完美解决问题。

下面是我的错误代码片段：


```
try {
			response.close();
		} catch (IOException e2) {
			output("响应关闭失败！", e2);
		}
		data_size = entity.getContentLength();// 获取相应数据大小
		if (data_size == -1) {// 如果为-1，则重置data_size
			data_size = 0;
		}
		String content = null;
		try {
			content = EntityUtils.toString(entity);// 用string接收响应实体
			
			EntityUtils.consume(entity);// 消耗响应实体
		} catch (ParseException e1) {
			output("解析响应实体异常！", e1);
		} catch (IOException e1) {
			output("解析响应实体时java IO 异常！", e1);
		} // 解析响应
```

下面是修改之后的代码片段：


```
String content = null;
		try {
			content = EntityUtils.toString(entity);// 用string接收响应实体
			EntityUtils.consume(entity);// 消耗响应实体
		} catch (ParseException e1) {
			output("解析响应实体异常！", e1);
		} catch (IOException e1) {
			output("解析响应实体时java IO 异常！", e1);
		} // 解析响应
		try {
			response.close();
		} catch (IOException e2) {
			output("响应关闭失败！", e2);
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
