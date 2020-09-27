# 基于java的直线型接口测试框架初探


在使用java语言作为接口测试的过程中，发现java语言的简洁性的确不如脚本语言，如python，很多功能python一行代码几个方法就能搞定，java需要几行，而且有时候并不利于理解。最近接触到了一个词“直线型”代码。看了之后有所感觉，重新写了一个直线型代码风格的接口请求框架。

源码如下：

```

package com.fun.frame.httpclient

import com.fun.base.bean.RequestInfo
import com.fun.config.RequestType
import net.sf.json.JSONObject
import org.apache.commons.lang3.StringUtils
import org.apache.http.Header
import org.apache.http.client.methods.HttpRequestBase
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 重写FanLibrary，使用面对对象思想
 */
public class FunRequest extends FanLibrary {

    static Logger logger = LoggerFactory.getLogger(FunRequest.class)

    /**
     * 请求类型，true为get，false为post
     */
    RequestType requestType

    /**
     * 请求对象
     */
    HttpRequestBase request

    /**
     * host地址
     */
    String host

    /**
     * 接口地址
     */
    String apiName

    /**
     * 请求地址,如果为空则由host和apiname拼接
     */
    String uri

    /**
     * header集合
     */
    List<Header> headers = new ArrayList<>()

    /**
     * get参数
     */
    JSONObject args = new JSONObject()

    /**
     * post参数
     */
    JSONObject params = new JSONObject()

    /**
     * json参数
     */
    JSONObject json = new JSONObject()

    /**
     * 构造方法
     *
     * @param requestType
     */
    private FunRequest(RequestType requestType) {
        this.requestType = requestType
    }

    /**
     * 获取get对象
     *
     * @return
     */
    public static FunRequest isGet() {
        new FunRequest(RequestType.GET)
    }

    /**
     * 获取post对象
     *
     * @return
     */
    public static FunRequest isPost() {
        new FunRequest(RequestType.POST)
    }

    /**
     * 设置host
     *
     * @param host
     * @return
     */
    public FunRequest setHost(String host) {
        this.host = host
        this
    }

    /**
     * 设置接口地址
     *
     * @param apiName
     * @return
     */
    public FunRequest setApiName(String apiName) {
        this.apiName = apiName
        this
    }

    /**
     * 设置uri
     *
     * @param uri
     * @return
     */
    public FunRequest setUri(String uri) {
        this.uri = uri
        this
    }

    /**
     * 添加get参数
     *
     * @param key
     * @param value
     * @return
     */
    public FunRequest addArgs(Object key, Object value) {
        args.put(key, value)
        this
    }

    /**
     * 添加post参数
     *
     * @param key
     * @param value
     * @return
     */
    public FunRequest addParam(Object key, Object value) {
        params.put(key, value)
        this
    }

    /**
     * 添加json参数
     *
     * @param key
     * @param value
     * @return
     */
    public FunRequest addJson(Object key, Object value) {
        json.put(key, value)
        this
    }

    /**
     * 添加header
     *
     * @param key
     * @param value
     * @return
     */
    public FunRequest addHeader(Object key, Object value) {
        headers << getHeader(key.toString(), value.toString())
        this
    }

    /**
     * 添加header
     *
     * @param header
     * @return
     */
    public FunRequest addHeader(Header header) {
        headers.add(header)
        this
    }

    /**
     * 批量添加header
     *
     * @param header
     * @return
     */
    public FunRequest addHeader(List<Header> header) {
        header.each { h -> headers << h }
        this
    }

    /**
     * 增加header中cookies
     *
     * @param cookies
     * @return
     */
    public FunRequest addCookies(JSONObject cookies) {
        headers << getCookies(cookies)
        this
    }

    /**
     * 获取请求响应，兼容相关参数方法，不包括file
     *
     * @return
     */
    public JSONObject getResponse() {
        if (StringUtils.isEmpty(uri))
            uri = host + apiName
        switch (requestType) {
            case RequestType.GET:
                request = FanLibrary.getHttpGet(uri, args)
                break
            case RequestType.POST:
                request = !params.isEmpty() ? FanLibrary.getHttpPost(uri + changeJsonToArguments(args), params) : !json.isEmpty() ? getHttpPost(uri + changeJsonToArguments(args), json.toString()) : getHttpPost(uri + changeJsonToArguments(args))
                break
        }
        headers.each { x -> request.addHeader(x) }
        return getHttpResponse(request)
    }


    /**
     * 获取请求对象
     *
     * @return
     */
    public HttpRequestBase getRequest() {
        logger.debug("请求信息：{}",new RequestInfo(this.request).toString())
        this.request
    }

    @Override
    public String toString() {
        JSONObject.fromObject(this).toString()
    }
}

```

使用方法如下：

```
    public static void main(String[] args) {
        JSONObject response = FunRequest.isGet()
                .setHost("www.funtester.cn")
                .setApiName("/test")
                .addArgs("uname", "FunTester")
                .addArgs("passoword", "FunTester")
                .addArgs("type", "FunTester")
                .addHeader("token", "FunTester")
                .addCookies(getJson("login=false"))
                .getResponse();
        output(response);

        FanLibrary.testOver();
    }
```

### 技术类文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
8. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
9. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
10. [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
11. [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)
12. [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)

### 非技术文章精选
1. [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
2. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
3. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
4. [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)