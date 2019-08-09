package com.fun.base.bean

import com.fun.db.mysql.MySqlTest


/**
 * 性能测试结果集
 */
class PerformanceResultBean extends AbstractBean {

    String desc;

    int threads;

    int total;

    int rt;

    double qps;

    PerformanceResultBean(int threads, int total, int rt, double qps, String desc) {
        this.threads = threads;
        this.total = total;
        this.rt = rt;
        this.qps = qps;
        this.desc = desc;
        this.print()
        MySqlTest.savePerformanceBean(this)
    }
}
