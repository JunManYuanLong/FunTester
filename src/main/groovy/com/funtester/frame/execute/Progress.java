package com.funtester.frame.execute;

import com.funtester.base.constaint.*;
import com.funtester.base.exception.ParamException;
import com.funtester.frame.SourceCode;
import com.funtester.utils.StringUtil;
import com.funtester.utils.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * 用于异步展示性能测试进度的多线程类
 *
 * @param <F> 多线程任务{@link ThreadBase}对象的实现子类
 */
public class Progress<F extends ThreadBase> extends SourceCode implements Runnable {

    private static Logger logger = LogManager.getLogger(Progress.class);

    /**
     * 会长
     */
    private static final String SUFFIX = "QPS变化曲线";

    /**
     * 记录每一次获取QPS的值,可能用于结果展示
     */
    public List<Integer> qs = new ArrayList<>();

    /**
     * 多线程任务类对象
     */
    private List<F> threads;

    /**
     * 线程数,用于计算实时QPS
     */
    public int threadNum;

    /**
     * 进度条的长度
     */
    private static final int LENGTH = 67;

    /**
     * 标志符号
     */
    private static final String ONE = getPart(3);

    /**
     * 总开关,是否运行,默认true
     */
    private boolean st = true;

    /**
     * 是否次数模型
     */
    private boolean isTimesMode;

    /**
     * 用于区分固定QPS请求模型,这里不计算固定QPS模型中的实时QPS
     */
    private boolean canCount;

    /**
     * 多线程任务基类对象,本类中不处理,只用来获取值,若使用的话请调用clone()方法
     */
    private F base;

    /**
     * 统计请求量
     */
    private LongAdder excuteNum;

    /**
     * 限制条件
     */
    private int limit;

    /**
     * 非精确时间,误差可以忽略
     */
    private long startTime = Time.getTimeStamp();

    /**
     * 描述
     */
    public String taskDesc;

    /**
     * 运行信息,用于外部访问
     */
    public String runInfo;

    /**
     * 记录上一次请求总量
     */
    private int last;

    /**
     * 固定线程模型
     *
     * @param threads
     * @param desc
     */
    public Progress(final List<F> threads, String desc) {
        this.threads = threads;
        this.threadNum = threads.size();
        this.taskDesc = desc;
        this.base = threads.get(0);
        init();
    }

    /**
     * 适配固定QPS模型
     *
     * @param threads
     * @param desc
     * @param excuteNum
     */
    public Progress(final List<F> threads, String desc, final LongAdder excuteNum) {
        this.threads = threads;
        this.threadNum = threads.size();
        this.taskDesc = desc;
        this.excuteNum = excuteNum;
        this.base = threads.get(0);
        init();
    }

    /**
     * 初始化对象,对istimesMode和limit赋值
     */
    private void init() {
        if (base instanceof FixedThread) {
            FixedThread limitThread = (FixedThread) this.base;
            this.isTimesMode = limitThread.isTimesMode;
            this.limit = limitThread.limit;
            this.canCount = true;
        } else if (base instanceof FixedQps) {
            FixedQps fix = (FixedQps) base;
            this.canCount = false;
            this.isTimesMode = fix.isTimesMode;
            this.limit = fix.limit;
        } else {
            ParamException.fail("创建进度条对象失败!");
        }
    }

    @Override
    public void run() {
        double pro = 0;
        while (st) {
            sleep(LOOP_INTERVAL);
            pro = isTimesMode ? base.executeNum == 0 ? excuteNum.sum() * 1.0 / limit : base.executeNum * 1.0 / limit : (Time.getTimeStamp() - startTime) * 1.0 / limit;
            if (pro > 0.95) break;
            if (st) {
                runInfo = String.format("%s进度:%s  %s ,当前QPS: %d", taskDesc, getManyString(ONE, (int) (pro * LENGTH)), getPercent(pro * 100), getQPS());
                logger.info(runInfo);
            }
        }
    }

    /**
     * 获取某一个时刻的QPS
     *
     * @return
     */
    private int getQPS() {
        int qps = 0;
        if (canCount) {
            int sum = threads.stream().mapToInt(f -> f.executeNum).sum();
            qps = (sum - last) / (int) LOOP_INTERVAL;
            last = sum;
        } else {
            qps = excuteNum.intValue() / (int) ((Time.getTimeStamp() - startTime) / 1000);
        }
        qs.add(qps);
        return qps;
    }

    /**
     * 关闭线程,防止死循环
     */
    public void stop() {
        st = false;
        logger.info("{}进度:{}  {}", taskDesc, getManyString(ONE, LENGTH), "100%");
        printQPS();
    }

    /**
     * 打印QPS变化曲线
     */
    private void printQPS() {
        int size = qs.size();
        if (size < 5) return;
        if (size <= BUCKET_SIZE) {
            output(StatisticsUtil.draw(qs, StringUtil.center(taskDesc + SUFFIX, size * 3)));
        } else {
            double v = size * 1.0 / BUCKET_SIZE;
            List<Integer> qpss = range(BUCKET_SIZE).mapToObj(x -> qs.get((int) (x * v))).collect(Collectors.toList());
            output(StatisticsUtil.draw(qpss, StringUtil.center(taskDesc + SUFFIX, BUCKET_SIZE * 3)));
        }
    }

}
