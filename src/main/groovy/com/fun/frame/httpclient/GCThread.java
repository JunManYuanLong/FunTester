package com.fun.frame.httpclient;

import com.fun.frame.SourceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 从连接池中回收连接的多线程类
 */
public class GCThread extends SourceCode implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(GCThread.class);

    /**
     * 资源回收线程
     */
    private static volatile Thread gc = init();

    /**
     * 增加了线程状态的判断,同一进程多次运行HTTP请求的压测功能
     */
    public synchronized static void starts() {
        if (gc.getState() == Thread.State.NEW) gc.start();
        else if (gc.getState() == Thread.State.TERMINATED) gc = init();
    }

    /**
     * 初始化方法,获取新的gc线程对象
     *
     * @return
     */
    public static synchronized Thread init() {
        FLAG = true;
        return new Thread(new GCThread());
    }

    private GCThread() {
    }

    /**
     * 线程结束标志
     */
    private static boolean FLAG = true;

    @Override
    public void run() {
        logger.info("gc回收线程开始了！");
        while (FLAG) {
            sleep(3);
            ClientManage.recyclingConnection();
        }
        logger.info("gc回收线程结束了！");
    }

    /**
     * 结束线程方法
     */
    public static void stop() {
        FLAG = false;
    }


}