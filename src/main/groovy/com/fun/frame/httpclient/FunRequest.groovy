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
