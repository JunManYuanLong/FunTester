package com.funtester.utils

import com.alibaba.fastjson.JSONObject
import com.funtester.config.Constant
import com.funtester.config.RequestType
import com.funtester.frame.SourceCode
import com.funtester.httpclient.FunLibrary
import com.funtester.httpclient.FunRequest
import org.apache.http.Header
import org.apache.http.client.methods.HttpRequestBase

/**
 * 通过将浏览器中复制的curl文本信息转化成HTTPrequestbase对象工具类
 */
class CurlUtil {

    private static def filterWords = [".js", ".png", ".gif", ".css", ".ico", "list_unread", ".svg", ".htm", ".jpeg", ".ashx"]

    /**
     * 从curl复制结果中获取请求
     * @param path
     * @return
     */
    static List<HttpRequestBase> getRequests(String path) {
        def fileinfo = RWUtil.readTxtFileByLine(path.contains(Constant.OR) ? path : Constant.LONG_Path + path).stream().map {it.trim()}
        List<HttpRequestBase> requests = []
        def base = new CurlRequestBase()
        fileinfo.each {
            if (it.startsWith("curl")) {
                def split = it.split(" ", 2)
                def type = split[0]
                def value = split[1]
                base.url = value.substring(value.indexOf('h'), value.lastIndexOf("'"))
            } else if (it.startsWith("-H")) {
                def split = it.split(" ", 2)[1].split(": ")
                base.headers << FunLibrary.getHeader(split[0].substring(1), split[1].substring(0, split[1].lastIndexOf("'")))
            } else if (it.startsWith("--data-raw")) {
                base.params = SourceCode.getJson(it.substring(it.indexOf("'") + 1, it.lastIndexOf("'")).split("&"))
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
     * 添加URL过滤词汇
     * @param w
     */
    static void addFilterWord(String w) {
        filterWords << w
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
