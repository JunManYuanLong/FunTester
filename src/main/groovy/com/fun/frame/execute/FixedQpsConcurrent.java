package com.fun.frame.execute;

import com.fun.base.bean.PerformanceResultBean;
import com.fun.base.constaint.FixedQpsThread;
import com.fun.config.Constant;
import com.fun.frame.Save;
import com.fun.frame.SourceCode;
import com.fun.frame.httpclient.GCThread;
import com.fun.utils.Time;
import com.fun.utils.WriteRead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
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

    private static Logger logger = LoggerFactory.getLogger(FixedQpsConcurrent.class);

    public static boolean key = false;

    public static AtomicInteger executeTimes = new AtomicInteger();

    public static AtomicInteger errorTimes = new AtomicInteger();

    public static Vector<String> marks = new Vector<>();

    /**
     * 用于记录所有请求时间
     */
    public static Vector<Long> allTimes = new Vector<>();

    /**
     * 开始时间
     */
    public long startTime;

    /**
     * 结束时间
     */
    public long endTime;

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
        this.desc = desc + Time.getNow();
    }

    /**
     * @param threads 线程组
     * @param desc    任务描述
     */
    public FixedQpsConcurrent(List<FixedQpsThread> threads, String desc) {
        this();
        this.threads = threads;
        this.queueLength = threads.size();
        this.desc = desc + Time.getNow();
    }

    private FixedQpsConcurrent() {
        executorService = ThreadPoolUtil.createPool(20, 200, 3);
    }

    /**
     * 执行多线程任务
     * 默认取list中thread对象,丢入线程池,完成多线程执行,如果没有threadname,name默认采用desc+线程数作为threadname,去除末尾的日期
     */
    public PerformanceResultBean start() {
        key = false;
        FixedQpsThread fixedQpsThread = threads.get(0);
        boolean isTimesMode = fixedQpsThread.isTimesMode;
        int limit = fixedQpsThread.limit;
        int qps = fixedQpsThread.qps;
        long interval = 1_000_000_000 / qps;
        AidThread aidThread = new AidThread();
        new Thread(aidThread).start();
        startTime = Time.getTimeStamp();
        while (true) {
            executorService.execute(threads.get(limit-- % queueLength).clone());
            if (key ? true : isTimesMode ? limit < 1 : Time.getTimeStamp() - startTime > fixedQpsThread.limit) break;
            sleep(interval);
        }
        endTime = Time.getTimeStamp();
        aidThread.stop();
        GCThread.stop();
        try {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);//此方法需要在shutdown方法执行之后执行
        } catch (InterruptedException e) {
            logger.error("线程池等待任务结束失败!", e);
        }
        logger.info("总计执行 {} ，共用时：{} s,执行总数:{},错误数:{}!", fixedQpsThread.isTimesMode ? fixedQpsThread.limit + "次任务" : "秒", Time.getTimeDiffer(startTime, endTime), executeTimes, errorTimes);
        return over();
    }

    private PerformanceResultBean over() {
        key = true;
        Save.saveLongList(allTimes, "data/" + queueLength + desc);
        Save.saveStringListSync(marks, MARK_Path.replace(LONG_Path, EMPTY) + desc);
        allTimes = new Vector<>();
        marks = new Vector<>();
        executeTimes.set(0);
        errorTimes.set(0);
        return countQPS(queueLength, desc, Time.getTimeByTimestamp(startTime), Time.getTimeByTimestamp(endTime));
    }

    /**
     * 计算结果
     * <p>此结果仅供参考</p>
     *
     * @param name 线程数
     */
    public PerformanceResultBean countQPS(int name, String desc, String start, String end) {
        List<String> strings = WriteRead.readTxtFileByLine(Constant.DATA_Path + name + desc);
        int size = strings.size();
        List<Integer> data = strings.stream().map(x -> changeStringToInt(x)).collect(toList());
        int sum = data.stream().mapToInt(x -> x).sum();
        Collections.sort(data);
        String statistics = StatisticsUtil.statistics(data, desc, this.queueLength);
        double qps = this.threads.get(0).qps;
        return new PerformanceResultBean(desc, start, end, name, size, sum / size, qps, getPercent(executeTimes.get(), errorTimes.get()), 0, executeTimes.get(), statistics);
    }


    /**
     * 用于做后期的计算
     *
     * @param name
     * @param desc
     * @return
     */
    public PerformanceResultBean countQPS(int name, String desc) {
        return countQPS(name, desc, Time.getDate(), Time.getDate());
    }

    /**
     * 后期计算用
     *
     * @param name
     * @return
     */
    public PerformanceResultBean countQPS(int name) {
        return countQPS(name, EMPTY, Time.getDate(), Time.getDate());
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
            while (key) {
                long expect = (Time.getTimeStamp() - startTime) / 1000 * threads.get(0).qps;
                logger.info("期望执行数:{},实际执行数:{},设置QPS:{}", expect, executeTimes.get(), threads.get(0).qps);
                if (expect > executeTimes.get() + 10) {
                    range((int) expect - executeTimes.get()).forEach(x -> {
                        sleep(100);
                        executorService.execute(threads.get(i++ % queueLength).clone());
                    });
                }
                sleep(3);
            }
            logger.info("补偿线程结束!");
        }

        public void stop() {
            key = false;
        }


    }


}