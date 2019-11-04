package com.FunTester.mockito.utils_test

import com.fun.config.RequestType
import com.fun.frame.httpclient.FunRequest
import org.apache.http.Header
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.fun.frame.SourceCode.getJson
import static com.fun.frame.SourceCode.getLogger
import static org.mockito.ArgumentMatchers.anyInt
import static org.mockito.Mockito.*

class FunRequestTest extends Specification {

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
}
