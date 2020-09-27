# httpclient调用京东万象数字营销频道新闻api实例
本人在使用httpclient做练习的时候，偶然发现京东万象上有一个免费的频道新闻调用api，故尝试之，因为官网文档只给出的java代码都是封装后的，所以我自己写了一遍，又写了一些注释。分享代码，供大家参考。

下面是具体的调用代码：

```
public void testDemo() throws JSONException, UnsupportedOperationException, IOException {
		String url = "https://way.jd.com/jisuapi/get";//设置接口地址
		//设置参数
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("channel", channel[1]);
		jsonObject.put("num", "5");
		jsonObject.put("start", "0");
		jsonObject.put("appkey", APPKEY);
		String uri = changeJsonToArguments(jsonObject);//获取uri
		HttpGet get = new HttpGet(url+uri);//创建并实例化get接口
		JSONObject response = getHttpResponse(get);//获取响应
		output(response);//输出相应实体
		testOver();//关闭客户端
	}
```
下面是获取相应的方法（其中的封装方法已略去）：

```
/**
     * 获取响应实体
     * <p>会自动设置cookie，但是需要各个项目再自行实现cookie管理</p>
     * <p>该方法只会处理文本信息，对于文件处理可以调用两个过期的方法解决</p>
     *
     * @param request 请求对象
     * @return 返回json类型的对象
     */
    public static JSONObject getHttpResponse(HttpRequestBase request) {
        if (!isRightRequest(request)) return new JSONObject();
        beforeRequest(request);
        JSONObject res = new JSONObject();
        RequestInfo requestInfo = new RequestInfo(request);
        if (HEADER_KEY) output("===========request header===========", Arrays.asList(request.getAllHeaders()));
        long start = Time.getTimeStamp();
        try (CloseableHttpResponse response = ClientManage.httpsClient.execute(request)) {
            long end = Time.getTimeStamp();
            long elapsed_time = end - start;
            if (HEADER_KEY) output("===========response header===========", Arrays.asList(response.getAllHeaders()));
            int status = getStatus(response, res);
            JSONObject setCookies = afterResponse(response);
            String content = getContent(response);
            int data_size = content.length();
            res.putAll(getJsonResponse(content, setCookies));
            int code = iBase == null ? -2 : iBase.checkCode(res, requestInfo);
//                new AlertOver("响应状态码错误：" + status, "状态码错误：" + status, requestInfo.getUrl(), requestInfo).sendSystemMessage();
            MySqlTest.saveApiTestDate(requestInfo, data_size, elapsed_time, status, getMark(), code, LOCAL_IP, COMPUTER_USER_NAME);
        } catch (Exception e) {
            logger.warn("获取请求相应失败！", e);
            if (!SysInit.isBlack(requestInfo.getHost()))
                new AlertOver("接口请求失败", requestInfo.toString(), requestInfo.getUrl(), requestInfo).sendSystemMessage();
        } finally {
            HEADER_KEY = false;
            if (!SysInit.isBlack(requestInfo.getHost())) {
                if (requests.size() > 9) requests.removeFirst();
                boolean add = requests.add(request);
            }
        }
        return res;
    }
```
下面是解析响应实体的封装方法：
```
	    /**
     * 根据响应获取响应实体
     *
     * @param response
     * @return
     */
    public static String parseResponse(CloseableHttpResponse response) {
        HttpEntity entity = response.getEntity();// 获取响应实体
        String content = EMPTY;
        try {
            content = EntityUtils.toString(entity, DEFAULT_CHARSET);// 用string接收响应实体
            EntityUtils.consume(entity);// 消耗响应实体，并关闭相关资源占用
        } catch (Exception e1) {
            logger.warn("解析响应实体异常！", e1);
        }
        return content;
    }
```
下面是获取到的信息：


```
{"code":"10000"
"charge":false
"msg":"查询成功"
"result":{"msg":"ok"
"result":{"num":"5"
"channel":"新闻"
"list":[{"src":"澎湃新闻"
"weburl":"http://news.sina.com.cn/c/nd/2017-08-26/doc-ifykiqfe1818402.shtml"
"time":"2017-08-26 17:08"
"pic":""
"title":"北京市食药监局:海底捞限期一个月实现后厨公开"
"category":"news"
"content":"<p class=\"art_p\">原标题：北京市食药监局：海底捞北京所有门店限期一个月内后厨公开</p><p class=\"art_p\">@北京青年报 官方微博8月26日消息，2017年8月25日，有媒体反映本市“海底捞”劲松店、太阳宫店存在经营场所卫生条件存在问题等违规行为。北京市食药监局立即对上述两家门店进行立案调查，并对四川海底捞餐饮股份管理有限公司位于北京地区的1家中央厨房和26家门店开展全面检查，第一时间责任约谈该公司北京地区负责人。</p><p class=\"art_p\">今天下午，北京市食药监局再次约谈“海底捞”北京公司，将本次对“海底捞”全面检查发现的问题进行通报，问题包括消毒记录不全、餐饮具混放、未戴工作帽及口罩等，要求“海底捞”总部落实食品安全主体责任，全面进行限期整改，并按照《关于海底捞火锅北京劲松店、北京太阳宫店事件处理通报》中所承诺，主动向社会公开整改情况，主动接受社会监督。</p><p class=\"art_p\">同时，北京市食药监局表示，将把上述检查发现问题的门店记入北京市企业信用信息平台，并在第二年度餐饮服务单位量化分级中实施减分降级。</p><p class=\"art_p\">此外，要求“海底捞”总部按照承诺对北京各门店实现后厨公开、信息化、可视化，限期一个月完成，同时北京地区负责人能够主动对各门店进行随时检查。</p>"
"url":"http://news.sina.cn/gn/2017-08-26/detail-ifykiqfe1818402.d.html?cre=tianyi&mod=wnews&loc=9&r=25&doct=0&rfunc=100&tj=none&tr=25"}]}
"status":"0"}}
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
