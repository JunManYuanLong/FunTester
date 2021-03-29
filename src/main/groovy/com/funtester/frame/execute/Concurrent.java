package com.funtester.frame.execute;

import com.funtester.base.bean.PerformanceResultBean;
import com.funtester.base.constaint.ThreadBase;
import com.funtester.config.Constant;
import com.funtester.frame.Save;
import com.funtester.frame.SourceCode;
import com.funtester.utils.Time;
import com.funtester.utils.RWUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import static java.util.stream.Collectors.toList;

/**
 * 并发类，用于启动压力脚本
 */
public class Concurrent extends SourceCode {

    private static Logger logger = LogManager.getLogger(Concurrent.class);

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 结束时间
     */
    private long endTime;

    /**
     * 任务描述
     */
    public String desc;

    /**
     * 任务集
     */
    public List<ThreadBase> threads = new ArrayList<>();

    /**
     * 线程数
     */
    public int threadNum;

    /**
     * 执行失败总数
     */
    private int errorTotal;

    /**
     * 任务执行失败总数
     */
    private int failTotal;

    /**
     * 执行总数
     */
    private int executeTotal;

    /**
     * 用于记录所有请求时间
     */
    public static Vector<Integer> allTimes = new Vector<>();

    /**
     * 记录所有markrequest的信息
     */
    public static Vector<String> requestMark = new Vector<>();

    /**
     * 线程池
     */
    ExecutorService executorService;

    /**
     * 计数器
     */
    CountDownLatch countDownLatch;

    /**
     * @param thread    线程任务
     * @param threadNum 线程数
     * @param desc      任务描述
     */
    public Concurrent(ThreadBase thread, int threadNum, String desc) {
        this(threadNum, desc);
        range(threadNum).forEach(x -> threads.add(thread.clone()));
    }

    /**
     * @param threads 线程组
     * @param desc    任务描述
     */
    public Concurrent(List<ThreadBase> threads, String desc) {
        this(threads.size(), desc);
        this.threads = threads;
    }

    private Concurrent(int threadNum, String desc) {
        this.threadNum = threadNum;
        this.desc = StatisticsUtil.getFileName(desc);
        executorService = ThreadPoolUtil.createFixedPool(threadNum);
        countDownLatch = new CountDownLatch(threadNum);
    }

    private Concurrent() {

    }

    /**
     * 执行多线程任务
     * 默认取list中thread对象,丢入线程池,完成多线程执行,如果没有threadname,name默认采用desc+线程数作为threadname,去除末尾的日期
     */
    public PerformanceResultBean start() {
        Progress progress = new Progress(threads, StatisticsUtil.getTrueName(desc));
        new Thread(progress).start();
        startTime = Time.getTimeStamp();
        for (int i = 0; i < threadNum; i++) {
            ThreadBase thread = threads.get(i);
            if (StringUtils.isBlank(thread.threadName)) thread.threadName = StatisticsUtil.getTrueName(desc) + i;
            thread.setCountDownLatch(countDownLatch);
            executorService.execute(thread);
        }
        shutdownService(executorService, countDownLatch);
        endTime = Time.getTimeStamp();
        progress.stop();
        threads.forEach(x -> {
            if (x.status()) failTotal++;
            errorTotal += x.errorNum;
            executeTotal += x.executeNum;
        });
        logger.info("总计{}个线程，共用时：{} s,执行总数:{},错误数:{},失败数:{}", threadNum, Time.getTimeDiffer(startTime, endTime), executeTotal, errorTotal, failTotal);
        return over();
    }

    /**
     * 关闭任务相关资源
     *
     * @param executorService 线程池
     * @param countDownLatch  计数器
     */
    private static void shutdownService(ExecutorService executorService, CountDownLatch countDownLatch) {
        try {
            countDownLatch.await();
            executorService.shutdown();
        } catch (InterruptedException e) {
            logger.warn("线程池关闭失败！", e);
        }
    }

    private PerformanceResultBean over() {
        Save.saveIntegerList(allTimes, DATA_Path.replace(LONG_Path, EMPTY) + StatisticsUtil.getFileName(threadNum, desc));
        Save.saveStringListSync(Concurrent.requestMark, MARK_Path.replace(LONG_Path, EMPTY) + desc);
        allTimes = new Vector<>();
        requestMark = new Vector<>();
        return countQPS(threadNum, desc, Time.getTimeByTimestamp(startTime), Time.getTimeByTimestamp(endTime));
    }


    /**
     * 计算结果
     * <p>此结果仅供参考</p>
     * 此处因为start和end的不准确问题,所以采用改计算方法,与fixQPS有区别
     *
     * @param name 线程数
     */
    public PerformanceResultBean countQPS(int name, String desc, String start, String end) {
        List<String> strings = RWUtil.readTxtFileByLine(Constant.DATA_Path + StatisticsUtil.getFileName(name, desc));
        int size = strings.size();
        List<Integer> data = strings.stream().map(x -> changeStringToInt(x)).collect(toList());
        int sum = data.stream().mapToInt(x -> x).sum();
        String statistics = StatisticsUtil.statistics(data, desc, threadNum);
        int rt = sum / size;
        double qps = 1000.0 * name / rt;
        double qps2 = (executeTotal + errorTotal) * 1000.0 / (endTime - startTime);
        return new PerformanceResultBean(desc, start, end, name, size, rt, qps, qps2, getPercent(executeTotal, errorTotal), getPercent(threadNum, failTotal), executeTotal, statistics);
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


}