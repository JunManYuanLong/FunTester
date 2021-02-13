package com.FunTester.spock.pratice

import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.funtester.frame.SourceCode.getLogger

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

    def "校验对象"() {
        given:
        def per = new Per("fun", 12)
        expect:
        with(per) {
            name == "fun"
            age == 12
        }
    }


    def "when then结构测试"() {
        when:
        def s = plus(3, 2)
        def ss = plus(3, 2)
        then:
        verifyAll {
            s == 3
            ss == 3
        }
    }

/**
 * 测试方法
 * @param i
 * @param j
 * @return
 */
    def plus(int i, int j) {
        i
    }

/**
 * 测试类
 */
    class Per {

        Per(String name, int age) {
            this.name = name
            this.age = age
        }

        String name

        int age


    }
}



