package com.FunTester.spock.pratice

import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.fun.frame.SourceCode.getLogger

class test01 extends Specification {

    @Shared
    int a = 10;

    @Shared
    Logger logger = getLogger(this.getClass().getName())

    def setupSpec() {
        logger.info "测试类开始! ${logger.getName()}"
    }

    def setup() {
        logger.info "测试方法开始！"
    }

    def "这是一个测试"() {
        given: "准备测试数据"
        def s = 3
        def ss = 3
        expect: "验证结果"
        s == ss
    }

    def "表达式测试，表达式左右运算符号"() {
        given: "准备数据"

        expect: "测试方法"
        z == x + y

        where: "校验结果"
        x | y || z
        1 | 0 || 1
        2 | 1 || 3
    }

    def "表达式测试，表达式左右对象方法"() {
        expect:
        name.size() == length
        where:
        name      | length
        "Spock"   | 5
        "Kirk"    | 4
        "Scotty"  | 6
        "Sc3otty" | 7
    }

    def "表达式测试，表达式左右对象方法,数组表示测试数据"() {
        expect:
        name.size() == length
        where:
        name << ["fjdslj", "fds"]
        length << [6, 3]
    }
}
