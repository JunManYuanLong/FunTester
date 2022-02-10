package com.funtester.frame.execute;

import com.funtester.base.constaint.FunThread;
import com.funtester.base.interfaces.IFunController;
import com.funtester.config.Constant;
import com.funtester.frame.SourceCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
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
    public static ExecutorService executorService;

    public static IFunController controller;

    /**
     * @param threads 线程组
     */
    public FunConcurrent(List<FunThread> threads) {
        this.threads = threads;
        executorService = ThreadPoolUtil.createCachePool(Constant.THREADPOOL_MAX);
    }

    private FunConcurrent() {

    }

    /**
     * 执行多线程任务
     * 默认取list中thread对象,丢入线程池,完成多线程执行,如果没有threadname,name默认采用desc+线程数作为threadname,去除末尾的日期
     */
    public void start() {
        if (controller == null) controller = new FunTester();
        new Thread(controller, "接收器").start();
        threads.forEach(f -> addTask(f));
    }

    public static void addTask(FunThread thread) {
        boolean b = FunThread.addThread(thread);
        logger.info("任务{}添加{}", thread.threadName, b ? "成功" : "失败");
        if (b) executorService.execute(thread);
    }

    public static void addTask() {
        FunThread thread = FunThread.getRandom();
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
        FunThread thread = FunThread.getRandom();
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
                        break;
                }
            }
        }

        @Override
        public void add() {
            addTask();
        }

        @Override
        public void reduce() {
            removeTask();
        }

        @Override
        public void over() {
            logger.info("动态结束任务!");
            FunThread.stop();
        }

    }


}