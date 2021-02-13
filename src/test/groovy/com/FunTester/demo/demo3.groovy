package com.FunTester.demo

import com.funtester.frame.SourceCode
import com.alibaba.fastjson.JSONObject

/**
 * Groovy中的闭包
 */
class demo3 extends SourceCode {

    public static void main(String[] args) {

        def a = 0..2

        def Closure get = {x -> println x}

        a.each(get)

        a.each(fff())

        JSONObject ss = getJson("3243=423", "3242=23332")

        ss.each {println it.getKey() + CONNECTOR + it.getValue()}

        Closure aa = {println it.getKey() + CONNECTOR + it.getValue()}
        println a

        ss.each(aa)

        ss.each(sss())

        def plus = {x, y -> return x + y % 2}

        println plus(3, 5)

        println plus.call(3, 2)


        def fun = {
            num, fc, Line -> println fc(num) + "${new Date()}" + "$Line"
        }

        def fc = {x -> return x % 2}

        fun(3, fc, "few")

        def cc = fun.curry(2, fc)

        cc("fdsa")

        def d1 = dd(3)

        println d1(32)

        println a.collect {it * 2}

        def list = a.toList()

        while (list) {
            list.remove(0)
        }

        while (a = 3) {
            println a
            break
        }

        if ((a = 2)) println 32

    }

    static def Closure sss(entry) {
        return {println it.key + CONNECTOR + it.value}
    }


    static def Closure fff() {
        return {x -> println x}
    }

    static def dd(int i) {
        return {i += it}
    }


}
