package com.funtester.utils

import java.util.function.Function
import java.util.function.Predicate
import java.util.function.Supplier

/**
 * 处理Java与Groovy不兼容的问题
 **/
class JToG {

    static Closure toClosure(Supplier supplier) {
        return {
            supplier.get()
        }
    }

    static Closure toClosure(Function function) {
        return {def t -> function.apply(t)
        }
    }

    static Closure toClosure(Predicate predicate) {
        return {def t -> predicate.test(t)
        }
    }

}
