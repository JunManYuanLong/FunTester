# 运行越来越快的Java热点代码

对于程序来说，通常只有一部分代码会被经常执行，而应用的性能主要取决于这些代码执行得有多快。这些关键代码段被称为应用的热点代码，代码执行得越多就被认为是越热。

因此JVM执行代码时，并不会无脑编译代码。第一，如果代码只执行一次，那编译完全就是浪费精力。对于只执行一次的代码，解释执行Java字节码比先编译然后执行的速度快。但如果代码是经常被调用的方法，编译就值得了：编译的代码更快，多次执行累积节约的时间远超过了编译所花费的时间。
 
这种权衡是编译器先解释执行代码的原因之一：编译器可以找出哪个方法被调用得足够频繁，可以进行编译。第二个理由是为了优化: JVM执行特定方法或者循环的次数越多，它就会越了解这段代码。这使得JVM可以在编译代码时进行大量优化。

测试Demo1，运行完全一模一样的代码，性能大概提升了为原来的1/16：

```Groovy
package com.fun


import com.fun.frame.SourceCode

class TSSS extends SourceCode {


    public static void main(String[] args) {

        def mark0 = getNanoMark();
        100.times {
            getPercent(23, 17)
        }
        def mark1 = getNanoMark()
        output(getFormatNumber(mark1 - mark0))


        def mark10 = getNanoMark();
        100.times {
            getPercent(23, 17)
        }
        def mark11 = getNanoMark()
        output(getFormatNumber(mark11 - mark10))


    }


}
```

控制台输出：


```Java
INFO-> 当前用户：fv，IP：192.168.0.100，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.3
INFO-> 161,422,326
INFO-> 10,559,361

Process finished with exit code 0

```

测试Demo2，运行相同的方法，随机的参数，性能提升为原来的1/2：


```Groovy
package com.fun


import com.fun.frame.SourceCode

class TSSS extends SourceCode {


    public static void main(String[] args) {

        def mark0 = getNanoMark();
        100.times {
            getPercent(getRandomInt(100), getRandomInt(30))
        }
        def mark1 = getNanoMark()
        output(getFormatNumber(mark1 - mark0))


        def mark10 = getNanoMark();
        100.times {
            getPercent(getRandomInt(100), getRandomInt(30))
        }
        def mark11 = getNanoMark()
        output(getFormatNumber(mark11 - mark10))


    }


}

```

控制台输出：


```Java
INFO-> 当前用户：fv，IP：192.168.0.100，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.3
INFO-> 197,810,630
INFO-> 9,012,579

Process finished with exit code 0

```

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

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDkiawpL3o8umv1EgHOc2OE1iaib1vR7Q6DH1FSpP4HVuibsibicftEqUqfXZpE2FyN7nIPvHwhWQdG6n0g/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)