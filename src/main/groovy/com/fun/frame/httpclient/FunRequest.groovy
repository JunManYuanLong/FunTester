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
public class FunRequest extends FanLibrary implements Serializable{

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
