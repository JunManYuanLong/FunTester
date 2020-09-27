# Groovy单元测试框架spock数据驱动Demo

> spock是一款全能型的单元测试框架。

上次文章分享了spock框架的基础功能的使用，在此基础上，我根据自己写的Groovy的封装方法、数据驱动以及一些Groovy的高级语法做了一些尝试。发现还是有一点点问题，不知道是不是因为我本身是Java和Groovy混编的项目导致的，不过也都有了解决方案。

分享代码，供各位参考：


```
package com.FunTester.spock.pratice

import com.fun.config.PropertyUtils
import com.fun.frame.SourceCode
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.fun.config.Constant.EMPTY
import static com.fun.config.Constant.getLongFile
import static com.fun.frame.Output.output
import static com.fun.frame.SourceCode.*

class Test02 extends Specification {

    @Shared
    def properties = PropertyUtils.getLocalProperties(getLongFile("1"))

    @Shared
    def cc = Arrays.asList(properties.getArrays("c")).stream().map {x -> Integer.valueOf(x)}.collect() as List

    @Shared
    def bb = Arrays.asList(properties.getArrays("b")).stream().map {x -> Integer.valueOf(x)}.collect() as List

    @Shared
    def aa = Arrays.asList(properties.getArrays("a")).stream().map {x -> Integer.valueOf(x)}.collect() as List

    @Shared
    Logger logger = getLogger(Test02.class.getName())

    def setup() {
        logger.info("测试方法开始了")
    }

    def cleanup() {
        logger.info("测试方法结束了")
    }

    def setupSpec() {
        logger.info("测试类[${getClass().getName()}]开始了")
    }

    def cleanupSpec() {
        logger.info("测试类[${getClass().getName()}]结束了")
    }

    def "测试数据驱动Demo"() {
        given:
        int c = 0

        expect:
        10 == a + b + c

        where:
        a | b
        1 | 9
        8 | 2
    }

    def "测试数据读写完成数据驱动"() {
        given:
        def a = 0
        def arrays = properties.getArrays("id")
        def s1 = properties.getPropertyInt("size1")
        def s2 = properties.getPropertyInt("size2")
        def list = Arrays.asList(arrays).stream().filter {x -> x.length() > 1}.collect() as List

        expect:
        s1 == arrays.size()
        s2 == list.size()
    }

    def "测试自定义对象操作"() {
        given: "给一个自定义对象"
        def per = new Per()
        per.age = 23
        per.name = "FunTester"
        def a = per

        expect:
        a.name == "FunTester"

    }

    def "线程安全测试"() {
        given: "多线程支持测试,此处线程数改成很大之后效果比较明显"
        range(2).forEach {new Per().start()}
        sleep(1000)
        output(Per.i)
        expect:
        4 == Per.i
    }

    def "测试集合验证使用数据驱动"() {
        given: "此处写的无法被where使用"

        expect:
        c * c == a * a + b * b

        where:
        c << cc
        b << bb
        a << aa
    }

    def "测试数组0..10方式是否可用"() {
        expect:
        true == SourceCode.isNumber(x + EMPTY)

        where: "需要用括号,不然会报错"
        x << (0..5)

    }

    def "测试lambda写法是否可用"() {
        given:
        def collect =  range(10).filter {x -> x % 2 == 1}.collect() as List

        expect:
        collect.size() == 5
        collect.contains(3)
        collect.contains(5)
    }


/**
 * 测试类
 */
    class Per extends Thread {

        static int i

        @Override
        public void run() {
            i++
            sleep(100)
            i++
        }

        Per() {
        }

        Per(String name, int age) {
            this()
            this.name = name
            this.age = age
        }

        String name

        int age

        static def getAll() {
            i
        }

    }

}
```

下次我会针对自己写的工具类和封装的请求对象进行一些spock方面的代码演示，欢迎各位关注。

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

![长按关注](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)