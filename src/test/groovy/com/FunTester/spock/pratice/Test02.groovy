package com.FunTester.spock.pratice

import com.fun.config.PropertyUtils
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.fun.config.Constant.getLongFile
import static com.fun.frame.SourceCode.getLogger

class Test02 extends Specification {

    @Shared
    Logger logger = getLogger(Test02.class.getName())

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

    def "测试数据驱动Demo"() {
        given:
        int c = 0

        expect:
        10 == a + b + c

        where:
        a | b
        1 | 9
        8 | 2
    }

    def "测试数据读写完成数据驱动"() {
        given:
        def a = 0
        def properties = PropertyUtils.getLocalProperties(getLongFile("1"))
        def arrays = properties.getArrays("id")
        def s1 = properties.getPropertyInt("size1")
        def s2 = properties.getPropertyInt("size2")
        def list = Arrays.asList(arrays).stream().filter{x -> x.length() > 1}.collect() as List
        expect:
        s1 == arrays.size()
        s2 == list.size()
    }

}
