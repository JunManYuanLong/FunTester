# Java中interface属性和实例方法

[原文地址](https://blogs.oracle.com/javamagazine/quiz-yourself-default-methods-advanced)

给定代码：

```java
interface Nameable {    
    default void setName(String name) {
        this.name = name;
    }    
    default String getName() {
        return this.name;
    }
}

class Employee implements Nameable {
    protected String name;   
}

class HR {
    public static void main(String[] args) {
        Employee e = new Employee();
        e.setName("John Doe");
        System.out.println(e.getName());
    }
}

```

结果是什么？单选题。

A.接口Nameable无法编译。
B.类Employee无法编译。
C.类HR无法编译。
D.输出John Doe。

这段代码研究了default添加到Java 8 中的方法功能的各个方面。默认方法是在接口中通过实现定义的实例方法。尽管与在类中定义的常规实例方法相比，此类方法的继承方式有所不同，但是此功能仍然在Java中创建了多种实现继承的形式。

为了限制多重继承引起的问题，Java采取了两个步骤。第一个只是告诫您将功能用于特定和有限的目的，尤其是库接口的扩展（尽管有趣的是核心API本身实际上违反了该指南）。第二个问题是，尽管可以定义方法，但实例变量却不能定义（除了`public static final`的值）。 同样，在类中声明的字段对接口不可见（类实现了接口，但是接口无法知道哪些类可以实现它们）； default方法无法访问任何实例变量。因此，避免了多重实现继承的真正麻烦的问题。

这些选择的结果是，尽管default方法确实具有`this`引用（它们是实例方法），但是只能通过实例方法（`abstract`和`default`）以及`public static final`在接口中声明的字段访问。直接引用任何常规实例状态是不可能的。（abstract方法的实现可以这样做，但是此类代码是在类中编写的，而不是在接口中编写的。）

在此问题中，`Nameable`接口中没有`name`字段。因此，无法编译`this.name`中这两种`default`方法的实现，因为他们无法访问到接口实例中的字段。由此，选项A是正确的。

让我们看一下在接口中添加变量的问题。假设这样添加`String name`到`Nameable`接口：

```Java
interface Nameable {
    String name = "John Doe";
    …
}
```
默认情况下，接口中的所有字段都是`final`（因此，在声明期间必须进行赋值），它们是`public`和`static`。

使用此添加，将编译getName在Nameable接口中声明的方法。这里再次是为了方便：


```Java
default String getName() { 
    return this.name; // 可以的
}
```
但是，该`setName`方法仍将无法编译，因为`final`修饰的变量无法重新赋值：

```Java
default void setName(String name) {
    this.name = name; // 错误的
}
```

因此，即使`String name`在接口中添加了字段，如果不对`default`方法的代码做进一步的更改，代码也不会如所示那样编译。


---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
 - [Selenium 4.0 Alpha更新日志](https://mp.weixin.qq.com/s/tU7sm-pcbpRNwDU9D3OVTQ)
- [Selenium 4.0 Alpha更新实践](https://mp.weixin.qq.com/s/yT9wpO5o5aWBUus494TIHw)
- [如何统一接口测试的功能、自动化和性能测试用例](https://mp.weixin.qq.com/s/1xqtXNVw7BdUa03nVcsMTg)

## 非技术文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)
- [API测试基础](https://mp.weixin.qq.com/s/bkbUEa9CF21xMYSlhPcULw)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)