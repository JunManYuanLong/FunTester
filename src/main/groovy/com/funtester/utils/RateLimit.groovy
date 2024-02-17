package com.funtester.utils

import com.funtester.frame.SourceCode

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

/**
 * 限流工具,支持N/M限流
 */
class RateLimit {

    /**
     * 总限流配置
     */
    Map<String, LimitConfig> config = [:]

    /**
     * 最后一次请求时间
     */
    Map<String, Integer> lastTime = [:]

    /**
     * 请求次数
     */
    Map<String, AtomicInteger> requestTimes = [:]

    /**
     * 所有key的锁
     */
    Map<String, ReentrantLock> allLock = [:]

    /**
     * 写锁
     */
    ReentrantLock writeLock = new ReentrantLock()

    /**
     * 是否限流
     * @param key 限流key
     * @return
     */
    boolean isLimit(String key) {
        if (!config.containsKey(key)) {
            addConfig(key, 2, 2) //默认配置
            return isLimit(key) //递归,初始化配置
        }
        def mark = SourceCode.getMark()
        if (mark - lastTime[key] >= config[key].duration) {//进入下一个限流周期
            if (allLock[key].tryLock(1, TimeUnit.SECONDS)) {
                if (mark - lastTime[key] >= config[key].duration) {
                    lastTime[key] = mark
                    requestTimes[key] = new AtomicInteger(1)
                    allLock[key].unlock()
                    return false
                } else {
                    return true
                }
            }
        }
        if (requestTimes[key].get() >= config[key].maxTimes) //超过最大次数
            return true
        requestTimes[key].getAndIncrement() //增加次数
        return false
    }

    /**
     * 添加限流配置
     * @param key 限流key
     * @param maxTimes 最大次数
     * @param duration 限流时间,单位秒
     * @return
     */
    def addConfig(String key, int maxTimes, int duration) {
        if (writeLock.tryLock(1, TimeUnit.SECONDS)) {
            try {
                if (!config.containsKey(key)) {
                    config[key] = new LimitConfig(maxTimes: maxTimes, duration: duration)
                    allLock[key] = new ReentrantLock()
                    lastTime[key] = SourceCode.getMark()
                    requestTimes[key] = new AtomicInteger(0)
                }
            } catch (e) {

            } finally {
                writeLock.unlock()
            }
        }
    }

    /**
     * 限流配置
     */
    static class LimitConfig {

        /**
         * 最大次数
         */
        int maxTimes

        /**
         * 限流时间计算持续时间,单位秒
         */
        int duration

    }
}
