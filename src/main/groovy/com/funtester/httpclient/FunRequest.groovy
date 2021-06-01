package com.funtester.httpclient

import com.alibaba.fastjson.JSON
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
import org.apache.http.client.methods.HttpPut
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.client.methods.RequestBuilder
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
    String path = EMPTY

    /**
     * 请求地址,如果为空则由host和path拼接
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
     * json参数,用于POST和put
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
     * 获取put请求对象
     * @return
     */
    static FunRequest isPut() {
        new FunRequest(RequestType.PUT)
    }

    /**
     * 获取delete请求对象
     * @return
     */
    static FunRequest isDelete() {
        new FunRequest(RequestType.DELETE)
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
     * @param path
     * @return
     */
    FunRequest setpath(String path) {
        this.path = path
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
        headers << FunLibrary.getHeader(key.toString(), value.toString())
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
        headers << FunLibrary.getCookies(cookies)
        this
    }

    FunRequest addHeaders(List<Header> headers) {
        this.headers.addAll(headers)
        this
    }

    FunRequest addHeaders(JSONObject headers) {
        headers.each {x ->
            this.headers.add(FunLibrary.getHeader(x.getKey().toString(), x.getValue().toString()))
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
            uri = host + path
        switch (requestType) {
            case RequestType.GET:
                request = FunLibrary.getHttpGet(uri, args)
                break
            case RequestType.POST:
                request = !params.isEmpty() ? FunLibrary.getHttpPost(uri + FunLibrary.changeJsonToArguments(args), params) : !json.isEmpty() ? FunLibrary.getHttpPost(uri + FunLibrary.changeJsonToArguments(args), json.toString()) : FunLibrary.getHttpPost(uri + FunLibrary.changeJsonToArguments(args))
                break
            case RequestType.PUT:
                request = FunLibrary.getHttpPut(uri, json)
                break
            case RequestType.DELETE:
                request = FunLibrary.getHttpDelete(uri)
                break
            case RequestType.PATCH:
                request = FunLibrary.getHttpPatch(uri, params)
            default:
                break
        }
        for (Header it : headers) {
            if (it.getName() != HttpClientConstant.ContentType_JSON.getName()) request.addHeader(it)
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
                ", path='" + path + '\'' +
                ", uri='" + uri + '\'' +
                ", headers=" + FunLibrary.header2Json(headers).toString() +
                ", args=" + args.toString() +
                ", params=" + params.toString() +
                ", json=" + json.toString() +
                ", response=" + response.toString() +
                '}'
    }


    /**
     * 将对象转化成JSON,用于接口参数解析
     * @return
     */
    String toJson() {
        JSON.toJSONString(this)
    }

    /**
     * 将请求对象转成curl命令行
     * @return
     */
    String toCurl() {
        StringBuffer curl = new StringBuffer("curl -w HTTPcode%{http_code}:代理返回code%{http_connect}:数据类型%{content_type}:DNS解析时间%{time_namelookup}:%{time_redirect}:连接建立完成时间%{time_pretransfer}:连接时间%{time_connect}:开始传输时间%{time_starttransfer}:总时间%{time_total}:下载速度%{speed_download}:speed_upload%{speed_upload} ")
        curl << " -X ${requestType.getName()} "
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
        String uri = base.getURI().toString()
        RequestType requestType = RequestType.getInstance(method)
        List<Header> headers = Arrays.asList(base.getAllHeaders())
        if (requestType == requestType.GET) {
            request = isGet().setUri(uri).addHeaders(headers)
        } else if (requestType == RequestType.POST) {
            HttpPost post = (HttpPost) base
            HttpEntity entity = post.getEntity()
            if (entity == null) {
                request = isPost().setUri(uri).addHeader(headers)
            } else {
                Header type = entity.getContentType()
                String value = type == null ? EMPTY : type.getValue()
                String content = FunLibrary.getContent(entity)
                if (value.equalsIgnoreCase(HttpClientConstant.ContentType_TEXT.getValue()) || value.equalsIgnoreCase(HttpClientConstant.ContentType_JSON.getValue())) {
                    request = isPost().setUri(uri).addHeaders(headers).addJson(JSONObject.parseObject(content))
                } else if (value.equalsIgnoreCase(HttpClientConstant.ContentType_FORM.getValue())) {
                    request = isPost().setUri(uri).addHeaders(headers).addParams(getJson(content.split("&")))
                }
            }
        } else if (requestType == RequestType.PUT) {
            HttpPut put = (HttpPut) base
            String content = FunLibrary.getContent(put.getEntity())
            request = isPut().setUri(uri).addHeaders(headers).setJson(JSONObject.parseObject(content))
        } else if (requestType == RequestType.DELETE) {
            request = isDelete().setUri(uri)
        } else {
            RequestException.fail("不支持的请求类型!")
        }
        return request
    }

    /**
     * 从字符串中获取请求对象
     * @param fun
     * @return
     */
    static FunRequest initFromString(String fun) {
        def f = JSON.parseObject(fun)
        initFromJson(f)
    }

    /**
     * 从JSON中初始化对象
     * @param f
     * @return
     */
    static FunRequest initFromJson(JSONObject f) {
        RequestType requestType = RequestType.getInstance(f.requestType)
        def request = new FunRequest(requestType)
        request.host = f.host
        request.path = f.path
        request.uri = f.uri
        request.args = f.args
        request.json = f.json
        request.params = f.params
        f.headers.each {
            request.addHeader(it.name, it.value)
        }
        request
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
