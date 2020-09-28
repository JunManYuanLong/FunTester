# 从Java到Groovy的八级进化论



Groovy和Java确实是亲戚关系，它们的语法非常相似，因此对于Java开发人员来说，Groovy非常容易学习。相似之处在于，大多数Java程序甚至都是有效的Groovy程序（把文件后缀`.java`改成`.groovy`即可）。

我将从一个基础`Hello World`程序开始，分享一下Java如何演化成Groovy的Demo。

## 原始的`Hello World`


```Java
public class HelloWorld {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String greet() {
        return "Hello " + name;
    }

    public static void main(String[] args) {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.setName("Groovy");
        System.out.println(helloWorld.greet());
    }

}
```

这个`Hello World`类具有一个私有字段，该私有字段表示名称及其相关的`getter()`和`setter()`。还有一个`greet()`方法，该方法将返回著名的`Hello World`字符串。然后是一个`main()`方法，该方法将实例化我们的类，设置名称并在输出欢迎消息。

## Groovy程序一级进化


```Groovy


public class HelloWorld {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String greet() {
        return "Hello " + name;
    }

    public static void main(String[] args) {
        HelloWorld helloWorld = new HelloWorld()
        helloWorld.setName("Groovy")
        System.out.println(helloWorld.greet());
    }
}
```

初级进化的Groovy跟Java程序是完全相同的程序！这并不是真正的Groovy程序，因为我们可以对其进行改进以使其更加简洁易读。

## Groovy程序二级进化

我们将通过执行一些简单的步骤来`Groovy化`该程序。第一步将是删除分号：并且，同时，我还将删除public关键字，因为默认情况下，类和方法在`Groovy`中是`public`，除非另有明确说明。我们的`Groovy`程序现在变为：


```Groovy
class HelloWorld {

    private String name

    void setName(String name) {
        this.name = name
    }

    String getName() {
        return name
    }

    String greet() {
        return "Hello " + name
    }

    static void main(String[] args) {
        HelloWorld helloWorld = new HelloWorld()
        helloWorld.setName("Groovy")
        System.out.println(helloWorld.greet())
    }
}
```

## Groovy程序三级进化

我们还可以使用`Groovy`的特殊字符串：`GString`。在用双引号分隔的普通字符串中，可以放置一些用`${someVariable}`分隔的占位符，当打印该字符串时，它们将被变量或表达式的值替换。因此，无需费心手动连接字符串。那么，我们的`greet()`方法是什么样的呢？


```Groovy
String greet() {
    return "Hello ${name}"
}
```
## Groovy程序四级进化

更进一步：可以省略`return`关键字，方法的最后一个计算表达式将是返回值。因此，`greet()`现在甚至更简单了：


```Groovy
class HelloWorld {

    private String name

    void setName(String name) {
        this.name = name
    }

    String getName() {
        name
    }

    String greet() {
        "Hello ${name}"
    }

    static void main(String[] args) {
        HelloWorld helloWorld = new HelloWorld()
        helloWorld.setName("Groovy")
        System.out.println(helloWorld.greet())
    }
}
```


甚至可以自由地将`getName()`和`greet()`方法转换为单行代码。

## Groovy程序五级进化

但是接下来呢？`Groovy`类属性方法开始起作用。可以简单地声明一个属性，而不是创建一个私有字段并编写一个`getter`和`setter`方法。在`Groovy`中，属性非常简单，因为它们仅是字段声明，没有任何特定的可见性。我们的`name`属性将只是`String name`，仅此而已。`Groovy`将免费提供一个私有字段以及相关的`getter`和`setter`。对于调用`setName()`，您将能够编写`helloWorld.name ="Groovy"`，对于`getName()`，只需编写`helloWorld.name`。

```Groovy
class HelloWorld {

    String name

    String greet() {
        "Hello ${name}"
    }

    static void main(String[] args) {
        HelloWorld helloWorld = new HelloWorld()
        helloWorld.name = "Groovy"
        System.out.println(helloWorld.greet())
    }
}
```

## Groovy程序六级进化

Groovy为您提供了一些诸如打印之类的日常任务的便捷方法和快捷方式。您可以将`System.out.println()`替换为`println()`。Groovy甚至通过提供其他实用程序方法来装饰JDK类。对于顶级语句（仅是带有某些参数的方法调用的语句），可以省略括号。


```Groovy
class HelloWorld {

    String name

    String greet() {
        "Hello ${name}"
    }

    static void main(String[] args) {
        HelloWorld helloWorld = new HelloWorld()
        helloWorld.name = "Groovy"
        println helloWorld.greet()
    }
}
```

## Groovy程序七级进化

到目前为止，我们通过定义每个方法，变量或字段的类型在各处使用了强类型。`Groovy`还支持动态键入。因此，如果愿意，我们可以摆脱所有类型：


```Groovy
class HelloWorld {

    def name

    def greet() {
        "Hello ${name}"
    }

    static main(args) {
        def helloWorld = new HelloWorld()
			helloWorld.name = "Groovy"
			println helloWorld.greet()
    }
}
```

我将字符串转换为`def`关键字，我们能够删除`main`方法的`void`返回类型以及其参数的字符串类型数组。

## Groovy程序八级进化

Groovy是一种面向对象的语言，它支持与Java相同的编程模型。但是同时Groovy还拥有一个重要的特性：Groovy是一种脚本语言，因为它允许编写一些不需要定义类结构的自由格式程序。因此，在本教程的最后一步，我们将完全摆脱主要方法：

```Groovy
class HelloWorld {

    def name

    def greet() {
        "Hello ${name}"
    }
}w

def helloWorld = new HelloWorld()
helloWorld.name = "Groovy"
println helloWorld.greet()
```

脚本实际上只是一堆随意扔到程序中的代码。甚至可以在脚本中定义几个类，就像我们的`HelloWorld`类一样。

---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [Java并发BUG基础篇](https://mp.weixin.qq.com/s/NR4vYx81HtgAEqH2Q93k2Q)
- [Java并发BUG提升篇](https://mp.weixin.qq.com/s/GCRRe8hJpe1QJtxq9VBEhg)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [压测中测量异步写入接口的延迟](https://mp.weixin.qq.com/s/odvK1iYgg4eRVtOOPbq15w)
- [多种登录方式定量性能测试方案](https://mp.weixin.qq.com/s/WuZ2h2rr0rNBgEvQVioacA)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [左移测试](https://mp.weixin.qq.com/s/8zXkWV4ils17hUqlXIpXSw)
- [选择手动测试还是自动化测试？](https://mp.weixin.qq.com/s/4haRrfSIp5Plgm_GN98lRA)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCxr0Sa2MXpNKicZE024zJm7vIAFRC09bPV9iaMer9Ncq8xppcYF73sDHbrG2iaBtRqCFibdckDTcojKg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDkiawpL3o8umv1EgHOc2OE1H8DtTMQSXWTOgFYPMSGtoX2BZlricBBJun4hMGUOJd7uibe68zQecRFw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDkiawpL3o8umv1EgHOc2OE1iaib1vR7Q6DH1FSpP4HVuibsibicftEqUqfXZpE2FyN7nIPvHwhWQdG6n0g/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)