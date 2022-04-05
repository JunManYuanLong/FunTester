package com.funtester.frame.execute

import com.funtester.base.interfaces.IFunController
import com.funtester.frame.SourceCode
import com.funtester.utils.Regex
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.atomic.LongAdder

/**
 * 动态压测模型的启动类
 */
public class FunQpsConcurrent extends SourceCode {

    private static Logger logger = LogManager.getLogger(FunQpsConcurrent.class);

    static ThreadPoolExecutor executor

    static int count_interval = 5

    public static IFunController controller;

    LongAdder total = new LongAdder()

    static boolean key = true

    Closure produce

    int qps = 1

    private FunQpsConcurrent() {

    }

    FunQpsConcurrent(Closure closure) {
        this.produce = closure
    }

    void start() {
        if (executor == null) executor = ThreadPoolUtil.createQpsPool("Q")
        if (controller == null) controller = new FunTester();
        new Thread(controller, "接收器").start();
        while (key) {
            ThreadPoolUtil.executeTask(executor, qps, produce, total)
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
                    default:
                        if (Regex.isMatch(input, "(F|f)\\d+")) QPS_STEP = changeStringToInt(input.substring(1));
                        break;
                }
            }
        }

        @Override
        public void add() {
            qps += QPS_STEP
        }

        @Override
        public void reduce() {
            qps -= QPS_STEP
        }

        @Override
        public void over() {
            inputKey = false
            key = false
            logger.info("动态结束任务!");
        }

    }

}