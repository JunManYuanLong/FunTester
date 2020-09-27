# httpclient发送https协议请求以及javax.net.ssl.SSLHandshakeException解决办法
本人在做接口自动化的过程中，遇到了请求第三方https协议请求，在经过了短暂的知识重新学习之后，写完代码执行起来总是遇到一个异常，在用客户端执行请求的时候抛出来的，下面是异常的信息：


```
Exception in thread "main" javax.net.ssl.SSLHandshakeException: Received fatal alert: handshake_failure
	at sun.security.ssl.Alerts.getSSLException(Alerts.java:192)
	at sun.security.ssl.Alerts.getSSLException(Alerts.java:154)
	at sun.security.ssl.SSLSocketImpl.recvAlert(SSLSocketImpl.java:2023)
	at sun.security.ssl.SSLSocketImpl.readRecord(SSLSocketImpl.java:1125)
	at sun.security.ssl.SSLSocketImpl.performInitialHandshake(SSLSocketImpl.java:1375)
	at sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1403)
	at sun.security.ssl.SSLSocketImpl.startHandshake(SSLSocketImpl.java:1387)
	at org.apache.http.conn.ssl.SSLConnectionSocketFactory.createLayeredSocket(SSLConnectionSocketFactory.java:394)
	at org.apache.http.conn.ssl.SSLConnectionSocketFactory.connectSocket(SSLConnectionSocketFactory.java:353)
	at org.apache.http.impl.conn.DefaultHttpClientConnectionOperator.connect(DefaultHttpClientConnectionOperator.java:134)
	at org.apache.http.impl.conn.PoolingHttpClientConnectionManager.connect(PoolingHttpClientConnectionManager.java:353)
	at org.apache.http.impl.execchain.MainClientExec.establishRoute(MainClientExec.java:380)
	at org.apache.http.impl.execchain.MainClientExec.execute(MainClientExec.java:236)
	at org.apache.http.impl.execchain.ProtocolExec.execute(ProtocolExec.java:184)
	at org.apache.http.impl.execchain.RetryExec.execute(RetryExec.java:88)
	at org.apache.http.impl.execchain.RedirectExec.execute(RedirectExec.java:110)
	at org.apache.http.impl.client.InternalHttpClient.doExecute(InternalHttpClient.java:184)
	at org.apache.http.impl.client.CloseableHttpClient.execute(CloseableHttpClient.java:82)
	at org.apache.http.impl.client.CloseableHttpClient.execute(CloseableHttpClient.java:107)
```
查阅了很多资料，大部分都说是因为java本身安全机制引起的，重新去官网下载jar包替换即可。以下是修改方法：

> 因为jdk中jce的安全机制导致报的错，要去oracle官网下载对应的jce包替换jdk中的jce包。
jce所在地址： %JAVA_HOME%\jre\lib\security里的local_policy.jar,US_export_policy.jar

[JDK7](http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html)
[JDK8](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)

具体异常教程如下：[传送门](https://blog.csdn.net/taiyangdao/article/details/54707184)

在原因的第二种里面，有一个需要校验本身的TLS的版本和服务端版本是否一致，我就是在这里出了问题，导致的这个异常。因为我一直用的默认参数去创建新的套接字对象。下面是我用Charles拦截的请求的header信息：

![](/blog/pic/20180109133348068.png)

上面圈起来的地方就是现实的服务器的TLS版本，相应地改掉自己代码的中设置版本的地方即可。

下面是我的代码：大多数参考了网上的教程，自己做了一些修改，大同小异。下面是获取SSLcontext对象的方法，实现了X509TrustManager接口，里面方法不用修改。


```
	/**
	 * 获取SSL套接字对象 重点重点：设置tls协议的版本
	 * 
	 * @return
	 */
	public static SSLContext createIgnoreVerifySSL() {
		SSLContext sslContext = null;// 创建套接字对象
		try {
			sslContext = SSLContext.getInstance("TLSv1.2");//指定TLS版本
		} catch (NoSuchAlgorithmException e) {
			SourceCode.getInstance().output("创建套接字失败！", e);
		}
		// 实现X509TrustManager接口，用于绕过验证
		X509TrustManager trustManager = new X509TrustManager() {
			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}
 
			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
					String paramString) throws CertificateException {
			}
 
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		try {
			sslContext.init(null, new TrustManager[] { trustManager }, null);//初始化sslContext对象
		} catch (KeyManagementException e) {
			SourceCode.getInstance().output("初始化套接字失败！", e);
		}
		return sslContext;
	}
```
下面是创建https协议的client的方法，其中用到了连接池的使用：


```
	/**
	 * 获取https协议请求对象
	 * 
	 * @return
	 */
	public static CloseableHttpClient getCloseableHttpsClients() {
		// 采用绕过验证的方式处理https请求
		SSLContext sslcontext = createIgnoreVerifySSL();
		// 设置协议http和https对应的处理socket链接工厂的对象
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(sslcontext)).build();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		HttpClients.custom().setConnectionManager(connManager);
		// 创建自定义的httpsclient对象
		CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
		return client;
	}
```
还有一个继承httpclient类，重写createDefault()方法来实现请求https的，经过实验，对于一些https协议是没有问题的。


### 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
- [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
- [python plotly处理接口性能测试数据方法封装](https://mp.weixin.qq.com/s/NxVdvYlD7PheNCv8AMYqhg)
- [单点登录性能测试方案](https://mp.weixin.qq.com/s/sv8FnvIq44dFEq63LpOD2A)



### 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
- [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
- [测试之《代码不朽》脑图](https://mp.weixin.qq.com/s/2aGLK3knUiiSoex-kmi0GA)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [优秀软件开发人员的态度](https://mp.weixin.qq.com/s/0uEEeFaR27aTlyp-sm61bA)
- [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [自动化测试解决了什么问题](https://mp.weixin.qq.com/s/96k2I_OBHayliYGs2xo6OA)
- [17种软件测试人员常用的高效技能-上](https://mp.weixin.qq.com/s/vrM_LxQMgTSdJxaPnD_CqQ)
- [17种软件测试人员常用的高效技能-下](https://mp.weixin.qq.com/s/uyWdVm74TYKb62eIRKL7nQ)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)
- [编写测试用例的17个技巧](https://mp.weixin.qq.com/s/2OPKYEQkl3o1M9fenF-uMA)


### 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
- [JVM的对象和堆](https://mp.weixin.qq.com/s/iNDpTz3gBK3By_bvUnrWOA)
