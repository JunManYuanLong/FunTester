package com.FunTester.spock.utils_test

import com.funtester.utils.ArgsUtil
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

class ArgsUtilTest extends Specification {

    @Shared
    Logger logger = com.funtester.frame.SourceCode.getLogger(this.getClass().getName())

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

    def "测试从args获取参数的"() {
        given:
        String[] args = ["abc", "ccc"]
        ArgsUtil util = new ArgsUtil(args)

        expect:
        2 == util.getIntOrdefault(3, 2)
    }

    def "测试从args获取参数的Demo正确执行"() {
        given:
        String[] args = ["2", "12"]
        ArgsUtil util = new ArgsUtil(args)

        expect:
        12 == util.getIntOrdefault(1, 2)
    }

    def "测试从args获取参数的,高级语法"() {
        expect:
        value == new ArgsUtil(abc).getIntOrdefault(1, 2)

        where:
        value | abc
        32 | ["2","32"] as String[]
        2 | ["da"] as String[]
    }
}
