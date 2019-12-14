package com.fun.utils

import com.fun.frame.SourceCode
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 时间观察者类，用于简单记录执行时间
 */
class TimeWatch extends SourceCode implements Serializable {

    private static final long serialVersionUID = -4156600036913348727L;

    static Logger logger = LoggerFactory.getLogger(TimeWatch.class)
/**
 * 默认的名称
 */

    def name = "default"
/**
 * 纳秒
 */

    def startNano
    /**
     * 标记集合
     */

    def marks = new HashMap<String, Mark>()
    /**
     * 毫秒
     */

    def startMillis

/**
 * 无参创建方法，默认名称
 * @return
 */
    public static TimeWatch create() {
        final TimeWatch timeWatch = new TimeWatch()
        timeWatch.start()
    }

/**
 * 创建方法
 * @param name
 * @return
 */
    public static TimeWatch create(def name) {
        final TimeWatch timeWatch = new TimeWatch()
        timeWatch.start()
    }


    private TimeWatch() {
    }

/**
 * 开始记录
 * @return
 */
    def start() {
        reset()
    }

/**
 * 重置
 */
    def reset() {
        startNano = getNanoMark()
        startMillis = Time.getTimeStamp()
        this
    }
/**
 * 标记
 * @param name
 * @return
 */
    String mark(String name) {
        marks.put name, new Mark(name)
        name
    }

/**
 * 标记
 * @return
 */
    String mark() {
        mark(name)
    }

/**
 * 获取标记时间
 * @return
 */
    def getMarkTime() {
        if (marks.containsKey(name)) {
            def diff = Time.getTimeStamp() - marks.get(name).getStartMillis()
            logger.info(LINE + "观察者：{}的标记：{}记录时间：{} ms", name, name, getFormatNumber(diff))
        } else {
            logger.warn("没有默认标记！")
        }
    }

/**
 * 获取标记时间
 * @return
 */
    def getMarkNanoTime() {
        if (marks.containsKey(name)) {
            def diff = getNanoMark() - marks.get(name).getStartNano()
            logger.info(LINE + "观察者：{}的标记：{}记录时间：{} ns", name, name, getFormatNumber(diff))
        } else {
            logger.warn("没有默认标记！")
        }
    }


/**
 * 获取某个标记的记录时间
 * @param name
 * @return
 */
    def getMarkTime(String name) {
        if (marks.containsKey(name)) {
            def diff = Time.getTimeStamp() - marks.get(name).getStartMillis()
            logger.info(LINE + "观察者：{}的标记：{}记录时间：{} ms", name, name, getFormatNumber(diff))
        } else {
            logger.warn("没有{}标记！", name)
        }
    }

/**
 * 获取某个标记的记录时间
 * @param name
 * @return
 */
    def getMarkNanoTime(String name) {
        if (marks.containsKey(name)) {
            def diff = getNanoMark() - marks.get(name).getStartNano()
            logger.info(LINE + "观察者：{}的标记：{}记录时间：{} ns", name, name, getFormatNumber(diff))
        } else {
            logger.warn("没有{}标记！", name)
        }
    }


/**
 * 获取记录时间
 * @return
 */
    def getTime() {
        def diff = Time.getTimeStamp() - startMillis
        logger.info(LINE + "观察者：{}，记录时间：{} ms", getName(), getFormatNumber(diff))
        diff
    }

/**
 * 获取记录时间纳秒
 * @return
 */
    def getNanoTime() {
        long diff = getNanoMark() - startNano
        logger.info(LINE + "观察者：{}，记录时间：{} ns", getName(), getFormatNumber(diff))
        diff
    }

/**
 * 获取标记与观察者的时间差
 * @param name
 * @return
 */
    def getDiffTime(String name) {
        if (marks.containsKey(name)) {
            def diff = marks.get(name).getStartTimeMillis() - this.getStartTimeMillis()
            logger.info(LINE + "观察者：{}和标记：{}记录时间差：{} ms", name, name, getFormatNumber(diff))
        } else {
            logger.warn("没有{}标记！", name)
        }
    }

    /**
     * 获取标记与观察者的时间差
     * @param name
     * @return
     */
    def getDiffNanoTime(String name) {
        if (marks.containsKey(name)) {
            def diff = marks.get(name).getStartNano() - this.getStartTime()
            logger.info(LINE + "观察者：{}和标记：{}记录时间差：{} ns", name, name, getFormatNumber(diff > 0 ? diff : -diff))
        } else {
            logger.warn("没有{}标记！", name)
        }
    }

/**
 * 获取两个标记的时间差
 * @param first
 * @param second
 * @return
 */
    def getDiffTime(String first, String second) {
        if (marks.containsKey(first) && marks.containsKey(second)) {
            def diff = marks.get(second).getStartMillis() - marks.get(first).getStartMillis()
            logger.info(LINE + "标记：{}和标记：{}记录时间差：{} ms", first, second, diff)
        } else {
            logger.warn("没有{}标记！", first + TAB + second)
        }
    }


/**
 * 获取两个标记的时间差
 * @param first
 * @param second
 * @return
 */
    def getDiffNanoTime(String first, String second) {
        if (marks.containsKey(first) && marks.containsKey(second)) {
            def diff = marks.get(second).getStartNano() - marks.get(first).getStartNano()
            logger.info(LINE + "标记：{}和标记：{}记录时间差：{} ns", first, second, getFormatNumber(diff))
        } else {
            logger.warn("没有{}标记！", first + TAB + second)
        }
    }

    @Override
    public String toString() {
        return "时间观察者：" + this.name
    }

    @Override
    public TimeWatch clone() {
        def watch = new TimeWatch()
        watch.name = getName() + "_c"
        watch.startTime = getStartTime()
        watch.startTimeMillis = getStartTimeMillis()
        watch
    }
/**
 * 标记类
 */
    class Mark implements Serializable {

        private static final long serialVersionUID = -4156604036913335727L;

        public Mark(def name) {
            this.name = name
            reset()
        }

        def name

        def startNano

        def startMillis

        def lastNano

        def lastMills

        def reset() {
            this.startNano = getNanoMark()
            this.startMillis = Time.getTimeStamp()
        }
    }
}
