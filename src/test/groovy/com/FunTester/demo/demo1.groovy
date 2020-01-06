package com.FunTester.demo

import com.fun.frame.SourceCode

class demo1 extends SourceCode {

    public static void main(String[] args) {
        String ss = "fanddss"

        println ss - "fan"
        println ss.center(80, "-")
        println ss.padLeft(10)
        println ss.padLeft(10, PART)
        10.times {
            print it
        }

        println ss

        (0..10).each {
            print it
        }

        def s = "fun" * 3 << "fan"

        println s

        def greeting = 'Hello'
        greeting <<= ' Groovy'
        assert greeting instanceof java.lang.StringBuffer
        greeting << '!'
        assert greeting.toString() == 'Hello Groovy!'
        greeting[1..4] = 'i'
        assert greeting.toString() == 'Hi Groovy!'

        Demo demo = new Demo()
        Demo a = new Demo()

        Demo demo1 = demo + a

        Demo sssss = demo + 3

        Demo fsa = demo * 3

        Demo demo2 = demo / 9

        Demo demo3 = demo << a

        Demo demo4 = demo >> a

        Demo demo5 = demo++


        def i = 2 >>> 1

    }

    static class Demo {

        def plus(Demo demo) {
            output("加法对象")
            this
        }

        def plus(int s) {
            output("加法")
            this
        }

        def multiply(int a) {
            output("乘法")
            this
        }

        def div(int a) {
            output("除法")
            this
        }

        def leftShift(Demo demo) {
            output("<<操作")
            this
        }

        def rightShift(Demo demo) {
            output(">>操作")
            this
        }

        def next() {
            output("++操作")
            this
        }
    }
}
