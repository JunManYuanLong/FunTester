package com.funtester.utils

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
        list.count {s -> s.toString().equals(str.toString())}
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
}
