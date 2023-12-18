package com.funtester.base.interfaces;

import com.alibaba.fastjson.JSONObject;
import com.funtester.base.bean.RequestInfo;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;

import java.io.File;

/**
 * 每个项目需要重写的方法
 */
public interface IBase {

    /**
     * 登录
     */
    void login();

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
    JSONObject getResponse(HttpUriRequestBase request);

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
     * <p>
     * 用于处理响应结果，一般校验json的必要层级和响应码,只在{@link IBase}一层使用,不在框架中使用,在{@link IBase}中的实践,可以直接使用框架返回响应code进行判断,框架默认将业务code返回到响应中,key为{@link com.funtester.config.Constant#DEFAULT_STRING}
     * </p>
     *
     * @param response
     * @return
     */
    boolean isRight(JSONObject response);

    /**
     * 检查响应是否符合标准
     * <p>
     * 会在FunLibrary类使用，如果没有ibase对象，会默认返回test_error_code
     * requestinfo主要用于校验该请求是否需要校验，黑名单有配置black_host提供
     * </p>
     *
     * @param response    响应json
     * @param requestInfo 请求info
     * @return
     */
    int checkCode(JSONObject response, RequestInfo requestInfo);

    /**
     * 设置header
     */
    void setHeaders(HttpUriRequestBase request);

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

    /**
     * 初始化对象，从json数据中，一般指cookie
     * <p>
     * 主要用于new了新的对象之后，然后赋值的操作，场景是从另外一个服务的对象拷贝到现在的对象，区别于clone，因为可能还会涉及其他的验证，所以单独写出一个方法，极少用到
     * </p>
     */
    void init(JSONObject info);


    /**
     * 记录请求,已经改成了由项目框架自己实现记录最后请求的功能,FunTester不再提供该功能
     * 获取{@link IBase#getRequest()}
     */
    void recordRequest(HttpUriRequestBase base);

    /**
     * 获取请求,用于并发
     *
     * @return
     */
    HttpUriRequestBase getRequest();

    /**
     * 输出JSON格式的响应结果,用于统一屏蔽打印或者不打印响应内容
     *
     * @param response
     */
    public void printRes(JSONObject response);


    /**
     * 打印所有的请求header,此处功能与print响应类似,需要用一个开关控制
     *
     * @param request
     */
    public void printHeader(HttpUriRequestBase request);

}
