# httpclient获取响应实体和信息的封装方法
2018年07月19日更新，主要是解耦之后方法很多地方发生了变化，httpclient用了连接池方式，作为一个静态变量处理，请求头和响应头以及cookies设置都有了相关处理方法，本来这个方法已经快超过100行了，解耦之后分成了几个小方法，方便修改和调试。分两部分，一部分是框架，只做了公共处理，另外一部分是每个项目的base类需要重新实现一些这个方法来处理header信息以及需要特殊处理的地方。分享代码如下：



```
/**
	 * 获取响应实体
	 *
	 * @param request 请求对象
	 * @return 返回json类型的对象
	 */
	protected static JSONObject getHttpResponse(HttpRequestBase request) {
		beforeRequest(request);
		JSONObject jsonObject = new JSONObject();
		RequestInfo requestInfo = new RequestInfo(request);
		CloseableHttpResponse response = null;// 创建响应对象
		Date start = getDate();// 记录开始时间
		try {
			response = ClientManage.httpsClient.execute(request);
		} catch (Exception e) {
			new Thread(() -> new AlertOver("接口请求失败", requestInfo.toString() + Arrays.toString(e.getStackTrace()), requestInfo.getUrl()).sendSystemMessage()).start();
			return jsonObject;
		}
		Date end = getDate();// 记录结束时间
		double elapsed_time = getTimeDiffer(start, end);// 获取响应耗时
		afterResponse(response);
		int status = response.getStatusLine().getStatusCode();// 获取响应状态
		String content = getContent(response);
		long data_size = content.length();
		jsonObject = getResponse(content);
		int code = checkCode(jsonObject, requestInfo.getUrl());
		if (status != HttpStatus.SC_OK)
			new Thread(() -> new AlertOver("响应状态码错误", "状态码错误：" + status, requestInfo.getUrl()).sendSystemMessage());
		MySqlTest.saveApiTestDate(requestInfo, data_size, elapsed_time, status, getMark(), code, LOCAL_IP, COMPUTER_USER_NAME);
		return jsonObject;
	}
```
下面是里面封装方法的依次分享：


```
/**
	 * 发送请求之前，配置请求管理器，设置IP，user_agent和cookie
	 *
	 * @param request
	 */
	private static void beforeRequest(HttpRequestBase request) {
		request.setConfig(requestConfig);//设置请求配置
		request.addHeader(getHeader("X-FORWARDED-FOR", getRandomIP()));//随机生成ip
		request.addHeader(HTTP.USER_AGENT, USER_AGENT);
		if (cookies != null && !Arrays.toString(request.getAllHeaders()).contains(REQUEST_HEADER_COOKIE))
			request.addHeader(REQUEST_HEADER_COOKIE, cookies);// 添加cookie
	}
```
requestinfo类的方法：


```
package com.fission.source.httpclient;
 
import com.fission.source.source.SourceCode;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
 
import java.io.IOException;
 
/**
 * 请求信息封装类
 */
@Data
public class RequestInfo extends SourceCode {
	/**
	 * 接口地址
	 */
	String apiName;
	/**
	 * 请求的url
	 */
	String url;
	/**
	 * 请求的uri
	 */
	String uri;
	/**
	 * 方法，get/post
	 */
	String method;
	/**
	 * 域名
	 */
	String host;
	/**
	 * 协议类型
	 */
	String type;
	/**
	 * 参数
	 */
	String params;
 
	/**
	 * 通过request获取请求的相关信息，并输出部分信息
	 *
	 * @param request
	 */
	public RequestInfo(HttpRequestBase request) {
		getRequestInfo(request);
		if (!host.contains("alertover") && !host.contains("lonelymind"))
			output(TAB + TAB + TAB + "请求uri：" + uri + LINE + TAB + TAB + TAB + "请求参数是：" + params);
	}
 
	/**
	 * 封装获取请求的各种信息的方法
	 *
	 * @param request 传入请求对象
	 * @return 返回一个map，包含api_name,host_name,type，method，params
	 */
	private void getRequestInfo(HttpRequestBase request) {
		method = request.getMethod();// 获取method
		uri = request.getURI().toString();// 获取uri
		getRequestUrl(uri);
		String one = url.substring(url.indexOf("//") + 2);// 删除掉http://
		apiName = one.substring(one.indexOf("/"));// 获取接口名
		host = one.substring(0, one.indexOf("/"));// 获取host地址
		type = url.substring(0, url.indexOf("//") - 1);// 获取协议类型
		if (method.equals(REQUEST_TYPE_GET)) {
			params = uri.substring(uri.indexOf("?") + 1, uri.length());
		} else if (method.equals(REQUEST_TYPE_POST)) {
			getPostRequestParams(request);
		}
	}
 
	/**
	 * 获取请求url，遇到get请求，先截取
	 *
	 * @param uri
	 */
	private void getRequestUrl(String uri) {
		url = uri;
		if (uri.contains("?")) url = uri.substring(0, uri.indexOf("?"));
	}
 
	/**
	 * 获取post请求的参数
	 *
	 * @param request
	 */
	private void getPostRequestParams(HttpRequestBase request) {
		HttpPost httpPost = (HttpPost) request;// 强转httppost请求
		HttpEntity entity = httpPost.getEntity();// 获取实体
		if (entity == null) return;
		try {
			params = EntityUtils.toString(entity);// 解析实体
			EntityUtils.consume(entity);// 确保实体消耗
		} catch (UnsupportedOperationException e) {
			params = "entity类型：" + entity.getClass();
		} catch (ParseException e) {
			output("解析响应实体异常！", e);
		} catch (IOException e) {
			output("解析响应实体时java IO 异常！", e);
		}
	}
}
```

```
/**
	 * 响应结束之后，处理响应头信息，如set-cookien内容
	 *
	 * @param response
	 */
	private static void afterResponse(CloseableHttpResponse response) {
		if (response == null) return;
		List<Header> headers = Arrays.asList(response.getHeaders("Set-Cookie"));
		if (!headers.equals(new ArrayList<>())) setCookies(headers);
		if (cookieSet == false && cookies != null)
			cookieSet = true;
	}
```

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

```
	/**
	 * 根据解析好的content，转化json对象
	 *
	 * @param content
	 * @return
	 */
	private static JSONObject getResponse(String content) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = JSONObject.fromObject(content);
		} catch (Exception e) {
			jsonObject.put("content", content);
			jsonObject.put("code", TEST_ERROR_CODE);
		}
		return jsonObject;
	}
```

```
	/**
	 * 获取并检查code
	 *
	 * @param jsonObject
	 * @param url
	 * @return
	 */
	private static int checkCode(JSONObject jsonObject, String url) {
		int code = TEST_ERROR_CODE;
		try {
			code = jsonObject.getInt("code");
			if (ERROR_CODE_LIST.contains(code))
				new Thread(() -> new AlertOver("responseCode错误", jsonObject.toString(), url).sendSystemMessage());
		} catch (Exception e) {
			output("响应非标准响应体！", e);
		}
		return code;
	}
```
* 其中数据库存储的和发送提醒消息的这里就不说了，数据库一部存储还在优化，alertover使用方法在其他文章分享过了。

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>