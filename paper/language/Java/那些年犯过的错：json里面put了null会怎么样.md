# 那些年犯过的错：json里面put了null会怎么样


<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

# json里面put了null会怎么样？

本人在使用httpclient做接口测试的过程中，遇到一个请求系统资源的接口。里面每项资源都有一个version，逻辑是：如果第一次请求，version传空，就会获取最近的resource信息，如果穿了版本号，一致返回空表示不更新，不一致会更新本地的缓存数据。我写了一个方法，第一次请求的时候会把版本号保存下载，第二次请求会这些版本号信息当做参数传入，检验是否返回空。结果发现了一个jsonobject的问题，就是第一次请求的时候，传null的参数并没有带上参数。经过查阅相关资料发现了，jsonobject如果在put方法的时候，value值是null，就隐藏该项，所以导致会从json数据中消失。

下面放一下自己的代码：


```
/**
	 * 获取系统静态资源
	 * 
	 * 第一次获取设置各个版本号，第二次直接使用版本号
	 * 
	 * @return
	 */
	public JSONObject getResource() {
		JSONObject response = null;
		String url = urls.getString("static_config");
		JSONObject args = getParams(token);
		args.put("audio_country", "");
		args.put("audio_label", "");
		args.put("game_user_number", "");
		args.put("banner", "");
		args.put("video_open_time", "");
		args.put("tip", "");
		args.put("objective", objectiveVersion);
		args.put("startup_page", startupVersion);
		args.put("game", gameVersion);
		args.put("app", appVersion);
		args.put("gift", giftVersion);
		args.put("app_media_config", videoVersion);
		HttpGet httpGet = getHttpGet(url, args);
		output(args.size());
		response = getHttpResponseEntityByJson(httpGet);
		// output(response);
		if (response.containsKey("dataInfo") && objectiveVersion.isEmpty()) {
			JSONObject dataInfo = response.getJSONObject("dataInfo");
			objectiveVersion = dataInfo.getJSONObject("objective").getString("version");
			startupVersion = dataInfo.getJSONObject("startup_page").getString("version");
			gameVersion = dataInfo.getJSONObject("game").getString("version");
			appVersion = dataInfo.getJSONObject("app").getString("version");
			giftVersion = dataInfo.getJSONObject("gift").getString("version");
			videoVersion = dataInfo.getJSONObject("video_open_time").getString("version");
		}
		return response;
	}
```
在几个版本号初始化的时候，因为是string类型的成员变量，所以默认值是null，导致请求不到数据。更改初始化值为“""”即可，下面是初始化成员变量的代码。


```
public String objectiveVersion = "";// 目的页版本号
public String startupVersion = "";// 启动页版本号
public String gameVersion = "";// 游戏版本号
public String appVersion = "";// app更新配置版本号
public String giftVersion = "";// 礼物版本号
public String videoVersion = "";// 视频版本号
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

## [公众号地图](https://mp.weixin.qq.com/s/36RbP20beZ8oWJ9nLAxG3g) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>