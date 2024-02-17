package com.funtester.utils


import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
/**
 * 限流工具,基于Caffeine实现,支持动态配置,根据TPS限流
 */
class TpsLimit {

    Map<String, Integer> qpsConfig = [:]

    LoadingCache<Object, AtomicInteger> build = Caffeine.newBuilder().refreshAfterWrite(1, TimeUnit.SECONDS).build((key) -> {
        return new AtomicInteger()
    })

    /**
     * 是否限流
     * @param key
     * @return
     */
    boolean isLimit(String key) {
        AtomicInteger atomicInteger = build.get(key)
        if (atomicInteger.get() >= qpsConfig.get(key, 1)) {
            return true
        }
        atomicInteger.incrementAndGet()
        return false
    }

    /**
     * 添加限流配置
     * @param key
     * @param qps
     * @return
     */
    def addConfig(String key, Integer qps) {
        qpsConfig.put(key, qps)
    }

}
