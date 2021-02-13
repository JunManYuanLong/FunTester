package com.funtester.base.bean

import com.funtester.db.mysql.MySqlTest
import com.funtester.frame.Output
import com.funtester.utils.DecodeEncode

/**
 * 性能测试结果集
 */
@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "SE_TRANSIENT_FIELD_NOT_RESTORED")
class PerformanceResultBean extends AbstractBean implements Serializable{

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
        this.table = DecodeEncode.zipBase64(table)
        Output.output(this.toJson())
        Output.output(table)
        MySqlTest.savePerformanceBean(this)
    }

}
