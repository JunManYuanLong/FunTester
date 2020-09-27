# JSONArray基本操作--视频演示

> 相信一万行代码的理论！

之前讲过了一期[json对象基本操作--视频讲解](https://mp.weixin.qq.com/s/MQtcIGKwWGEMb2XD3zmAIQ)，中间对JSONArray的操作没讲清楚，特意补了一期视频，欢迎大家多提意见，共同进步。

视频专题：

- [FunTester测试框架视频讲解（序）](https://mp.weixin.qq.com/s/CJrHAAniDMyr5oDXYHpPcQ)
- [获取HTTP请求对象--测试框架视频讲解](https://mp.weixin.qq.com/s/hG89sGf96GcPb2hGnludsw)
- [发送请求和解析响应—测试框架视频解读](https://mp.weixin.qq.com/s/xUQ8o3YuZOChXZ2UGR1Kyw)
- [json对象基本操作--视频讲解](https://mp.weixin.qq.com/s/MQtcIGKwWGEMb2XD3zmAIQ)
- [GET请求实践--测试框架视频讲解](https://mp.weixin.qq.com/s/_ZEDmRPXe4SLjCgdwDtC7A)
- [POST请求实践--视频演示](https://mp.weixin.qq.com/s/g0mLzMQ4Br2e592m3p68eg)
- [如何处理header和cookie--视频演示](https://mp.weixin.qq.com/s/MkwzT9VPglSnOxY7geSUiQ)
- [FunRequest类功能--视频演示](https://mp.weixin.qq.com/s/WGS6ZwAvw7X4MC004Gz4pA)
- [接口测试业务验证--视频演示](https://mp.weixin.qq.com/s/DH8HDmaritXQnkBIFOadoA)


## 接口测试中业务验证

- [点击观看视频](https://mp.weixin.qq.com/s/OosDbRoknMe1riaPc3hhLg)

----
**gitee地址：https://gitee.com/fanapi/tester**

## 代码


```Java
package com.fun;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fun.frame.SourceCode;
import org.slf4j.Logger;

public class Fun extends SourceCode {

    public static Logger logger = getLogger(Fun.class);
    public static void main(String[] args) {
        JSONObject json = new JSONObject();
        json.put("22", "fdskjflsj");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("2", 3243);
        jsonObject.put("213", 213243);
        jsonObject.put("32", 321243);
        JSONArray array = new JSONArray();
        array.add(jsonObject.clone());
        JSONObject clone =(JSONObject) jsonObject.clone();
        clone.put("323", "323");
        array.add(clone);
        array.add(jsonObject.clone());
        json.put("array", array);
        output(json);
        output(json.toString());
        String ss = "{\"22\":\"fdskjflsj\",\"array\":[{\"2\":3243,\"213\":213243,\"32\":321243},{\"2\":3243,\"213\":213243,\"323\":\"323\",\"32\":321243},{\"2\":3243,\"213\":213243,\"32\":321243}]}";
        JSONObject jsonObject1 = JSONObject.parseObject(ss);
        jsonObject1.put("22", "分");
        output(jsonObject1);
    }


}

```

---
* **郑重声明**：文章首发于公众号“FunTester”，欢迎关注交流，禁止第三方转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [压测中测量异步写入接口的延迟](https://mp.weixin.qq.com/s/odvK1iYgg4eRVtOOPbq15w)
- [多种登录方式定量性能测试方案](https://mp.weixin.qq.com/s/WuZ2h2rr0rNBgEvQVioacA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [多项目登录互踢测试用例](https://mp.weixin.qq.com/s/Nn_CUy_j7j6bUwHSkO0pCQ)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [选择手动测试还是自动化测试？](https://mp.weixin.qq.com/s/4haRrfSIp5Plgm_GN98lRA)
- [自动化测试项目为何失败](https://mp.weixin.qq.com/s/KFJXuLjjs1hii47C1BH8PA)
- [简化测试用例](https://mp.weixin.qq.com/s/BhwfDqhN9yoa3Iul_Eu5TA)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzBiaBZzt2rchWvBn0pztDTcYwUrHyWvCCIxiaHORQ1xe1vID42zWVicABw6dHibFChrlbFqVR5vO96eVQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)