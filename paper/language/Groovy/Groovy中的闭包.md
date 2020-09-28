# Groovy中的闭包



书接上文：

- [从Java到Groovy的八级进化论](https://mp.weixin.qq.com/s/QTrRHsD3w-zLGbn79y8yUg)
- [Groovy中的list](https://mp.weixin.qq.com/s/0mUe1_WrUiEm1t6kqCV3eQ)

今天分享一下Groovy的闭包。基本上，我们可以将闭包视为代码，语句块，它们可以访问周围范围的所有变量或方法，并且可以将这些代码块分配给变量，并将它们传递到其他地方。

```Python
            def c = { println "hello" }
            c()
```

很简单，闭包只是一个语句，用大括号包含。您可以将其分配给变量，然后像常规方法调用一样调用此闭包。闭包具有一个默认的隐式参数，称为`it`。还可以提供自定义的参数。同样，就像方法中的情况一样，闭包的最后一个表达式是闭包的返回值。但是，如果您觉得可读性更高，也可以使用`return`关键字。


```Python
            def square = { it * it }
            assert square(3) == 9
            def lengthThan = { String s, int i -> return s.size() > i }
            //def lengthThan = { String s, int i -> s.size() > i }
            assert lengthThan("FunTester", 4) == true
            assert lengthThan("Fun", 6) == false
```

既然我们已经发现了闭包是什么样子，以及如何分配它们并调用它们，我们将了解如何将闭包作为参数传递给另一个方法，因为这就是我们要做的`Groovy`在集合上添加的`each()`和`findAll()`方法。


```Python
            def log(Closure c) {
                println "Calling closure"
                def start = System.currentTimeMillis()
                println "记录信息: " + c()
                def end = System.currentTimeMillis()
                println "记录结果成功。(耗时: ${end - start} ms)"
            }
            log({ return "Groovy is best language!" })
            // 可以去掉括号
            log { return "Groovy is best language!" }
```

现在，闭包方面，我们已经小试牛刀。接下来，我们通过对集合使用`each()`方法对列表的每个元素调用闭包。我们将打印列表的所有名称：

```Python

            names.each({ String name -> println name })
            names.each { String name -> println name }
            names.each { println it }
```

在使用`each()`对每个元素应用闭包之后，由于使用了`findAll()`方法，我们将根据过滤器闭包来过滤名称的原始列表，从而创建一个新列表。此方法将查找集合中与作为参数传递的闭包所表示的条件匹配的所有元素。将在每个元素上调用此闭包，并且`findAll()`方法将仅返回一个布尔值，该布尔值说明当前值是否匹配。最后，将返回一个包含匹配元素的新列表。该闭包将替换原始`Java`类的`lengthThan()`方法。

`            def shortNames = names.findAll { it.size() <= 3 }`

Groovy提供了其他几种此类方法，例如：

* find()：找到匹配的第一个元素
* every()：如果所有元素均符合条件闭包，则返回true
* any()：如果至少一个元素匹配，则返回true

现在，该通过应用我们刚刚了解的有关列表和闭包的知识，完成将最初的Java类转换为更简洁的Groovy类的时候了：

```Python
            def names = ["Ted", "Fred", "Jed", "Ned"]
            println names
            def shortNames = names.findAll { it.size() <= 3 }
            println shortNames.size()
            shortNames.each { println it }
```

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [多项目登录互踢测试用例](https://mp.weixin.qq.com/s/Nn_CUy_j7j6bUwHSkO0pCQ)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [绑定手机号性能测试](https://mp.weixin.qq.com/s/K5x1t1dKtIT2NKV6k4v5mw)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [简化测试用例](https://mp.weixin.qq.com/s/BhwfDqhN9yoa3Iul_Eu5TA)
- [生产环境中进行自动化测试](https://mp.weixin.qq.com/s/JKEGRLOlgpINUxs-6mohzA)
- [所谓UI测试](https://mp.weixin.qq.com/s/wDvUy_BhQZCSCqrlC2j1qA)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
