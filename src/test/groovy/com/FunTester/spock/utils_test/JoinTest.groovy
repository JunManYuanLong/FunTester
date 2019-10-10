package com.FunTester.spock.utils_test

import com.fun.utils.Join
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.fun.config.Constant.SPACE_1
import static com.fun.frame.SourceCode.getLogger

class JoinTest extends Specification {

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

    def "测试字符串用符号拼接"() {
        given: "准备测试数据"
        def str = "hello"
        expect: "验证测试方法"
        "h e l l o" == (Join.join(str, SPACE_1))
    }

    def "测试字符串用符号拼接，使用连接符"() {
        given: "准备测试数据"
        def str = "hello"
        expect: "验证测试方法"
        "h_e_l_l_o" == Join.join(str, "_")
    }

    def "测试字符串用符号拼接，前后缀"() {
        given: "准备测试数据"
        def str = "hello"
        expect: "验证测试方法"
        "[h_e_l_l_o]" == Join.join(str, "_", "[", "]")
    }

    def "测试list"() {
        given: "准备测试数据"
        def list = 0..3 as List

        expect: "验证测试方法"
        str == Join.join(list, sep)

        where:
        str       || sep
        "0123"    || ""
        "0.1.2.3" || "."
        "0 1 2 3" || " "
    }
}
