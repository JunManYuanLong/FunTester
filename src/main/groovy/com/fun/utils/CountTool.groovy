package com.fun.utils

import com.fun.frame.SourceCode
import net.sf.json.JSONObject

import java.util.stream.Collectors

/**
 * 统计出现次数相关类
 */
class CountTool extends SourceCode {
    /**
     * 统计数据出现的次数
     *
     * @param counts 统计的 jsonobject 对象
     * @param object 需要统计的数据
     */
    static def count(JSONObject counts, Object object) {
        count(counts, object, 1)
    }

    /**
     * 统计数据出现的次数
     *
     * @param counts 统计的 jsonobject 对象
     * @param object 需要统计的数据
     * @param num 默认值
     */
    static def count(JSONObject counts, Object object, int num) {
        counts.put(object, Integer.valueOf(counts.getOrDefault(object.toString(), num)))
    }

/**
 * 统计某个list里面某个元素出现的次数
 * @param list
 * @param str
 * @return
 */
    static def count(List list, def str) {
        list.count { s -> s.toString().equals(str.toString()) }
    }

/**
 * 统计某个list里面各个元素出现的次数
 * collect,是一个map<object,list>对象
 * @param list
 * @return
 */
    static def count(List list) {
        def collect = list.stream().collect(Collectors.groupingBy { x -> x.toString() })
        collect.keySet().stream().sorted().forEach { x ->
            output("元素：${x}，次数：${collect.get(x).size()}")
        }
    }
}
