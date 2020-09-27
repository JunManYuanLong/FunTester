# 使用httpclient实现图灵机器人web api调用实例
本人在使用图灵机器人的过程中，发现很不错，想试了通过api请求来获取回复，这样可以做一个页面聊天还是很不错的。网上搜到的文章好多都是get接口，现在已经不能用了，也不用urlencodeer方法处理info信息了。经过尝试，终于成功，分享方法代码，供大家参考。

> 目前图灵已经取消了非认证免费用户的请求次数。
> 现在httpclient自带的EntityUtils解析响应效果非常好，例子代码有点老了。

```
public static String getReplyFromRobot(String text) throws JSONException, ClientProtocolException, IOException {
		String url = "http://www.tuling123.com/openapi/api";//设置访问接口地址
	    CloseableHttpClient httpClient = HttpClients.createDefault();//创建并实例化连接
	    JSONObject jsonObject = new JSONObject();//创建并实例化jsonobject
	    jsonObject.put("key", "915b34e69c0371");//输入key
	    jsonObject.put("info", text);//输入信息
//	    jsonObject.put("loc", "北京市中关村");//设置地点
	    jsonObject.put("userid", "915b34e41cb351c0371");//设置用户id
	    String arguments = changeJsonToArguments(jsonObject);//将json数据转化为参数
	    HttpPost httpPost = new HttpPost(url+arguments);//请求post接口
	    HttpResponse response = httpClient.execute(httpPost);//获取响应
	    InputStream inputStream = response.getEntity().getContent();//创建并实例化字节输入流，使用响应实体作为输入流
	    InputStreamReader reader = new InputStreamReader(inputStream, "utf-8");//创建并实例化字符输入流，并设置编码格式
	    StringBuffer buffer = new StringBuffer(" ");//创建并实例化stringbuffer，存放响应信息
	    char[] buff = new char[512];//创建并实例化字符数组
	    int length = 0;//声明变量length，表示读取长度
	    while ((length = reader.read(buff)) != -1) {//循环读取字符输入流
	       String x = new String(buff, 0, length);//获取读取到的有效内容
	       System.out.println(x);//输出内容
	       buffer.append(x);//将读取到的内容添加到stringbuffer中
	    }
	    JSONObject dsa = new JSONObject(buffer.toString().trim());//将响应结果转化为jsonobject
		String message = dsa.getString("text");//获取返回消息
	    return message;//返回消息
	}
```

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
12. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
