package com.fun.frame.excute;

import com.fun.base.bean.PerformanceResultBean;
import com.fun.base.constaint.ThreadBase;
import com.fun.config.Constant;
import com.fun.frame.Save;
import com.fun.frame.SourceCode;
import com.fun.utils.Time;
import com.fun.utils.WriteRead;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 并发类，用于启动压力脚本
 */
public class Concurrent extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(Concurrent.class);

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
    public String desc = "FunTester";

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
    private int excuteTotal;

    /**
     * 用于记录所有请求时间
     */
    public static Vector<Long> allTimes = new Vector<>();

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
     */
    public Concurrent(ThreadBase thread, int threadNum) {
        this(threadNum);
        range(threadNum).forEach(x -> threads.add(thread.clone()));
    }

    /**
     * @param threads 线程组
     */
    public Concurrent(List<ThreadBase> threads) {
        this(threads.size());
        this.threads = threads;
    }

    /**
     * @param thread    线程任务
     * @param threadNum 线程数
     * @param desc      任务描述
     */
    public Concurrent(ThreadBase thread, int threadNum, String desc) {
        this(thread, threadNum);
        this.desc = desc + Time.getNow();
    }

    /**
     * @param threads 线程组
     * @param desc    任务描述
     */
    public Concurrent(List<ThreadBase> threads, String desc) {
        this(threads);
        this.desc = desc + Time.getNow();
    }

    private Concurrent(int threadNum) {
        this.threadNum = threadNum;
        executorService = Executors.newFixedThreadPool(threadNum);
        countDownLatch = new CountDownLatch(threadNum);
    }

    private Concurrent() {

    }

    /**
     * 执行多线程任务
     */
    public PerformanceResultBean start() {
        startTime = Time.getTimeStamp();
        for (int i = 0; i < threadNum; i++) {
            ThreadBase thread = getThread(i);
            thread.setCountDownLatch(countDownLatch);
            executorService.execute(thread);
        }
        shutdownService(executorService, countDownLatch);
        endTime = Time.getTimeStamp();
        threads.forEach(x -> {
            if (x.status()) failTotal++;
            errorTotal += x.errorNum;
            excuteTotal += x.excuteNum;
        });
        logger.info("总计{}个线程，共用时：{} s,执行总数:{},错误数:{},失败数:{}", threadNum, Time.getTimeDiffer(startTime, endTime), excuteTotal, errorTotal, failTotal);
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
        Save.saveLongList(allTimes, threadNum + desc);
        Save.saveStringListSync(Concurrent.requestMark, MARK_Path.replace(LONG_Path, EMPTY) + desc);
        allTimes = new Vector<>();
        requestMark = new Vector<>();
        return countQPS(threadNum, desc, Time.getTimeByTimestamp(startTime), Time.getTimeByTimestamp(endTime));
    }

    ThreadBase getThread(int i) {
        return threads.get(i);
    }

    /**
     * 计算结果
     * <p>此结果仅供参考</p>
     *
     * @param name 线程数
     */
    public PerformanceResultBean countQPS(int name, String desc, String start, String end) {
        List<String> strings = WriteRead.readTxtFileByLine(Constant.LONG_Path + name + desc);
        int size = strings.size();
        List<Integer> data = strings.stream().map(x -> changeStringToInt(x)).collect(toList());
        int sum = data.stream().mapToInt(x -> x).sum();
        Collections.sort(data);
        String statistics = statistics(data, desc);
        double qps = 1000.0 * size * name / sum;
        return new PerformanceResultBean(desc, start, end, name, size, sum / size, qps, getPercent(excuteTotal, errorTotal), getPercent(threadNum, failTotal), excuteTotal, statistics);
    }

    /**
     * 将性能测试数据图表展示
     *
     * <p>
     * 将数据排序,然后按照循序分桶,选择桶中中位数作代码,通过二维数组转化成柱状图
     * </p>
     *
     * @param data 性能测试数据,也可以其他统计数据
     * @return
     */
    public static String statistics(List<Integer> data, String title) {
        int size = data.size();
        if (size < 1000) return EMPTY;
        int[] ints = range(1, BUCKET_SIZE + 1).map(x -> data.get(size * x / BUCKET_SIZE - size / BUCKET_SIZE / 2)).toArray();
        int largest = ints[BUCKET_SIZE - 1];
        String[][] map = Arrays.asList(ArrayUtils.toObject(ints)).stream().map(x -> getPercent(x, largest, BUCKET_SIZE)).collect(toList()).toArray(new String[BUCKET_SIZE][BUCKET_SIZE]);
        String[][] result = new String[BUCKET_SIZE][BUCKET_SIZE];
        /*将二维数组反转成竖排*/
        for (int i = 0; i < BUCKET_SIZE; i++) {
            for (int j = 0; j < BUCKET_SIZE; j++) {
                result[i][j] = getManyString(map[j][BUCKET_SIZE - 1 - i], 2) + SPACE_1;
            }
        }
        StringBuffer table = new StringBuffer(LINE + getManyString(TAB, 4) + ((title == null || title.length() == 0) ? DEFAULT_STRING : title) + LINE + LINE + TAB + ">>响应时间分布图,横轴排序分成桶的序号,纵轴每个桶的中位数<<" + LINE + TAB + TAB + "--<中位数数据最小值为:" + ints[0] + " ms,最大值:" + ints[BUCKET_SIZE - 1] + " ms>--" + LINE);
        for (int i = 0; i < BUCKET_SIZE; i++) {
            table.append(Arrays.asList(result[i]).stream().collect(Collectors.joining()) + LINE);
        }
        return table.toString();
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
     * 将数据转化成string数组
     *
     * @param part   数据
     * @param total  基准数据,默认最大的中位数
     * @param length
     * @return
     */
    public static String[] getPercent(int part, int total, int length) {
        int i = part * 8 * length / total;
        int prefix = i / 8;
        int suffix = i % 8;
        String s = getManyString(PERCENT[8], prefix) + (prefix == length ? EMPTY : PERCENT[suffix] + getManyString(SPACE_1, length - prefix - 1));
        return s.split(EMPTY);
    }


}