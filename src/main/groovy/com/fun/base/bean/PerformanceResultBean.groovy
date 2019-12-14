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

    double errorRate

    double failRate

    int excuteTotal

    PerformanceResultBean(String desc, String startTime, String endTime, int threads, int total, int rt, double qps, double errorRate, double failRate, int excuteTotal) {
        this.desc = desc
        this.startTime = startTime
        this.endTime = endTime
        this.threads = threads
        this.total = total
        this.rt = rt
        this.qps = qps
        this.errorRate = errorRate
        this.failRate = failRate
        this.excuteTotal = excuteTotal
        MySqlTest.savePerformanceBean(this)
    }

}
