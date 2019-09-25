package com.fun.base.bean

import com.fun.db.mysql.MySqlTest


/**
 * 性能测试结果集
 */
class PerformanceResultBean extends AbstractBean {

    String desc

    String startTime

    String endTime

    int threads

    int total

    int rt

    double qps

    public PerformanceResultBean(int threads, int total, int rt, double qps, String desc, String start, String end) {
        this.threads = threads
        this.total = total
        this.rt = rt
        this.qps = qps
        this.desc = desc
        this.startTime = start
        this.endTime = end
        this.print()
        MySqlTest.savePerformanceBean(this)
    }
}
