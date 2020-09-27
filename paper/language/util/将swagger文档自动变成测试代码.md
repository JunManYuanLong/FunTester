# 将swagger文档自动变成测试代码
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---


更新，主要是解耦代码中的长方法。

在看过一本《代码不朽》的书之后，深受启发，要编写高质量的代码，可维护性一定要弄好，经过尝试，已经将原来的magic()方法修改成为N个短方法，代码逻辑一目了然，分享解耦之后的代码。

非常推荐这本书，虽然很薄，略贵，但干货很多，过些天会分享这本书中的内容。

下面是修改后的swagger.java的代码：

```
package com.fission.source.until;
 
import com.fission.source.profile.Constant;
import com.fission.source.source.SourceCode;
import lombok.Data;
import net.sf.json.JSONObject;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
 
@Data
public class Request extends SourceCode {
 
	/**
	 * 请求的url
	 */
	private String url;
	/**
	 * 请求类型
	 */
	private String type;
	/**
	 * 接口名称
	 */
	private String apiName;
	/**
	 * 接口描述
	 */
	private String desc;
	/**
	 * restful参数
	 */
	List<String> restfulArgs = new ArrayList<>();
	/**
	 * query参数
	 */
	JSONObject args = new JSONObject();
	/**
	 * formdata参数
	 */
	JSONObject params = new JSONObject();
	/**
	 * 参数替换字符串，用户想方法里面添加参数
	 */
	StringBuffer stringBuffer = new StringBuffer();
 
	/**
	 * 代码文本
	 */
	StringBuffer code = new StringBuffer();
 
	/**
	 * 如果遇到post请求，fromdata参数为空时，url里面直接拼接请求字符串
	 */
	boolean postNoParams = false;
 
 
    /**
     * 拼接json参数
     *
     * @param i 0：get请求；1：post请求
     */
    private void spliceArgs(int i) {
        String type = i == 1 ? "params" : "args";
        code.append(LINE + TAB + TAB + "JSONObject " + type + " = new JSONObject();");
        Set keySet = i == 0 ? args.keySet() : params.keySet();
        keySet.forEach(key -> {
//			if (!key.toString().equals(LOGINKEY))
            collectArgs(key.toString(), params.getString(key.toString()));
            code.append(LINE + TAB + TAB + type + ".put(\"" + key.toString() + "\", " + key.toString() + ");");
        });
    }
 
 
	/**
	 * 收集参数，拼接往json传参的代码行
	 *
	 * @param key
	 * @param value
	 */
	private void collectArgs(String key, String value) {
		if (value.equals("string")) stringBuffer.append("String " + key.toString() + ",");
		if (value.equals("integer")) stringBuffer.append("int " + key.toString() + ",");
	}
 
	/**
	 * 收集restful参数，处理url
	 */
	private void collectRestfulArgs() {
		if (url.contains("{")) {//restful公参处理，并提取到restfulargs里面
			List<String> regexAll = regexAll(url, "\\{[^}]+\\}");
			regexAll.forEach(regex -> {
				regex = regex.replace("{", EMPTY).replace("}", EMPTY);
				restfulArgs.add(regex);
			});
		}
	}
 
	/**
	 * 拼接url
	 *
	 * @return
	 */
	private String spliceUrl() {
		collectRestfulArgs();
		url = url.contains("{") ? url : url + "\"";
		url = "\"" + url.replace("}/{", "+OR+").replace("{", "\"+").replace("}", EMPTY);
		return TAB + TAB + "String url = HOST + " + url + ";";
	}
 
	/**
	 * 拼接get请求
	 */
	private void spliceGet() {
		if (!args.isEmpty()) {
			spliceArgs(0);
			code.append(LINE + TAB + TAB + "HttpGet httpGet = getHttpGet(url, args);");//拼接获取请求方法
		} else {
			code.append(LINE + TAB + TAB + "HttpGet httpGet = getHttpGet(url);");//拼接获取请求方法
		}
		code.append(LINE + TAB + TAB + "JSONObject response = getHttpResponse(httpGet);");//拼接发送请求获取响应的方法
	}
 
	/**
	 * 拼接post请求
	 */
	private void splicePost() {
		if (!args.isEmpty()) spliceArgs(0);
		if (!params.isEmpty()) spliceArgs(1);
		if (args.isEmpty()) {//处理为空的情况
			if (!params.isEmpty()) {
				code.append(LINE + TAB + TAB + "HttpPost httpPost = getHttpPost(url, params);");
			} else {
				code.append(LINE + TAB + TAB + "HttpPost httpPost = getHttpPost(url);");
			}
		} else {
			if (!params.isEmpty()) {
				code.append(LINE + TAB + TAB + "HttpPost httpPost = getHttpPost(url, args, params);");
			} else {
				postNoParams = true;
			}
		}
		code.append(LINE + TAB + TAB + "JSONObject response = getHttpResponse(httpPost);");
	}
 
	/**
	 * 拼接响应后代码行
	 *
	 * @return
	 */
	private String spliceEnd() {
		restfulArgs.forEach(key -> stringBuffer.append("int " + key.toString() + ","));//在方法中添加参数类型的名称
		code.append(LINE + TAB + TAB + "output(response);");//拼接输出响应
		code.append(LINE + TAB + TAB + "return response;");//返回响应
		code.append(LINE + TAB + "}");
		return code.toString().replace("() {", "(" + stringBuffer.toString() + ") {").replace(",)", ")");//替换参数类型和名称
	}
 
	/**
	 * 把request对象变成代码的方法
	 *
	 * @return
	 */
	public String magic() {
		code.append(TAB + "/**\n\t * " + desc + "\n\t *\n\t * @return\n\t */" + LINE);
		code.append(TAB + "public JSONObject " + apiName + "() {" + LINE);//新建方法行
		String urlLine = spliceUrl();
		code.append(urlLine);
		if (restfulArgs.size() > 0) restfulArgs.forEach(arg -> args.remove(arg));//将公参从args里面删除
		if (type.equals(REQUEST_TYPE_GET)) 	spliceGet();
		if (type.equals(REQUEST_TYPE_POST)) splicePost();
		String finalCode = spliceEnd();
		if (postNoParams) finalCode.replace(urlLine, urlLine.replace(";", EMPTY) + " + changeJsonToArguments(args)");
		return finalCode;
	}
}
```

---------------------分割线-----------------------


本人在做接口测试的过程中，都是用java代码实现的接口请求，其中很多部分都是重复的或者有规律的。

本着凡事重复的皆可自动化的精神。在跟开发同学沟通确认之后，有了一套方案，接口文档一律采用swagger的形式，get接口传query参数，post请求传formdata参数，（文件上传除外）公参一律header。在规范接口文档之后，我通过解析swagger的json数据，就可以自动生成测试代码了，用了几天，解决了几个bug之后，现在尚且稳定可靠，分享代码供大家参考。

顺道说一句：一定先跟开发约定好规范，不然会很惨。

我会先把swagger的json数据根据那么或者url解析成具体的request对象，然后根据需要把request对象输出成代码。

```
package com.fission.source.until;
 
import com.fission.source.source.ApiLibrary;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
 
import javax.naming.Name;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
 
public class Swagger extends ApiLibrary {
	/**
	 * 关键字，用于url前
	 */
	String key;
	/**
	 * swagger文档地址
	 */
	String swaggerPath;
	/**
	 * 构造方法中接口地址类别
	 */
	String name;
	/**
	 * 构造方法中接口地址
	 */
	String url;
	/**
	 * swagger地址所有类别
	 */
	List<String> names = new ArrayList<>();
	/**
	 * 某类别所有接口地址
	 */
	List<String> urls = new ArrayList<>();
	/**
	 * swagger文档转换成的json对象
	 */
	JSONObject swagger = new JSONObject();
	/**
	 * 所有接口地址的json对象
	 */
	JSONObject paths = new JSONObject();
	/**
	 * 对应构造方法中url的request对象
	 */
	Request request = new Request();
	/**
	 * 对应构造方法中name的所有request对象
	 */
	List<Request> requests = new ArrayList<>();
 
 
	public static void main(String[] args) {
		String surl = "http://10.10.32.158:6005/swagger.json";
		String sname = "公会初始模块";
		Swagger swagger = new Swagger(surl, sname);
		swagger.requests.forEach(request1 -> request1.magic());
		testOver();
	}
 
 
	/**
	 * 获取某一类的接口的request对象
	 *
	 * @param swaggerPath
	 * @param name
	 */
	public Swagger(String swaggerPath, String name) {
		this.swaggerPath = swaggerPath;
		this.name = name;
		build();
	}
 
	/**
	 * 获取某一类的某一个接口的request对象
	 *
	 * @param swaggerPath
	 * @param name
	 * @param url
	 */
	public Swagger(String swaggerPath, String name, String url) {
		this.swaggerPath = swaggerPath;
		this.name = name;
		this.url = url;
		build();
		request = getRequest(url);
	}
 
 
	public String getKey() {
		key = regexAll(swaggerPath, "/((?!/).)*/swagger.json").get(0);
		key = key.replace(OR, EMPTY).replace("swagger.json", EMPTY);
		if (key.contains(":")) key = EMPTY;
		return this.key;
	}
 
	/**
	 * 获取name下所有接口的request对象
	 */
	private void getRequests() {
		urls.forEach(url -> {
			Request request = getRequest(url);
			if (request != null) requests.add(request);
		});
	}
 
	/**
	 * 初始化处理方法
	 */
	public void build() {
		swagger = getHttpResponse(getHttpGet(swaggerPath));
		getKey();
		getNames();
		getPaths();
		getUrls();
		getRequests();
 
	}
 
	/**
	 * 获取某一个url地址的请求request对象
	 *
	 * @param url 接口地址
	 * @return
	 */
	private Request getRequest(String url) {
		Request request = new Request();
		request.setUrl((OR + key + url).replace("//", "/"));
		JSONObject json1 = paths.getJSONObject(url);
		JSONObject json2 = new JSONObject();
		if (json1.containsKey("get")) {
			request.setType(REQUEST_TYPE_GET);
			json2 = json1.getJSONObject("get");
		} else if (json1.containsKey("post")) {
			request.setType(REQUEST_TYPE_POST);
			json2 = json1.getJSONObject("post");
		}
		String tags = json2.get("tags").toString();
		if (!tags.contains(name)) return null;
		String apiName = json2.getString("operationId");
		request.setApiName(apiName);
		String desc = json2.getString("summary");
		request.setDesc(desc);
		JSONArray json3 = json2.getJSONArray("parameters");
		JSONObject json5 = new JSONObject();
		JSONObject json6 = new JSONObject();
		json3.forEach(json -> {//获取参数，区分query和formdata
			JSONObject json4 = (JSONObject) json;
			String in = json4.getString("in");
			if (in.equals("query")) {
				boolean required = json4.getBoolean("required");
				if (required) {
					String format = json4.getString("type");
					String name = json4.getString("name");
					json5.put(name, format);
				}
			} else if (in.equals("formData")) {
				boolean required = json4.getBoolean("required");
				if (required) {
					String format = json4.getString("type");
					String name = json4.getString("name");
					json6.put(name, format);
				}
			}
		});
		request.setArgs(json5);
		request.setParams(json6);
		return request;
	}
 
	/**
	 * 获取name下所有接口的地址
	 */
	private void getUrls() {
		Set keySet = paths.keySet();
		keySet.forEach(key -> urls.add(key.toString()));
	}
 
 
	/**
	 * 获取所有name
	 */
	private void getNames() {
		JSONArray tags = swagger.getJSONArray("tags");
		tags.forEach(info -> {
			JSONObject name = (JSONObject) info;
			names.add(name.getString("name"));
		});
	}
 
	/**
	 * 获取所有的接口地址
	 */
	private void getPaths() {
		paths = swagger.getJSONObject("paths");
	}
 
 
}
```
下面是request类：


```
package com.fission.source.until;
 
import com.fission.source.profile.Constant;
import com.fission.source.source.SourceCode;
import net.sf.json.JSONObject;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class Request extends SourceCode {
 
	/**
	 * 请求的url
	 */
	private String url;
	/**
	 * 请求类型
	 */
	private String type;
	/**
	 * 接口名称
	 */
	private String apiName;
	/**
	 * 接口描述
	 */
	private String desc;
	/**
	 * restful参数
	 */
	List<String> restfulArgs = new ArrayList<>();
	/**
	 * query参数
	 */
	JSONObject args = new JSONObject();
	/**
	 * formdata参数
	 */
	JSONObject params = new JSONObject();
 
 
	/**
	 * 把request对象变成代码的方法
	 *
	 * @return
	 */
	public String magic() {
		StringBuffer code = new StringBuffer(Constant.TAB + "/**\n\t * " + desc + "\n\t *\n\t * @return\n\t */" + LINE);
		code.append(TAB + "public JSONObject " + apiName + "() {" + LINE);//新建方法行
		if (url.contains("{")) {//restful公参处理，并提取到restfulargs里面
			List<String> regexAll = regexAll(url, "\\{[^}]+\\}");
			regexAll.forEach(regex -> {
				regex = regex.replace("{", EMPTY).replace("}", EMPTY);
				restfulArgs.add(regex);
			});
		}
		url = url.contains("{") ? url : url + "\"";
		url = "\"" + url.replace("}/{", "+OR+").replace("{", "\"+").replace("}", EMPTY);
		String urlLine = TAB + TAB + "String url = HOST + " + url + ";";
		code.append(urlLine);
		StringBuffer stringBuffer = new StringBuffer();//参数替换字符串，用户想方法里面添加参数
		boolean postNoParams = false;//如果遇到post请求，fromdata参数为空时，url里面直接拼接请求字符串
		if (restfulArgs.size() > 0) restfulArgs.forEach(arg -> args.remove(arg));//将公参从args里面删除
		if (type.equals(REQUEST_TYPE_GET)) {
			if (!args.isEmpty()) {
				code.append(LINE + TAB + TAB + "JSONObject args = new JSONObject();");
				Set keySet = args.keySet();
				keySet.forEach(key -> {//拼接get请求参数，json类型，排除公参
					if (!key.toString().equals(LOGINKEY)) {
						String value = params.getString(key.toString());
						if (value.equals("string")) {
							stringBuffer.append("String " + key.toString() + ",");
						} else if (value.equals("integer")) {
							stringBuffer.append("int " + key.toString() + ",");
						}
					}
					code.append(LINE + TAB + TAB + "args.put(\"" + key.toString() + ", " + key.toString() + ");");
				});
				code.append(LINE + TAB + TAB + "HttpGet httpGet = getHttpGet(url, args);");//拼接获取请求方法
			} else {
				code.append(LINE + TAB + TAB + "HttpGet httpGet = getHttpGet(url);");//拼接获取请求方法
			}
			code.append(LINE + TAB + TAB + "JSONObject response = getHttpResponse(httpGet);");//拼接发送请求获取响应的方法
		} else if (type.equals(REQUEST_TYPE_POST)) {
			boolean argsEmpty = args.isEmpty();//querty参数是否为空
			boolean paramsEmpty = params.isEmpty();//formdata参数是否为空
			if (!argsEmpty) {
				code.append(LINE + TAB + TAB + "JSONObject args = new JSONObject();");
				Set keySet = args.keySet();
				keySet.forEach(key -> {//拼接query请求参数，json类型，排除公参
					if (!key.toString().equals(LOGINKEY)) {
						String value = params.getString(key.toString());
						if (value.equals("string")) {
							stringBuffer.append("String " + key.toString() + ",");
						} else if (value.equals("integer")) {
							stringBuffer.append("int " + key.toString() + ",");
						}
					}
					code.append(LINE + TAB + TAB + "args.put(\"" + key.toString() + ", " + key.toString() + ");");
				});
			}
			if (!paramsEmpty) {
				code.append(LINE + TAB + TAB + "JSONObject params = new JSONObject();");
				Set keySet = params.keySet();
				keySet.forEach(key -> {
					if (!key.toString().equals(LOGINKEY)) {
						String value = params.getString(key.toString());
						if (value.equals("string")) {
							stringBuffer.append("String " + key.toString() + ",");
						} else if (value.equals("integer")) {
							stringBuffer.append("int " + key.toString() + ",");
						}
					}
					code.append(LINE + TAB + TAB + "params.put(\"" + key.toString() + "\", " + key.toString() + ");");
				});
			}
			if (argsEmpty) {//处理为空的情况
				if (!paramsEmpty) {
					code.append(LINE + TAB + TAB + "HttpPost httpPost = getHttpPost(url, params);");
				} else {
					code.append(LINE + TAB + TAB + "HttpPost httpPost = getHttpPost(url);");
				}
			} else {
				if (!paramsEmpty) {
					code.append(LINE + TAB + TAB + "HttpPost httpPost = getHttpPost(url, args, params);");
				} else {
					postNoParams = true;
				}
			}
			code.append(LINE + TAB + TAB + "JSONObject response = getHttpResponse(httpPost);");
		}
		restfulArgs.forEach(key -> stringBuffer.append("int " + key.toString() + ","));//在方法中添加参数类型的名称
		code.append(LINE + TAB + TAB + "output(response);");//拼接输出响应
		code.append(LINE + TAB + TAB + "return response;");//返回响应
		code.append(LINE + TAB + "}");
		String finalCode = code.toString().replace("() {", "(" + stringBuffer.toString() + ") {").replace(",)", ")");//替换参数类型和名称
		if (postNoParams)//如果post请求，formdata参数为空，query参数直接拼接在url里面
			finalCode.replace(urlLine, urlLine.replace(";", EMPTY) + " + changeJsonToArguments(args)");
		output(finalCode);
		return finalCode;
	}
 
 
	public String getUrl() {
		return url;
	}
 
	public void setUrl(String url) {
		this.url = url;
	}
 
	public String getType() {
		return type;
	}
 
	public void setType(String type) {
		this.type = type;
	}
 
	public String getApiName() {
		return apiName;
	}
 
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
 
	public String getDesc() {
		return desc;
	}
 
	public void setDesc(String desc) {
		this.desc = desc;
	}
 
	public JSONObject getArgs() {
		return args;
	}
 
	public void setArgs(JSONObject args) {
		this.args = args;
	}
 
	public JSONObject getParams() {
		return params;
	}
 
	public void setParams(JSONObject params) {
		this.params = params;
	}
 
	public List<String> getRestfulArgs() {
		return restfulArgs;
	}
 
	public void setRestfulArgs(List<String> restfulArgs) {
		this.restfulArgs = restfulArgs;
	}
 
}
```
最后的代码如下：

![](/blog/pic/20180712105217846.png)


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

## [公众号地图](https://mp.weixin.qq.com/s/36RbP20beZ8oWJ9nLAxG3g) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)



> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>