# 测试如何处理Java异常




本文主要内容：处理`Java异常`的几种情况下的错误示范和正确示范。用`Java`处理异常不是一件容易的事，幸好对于测试来讲，没有那么多悬而不决的场景，只要开发之前进行简单的`异常约束`基本能解决所有问题。

本文将向演示最重要的一些处理`Java异常`的场景，用来入门或改善异常处理。异常是程序执行期间的异常情况。

# 永远不要在catch块中毁灭异常

错误示范：

```Java
catch (NoSuchMethodException e) {
   return null;
}
```

永远不要直接返回`null`而不是处理异常，它会清除掉异常信息并导致错误。如果不了解失败的原因，那么将来就难以发现问题从而更快解决问题。

# 声明可能抛出的特定检查异常

错误示范：

```Java
public void test() throws Exception { 
}
```

尽量避免使用上面的代码，必须声明该方法可能引发的特定检查异常。如果用户有许多已检查的异常，则必须将其覆盖在用户的异常中，并将信息附加到异常消息中。

正确示范：

```Java
public void test() throws SpecificException1, SpecificException2 {3
}
```

# 不要catch exception，而需要catch特定的子类

错误示范：

```Java
try {
		someMethod();
}catch (Exception e) {
		LOGGER.error("失败了！", e);
}
```

`catch`异常的主要问题是，如果用户稍后调用的方法将新检查的异常，则开发人员希望处理特定的新异常。如果用户的代码`catch exception`，那么将永远无法理解该变化。用户的代码于具体需求的差异，并且可能在运行时的某个时间点崩溃。

# 永远不要`catch`任何Throwable类

`Java`提供了表示不同类型异常的类层次结构。`java.lang`包的`Throwable`类是所有异常类的超类。

下图是Java异常类的继承关系，看完之后相信你再也不会有`catch throwable`的想法了。

```Java
└────Throwable
     ├────Error
     │    ├────NoClassDefFoundError
     │    └────OutOfMemoryError
     └────Exception
          ├────RuntimeException
          │    ├────IllegalArgumentException
          │    └────NullPointerException
          ├────FileNotFoundException
          └────IOException

```


# 自定义异常避免丢失堆栈信息

错误示范：

```Java
catch (NoSuchMethodException e) 
{
throw new MyServiceException("自定义错误: " + e.getMessage()); 
}
```

上面的命令可能会失去异常的堆栈跟踪，对于排查问题制造了障碍。

正确示范：

```Java
catch (NoSuchMethodException e) {
     throw new MyServiceException("自定义错误: " , e); 
     }
```

# 不要同时记录和抛出异常

错误示范：

```Java
catch (NoSuchMethodException e) {
   LOGGER.error("错误：", e);
   throw e;
}
```

如上面的代码所示，抛出和记录可能会在日志文件中导致多个日志消息。对于浏览日志的开发人员，代码中的单个问题可能会造成大量错误信息，不利于定位`BUG`。

# 不要从finally块抛出异常

错误示范：

```Java
try {
  someMethod();  //抛出异常
}
 finally
{
  cleanUp();    //如果这里也抛出异常，则无法处理
}
```

准确地说，`cleanUp()`永远不会引发异常。在上述情况下，如果`someMethod()`出现异常，并且在`finally`块中，`cleanUp()`也出现异常，则该方法之外的其他异常将消失，原始的第一个异常（正确的原因）将永远消失。 

# 毫无作用的catch

```Java
catch (NoSuchMethodException e) 
{
throw e; //直接抛出没什么卵用
}
```

如果不能在`catch`块中处理它，那么最好的建议是直接抛出这个异常。

# 不处理异常，使用finally而不是catch

正确示范：

```Java
try {
  someMethod();  
} 
finally
{
  cleanUp();    
}

```

这也是一个好习惯。如果在方法内部访问`someMethod()`，并且抛出一些您不想在方法中处理的异常，但是仍然希望进行`cleanUp()`以防万一，那么请在`finally`块中进行`cleanUp()`。



# 结论

`java`异常处理是必不可少的，有多种针对不同场景下的解决方案。我歘和本文可帮助`Java测试`新手获得有关处理`Java异常`的基本认识。


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
- [Selenium并行测试基础](https://mp.weixin.qq.com/s/OfXipd7YtqL2AdGAQ5cIMw)
- [Selenium并行测试最佳实践](https://mp.weixin.qq.com/s/-RsQZaT5pH8DHPvm0L8Hjw)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)