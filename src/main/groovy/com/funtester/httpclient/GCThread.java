package com.funtester.httpclient;

import com.funtester.config.HttpClientConstant;

import static com.funtester.frame.SourceCode.sleep;

/**
 * 从连接池中回收连接的多线程类
 */
public class GCThread implements Runnable {

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
        while (FLAG) {
            sleep(HttpClientConstant.LOOP_INTERVAL);
            ClientManage.recyclingConnection();
        }
    }

    /**
     * 结束线程方法
     */
    public static void stop() {
        FLAG = false;
    }


}