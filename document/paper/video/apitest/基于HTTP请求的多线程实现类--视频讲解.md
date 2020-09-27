# 基于HTTP请求的多线程实现类--视频讲解

> 相信一万行代码的理论！

上期将了定时和定量两种压测模式的虚拟类，本期分享一下基于单个`HTTP`请求对象`HTTPrequestbase`的两个压测模式的具体实现类。比较关键的就是`GCThread`的启动和结束，还有就是`doing()`方法的实现，就是把`HTTPrequestbase`对象发送请求然后解析响应，这里并没有去管响应结果的校验和断言，原因就是比较复杂，需要具体情况具体处理，难以通过一个通用的方法校验，还有一个原因就是很多时候没必要，可以通过监控服务端日志和其他统计方式统计相关业务数据来达到判断所有请求是否有报错和不成功的请求。其中应该着重注意就是对象拷贝，不管是`多线程类对象`还是`HTTPrequestbase`对象，如果不实现`clone()`方法，可能会有BUG。

性能测试系列视频如下：

- [性能框架多线程基类和执行类--视频讲解](https://mp.weixin.qq.com/s/8Dh-5XfvX8Fm4IqmzbtY6Q)
- [定时和定量压测模式实现--视频讲解](https://mp.weixin.qq.com/s/l_4wCjVM1fAVRHgEPrcrwg)

* 接口测试视频基础部分已经录完了，**后台回复“接口视频”可观看完整接口测试视频**。


## 基于HTTP请求的多线程实现类

- [点击观看视频](https://mp.weixin.qq.com/s/8SG1xtzq8ArY84Bxm_SNow)

----
**gitee地址：https://gitee.com/fanapi/tester**

## 定量模式


```Java
package com.fun.frame.thead;

import com.fun.base.constaint.ThreadLimitTimesCount;
import com.fun.base.interfaces.MarkThread;
import com.fun.frame.httpclient.FanLibrary;
import com.fun.frame.httpclient.FunRequest;
import com.fun.frame.httpclient.GCThread;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http请求多线程类
 */
public class RequestThreadTimes extends ThreadLimitTimesCount {

    static Logger logger = LoggerFactory.getLogger(RequestThreadTimes.class);

    /**
     * 请求
     */
    public HttpRequestBase request;

    /**
     * 单请求多线程多次任务构造方法
     *
     * @param request 被执行的请求
     * @param times   每个线程运行的次数
     */
    public RequestThreadTimes(HttpRequestBase request, int times) {
        super(null, times, null);
        this.request = request;
    }

    /**
     * 应对对每个请求进行标记的情况
     *
     * @param request
     * @param times
     * @param mark
     */
    public RequestThreadTimes(HttpRequestBase request, int times, MarkThread mark) {
        super(null, times, mark);
        this.request = request;
    }

    protected RequestThreadTimes() {
        super();
    }

    @Override
    public void before() {
        super.before();
        GCThread.starts();
    }

    /**
     * @throws Exception
     */
    @Override
    protected void doing() throws Exception {
        FanLibrary.excuteSimlple(request);
    }

    @Override
    public RequestThreadTimes clone() {
        RequestThreadTimes threadTimes = new RequestThreadTimes();
        threadTimes.times = this.times;
        threadTimes.request = FunRequest.cloneRequest(request);
        threadTimes.mark = mark == null ? null : mark.clone();
        return threadTimes;
    }


}

```

## 定时模式


```Java
package com.fun.frame.thead;

import com.fun.base.constaint.ThreadLimitTimeCount;
import com.fun.base.interfaces.MarkThread;
import com.fun.frame.httpclient.FanLibrary;
import com.fun.frame.httpclient.FunRequest;
import com.fun.frame.httpclient.GCThread;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http请求多线程类
 */
public class RequestThreadTime extends ThreadLimitTimeCount {

    static Logger logger = LoggerFactory.getLogger(RequestThreadTime.class);

    /**
     * 请求
     */
    public HttpRequestBase request;

    /**
     * 单请求多线程多次任务构造方法
     *
     * @param request 被执行的请求
     * @param time    每个线程运行的次数
     */
    public RequestThreadTime(HttpRequestBase request, int time) {
        super(null,time,null);
        this.request = request;
    }

    /**
     * @param request 被执行的请求
     * @param time    执行时间
     * @param mark    标记类对象
     */
    public RequestThreadTime(HttpRequestBase request, int time, MarkThread mark) {
        super(null,time, mark);
        this.request = request;
    }

    protected RequestThreadTime() {
        super();
    }

    @Override
    public void before() {
        super.before();
        GCThread.starts();
    }

    @Override
    protected void doing() throws Exception {
        FanLibrary.excuteSimlple(request);
    }


    @Override
    public RequestThreadTime clone() {
        RequestThreadTime threadTime = new RequestThreadTime();
        threadTime.time = this.time;
        threadTime.request = FunRequest.cloneRequest(request);
        threadTime.mark = mark == null ? null : mark.clone();
        return threadTime;
    }


}

```

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [多项目登录互踢测试用例](https://mp.weixin.qq.com/s/Nn_CUy_j7j6bUwHSkO0pCQ)
- [从Java到Groovy的八级进化论](https://mp.weixin.qq.com/s/QTrRHsD3w-zLGbn79y8yUg)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [简化测试用例](https://mp.weixin.qq.com/s/BhwfDqhN9yoa3Iul_Eu5TA)
- [软件测试中的虚拟化](https://mp.weixin.qq.com/s/zHyJiNFgHIo2ZaPFXsxQMg)
- [生产环境中进行自动化测试](https://mp.weixin.qq.com/s/JKEGRLOlgpINUxs-6mohzA)
- [所谓UI测试](https://mp.weixin.qq.com/s/wDvUy_BhQZCSCqrlC2j1qA)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
