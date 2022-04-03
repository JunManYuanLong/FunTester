package com.funtester.frame.execute;

import com.funtester.base.constaint.FunThread;
import com.funtester.base.constaint.ThreadBase;
import com.funtester.base.interfaces.IFunController;
import com.funtester.config.Constant;
import com.funtester.frame.SourceCode;
import com.funtester.utils.Regex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 动态压测模型的启动类
 */
public class FunConcurrent extends SourceCode {

    private static Logger logger = LogManager.getLogger(FunConcurrent.class);

    /**
     * 任务集
     */
    public List<FunThread> threads = new ArrayList<>();

    /**
     * 线程池
     */
    public static ExecutorService executor;

    public static IFunController controller;

    /**
     * @param threads 线程组
     */
    public FunConcurrent(List<FunThread> threads, String name) {
        this.threads.addAll(threads);
        ThreadBase.progress = new Progress(threads, name);
    }

    public FunConcurrent(FunThread thread, String name) {
        this(Arrays.asList(thread), name);
    }

    private FunConcurrent() {

    }

    /**
     * 执行多线程任务
     * 默认取list中thread对象,丢入线程池,完成多线程执行,如果没有threadname,name默认采用desc+线程数作为threadname,去除末尾的日期
     */
    public void start() {
        if (executor == null) executor = ThreadPoolUtil.createCachePool(Constant.THREADPOOL_MAX);
        if (controller == null) controller = new FunTester();
        new Thread(controller, "接收器").start();
        threads.forEach(f -> addTask(f));
    }

    /**
     * 向动态模型中添加任务
     *
     * @param thread
     */
    public static void addTask(FunThread thread) {
        boolean b = FunThread.addThread(thread);
        logger.info("任务{}添加{}", thread.threadName, b ? "成功" : "失败");
        if (b) executor.execute(thread);
    }

    /**
     * 添加任务,默认随机现有任务
     */
    public static void addTask() {
        FunThread thread = FunThread.getRandomThread();
        addTask(thread.clone());
    }

    public static void removeTask(FunThread thread) {
        logger.info("任务{}被终止", thread.threadName);
        FunThread.remoreThread(thread);
    }

    public static void removeTask(String name) {
        logger.info("任务{}被终止", name);
        FunThread.remoreThread(name);
    }

    public static void removeTask() {
        FunThread thread = FunThread.getRandomThread();
        removeTask(thread);
    }

    private static class FunTester implements IFunController {

        boolean key = true;

        @Override
        public void run() {
            while (key) {
                String input = getInput();
                switch (input) {
                    case "+":
                        add();
                        break;
                    case "-":
                        reduce();
                        break;
                    case "*":
                        over();
                        key = false;
                        break;
                    default:
                        if (Regex.isMatch(input, "(F|f)\\d+")) THREAD_STEP = changeStringToInt(input.substring(1));
                        break;
                }
                FunThread.printInfo();
            }
        }

        @Override
        public void add() {
            range(THREAD_STEP).forEach(f -> {
                addTask();
                sleep(1.0);
            });
        }

        @Override
        public void reduce() {
            range(THREAD_STEP).forEach(f -> {
                removeTask();
                sleep(1.0);
            });
        }

        @Override
        public void over() {
            FunThread.stop();
            logger.info("动态结束任务!");
        }

    }


}