# httpclient接口测试中如何实现自动设置和自动更新cookie

<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
本人在做接口测试的过程中，遇到一个问题，网站使用cookie进行权限的校验，每次等过之后cookie里面的一个值就会发生变化。之前的做法是每次执行的时候重新获取一下cookie里面的各项数值，然后把cookie带入到接下来的各项请求当中去。然而有些地方又需要单独设置一些cookie，这样就导致我很多接口请求方法的封装都需要进行cookie的处理。我想了一个办法，把每次请求响应里面的setcookie的内容存下来，以后的每次请求都带上这个cookie，如果有单独设置好的cookie的话就不添加默认的cookie。经过尝试效果还不错，分享代码供大家参考。

下面是执行请求的方法，关于cookies的代码都在里面了看一下：


```
	/**
	 * 获取响应实体
	 * 
	 * @param request
	 *            请求对象
	 * @return 返回json类型的对象
	 */
	public static JSONObject getHttpResponseEntityByJson(HttpRequestBase request) {
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeOut)
				.setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
		request.setConfig(requestConfig);
		output("请求地址：" + request.getURI());
		JSONObject jsonObject = new JSONObject();
		CloseableHttpResponse response = null;// 创建响应对象
		long data_size = 0;// 用于存放数据大小
		Map<String, String> info = getRequestInfo(request);
		String api_name = info.get("api_name");
		String type = info.get("type");
		String host_name = info.get("host_name");
		String method = info.get("method");
		String params = info.get("params");
		output("请求参数是：" + params);
		request.addHeader(HTTP.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 "
				+ "(KHTML, like Gecko) Chrome/64.0.3282.167 Safari/537.36");// 符合应用防火墙
		// 如果已经设置过cookie的话，不设置默认cookie,这里之前设置过的header只有user_agent
		if (cookies != null && cookies.length() > 1 && request.getAllHeaders().length < 2)
			request.addHeader("Cookie", cookies);// 添加cookie
		if (mark == 0) {
			mark = getMark() / 100;
		}
		Date start = getDate();// 记录开始时间
		try {
			response = httpsClient.execute(request);
		} catch (ClientProtocolException e1) {
			output("client请求异常", e1);
		} catch (IOException e1) {
			output("执行请求时java IO 异常！", e1);
		} catch (Exception e) {
			output("请求发生错误！", e);
		}
		Date end = getDate();// 记录结束时间
		double elapsed_time = getTimeDiffer(start, end);// 获取响应耗时
		if (response == null) {
			return null;
		}
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
					cookies =  value;
				cookies = cookies + ";" + value;
			}
		}
		// 更新cookieKey
		if (cookieKey == false && cookies != null) {
			cookieKey = true;
		}
		int status = response.getStatusLine().getStatusCode();// 获取响应状态
		HttpEntity entity = response.getEntity();// 获取响应实体
		data_size = entity.getContentLength();// 获取相应数据大小
		if (data_size == -1) {// 如果为-1，则重置data_size
			data_size = 0;
		}
		String content = null;
		try {
			content = EntityUtils.toString(entity, "utf-8");// 用string接收响应实体
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
				// output("响应内容：" + content, e);
				output("响应内容不是 json 格式的！，默认转换为 json 格式！");
			}
		} else {
			output("相应状态码错误，相应内容：" + content);
		}
		MySqlTest.getInstance().saveApiTestDate(host_name, api_name, data_size, elapsed_time, status, type, mark,
				method);
		return jsonObject;
	}
```
其中两个变量的值设置如下：


```
	public static String cookies;// string类型的cookie值
	public static boolean cookieKey = false;// 表示cookie默认值是否被设置，用于是否更新cookie的标志
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>