# httpclient处理多用户同时在线


在使用httpclient做接口相关测试的过程中，遇到过一个障碍：如何处理多用户同时登陆。之前用户身份凭证一般都是做公参里面处理或者在header中单独定义一个或者几个字段，cookie都是使用httpclient自带的管理器自动管理的。
后来用户凭证存到了cookie里，这里就有了一些障碍，经过翻看资料得到了解决。

#### 首先取消cookie的自动管理
设置如下：

```
    /**
     * 获取请求超时控制器
     * <p>
     * cookieSpec:即cookie策略。参数为cookiespecs的一些字段。作用：
     * 1、如果网站header中有set-cookie字段时，采用默认方式可能会被cookie reject，无法写入cookie。将此属性设置成CookieSpecs.STANDARD_STRICT可避免此情况。
     * 2、如果要想忽略cookie访问，则将此属性设置成CookieSpecs.IGNORE_COOKIES。
     * </p>
     *
     * @return
     */
    private static RequestConfig getRequestConfig() {
        return RequestConfig.custom().setConnectionRequestTimeout(HttpClientConstant.CONNECT_REQUEST_TIMEOUT).setConnectTimeout(HttpClientConstant.CONNECT_TIMEOUT).setSocketTimeout(HttpClientConstant.SOCKET_TIMEOUT).setCookieSpec(CookieSpecs.IGNORE_COOKIES).setRedirectsEnabled(false).build();
    }
```
这里说明一点，这个requestconfig既可以在对HTTPrequestbase进行设置，也可以对CloseableHttpClient进行设置，由于在各个项目中都采取了单独处理cookie的设置，所以我是直接放在了连接池CloseableHttpClient对象的设置里面，如下：

```
    /**
     * 通过连接池获取https协议请求对象
     * <p>
     * 此处会默认添加一天defaultcookiesstore，会处理响应头中的set-cookie字段
     * 增加默认的请求控制器
     * </p>
     *
     * @return
     */
    private static CloseableHttpClient getCloseableHttpsClients() {
        // 创建自定义的httpsclient对象
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).setRetryHandler(httpRequestRetryHandler).setDefaultRequestConfig(requestConfig).build();
//         CloseableHttpClient client = HttpClients.createDefault();//非连接池创建
        return client;
    }
```

#### 其次处理set-cookie信息
我的方案是在处理响应的时候，只用closeablehttpresponse对象接收响应的，然后在单独在header里面遍历set-cookie字段的值，在处理json对象作为返回体的时候添加进去，如下：

```
    /**
     * 响应结束之后，处理响应头信息，如set-cookien内容
     *
     * @param response 响应内容
     * @return
     */
    private static JSONObject afterResponse(CloseableHttpResponse response) {
        JSONObject cookies = new JSONObject();
        List<Header> headers = Arrays.asList(response.getHeaders("Set-Cookie"));
        if (headers.size() == 0) return cookies;
        headers.forEach(x -> {
            String[] split = x.getValue().split(";")[0].split("=", 2);
            cookies.put(split[0], split[1]);
        });
        return cookies;
    }
    
    
    /**
     * 根据解析好的content，转化json对象
     *
     * @param content
     * @return
     */
    private static JSONObject getJsonResponse(String content, JSONObject cookies) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = JSONObject.fromObject(content);
        } catch (Exception e) {
            jsonObject.put("content", content);
            jsonObject.put("code", TEST_ERROR_CODE);
            logger.warn("响应体非json格式，已经自动转换成json格式！");
        } finally {
            if (!cookies.isEmpty()) jsonObject.put(HttpClientConstant.COOKIE, cookies);
            return jsonObject;
        }
    }
```
#### 最后处理多用户保存和携带cookie
在每个项目的base对象接收到响应之后存储cookie以便子类继承，在每次发送请求的时候带上当前对象的cookie，以对象形式存储每一个用户，达到多用户同时登录的目的，如下：

```
    @Override
    public JSONObject getResponse(HttpRequestBase httpRequestBase) {
        setHeaders(httpRequestBase);
        JSONObject response = FanLibrary.getHttpResponse(httpRequestBase);
        handleResponseHeader(response);
        return response;
    }

    @Override
    public void setHeaders(HttpRequestBase httpRequestBase) {
        httpRequestBase.addHeader(Common.REQUEST_ID);
        httpRequestBase.addHeader(FanLibrary.getHeader("token", token));
        if (!cookies.isEmpty()) httpRequestBase.addHeader(FanLibrary.getCookies(cookies));
    }

    @Override
    public void handleResponseHeader(JSONObject response) {
        if (!response.containsKey(HttpClientConstant.COOKIE)) return;
        cookies.putAll(response.getJSONObject(HttpClientConstant.COOKIE));
        response.remove(HttpClientConstant.COOKIE);
    }
```

* 在多用户多线程请求的场景下，在初始化每一个对象的时候小概率会发生一些问题：可能同一个对象会被初始化多次，这样在第二次初始化之前创建的子类对象存储的cookie会失效，由于没有做通知改变功能（多线程编程搞不定），所以测试的时候统一采用了线程绑定用户的模式，一个线程对应一个用户，防止发生混乱。


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
13. [Mac+httpclient高并发配置实例](https://mp.weixin.qq.com/s/r4a-vGz0pxeZBPPH3phujw)
14. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)

