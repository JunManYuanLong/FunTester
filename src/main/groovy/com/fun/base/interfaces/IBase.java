package com.fun.base.interfaces;

import com.fun.base.bean.RequestInfo;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.File;

/**
 * 每个项目需要重写的方法
 */
public interface IBase {

    /**
     * 获取get请求对象
     *
     * @param url
     * @return
     */
    HttpGet getGet(String url);

    /**
     * 获取get请求对象
     *
     * @param url
     * @param arg
     * @return
     */
    HttpGet getGet(String url, JSONObject arg);

    /**
     * 获取post请求对象
     *
     * @param url
     * @return
     */
    HttpPost getPost(String url);

    /**
     * 获取post请求对象
     *
     * @param url
     * @param params
     * @return
     */
    HttpPost getPost(String url, JSONObject params);

    /**
     * 获取post请求对象
     *
     * @param url
     * @param params
     * @param file
     * @return
     */
    HttpPost getPost(String url, JSONObject params, File file);

    /**
     * 获取响应
     *
     * @param request
     * @return
     */
    JSONObject getResponse(HttpRequestBase request);

    /**
     * 获取响应
     *
     * @param url
     * @return
     */
    JSONObject getGetResponse(String url);

    /**
     * 获取响应
     *
     * @param url
     * @param args
     * @return
     */
    JSONObject getGetResponse(String url, JSONObject args);

    /**
     * 获取响应
     *
     * @param url
     * @return
     */
    JSONObject getPostResponse(String url);

    /**
     * 获取响应
     *
     * @param url
     * @param params
     * @return
     */
    JSONObject getPostResponse(String url, JSONObject params);

    /**
     * 获取响应
     *
     * @param url
     * @param params
     * @param file
     * @return
     */
    JSONObject getPostResponse(String url, JSONObject params, File file);

    /**
     * 校验响应正确性
     *
     * @param response
     * @return
     */
    boolean isRight(JSONObject response);

    /**
     * 检查响应是否符合标准
     *
     * @param response    响应json
     * @param requestInfo 请求info
     * @return
     */
    int checkCode(JSONObject response, RequestInfo requestInfo);

    /**
     * 登录
     */
    void login();

    /**
     * 设置cookies
     */
    void setHeaders(HttpRequestBase request);

    /**
     * 处理响应结果
     *
     * @param response
     */
    void handleResponseHeader(JSONObject response);

    /**
     * 获取公共的登录参数
     *
     * @return
     */
    JSONObject getParams();
}
