package com.fun.base.bean

import com.fun.db.mysql.MySqlTest
import com.fun.frame.Output

/**
 * 性能测试结果集
 */
@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "SE_TRANSIENT_FIELD_NOT_RESTORED")
class PerformanceResultBean extends AbstractBean {

    private static final long serialVersionUID = -1595942562342357L;

    /**
     * 测试用例描述
     */

    String mark
    /**
     * 开始时间
     */

    String startTime
    /**
     * 结束时间
     */

    String endTime
    /**
     * 表格信息
     */
    String table
    /**
     * 线程数
     */

    int threads
    /**
     * 总请求次数
     */

    int total
    /**
     * 平均响应时间
     */

    int rt
    /**
     * 吞吐量
     */

    double qps
    /**
     * 错误率
     */

    double errorRate
    /**
     * 失败率
     */

    double failRate
    /**
     * 执行总数
     */

    int executeTotal

    PerformanceResultBean(String mark, String startTime, String endTime, int threads, int total, int rt, double qps, double errorRate, double failRate, int executeTotal, String table) {
        this.mark = mark
        this.startTime = startTime
        this.endTime = endTime
        this.threads = threads
        this.total = total
        this.rt = rt
        this.qps = qps
        this.errorRate = errorRate
        this.failRate = failRate
        this.executeTotal = executeTotal
        this.table = table
        Output.output(this.toJson())
        Output.output(this.table)
        MySqlTest.savePerformanceBean(this)
    }

}
