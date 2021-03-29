package com.funtester.frame.execute;

import com.funtester.base.constaint.FixedQpsThread;
import com.funtester.base.constaint.ThreadBase;
import com.funtester.base.constaint.ThreadLimitTimeCount;
import com.funtester.base.constaint.ThreadLimitTimesCount;
import com.funtester.base.exception.ParamException;
import com.funtester.config.HttpClientConstant;
import com.funtester.frame.SourceCode;
import com.funtester.utils.StringUtil;
import com.funtester.utils.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
    private int threadNum;

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
     * 在固定QPS模式中使用
     */
    private AtomicInteger excuteNum;

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
    private String taskDesc;

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
    public Progress(final List<F> threads, String desc, final AtomicInteger excuteNum) {
        this.threads = threads;
        this.threadNum = threads.size();
        this.taskDesc = desc;
        this.base = threads.get(0);
        init();
    }

    /**
     * 初始化对象,对istimesMode和limit赋值
     */
    private void init() {
        if (base instanceof ThreadLimitTimeCount) {
            this.isTimesMode = false;
            this.canCount = true;
            this.limit = ((ThreadLimitTimeCount) base).time;
        } else if (base instanceof ThreadLimitTimesCount) {
            this.isTimesMode = true;
            this.canCount = true;
            this.limit = ((ThreadLimitTimesCount) base).times;
        } else if (base instanceof FixedQpsThread) {
            FixedQpsThread fix = (FixedQpsThread) base;
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
            sleep(HttpClientConstant.LOOP_INTERVAL);
            pro = isTimesMode ? base.executeNum == 0 ? FixedQpsConcurrent.executeTimes.get() * 1.0 / limit : base.executeNum * 1.0 / limit : (Time.getTimeStamp() - startTime) * 1.0 / limit;
            if (pro > 0.95) break;
            if (st)
                logger.info("{}进度:{}  {} ,当前QPS: {}", taskDesc, getManyString(ONE, (int) (pro * LENGTH)), getPercent(pro * 100), getQPS());
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
            List<Integer> times = new ArrayList<>();
            for (int i = 0; i < threadNum; i++) {
                List<Integer> costs = threads.get(i).costs;
                int size = costs.size();
                if (size < 3) continue;
                times.add(costs.get(size - 1));
                times.add(costs.get(size - 2));
            }
            qps = times.isEmpty() ? 0 : (int) (1000 * threadNum / (times.stream().collect(Collectors.summarizingInt(x -> x)).getAverage()));
        } else {
            qps = excuteNum.get() / (int) (Time.getTimeStamp() - startTime);
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
