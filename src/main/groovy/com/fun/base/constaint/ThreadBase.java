package com.fun.base.constaint;

import com.fun.base.interfaces.MarkThread;
import com.fun.frame.SourceCode;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 多线程任务基类，可单独使用
 *
 * @param <T> 必需实现Serializable
 */
public abstract class ThreadBase<T> extends SourceCode implements Runnable {

    public String threadmark;

    /**
     * 错误数
     */
    public int errorNum;

    /**
     * 执行数,一般与响应时间记录数量相同
     */
    public int excuteNum;

    /**
     * 计数锁
     * <p>
     * 会在concurrent类里面根据线程数自动设定
     * </p>
     */
    protected CountDownLatch countDownLatch;

    /**
     * 标记对象
     */
    public MarkThread mark;

    /**
     * 用于设置访问资源,用于闭包中无法访问包外实例对象的情况
     */
    public T t;

    protected ThreadBase() {
    }

    /**
     * groovy无法直接访问t，所以写了这个方法,如果报错可以忽略,直接运行,兴许可以成功的
     *
     * @return
     */
    public String getTString() {
        return t.toString();
    }

    /**
     * 运行待测方法的之前的准备
     */
    protected abstract void before();

    /**
     * 待测方法
     *
     * @throws Exception 抛出异常后记录错误次数,一般在性能测试的时候重置重试控制器不再重试
     */
    protected abstract void doing() throws Exception;

    /**
     * 运行待测方法后的处理
     */
    protected void after() {
        if (countDownLatch != null)
            countDownLatch.countDown();
    }

    /**
     * 设置计数器
     *
     * @param countDownLatch
     */
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

    /**
     * Groovy乘法调用方法
     *
     * @param num
     * @return
     */
    public List<ThreadBase> multiply(int num) {
        return range(num).mapToObj(x -> this.clone()).collect(Collectors.toList());
    }


}
