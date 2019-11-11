package com.FunTester.spock.pratice

import com.fun.config.PropertyUtils
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.fun.config.Constant.getLongFile
import static com.fun.frame.Output.output
import static com.fun.frame.SourceCode.*

class Test02 extends Specification {

    @Shared
    def properties = PropertyUtils.getLocalProperties(getLongFile("1"))

    @Shared
    def cc = Arrays.asList(properties.getArrays("c")).stream().map {x -> Integer.valueOf(x)}.collect() as List

    @Shared
    def bb = Arrays.asList(properties.getArrays("b")).stream().map {x -> Integer.valueOf(x)}.collect() as List

    @Shared
    def aa = Arrays.asList(properties.getArrays("a")).stream().map {x -> Integer.valueOf(x)}.collect() as List

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
        def arrays = properties.getArrays("id")
        def s1 = properties.getPropertyInt("size1")
        def s2 = properties.getPropertyInt("size2")
        def list = Arrays.asList(arrays).stream().filter {x -> x.length() > 1}.collect() as List

        expect:
        s1 == arrays.size()
        s2 == list.size()
    }

    def "测试自定义对象操作"() {
        given: "给一个自定义对象"
        def per = new Per()
        per.age = 23
        per.name = "FunTester"
        def a = per

        expect:
        a.name == "FunTester"

    }

    def "线程安全测试"() {
        given: "多线程支持测试,此处线程数改成很大之后效果比较明显"
        range(2).forEach {new Per().start()}
        sleep(1000)
        output(Per.i)
        expect:
        4 == Per.i
    }

    def "测试集合验证使用数据驱动"() {
        given: "此处写的无法被where使用"

        expect:
        c * c == a * a + b * b

        where:
        c << cc
        b << bb
        a << aa
    }


/**
 * 测试类
 */
    class Per extends Thread {

        static int i

        @Override
        public void run() {
            i++
            sleep(100)
            i++
        }

        Per() {
        }

        Per(String name, int age) {
            this()
            this.name = name
            this.age = age
        }

        String name

        int age

        static def getAll() {
            i
        }

    }

}