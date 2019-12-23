package com.fun.utils;

import com.fun.frame.SourceCode;

public class ArgsUtil extends SourceCode {

    String[] all;

    public ArgsUtil(String[] args) {
        all = args;
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

}
