package com.fun.base.constaint;

import com.fun.frame.SourceCode;

import java.util.concurrent.CountDownLatch;

/**
 * 多线程任务基类，可单独使用
 */
public abstract class ThreadBase<T> extends SourceCode implements Runnable {

    public int errorNum;

    /**
     * 计数锁
     * <p>
     * 会在concurrent类里面根据线程数自动设定
     * </p>
     */
    CountDownLatch countDownLatch;

    /**
     * 用于设置访问资源
     */
    public T t;

    protected ThreadBase() {
    }

    /**
     * groovy无法直接访问t，所以写了这个方法
     *
     * @return
     */
    public String getT() {
        return t.toString();
    }

    /**
     * 运行待测方法的之前的准备
     */
    protected abstract void before();

    /**
     * 待测方法
     *
     * @throws Exception
     */
    protected abstract void doing() throws Exception;

    /**
     * 运行待测方法后的处理
     */
    protected abstract void after();

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }


}
