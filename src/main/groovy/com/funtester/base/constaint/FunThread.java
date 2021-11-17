package com.funtester.base.constaint;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Vector;

public abstract class FunThread<F> extends ThreadBase {

    private static final long serialVersionUID = 7878297575504772944L;

    private static final Logger logger = LogManager.getLogger();

    /**
     * 统一管理所有存活线程
     */
    private static Vector<FunThread> threads = new Vector<>();

    /**
     * 单线程中断开关,用于动态调整并发压力,默认值false
     */
    private boolean BREAK_KEY = false;

    public FunThread(F f, String name) {
        this.isTimesMode = true;
        this.threadName = name;
        this.limit = Integer.MAX_VALUE;
        this.f = f;
    }

    protected FunThread() {
        super();
    }


    @Override
    public void run() {
        before();
        while (!BREAK_KEY) {
            try {
                doing();
            } catch (Exception e) {
                logger.warn("执行任务失败！", e);
            }
        }
    }

    /**
     * 运行待测方法的之前的准备
     */
    public void before() {
    }

    /**
     * 动态模型正常不会结束
     */
    protected void after() {
    }


    private static synchronized boolean checkName(String name) {
        for (FunThread thread : threads) {
            String threadName = thread.threadName;
            if (StringUtils.isAnyBlank(threadName, name) || threadName.equalsIgnoreCase(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 拷贝对象方法,用于统计单一对象多线程调用时候的请求数和成功数,对于<T>的复杂情况,需要将T类型也重写clone方法
     *
     * @return
     */
    @Override
    public abstract FunThread clone();

    /**
     * 线程终止,用于动态调节并发压力
     */
    public void interrupt() {
        BREAK_KEY = true;
    }

    /**
     * 用于在某些情况下提前终止测试
     */
    public static synchronized void stop() {
        threads.forEach(f -> f.interrupt());
        threads.clear();
    }

    public static synchronized boolean addThread(FunThread base) {
        if (!checkName(base.threadName)) return false;
        return threads.add(base);
    }

    /**
     * 删除某个任务,或者停止
     *
     * @param base
     */
    public static synchronized void remoreThread(FunThread base) {
        base.interrupt();
        threads.remove(base);
    }

    public static synchronized FunThread find(String name) {
        for (int i = 0; i < threads.size(); i++) {
            FunThread funThread = threads.get(i);
            if (StringUtils.isNoneBlank(funThread.threadName, name) && funThread.threadName.equalsIgnoreCase(name)) {
                return funThread;
            }
        }
        return null;
    }

    public static synchronized void remoreThread(String name) {
        FunThread funThread = find(name);
        if (funThread == null) remoreThread(funThread);
    }

    public static synchronized FunThread getRandom() {
        return random(threads);
    }

    public static synchronized int aliveSize() {
        return threads.size();
    }

}