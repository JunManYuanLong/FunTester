# arthas命令trace追踪方法链路视频演示

> `arthas`是一个`Java`开源诊断神器。

今天分享一个非常重要的命令`trace`，官网定义这个方法的功能如下：方法内部调用路径，并输出方法路径上的每个节点上耗时
`trace`命令能主动搜索 `class-pattern／method-pattern` 对应的方法调用路径，渲染和统计整个调用链路上的所有性能开销和追踪调用链路。

`trace`一些高级用法也是离不开[arthas命令ognl](https://mp.weixin.qq.com/s/cMCaXFwjp6QHFq40TvP4bQ)中提到的`ognl`语法支持，有兴趣的童鞋可以多去研究研究`ognl`的语法。

* `trace`对于`lambda`表达式支持不好。

# 效果展示

```Java
[arthas@71728]$ trace com.fun.frame.httpclient.FanLibrary getHttpResponse
Press Q or Ctrl+C to abort.
Affect(class-cnt:2 , method-cnt:1) cost in 107 ms.
`---ts=2020-05-16 14:38:18;thread_name=main;id=1;is_daemon=false;priority=5;TCCL=sun.misc.Launcher$AppClassLoader@14dad5dc
    `---[49.749941ms] com.fun.frame.httpclient.FanLibrary:getHttpResponse()
        +---[0.100454ms] com.fun.frame.httpclient.FanLibrary:isRightRequest() #322
        +---[0.732549ms] com.fun.frame.httpclient.FanLibrary:beforeRequest() #323
        +---[0.014092ms] com.alibaba.fastjson.JSONObject:<init>() #324
        +---[0.107529ms] com.fun.base.bean.RequestInfo:<init>() #325
        +---[0.014428ms] com.fun.utils.Time:getTimeStamp() #327
        +---[39.088784ms] org.apache.http.impl.client.CloseableHttpClient:execute() #328
        +---[0.007822ms] com.fun.utils.Time:getTimeStamp() #329
        +---[0.027006ms] com.fun.frame.httpclient.FanLibrary:getStatus() #332
        +---[0.037918ms] com.fun.frame.httpclient.FanLibrary:afterResponse() #333
        +---[0.548651ms] com.fun.frame.httpclient.FanLibrary:getContent() #334
        +---[1.198112ms] com.fun.frame.httpclient.FanLibrary:getJsonResponse() #336
        +---[0.030268ms] com.alibaba.fastjson.JSONObject:putAll() #336
        +---[0.010035ms] com.fun.frame.httpclient.FanLibrary:getMark() #340
        +---[1.491255ms] com.fun.db.mysql.MySqlTest:saveApiTestDate() #340
        +---[0.01706ms] org.apache.http.client.methods.CloseableHttpResponse:close() #342
        `---[0.655007ms] com.fun.base.bean.RequestInfo:isBlack() #348
```

# arthas命令trace追踪方法链路

- [点击观看视频](https://mp.weixin.qq.com/s/bzkdKZugkOl8C-_xTw92YA)

# 代码


```Java
package com.fun

import com.alibaba.fastjson.JSONObject
import com.fun.frame.httpclient.FanLibrary
import org.apache.http.client.methods.HttpGet

class sd extends FanLibrary {

    public static void main(String[] args) {

        while (true) {
            String url = "https://api.apiopen.top/getAllUrl"
            HttpGet get = getHttpGet(url)
            JSONObject response = getHttpResponse(get)
            output(response)
            output(test())
            sleep(5000)
        }
        testOver()
    }

    static String test() {
        sleep(1000)
        for (int i = 0; i < 5; i++) {
            getRandomInt(100)
        }
        ArrayList list = Arrays.asList(32, 432, 432, 423, 423, 32)
        list.stream().forEach({x -> output(x)})

        DEFAULT_STRING
    }
}

```

其中`gethttpresponse()`方法代码：


```Java
 /**
     * 获取响应实体
     * <p>会自动设置cookie，但是需要各个项目再自行实现cookie管理</p>
     * <p>该方法只会处理文本信息，对于文件处理可以调用两个过期的方法解决</p>
     *
     * @param request 请求对象
     * @return 返回json类型的对象
     */
    public static JSONObject getHttpResponse(HttpRequestBase request) {
        if (!isRightRequest(request)) RequestException.fail(request);
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
            if (iBase != null && !iBase.isRight(res))
                new AlertOver("响应状态码错误：" + status, "状态码错误：" + status, requestInfo.getUrl(), requestInfo).sendSystemMessage();
            MySqlTest.saveApiTestDate(requestInfo, data_size, elapsed_time, status, getMark(), code, LOCAL_IP, COMPUTER_USER_NAME);
            if (SAVE_KEY) FunRequest.save(request, res);
        } catch (Exception e) {
            logger.warn("获取请求相应失败！", e);
            if (!requestInfo.isBlack())
                new AlertOver("接口请求失败", requestInfo.toString(), requestInfo.getUrl(), requestInfo).sendSystemMessage();
        } finally {
            HEADER_KEY = false;
            if (!requestInfo.isBlack()) {
                lastRequest = request;
            }
        }
        return res;
    }
```

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester十八张原创专辑](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**，合作请联系`Fhaohaizi@163.com`。

### 热文精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [删除List中null的N种方法--最后放大招](https://mp.weixin.qq.com/s/4mfskN781dybyL59dbSbeQ)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [自动化策略六步走](https://mp.weixin.qq.com/s/He69k8iCKhTKD1j-yV6M5g)
- [功能自动化测试策略](https://mp.weixin.qq.com/s/qHmcblN4cD4JK6jT7oU4fQ)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



