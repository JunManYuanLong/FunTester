package com.fun.base.bean

/**
 * 性能测试结果集
 */
public class PerformanceResultBean extends BaseBean {

    int thrads;

    int total;

    int rt;

    double qps;

    public PerformanceResultBean(int thrads, int total, int rt, double qps) {
        this.thrads = thrads;
        this.total = total;
        this.rt = rt;
        this.qps = qps;
    }
}
