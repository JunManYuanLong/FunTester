package com.FunTester.spock.utils_test

import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.fun.frame.SourceCode.getLogger

class WriteReadTest extends Specification{
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

    def "测试读写文件内容"() {
        given:

        expect:
        2 == 2
    }
}
