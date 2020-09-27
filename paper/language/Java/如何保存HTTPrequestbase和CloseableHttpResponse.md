# 如何保存HTTPrequestbase和CloseableHttpResponse

在测试过程中，有一个重要的工作就是保存记录“现场”，以方便开发人员更快发现BUG解决问题。在接口测试中更是如此，如果开发人员能够根据BUG的信息直接复现请求，是一件很方便的事情。为此我想了一个再框架中增加保存HTTPrequestbase和CloseableHttpResponse两个对象的功能，其中主要是HTTPrequestbase的信息，CloseableHttpResponse以响应内容为主，因为每次请求我都会把必要信息（host，API，HTTP code，响应code，响应时间等等记录）。

下面是更新过的`funrequest`类的代码，更新内容时后面几个静态方法：


```
package com.fun.frame.httpclient

import com.fun.base.bean.RequestInfo
import com.fun.base.exception.RequestException
import com.fun.config.HttpClientConstant
import com.fun.config.RequestType
import com.fun.frame.Save
import com.fun.utils.Time
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
public class FunRequest extends FanLibrary implements Serializable, Cloneable {

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
        def fun = new FunRequest()
        fun.setRequest(cloneRequest(getRequest()))
        fun
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
                request = FunRequest.isPost().setUri(uri).setHeaders(headers).setJson(JSONObject.fromObject(content));
            } else if (value.equalsIgnoreCase(HttpClientConstant.ContentType_FORM.getValue())) {
                request = FunRequest.isPost().setUri(uri).setHeaders(headers).setParams(getJson(content.split("&")));
            }
        } else {
            RequestException.fail("不支持的请求类型!");
        }
        return request;
    }


/**
 * 拷贝HttpRequestBase对象
 * @param base
 * @return
 */
    static HttpRequestBase cloneRequest(HttpRequestBase base) {
        return initFromRequest(base).getRequest()
    }

/**
 * 保存请求和响应
 * @param base
 * @param response
 */
    public static void save(HttpRequestBase base, JSONObject response) {
        FunRequest request = initFromRequest(base)
        request.setResponse(response);
        Save.info("/request/" + Time.getDate().substring(8) + SPACE_1 + request.getUri().replace(OR, PART), request.toString());
    }

}
```

然后在框架是添加一个`key`来控制是否保存响应，然后调用保存方法：
`            if (SAVE_KEY) FunRequest.save(request, res);`
其中，res是响应内容，已经解析为json格式，对于非json格式响应做了兼容。同事在保存路径和保存量也做配置初始化的过程中做了校验，这个太简单就不发了。
其中一个`header2Json`方法是为了解决保存header时候不必须信息太多的问题，内容如下：

```
    /**
     * 将header转成json对象
     *
     * @param headers
     * @return
     */
    public static JSONObject header2Json(List<Header> headers) {
        JSONObject h = new JSONObject();
        headers.forEach(x -> h.put(x.getName(), x.getValue()));
        return h;
    }
```

这个是内容：
`{requestType='post', host='', apiName='', uri='https://cn.bing.com/search', headers={"Content-Type":"application/x-www-form-urlencoded; charset=UTF-8"}, args={}, params={"q":"fun"}, json={}, response={"3242":"234"}}`

这样做的好处就是，如果想复现某个出现问题的request，直接从文件中读取保存的request信息，借由`funrequest`类对象即可复现这个请求，还可以跟记录的`response`做对比。


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
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)
- [2019年浏览器市场份额排行榜](https://mp.weixin.qq.com/s/4NmJ_ZCPD5UwaRCtaCfjEg)
- [API测试基础](https://mp.weixin.qq.com/s/bkbUEa9CF21xMYSlhPcULw)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)