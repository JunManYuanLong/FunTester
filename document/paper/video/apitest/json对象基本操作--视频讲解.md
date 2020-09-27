# json对象基本操作--视频讲解

经小伙伴的提醒，决定插播一期json对象基本操作的视频。由于我录视频没有很充分的前期准备，因为太费时间了，所以都是想到什么内容，打个腹稿，准备一下设备就开始`喷`了。欢迎多提意见。

视频专题：

- [FunTester测试框架视频讲解（序）](https://mp.weixin.qq.com/s/CJrHAAniDMyr5oDXYHpPcQ)
- [获取HTTP请求对象--测试框架视频讲解](https://mp.weixin.qq.com/s/hG89sGf96GcPb2hGnludsw)
- [发送请求和解析响应—测试框架视频解读](https://mp.weixin.qq.com/s/xUQ8o3YuZOChXZ2UGR1Kyw)

由于公众号视频长度限制，我分成了两个视频。一个是json对象创建和添加数据，一个是从json对象获取数据和格式化输出。第二个视频讲格式化输出的时候有个地方澄清一下，就是`jsonStr = jsonStr.replaceAll("\\\\/", OR);`这行代码，主要是处理接口响应结果中包含`\/\/`这种数据的。

中间提到的深浅拷贝和控制台输出方法的内容可以查看往期文章：

- [从JVM堆内存分析验证深浅拷贝](https://mp.weixin.qq.com/s/SdYDnoau1rjjvPC2SUymBg)
- [将json数据格式化输出到控制台](https://mp.weixin.qq.com/s/2IPwvh-33Ov2jBh0_L8shA)

## 新建json对象添加数据

- [点击观看视频](https://mp.weixin.qq.com/s/MQtcIGKwWGEMb2XD3zmAIQ)


## 获取json中数据和格式化输出

- [点击观看视频](https://mp.weixin.qq.com/s/MQtcIGKwWGEMb2XD3zmAIQ)


**gitee地址：https://gitee.com/fanapi/tester**

## Demo代码：


```Java
package com.fun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fun.frame.SourceCode;

public class Fun extends SourceCode {

    public static void main(String[] args) {
//        JSONObject json = new JSONObject();
//        json.put("2", 323333);
//        json.put("22", "fdskjflsj");
//        json.put("222", false);
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("2", 3243);
//        jsonObject.put("213", 213243);
//        jsonObject.put("32", 321243);
//        json.put("221", jsonObject);
//        JSONArray array = new JSONArray();
//        array.add(jsonObject.clone());
//        array.add(jsonObject.clone());
//        array.add(jsonObject.clone());
//        json.put("array", array);
//        output(json);
//        output(json.toString());
        String jsonstr = "{\"22\":\"fdskjflsj\",\"221\":{\"2\":3243,\"213\":213243,\"32\":321243},\"2\":323333,\"222\":false,\"array\":[{\"2\":3243,\"213\":213243,\"32\":321243},{\"2\":3243,\"213\":213243,\"32\":321243},{\"2\":3243,\"213\":213243,\"32\":321243}]}";
        JSONObject jsonObject1 = JSON.parseObject(jsonstr);
        jsonObject1.put("array", "1/2");
//        output(jsonObject1.equals(json));
        output(jsonObject1);
        output(jsonObject1.getBooleanValue("222"));
        output(jsonObject1.getJSONObject("221"));
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


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCxr0Sa2MXpNKicZE024zJm7vIAFRC09bPV9iaMer9Ncq8xppcYF73sDHbrG2iaBtRqCFibdckDTcojKg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDkiawpL3o8umv1EgHOc2OE1H8DtTMQSXWTOgFYPMSGtoX2BZlricBBJun4hMGUOJd7uibe68zQecRFw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDkiawpL3o8umv1EgHOc2OE1iaib1vR7Q6DH1FSpP4HVuibsibicftEqUqfXZpE2FyN7nIPvHwhWQdG6n0g/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
