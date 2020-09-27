# arthas快速入门视频演示

之前介绍过了`arthas`这个阿里的开源Java诊断工具，实在是非常好用。这里赶紧补上了快速入门的视频。

基本内容就是，介绍、安装、启动和几个基础命令。

官方文档地址如下：`https://alibaba.github.io/arthas/`

本期视频演示的具体内容如下：

快速入门
1. 启动Demo
2. 启动arthas
3. 查看dashboard
4. 通过thread命令来获取到进程
5. watch
6. 退出arthas

## arthas快速入门

- [点击观看视频](https://mp.weixin.qq.com/s/Wl5QMD52isGTRuAP4Cpo-A)

由于我并没有使用官方的演示`Demo`，自己随手写了一个，下面是代码：


```Groovy
package com.fun


import com.fun.frame.httpclient.FanLibrary
import com.fun.utils.RString

class sd extends FanLibrary {

    public static void main(String[] args) {

        while (true) {
            sleep(1000)
            RString.getStringWithoutNum(10)
            sleep(1000)
            String url = "https://api.apiopen.top/getAllUrl"
            def get = getHttpGet(url)
            def response = getHttpResponse(get)
        }
        testOver()
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
