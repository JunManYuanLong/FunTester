package com.fun.base.constaint;

import com.fun.frame.SourceCode;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;

/**
 * 多线程任务基类，可单独使用
 *
 * @param <T> 必需实现Serializable
 */
public abstract class ThreadBase<T> extends SourceCode implements Runnable, Serializable {

    private static final long serialVersionUID = -1060930011532994028L;

    public int errorNum;

    public int excuteNum;

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

    /**
     * 拷贝对象方法,用于统计单一对象多线程调用时候的请求数和成功数,对于<T>的复杂情况,需要将T类型也重写clone方法
     *
     * <p>
     * 此处若具体实现类而非虚拟类建议自己写clone方法
     * </p>
     *
     * @return
     */
    @Override
    public ThreadBase clone() {
        return deepClone(this);
    }

    /**
     * 线程任务是否需要提前关闭,默认返回false
     * <p>
     * 一般用于单线程错误率过高的情况
     * </p>
     *
     * @return
     */
    public boolean status() {
        return false;
    }


}
