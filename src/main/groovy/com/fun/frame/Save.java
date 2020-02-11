package com.fun.frame;

import com.fun.utils.WriteRead;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用来保存数据的类，如果文件已经存在会删除原来的文件
 */
public class Save extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(Save.class);

    /**
     * 保存信息，每次回删除文件，默认当前工作空间
     *
     * @param content 内容
     */
    public static void info(String content) {
        info("long", content);
    }

    public static void info(String name, String content) {
        File dirFile = new File(LONG_Path + name);
        if (dirFile.exists()) dirFile.delete();
        WriteRead.writeText(dirFile, content);
        logger.info("数据保存成功！文件名：{}{}", LONG_Path, name);
    }

    /**
     * 保存list数据到本地文件
     */
    public static void saveLongList(Collection<Long> data, Object name) {
        List<String> list = new ArrayList<>();
        data.forEach(num -> list.add(num.toString()));
        saveStringList(list, name.toString());
        data.removeAll(data);
    }

    /**
     * 保存list数据到本地文件
     */
    public static void saveIntegerList(Collection<Integer> data, String name) {
        List<String> list = new ArrayList<>();
        data.forEach(num -> list.add(num.toString()));
        saveStringList(list, name);
    }

    /**
     * 保存list数据到本地文件
     */
    public static void saveDoubleList(Collection<Double> data, String name) {
        List<String> list = new ArrayList<>();
        data.forEach(num -> list.add(num.toString()));
        saveStringList(list, name);
    }

    /**
     * 保存list数据，long类型无法覆盖
     *
     * @param data
     * @param name
     */
    public static void saveList(Collection<Object> data, String name) {
        List<String> list = new ArrayList<>();
        data.forEach(num -> list.add(num.toString()));
        saveStringList(list, name);
    }

    /**
     * 保存list数据到本地文件
     */
    public static void saveStringList(Collection<String> data, String name) {
        String join = StringUtils.join(data, LINE);
        info(name, join);
    }

    /**
     * 保存json数据到本地文件
     */
    public static void saveJson(JSONObject data, String name) {
        StringBuffer buffer = new StringBuffer();
        data.keySet().forEach(x -> buffer.append(LINE + x.toString() + PART + data.getString(x.toString())));
        info(name, buffer.toString().substring(2));
    }

    /**
     * 同步save数据,用于匿名类多线程保存测试数据
     *
     * @param data
     * @param name
     */
    public static void saveStringListSync(Collection<String> data, String name) {
        synchronized (Save.class) {
            if (data.isEmpty()) return;
            saveStringList(data, name);
        }
    }


}
