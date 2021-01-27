package com.fun.utils

import com.alibaba.fastjson.JSONObject
import com.fun.config.RequestType
import com.fun.frame.SourceCode
import com.fun.frame.httpclient.FanLibrary
import com.fun.frame.httpclient.FunRequest
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.Header
/**
 * 通过将浏览器中复制的curl文本信息转化成HTTPrequestbase对象工具类
 */
class CurlUtil extends SourceCode {

    public static def filterWords = [".js", ".png", ".gif", ".css", ".ico", "list_unread", ".svg", ".htm", ".jpeg", ".ashx"]

    /**
     * 从curl复制结果中获取请求
     * @param path
     * @return
     */
    public static List<HttpRequestBase> getRequests(String path) {
        def fileinfo = WriteRead.readTxtFileByLine(path.contains(OR) ? path : LONG_Path + path).stream().map {it.trim()}
        def requests = []
        def base = new CurlRequestBase()
        fileinfo.each {
            if (it.startsWith("curl")) {
                def split = it.split(" ", 2)
                def type = split[0]
                def value = split[1]
                base.url = value.substring(value.indexOf('h'), value.lastIndexOf("'"))
            } else if (it.startsWith("-H")) {
                def split = it.split(" ", 2)[1].split(": ")
                base.headers << FanLibrary.getHeader(split[0].substring(1), split[1].substring(0, split[1].lastIndexOf("'")))
            } else if (it.startsWith("--data-raw")) {
                base.params = getJson(it.substring(it.indexOf("'") + 1, it.lastIndexOf("'")).split("&"))
                base.type = RequestType.POST
            } else if (it.startsWith("--compressed")) {
                requests << getRequest(base)
                base = new CurlRequestBase()
            }
        }
        requests.findAll {
            it != null && it.getFirstHeader("accept").getValue().contains("application/json")
        }
    }

    /**
     * 将curlrequestbase对象转换成HTTPrequestbase
     * @param base
     * @return
     */
    static HttpRequestBase getRequest(CurlRequestBase base) {
        if (filterWords.any {
            base.url.contains(it)
        }) return
        base.type == RequestType.GET ? FunRequest.isGet().setUri(base.url).addHeader(base.headers).getRequest() : FunRequest.isPost().setUri(base.url).addHeader(base.headers).addParams(base.params).getRequest()
    }

    /**
     * 用于存储每一个请求的详情
     */
    static class CurlRequestBase {

        String url

        RequestType type = RequestType.GET

        List<Header> headers = new ArrayList<>()

        JSONObject params = new JSONObject()

    }
}
