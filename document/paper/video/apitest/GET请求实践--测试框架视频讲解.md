# GET请求实践--测试框架视频讲解

讲完json对象的操作，今天开始正式进入正题——接口测试。这里的接口指的是HTTP接口测试，主要的请求方法是`GET`和`POST`，下面开始讲`GET`请求的测试实践。

视频专题：

- [FunTester测试框架视频讲解（序）](https://mp.weixin.qq.com/s/CJrHAAniDMyr5oDXYHpPcQ)
- [获取HTTP请求对象--测试框架视频讲解](https://mp.weixin.qq.com/s/hG89sGf96GcPb2hGnludsw)
- [发送请求和解析响应—测试框架视频解读](https://mp.weixin.qq.com/s/xUQ8o3YuZOChXZ2UGR1Kyw)
- [json对象基本操作--视频讲解](https://mp.weixin.qq.com/s/MQtcIGKwWGEMb2XD3zmAIQ)

## 内容概述

以腾讯天气中一个获取某地天气信息详情的接口为案例，演示如何构造HTTP请求对象，如何把参数组装到请求对象中，如何解析响应结果，获取信息，进行校验等等。由于第三方接口，这里没有问题，代码比较简单，相当于文档了。

中间本来想讲讲断言的，发现很久不写，有点翻车，索性放弃了，各位见谅。


## GET请求测试实践

- [点击观看视频](https://mp.weixin.qq.com/s/_ZEDmRPXe4SLjCgdwDtC7A)

**gitee地址：https://gitee.com/fanapi/tester**

## 测试代码


```Java
package com.fun;

import com.alibaba.fastjson.JSONObject;
import com.fun.frame.httpclient.FanLibrary;
import org.apache.http.client.methods.HttpGet;

public class WeacherTest extends FanLibrary {

    public static void main(String[] args) {
        String url = "https://wis.qq.com/weather/common";
        JSONObject params = new JSONObject();
        params.put("source", "pc");
        params.put("province", "北京市");
        params.put("city", "北京市");
        params.put("county", "西城区");
        params.put("weather_type", "observe|forecast_1h|forecast_24h|index|alarm|limit|tips|rise");
        HttpGet httpGet = getHttpGet(url, params);
        JSONObject response = getHttpResponse(httpGet);
        output(response);
        output("响应状态码:" + response.getInteger("status"));
//        output("响应信息:" + response.getString("message"));
//        JSONObject info = response.getJSONObject("data").getJSONObject("forecast_24h");
//        Set<String> keySet = info.keySet();
//        for (String key : keySet) {
//            int max_degree = info.getJSONObject(key).getIntValue("max_degree");
//            String time = info.getJSONObject(key).getString("time");
//            output(time + "最高气温:" + max_degree + "摄氏度");
//        }


        testOver();
    }


}
```



---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [压测中测量异步写入接口的延迟](https://mp.weixin.qq.com/s/odvK1iYgg4eRVtOOPbq15w)
- [多种登录方式定量性能测试方案](https://mp.weixin.qq.com/s/WuZ2h2rr0rNBgEvQVioacA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [左移测试](https://mp.weixin.qq.com/s/8zXkWV4ils17hUqlXIpXSw)
- [选择手动测试还是自动化测试？](https://mp.weixin.qq.com/s/4haRrfSIp5Plgm_GN98lRA)
- [自动化测试项目为何失败](https://mp.weixin.qq.com/s/KFJXuLjjs1hii47C1BH8PA)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzBiaBZzt2rchWvBn0pztDTcYwUrHyWvCCIxiaHORQ1xe1vID42zWVicABw6dHibFChrlbFqVR5vO96eVQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)