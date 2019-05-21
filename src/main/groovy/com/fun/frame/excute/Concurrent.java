package com.fun.frame.excute;

import com.fun.base.bean.PerformanceResultBean;
import com.fun.frame.Save;
import com.fun.frame.SourceCode;
import com.fun.frame.thead.ThreadBase;
import com.fun.config.Constant;
import com.fun.utils.Time;
import com.fun.utils.WriteRead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 并发类，用于启动压力脚本
 */
public class Concurrent {

    private static Logger logger = LoggerFactory.getLogger(Concurrent.class);

    /**
     * 线程任务
     */
    public ThreadBase thread;

    /**
     * 任务集
     */
    public List<ThreadBase> threads;

    /**
     * 线程数
     */
    public int num;

    public static Vector<Long> allTimes = new Vector<>();

    /**
     * 线程池
     */
    ExecutorService executorService;

    /**
     * 计数器
     */
    CountDownLatch countDownLatch;

    /**
     * @param thread 线程任务
     * @param num    线程数
     */
    public Concurrent(ThreadBase thread, int num) {
        this(num);
        this.thread = thread;
    }

    /**
     * @param threads 线程组
     */
    public Concurrent(List<ThreadBase> threads) {
        this(threads.size());
        this.threads = threads;
    }

    private Concurrent(int num) {
        this.num = num;
        executorService = Executors.newFixedThreadPool(num);
        countDownLatch = new CountDownLatch(num);
    }

    /**
     * 执行多线程任务
     */
    public PerformanceResultBean start() {
        long start = Time.getTimeStamp();
        for (int i = 0; i < num; i++) {
            ThreadBase thread = getThread(i);
            thread.setCountDownLatch(countDownLatch);
            executorService.execute(thread);
        }
        shutdownService(executorService, countDownLatch);
        long end = Time.getTimeStamp();
        logger.info("总计" + num + "个线程，共用时：" + Time.getTimeDiffer(start, end) + "秒！");
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
        Save.saveLongList(allTimes, num);
        return countQPS(num);
    }

    ThreadBase getThread(int i) {
        if (threads == null) return thread;
        return threads.get(i);
    }

    /**
     * 计算结果
     * <p>此结果仅供参考</p>
     *
     * @param name 线程数
     */
    public static PerformanceResultBean countQPS(int name) {
        List<String> strings = WriteRead.readTxtFileByLine(Constant.LONG_Path + name + Constant.FILE_TYPE_LOG);
        int size = strings.size();
        int sum = 0;
        for (int i = 0; i < size; i++) {
            int time = SourceCode.changeStringToInt(strings.get(i));
            sum += time;
        }
        double v = 1000.0 * size * name / sum;
        PerformanceResultBean performanceResultBean = new PerformanceResultBean(name, size, sum / size, v);
        performanceResultBean.print();
        return performanceResultBean;
    }
}