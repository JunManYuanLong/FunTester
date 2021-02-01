package com.fun.frame.execute;

import com.fun.base.bean.PerformanceResultBean;
import com.fun.base.constaint.FixedQpsThread;
import com.fun.config.Constant;
import com.fun.config.HttpClientConstant;
import com.fun.frame.Save;
import com.fun.frame.SourceCode;
import com.fun.frame.httpclient.GCThread;
import com.fun.utils.Time;
import com.fun.utils.WriteRead;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

/**
 * 并发类，用于启动压力脚本
 */
@SuppressFBWarnings({"ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD", "MS_SHOULD_BE_FINAL", "MS_PKGPROTECT"})
public class FixedQpsConcurrent extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(FixedQpsConcurrent.class);

    public static boolean key = false;

    public static AtomicInteger executeTimes = new AtomicInteger(0);

    public static AtomicInteger errorTimes = new AtomicInteger(0);

    @SuppressFBWarnings("MS_CANNOT_BE_FINAL")
    public static Vector<String> marks = new Vector<>();

    /**
     * 基础任务对象
     */
    public FixedQpsThread baseThread;

    /**
     * 用于记录所有请求时间
     */
    @SuppressFBWarnings("MS_CANNOT_BE_FINAL")
    public static Vector<Long> allTimes = new Vector<>();

    /**
     * 开始时间
     */
    public long startTime;

    /**
     * 结束时间
     */
    public long endTime;

    /**
     * 任务队列的长度,因为会循环去那队列的任务
     */
    public int queueLength;

    /**
     * 任务描述
     */
    public String desc = DEFAULT_STRING;

    /**
     * 任务集
     */
    public List<FixedQpsThread> threads = new ArrayList<>();

    /**
     * 线程池
     */
    ExecutorService executorService;

    /**
     * @param thread 线程任务
     */
    public FixedQpsConcurrent(FixedQpsThread thread) {
        this(thread, DEFAULT_STRING);
    }

    /**
     * @param threads 线程组
     */
    public FixedQpsConcurrent(List<FixedQpsThread> threads) {
        this(threads, DEFAULT_STRING);
    }

    /**
     * @param thread 线程任务
     * @param desc   任务描述
     */
    public FixedQpsConcurrent(FixedQpsThread thread, String desc) {
        this();
        this.queueLength = 1;
        threads.add(thread);
        baseThread = thread;
        this.desc = desc + Time.getNow();
    }

    /**
     * @param threads 线程组
     * @param desc    任务描述
     */
    public FixedQpsConcurrent(List<FixedQpsThread> threads, String desc) {
        this();
        this.threads = threads;
        baseThread = threads.get(0);
        this.queueLength = threads.size();
        this.desc = desc + Time.getNow();
    }

    /**
     * 初始化连接池
     */
    private FixedQpsConcurrent() {
        executorService = ThreadPoolUtil.createPool(HttpClientConstant.THREADPOOL_CORE, HttpClientConstant.THREADPOOL_MAX, HttpClientConstant.THREAD_ALIVE_TIME);
    }

    /**
     * 重置连接池,用以改变并发能力
     *
     * @param core
     * @param max
     */
    public void initPool(int core, int max) {
        executorService = ThreadPoolUtil.createPool(core, max, HttpClientConstant.THREAD_ALIVE_TIME);
    }

    /**
     * 执行多线程任务
     * 默认取list中thread对象,丢入线程池,完成多线程执行,如果没有threadname,name默认采用desc+线程数作为threadname,去除末尾的日期
     */
    public PerformanceResultBean start() {
        key = false;
        Progress progress = new Progress(threads.get(0), desc.replaceAll("\\d{14}$", EMPTY));
        new Thread(progress).start();
        boolean isTimesMode = baseThread.isTimesMode;
        int limit = baseThread.limit;
        int qps = baseThread.qps;
        long interval = 1_000_000_000 / qps;//此处单位1s=1000ms,1ms=1000000ns
        startTime = Time.getTimeStamp();
        AidThread aidThread = new AidThread();
        new Thread(aidThread).start();
        while (true) {
            executorService.execute(threads.get(limit-- % queueLength).clone());
            if (key ? true : isTimesMode ? limit < 1 : Time.getTimeStamp() - startTime > limit) break;
            sleep(interval);
        }
        endTime = Time.getTimeStamp();
        aidThread.stop();
        progress.stop();
        GCThread.stop();
        try {
            executorService.shutdown();
            executorService.awaitTermination(HttpClientConstant.WAIT_TERMINATION_TIMEOUT, TimeUnit.SECONDS);//此方法需要在shutdown方法执行之后执行
        } catch (InterruptedException e) {
            logger.error("线程池等待任务结束失败!", e);
        }
        logger.info("总计执行 {} ，共用时：{} s,执行总数:{},错误数:{}!", baseThread.isTimesMode ? baseThread.limit + "次任务" : "秒", Time.getTimeDiffer(startTime, endTime), executeTimes, errorTimes);
        return over();
    }

    private PerformanceResultBean over() {
        key = true;
        Save.saveLongList(allTimes, "data/" + desc + queueLength);
        Save.saveStringListSync(marks, MARK_Path.replace(LONG_Path, EMPTY) + desc);
        allTimes = new Vector<>();
        marks = new Vector<>();
        int executeNum = executeTimes.getAndSet(0);
        int errorNum = errorTimes.getAndSet(0);
        return countQPS(queueLength, desc, Time.getTimeByTimestamp(startTime), Time.getTimeByTimestamp(endTime), executeNum, errorNum);
    }

    /**
     * 计算结果
     * <p>此结果仅供参考</p>
     *由于fixQPS模型没有固定线程数,所以智能采取QPS=Q/T的计算,与concurrent有区别
     * @param name 线程数
     */
    public static PerformanceResultBean countQPS(int name, String desc, String start, String end, int executeNum, int errorNum) {
        List<String> strings = WriteRead.readTxtFileByLine(Constant.DATA_Path + desc + name);
        int size = strings.size();
        List<Integer> data = strings.stream().map(x -> changeStringToInt(x)).collect(toList());
        int sum = data.stream().mapToInt(x -> x).sum();
        String statistics = StatisticsUtil.statistics(data, desc, name);
        double qps = (executeNum * 1000_000 / (Time.getTimeStamp(end) - Time.getTimeStamp(start))) / 1000.0;
        return new PerformanceResultBean(desc, start, end, name, size, sum / size, qps, getPercent(executeNum, errorNum), 0, executeNum, statistics);
    }


    /**
     * 用于做后期的计算
     *
     * @param name
     * @param desc
     * @return
     */
    public PerformanceResultBean countQPS(int name, String desc) {
        return countQPS(name, desc, Time.getDate(), Time.getDate(), 0, 0);
    }

    /**
     * 后期计算用
     *
     * @param name
     * @return
     */
    public PerformanceResultBean countQPS(int name) {
        return countQPS(name, EMPTY, Time.getDate(), Time.getDate(), 0, 0);
    }


    /**
     * 补偿线程
     */
    class AidThread implements Runnable {

        private boolean key = true;

        int i;

        public AidThread() {

        }

        @Override
        public void run() {
            logger.info("补偿线程开始!");
            try {
                while (key) {
                    sleep(HttpClientConstant.LOOP_INTERVAL);
                    int actual = executeTimes.get();
                    int qps = baseThread.qps;
                    long expect = (Time.getTimeStamp() - FixedQpsConcurrent.this.startTime) / 1000 * qps;
                    if (expect > actual + qps) {
                        logger.info("期望执行数:{},实际执行数:{},设置QPS:{}", expect, actual, qps);
                        range((int) expect - actual).forEach(x -> {
                            sleep(100);
                            if (!executorService.isShutdown())
                                executorService.execute(threads.get(this.i++ % queueLength).clone());
                        });
                    }
                }
                logger.info("补偿线程结束!");
            } catch (Exception e) {
                logger.error("补偿线程发生错误!", e);
            }
        }

        public void stop() {
            key = false;
        }


    }


}