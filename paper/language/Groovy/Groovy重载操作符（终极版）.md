# Groovy重载操作符（终极版）

最近在研究`JSonpath`在接口验证和接口串联之间的应用，目前进度尚可，已经在语法封装上有了一个思路。借助`Groovy`重载操作符的特性，适配一部分类似于`=`、`>`、`>=`和`classType`这样的验证功能，重新翻起来了《Groovy in action》这本神书，重新复习了一下，再看看自己之前写过的[Groovy重载操作符](https://mp.weixin.qq.com/s/4jW06Q4_vjFR9DovRTTuHg)，有点对不住读者。

特意将官方`API`里面所有的操作符重载都实现了一遍，对于一些疑问做了一些注释，这里有两个操作符未能实现：`-`和`+`，这并不是`加`和`减`，而是表示正负值的，`Groovy`里面是可以直接对对象使用这两个操作符来完成数值的`正负转换`，对于`List`对象同样适用。但是这两个操作符只能对*数值型*和*数值型List*使用，目前尚未解决这个操作符的其他类型使用的方案。

* 这里需要注意`++`和`--`操作符，`Groovy`没有区分前后，而且根据实现逻辑会最终赋值给当前对象，所以需要一个返回值，不然很容易报空指针异常，当然也可以通过`?.`安全引用来避免，这个有空再讲了。文档中：Groovy还可以重写`.`这个操作符，有兴趣的同学可以一起研究。

## Demo代码

```Groovy
package com.fun.utils

import com.fun.frame.SourceCode

/**
 * 操作符重写类,用于匹配JSonpath验证语法
 */
class Verify extends SourceCode implements Comparable {


    def plus(int i) {
        logger.info("操作符: + {}", i)
    }

    def minus(int i) {
        logger.info("操作符: - {}", i)
    }

    def multiply(int i) {
        logger.info("操作符: * {}", i)
    }

    def div(int i) {
        logger.info("操作符: / {}", i)
    }

    def mod(int i) {
        logger.info("操作符: % {}", i)
    }
    /**
     * 必需有返回值
     * <p>
     *     defv=a;a=a.next();v a = a.next(); a
     * </p>
     *
     * @return
     */
    def next() {
        logger.info("操作符: i++ 或者 ++i")
        this
    }
    /**
     * 必需有返回值
     * <p>
     *     defv=a;a= a.previous(); v
     * </p>
     *
     * @return
     */
    def previous() {
        logger.info("操作符: i-- 或者 --i")
        this
    }

    def power(Verify verify) {
        logger.info("操作符: ** ${verify}")

    }

    def or(Verify verify) {
        logger.info("操作符: | ${verify}")
    }

    def and(Verify verify) {
        logger.info("操作符: & ${verify}")
    }

    def xor(Verify verify) {
        logger.info("操作符: ^ ${verify}")
    }

    def bitwiseNegate() {
        logger.info("操作符: ~i")
    }

    def getAt(int a = 3) {
        logger.info("操作符: i[${a}]")
    }

    def putAt(int a = 3, int c = 3) {
        logger.info("操作符: i[${a}]=${c}")

    }

    def leftShift(Verify verify) {
        logger.info("操作符: <<  ${verify}")
    }

    def rightShift(Verify verify) {
        logger.info("操作符: >>  ${verify}")
    }

    def rightShiftUnsigned(Verify verify) {
        logger.info("操作符: >>>  ${verify}")
    }

    def isCase(Verify verify) {
        logger.info("操作符: case  ${verify}")
        true
    }
    /**
     * if (a implements Comparable) { a.compareTo(b) == 0 } else { a.equals(b) }* @param a
     * @return
     */
    def equals(Verify verify) {
        logger.info("操作符: ==  ${verify}")
        //todo:重写
        true
    }

/**
 * a <=> b  a.compareTo(b)
 * a>b      a.compareTo(b) > 0
 * a>=b     a.compareTo(b) >= 0
 * a<b      a.compareTo(b) < 0
 * a<=b     a.compareTo(b) <= 0

 * @param o
 * @return
 */
    @Override
    int compareTo(Object o) {
        logger.info("操作符: <=> >= > < 等等")
        //todo:重写
        return 0
    }

    def <T> T asType(Class<T> tClass) {
        logger.info(tClass.toString())
        if (tClass.equals(Integer)) 4
    }
}

```

--- 
* 公众号**FunTester**首发，更多原创文章：[440+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [开源测试服务](https://mp.weixin.qq.com/s/ZOs0cp_vt6_iiundHaKk4g)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)
- [测试开发工程师工作技巧](https://mp.weixin.qq.com/s/TvrUCisja5Zbq-NIwy_2fQ)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)