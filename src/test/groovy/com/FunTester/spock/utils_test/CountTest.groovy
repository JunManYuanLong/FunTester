package com.FunTester.spock.utils_test

import com.fun.utils.CountTool
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.fun.frame.SourceCode.getLogger

class CountTest extends Specification {

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

    def "测试统计数字出现次数"() {
        given:
        def list = [3, 2, 1, 2, 3, 3]
        def count = CountTool.count(list)

        expect:
        num == count.get(key)

        where:
        num || key
        1   || 1
        2   || 2
        3   || 3
    }

    def "测试统计数字出现次数，使用数组"() {
        given:
        def list = [3, 2, 1, 2, 3, 3]
        def count = CountTool.count(list)

        expect:
        num == count.get(num)

        where:
        num << [1, 2, 3]
    }

    def "测试map统计数据"() {
        given:
        def count = new HashMap()
        CountTool.count(count, 2)

        expect:
        1 == count.get(2)
        1 == CountTool.count(count, 2)
        2 == count.get(2)
    }

}
