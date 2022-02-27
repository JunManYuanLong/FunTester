package com.funtester.frame.execute;

import com.funtester.base.bean.PerformanceResultBean;
import com.funtester.base.constaint.FixedQps;
import com.funtester.base.constaint.ThreadBase;
import com.funtester.config.Constant;
import com.funtester.frame.Save;
import com.funtester.frame.SourceCode;
import com.funtester.httpclient.GCThread;
import com.funtester.utils.CountUtil;
import com.funtester.utils.RWUtil;
import com.funtester.utils.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

/**
 * 并发类，用于启动压力脚本
 */
public class FixedQpsConcurrent extends SourceCode {

    private static Logger logger = LogManager.getLogger(FixedQpsConcurrent.class);

    public static boolean needAbord = false;

    public static AtomicInteger executeTimes = new AtomicInteger(0);

    public static AtomicInteger errorTimes = new AtomicInteger(0);

    public static Vector<String> marks = new Vector<>();

    /**
     * 基础任务对象
     */
    public FixedQps baseThread;

    /**
     * 用于记录所有请求时间
     */
    public static Vector<Short> allTimes = new Vector<>();

    /**
     * 开始时间
     */
    public long startTime;

    /**
     * 结束时间
     */
    public long endTime;

    /**
     * 初始间隔时间
     */
    public long interval;

    /**
     * 总执行线程数
     */
    public int executeThread;

    /**
     * 任务队列的长度,因为会循环去那队列的任务
     */
    public int queueLength;

    /**
     * 任务描述
     */
    public String desc;

    /**
     * 任务集
     */
    public List<FixedQps> threads = new ArrayList<>();

    /**
     * 线程池
     */
    ThreadPoolExecutor executor;

    /**
     * @param thread 线程任务
     * @param desc   任务描述
     */
    public FixedQpsConcurrent(FixedQps thread, String desc) {
        this(desc);
        this.queueLength = 1;
        threads.add(thread);
        baseThread = thread;
    }

    /**
     * @param threads 线程组
     * @param desc    任务描述
     */
    public FixedQpsConcurrent(List<FixedQps> threads, String desc) {
        this(desc);
        this.threads = threads;
        baseThread = threads.get(0);
        this.queueLength = threads.size();
    }

    /**
     * 初始化连接池
     */
    private FixedQpsConcurrent(String desc) {
        this.desc = StatisticsUtil.getFileName(desc);
        if (executor == null)
            executor = ThreadPoolUtil.createCachePool(Constant.THREADPOOL_MAX);
    }

    private FixedQpsConcurrent() {

    }

    /**
     * 执行多线程任务
     * 默认取list中thread对象,丢入线程池,完成多线程执行,如果没有threadname,name默认采用desc+线程数作为threadname,去除末尾的日期
     */
    public PerformanceResultBean start() {
        boolean isTimesMode = baseThread.isTimesMode;
        int limit = baseThread.limit;
        int qps = baseThread.qps;
        executeThread = qps / Constant.QPS_PER_THREAD + 1;
        interval = 1_000_000_000 / qps;//此处单位1s=1000ms,1ms=1000000ns
        if (RUNUP_TIME > 0) {
            int runupTotal = qps * PREFIX_RUN;//计算总的请求量
            double diffTime = 2 * (Constant.RUNUP_TIME / PREFIX_RUN * interval - interval);//计算最大时间间隔和最小时间间隔差值
            double piece = diffTime / runupTotal;//计算每一次请求时间增量
            for (int i = runupTotal; i > 0; i--) {
                if (executor.getActiveCount() + 5 > Constant.THREADPOOL_MAX) continue;//避免出发线程池拒绝策略
                this.executor.execute(threads.get(limit-- % queueLength).clone());
                sleep((long) (interval + i * piece));
            }
            sleep(1.0);
            allTimes = new Vector<>();
            marks = new Vector<>();
            executeTimes.set(0);
            errorTimes.set(0);
        }
        logger.info("=========预热完成,开始测试!=========");
        ThreadBase.progress = new Progress(threads, StatisticsUtil.getTrueName(desc), executeTimes);
        new Thread(ThreadBase.progress, "progress").start();
        startTime = Time.getTimeStamp();
        CountDownLatch countDownLatch = new CountDownLatch(executeThread);
        for (int i = 0; i < executeThread; i++) {
            new FunTester(countDownLatch).start();
        }
        endTime = Time.getTimeStamp();
        try {
            countDownLatch.await();
            ThreadBase.progress.stop();
            GCThread.stop();
            executor.shutdown();
            executor.awaitTermination(Constant.WAIT_TERMINATION_TIMEOUT, TimeUnit.SECONDS);//此方法需要在shutdown方法执行之后执行
        } catch (InterruptedException e) {
            logger.error("线程池等待任务结束失败!", e);
        }
        logger.info("总计执行 {} ，共用时：{} s,执行总数:{},错误数:{}!", baseThread.isTimesMode ? baseThread.limit + "次任务" : "秒", Time.getTimeDiffer(startTime, endTime), formatLong(executeTimes), errorTimes);
        return over();
    }

    /**
     * 执行请求生成和执行类
     */
    private class FunTester extends Thread {

        FunTester(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        CountDownLatch countDownLatch;

        boolean isTimesMode = baseThread.isTimesMode;

        int limit = baseThread.limit / executeThread;

        long nanosec = interval * executeThread;

        @Override
        public void run() {
            try {
                while (true) {
                    try {
                        executor.execute(threads.get(limit-- % queueLength).clone());
                        executeTimes.getAndIncrement();
                        if (needAbord || (isTimesMode ? limit < 1 : Time.getTimeStamp() - startTime > limit)) break;
                        SourceCode.sleep(nanosec);
                    } catch (RejectedExecutionException e) {
                        logger.warn("线程池已满,任务被丢弃", e.getCause());
                    }
                }
            } catch (Exception e) {
                logger.warn("任务发生器执行发生错误了!", e);
            } finally {
                countDownLatch.countDown();
            }
        }

    }

    private PerformanceResultBean over() {
        needAbord = true;
        if (!ThreadBase.COUNT) return null;
        Save.saveIntegerList(allTimes, DATA_Path.replace(LONG_Path, EMPTY) + StatisticsUtil.getFileName(queueLength, desc));
        Save.saveStringListSync(marks, MARK_Path.replace(LONG_Path, EMPTY) + desc);
        allTimes = new Vector<>();
        marks = new Vector<>();
        int executeNum = executeTimes.getAndSet(0);
        int errorNum = errorTimes.getAndSet(0);
        return countQPS(queueLength, desc, startTime, endTime, executeNum, errorNum);
    }

    /**
     * 计算结果
     * <p>此结果仅供参考</p>
     * 由于fixQPS模型没有固定线程数,所以智能采取QPS=Q/T的计算,与concurrent有区别
     *
     * @param name 线程数
     */
    public PerformanceResultBean countQPS(int name, String desc, long start, long end, int executeNum, int errorNum) {
        List<String> strings = RWUtil.readByLine(Constant.DATA_Path + StatisticsUtil.getFileName(name, desc));
        int size = strings.size();
        List<Integer> data = strings.stream().map(x -> changeStringToInt(x)).collect(toList());
        int sum = data.stream().mapToInt(x -> x).sum();
        String statistics = StatisticsUtil.statistics(data, desc, name);
        double qps = executeNum * 1000.0 / (end - start);
        int qps2 = baseThread.qps;
        return new PerformanceResultBean(desc, Time.getTimeByTimestamp(start), Time.getTimeByTimestamp(end), name, size, sum / size, qps, qps2, getPercent(executeNum, errorNum), 0, executeNum, statistics, CountUtil.index(data).toString());
    }


}