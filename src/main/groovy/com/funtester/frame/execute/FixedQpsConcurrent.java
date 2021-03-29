package com.funtester.frame.execute;

import com.funtester.base.bean.PerformanceResultBean;
import com.funtester.base.constaint.FixedQpsThread;
import com.funtester.config.Constant;
import com.funtester.config.HttpClientConstant;
import com.funtester.frame.Save;
import com.funtester.frame.SourceCode;
import com.funtester.httpclient.GCThread;
import com.funtester.utils.Time;
import com.funtester.utils.RWUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class FixedQpsConcurrent extends SourceCode {

    private static Logger logger = LogManager.getLogger(FixedQpsConcurrent.class);

    public static boolean key = false;

    public static AtomicInteger executeTimes = new AtomicInteger(0);

    public static AtomicInteger errorTimes = new AtomicInteger(0);

    public static Vector<String> marks = new Vector<>();

    /**
     * 基础任务对象
     */
    public FixedQpsThread baseThread;

    /**
     * 用于记录所有请求时间
     */
    public static Vector<Integer> allTimes = new Vector<>();

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
    public String desc;

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
     * @param desc   任务描述
     */
    public FixedQpsConcurrent(FixedQpsThread thread, String desc) {
        this(desc);
        this.queueLength = 1;
        threads.add(thread);
        baseThread = thread;
    }

    /**
     * @param threads 线程组
     * @param desc    任务描述
     */
    public FixedQpsConcurrent(List<FixedQpsThread> threads, String desc) {
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
        executorService = ThreadPoolUtil.createPool(HttpClientConstant.THREADPOOL_CORE, HttpClientConstant.THREADPOOL_MAX, HttpClientConstant.THREAD_ALIVE_TIME);
    }

    private FixedQpsConcurrent() {

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
        Progress progress = new Progress(threads, StatisticsUtil.getTrueName(desc), executeTimes);
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
        List<String> strings = RWUtil.readTxtFileByLine(Constant.DATA_Path + StatisticsUtil.getFileName(name, desc));
        int size = strings.size();
        List<Integer> data = strings.stream().map(x -> changeStringToInt(x)).collect(toList());
        int sum = data.stream().mapToInt(x -> x).sum();
        String statistics = StatisticsUtil.statistics(data, desc, name);
        double qps = executeNum * 1000.0 / (end - start);
        int qps2 = baseThread.qps;
        return new PerformanceResultBean(desc, Time.getTimeByTimestamp(start), Time.getTimeByTimestamp(end), name, size, sum / size, qps, qps2, getPercent(executeNum, errorNum), 0, executeNum, statistics);
    }

    /**
     * 补偿线程,如果超过一半QPS量,才会进行补偿,补偿速率为每秒20个
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
                    long expect = (Time.getTimeStamp() - FixedQpsConcurrent.this.startTime) / 2000 * qps;
                    if (expect > actual + qps) {
                        logger.info("期望执行数:{},实际执行数:{},设置QPS:{}", expect, actual, qps);
                        range((int) expect - actual).forEach(x -> {
                            sleep(0.05);
                            if (!executorService.isShutdown()) {
                                executorService.execute(threads.get(this.i++ % queueLength).clone());
                            }
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