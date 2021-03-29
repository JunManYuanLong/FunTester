package com.funtester.httpclient

import com.alibaba.fastjson.JSONObject
import com.funtester.base.bean.RequestInfo
import com.funtester.base.exception.RequestException
import com.funtester.config.HttpClientConstant
import com.funtester.config.RequestType
import com.funtester.frame.Save
import com.funtester.frame.SourceCode
import com.funtester.utils.Time
import org.apache.commons.lang3.StringUtils
import org.apache.http.Header
import org.apache.http.HttpEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.util.EntityUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

/**
 * 重写FunLibrary，使用面对对象思想,不用轻易使用set属性方法,可能存在BUG
 */
class FunRequest extends SourceCode implements Serializable, Cloneable {

    private static final long serialVersionUID = -4153600036943378727L

    private static Logger logger = LogManager.getLogger(FunRequest.class)

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
    FunRequest addHeader(Header header) {
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

    FunRequest addHeaders(List<Header> headers) {
        this.headers.addAll(headers)
        this
    }

    FunRequest addHeaders(JSONObject headers) {
        headers.each {x ->
            this.headers.add(getHeader(x.getKey().toString(), x.getValue().toString()))
        }
        this
    }

    FunRequest addArgs(JSONObject args) {
        this.args.putAll(args)
        this
    }

    FunRequest addParams(JSONObject params) {
        this.params.putAll(params)
        this
    }

    FunRequest addJson(JSONObject json) {
        this.json.putAll(json)
        this
    }

    /**
     * 获取请求响应，兼容相关参数方法，不包括file
     *
     * @return
     */
    JSONObject getResponse() {
        response = response.isEmpty() ? FunLibrary.getHttpResponse(request == null ? getRequest() : request) : response
        response
    }


    /**
     * 获取请求对象
     *
     * @return
     */
    HttpRequestBase getRequest() {
        if (request != null) request
        if (StringUtils.isEmpty(uri))
            uri = host + apiName
        switch (requestType) {
            case RequestType.GET:
                request = FunLibrary.getHttpGet(uri, args)
                break
            case RequestType.POST:
                request = !params.isEmpty() ? FunLibrary.getHttpPost(uri + FunLibrary.changeJsonToArguments(args), params) : !json.isEmpty() ? FunLibrary.getHttpPost(uri + FunLibrary.changeJsonToArguments(args), json.toString()) : FunLibrary.getHttpPost(uri + FunLibrary.changeJsonToArguments(args))
                break
        }
        for (Header header in headers) {
            request.addHeader(header)
        }
        logger.debug("请求信息：{}", new RequestInfo(this.request).toString())
        request
    }

    FunRequest setHeaders(List<Header> headers) {
        this.headers = headers
        this
    }

    FunRequest setArgs(JSONObject args) {
        this.args = args
        this
    }

    FunRequest setParams(JSONObject params) {
        this.params = params
        this
    }

    FunRequest setJson(JSONObject json) {
        this.json = json
        this
    }

    @Override
    FunRequest clone() {
        initFromRequest(this.getRequest())
    }

    @Override
    String toString() {
        return "{" +
                "requestType='" + requestType.getName() + '\'' +
                ", host='" + host + '\'' +
                ", apiName='" + apiName + '\'' +
                ", uri='" + uri + '\'' +
                ", headers=" + FunLibrary.header2Json(headers).toString() +
                ", args=" + args.toString() +
                ", params=" + params.toString() +
                ", json=" + json.toString() +
                ", response=" + response.toString() +
                '}'
    }

    /**
     * 将请求对象转成curl命令行
     * @return
     */
    String toCurl() {
        StringBuffer curl = new StringBuffer("curl -w HTTPcode%{http_code}:代理返回code%{http_connect}:数据类型%{content_type}:DNS解析时间%{time_namelookup}:%{time_redirect}:连接建立完成时间%{time_pretransfer}:连接时间%{time_connect}:开始传输时间%{time_starttransfer}:总时间%{time_total}:下载速度%{speed_download}:speed_upload%{speed_upload} ")
        if (requestType == RequestType.GET) curl << " -G"
        headers.each {
            curl << " -H '${it.getName()}:${it.getValue().replace(SPACE_1, EMPTY)}'"
        }
        switch (requestType) {
            case RequestType.GET:
                args.each {
                    curl << " -d '${it.key}=${it.value}'"
                }
                break
            case RequestType.POST:
                if (!params.isEmpty()) {
                    curl << " -H Content-Type:application/x-www-form-urlencoded"
                    params.each {
                        curl << " -F '${it.key}=${it.value}'"
                    }
                }
                if (!json.isEmpty()) {
                    curl << " -H \"Content-Type:application/json\"" //此处多余,防止从外部构建curl命令
                    json.each {
                        curl << " -d '${it.key}=${it.value}'"
                    }
                }
                break
            default:
                break
        }
        curl << " ${uri}"
        //        curl << " --compressed" //这里防止生成多个curl请求,批量生成有用
        curl.toString()
    }

    /**
     * 将请求对象转成curl命令行
     * @param requestBase
     * @return
     */
    static String reqToCurl(HttpRequestBase requestBase) {
        initFromRequest(requestBase).toCurl()
    }

    /**
     * 从requestbase对象从初始化funrequest
     * @param base
     * @return
     */
    static FunRequest initFromRequest(HttpRequestBase base) {
        FunRequest request = null
        String method = base.getMethod()
        RequestType requestType = RequestType.getRequestType(method)
        String uri = base.getURI().toString()
        List<Header> headers = Arrays.asList(base.getAllHeaders())
        if (requestType == requestType.GET) {
            request = isGet().setUri(uri).addHeaders(headers)
        } else if (requestType == RequestType.POST) {
            HttpPost post = (HttpPost) base
            HttpEntity entity = post.getEntity()
            String value = entity.getContentType().getValue()
            String content = null
            try {
                content = EntityUtils.toString(entity)
            } catch (IOException e) {
                logger.error("解析响应失败!", e)
                fail()
            }
            if (value.equalsIgnoreCase(HttpClientConstant.ContentType_TEXT.getValue()) || value.equalsIgnoreCase(HttpClientConstant.ContentType_JSON.getValue())) {
                request = isPost().setUri(uri).addHeaders(headers).addJson(JSONObject.parseObject(content))
            } else if (value.equalsIgnoreCase(HttpClientConstant.ContentType_FORM.getValue())) {
                request = isPost().setUri(uri).addHeaders(headers).addParams(getJson(content.split("&")))
            }
        } else {
            RequestException.fail("不支持的请求类型!")
        }
        return request
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
    static void save(HttpRequestBase base, JSONObject response) {
        FunRequest request = initFromRequest(base)
        request.setResponse(response)
        Save.info("/request/" + Time.getDate().substring(8) + SPACE_1 + request.getUri().replace(OR, CONNECTOR).replaceAll("https*:_+", EMPTY), request.toString())
    }


}
