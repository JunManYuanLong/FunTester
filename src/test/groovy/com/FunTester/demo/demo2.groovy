package com.FunTester.demo

import com.fun.frame.SourceCode

class demo2 extends SourceCode {

    public static void main(String[] args) {
        def str = "fantester"
        def matcher = str =~ "\\wt"
        println matcher.find()
        println matcher[0]
        println matcher.size()
        matcher.each {println it}
        def b = str ==~ ".*er"
        output b

        def stra = /.*test\w+/

        println str ==~ stra

        ("fanfanfanfan" =~ "\\wf").each {println it}

        "fanfanfanfan".eachMatch(/\wa/){println it}

        new File(getLongFile("1")).eachLine {println it}

        new File(getLongFile("33")) << DEFAULT_STRING
        def a = [4,2432,43,24,2,4,32,42,4,2]
        def set = a.toSet()
        println set
        def any = a.any {
            it > 100
        }
        println any
        def every = a.every {
            it < 10000
        }
        println every
    }
}
