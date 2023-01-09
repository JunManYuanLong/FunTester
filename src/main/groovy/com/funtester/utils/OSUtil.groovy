package com.funtester.utils

import com.funtester.frame.SourceCode

import java.lang.management.ManagementFactory
import java.lang.management.OperatingSystemMXBean
import java.lang.management.ThreadMXBean

class OSUtil extends SourceCode {

    static OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean()

    static ThreadMXBean threadBean = ManagementFactory.getThreadMXBean()

    static long lastSysTime = System.nanoTime()

    static long lastUserTime = 0

    /**
     * 获取最大进程数N,CPU使用率N*100%
     * @return
     */
    static int getAvailableProcessors() {
        osMxBean.getAvailableProcessors()
    }

    /**
     * 获取当前线程CPU使用率,最大100
     * 已乘以100,已经除以了系统最大进程数
     * @return
     */
    static double getCpuUsage() {
        long totalTime = 0
        for (long id : threadBean.getAllThreadIds()) {
            totalTime += threadBean.getThreadCpuTime(id)
        }
        long curtime = System.nanoTime()
        long usedTime = totalTime - lastUserTime
        long totalPassedTime = curtime - lastSysTime
        lastSysTime = curtime
        lastUserTime = totalTime
        def d = (((double) usedTime) / totalPassedTime / osMxBean.getAvailableProcessors()) * 100
        return d > 100 ? 8 : d
    }

}
