package com.funtester.frame.execute;

import com.funtester.base.bean.PerformanceResultBean;
import com.funtester.base.constaint.ThreadBase;
import com.funtester.config.Constant;
import com.funtester.frame.Save;
import com.funtester.frame.SourceCode;
import com.funtester.utils.CountUtil;
import com.funtester.utils.RWUtil;
import com.funtester.utils.Time;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

/**
 * 并发类，用于启动压力脚本
 */
public class HoldConcurrent extends SourceCode {

    private static Logger logger = LogManager.getLogger(HoldConcurrent.class);

    /**
     * 用来标记状态
     */
    public static AtomicInteger HOLD = new AtomicInteger(0);

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * `
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
     * 执行总数
     */
    private int executeTotal;

    /**
     * 用于记录所有请求时间
     */
    public static Vector<Short> allTimes = new Vector<>();

    /**
     * 记录所有markrequest的信息
     */
    public static Vector<String> requestMark = new Vector<>();

    /**
     * 线程池
     */
    ExecutorService executorService;


    /**
     * 多线程多阶段同步类,用于多线程任务阶段管理
     */
    public static Phaser phaser;

    /**
     * @param thread    线程任务
     * @param threadNum 线程数
     * @param desc      任务描述
     */
    public HoldConcurrent(ThreadBase thread, int threadNum, String desc) {
        this(threadNum, desc);
        range(threadNum).forEach(x -> threads.add(thread.clone()));
    }

    /**
     * @param threads 线程组
     * @param desc    任务描述
     */
    public HoldConcurrent(List<ThreadBase> threads, String desc) {
        this(threads.size(), desc);
        this.threads = threads;
    }

    private HoldConcurrent(int threadNum, String desc) {
        this.threadNum = threadNum;
        this.desc = StatisticsUtil.getFileName(desc);
        phaser = new Phaser(1);
        executorService = ThreadPoolUtil.createFixedPool(threadNum, "Hold");
    }

    private HoldConcurrent() {

    }

    /**
     * 执行多线程任务
     * 默认取list中thread对象,丢入线程池,完成多线程执行,如果没有threadname,name默认采用desc+线程数作为threadname,去除末尾的日期
     */
    public PerformanceResultBean start() {
        Thread funtester = new Thread(new FunTester());
        funtester.start();
        ThreadBase.progress = new Progress(threads, StatisticsUtil.getTrueName(desc));
        new Thread(ThreadBase.progress, "progress").start();
        startTime = Time.getTimeStamp();
        for (int i = 0; i < threadNum; i++) {
            if (HOLD.get() == 1) {
                threadNum = i;
                break;
            }
            ThreadBase thread = threads.get(i);
            if (StringUtils.isBlank(thread.threadName)) thread.threadName = StatisticsUtil.getTrueName(desc) + i;
            sleep(RUNUP_TIME / threadNum);
            executorService.execute(thread);
            logger.info("已经启动了 {} 个线程!", i + 1);
        }
        phaser.arriveAndAwaitAdvance();
        executorService.shutdown();
        ThreadBase.progress.stop();
        threads.forEach(x -> {
            errorTotal += x.errorNum;
            executeTotal += x.executeNum;
        });
        endTime = Time.getTimeStamp();
        HOLD.set(0);
        logger.info("总计{}个线程，共用时：{} s,执行总数:{},错误数:{}", threadNum, Time.getTimeDiffer(startTime, endTime), formatLong(executeTotal), errorTotal);
        return over();
    }

    private static class FunTester implements Runnable {

        @Override
        public void run() {
            waitForKey(INTPUT_KEY);
            HOLD.set(1);
            output("压力暂停");
        }

    }

    private PerformanceResultBean over() {
        Save.saveIntegerList(allTimes, DATA_Path.replace(LONG_Path, EMPTY) + StatisticsUtil.getFileName(threadNum, desc));
        Save.saveStringListSync(HoldConcurrent.requestMark, MARK_Path.replace(LONG_Path, EMPTY) + desc);
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
        List<String> strings = RWUtil.readByLine(Constant.DATA_Path + StatisticsUtil.getFileName(name, desc));
        int size = strings.size() == 0 ? 1 : strings.size();
        List<Integer> data = strings.stream().map(x -> changeStringToInt(x)).collect(toList());
        int sum = data.stream().mapToInt(x -> x).sum();
        String statistics = StatisticsUtil.statistics(data, desc, threadNum);
        int rt = sum / size;
        double qps = 1000.0 * name / (rt == 0 ? 1 : rt);
        double qps2 = (executeTotal + errorTotal) * 1000.0 / (endTime - startTime);
        return new PerformanceResultBean(desc, start, end, name, size, rt, qps, qps2, getPercent(executeTotal, errorTotal), executeTotal, statistics, CountUtil.index(data).toString());
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