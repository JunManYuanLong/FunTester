package com.fun.utils

import com.fun.frame.SourceCode
import net.sf.json.JSONObject

import java.util.stream.Collectors

class CountTool extends SourceCode {
    /**
     * 统计数据出现的次数
     *
     * @param counts 统计的 jsonobject 对象
     * @param object 需要统计的数据
     */
    static count(JSONObject counts, Object object) {
        count(counts, object, 1)
    }

    /**
     * 统计数据出现的次数
     *
     * @param counts 统计的 jsonobject 对象
     * @param object 需要统计的数据
     */
    def static count(JSONObject counts, Object object, int num) {
        if (counts.containsKey(object.toString())) {
            int i = counts.getInt(object.toString()) + num
            counts.put(object.toString(), i)
        } else {
            counts.put(object.toString(), num)
        }
    }

/**
 * 统计某个list里面某个元素出现的次数
 * @param list
 * @param str
 * @return
 */
    def static count(List list, def str) {
        list.count { s -> s.toString().equals(str.toString()) }
    }

/**
 * 统计某个list里面各个元素出现的次数
 * @param list
 * @return
 */
    def static count(List list) {
        Map<Integer, List<Object>> collect = list.stream().collect(Collectors.groupingBy { x -> x.toString() })
        collect.keySet().stream().sorted().forEach { x ->
            output("元素：${x}，次数：${collect.get(x).size()}")
        }
    }
}
