# 混合Java函数和Groovy闭包

[原文地址](https://dzone.com/articles/mixing-java-functions-and-groovy-closures?fromrel=true)

之前分享过[Groovy中的闭包](https://mp.weixin.qq.com/s/pfcG47gSPfUveAaEfdeo8A)，在我日常的工作中，就会想到一个问题：“如何在`Groovy`中使用`Java`方法？”

在许多情况下，闭包和函数或多或少提供相同的功能。`Groovy`支持以上这些功能，但不支持`lambda`。语法冲突，因为`Groovy`中已经使用了箭头符号。`Java`函数和`groovy`闭包可以通过某种方式一起混合使用。

第一个示例是使用`Groovy`中的`Closure`实现`Java`功能接口：

```Groovy
BiFunction test = { 
    Integer a, Integer b ->
    a > b ? true : false
}
assert test.apply(1, 2) == false
```

这实际上与`Groovy`中的内容相同：

```Groovy
def test = { Integer a, Integer b ->
    a > b ? true : false
}
assert test.call(1 ,2) == false
```

除了无论如何都会忽略的信息外，这两个示例在语义和语法上几乎相同。

一些`Java`方法将函数作为参数。例如`Collection`的`removeIf()`方法：


```Groovy
List list = [1,4,2,3,7,4]
Predicate test = { a ->
    a > 2 ? true : false
}
list.removeIf(test)
assert list.size() == 2 && list[0] == 1 && list[1] == 2
```

在上面的示例中，该谓词使用闭包实现，并传递给`Java`方法`removeIf`，该方法执行该谓词。`Groovy`与`Java`语法和功能很好地结合在一起。

不幸的是，`Groovy`不支持`lambda`语法。但是不妨碍我们在Groovy中使用`lambda`。请看以下示例：


```Groovy
def min = { Integer a, Integer b -> 
    a < b ? a : b 
} 
Integer result = list.stream().reduce(min).get() 
assert result == 1
```

我没有使用`Java`语法作为`reduce`方法，而是传递了带有正确输入参数的闭包。然后可以照常使用`Java stream`功能。如果正确使用函数和闭包，则可以将`Java`和`Groovy`的优势融合在一起，从而创建更强大的组合。

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [接口测试视频专题](https://mp.weixin.qq.com/s/4mKpW3QiVRee3kcVOSraog)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [简化测试用例](https://mp.weixin.qq.com/s/BhwfDqhN9yoa3Iul_Eu5TA)
- [自动化策略六步走](https://mp.weixin.qq.com/s/He69k8iCKhTKD1j-yV6M5g)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)