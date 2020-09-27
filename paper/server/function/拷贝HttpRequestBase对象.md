# 拷贝HttpRequestBase对象

在实践[性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)的过程中，我实现了一个单个HttpRequestBase对象的concurrent对象创建，单之前都是用使用唯一的HttpRequestBase对象进行多线程请求，目前来看是没有问题的，但为了防止以后出现意外BUG和统一concurrent的构造方法使用，故尝试拷贝了一个HttpRequestBase对象。原因是因为之前封装的深拷贝方法对于HttpRequestBase对象的实现类如：httpget和httppost并不适用，因为没有实现Serializable接口。所以单独写了一个HttpRequestBase对象的拷贝方法，供大家参考。

下面是`FunRequest`类的代码，深拷贝的静态方法在最后。
```
package com.fun.frame.httpclient

import com.fun.base.bean.RequestInfo
import com.fun.base.exception.RequestException
import com.fun.config.HttpClientConstant
import com.fun.config.RequestType
import net.sf.json.JSONObject
import org.apache.commons.lang3.StringUtils
import org.apache.http.Header
import org.apache.http.HttpEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.util.EntityUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 重写FanLibrary，使用面对对象思想
 */
public class FunRequest extends FanLibrary implements Serializable,Cloneable {

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
     * post参数,表单
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
        return getHttpResponse(request == null ? getRequest() : request)
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
    String toString() {
        JSONObject.fromObject(this).toString()
    }

    @Override
    FunRequest clone() {
        def fun = new FunRequest()
        fun.setRequest(cloneRequest(getRequest()))
        fun
    }

    static HttpRequestBase cloneRequest(HttpRequestBase base) {
        String method = base.getMethod();
        RequestType requestType = RequestType.getRequestType(method);
        String uri = base.getURI().toString();
        List<Header> headers = Arrays.asList(base.getAllHeaders());
        if (requestType == requestType.GET) {
            return FunRequest.isGet().setUri(uri).setHeaders(headers).getRequest();
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
                return FunRequest.isPost().setUri(uri).setHeaders(headers).setJson(JSONObject.fromObject(content)).getRequest();
            } else if (value.equalsIgnoreCase(HttpClientConstant.ContentType_FORM.getValue())) {
                return FunRequest.isPost().setUri(uri).setHeaders(headers).setParams(getJson(content.split("&"))).getRequest();
            }
        } else {
            RequestException.fail("不支持的请求类型!");
        }

    }

}

```

---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [Selenium 4.0 Alpha更新日志](https://mp.weixin.qq.com/s/tU7sm-pcbpRNwDU9D3OVTQ)
- [Selenium 4.0 Alpha更新实践](https://mp.weixin.qq.com/s/yT9wpO5o5aWBUus494TIHw)

## 非技术文章精选

- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [测试人员如何成为变革的推动者](https://mp.weixin.qq.com/s/0nTZHBOuKG0rewKAeyIqwA)
- [编写测试用例的技巧](https://mp.weixin.qq.com/s/zZAh_XXXGOyhlm6ebzs06Q)
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)