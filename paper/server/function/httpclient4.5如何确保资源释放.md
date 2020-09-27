# httpclient4.5如何确保资源释放

更新：releaseConnection()这个方法已经不再推荐了，我用的httpclient4.5的jar包，不需要对request进行这个操作了，看官方文档解释是更换了连接池管理类，最新的是：PoolingHttpClientConnectionManager。

在请求失败的时候response为空，故关闭之前做非空校验。下面是我新的方法：


```
	/**
	 * 根据响应获取响应实体
	 *
	 * @param response
	 * @return
	 */
	private static String getContent(CloseableHttpResponse response) {
		HttpEntity entity = response.getEntity();// 获取响应实体
		String content = EMPTY;
		try {
			content = EntityUtils.toString(entity, UTF_8);// 用string接收响应实体
			EntityUtils.consume(entity);// 消耗响应实体，并关闭相关资源占用
			if (response != null) response.close();
		} catch (ParseException e1) {
			output("解析响应实体异常！", e1);
		} catch (IOException e1) {
			output("解析响应实体时java IO 异常！", e1);
		}
		return content;
	}
```

--------------------------------分割线--------------------------------------

本人在学习使用httpclient的过程中，对于资源释放的一直很不理解，最近特意研究了一下这块，网上很多教程和文章造成了一些误导，可能是因为时间比较久了，版本更新导致的，我的版本是httpclient4.5，关于资源释放的分享一下自己的理解，如有不正确的地方，还请指出。

以下内容是我关于资源释放的理解，建立在本身项目的基础上的，有些地方并不是官方给的方法，主要是在消耗相应实体方面，我并没有使用abort()方法，因为没有这个需求。本文所以代码只是建立在普通请求的基础上，不涉及连接池和连接管理器相关内容。

关于response如何关闭：


```
CloseableHttpResponse response = null;// 创建响应对象
		//中间做一些事情
		try {
			response.close();
		} catch (IOException e2) {
			output("响应关闭失败！", e2);
		}
```

这里插一句，只有你确定响应长度之后才能用这个方法，不然你得关闭各种流，然后调用一个consume()方法。或者直接用abort()方法，或者使用官方推荐关闭方法。

 

推荐消耗实体内容的方式是使用它的HttpEntity#getContent()或HttpEntity#writeTo(OutputStream)方法。

关于释放连接，这个就比较简单了，请求完成之后，执行释放连接的方法就可以了：

`request.releaseConnection();`

在释放后可以复用的，之前看到一些方法有些在释放后是不可以复用的，这个具体得在连接池和连接管理器中再具体介绍了。
 

最后的最后的，一定要关闭客户端：

```
protected static CloseableHttpClient httpClient = getCloseableHttpClients();
//中间做一些事情，最后一定要关闭
	httpClient.close();
```

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
