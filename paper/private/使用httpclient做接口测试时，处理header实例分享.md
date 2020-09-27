# 使用httpclient做接口测试时，处理header实例分享



本人在做接口测试的过程中，遇到了一次服务端框架调整，把之前放在url里面的的一些公参放到了header里面固定的字段，由于涉及到了之前写过的接口自动化方法和用例。所以有些麻烦，经过了一段时间的尝试和修改，终于完成了，分享一下经验。

服务端框架调整思路如下：把以前的公参全都放入header对应的字段中，老接口依然使用url传参，新接口使用新框架，新框架兼容老接口的请求方式，但在某一个时间后可能会不兼容。

我之前处理过header的内容有cookie和user_agent，这里代码会有体现。由于多个项目cookies和公参的方式不一样，所以请求方法采用了同一个请求方法，但是header设置用了两套。思路就是有一个包含请求所需的header的list集合，不管谁来请求接口，只要设置一下这个list就ok了，更新参数也是同样的道理。

具体的请求代码如下：


```
/**
	 * 获取响应实体
	 *
	 * @param request 请求对象
	 * @return 返回json类型的对象
	 */
	public static JSONObject getHttpResponseEntityByJson(HttpRequestBase request) {
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeOut).setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
		request.setConfig(requestConfig);
		String url = request.getURI().toString();
		output(TAB + TAB + "请求地址：" + url);
		JSONObject jsonObject = new JSONObject();
		CloseableHttpResponse response = null;// 创建响应对象
		long data_size = 0;// 用于存放数据大小
		Map<String, String> info = getRequestInfo(request);
		String api_name = info.get("api_name");
		String type = info.get("type");
		String host_name = info.get("host_name");
		String method = info.get("method");
		String params = info.get("params");
		output(TAB + TAB + "请求参数是：" + params);
		request.addHeader(HTTP.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.167 Safari/537.36");// 符合应用防火墙
		// 如果已经设置过cookie的话，不设置默认cookie,这里之前设置过的header只有user_agent
		if (cookies != null && !Arrays.toString(request.getAllHeaders()).contains("Cookie"))
			request.addHeader("Cookie", cookies);// 添加cookie
		if (headers != null)//设置header，适应新的后台框架
			headers.forEach((header -> request.addHeader(header)));
		if (mark == 0)
			mark = getMark() / 100;
		Date start = getDate();// 记录开始时间
		try {
			response = httpsClient.execute(request);
		} catch (Exception e) {
			output("请求发生错误！", e);
			String message = "url:" + url + LINE + "参数：" + params + LINE + "错误：" + Arrays.toString(e.getStackTrace());
			new AlertOver("接口请求失败", message, url).sendSystemMessage();
		}
		Date end = getDate();// 记录结束时间
		double elapsed_time = getTimeDiffer(start, end);// 获取响应耗时
		if (response == null)
			return null;
		Header[] header = response.getAllHeaders();// 获取响应header
		int headerSize = header.length;
		for (int i = 0; i < headerSize; i++) {// 遍历header
			String name = header[i].getName();
			if (name.equals("Set-Cookie")) {// 处理cookies
				if (cookieKey) {
					cookies = null;
					cookieKey = false;
				}
				String value = header[i].getValue().split(";")[0];
				if (cookies == null)// 拼接cookie
					cookies = value;
				cookies = cookies + ";" + value;
			}
		}
		// 更新cookieKey
		if (cookieKey == false && cookies != null)
			cookieKey = true;
		int status = response.getStatusLine().getStatusCode();// 获取响应状态
		HttpEntity entity = response.getEntity();// 获取响应实体
		data_size = entity.getContentLength();// 获取相应数据大小
		if (data_size == -1) // 如果为-1，则重置data_size
			data_size = 0;
		String content = null;
		try {
			content = EntityUtils.toString(entity, HTTP.UTF_8);// 用string接收响应实体
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
		if (data_size == 0) {// 如果被重置或者没有获取到，则data_size等于解析string大小
			try {
				data_size = content.length();
			} catch (Exception e) {
				data_size = 1;
				output("获取响应长度异常！", e);
			}
		}
		if (status == 200) {
			try {
				jsonObject = JSONObject.fromObject(content);
			} catch (Exception e) {
				jsonObject.put("content", content);
				jsonObject.put("code", -2);
				output("响应内容不是 json 格式的！，默认转换为 json 格式！");
			}
		} else {
			jsonObject.put("HttpStatus", status);
			jsonObject.put("code", -2);
			jsonObject.put("content", content);
			String msg = "状态码错误：" + status + "，响应内容：" + content;
			output(msg);
			new AlertOver("响应状态码错误", msg).sendSystemMessage();
		}
		int code = jsonObject.getInt("code");
		MySqlTest.getInstance().saveApiTestDate(host_name, api_name, data_size, elapsed_time, status, type, mark,
				method, code);
		if (code != 0 && (code < 10000 || code == 500 || code % 1000 / 100 == 4 || code % 1000 / 100 == 5))
			outputResponse(jsonObject, code);//抛出响应信息,并通知
		return jsonObject;
	}
```
下面是两个项目的设置header的方法：


```
/**
	 * 添加通用参数，已经重载
	 *
	 * @param args
	 */
	public static JSONObject getParams(String token) {
		JSONObject args = new JSONObject();
		args.put("_t", _t);
		args.put("_app", _app);
		args.put("_v", _v);
		args.put("token", token);
		setHeaders(token);
		return args;
	}
 
	/**
	 * 设置header
	 *
	 * @param token
	 */
	private static void setHeaders(String token) {
		headers.clear();
		headers.add(getHeader("X-Client-Type", _t));
		headers.add(getHeader("X-Client-App", _app));
		headers.add(getHeader("X-Token", token));
	}
```

```
/**
	 * 设置header，设置前清空header，此方法在登录成功后执行
	 */
	public static void setHeaders() {
		headers.clear();
		headers.add(getHeader("X-token", loginKey));
	}
```
其中getHeader()的方法如下：


```
/**
	 * 生成header
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	public static Header getHeader(String name, String value) {
		return new Header() {
			@Override
			public String getName() {
				return name;
			}
 
			@Override
			public String getValue() {
				return value;
			}
 
			@Override
			public HeaderElement[] getElements() throws ParseException {
				return new HeaderElement[0];
			}
		};
	}
```
查到有实现类了，故作修改：

```
/**
	 * 生成header
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	public static Header getHeader(String name, String value) {
		return new BasicHeader(name, value);
	}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>