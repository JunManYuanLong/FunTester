package com.funtester.frame.execute


import com.funtester.base.interfaces.IFunController
import com.funtester.config.Constant
import com.funtester.frame.SourceCode
import com.funtester.utils.Regex
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.atomic.LongAdder

/**
 * 动态压测模型的启动类
 */
class FunQpsConcurrent extends SourceCode {

    private static Logger logger = LogManager.getLogger(FunQpsConcurrent.class);
    static ThreadPoolExecutor executor

    public static IFunController controller;

    LongAdder total = new LongAdder()

    public static boolean key = true

    Closure produce

    String name

    int qps = 1

    private FunQpsConcurrent() {

    }

    FunQpsConcurrent(Closure closure, def name = DEFAULT_STRING) {
        this.produce = closure
        this.name = name
    }

    void start() {
        if (executor == null) executor = ThreadPoolUtil.createCachePool(Constant.THREADPOOL_MAX, "Q")
        if (controller == null) controller = new FunTester();
        new Thread(controller, "接收器").start();
        while (key) {
            ThreadPoolUtil.executeTask(executor, qps, produce, total, name)
        }
        stop()
    }


    /**
     * 中止
     * @return
     */
    def stop() {
        key = false
        executor.shutdown()
        logger.info("FunQPS压测关闭了!")
    }

    private class FunTester implements IFunController {

        boolean inputKey = true;
        /**
         * 控制
         */
        boolean autoKey = false

        @Override
        public void run() {
            while (inputKey) {
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
                        break;
                    case "/":
                        autoKey = true
                        break;
                    default:
                        if (Regex.isMatch(input, "(F|f)\\d+")) QPS_STEP = changeStringToInt(input.substring(1));
                        if (Regex.isMatch(input, "(T|t)\\d+(D|d)\\d+")) {
                            def split = (input - "T" - "t").split(/(d|D)/)
                            auto(split[0] as int, split[1] as int)
                        }
                        break;
                }
            }
        }

        /**
         * 自动控制递增功能
         * @param target 目标QPS
         * @param duration 持续时间
         * @return
         */
        def auto(int target, duration) {
            fun {
                for (i in 0..<duration) {
                    if (autoKey) break
                    qps += (target - qps) / duration
                    sleep(1.0)
                }
                if (!autoKey) qps = target
                autoKey = false
            }
        }

        @Override
        public void add() {
            qps += QPS_STEP
        }

        @Override
        public void reduce() {
            qps -= QPS_STEP
            if (qps < 1) over()
        }

        @Override
        public void over() {
            inputKey = false
            key = false
            logger.info("动态结束任务!");
        }

    }

}