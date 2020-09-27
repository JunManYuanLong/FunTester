# arthas命令watch观察方法调用（上）

> `arthas`是一个`Java`开源诊断神器。

今天分享一个非常重要的命令`watch`，官网定义这个方法的功能如下：让你能方便的观察到指定方法的调用情况。能观察到的范围为：返回值、抛出异常、入参，通过编写 OGNL 表达式进行对应变量的查看。

由于涉及到比较多的命令参数和`ognl`表达式的应用，内容比较多，所以分了上下两期，上主要讲官网`Demo`内容，下主要讲实践。顺道说一下，官网的文档标题和实际`Demo`有几处不太一致的地方，大家如果要学习的话，以`Demo`代码展示为主就好。

# 主要参数

|参数名称	|参数说明|
|-----|-----|
|class-pattern|	类名表达式匹配|
|method-pattern	|方法名表达式匹配|
|express	|观察表达式|
|condition-express	|条件表达式|
|[b]	|在方法调用之前观察|
|[e]	|在方法异常之后观察|
|[s]	|在方法返回之后观察|
|[f]	|在方法结束之后(正常返回和异常返回)观察|
|[E]	|开启正则表达式匹配，默认为通配符匹配|
|[x:]	|指定输出结果的属性遍历深度，默认为 1|

# arthas命令watch观察方法调用（上）

- [点击观看视频](https://mp.weixin.qq.com/s/6fMKP7H4Q7ll_0v-wyN19g)


# 特别说明

* `watch`命令定义了4个观察事件点，即`-b`方法调用前,`-e`方法异常后，`-s`方法返回后，`-f`方法结束后
* 4个观察事件点`-b`、`-e`、`-s`默认关闭，`-f`默认打开，当指定观察点被打开后，在相应事件点会对观察表达式进行求值并输出
* 这里要注意方法入参和方法出参的区别，有可能在中间被修改导致前后不一致，除了`-b`事件点 `params`代表方法入参外，其余事件都代表方法出参
* 当使用`-b`时，由于观察事件点是在方法调用前，此时返回值或异常均不存在

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester十八张原创专辑](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**，合作请联系`Fhaohaizi@163.com`。

### 热文精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [删除List中null的N种方法--最后放大招](https://mp.weixin.qq.com/s/4mfskN781dybyL59dbSbeQ)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [自动化策略六步走](https://mp.weixin.qq.com/s/He69k8iCKhTKD1j-yV6M5g)
- [功能自动化测试策略](https://mp.weixin.qq.com/s/qHmcblN4cD4JK6jT7oU4fQ)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)