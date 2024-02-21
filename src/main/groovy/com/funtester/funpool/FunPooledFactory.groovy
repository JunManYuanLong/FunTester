package com.funtester.funpool

/**
 * Factory to create new objects
 * @param <F>
 */
interface  FunPooledFactory<F> {

    /**
     * Create new objects
     * @return
     */
    F newInstance()

}