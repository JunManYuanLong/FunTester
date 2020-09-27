# Groovy中的元组

Groovy元组是有序的，不变的元素列表。`Groovy`有自己的`groovy.lang.Tuple`类。我们可以通过构造函数Tuple提供需要包含在其中的所有元素来创建一个实例`Tuple`。我们不能将新元素添加到Tuple实例或删除元素。我们甚至不能更改元组中的元素，因此它是完全不变的。这使得它非常适合用作需要返回多个值的方法的返回值。`Groovy`还提供了一个`Tuple2`仅可用于两个元素的元组实例的类。元素在`Tuple2`实例中键入。

在以下示例中，我们看到`Tuple`和`Tuple2`类的不同用法：


```Groovy
package com.FunTester.demo

import com.fun.frame.SourceCode

class demo5 extends SourceCode {

    public static void main(String[] args) {
        def tuple = new Tuple('one', 1, getJson("demo=1"))
        println tuple.size() == 3
        println tuple.get(0) == 'one'
        println tuple[1] == 1
        println tuple.last().demo == 1
        //尝试修改tuple
        try {
            tuple.add('extra')
            println false
        } catch (Exception e) {
            println e
        }
        try {
            tuple.remove('one')
            println false
        } catch (Exception e) {
            println e
        }
        try {
            tuple[0] = 'new value'
            println false
        } catch (Exception e) {
            println e
        }
        //tuple2  Demo 到tuple9
        def pair = new Tuple2('two', 2)
        println pair.first == 'two'
        println pair.second == 2
        def tuple3 = new Tuple3("true", 3, 4)
        println tuple3.third

        def (String a, Integer b) = dd('sum', 1, 2, 3)
        println a == 'sum'
        println b == 6
    }

    static def dd(String key, int ... values) {
        new Tuple2(key, values.sum())
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

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDkiawpL3o8umv1EgHOc2OE1H8DtTMQSXWTOgFYPMSGtoX2BZlricBBJun4hMGUOJd7uibe68zQecRFw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)