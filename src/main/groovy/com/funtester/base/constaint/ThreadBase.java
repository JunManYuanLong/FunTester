package com.funtester.base.constaint;

import com.funtester.base.interfaces.MarkThread;
import com.funtester.frame.SourceCode;
import com.funtester.frame.execute.Progress;
import com.funtester.httpclient.FunLibrary;
import com.funtester.utils.CountUtil;
import com.funtester.utils.Time;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 多线程任务基类，可单独使用
 *
 * @param <F> 必需实现Serializable
 */
public abstract class ThreadBase<F> extends SourceCode implements Runnable, Serializable {

    private static final long serialVersionUID = -1282879464717720145L;

    /**
     * 全局的时间终止开关,true表示终止,false表示不终止.程序运行中是false,结束运行是true,保障只有一个程序在运行
     */
    private static AtomicBoolean ABORT = new AtomicBoolean(true);

    /**
     * 是否记录响应时间,默认否
     */
    public static boolean COUNT = false;

    /**
     * 拦截请求响应时间
     */
    public static boolean INTERCEPT = false;

    /**
     * 用于存放拦截的请求响应时间
     */
    public static List<Short> interceptCosts = new ArrayList<>();

    /**
     * 用于记录当前执行状态信息
     */
    public static Progress progress;

    /**
     * 根据属性isTimesMode判断,次数或者时间(单位ms)
     */
    public int limit;

    /**
     * 测试模式,按照次数或者按照时间
     */
    public boolean isTimesMode;

    /**
     * 线程的名字
     */
    public String threadName;

    /**
     * 线程标记对象,用户标记请求或者单次执行任务的
     */
    public String threadmark;

    /**
     * 错误数
     * <p>这里注意使用{@link FunLibrary#getHttpResponse(org.apache.http.client.methods.HttpRequestBase)}方法获取响应的功能封装方法,即使报错也不会抛异常.这样会导致errorNum错误数为零</p>
     */
    public int errorNum;

    /**
     * 执行数,一般与响应时间记录数量相同
     */
    public int executeNum;

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
     * 用于设置访问资源,用于闭包中无法访问包外实例对象的情况,这里还有一个用处就是在标记线程对象的时候,用到了这个t(参数标记模式中)
     *
     * @since 2020年10月19日, 统一用来设置HTTPrequestbase对象.同样可以用于执行SQL和redis查询语句或者对象, 暂未使用dubbo尝试
     */
    public F f;

    protected ThreadBase() {
    }

    /**
     * 记录所有超时的请求标记
     */
    public List<String> marks = new ArrayList<>();

    /**
     * 用于存储请求耗时集合
     * 2021年03月16日,将统计集合提取为对象属性,用于外部访问,可用于取样器实现
     */
    public List<Short> costs = new ArrayList<>();

    /**
     * 运行待测方法的之前的准备
     */
    public void before() {
        ABORT.set(false);
    }

    /**
     * 待测方法
     *
     * @throws Exception 抛出异常后记录错误次数,一般在性能测试的时候重置重试控制器不再重试
     */
    protected abstract void doing() throws Exception;

    /**
     * 运行待测方法后的处理,不适用于动态模型{@link FunThread}
     */
    protected void after() {
        costs = new ArrayList<>();
        marks = new ArrayList<>();
        stop();
        if (countDownLatch != null)
            countDownLatch.countDown();
    }

    /**
     * 记录响应时间
     *
     * @param s 开始时间
     */
    public void count(long s) {
        if (COUNT) costs.add((short) (Time.getTimeStamp() - s));
        if (INTERCEPT) interceptCosts.add((short) (Time.getTimeStamp() - s));
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
     * 此处若具体实现类而非虚拟类建议自己写clone方法,子类重写需注意{@link ThreadBase#initBase()}方法调用
     * </p>
     *
     * @return
     */
    @Override
    public abstract ThreadBase clone();

    /**
     * 用于对象拷贝之后,清空存储列表
     */
    public void initBase() {
        this.executeNum = 0;
        this.errorNum = 0;
        this.costs = new ArrayList<>();
        this.marks = new ArrayList<>();
    }

    /**
     * 线程任务是否需要提前关闭,默认返回false,不再统一实现,内部静态类中实现
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

    /**
     * 用于在某些情况下提前终止测试
     */
    public static void stop() {
        ABORT.set(true);
    }

    /**
     * true表示终止,false表示不终止.运行中false,非运行状态true
     *
     * @return
     */
    public static boolean needAbort() {
        return ABORT.get();
    }

    public static void startIntercept() {
        INTERCEPT = true;
    }

    public static void stopIntercept() {
        INTERCEPT = false;
    }

    /**
     * 统计RT,清空{@link ThreadBase#interceptCosts}
     *
     * @return
     */
    public static CountUtil.FunIndex count() {
        stopIntercept();
        sleep(1.0);
        CountUtil.FunIndex index = CountUtil.index(interceptCosts);
        interceptCosts = new Vector<>();
        return index;
    }

}
