package com.fun.base.bean

/**
 * 性能测试结果集
 */
class PerformanceResultBean extends BaseBean {

    int thrads;

    int total;

    int rt;

    double qps;

    PerformanceResultBean(int thrads, int total, int rt, double qps) {
        this.thrads = thrads;
        this.total = total;
        this.rt = rt;
        this.qps = qps;
    }
}
