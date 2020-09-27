# 删除List中null的N种方法--最后放大招

* 从`List`列表中删除`null`的不同方法：

抛砖引玉，先抛砖，大招在最后。

# Java 7或更低版​​本

当使用`Java 7`或更低版​​本时，我们可以使用以下结构从列表中删除所有空值：


```Java
@Test
public removeNull() {
 
    List<String> list = new ArrayList<>(Arrays.asList("A", null, "B", null));
 
    list.removeAll(Collections.singleton(null));
 
    assertThat(list, hasSize(2));
}
```

* 请注意，在此处创建了一个可变列表。尝试从不可变列表中删除`null`将抛出`java.lang.UnsupportedOperationException`的错误。

# Java 8或更高版本

从`Java 8`或更高版本，从`List`列表中删除`null`的方法非常直观且优雅：


```Java
@Test
public removeNull() {
 
    List<String> list = new ArrayList<>(Arrays.asList("A", null, "B", null));
 
    list.removeIf(Objects::isNull);
 
    assertThat(list, hasSize(2));
}
```

我们可以简单地使用removeIf（）构造来删除所有空值。

如果我们不想更改现有列表，而是返回一个包含所有非空值的新列表，则可以：


```Java
@Test
public removeNull() {
 
    List<String> list = new ArrayList<>(Arrays.asList("A", null, "B", null));
 
    List<String> newList = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
 
    assertThat(list, hasSize(4));
    assertThat(newList, hasSize(2));
}
```

# Apache Commons

`Apache Commons CollectionUtils`类提供了一个`filter`方法，该方法也可以解决我们的目的。传入的谓词将应用于列表中的所有元素：


```Java
@Test
public removeNull() {
 
    List<String> list = new ArrayList<>(Arrays.asList("A", null, "B", null));
 
    CollectionUtils.filter(list, PredicateUtils.notNullPredicate());
 
    assertThat(list, hasSize(2));
}
```

# Google Guava

`Guava`中的`Iterables`类提供了`removeIf()`方法，以帮助我们根据给定的谓词过滤值。

```Java
@Test
public removeNull() {
 
    List<String> list = new ArrayList<>(Arrays.asList("A", null, "B", null));
 
    Iterables.removeIf(list, Predicates.isNull());
 
    assertThat(list, hasSize(2));
}
```

另外，如果我们不想修改现有列表，Guava允许我们创建一个新的列表：

```Java
@Test
public removeNull() {
 
    List<String> list = new ArrayList<>(Arrays.asList("A", null, "B", null));
 
    List<String> newList = new ArrayList<>(Iterables.filter(list, Predicates.notNull()));
 
    assertThat(list, hasSize(4));
    assertThat(newList, hasSize(2));
}
```


```Java
@Test
public removeNull() {
 
    List<String> list = new ArrayList<>(Arrays.asList("A", null, "B", null));
 
    List<String> newList = new ArrayList<>(Iterables.filter(list, Predicates.notNull()));
 
    assertThat(list, hasSize(4));
    assertThat(newList, hasSize(2));
}
```

# Groovy大招


```Python
			List<String> list = new ArrayList<>(Arrays.asList("A", null, "B", null));

        def re = list.findAll {
            it != null
        }

        assert re == ["A", "B"]
```


有兴趣可以读读[Groovy中的闭包](https://mp.weixin.qq.com/s/pfcG47gSPfUveAaEfdeo8A)


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