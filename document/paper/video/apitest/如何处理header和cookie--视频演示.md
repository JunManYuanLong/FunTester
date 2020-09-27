# 如何处理header和cookie--视频演示

终于把基础的请求讲完了，感觉还算顺利，希望读者们也能从中得到一些对自己有帮助的东西。会者不难难者不会，每次二十多分钟很快能把Demo讲完，如果想要掌握接口测试，还要多写代码，多实践。

相信一万行代码的理论！

视频专题：

- [FunTester测试框架视频讲解（序）](https://mp.weixin.qq.com/s/CJrHAAniDMyr5oDXYHpPcQ)
- [获取HTTP请求对象--测试框架视频讲解](https://mp.weixin.qq.com/s/hG89sGf96GcPb2hGnludsw)
- [发送请求和解析响应—测试框架视频解读](https://mp.weixin.qq.com/s/xUQ8o3YuZOChXZ2UGR1Kyw)
- [json对象基本操作--视频讲解](https://mp.weixin.qq.com/s/MQtcIGKwWGEMb2XD3zmAIQ)
- [GET请求实践--测试框架视频讲解](https://mp.weixin.qq.com/s/_ZEDmRPXe4SLjCgdwDtC7A)
- [POST请求实践--视频演示](https://mp.weixin.qq.com/s/g0mLzMQ4Br2e592m3p68eg)

本次分享如何处理`header`和`cookie`，一般来讲，如果是自动化测试项目，这些数据的处理都是通过测试框架完成的，很少手动测试。这里演示的Demo，比较适合的就是比较独立的测试任务场景。

## 如何处理header和cookie

- [点击观看视频](https://mp.weixin.qq.com/s/MkwzT9VPglSnOxY7geSUiQ)

----
**gitee地址：https://gitee.com/fanapi/tester**

## 代码


```Java
package com.fun;


import com.alibaba.fastjson.JSONObject;
import com.fun.config.HttpClientConstant;
import com.fun.frame.httpclient.FanLibrary;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;

public class AR extends FanLibrary {

    public static String APIKEY;

    public static void main(String[] args) {
        printHeader();
        developerLogin();
//        registerUser();


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
        httpPost.addHeader(HttpClientConstant.X_Requested_KWith);
        httpPost.addHeader(getHeader("name", "FunTester"));
//        JSONObject cookie = new JSONObject();
//        cookie.put("name", "FunTester");
//        cookie.put("pwd", "FunTester");
//        cookie.put("age", "22");
//        Header cookies = getCookies(cookie);
        String dd = "_zap=3f36e41c-ea6e-4436-892e-3509be0a60be; _xsrf=0LCDSGzBzIxS6kWPEmf94J8KtpfmFti1; ISSW=1; d_c0=\"AADZkMsmABGPTqeAzeeGiltFGWN_rrYKq6s=|1584847946\"; _ga=GA1.2.910125274.1584847946; _gid=GA1.2.455967142.1584847946; capsion_ticket=\"2|1:0|10:1584847947|14:capsion_ticket|44:M2IwNmRhNmQ1MGViNGQ3Y2E4MDU0OTYwZjQ5ZDU5NDA=|33ef00769fdd6874a735867c43b2a0d0265c1ec8f9e106c2eb0d4805c64e4c8f\"; z_c0=\"2|1:0|10:1584847951|4:z_c0|92:Mi4xbDBITUFRQUFBQUFBQU5tUXl5WUFFU1lBQUFCZ0FsVk5UeXBrWHdCbFMxOGNwajI0QXgzMldCYTNqaGd3NmpkTlh3|915961266a0495f8ad137d24f81a0fe1020b8712019d6d8d355e4f9d65159868\"; tshl=; tst=r; q_c1=f36a39fa82744e4992cd32c16194783d|1584879682000|1584879682000; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1584847946,1584945282; _gat_gtag_UA_149949619_1=1; Hm_lpvt_98beee57fd2ef70ccdd5ca52b9740c49=1584953227; KLBRSID=9d75f80756f65c61b0a50d80b4ca9b13|1584953228|1584953191";
//        httpPost.addHeader(cookies);
        httpPost.addHeader(HttpClientConstant.COOKIE, dd);
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

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzBiaBZzt2rchWvBn0pztDTcYwUrHyWvCCIxiaHORQ1xe1vID42zWVicABw6dHibFChrlbFqVR5vO96eVQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)