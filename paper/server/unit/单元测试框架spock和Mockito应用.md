# 单元测试框架spock和Mockito应用

# 先介绍一下两位主角

`spock`是一款基于Groovy语言的单元测试框架，其基础也是`Java`的`Junit`，目前最新版已经到了`2.0`，但对`Groovy`和响应的`Java`版本要求较高，具体信息参考：[Spock 2.0 M1版本初探](https://mp.weixin.qq.com/s/nyYh2QzER03kIk-w9P9GNw)。

`Mockito`是一个模拟测试框架，可以让你用优雅，简洁的接口写出漂亮的单元测试。Mockito可以让单元测试易于可读，产生简洁的校验错误。[TDD测试驱动开发](https://mp.weixin.qq.com/s/diW_2HSbWMEsn8G6uQriOg)要求我们先写单元测试，再写实现代码。在写单元测试的过程中，由于各种依赖的关系导致的阻碍，我们必需用到`Mockito`类似的框架来完成资源、对象的模拟。

# Gradle配置


```Groovy
    testCompile 'org.mockito:mockito-core:2.7.22'
    testCompile group: 'org.spockframework', name: 'spock-core', version: '1.3-groovy-2.5'
    testCompile group: 'org.springframework', name: 'spring-test', version: '5.1.9.RELEASE'
    testCompile group: 'org.hamcrest', name: 'hamcrest-library', version: '1.3'
    testCompile group: 'junit', name: 'junit', version: '4.12'
    testCompile group: 'org.powermock', name: 'powermock-module-junit4', version: '2.0.0'
    testCompile group: 'org.powermock', name: 'powermock-api-mockito2', version: '2.0.2'
```

# Demo代码

下面是演示代码：


```Groovy
package com.FunTester.mockito.practise

import org.apache.http.client.methods.HttpRequestBase
import org.slf4j.Logger
import spock.lang.Shared
import spock.lang.Specification

import static com.fun.config.Constant.SPACE_1
import static com.fun.frame.SourceCode.getLogger
import static org.mockito.AdditionalAnswers.returnsFirstArg
import static org.mockito.Matchers.anyInt
import static org.mockito.Mockito.*

class Demo extends Specification {

    @Shared
    Logger logger = getLogger(this.getClass().getName())

    @Shared
    List listsss = mock(List)

    @Shared
    HttpRequestBase httpRequestBase = mock(HttpRequestBase.class)

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

    def "这是一个普通的demo"() {
        given:"创建一个存根list,添加一些元素"
        List mockedList = mock(List.class);
        mockedList.add("one");
        mockedList.add("two");
        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");
        when(mockedList.size()).thenReturn(5);
        mockedList.add("3")
        mockedList.add("3")
        mockedList.add("3")
        mockedList.add("3")

        expect:"验证属性以及方法调用次数"
        5 == mockedList.size()
        false == verify(mockedList, atLeastOnce()).add("one")
        false == verify(mockedList, times(3)).add("three times")
        false == verify(mockedList, atMost(4)).add("3")
        false == verify(mockedList, never()).add("30")
    }

    def "这是一个测试的mockito模拟方法返回"() {
        given: "虚拟一个迭代器对象"
        def iterator = mock(Iterator.class)
        when(iterator.next()).thenReturn("hello").thenReturn("world")

        expect: "测试迭代器元素拼接"
        "hello world" == iterator.next() + SPACE_1 + iterator.next()
    }

    def "这是一个测试,用来在对象初始化之后mock对象的"() {
        given: "创建对象后再Mockito"
        def iterator = new ArrayList()
        iterator.add("323")
        def list = spy(iterator)
        doReturn("fun").when(list).get(3)
        doReturn(3).when(list).get(0)

        expect:
        list.contains("323")
        "fun" == list.get(3)
        3 == list.get(0)
    }

    def "这是一个测试,抛出异常的测试用例"() {
        given: "创建测试对象"
        def object = mock(ArrayList.class)
        when(object.get(1)).thenThrow(new IndexOutOfBoundsException("我是测试"))//只能抛出可能的抛出的异常
        def re = 0
        try {
            object.get(1)
        } catch (IndexOutOfBoundsException e) {
            re = 1
        }

        expect:
        re == 1
    }

    def "这是一个测试方法返回值的用例"() {
        given:
        def j = mock(DemoJ.class)
        doAnswer(returnsFirstArg()).when(j).ds(anyInt(), anyInt())

//        when(list.add(anyString())).thenAnswer(returnsFirstArg());
        // with then() alias:
//        when(list.add(anyString())).then(returnsFirstArg());
        expect:
        3 == j.ds(3, 32)

    }

    def "我是测试共享Mock对象的用例"() {
        given:

        when(listsss.get(anyInt())).thenReturn(3)

        expect:
        3 == listsss.get(3)
    }

/**
 *      对于未指定mock的方法，spy默认会调用真实的方法，有返回值的返回真实的返回值，而mock默认不执行，有返回值的，默认返回null
 */
    def "spy和mock区别"() {
        given:
        def list = [1,2,3,4]
        def integers = spy(list)
        when(integers.size()).thenReturn(9)

        expect:
        integers.size() == 9
        integers.get(0) == 1

    }
}

```

* 经过我的测试，`Mockito`的基础功能在`spock`应用还是非常流畅的，但是一些高级语法还是无法使用，如果在实际项目中使用请多调研两者差别，大概率还是要混合编程。

参考文章：

- [Maven和Gradle中配置单元测试框架Spock](https://mp.weixin.qq.com/s/kL5keijAAZwmq_DO1NDBtw)
- [Groovy单元测试框架spock基础功能Demo](https://mp.weixin.qq.com/s/fQCyIyeQANbu2YP2ML6_8Q)
- [Groovy单元测试框架spock数据驱动Demo](https://mp.weixin.qq.com/s/uCAB7Mxt1JZW229aKp-uVQ)

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester十八张原创专辑](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**，合作请联系`Fhaohaizi@163.com`。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [接口测试视频专题](https://mp.weixin.qq.com/s/4mKpW3QiVRee3kcVOSraog)
- [删除List中null的N种方法--最后放大招](https://mp.weixin.qq.com/s/4mfskN781dybyL59dbSbeQ)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [自动化策略六步走](https://mp.weixin.qq.com/s/He69k8iCKhTKD1j-yV6M5g)
- [功能自动化测试策略](https://mp.weixin.qq.com/s/qHmcblN4cD4JK6jT7oU4fQ)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)