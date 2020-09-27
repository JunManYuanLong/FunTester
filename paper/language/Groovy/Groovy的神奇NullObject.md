# Groovy的神奇NullObject

[原文地址](https://www.mscharhag.com/groovy/groovy-null-nullobject)

`Java`中`null`和`Groovy`中`null`的一些明显差异。 首先看一下Demo：

`Object o = null`

该语句在`Java`和`Groovy`中工作正常（`Java`在行尾需要分号）。但是，其效果略有不同。

在`Java`中，`null`是一种特殊对象，它被分配给不指向任何对象的引用类型。每次尝试对`null`引用执行任何操作（例如调用方法或访问成员变量）时，都会引发`NullPointerException`。

在`Groovy`中，`null`是一个对象！它是`org.codehaus.groovy.runtime.NullObject`的实例。大多数情况下，`NullObject`将抛出`NullPointerException`。但是，可以在`NullObject`上调用一些方法：


```Groovy
import org.codehaus.groovy.runtime.NullObject
 
			output	NullObject == null.getClass()
        output		true == null.equals(null)
        output		false == null.asBoolean()
        output		"null!" == null + "!"
        output		false == null.iterator().hasNext()
```

控制台输出：


```Groovy
INFO-> 当前用户：fv，IP：192.168.0.104，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.4
INFO-> true
INFO-> true
INFO-> true
INFO-> true
INFO-> true

```

如我们所见，在某些情况下`null`对象可以保护开发人员免受`NullPointerExceptions`的侵害。`asBoolean()`始终返回`false`，并确保在必要时可以将null转换为布尔值。`iterator()`返回`java.util.Collections $ EmptyIterator`的实例。因此，可以安全地遍历对象而无需显式检查`null`。

有趣的是，在正式的常规文档中我还没有找到有关`NullObject`的任何信息。它不是在提到从Java的差异也不是`Groovy`的空对象模式。可能没有实际的用例，甚至可以创建自己的`NullObject`实例：

```Groovy
Class c = null.getClass()
NullObject myNull = c.newInstance()
```

但是请注意，如果传入默认的`NullObject`实例，则`equals()`方法仅返回`true`。因此对于您自己的`NullObject`实例，它可能会出错：

```Groovy
			output myNull.equals(myNull)
        output myNull.equals(null)
```
控制台输出：

```Groovy
INFO-> false
INFO-> true
```

您还可以通过修改`metaClass`的`NullObject`添加自定义的方法：

```Groovy
        NullObject.metaClass.test = {output(getMark())}
        null.test()
```

控制台输出：

```Groovy
INFO-> 1586415029
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
- [6个重要的JVM性能参数](https://mp.weixin.qq.com/s/b1QnapiAVn0HD5DQU9JrIw)

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


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDkiawpL3o8umv1EgHOc2OE1H8DtTMQSXWTOgFYPMSGtoX2BZlricBBJun4hMGUOJd7uibe68zQecRFw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)