# 用Groovy在JMeter中执行命令行

前两期文章讲了一些Groovy和JMeter一些配合使用：

- [用Groovy处理JMeter断言和日志](https://mp.weixin.qq.com/s/Q4yPA4p8dZYAARZ60ZDh9w)
- [用Groovy处理JMeter变量](https://mp.weixin.qq.com/s/BxtweLrBUptM8r3LxmeM_Q)

这次再来分享一下，Groovy在JMeter中执行命令行的方法，目前我暂时想不到很多适用场景，因为的确我对JMeter并不是很熟悉，也就这两天才开始看一些资料。如果你有很好的适用场景，可以留言告诉我，一起交流交流。

* 首先新建一个简单的线程组和一个简单的请求：

![](http://pic.automancloud.com/1583240306684.jpg)

* 添加JSR223 预处理程序（后置处理程序需要下一次次请求）

![](http://pic.automancloud.com/QQ20200304-152436.png)

脚本内容：

```Groovy
log.info("jps -lv".execute().text)

"jps -lv".execute().text.eachLine{
	log.error("222222222 ${it}")
}

log.info("jmeter -v".execute().text)

```

* 控制台输出：


```Groovy
2020-03-04 15:25:56,544 INFO o.a.j.e.StandardJMeterEngine: Running the test!
2020-03-04 15:25:56,550 INFO o.a.j.s.SampleEvent: List of sample_variables: []
2020-03-04 15:25:56,551 INFO o.a.j.g.u.JMeterMenuBar: setRunning(true, *local*)
2020-03-04 15:25:56,905 INFO o.a.j.e.StandardJMeterEngine: Starting ThreadGroup: 1 : 线程组
2020-03-04 15:25:56,905 INFO o.a.j.e.StandardJMeterEngine: Starting 1 threads for group 线程组.
2020-03-04 15:25:56,906 INFO o.a.j.e.StandardJMeterEngine: Thread will continue on error
2020-03-04 15:25:56,906 INFO o.a.j.t.ThreadGroup: Starting thread group... number=1 threads=1 ramp-up=1 perThread=1000.0 delayedStart=false
2020-03-04 15:25:56,907 INFO o.a.j.t.ThreadGroup: Started thread group number 1
2020-03-04 15:25:56,907 INFO o.a.j.e.StandardJMeterEngine: All thread groups have been started
2020-03-04 15:25:56,908 INFO o.a.j.t.JMeterThread: Thread started: 线程组 1-1
2020-03-04 15:25:57,220 INFO o.a.j.m.J.JSR223 预处理程序: 48993  -Xms512m -Xmx1500m -XX:ReservedCodeCacheSize=500m -XX:+UseCompressedOops -Dfile.encoding=UTF-8 -XX:+UseConcMarkSweepGC -XX:SoftRefLRUPolicyMSPerMB=50 -ea -Dsun.io.useCanonCaches=false -Djava.net.preferIPv4Stack=true -Djdk.http.auth.tunneling.disabledSchemes="" -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitStackTraceInFastThrow -Xverify:none -javaagent:/Users/fv/jetbrains-agent.jar -Djb.vmOptionsFile=/Users/fv/Library/Preferences/IntelliJIdea2018.3/idea.vmoptions -Didea.java.redist=jdk-bundled -Didea.paths.selector=IntelliJIdea2018.3 -Didea.executable=idea -Didea.home.path=/Applications/IntelliJ IDEA.app/Contents
49572 sun.tools.jps.Jps -Dapplication.home=/Library/Java/JavaVirtualMachines/jdk1.8.0_51.jdk/Contents/Home -Xms8m
48524 ./ApacheJMeter.jar -Dapple.laf.useScreenMenuBar=true -Dapple.eawt.quitStrategy=CLOSE_ALL_WINDOWS
49053 org.jetbrains.idea.maven.server.RemoteMavenServer -Djava.awt.headless=true -Didea.version==2018.3.5 -Xmx768m -Didea.maven.embedder.version=3.3.9 -Dfile.encoding=UTF-8

2020-03-04 15:25:57,393 ERROR o.a.j.m.J.JSR223 预处理程序: 222222222 48993  -Xms512m -Xmx1500m -XX:ReservedCodeCacheSize=500m -XX:+UseCompressedOops -Dfile.encoding=UTF-8 -XX:+UseConcMarkSweepGC -XX:SoftRefLRUPolicyMSPerMB=50 -ea -Dsun.io.useCanonCaches=false -Djava.net.preferIPv4Stack=true -Djdk.http.auth.tunneling.disabledSchemes="" -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitStackTraceInFastThrow -Xverify:none -javaagent:/Users/fv/jetbrains-agent.jar -Djb.vmOptionsFile=/Users/fv/Library/Preferences/IntelliJIdea2018.3/idea.vmoptions -Didea.java.redist=jdk-bundled -Didea.paths.selector=IntelliJIdea2018.3 -Didea.executable=idea -Didea.home.path=/Applications/IntelliJ IDEA.app/Contents
2020-03-04 15:25:57,393 ERROR o.a.j.m.J.JSR223 预处理程序: 222222222 49573 sun.tools.jps.Jps -Dapplication.home=/Library/Java/JavaVirtualMachines/jdk1.8.0_51.jdk/Contents/Home -Xms8m
2020-03-04 15:25:57,393 ERROR o.a.j.m.J.JSR223 预处理程序: 222222222 48524 ./ApacheJMeter.jar -Dapple.laf.useScreenMenuBar=true -Dapple.eawt.quitStrategy=CLOSE_ALL_WINDOWS
2020-03-04 15:25:57,393 ERROR o.a.j.m.J.JSR223 预处理程序: 222222222 49053 org.jetbrains.idea.maven.server.RemoteMavenServer -Djava.awt.headless=true -Didea.version==2018.3.5 -Xmx768m -Didea.maven.embedder.version=3.3.9 -Dfile.encoding=UTF-8
2020-03-04 15:25:58,421 INFO o.a.j.m.J.JSR223 预处理程序:     _    ____   _    ____ _   _ _____       _ __  __ _____ _____ _____ ____     
   / \  |  _ \ / \  / ___| | | | ____|     | |  \/  | ____|_   _| ____|  _ \   
  / _ \ | |_) / _ \| |   | |_| |  _|    _  | | |\/| |  _|   | | |  _| | |_) | 
 / ___ \|  __/ ___ \ |___|  _  | |___  | |_| | |  | | |___  | | | |___|  _ <  
/_/   \_\_| /_/   \_\____|_| |_|_____|  \___/|_|  |_|_____| |_| |_____|_| \_\ 5.1 r1853635  

Copyright (c) 1999-2019 The Apache Software Foundation


2020-03-04 15:25:58,424 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-1
2020-03-04 15:25:58,424 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-1
2020-03-04 15:25:58,424 INFO o.a.j.e.StandardJMeterEngine: Notifying test listeners of end of test
2020-03-04 15:25:58,425 INFO o.a.j.g.u.JMeterMenuBar: setRunning(false, *local*)

```

虽然不很擅长JMeter，这些功能需求主要还是来自于测试同行同事，我顺便查一下，写一些Demo，如果你对Groovy在JMeter应用也有类似的功能需求，可以留言，我趁着JMeter知识还没在我脑海中衰退，写个Demo给你。

---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [Java并发BUG基础篇](https://mp.weixin.qq.com/s/NR4vYx81HtgAEqH2Q93k2Q)
- [Java并发BUG提升篇](https://mp.weixin.qq.com/s/GCRRe8hJpe1QJtxq9VBEhg)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [压测中测量异步写入接口的延迟](https://mp.weixin.qq.com/s/odvK1iYgg4eRVtOOPbq15w)
- [多种登录方式定量性能测试方案](https://mp.weixin.qq.com/s/WuZ2h2rr0rNBgEvQVioacA)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [左移测试](https://mp.weixin.qq.com/s/8zXkWV4ils17hUqlXIpXSw)
- [选择手动测试还是自动化测试？](https://mp.weixin.qq.com/s/4haRrfSIp5Plgm_GN98lRA)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCxr0Sa2MXpNKicZE024zJm7vIAFRC09bPV9iaMer9Ncq8xppcYF73sDHbrG2iaBtRqCFibdckDTcojKg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)