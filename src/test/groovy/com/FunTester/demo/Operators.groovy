package com.FunTester.demo

import com.fun.frame.SourceCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Operators extends SourceCode implements Comparable {

    private static Logger logger = LoggerFactory.getLogger(Operators.class)

    def plus(int i) {
        logger.info("操作符: + {}", i)

    }

    def minus(int i) {
        logger.info("操作符: - {}", i)

    }

    def multiply(int i) {
        logger.info("操作符: * {}", i)

    }

    def div(int i) {
        logger.info("操作符: / {}", i)

    }


    def mod(int i) {
        logger.info("操作符: % {}", i)

    }
    /**
     * 必需有返回值
     * <p>
     *     defv=a;a=a.next();v a = a.next(); a
     * </p>
     *
     * @return
     */
    def next() {
        logger.info("操作符: i++ 或者 ++i")
        this
    }
    /**
     * 必需有返回值
     * <p>
     *     defv=a;a= a.previous(); v
     * </p>
     *
     * @return
     */
    def previous() {
        logger.info("操作符: i-- 或者 --i")
        this
    }

    def power(int a = 3) {
        logger.info("操作符: ** ${a}")

    }

    def or(int a = 3) {
        logger.info("操作符: | ${a}")

    }

    def and(int a = 3) {
        logger.info("操作符: & ${a}")

    }

    def xor(int a = 3) {
        logger.info("操作符: ^ ${a}")

    }

    def bitwiseNegate() {
        logger.info("操作符: ~i")

    }

    def getAt(int a = 3) {
        logger.info("操作符: i[${a}]")

    }

    def putAt(int a = 3, int c = 3) {
        logger.info("操作符: i[${a}]=${c}")

    }

    def leftShift(int a = 3) {
        logger.info("操作符: <<  ${a}")

    }

    def rightShift(int a = 3) {
        logger.info("操作符: >>  ${a}")

    }

    def rightShiftUnsigned(int a = 3) {
        logger.info("操作符: >>>  ${a}")
    }

    def isCase(int a = 3) {
        logger.info("操作符: case  ${a}")
        true
    }
    /**
     * if (a implements Comparable) { a.compareTo(b) == 0 } else { a.equals(b) }* @param a
     * @return
     */
    def equals(int a = 3) {
        logger.info("操作符: ==  ${a}")
        true
    }

    @Override
    int compareTo(Object o) {
        logger.info("操作符: <=> >= > < 等等")
        return 0
    }

    def <T> T asType(Class<T> tClass) {
        logger.info(tClass.toString())
        if (tClass.equals(Integer)) 4
    }

    public static void main(String[] args) {
        def operators = new Operators()
        operators + 1
        operators - 1
        operators * 1
        operators / 1
        operators % 1
        operators++
        ++operators//此处调用的方法同样是next()
        operators--
        --operators//此处同i++
        operators**8
        operators | 89
        operators & 89
        operators ^ 89
        ~operators
        operators[0]
        operators[2] = 3
        operators << 4
        operators >> 4
        operators >>> 4
        switch (2) {
            case operators:
                println 333
                break
        }
        println "a in b ${2 in operators}"
        if (operators == 3) println 3323
        println operators >= 1
        println operators as Integer

    }


}
