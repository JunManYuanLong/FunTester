package com.funtester.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.funtester.frame.SourceCode;

import java.io.File;

public class ArgsUtil extends SourceCode {

    String[] all;

    JSONObject params = new JSONObject();

    public ArgsUtil(String[] args) {
        all = (String[]) args.clone();
        toJson();
    }

    /**
     * 兼容成对参数
     *
     * @return
     */
    public JSONObject toJson() {
        JSONObject params = new JSONObject();
        if (all.length % 2 == 1) return params;
        for (int i = 0; i < all.length; i += 2) {
            String key = all[i];
            String value = all[i + 1];
            params.put(key, value);
        }
        return params;
    }

    /**
     * 获取成对参数值
     *
     * @param key
     * @return
     */
    public String getValue(String key) {
        return params.getString(key);
    }

    /**
     * 获取int参数
     *
     * @param i 获取的参数索引
     * @param k 默认值
     * @return
     */
    public int getIntOrdefault(int i, int k) {
        return i >= all.length ? k : changeStringToInt(all[i]);
    }

    /**
     * 获取boolean参数
     *
     * @param i
     * @param k
     * @return
     */
    public boolean getBooleanOrdefault(int i, boolean k) {
        return i >= all.length ? k : changeStringToBoolean(all[i]);
    }


    /**
     * @param i
     * @param k
     * @return
     */
    public String getStringOrdefault(int i, String k) {
        return i >= all.length ? k : all[i];
    }


    /**
     * @param i
     * @param path
     * @return
     */
    public File getFileOrDefault(int i, String path) {
        return i >= all.length ? new File(path) : new File(all[i]);
    }

    /**
     * @param i
     * @param json
     * @return
     */
    public JSONObject getJsonOrDefault(int i, String json) {
        return i >= all.length ? JSON.parseObject(json) : JSON.parseObject(all[i]);
    }


}
