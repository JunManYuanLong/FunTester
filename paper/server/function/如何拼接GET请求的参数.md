# 如何拼接GET请求的参数
本人在做接口测试的过程中，之前写了一个用字符串替换的方法来处理get接口的参数拼接，后来优化了这个方法，兼容了中文字符提示非法字符的情况，使用了java自带的urlencode方法。包括第一种方法，之前传的是json。分享代码，供大家参考。

更新：


```
	/**
	 * 方法已重载，获取get对象
	 * <p>方法重载，主要区别参数，会自动进行urlencode操作</p>
	 *
	 * @param url  表示请求地址
	 * @param args 表示传入数据
	 * @return 返回get对象
	 */
 
	public static HttpGet getHttpGet(String url, JSONObject args) {
		if (args == null || args.size() == 0)
			return getHttpGet(url);
		String uri = url + changeJsonToArguments(args);
		return getHttpGet(uri.replace(" ", ""));
	}
 
	/**
	 * 方法已重载，获取get对象
	 * <p>方法重载，主要区别参数，会自动进行urlencode操作</p>
	 *
	 * @param url  表示请求地址
	 * @param args 表示传入数据
	 * @return 返回get对象
	 */
	public static HttpGet getHttpGet(String url) {
		return new HttpGet(url);
	}
```
其中changjsontoarguments方法如下：


```
/**
	 * 把json数据转化为参数，为get请求和post请求stringentity的时候使用
	 *
	 * @param argument 请求参数，json数据类型，map类型，可转化
	 * @return 返回拼接参数后的地址
	 */
	public static String changeJsonToArguments(JSONObject argument) {
		Set<String> keys = argument.keySet();
		for (String key : keys) {
			String value = argument.getString(key);
			argument.put(key, urlEncoderText(value));
		}
		String one = argument.toString();
		String two = "?" + one.substring(1, one.length() - 1).replace(",", "&").replace(":", "=").replace("\"", "");
		return two;
	}
```
--------------------------分割线------------------------------


```
	/**
	 * 把json数据转化为参数，为get请求和post请求stringentity的时候使用
	 * 
	 * @param argument
	 *            请求参数，json数据类型，map类型，可转化
	 * @return 返回拼接参数后的地址
	 */
	@SuppressWarnings("unused")
	private String changeJsonToArguments(JSONObject argument) {
		String one = argument.toString();
		String two = "?" + one.substring(1, one.length() - 1).replace(",", "&").replace(":", "=").replace("\"", "");
		return two;
	}
 
 
	private String changeJsonToArguments(Map<String, String> apiCase) {
		Set<String> keys = apiCase.keySet();
		StringBuffer arg = new StringBuffer("?");
		for (String key : keys) {
			arg.append((key) + "=" + urlEncoderText(apiCase.get(key)) + "&");
		}
		return arg.deleteCharAt(arg.length() -1).toString();//此处为了兼容case内容为空
	}
```

```
/**
	 * 把json数据转化为参数，为get请求和post请求stringentity的时候使用
	 * 
	 * @param argument
	 *            请求参数，json数据类型，map类型，可转化
	 * @return 返回拼接参数后的地址
	 */
	@SuppressWarnings("unused")
	private String changeJsonToArguments(JSONObject argument) {
		String one = argument.toString();
		String two = "?" + one.substring(1, one.length() - 1).replace(",", "&").replace(":", "=").replace("\"", "");
		return two;
	}
 
 
	private String changeJsonToArguments(Map<String, String> apiCase) {
		Set<String> keys = apiCase.keySet();
		StringBuffer arg = new StringBuffer("?");
		for (String key : keys) {
			arg.append((key) + "=" + urlEncoderText(apiCase.get(key)) + "&");
		}
		return arg.deleteCharAt(arg.length() -1).toString();//此处为了兼容case内容为空
	}
```

```
	/**
	 * url进行转码，常用于网络请求
	 * 
	 * @param text
	 *            需要加密的文本
	 * @return 返回加密后的文本
	 */
	public String urlEncoderText(String text) {
		String result = "";
		try {
			result = java.net.URLEncoder.encode(text, "utf-8");
		} catch (UnsupportedEncodingException e) {
			output("数据格式错误！");
			e.printStackTrace();
		}
		return result;
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
13. [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)


### 非技术文章精选
1. [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
2. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
3. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
4. [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
5. [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
6. [测试之《代码不朽》脑图](https://mp.weixin.qq.com/s/2aGLK3knUiiSoex-kmi0GA)
7. [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
8. [优秀软件开发人员的态度](https://mp.weixin.qq.com/s/0uEEeFaR27aTlyp-sm61bA)
9. [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
10. [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
11. [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
12. [自动化测试解决了什么问题](https://mp.weixin.qq.com/s/96k2I_OBHayliYGs2xo6OA)

