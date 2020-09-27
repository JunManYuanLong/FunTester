# 获取JVM转储文件的Java工具类

[原文地址](https://blogs.oracle.com/sundararajan/programmatically-dumping-heap-from-java-applications)

在上期文章[如何获取JVM堆转储文件](https://mp.weixin.qq.com/s/qCg7nsXVvT1q-9yquQOfWA)中，介绍了几种方法获取JVM的转储文件，其中编程方法是里面唯一一个从JVM内部获取的方法。这里就不演示了其他方法获取正在运行的应用程序的堆转储，重点放在了使用编程来获取转储文件的方法，并演示了如何使用jhat工具浏览/分析生成的二进制堆转储。

你可能想在各个时间点从应用程序中转储多个堆快照，然后使用jhat离线分析这些快照。如何以编程方式从应用程序中转储堆？下面给出了一个例子。您可以从应用程序中转储堆，但必须进行一些编程，如下所示：

```
package com.fun.utils;

import com.fun.frame.SourceCode;
import com.sun.management.HotSpotDiagnosticMXBean;
import org.slf4j.Logger;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

public class HeapDumper extends SourceCode {

    private static Logger logger = getLogger();

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
    static void dumpHeap(String fileName, boolean live) {
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
```

* 重要说明：虽然可以从应用程序中转储多个堆快照，但不能将多个转储中的对象相关联。jmap工具使用对象地址作为对象标识符-在垃圾回收之间有所不同[回想一下GC可能会移动更改对象地址的对象]。但是，您可以通过汇总统计数据（例如直方图等）进行关联。

下面将生产好的`heap.bin`文件拉回到本地或者在服务端用`jhat -port 8888 heap.bin`工具进行处理，然后访问：`http://localhost:8888`即可查看当时JVM堆内存的使用情况。
如图：

![](http://pic.automancloud.com/QQ20191207-150829.png)

---
* **郑重声明**：文章禁止第三方（腾讯云除外）转载、发表，事情原委[测试窝，首页抄我七篇原创还拉黑，你们的良心不会痛吗？](https://mp.weixin.qq.com/s/ke5avkknkDMCLMAOGT7wiQ)

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [JUnit中用于Selenium测试的中实践](https://mp.weixin.qq.com/s/KG4sltQMCfH2MGXkRdtnwA)

## 非技术文章精选

- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [自动化和手动测试，保持平衡！](https://mp.weixin.qq.com/s/mMr_4C98W_FOkks2i2TiCg)
- [自动化测试生命周期](https://mp.weixin.qq.com/s/SH-vb2RagYQ3sfCY8QM5ew)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)