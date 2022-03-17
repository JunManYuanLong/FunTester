package com.funtester.frame.execute

import com.funtester.base.constaint.FunThread
import com.funtester.base.event.FunCount
import com.funtester.base.interfaces.IFunController
import com.funtester.frame.SourceCode
import com.funtester.utils.Regex
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.atomic.LongAdder

class FunEventConcurrent extends SourceCode {

    private static final Logger logger = LogManager.getLogger(FunEventConcurrent.class);

    static ThreadPoolExecutor executor

    public static IFunController controller;

    LongAdder total = new LongAdder()

    boolean key = true

    FunCount funcount

    Closure produce

    FunEventConcurrent(Closure closure, FunCount funCount = new FunCount(1, 0, 1, Integer.MAX_VALUE, MAX_QPS, "QPS模型计数器")) {
        this.funcount = funCount
        this.produce = closure
        start()
    }

    void start() {
        if (executor == null) executor = ThreadPoolUtil.createPool()
        if (controller == null) controller = new FunTester();
        new Thread(controller, "接收器").start();
        funcount.start()
        def index = 0
        while (key) {
            def qps = funcount.getQps()
            if (qps == 0) continue
            if (qps < 0) break
            fun {
                qps.times {
                    if (key) {
                        executor.execute(new Runnable() {

                            @Override
                            void run() {
                                produce()
                                total.increment()
                            }
                        })
                    }
                }
            }
            sleep(1.0)
            if (index++ % LOOP_INTERVAL == 0) logger.info("当前设计QPS:{},实际QPS:{} 活跃线程数:{}", qps, total.sumThenReset() / LOOP_INTERVAL as int, executor.getActiveCount())
        }
        stop()
    }


    /**
     * 中止
     * @return
     */
    def stop() {
        key = false
        funcount.stop()
        executor.shutdown()
        logger.info("FunEvent压测关闭了!")
    }

    private class FunTester implements IFunController {

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
                        if (Regex.isMatch(input, "F\\d+")) QPS_STEP = changeStringToInt(input.substring(1));
                        break;
                }
            }
        }

        @Override
        public void add() {
            funcount.add(QPS_STEP)
        }

        @Override
        public void reduce() {
            funcount.add(-QPS_STEP)
        }

        @Override
        public void over() {
            logger.info("动态结束任务!");
            FunThread.stop();
        }

    }

}
