package com.funtester.frame.execute

import com.funtester.config.Constant
import com.funtester.config.HttpClientConstant
import com.funtester.frame.SourceCode
import com.funtester.utils.StringUtil
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Java线程池Demo
 */
class ThreadPoolUtil {

    private static final Logger logger = LogManager.getLogger(ThreadPoolUtil.class);

    private static AtomicInteger threadNum = new AtomicInteger(1)


    private static volatile ExecutorService funPool;

    private static volatile ThreadFactory FunFactory;


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
    static ThreadPoolExecutor createPool(int core = HttpClientConstant.THREADPOOL_CORE, int max = HttpClientConstant.THREADPOOL_MAX, int liveTime = HttpClientConstant.THREAD_ALIVE_TIME, BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(Constant.MAX_WAIT_TASK), ThreadFactory factory = getFactory(), RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy()) {
        return new ThreadPoolExecutor(core, max, liveTime, TimeUnit.SECONDS, workQueue, factory, rejectedExecutionHandler);
    }

    /**
     * 定长的线程池
     *
     * @param size
     * @return
     */
    static ExecutorService createFixedPool(int size = 10) {
        return createPool(size, size);
    }

    /**
     * 缓存线程池,默认最大长度256
     *
     * @return
     */
    static ExecutorService createCachePool() {
        return createPool(0, 256, 1);
    }

    /**
     * 获取异步任务连接池
     * @return
     */
    static ExecutorService getFunPool() {
        if (funPool == null) {
            synchronized (ThreadPoolUtil.class) {
                if (funPool == null) {
                    funPool = createFixedPool(Constant.POOL_SIZE);
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
    static ThreadFactory getFactory() {
        if (FunFactory == null) {
            synchronized (ThreadPoolUtil.class) {
                if (FunFactory == null) {
                    FunFactory = new ThreadFactory() {

                        @Override
                        Thread newThread(Runnable runnable) {
                            Thread thread = new Thread(runnable);
                            def increment = threadNum.getAndIncrement()
                            thread.setName("FT-" + StringUtil.right(Constant.EMPTY+increment, 3));
                            return thread;
                        }
                    }
                }
            }
        }
        return FunFactory
    }

    /**
     * 关闭异步线程池,不然会停不下来
     */
    static void shutFun() {
        if (getFunPool().isShutdown()) return
        logger.warn("异步线程池关闭!")
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
                SourceCode.sleep(3.0)
                while (checkMain()) {
                    SourceCode.sleep(1.0)
                }
                ThreadPoolUtil.shutFun()
            }
        })
        thread.setDaemon(true)
        thread.start()
        logger.info("守护线程开启!")
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
        threads.each {
            if (it.getName() == "main") return true
        }
        false
    }

}
