# groovy如何使用java接口测试框架发送http请求
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---


本人在使用java写框架做http接口测试的过程中，经过大神指点思路，发现用例还是要用脚本语言来做会更加有利于后期的用例执行和用例管理。最近在研究大神推荐的groovy脚本语言，略有一些小成绩。下面分享一下如何在groovy里面使用自己写的基于java的接口测试框架来发送http请求的方法。groovy的文档发送http请求个人感觉略微麻烦了，而且我已经封装好了发送和接受请求的方法，以及一些其他功能。

思路如下：把写好的框架打包jar包，然后再groovy里引入，直接使用框架的方法发送http请求，并做响应的处理。

分享groovy代码：


```
import net.sf.json.JSONObject
import org.apache.http.client.methods.HttpGet
import source.FanLibrary
 
class one extends FanLibrary {
    static void main(String[] args) {
        def httpGet = new HttpGet("http://cost-api.fissionpie.com/cost?_t=200&_app=1&_v=1.0.0&token=601_200_1524035146010_d78096e246592dfa&userId=56&type=1&maxId=0&length=10");
        def response = getHttpResponseEntityByJson(httpGet);
        println response
    }
}
```
下面是返回结果：
![](/blog/pic/2018041817172483.png)

### 往期文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
8. [将json数据格式化输出到控制台](https://mp.weixin.qq.com/s/2IPwvh-33Ov2jBh0_L8shA)
9. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
10. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
11. [Mac+httpclient高并发配置实例](https://mp.weixin.qq.com/s/r4a-vGz0pxeZBPPH3phujw)
12. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)

## [公众号地图](https://mp.weixin.qq.com/s/36RbP20beZ8oWJ9nLAxG3g) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>