# 如何获取JAVA HEAP DUMPS（转储文件）

[原文地址]( https://blog.heaphero.io/2017/10/13/how-to-capture-java-heap-dumps-7-options/)

堆转储是诊断与内存相关的问题（例如内存泄漏缓慢，垃圾回收问题和 java.lang.OutOfMemoryError。它们也是优化内存消耗的重要工具。

有很多很不错的的工具，例如Eclipse MAT和Heap Hero，可以分析堆转储。但是，您需要为这些工具提供以正确的格式和正确的时间点捕获的堆转储。

本文为您提供了捕获堆转储的多个选项。但是，我认为前三个是有效的选择，而其他三个则是个不错的选择。

## jmap
jmap打印堆转储到指定的文件位置。该工具打包在JDK中。可以在`JAVA_HOMTE\bin`文件夹中找到它。

这是调用jmap的方法：

```
jmap -dump:format=b,file=<file-path> <pid> 

where
pid: is the Java Process Id, whose heap dump should be captured
file-path: is the file path where heap dump will be written in to.
```

例：
`jmap -dump:format=b,file=/opt/tmp/heapdump.bin 37320`

* 注意：参数“live”选项非常重要。如果传递了此选项，则仅将内存中的存活的对象写入堆转储文件。如果未通过此选项，则所有对象，即使是准备进行垃圾回收的对象，都将打印在堆转储文件中。它将大大增加堆转储文件的大小。这也将使分析变得乏味无聊。要解决内存问题或优化内存，只选用“live”选项就足够了。

## HeapDumpOnOutOfMemoryError

当应用程序遇到java.lang.OutOfMemoryError时，理想的方法是立即捕获堆转储以诊断问题，因为您想知道java.lang.OutOfMemoryError发生时内存中有哪些对象以及它们占据的内存百分比。但是，由于很多方面的原因，大多数情况下，IT/运营团队都无法及时捕获堆转储。不仅如此，他们还重新启动了应用程序。如果没有在正确的时间捕获堆转储，就很难诊断出任何内存问题。

这就是该选项非常方便的地方。在应用程序启动期间传递“ -XX：+ HeapDumpOnOutOfMemoryError”系统属性时，JVM将在JVM遇到OutOfMemoryError时立即捕获堆转储。

用法：

`-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/tmp/heapdump.bin`

* 注意：捕获的堆转储将在'-XX：HeapDumpPath'系统属性指定的位置打印。
* 最佳实践：始终保持在所有应用程序中配置此属性，因为您永远不知道何时会发生OutOfMemoryError。

## jcmd
jcmd工具用于将诊断命令请求发送到JVM。它打包为JDK的一部分。可以在`JAVA_HOMTE\bin`文件夹中找到它。

这是调用jcmd的方法：

```
jcmd <pid> GC.heap_dump <file-path>
where
pid: is the Java Process Id, whose heap dump should be captured
file-path: is the file path where heap dump will be written in to.
```

例：

`jcmd 37320 GC.heap_dump /opt/tmp/heapdump.bin`

## JVisualVM

JVisualVM是一个监视，故障排除工具，打包在JDK中。启动此工具时，您可以看到本地计算机上正在运行的所有Java进程。您也可以使用此工具连接到在远程计算机上运行的Java进程。

步骤：

* 在`JAVA_HOMTE\bin`文件夹下启动jvisualvm
* 右键单击其中一个Java进程
* 点击下拉菜单上的“堆转储”选项
* 将生成堆转储
* 将在“摘要”选项卡>“基本信息”>“文件”部分中指定生成堆转储的文件路径。

![从JVisualVM捕获堆转储](http://q0zm42rte.bkt.clouddn.com/QQ20191126-143125.png)

## JMX

有一个com.sun.management:type=HotSpotDiagnostic MBean。此MBean具有“dumpHeap”操作。调用此操作将捕获堆转储。'dumpHeap'操作采用两个输入参数：

* outputFile：应将堆转储写入的文件路径
* live：传递“ true”时，仅捕获堆中的活动对象


您可以使用JConsole，jmxsh，Java Mission Control 等JMX客户端来调用此MBean操作。我这里使用了jconsole：

![使用JConsole作为JMX客户端来生成堆转储](http://q0zm42rte.bkt.clouddn.com/QQ20191126-144859.png)

## 编程代码

除了使用工具之外，您还可以以编程方式从应用程序中捕获堆转储。在某些情况下，您可能希望基于应用程序中的某些事件来捕获堆转储。可以通过调用`com.sun.management:type=HotSpotDiagnostic MBean JMX Bean`，提供了从应用程序捕获堆转储的源代码。
