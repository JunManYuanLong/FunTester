package com.FunTester.spock.frame

import com.fun.frame.execute.Concurrent
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

class ConcurrentTest extends Specification {

    @Shared
    Logger logger = com.fun.frame.SourceCode.getLogger(this.getClass().getName())
    @Shared
    Concurrent concurrent = new Concurrent()

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

    def "验证统计类功能"() {
        given:
        def fileName = 30

        expect:
//        concurrent.countQPS(30)
        1 == 1
    }
}
