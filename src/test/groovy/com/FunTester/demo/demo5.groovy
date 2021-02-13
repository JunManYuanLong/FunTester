package com.FunTester.demo

import com.funtester.frame.SourceCode

/**
 * Groovy元组
 */
class demo5 extends SourceCode {

    public static void main(String[] args) {
        def tuple = new Tuple('one', 1, getJson("demo=1"))
        println tuple.size() == 3
        println tuple.get(0) == 'one'
        println tuple[1] == 1
        println tuple.last().demo == 1
        //尝试修改tuple
        try {
            tuple.add('extra')
            println false
        } catch (Exception e) {
            println e
        }
        try {
            tuple.remove('one')
            println false
        } catch (Exception e) {
            println e
        }
        try {
            tuple[0] = 'new value'
            println false
        } catch (Exception e) {
            println e
        }
        //tuple2  Demo 到tuple9
        def pair = new Tuple2('two', 2)
        println pair.first == 'two'
        println pair.second == 2
        def tuple3 = new Tuple3("true", 3, 4)
        println tuple3.third

        def (String a, Integer b) = dd('sum', 1, 2, 3)
        println a == 'sum'
        println b == 6
    }

    static def dd(String key, int ... values) {
        new Tuple2(key, values.sum())
    }

}
