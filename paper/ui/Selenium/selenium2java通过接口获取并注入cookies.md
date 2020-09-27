# selenium2java通过接口获取并注入cookies
本人在使用selenium做测试的过程中，很多用例都是需要在登录状态下才能运行的，之前都是封装一个登录的方法，在学习了httpclient之后，想到一个通过请求登录接口来获取cookies值，再向浏览器插入cookies，使用户处于登录状态。分享代码，供大家参考。

下面是封装好的登录方法：

```
	public void loginByApi() throws InterruptedException, NoSuchAlgorithmException, JSONException, IOException {
		ApiLibrary apiLibrary = new ApiLibrary(getUserName(), getUserPassWord());//实例化接口类
		Map<String, String> cookies = apiLibrary.getCookiesArguments();//获取cookies信息
		addCookie(cookies);//向浏览器插入cookies
		sleep(1);//休眠等待
		refresh();//刷新
	}
```
下面是封装的api类的请求接口获取cookies的方法：

```
	//获取cookies，map集合
	public Map<String, String> getCookiesArguments() throws JSONException, IOException {
		Map<String, String> cookiesArgs = new HashMap<String, String>();//创建存放cookies的map集合
		CloseableHttpClient httpClient = HttpClients.createDefault();//创建并实例化连接对象
		JSONObject jsonObject = new JSONObject();//创建并实例化json对象
		jsonObject.put("did", "web");//设置登录类型
		jsonObject.put("telnum", userMobile);//设置帐号
		jsonObject.put("password", passWord);//设置密码
		output(userMobile);
		output(passWord);
		String arguments = changeJsonToArguments(jsonObject);//将json对象转化为接口参数
		String url = "http://beta-web.gaotu100.com/user/web/login";//接口地址
		HttpPost httpPost = new HttpPost(url + arguments);//创建并实例化post请求连接
		CloseableHttpResponse response = httpClient.execute(httpPost);//执行请求获取响应
		output(response.getStatusLine().getStatusCode());//输出状态码
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			output("返回状态错误！");
		}
		HttpEntity entity = response.getEntity(); //获取响应实体
		JSONObject ssString = new JSONObject(EntityUtils.toString(entity));//获取响应实体的json数据
		output(ssString.toString());
		//获取相应数据
		String chat_app_id = ssString.getString("chat_app_id");
		String chat_name = ssString.getString("chat_name");
		String chat_user_sig = ssString.getString("chat_user_sig");
		String name = urlEncoderText(ssString.getString("name"));//对昵称进行转码
		String sid = ssString.getString("session_id");
		String role = "0";//默认为0
		//将数据存入map集合中
		cookiesArgs.put("chat_app_id", chat_app_id);
		cookiesArgs.put("chat_name", chat_name);
		cookiesArgs.put("chat_user_sig", chat_user_sig);
		cookiesArgs.put("name", name);
		cookiesArgs.put("sid", sid);
		cookiesArgs.put("role", role);
		httpClient.close();//关闭链接
		return cookiesArgs;//返回map集合
	}
```
下面是向浏览器插入cookies的方法：

```
	public void addCookie(Map<String, String> args) {
		Set<String> keys = args.keySet();
		for(String key : keys){
			driver.manage().addCookie(new Cookie(key, args.get(key)));
		}
	}
```


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
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)



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


# [点击查看公众号地图](https://mp.weixin.qq.com/s/l_zkWzQL65OIQOjKIvdG-Q)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>