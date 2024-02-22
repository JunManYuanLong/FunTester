package com.funtester.utils

import com.funtester.base.bean.AbstractBean
import com.funtester.frame.SourceCode
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.stream.Collectors

/**
 * 统计出现次数相关类
 */
class CountUtil {

    private static Logger logger = LogManager.getLogger(CountUtil.class)

    /**
     * 统计数据出现的次数
     *
     * @param counts 统计的 jsonobject 对象
     * @param object 需要统计的数据
     */
    static def count(Map counts, Object object) {
        count(counts, object, 1)
    }

    /**
     * 统计数据出现的次数
     *
     * @param counts 统计的 jsonobject 对象
     * @param object 需要统计的数据
     * @param num 增加值
     */
    static def count(Map counts, Object object, int num) {
        counts.put(object, Integer.valueOf(counts.getOrDefault(object, 0) + num))
    }

    /**
     * 统计某个list里面某个元素出现的次数
     * @param list
     * @param str
     * @return
     */
    static def count(List list, def str) {
        list.count {s -> (s.toString() == str.toString())}
    }

    /**
     * 统计某个list里面各个元素出现的次数
     * collect,是一个map<object,integer>对象
     * @param list
     * @return
     */
    static def count(List list) {
        list.stream().collect(Collectors.groupingBy {x -> x}).each {
            it.setValue(it.value.size())
            logger.info("元素：${it.key}，次数：${it.value}")
        }
    }

    /**
     * 统计list各分位数据
     * @param c
     * @return
     */
    static FunIndex index(List<? extends Number> c) {
        if (c == null || c.size() == 0) return new FunIndex()
        c.sort()
        int size = c.size()
        double min = c.first()
        double max = c.last()
        double p99 = c.get(size * 0.99 as Integer)
        double p999 = c.get(size * 0.999 as Integer)
        double p95 = c.get(size * 0.95 as Integer)
        double avg = SourceCode.changeStringToDouble(SourceCode.formatNumber(c.average(), "#.###"))
        def mid = c.get(size / 2 as Integer)
        new FunIndex(avg: avg, mid: mid, min: min, max: max, p99: p99, p999: p999, p95: p95)
    }


    /**
     * 统计结果
     */
    static class FunIndex extends AbstractBean {

        String title

        Double avg

        Double mid

        Double min

        Double max

        Double p99

        Double p999

        Double p95

        FunIndex setTitle(String title) {
            this.title = title
            this
        }

        @Override
        String toString() {
            "平均值:$avg ,最大值$max ,最小值:$min ,中位数:$mid p99:$p99 p95:$p95"
        }
    }

}
