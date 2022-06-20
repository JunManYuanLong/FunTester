package com.funtester.frame.execute


import com.funtester.config.Constant
import com.funtester.frame.Output
import com.funtester.frame.SourceCode
import com.funtester.utils.StringUtil
import com.funtester.utils.Time
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.LongAdder

/**
 * Java线程池Demo
 */
class ThreadPoolUtil extends Constant {

    private static final Logger logger = LogManager.getLogger(ThreadPoolUtil.class);

    private static AtomicInteger threadNum = new AtomicInteger(1)


    private static volatile ExecutorService funPool;

    /**
     * 异步执行任务
     * @param runnable
     */
    static void executeSync(Runnable runnable) {
        getFunPool().execute(runnable)
    }

    /**
     * 重建可变线程池
     * corePoolSize：核心池的大小，这个参数跟后面讲述的线程池的实现原理有非常大的关系。在创建了线程池后，默认情况下，线程池中并没有任何线程，而是等待有任务到来才创建线程去执行任务，除非调用了prestartAllCoreThreads()或者prestartCoreThread()方法，从这2个方法的名字就可以看出，是预创建线程的意思，即在没有任务到来之前就创建corePoolSize个线程或者一个线程。默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，就会创建一个线程去执行任务，当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；
     * maximumPoolSize：线程池最大线程数，这个参数也是一个非常重要的参数，它表示在线程池中最多能创建多少个线程；
     * keepAliveTime：表示线程没有任务执行时最多保持多久时间会终止。默认情况下，只有当线程池中的线程数大于corePoolSize时，keepAliveTime才会起作用，直到线程池中的线程数不大于corePoolSize，即当线程池中的线程数大于corePoolSize时，如果一个线程空闲的时间达到keepAliveTime，则会终止，直到线程池中的线程数不超过corePoolSize。但是如果调用了allowCoreThreadTimeOut(boolean)方法，在线程池中的线程数不大于corePoolSize时，keepAliveTime参数也会起作用，直到线程池中的线程数为0；
     * unit：参数keepAliveTime的时间单位，有7种取值，在TimeUnit类中有7种静态属性：
     * workQueue：一个阻塞队列，用来存储等待执行的任务，这个参数的选择也很重要，会对线程池的运行过程产生重大影响，一般来说，这里的阻塞队列有以下几种选择：ArrayBlockingQueue;LinkedBlockingQueue; SynchronousQueue;
     * 　　ArrayBlockingQueue和PriorityBlockingQueue使用较少，一般使用LinkedBlockingQueue和Synchronous。线程池的排队策略与BlockingQueue有关。
     * threadFactory：线程工厂，主要用来创建线程；
     * handler：表示当拒绝处理任务时的策略，有以下四种取值：
     * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
     * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
     * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
     * ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
     * 当workqueue满了之后,线程池会创建新的
     * @param core 核心线程数
     * @param max 最大线程数
     * @param liveTime 空闲时间
     * @return
     */
    static ThreadPoolExecutor createPool(int core = Constant.THREADPOOL_CORE, int max = Constant.THREADPOOL_MAX, int liveTime = Constant.ALIVE_TIME, BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(Constant.MAX_WAIT_TASK), ThreadFactory factory = getFactory(), RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy()) {
        return new ThreadPoolExecutor(core, max, liveTime, TimeUnit.SECONDS, workQueue, factory, rejectedExecutionHandler);
    }

    /**
     * 获取QPS模型的
     * @param name
     * @return
     */
    static ThreadPoolExecutor createQpsPool(String name) {
        createPool(LUCKY_NUM + 1, THREADPOOL_MAX, ALIVE_TIME, new LinkedBlockingQueue<Runnable>(LUCKY_NUM + COUNT_INTERVAL), ThreadPoolUtil.getFactory(name))
    }

    /**
     * 定长的线程池
     *
     * @param size
     * @return
     */
    static ThreadPoolExecutor createFixedPool(int size = 10, String name = "P") {
        return createPool(size, size, Constant.ALIVE_TIME, new LinkedBlockingQueue<Runnable>(Constant.MAX_WAIT_TASK), getFactory(name));
    }

    /**
     * 缓存线程池,默认最大长度256
     * {@link java.util.concurrent.SynchronousQueue}写入操作等待拉取操作.实际容量为0的队列
     * @return
     */
    static ThreadPoolExecutor createCachePool(int max = 256, String name = "Cache") {
        return createPool(0, max, Constant.ALIVE_TIME, new SynchronousQueue<Runnable>(), getFactory(name))
    }

    /**
     * 获取异步任务连接池
     * @return
     */
    static ThreadPoolExecutor getFunPool() {
        if (funPool == null) {
            synchronized (ThreadPoolUtil.class) {
                if (funPool == null) {
                    funPool = createFixedPool(Constant.POOL_SIZE, "F");
                    daemon()
                }
            }
        }
        return funPool
    }

    /**
     * 自定义{@link ThreadFactory}对象
     * @return
     */
    static ThreadFactory getFactory(String name = "F") {
        return new ThreadFactory() {

            int num = 1

            @Override
            Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName(name + "-" + StringUtil.right(Constant.EMPTY + num++, 2));
                return thread;
            }
        }
    }

    /**
     * 在QPS模型中执行QPS
     * @param executor
     * @param qps 每秒的QPS
     * @param produce
     * @param total
     */
    static void executeTask(ThreadPoolExecutor executor, int qps, Closure produce, LongAdder total) {
        SourceCode.sleep(1.0)
        if (qps < 1) return
        if (qps > LUCKY_NUM * TIMES_PER_TASK) LUCKY_NUM = qps / TIMES_PER_TASK as Integer
        ThreadPoolUtil.executeSync {
            LUCKY_NUM.times {
                executor.execute(new Runnable() {

                    @Override
                    void run() {
                        (qps / LUCKY_NUM).times {
                            produce()
                            total.increment()
                        }
                    }
                })

            }
            executor.execute(new Runnable() {

                @Override
                void run() {
                    (qps % LUCKY_NUM).times {
                        produce()
                        total.increment()
                    }
                }
            })
        }
        if (Time.getSecond() % COUNT_INTERVAL == 0) {
            int real = total.sumThenReset() / COUNT_INTERVAL as int
            def active = executor.getActiveCount()
            def count = active == 0 ? 1 : active
            logger.info("当前设计QPS:{},实际QPS:{} 活跃线程数:{} 单线程效率:{}", qps, real, active, real / count as int)
        }
    }

    /**
     * 关闭异步线程池,不然会停不下来
     */
    static void shutFun() {
        if (getFunPool().isShutdown()) return
        logger.warn(Output.rgb("异步线程池关闭!"))
        getFunPool().shutdown()
    }


    /**
     * 执行daemon线程,保障main方法结束后关闭线程池
     * @return
     */
    static boolean daemon() {
        def thread = new Thread(new Runnable() {

            @Override
            void run() {
                logger.info(Output.rgb("守护线程开启!"))
                while (checkMain()) {
                    SourceCode.sleep(1.0)
                }
                waitFunIdle()
                ThreadPoolUtil.shutFun()
            }
        })
        thread.setDaemon(true)
        thread.setName("Deamon")
        thread.start()
    }

    /**
     * 检查main线程是否存活
     * @return
     */
    static boolean checkMain() {
        def count = Thread.activeCount()
        def group = Thread.currentThread().getThreadGroup()
        def threads = new Thread[count]
        group.enumerate(threads)
        for (i in 0..<count) {
            def thread = threads[i]
            if (thread != null && thread.getName() == "main")
                return true
        }
        false
    }

    /**
     * 等待异步线程池空闲
     */
    static void waitFunIdle() {
        if (funPool == null) return
        SourceCode.time({
            SourceCode.waitFor {
                funPool.getQueue().size() == 0 && funPool.getActiveCount() == 0
            }
        }, 1, "异步线程池等待")
    }

    /**
     * 保留方法,备用
     */
    static void getAllThread() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup topGroup = group;
        // 遍历线程组树，获取根线程组
        while (group != null) {
            topGroup = group;
            group = group.getParent();
        }
        // 激活的线程数再加一倍，防止枚举时有可能刚好有动态线程生成
        int slackSize = topGroup.activeCount() * 2;
        Thread[] slackThreads = new Thread[slackSize];
        // 获取根线程组下的所有线程，返回的actualSize便是最终的线程数
        int actualSize = topGroup.enumerate(slackThreads);
        Thread[] atualThreads = new Thread[actualSize];
        // 复制slackThreads中有效的值到atualThreads
        System.arraycopy(slackThreads, 0, atualThreads, 0, actualSize);
        System.out.println("Threads size is " + atualThreads.length);
        for (Thread thread : atualThreads) {
            System.out.println("Thread name : " + thread.getName());
        }
    }

}
