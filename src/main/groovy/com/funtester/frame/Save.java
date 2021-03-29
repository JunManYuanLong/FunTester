package com.funtester.frame;

import com.funtester.base.exception.FailException;
import com.funtester.utils.RWUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.funtester.config.Constant.*;

/**
 * 用来保存数据的类，如果文件已经存在会删除原来的文件
 */
public class Save {

    private static Logger logger = LogManager.getLogger(Save.class);

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
        if (dirFile.exists()) {
            boolean delete = dirFile.delete();
            if (!delete) FailException.fail("删除文件失败!" + name);
        }
        RWUtil.writeText(dirFile, content);
        logger.info("数据保存成功！文件名：{}{}", LONG_Path, name);
    }

    /**
     * 保存list数据到本地文件
     */
    public static void saveLongList(Collection<Long> data, Object name) {
        List<String> list = new ArrayList<>();
        data.forEach(num -> list.add(num.toString()));
        saveStringList(list, name.toString());
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
        /*处理\n\t(LINE)*/
        if (buffer.length() > 2) info(name, buffer.substring(2));
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
