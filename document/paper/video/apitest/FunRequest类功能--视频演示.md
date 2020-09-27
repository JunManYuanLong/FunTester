# FunRequest类功能--视频演示


接口测试框架的视频目前告一段落，相信有一定Java基础的童鞋已经掌握了如何做一些简单的接口请求和响应处理。接下来会分享一下如何完成一个接口测试项目，不同于简单接口测试，测试项目需要面对更多的场景以及通用功能的复用。

> 相信一万行代码的理论！

视频专题：

- [FunTester测试框架视频讲解（序）](https://mp.weixin.qq.com/s/CJrHAAniDMyr5oDXYHpPcQ)
- [获取HTTP请求对象--测试框架视频讲解](https://mp.weixin.qq.com/s/hG89sGf96GcPb2hGnludsw)
- [发送请求和解析响应—测试框架视频解读](https://mp.weixin.qq.com/s/xUQ8o3YuZOChXZ2UGR1Kyw)
- [json对象基本操作--视频讲解](https://mp.weixin.qq.com/s/MQtcIGKwWGEMb2XD3zmAIQ)
- [GET请求实践--测试框架视频讲解](https://mp.weixin.qq.com/s/_ZEDmRPXe4SLjCgdwDtC7A)
- [POST请求实践--视频演示](https://mp.weixin.qq.com/s/g0mLzMQ4Br2e592m3p68eg)
- [如何处理header和cookie--视频演示](https://mp.weixin.qq.com/s/MkwzT9VPglSnOxY7geSUiQ)

本次分享funrequest类的功能和测试Demo，由于历史原因，funrequest类分成两部分内容：一是基于创造者模式的单接口测试请求框架；二是处理`HTTPrequestbase`对象`copy`和`save`的静态方法封装，这里一方面用于性能测试中对于线程对象的拷贝，一方面用于测试过程中保存请求和响应内容，方便一个追踪。

## FunRequest类功能

- [点击观看阅读视频](https://mp.weixin.qq.com/s/WGS6ZwAvw7X4MC004Gz4pA)

----
**gitee地址：https://gitee.com/fanapi/tester**

## 代码

```Java
package com.fun.frame.httpclient

import com.alibaba.fastjson.JSONObject
import com.fun.base.bean.RequestInfo
import com.fun.base.exception.RequestException
import com.fun.config.HttpClientConstant
import com.fun.config.RequestType
import com.fun.frame.Save
import com.fun.utils.Time
import org.apache.commons.lang3.StringUtils
import org.apache.http.Header
import org.apache.http.HttpEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.util.EntityUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 重写FanLibrary，使用面对对象思想
 */
class FunRequest extends FanLibrary implements Serializable, Cloneable {

    private static final long serialVersionUID = -4153600036943378727L;

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

    String host = EMPTY

    /**
     * 接口地址
     */

    String apiName = EMPTY

    /**
     * 请求地址,如果为空则由host和apiname拼接
     */

    String uri = EMPTY

    /**
     * header集合
     */

    List<Header> headers = new ArrayList<>()

    /**
     * get参数
     */

    JSONObject args = new JSONObject()

    /**
     * post参数,表单
     */

    JSONObject params = new JSONObject()

    /**
     * json参数
     */

    JSONObject json = new JSONObject()

    /**
     * 响应,若没有这个参数,从将funrequest对象转换成json对象时会自动调用getresponse方法
     */

    JSONObject response = new JSONObject()

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
    static FunRequest isGet() {
        new FunRequest(RequestType.GET)
    }

    /**
     * 获取post对象
     *
     * @return
     */
    static FunRequest isPost() {
        new FunRequest(RequestType.POST)
    }

    /**
     * 设置host
     *
     * @param host
     * @return
     */
    FunRequest setHost(String host) {
        this.host = host
        this
    }

    /**
     * 设置接口地址
     *
     * @param apiName
     * @return
     */
    FunRequest setApiName(String apiName) {
        this.apiName = apiName
        this
    }

    /**
     * 设置uri
     *
     * @param uri
     * @return
     */
    FunRequest setUri(String uri) {
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
    FunRequest addArgs(Object key, Object value) {
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
    FunRequest addParam(Object key, Object value) {
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
    FunRequest addJson(Object key, Object value) {
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
    FunRequest addHeader(Object key, Object value) {
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
    FunRequest addHeader(List<Header> header) {
        header.each {h -> headers << h}
        this
    }

    /**
     * 增加header中cookies
     *
     * @param cookies
     * @return
     */
    FunRequest addCookies(JSONObject cookies) {
        headers << getCookies(cookies)
        this
    }

    FunRequest setHeaders(List<Header> headers) {
        this.headers.addAll(headers)
        this
    }

    FunRequest setArgs(JSONObject args) {
        this.args.putAll(args)
        this
    }

    FunRequest setParams(JSONObject params) {
        this.params.putAll(params)
        this
    }

    FunRequest setJson(JSONObject json) {
        this.json.putAll(json)
        this
    }

    /**
     * 获取请求响应，兼容相关参数方法，不包括file
     *
     * @return
     */
    JSONObject getResponse() {
        response = response.isEmpty() ? getHttpResponse(request == null ? getRequest() : request) : response
        response
    }


    /**
     * 获取请求对象
     *
     * @return
     */
    HttpRequestBase getRequest() {
        if (request != null) request;
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
        for (Header header in headers) {
            request.addHeader(header)
        }
        logger.debug("请求信息：{}", new RequestInfo(this.request).toString())
        request
    }

    @Override
    FunRequest clone() {
        initFromRequest(this.getRequest())
    }

    @Override
    public String toString() {
        return "{" +
                "requestType='" + requestType.getName() + '\'' +
                ", host='" + host + '\'' +
                ", apiName='" + apiName + '\'' +
                ", uri='" + uri + '\'' +
                ", headers=" + header2Json(headers).toString() +
                ", args=" + args.toString() +
                ", params=" + params.toString() +
                ", json=" + json.toString() +
                ", response=" + getResponse().toString() +
                '}';
    }


/**
 * 从requestbase对象从初始化funrequest
 * @param base
 * @return
 */
    static FunRequest initFromRequest(HttpRequestBase base) {
        FunRequest request = null;
        String method = base.getMethod();
        RequestType requestType = RequestType.getRequestType(method);
        String uri = base.getURI().toString();
        List<Header> headers = Arrays.asList(base.getAllHeaders());
        if (requestType == requestType.GET) {
            request = FunRequest.isGet().setUri(uri).setHeaders(headers);
        } else if (requestType == RequestType.POST || requestType == RequestType.FUN) {
            HttpPost post = (HttpPost) base;
            HttpEntity entity = post.getEntity();
            String value = entity.getContentType().getValue();
            String content = null;
            try {
                content = EntityUtils.toString(entity);
            } catch (IOException e) {
                logger.error("解析响应失败!", e)
                fail();
            }
            if (value.equalsIgnoreCase(HttpClientConstant.ContentType_TEXT.getValue()) || value.equalsIgnoreCase(HttpClientConstant.ContentType_JSON.getValue())) {
                request = FunRequest.isPost().setUri(uri).setHeaders(headers).setJson(JSONObject.parseObject(content));
            } else if (value.equalsIgnoreCase(HttpClientConstant.ContentType_FORM.getValue())) {
                request = FunRequest.isPost().setUri(uri).setHeaders(headers).setParams(getJson(content.split("&")));
            }
        } else {
            RequestException.fail("不支持的请求类型!");
        }
        return request;
    }

    static HttpRequestBase doCopy(HttpRequestBase base) {
        (HttpRequestBase) RequestBuilder.copy(base).build()
    }

/**
 * 拷贝HttpRequestBase对象
 * @param base
 * @return
 */
    static HttpRequestBase cloneRequest(HttpRequestBase base) {
        initFromRequest(base).getRequest()
    }

/**
 * 保存请求和响应
 * @param base
 * @param response
 */
    public static void save(HttpRequestBase base, JSONObject response) {
        FunRequest request = initFromRequest(base)
        request.setResponse(response);
        Save.info("/request/" + Time.getDate().substring(8) + SPACE_1 + request.getUri().replace(OR, CONNECTOR).replaceAll("https*:_+", EMPTY), request.toString());
    }

}

```

---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [压测中测量异步写入接口的延迟](https://mp.weixin.qq.com/s/odvK1iYgg4eRVtOOPbq15w)
- [多种登录方式定量性能测试方案](https://mp.weixin.qq.com/s/WuZ2h2rr0rNBgEvQVioacA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [左移测试](https://mp.weixin.qq.com/s/8zXkWV4ils17hUqlXIpXSw)
- [选择手动测试还是自动化测试？](https://mp.weixin.qq.com/s/4haRrfSIp5Plgm_GN98lRA)
- [自动化测试项目为何失败](https://mp.weixin.qq.com/s/KFJXuLjjs1hii47C1BH8PA)



![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDkiawpL3o8umv1EgHOc2OE1H8DtTMQSXWTOgFYPMSGtoX2BZlricBBJun4hMGUOJd7uibe68zQecRFw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)