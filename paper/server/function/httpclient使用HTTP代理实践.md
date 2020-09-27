# httpclient使用HTTP代理实践

最近在做测试的时候遇到一个问题：就是内部网络做了限制，**部分服务只有在机房的网段内才能访问**。

虽然不清楚具体原因，不过的确给测试造成了一些麻烦，使用工具或者*Git*进行文件同步的话，虽然可行，但总归不是那么方便。再加上一些功能测试工具选择的问题，对我来讲添加了更多的麻烦。不过这倒是更有利于我的性能测试方案实施，具体情况如下：[如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)。

所以想到了在`Java`中使用`proxy`配置，然后通过在机房开发机中配置一个`HTTP`代理，然后本机请求从`proxy`配置得到一个代理服务器地址，然后绕去*内网*访问服务，这样就完美解决了这个方案。

## Demo代码


```Groovy
public static void main(String[] args) {
        def get = getHttpPost("http://ip-api.com/json/?lang=zh-CN ")
        def response1 = getHttpResponse(get)
        output(response1)
        setProxy(get,"104.129.198.211:10605")
        def response = getHttpResponse(get)
        output(response)


        testOver()
    }
```

控制台输出：


```shell
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 请求uri：http://ip-api.com/json/?lang=zh-CN,耗时：982 ms
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "zip":"",
＞  ① . "country":"中国",
＞  ① . "city":"北京",
＞  ① . "org":"Beijing Qishangzaixian Data Correspondence Technology Co.,  Ltd",
＞  ① . "timezone":"Asia/Shanghai",
＞  ① . "regionName":"北京市",
＞  ① . "isp":"IDC,  China Telecommunications Corporation",
＞  ① . "query":"118.26.128.202",
＞  ① . "lon":116.3889,
＞  ① . "as":"AS23724 IDC,  China Telecommunications Corporation",
＞  ① . "countryCode":"CN",
＞  ① . "region":"BJ",
＞  ① . "lat":39.9288,
＞  ① . "status":"success"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
INFO-> 请求uri：http://ip-api.com/json/?lang=zh-CN,耗时：488 ms
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "zip":"90014",
＞  ① . "country":"美国",
＞  ① . "city":"洛杉矶",
＞  ① . "org":"Zscaler,  Inc.",
＞  ① . "timezone":"America/Los_Angeles",
＞  ① . "regionName":"加利福尼亚州",
＞  ① . "isp":"ZSCALER,  INC.",
＞  ① . "query":"104.129.198.211",
＞  ① . "lon":-118.2641,
＞  ① . "as":"AS22616 ZSCALER,  INC.",
＞  ① . "countryCode":"US",
＞  ① . "region":"CA",
＞  ① . "lat":34.0494,
＞  ① . "status":"success"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~

Process finished with exit code 0

```

## 封装方法


```Java
    /**
     * 设置代理请求
     *
     * @param request
     * @param adress
     */
    public static void setProxy(HttpRequestBase request, String adress) {
        if (!Regex.isMatch(adress, "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))):([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-4]\\d{4}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])"))
            ParamException.fail("adress格式错误:" + adress);
        String[] split = adress.split(":");
        RequestConfig proxyRequestConfig = ClientManage.getProxyRequestConfig(split[0], changeStringToInt(split[1]));
        request.setConfig(proxyRequestConfig);
    }

    public static void setProxy(HttpRequestBase request, String ip, int port) {
        setProxy(request, ip + ":" + port);
    }

    /**
     * 获取代理配置项
     *
     * @param ip
     * @param port
     * @return
     */
    public static RequestConfig getProxyRequestConfig(String ip, int port) {
        return RequestConfig.custom().setConnectionRequestTimeout(HttpClientConstant.CONNECT_REQUEST_TIMEOUT).setConnectTimeout(HttpClientConstant.CONNECT_TIMEOUT).setSocketTimeout(HttpClientConstant.SOCKET_TIMEOUT).setCookieSpec(CookieSpecs.IGNORE_COOKIES).setRedirectsEnabled(false).setProxy(new HttpHost(ip, port)).build();
    }

```

* 点击原文，可查看项目源码，或者直接访问GitHub：https://github.com/JunManYuanLong/FunTester，记得给个星星吆！



--- 
* 公众号**FunTester**首发，更多原创文章：[FunTester440+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [开源测试服务](https://mp.weixin.qq.com/s/ZOs0cp_vt6_iiundHaKk4g)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [电子书网站爬虫实践](https://mp.weixin.qq.com/s/KGW0dIS5NTLgxyhSjxDiOw)
- [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)