package com.FunTester.mockito.utils_test

import com.funtester.config.RequestType
import com.funtester.frame.SourceCode
import com.funtester.httpclient.FunLibrary
import com.funtester.httpclient.FunRequest
import org.apache.http.Header
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.funtester.frame.Output.output
import static com.funtester.frame.SourceCode.getJson
import static org.mockito.ArgumentMatchers.anyInt
import static org.mockito.Mockito.*

class FunRequestTest extends Specification implements Serializable {

    private static final long serialVersionUID = -2751257651625435030L;

    @Shared
    Logger logger = getLogger(this.getClass().getName())

    def setupSpec() {
        logger.info "测试类开始! ${logger.getName()}"
    }

    def setup() {
        logger.info "测试方法开始！"
    }

    def cleanup() {
        logger.info "测试方法结束！"
    }

    def cleanupSpec() {
        logger.info "测试类结束! ${logger.getName()}"
    }

    def "简单测试demo"() {
        given: "测试对象"
        def json = getJson("name=123", "sex=2", "age=23", "token=abcd")
        def request = mock(FunRequest.class)
        when(request.getResponse()).thenReturn(json)

        expect:
        request.getResponse() == json
    }

    def "校验header的demo"() {
        given: "测试对象"
        def request = mock(FunRequest)
        def header = mock(Header)
        when(header.getName()).thenReturn("name")
        when(header.getValue()).thenReturn("FunTester")
        def list = mock(List)
        when(request.getHeaders()).thenReturn(list)
        doReturn(header).when(list).get(anyInt())
        doReturn(3).when(list).size()

        expect:
        request.getHeaders().size() == 3
        request.getHeaders().get(1).getName() == "name"
    }

    def "校验requesttype"() {
        given: "准备测试数据"
        def request = mock(FunRequest)
        doReturn(RequestType.POST).when(request).getRequestType()

        expect:
        request.getRequestType() == RequestType.POST
    }

    def "校验url测试"() {
        given:
        def request = FunRequest.isGet().setUri("www.funtest.cn")

        expect:
        "www.funtest.cn" == request.getUri()
    }

    def "测试拷贝请求的功能"() {
        given:
        HttpPost httpPost = FunLibrary.getHttpPost("https://cn.bing.com/search", SourceCode.getJson("q=fun"));
        FunLibrary.getHttpResponse(httpPost);

//        FunRequest.save(httpPost, getJson("3242=234"));

        HttpRequestBase httpRequestBase = FunRequest.cloneRequest(httpPost);

        HttpRequestBase base = FunRequest.doCopy(httpPost);

        FunLibrary.getHttpResponse(httpRequestBase);
        FunLibrary.getHttpResponse(base);



        output "拷贝请求成功!"
    }


    def "测试拷贝GET请求的功能"() {
        given:
        def re = FunLibrary.getHttpGet("https://cn.bing.com/search", SourceCode.getJson("q=fun"));
        FunLibrary.getHttpResponse(re);

//        FunRequest.save(httpPost, getJson("3242=234"));

        HttpRequestBase httpRequestBase = FunRequest.cloneRequest(re);

        HttpRequestBase base = FunRequest.doCopy(re);

        FunLibrary.getHttpResponse(httpRequestBase);
        FunLibrary.getHttpResponse(base);



        output "拷贝GET请求成功!"
    }

    def "测试get请求的url,save功能"() {
        given:
        def re = FunLibrary.getHttpGet("https://cn.bing.com/search", SourceCode.getJson("q=fun"));

        HttpRequestBase httpRequestBase = FunRequest.cloneRequest(re);

        def response = FunLibrary.getHttpResponse(re)

        FunRequest.save(re, response)


        output "拷贝GET请求成功!"
    }

}
