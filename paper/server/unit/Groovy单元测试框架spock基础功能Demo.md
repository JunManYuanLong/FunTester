# Groovy单元测试框架spock基础功能Demo

> spock是一款全能型的单元测试框架。

最近在做单元测试框架的调研和尝试，目前确定的方案框架包括是：spock，Junit，Mockito以及powermock。由于本身使用Groovy的原因，比较钟情于spock框架，但是奈何兼容性比较差，特别是跟Mockito等框架的高级语法的兼容。不过这不妨碍spock是一个非常优秀的单元测试框架，特别体现在用例的形式和测试报告的展示方式以及报错信息的展示（这个我最中意）。

在简单看过官方文档之后做了一些简单的Demo，分享给大家。（官方文档貌似有段时间没有更新了，如果用的话建议升级最新版）


```
package com.FunTester.spock.pratice

import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.fun.frame.SourceCode.getLogger

class test01 extends Specification {

    @Shared
    int a = 10;

    @Shared
    Logger logger = getLogger(this.getClass().getName())

    def setupSpec() {
        logger.info "测试类开始! ${logger.getName()}"
    }

    def setup() {
        logger.info "测试方法开始！"
    }

    def "这是一个测试"() {
        given: "准备测试数据"
        def s = 3
        def ss = 3
        expect: "验证结果"
        s == ss
    }

    def "表达式测试，表达式左右运算符号"() {
        given: "准备数据"

        expect: "测试方法"
        z == x + y

        where: "校验结果"
        x | y || z
        1 | 0 || 1
        2 | 1 || 3
    }

    def "表达式测试，表达式左右对象方法"() {
        expect:
        name.size() == length
        where:
        name      | length
        "Spock"   | 5
        "Kirk"    | 4
        "Scotty"  | 6
        "Sc3otty" | 7
    }

    def "表达式测试，表达式左右对象方法,数组表示测试数据"() {
        expect:
        name.size() == length
        where:
        name << ["fjdslj", "fds"]
        length << [6, 3]
    }

    def "校验对象"() {
        given:
        def per = new Per("fun", 12)
        expect:
        with(per) {
            name == "fun"
            age == 12
        }
    }


    def "when then结构测试"() {
        when:
        def s = plus(3, 2)
        def ss = plus(3, 2)
        then:
        verifyAll {
            s == 3
            ss == 3
        }
    }

/**
 * 测试方法
 * @param i
 * @param j
 * @return
 */
    def plus(int i, int j) {
        i
    }

/**
 * 测试类
 */
    class Per {

        Per(String name, int age) {
            this.name = name
            this.age = age
        }

        String name

        int age


    }
}
```

展示一下spock的测试报告：

![spock测试报告](/Users/fv/Documents/fan/blogPic/QQ20191111-155811.png)
![spock测试报告](/Users/fv/Documents/fan/blogPic/QQ20191111-155550.png)

* 有没有惊喜！！！

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [JUnit中用于Selenium测试的中实践](https://mp.weixin.qq.com/s/KG4sltQMCfH2MGXkRdtnwA)

## 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [自动化和手动测试，保持平衡！](https://mp.weixin.qq.com/s/mMr_4C98W_FOkks2i2TiCg)
- [自动化测试生命周期](https://mp.weixin.qq.com/s/SH-vb2RagYQ3sfCY8QM5ew)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)

## 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
- [JVM的对象和堆](https://mp.weixin.qq.com/s/iNDpTz3gBK3By_bvUnrWOA)

![长按关注](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)