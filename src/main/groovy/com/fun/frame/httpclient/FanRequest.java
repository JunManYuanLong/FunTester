package com.fun.frame.httpclient;

import com.fun.config.RequestType;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.ArrayList;
import java.util.List;

/**
 * 重写FanLibrary，使用面对对象思想
 */
public class FanRequest {

    /**
     * 请求类型，true为get，false为post
     */
    RequestType requestType;

    /**
     * 请求对象
     */
    HttpRequestBase request;

    /**
     * host地址
     */
    String host;

    /**
     * 接口地址
     */
    String apiName;

    /**
     * 请求地址,如果为空则由host和apiname拼接
     */
    String uri;

    /**
     * header集合
     */
    List<Header> headers = new ArrayList<>();

    /**
     * get参数
     */
    JSONObject args = new JSONObject();

    /**
     * post参数
     */
    JSONObject params = new JSONObject();

    /**
     * json参数
     */
    JSONObject json = new JSONObject();

    /**
     * 构造方法
     *
     * @param requestType
     */
    private FanRequest(RequestType requestType) {
        this.requestType = requestType;
    }

    /**
     * 获取get对象
     *
     * @return
     */
    public static FanRequest isGet() {
        FanRequest FanRequest = new FanRequest(RequestType.GET);
        return FanRequest;
    }

    /**
     * 获取post对象
     *
     * @return
     */
    public static FanRequest isPost() {
        FanRequest FanRequest = new FanRequest(RequestType.POST);
        return FanRequest;
    }

    /**
     * 设置host
     *
     * @param host
     * @return
     */
    public FanRequest setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * 设置接口地址
     *
     * @param apiName
     * @return
     */
    public FanRequest setApiName(String apiName) {
        this.apiName = apiName;
        return this;
    }

    /**
     * 设置uri
     *
     * @param uri
     * @return
     */
    public FanRequest setUri(String uri) {
        this.uri = uri;
        return this;
    }

    /**
     * 添加get参数
     *
     * @param key
     * @param value
     * @return
     */
    public FanRequest addArgs(Object key, Object value) {
        args.put(key, value);
        return this;
    }

    /**
     * 添加post参数
     *
     * @param key
     * @param value
     * @return
     */
    public FanRequest addParam(Object key, Object value) {
        params.put(key, value);
        return this;
    }

    /**
     * 添加json参数
     *
     * @param key
     * @param value
     * @return
     */
    public FanRequest addJson(Object key, Object value) {
        json.put(key, value);
        return this;
    }

    /**
     * 添加header
     *
     * @param key
     * @param value
     * @return
     */
    public FanRequest addHeader(Object key, Object value) {
        headers.add(FanLibrary.getHeader(key.toString(), value.toString()));
        return this;
    }

    /**
     * 添加header
     *
     * @param header
     * @return
     */
    public FanRequest addHeader(Header header) {
        headers.add(header);
        return this;
    }

    /**
     * 批量添加header
     *
     * @param header
     * @return
     */
    public FanRequest addHeader(List<Header> header) {
        header.forEach(header1 -> headers.add(header1));
        return this;
    }

    /**
     * 获取请求响应，兼容相关参数方法，不包括file
     *
     * @return
     */
    public JSONObject getResponse() {
        if (StringUtils.isEmpty(uri))
            uri = host + apiName;
        switch (requestType) {
            case GET:
                request = FanLibrary.getHttpGet(uri, args);
                break;
            case POST:
                request = !params.isEmpty() ? FanLibrary.getHttpPost(uri + FanLibrary.changeJsonToArguments(args), params) : !json.isEmpty() ? FanLibrary.getHttpPost(uri + FanLibrary.changeJsonToArguments(args), json.toString()) : FanLibrary.getHttpPost(uri + FanLibrary.changeJsonToArguments(args));
                break;
        }
        headers.forEach(header -> request.addHeader(header));
        return FanLibrary.getHttpResponse(request);
    }

    /**
     * 获取请求对象
     *
     * @return
     */
    public HttpRequestBase getRequest() {
        return this.request;
    }

    @Override
    public String toString() {
        return "host：" + host + ",api：" + apiName + ",uri:" + uri + ",args:" + args.toString() + ",params:" + params.toString() + ",json:" + json.toString() + "header:" + headers.size();
    }
}
