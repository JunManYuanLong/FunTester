package com.fun.base.bean

import com.fun.config.RequestType
import com.fun.config.SysInit
import org.apache.http.HttpEntity
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.util.EntityUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 请求信息封装类
 */
class RequestInfo extends AbstractBean {

    private static Logger logger = LoggerFactory.getLogger(RequestInfo.class)

    /**
     * 接口地址
     */
    String apiName

    /**
     * 请求的url
     */
    String url

    /**
     * 请求的uri
     */
    String uri

    /**
     * 方法，get/post
     */
    RequestType method

    /**
     * 域名
     */
    String host

    /**
     * 协议类型
     */
    String type

    /**
     * 参数
     */
    String params

    /**
     * host是否是黑名单
     */
    boolean isBlack;

    /**
     * 通过request获取请求的相关信息，并输出部分信息
     *
     * @param request
     */
    public RequestInfo(HttpRequestBase request) {
        getRequestInfo request
    }

    /**
     * 封装获取请求的各种信息的方法
     *
     * @param request 传入请求对象
     * @return 返回一个map，包含api_name,host_name,type，method，params
     */
    private void getRequestInfo(HttpRequestBase request) {
        method = RequestType.getRequestType request.getMethod()
        uri = request.getURI().toString()// 获取uri
        getRequestUrl(uri)
        String one = url.substring(url.indexOf("//") + 2)// 删除掉http://
        apiName = one.substring(one.indexOf("/"))// 获取接口名
        host = one.substring(0, one.indexOf("/"))// 获取host地址
        isBlack = SysInit.isBlack(host)
        type = url.substring(0, url.indexOf("//") - 1)// 获取协议类型
        if (method == RequestType.GET) {
            if (!uri.contains("?")) return
            params = uri.substring(uri.indexOf("?") + 1)
        } else if (method == RequestType.POST) {
            getPostRequestParams(request)
        }
    }

    /**
     * 获取请求url，遇到get请求，先截取
     *
     * @param uri
     */
    private void getRequestUrl(String uri) {
        url = uri.contains("?") ? uri.substring(0, uri.indexOf("?")) : uri
    }

    /**
     * 获取响应实体,post path,put方法适用
     *
     * @param request
     */
    private void getPostRequestParams(HttpEntityEnclosingRequestBase request) {
        HttpEntity entity = request.getEntity()// 获取实体
        if (entity == null) return
        try {
            params = EntityUtils.toString(entity)// 解析实体
            EntityUtils.consume(entity)// 确保实体消耗
        } catch (Exception e) {
            logger.warn("获取post请求参数时异常！")
            params = "entity类型：" + entity.getClass()
        }
    }

    boolean isBlack() {
        isBlack
    }

    @Override
    public String toString() {
        this.toJson().toString()
    }
}
