package com.funtester.utils

import com.funtester.base.bean.AbstractBean
import com.funtester.frame.SourceCode

import java.lang.management.*

class OSUtil extends SourceCode {

    static OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean()

    static MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean()

    static ThreadMXBean threadBean = ManagementFactory.getThreadMXBean()

    static long lastSysTime = System.nanoTime()

    static long lastUserTime = 0

    static List<GarbageCollectorMXBean> gcMxBeans = ManagementFactory.getGarbageCollectorMXBeans();


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
        def d = (((double) usedTime) / totalPassedTime / getAvailableProcessors()) * 100
        return d > 100 ? 8.88 : d
    }

    /**
     * 获取堆内存信息
     * @return
     */
    static def heapMemInfo() {
        memoryMXBean.getHeapMemoryUsage()
    }

    /**
     * 获取非堆内存信息
     * @return
     */
    static def noHeapMemInfo() {
        memoryMXBean.getNonHeapMemoryUsage()
    }

    /**
     * 获取系统一分钟内的平均load
     * @return
     */
    static def getLoad() {
        osMxBean.getSystemLoadAverage() / getAvailableProcessors()
    }

    /**
     * 获取GC信息{@link com.funtester.utils.OSUtil.GCInfo}
     * @return
     */
    static def getGCinfo() {
        def infos = []
        for (GarbageCollectorMXBean gcMxBean : gcMxBeans) {
            infos << new GCInfo(gcMxBean)
        }
        infos
    }

    /**
     * GC信息类
     */
    static class GCInfo extends AbstractBean {

        String name

        int count

        int time

        GCInfo(String name, int count, int time) {
            this.name = name
            this.count = count
            this.time = time
        }

        GCInfo(GarbageCollectorMXBean gcMxBean) {
            this.name = gcMxBean.getName()
            this.count = gcMxBean.getCollectionCount()
            this.time = gcMxBean.getCollectionTime()
        }

    }
}
