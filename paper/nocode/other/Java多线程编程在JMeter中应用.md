# Java多线程编程在JMeter中应用

在最近的工作中，遇到一个需求：在`JMeter`中生成一个全局唯一变量，获取一次自增x（这个不确定，可能根据响应信息）。这不是我的需求，只是从同事那边听说到的，周末没事儿想起来这个事儿。按照我的方案肯定是用`Java`或者`Groovy`去实现这个需求，肯定不会选`JMeter`。

如果非要给这个需求加一个`jmeter`的设定，我依然会选择用脚本语言实现，之前也写过一个专题：[Groovy在JMeter中应用专题](https://mp.weixin.qq.com/s/KcxPUDWl7MLQemFRoIV92A)。这次我的基本思路两个：1、还是新建一个全局的线程安全对象，然后通过自带的线程安全方法实现自增需求；2、处理全局变量时，通过**Java锁**实现单线程操作自增，然后实现多线程情况下的线程安全。

事实证明还是第二种方案比较好，因为我暂时还没找到如果通过脚本去`JMeter`里面新建一个全局线程安全类对象的方法。所以我是新建一个全局变量，赋予一个初始值，然后在每个线程脚本里面去处理这个全局变量，步骤如下：1、获取对象锁（这里指的是存放所有全局变量的对象**props**）；2、获取参数值，自增，重新赋值；3、释放对象锁，完成其他逻辑处理。

* 首先新建一个简单的线程组和一个简单的请求：

![](http://pic.automancloud.com/1583240306684.jpg)

* 添加JSR223 预处理程序（后置处理程序需要下一次次请求）

![](http://pic.automancloud.com/QQ20200303-210125.png)

脚本内容：


```Groovy
props.put("MY",test()+"")
//log.info(props.get("MY")+"")
def test() {
    synchronized (props) {
//        Thread.sleep(1000)
		int i = props.get("MY") as Integer
        log.info(i+"")
                i + 1
    }
}
```

这里有一个小知识点：*JMeter*中的对象*props*在**JVM**里面是有缓存的，经过我多次实验，只要*JMeter*不关闭，这个缓存一只会在。

* 可以通过复制私有变量来控制所有线程获取公共变量时的线程安全问题。


所以要在程序运行前设置一个初始值，如下：

![](http://pic.automancloud.com/WX20200719-104034x.png)

脚本内容如下：

```Groovy
//int i = props.get("MY") == null ? 0 :  props.get("MY") as Integer
props.put("MY","999")
```

* 日志输出：

下面是未加锁的情况：

```shell
2020-07-19 19:29:50,187 INFO o.a.j.t.JMeterThread: Thread started: 线程组 1-3
2020-07-19 19:29:50,191 INFO o.a.j.t.JMeterThread: Thread started: 线程组 1-4
2020-07-19 19:29:50,192 INFO o.a.j.t.ThreadGroup: Started thread group number 1
2020-07-19 19:29:50,192 INFO o.a.j.e.StandardJMeterEngine: All thread groups have been started
2020-07-19 19:29:50,199 INFO o.a.j.t.JMeterThread: Thread started: 线程组 1-5
2020-07-19 19:29:50,231 INFO o.a.j.m.J.JSR223 变量: 999
2020-07-19 19:29:50,232 INFO o.a.j.m.J.JSR223 变量: 999
2020-07-19 19:29:50,233 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-5
2020-07-19 19:29:50,233 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-5
2020-07-19 19:29:50,235 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-4
2020-07-19 19:29:50,235 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-4
2020-07-19 19:29:50,239 INFO o.a.j.m.J.JSR223 变量: 1000
2020-07-19 19:29:50,240 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-1
2020-07-19 19:29:50,240 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-1
2020-07-19 19:29:50,240 INFO o.a.j.m.J.JSR223 变量: 1000
2020-07-19 19:29:50,241 INFO o.a.j.m.J.JSR223 变量: 1001
2020-07-19 19:29:50,241 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-2
2020-07-19 19:29:50,241 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-2
2020-07-19 19:29:50,241 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-3
2020-07-19 19:29:50,241 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-3
```

下面是加锁的情况：


```shell
2020-07-19 19:28:40,988 INFO o.a.j.t.JMeterThread: Thread started: 线程组 1-5
2020-07-19 19:28:41,015 INFO o.a.j.m.J.JSR223 变量: 999
2020-07-19 19:28:41,017 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-1
2020-07-19 19:28:41,017 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-1
2020-07-19 19:28:41,031 INFO o.a.j.m.J.JSR223 变量: 1000
2020-07-19 19:28:41,041 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-2
2020-07-19 19:28:41,043 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-2
2020-07-19 19:28:41,044 INFO o.a.j.m.J.JSR223 变量: 1001
2020-07-19 19:28:41,046 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-5
2020-07-19 19:28:41,046 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-5
2020-07-19 19:28:41,046 INFO o.a.j.m.J.JSR223 变量: 1002
2020-07-19 19:28:41,048 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-4
2020-07-19 19:28:41,048 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-4
2020-07-19 19:28:41,049 INFO o.a.j.m.J.JSR223 变量: 1003
2020-07-19 19:28:41,052 INFO o.a.j.t.JMeterThread: Thread is done: 线程组 1-3
2020-07-19 19:28:41,052 INFO o.a.j.t.JMeterThread: Thread finished: 线程组 1-3
```


--- 
* 公众号**FunTester**首发，更多原创文章：[FunTester420+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)
- [有关UI测试计划](https://mp.weixin.qq.com/s/D0fMXwJF754a7Mr5ARY5tQ)
- [Selenium自动化测试技巧](https://mp.weixin.qq.com/s/EzrpFaBSVITO2Y2UvYvw0w)
- [敏捷测试中面临的挑战](https://mp.weixin.qq.com/s/vmsW56r1J7jWXHSZdcwbPg)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)