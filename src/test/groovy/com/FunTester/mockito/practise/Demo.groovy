package com.FunTester.mockito.practise

import org.apache.http.client.methods.HttpRequestBase
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.fun.config.Constant.SPACE_1
import static com.fun.frame.SourceCode.getLogger
import static org.mockito.Mockito.*

class Demo extends Specification {

    @Shared
    Logger logger = getLogger(this.getClass().getName())

    @Shared
    HttpRequestBase httpRequestBase = mock(HttpRequestBase.class)

    def setup() {
        logger.info("测试方法开始了")
    }

    def cleanup() {
        logger.info("测试方法结束了")
    }

    def setupSpec() {
        logger.info("测试类[${getClass().getName()}]开始了")
    }

    def cleanupSpec() {
        logger.info("测试类[${getClass().getName()}]结束了")
    }

    def "这是一个普通的demo"() {
        given:
        List mockedList = mock(List.class);
        mockedList.add("one");
        mockedList.add("two");
        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");
        when(mockedList.size()).thenReturn(5);
        mockedList.add("3")
        mockedList.add("3")
        mockedList.add("3")
        mockedList.add("3")

        expect:
        5 == mockedList.size()
        false == verify(mockedList, atLeastOnce()).add("one")
        false == verify(mockedList, times(3)).add("three times")
        false == verify(mockedList, atMost(4)).add("3")
    }

    def "这是一个测试的mockito模拟方法返回"() {
        given: "虚拟一个迭代器对象"
        def iterator = mock(Iterator.class)
        when(iterator.next()).thenReturn("hello").thenReturn("world")

        expect:
        "hello world" == iterator.next() + SPACE_1 + iterator.next()
    }

    def "这是一个测试,用来在对象初始化之后mock对象的"() {
        given: "创建对象后再Mockito"
        def iterator = new ArrayList()
        def list = spy(iterator)
        doReturn("fun").when(list).get(3)
        doReturn(3).when(list).get(0)

        expect:
        "fun" == list.get(3)
        3 == list.get(0)
    }

}
