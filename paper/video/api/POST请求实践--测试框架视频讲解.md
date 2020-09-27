# POST请求实践--测试框架视频讲解

讲完get，轮到post请求了，本期分享了post请求的实现，分享了一些参数依赖的情况。录制过程中翻车了好几次，各位见谅。

视频专题：

- [FunTester测试框架视频讲解（序）](https://mp.weixin.qq.com/s/CJrHAAniDMyr5oDXYHpPcQ)
- [获取HTTP请求对象--测试框架视频讲解](https://mp.weixin.qq.com/s/hG89sGf96GcPb2hGnludsw)
- [发送请求和解析响应—测试框架视频解读](https://mp.weixin.qq.com/s/xUQ8o3YuZOChXZ2UGR1Kyw)
- [json对象基本操作--视频讲解](https://mp.weixin.qq.com/s/MQtcIGKwWGEMb2XD3zmAIQ)
- [GET请求实践--测试框架视频讲解](https://mp.weixin.qq.com/s/_ZEDmRPXe4SLjCgdwDtC7A)

## 内容概述

今天主要讲了post接口的相关测试，前半段主题内容跟上期一致，演示了post请求的Demo，中间翻车好几次，幸亏接口文档比较简单。要是遇到复杂逻辑业务，一次性上车几乎是不可能的。

后半段分享了一个接口测试如何处理校验值，参数依赖等等，只是个简单的Demo，适合短期测试项目，写完就用，用完就扔的模式，用来做练习很不错。之前做过一些活动和游戏的测试，每周上线一个游戏活动，然后下线，软件工期非常短，这种就比较适合今天讲的模式。对于一个长期项目如何做接口测试以及如何接口自动化，后会有期了。

## post接口请求和基本业务验证

- [点击观看视频](https://mp.weixin.qq.com/s/g0mLzMQ4Br2e592m3p68eg)



**gitee地址：https://gitee.com/fanapi/tester**

## 代码


```Java
package com.fun;


import com.alibaba.fastjson.JSONObject;
import com.fun.frame.httpclient.FanLibrary;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;

public class AR extends FanLibrary {

    public static String APIKEY;

    public static void main(String[] args) {
//        developerLogin();
        registerUser();


        testOver();
    }

    public static void registerUser() {
        if (StringUtils.isEmpty(APIKEY)) developerLogin();
        String url = "https://api.apiopen.top/registerUser";
        JSONObject param = new JSONObject();
        param.put("apikey", APIKEY);
        param.put("name", "FunTester0021");
        param.put("passwd", "123456");
        param.put("nikeName", "FunTester");
        param.put("headerImg", "http://pic.automancloud.com/sick-jvm-heap-1.png");
        param.put("phone", "13100001111");
        param.put("email", "Fhaohaizi@163.com");
        param.put("vipGrade", "3");
        param.put("autograph", "abc");
        param.put("remarks", "这是测试用户!");
        HttpPost httpPost = getHttpPost(url, param);
        JSONObject response = getHttpResponse(httpPost);
        output(response);
    }

    public static void register() {
        String url = "https://api.apiopen.top/developerRegister";
        JSONObject param = new JSONObject();
        param.put("name", "FunTester");
        param.put("passwd", "FunTester");
        param.put("email", "Fhaohaizi@163.com");
        HttpPost httpPost = getHttpPost(url, param);
        JSONObject response = getHttpResponse(httpPost);
        output(response);
    }

    public static void developerLogin() {
        String url = "https://api.apiopen.top/developerLogin";
        JSONObject params = new JSONObject();
        params.put("name", "funtester");
        params.put("passwd", "funtester");
        HttpPost httpPost = getHttpPost(url, params);
        JSONObject response = getHttpResponse(httpPost);
        output(response);
        if (response.getIntValue("code") == 200) {
            APIKEY = response.getJSONObject("result").getString("apikey");
        } else {
            fail();
        }
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


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDkiawpL3o8umv1EgHOc2OE1H8DtTMQSXWTOgFYPMSGtoX2BZlricBBJun4hMGUOJd7uibe68zQecRFw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)