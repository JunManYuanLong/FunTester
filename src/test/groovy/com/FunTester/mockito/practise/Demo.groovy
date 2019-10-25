package com.FunTester.mockito.practise

import org.apache.http.client.methods.HttpRequestBase
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.fun.config.Constant.SPACE_1
import static com.fun.frame.SourceCode.getLogger
import static org.mockito.AdditionalAnswers.returnsFirstArg
import static org.mockito.Matchers.anyInt
import static org.mockito.Mockito.*

class Demo extends Specification {

    @Shared
    Logger logger = getLogger(this.getClass().getName())

    @Shared
    List listsss = mock(List)

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
        false == verify(mockedList, never()).add("30")
    }

    def "这是一个测试的mockito模拟方法返回"() {
        given: "虚拟一个迭代器对象"
        def iterator = mock(Iterator.class)
        when(iterator.next()).thenReturn("hello").thenReturn("world")

        expect: "测试迭代器元素拼接"
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

    def "这是一个测试,抛出异常的测试用例"() {
        given: "创建测试对象"
        def object = mock(ArrayList.class)
        when(object.get(1)).thenThrow(new IndexOutOfBoundsException("我是测试"))//只能抛出可能的抛出的异常
        def re = 0
        try {
            object.get(1)
        } catch (IndexOutOfBoundsException e) {
            re = 1
        }

        expect:
        re == 1
    }

    def "这是一个测试方法返回值的用例"() {
        given:
        def j = mock(DemoJ.class)
        doAnswer(returnsFirstArg()).when(j).ds(anyInt(), anyInt())

//        when(list.add(anyString())).thenAnswer(returnsFirstArg());
        // with then() alias:
//        when(list.add(anyString())).then(returnsFirstArg());
        expect:
        3 == j.ds(3, 32)

    }

    def "我是测试共享Mock对象的用例"() {
        given:

        when(listsss.get(anyInt())).thenReturn(3)

        expect:
        3 == listsss.get(3)
    }

}
