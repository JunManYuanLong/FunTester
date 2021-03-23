package com.funtester.base.bean

import com.funtester.db.mysql.MySqlTest
import com.funtester.frame.Output
import com.funtester.utils.DecodeEncode

/**
 * 性能测试结果集
 */
class PerformanceResultBean extends AbstractBean implements Serializable {

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
     * 吞吐量,公式为QPS=Thead/avg(time)
     */
    double qps

    /**
     * 通过QPS=count(r)/T公式计算得到的QPS,在固定QPS模式中,这个值来源于预设QPS
     */
    double qps2

    /**
     * 理论误差,两种统计模式
     */
    String deviation

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

    PerformanceResultBean(String mark, String startTime, String endTime, int threads, int total, int rt, double qps, double qps2, double errorRate, double failRate, int executeTotal, String table) {
        this.mark = mark
        this.startTime = startTime
        this.endTime = endTime
        this.threads = threads
        this.total = total
        this.rt = rt
        this.qps = qps
        this.qps2 = qps2
        this.errorRate = errorRate
        this.failRate = failRate
        this.executeTotal = executeTotal
        this.table = DecodeEncode.zipBase64(table)
        this.deviation = com.funtester.frame.SourceCode.getPercent(Math.abs(qps - qps2) * 100 / Math.max(qps, qps2))
        Output.output(this.toJson())
        Output.output(table)
        MySqlTest.savePerformanceBean(this)
    }

}
