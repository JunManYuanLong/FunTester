# Groovy中的list

[原文地址](https://dzone.com/articles/java-groovy-part-2-closures-an)

在上一期[从Java到Groovy的八级进化论](https://mp.weixin.qq.com/s/QTrRHsD3w-zLGbn79y8yUg)中，我分享了`Java`是如何转变成`Groovy`。今天，我将分享学习`Groovy`对`list`的语法支持。

以下Java类也是有效的`Groovy`类。其目的是过滤名称列表，以删除超过三个字符的名称。我们将创建一个名称列表，我们将调用一个负责过滤的实用程序方法，并打印结果。


```Java
package com.fun

import com.fun.frame.httpclient.FanLibrary
import org.slf4j.Logger

class TSSS extends FanLibrary {

    public static Logger logger = getLogger(TSSS.class)

    public static void main(String[] args) {
        List names = new ArrayList();
        names.add("Ted");
        names.add("Fred");
        names.add("Jed");
        names.add("Ned");
        List shortNames = filter(names, 3);
        output(shortNames.size());
        for (Iterator i = shortNames.iterator(); i.hasNext();) {
            String s = (String) i.next();
            output(s);
        }
    }

    public static List filter(List strings, int length) {
        List result = new ArrayList();
        for (String str : strings) {
            if (str.length() < length + 1) {
                result.add(str);
            }
        }
        return result;
    }

}
```

我们当然可以通过使用`Arrays＃asList()`方法来保存一些行来改进此`Java`示例。我将再次使用与上一篇文章相同的路径来`groovyfy`该程序。


```Groovy

package com.fun

import com.fun.frame.httpclient.FanLibrary
import org.slf4j.Logger

class TSSS extends FanLibrary {

    public static Logger logger = getLogger(TSSS.class)

    public static void main(String[] args) {
        List names = new ArrayList()
        names.add("Ted")
        names.add("Fred")
        names.add("Jed")
        names.add("Ned")
        List shortNames = filter(names, 3)
        output shortNames.size()
        for (String s : shortNames) {
            output s
        }
    }

    public static List filter(List strings, int length) {
        List result = new ArrayList()
        for (String str : strings) {
            if (str.length() < length + 1) {
                result.add(str)
            }
        }
        return result
    }

}
```

与其将类与方法main（）一起使用，不如将其转换为脚本，并且还将放弃静态类型信息：


```Java Python

List names = new ArrayList()
names.add("Ted")
names.add("Fred")
names.add("Jed")
names.add("Ned")
List shortNames = filter(names, 3)
println shortNames.size()
for (String s : shortNames) {
    println s
}

def filter(List strings, int length) {
    List result = new ArrayList()
    for (String str : strings) {
        if (str.length() < length + 1) {
            result.add(str)
        }
    }
    return result
}```

无需创建类的实例，我们只需调用`filter()`方法。到目前为止，这些小变化并不是真正的新事物，因为我们之前已经遵循了这些步骤。现在，我们要发现的是，借助`Groovy`列表的本机语法，如何使列表更加友好。那么我们如何定义一个新列表？

`def names = []`

而且，我们可以一次填充一个元素，而不是一次在列表中添加一个元素：

`def names = ["Ted", "Fred", "Jed", "Ned"]`

可以使用下标运算符设置和访问元素：


```Python
            assert names[1] == "Fred"
            names[1] = "Frederic"
```

`Groovy`还在列表上添加了一些有用的方法，以简化列表活动，例如枚举元素。`Groovy`通过“装饰”核心JDK类来做到这一点。列表上添加了两个方便的方法，它们是用于遍历所有元素的`each()`方法，以及用于查找符合某个条件的所有元素的`findAll()`方法。


```Groovy
        println names.any {
            it.length() > 3
        }

        println names.every {
            it.length() > 3
        }

        println names.find {
            it.length() > 3
        }
        
        println names.findAll() {
            it.length() > 2
        }
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

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDkiawpL3o8umv1EgHOc2OE1iaib1vR7Q6DH1FSpP4HVuibsibicftEqUqfXZpE2FyN7nIPvHwhWQdG6n0g/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)