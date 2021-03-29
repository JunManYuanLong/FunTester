package com.funtester.utils;

import com.sun.management.HotSpotDiagnosticMXBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

/**
 * 获取JVM内存转储文件的工具类
 */
public class HeapDumper {

    private static Logger logger = LogManager.getLogger(HeapDumper.class);

    /**
     * 这是HotSpot Diagnostic MBean的名称
     */
    private static final String HOTSPOT_BEAN_NAME = "com.sun.management:type=HotSpotDiagnostic";

    /**
     * 用于存储热点诊断MBean的字段
     */
    private static volatile HotSpotDiagnosticMXBean hotspotMBean;

    /**
     * 下载内存转储文件
     *
     * @param fileName 文件名,例如:heap.bin,不兼容路径,会在当前目录下生成
     * @param live
     */
    public static void dumpHeap(String fileName, boolean live) {
        initHotspotMBean();
        try {
            hotspotMBean.dumpHeap(fileName, live);
        } catch (Exception e) {
            logger.error("生成内存转储文件失败!", e);
        }
    }

    /**
     * 初始化热点诊断MBean
     */
    private static void initHotspotMBean() {
        if (hotspotMBean == null) {
            synchronized (HeapDumper.class) {
                if (hotspotMBean == null) {
                    try {
                        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
                        hotspotMBean = ManagementFactory.newPlatformMXBeanProxy(server, HOTSPOT_BEAN_NAME, HotSpotDiagnosticMXBean.class);
                    } catch (Exception e) {
                        logger.error("初始化mbean失败!", e);
                    }
                }
            }
        }
    }


}