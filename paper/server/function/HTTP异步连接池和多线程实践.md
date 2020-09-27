# HTTP异步连接池和多线程实践

今天在查询一个列表的时候，突然发现列表由于之前压测导致几万条脏数据积累。导致找一个数据比较麻烦，由于项目没有提供批量删除的功能，所以想了个办法通过接口把数据挨个删除。

思路如下：先去请求分页列表，然后解析数据，通过请求删除接口去一条一条的删除。

虽然比较简单，但是几万条数据还是耗费了比较长的时间，中间进行了一些优化，所以分成了好几个版本来完成。

## 第一版：串行请求

* 关于测试框架和项目实践可以参考以前的文章：[接口测试视频教程](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319017952112492545&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)

脚本如下：

```Java
public static void main(String[] args) {
    def base = getBase()
    def manager = new TeacherManager(base)
    3.upto(1000) {
        def list = manager.verifyList(it)
        list.getJSONObject("data")?.getJSONArray("list").each { x ->
            manager.verify(x.id, x.tel)
        }
    }
    allOver()
}
```

查询列表和删除记录的方法如下：


```Java
    public JSONObject verifyList(int page = 3) {
        String url = TeacherManagerApi.VERIFY_LIST;
        JSONObject params = getParams();
        params.put("page", page);
        params.put("page_size", 50);
        JSONObject response = getPostResponse(url, params);
        output(response);
        return response;
    }
    
        public JSONObject verify(int id = 0, String tel = "") {
        String url = TeacherManagerApi.VERIFY;
        JSONObject params = getParams();
        params.put("action", 2);//2:拒绝
        params.put("id", id);
        params.put("tel", tel);
        params.put("refused_result", "清空脏数据");
        JSONObject response = getPostResponse(url, params);
        return response;
    }
```

## 第二版：HTTP异步请求优化

主要是优化了`verify`方法，每次可以串行获取完列表之后，删除的接口请求就通过异步方法调用。方法如下：

```Java
    public JSONObject verify(int id = 0, String tel = "") {
        String url = TeacherManagerApi.VERIFY;
        JSONObject params = getParams();
        params.put("action", 2);//2:拒绝
        params.put("id", id);
        params.put("tel", tel);
        params.put("refused_result", "清空脏数据");
        output(params.toString());
        HttpPost post = getPost(url, params);
        setHeaders(post);
        FanLibrary.excuteSyncWithResponse(post);
        return null;
    }
```

异步连接池的方法如下：


```Java
    /**
     * 异步发送请求
     *
     * @param request
     */
    public static void excuteSync(HttpRequestBase request) {
        if (!ClientManage.httpAsyncClient.isRunning()) ClientManage.httpAsyncClient.start();
        ClientManage.httpAsyncClient.execute(request, null);
    }

    /**
     * 异步发送请求获取影响Demo
     * <p>经过测试没卵用</p>
     *
     * @param request
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static JSONObject excuteSyncWithResponse(HttpRequestBase request) {
        if (!ClientManage.httpAsyncClient.isRunning()) ClientManage.httpAsyncClient.start();
        Future<HttpResponse> execute = ClientManage.httpAsyncClient.execute(request, null);
        try {
            HttpResponse httpResponse = execute.get();
            String content = getContent(httpResponse);
            return getJsonResponse(content, null);
        } catch (Exception e) {
            logger.error("异步请求获取响应失败!", e);
        }
        return new JSONObject();
    }
```

获取异步连接池的方法：


```Java
    /**
     * 通过连接池获取https协议请求对象
     * <p>
     * 增加默认的请求控制器，和请求配置，连接控制器，取消了cookiestore，单独解析响应set-cookie和发送请求的header，适配多用户同时在线的情况
     * </p>
     *
     * @return
     */
    private static CloseableHttpAsyncClient getCloseableHttpAsyncClient() {
        return HttpAsyncClients.custom().setConnectionManager(NconnManager).setSSLHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).setSSLContext(sslContext).build();
    }
```

经过测试，异步发送请求的效率果然有所提高，但是有个问题就是不能立刻关闭连接池，不然会导致请求失败，提示连接池已经关闭。

## 第三版：多线程

核心代码如下：


```Java
public static void main(String[] args) {
    def base = getBase()
    def manager = new TeacherManager(base)
    3.upto(100) {
        new Thread({ ->
            def list = manager.verifyList(it)
            list.getJSONObject("data")?.getJSONArray("list").each { x ->
                manager.verify(x.id, x.tel)
            }
        }).start()
    }
    allOver()
}
```

经过测试，多线程比异步效率高太多了，而且异步总会出现一些问题，比如不成功，由于不关心返回了，很多情况也无法调试，如果使用异步加上获取响应值，有会其他操作，我觉得有点绕远路了。最后采取了多线程这个方案。一秒钟能删掉上百条数据，一会儿就删完了。

--- 
* 公众号**FunTester**首发，更多原创文章：[450+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [开源测试服务](https://mp.weixin.qq.com/s/ZOs0cp_vt6_iiundHaKk4g)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)
- [测试开发工程师工作技巧](https://mp.weixin.qq.com/s/TvrUCisja5Zbq-NIwy_2fQ)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)