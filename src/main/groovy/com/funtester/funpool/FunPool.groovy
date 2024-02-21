package com.funtester.funpool

import java.util.concurrent.LinkedBlockingQueue

/**
 * Object pool, using LinkedBlockingQueue to store objects, and using factory to create new objects, and using offer to return objects, and using poll to borrow objects, and using trimQueue to trim queue size
 * @param <F>
 */
class FunPool<F> {

    /**
     * Factory to create new objects
     */
    FunPooledFactory<F> factory

    /**
     * Object pool, using LinkedBlockingQueue to store objects
     */
    LinkedBlockingQueue<F> pool = new LinkedBlockingQueue<F>()

    FunPool(FunPooledFactory<F> factory) {
        this.factory = factory
    }

    /**
     * Borrow an object from the pool
     *
     * @param o the object to be borrowed
     * @return
     */
    F borrow() {
        F f = pool.poll()
        if (f == null) {
            f = factory.newInstance()
        }
        return f
    }

    /**
     * Return the object to the pool
     *
     * @param f the object to be returned
     */
    def back(F f) {
        boolean offer = pool.offer(f)
        if (!offer) f = null
    }

    /**
     * return size of object pool
     *
     * @return
     */
    int size() {
        return pool.size()
    }


    /**
     * Trim the queue size
     * @param size the size to be trimmed
     */
    def trimQueue(int size) {
        while (true) {
            if (size() <= size) break
            pool.poll()
        }
    }


}
