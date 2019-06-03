package com.fun.frame;

import com.fun.utils.WriteRead;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
        info("long.log", content);
    }

    public static void info(String name, String content) {
        File dirFile = new File(LONG_Path + name);
        if (dirFile.exists()) dirFile.delete();
        WriteRead.writeText(dirFile, content);
        logger.info("数据保存成功！文件名：" + name);
    }

    /**
     * 保存list数据到本地文件，name不需要后缀
     */
    public static void saveLongList(List<Long> data, Object name) {
        List<String> list = new ArrayList<>();
        data.forEach(num -> list.add(num.toString()));
        saveStringList(list, name.toString());
    }

    /**
     * 保存list数据到本地文件，name不需要后缀
     */
    public static void saveIntegerList(List<Integer> data, String name) {
        List<String> list = new ArrayList<>();
        data.forEach(num -> list.add(num.toString()));
        saveStringList(list, name);
    }

    /**
     * 保存list数据到本地文件，name不需要后缀
     */
    public static void saveDoubleList(List<Double> data, String name) {
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
    public static void saveList(List<Object> data, String name) {
        List<String> list = new ArrayList<>();
        data.forEach(num -> list.add(num.toString()));
        saveStringList(list, name);
    }

    /**
     * 保存list数据到本地文件,name不需要后缀
     */
    public static void saveStringList(List<String> data, String name) {
        String join = StringUtils.join(data, LINE);
        info(name + FILE_TYPE_LOG, join);
    }

    /**
     * 保存set数据到本地文件,name不需要后缀
     */
    public static void saveStringList(Set<String> data, String name) {
        List<String> objects = new ArrayList<>(data);
        saveStringList(objects, name);
    }

    /**
     * 保存json数据到本地文件，name不需要后缀
     */
    public static void saveJson(JSONObject data, String name) {
        StringBuffer buffer = new StringBuffer();
        data.keySet().forEach(x -> buffer.append(LINE + x.toString() + PART + data.getString(x.toString())));
        info(name + FILE_TYPE_LOG, buffer.toString().substring(2));
    }

    public static void main(String[] args) {
        List list = Arrays.asList(32432, 43242, 4, 2, 3, 4);
        String fan = StringUtils.join(list, "fan");
        output(fan);
    }
}
