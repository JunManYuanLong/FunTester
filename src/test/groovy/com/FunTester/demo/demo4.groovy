package com.FunTester.demo

import com.fun.frame.SourceCode
import com.alibaba.fastjson.JSONObject

/**
 * Groovy中的字典和方法参数默认值
 */
class demo4 extends SourceCode {

    public static void main(String[] args) {
        println ssss("foj")
        println s("foj3")

        Map map = [a: 2, b: 3]
        println map
        map.get("c",3)
        map.fun = 323
        println map
        def list = [3,4]

//        println s(*list)//编译器会报错,但是可以用

        List<JSONObject> ss = new ArrayList<>()
        ss.add(getJson("324=23"))
        ss.add(getJson("324=23"))
        output ss*.toString()
    }

    static def ssss(i) {
        i.toString()
    }

    static def s(i, k = 3333) {
        i + k
    }

}
