# alertover推送api的java httpclient实现实例

本人前几天发现一款很好用的推送app——alertover，但是官网api的应用示例竟然没有java应用的示例，所以自己尝试写了一个。使用httpclient请求了一下post接口，传了一下json数据，判断一下响应的状态码。现分享代码，共大家参考。


```
	public static void sendMessageToMobile(String title, String content, String receiver) throws JSONException, ClientProtocolException, IOException {
		String source = "s-6bf44a17-73ba-45dc-9443-c34c5d53";//mi5s发送源id
		if (title.equals(null)) {
			title = "测试";
		}
		if (content.equals(null)) {
			content = "我是008！";
		}
		title = new String(title.getBytes(), "ISO-8859-1");//转换字符编码格式
		content = new String(content.getBytes(), "ISO-8859-1");//转换字符编码格式
		CloseableHttpClient httpClients = HttpClients.createDefault();//新建连接
		JSONObject jsonObject = new JSONObject();//新建json数组
		jsonObject.put("source", source.trim());//添加发送源id
		jsonObject.put("receiver", receiver.trim());//添加接收组id
		jsonObject.put("content", content.trim());//发送内容
		jsonObject.put("title", title.trim());//发送标题
		HttpPost httpPost = new HttpPost("https://api.alertover.com/v1/alert");//post请求接口
		StringEntity entity = new StringEntity(jsonObject.toString());//设置报文实体
		entity.setContentEncoding("ISO-8859-1");//设置编码格式
		entity.setContentType("application/json");//设置contentType，发送数据格式
	    httpPost.setEntity(entity);//设置请求实体
	    HttpResponse res = httpClients.execute(httpPost);//执行post请求，得到响应
	    if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {//判断一下返回状态
			output("测试发送消息成功！");
			} else {
				HttpEntity httpEntity = res.getEntity();//获取响应实体
				output(httpEntity.toString());//输出相应实体
			}
	    httpClients.close();//关闭连接
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


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>