# Mock System.in和检查System.out



做在单元测试的时候，会遇到测试数据依赖于用户输入的情况，类似于代码`Scanner scanner = new Scanner(System.in);`。下面提供一种方法，可以在测试过程中设置`System.in`和`System.out`内容，很好地解决了模拟用户输入和检查输出的问题。

```Java
public MockInOut(String input) {
    orig = System.out;
    irig = System.in;
 
    os = new ByteArrayOutputStream();
    try {
        System.setOut(new PrintStream(os, false, charset.name()));
    } catch (UnsupportedEncodingException ex) {
        throw new RuntimeException(ex);
    }
 
    is = new ByteArrayInputStream(input.getBytes());
    System.setIn(is);
}
```

在这里设置了`System.out`和`System.in`，因此我们可以在执行后完全获得输出，并且这次不需要手动输入，因为在的语句中`Scanner scanner = new Scanner(System.in)`;，参数`System.in`是不会提示更改的，因此`scanner.nextLine()`将获得准备好的输入而无需等待。同样，输出将不会在控制台中打印，而是会累积到中`ByteArrayOutputStream`，随时可以访问。

如果想恢复`System.in`和`System.out`我们该怎么办？

```Java
/**
 * 重置System.in、System.out
 */
public void close() {
    os = null;
    is = null;
    System.setOut(orig);
    System.setIn(irig);
}
```

基本上，它会保存原始对象和内容，`in`并且`out`在需要恢复时，只需清楚`stream`并将原来的其放回原处，然后一切将照常进行。

您可以在下面复制简单的示例代码以进行快速测试。

```Java
import java.io.*;
import java.util.*;
 
class HelloWorld {
    
    public static void main(String[] args) throws IOException {
        PrintStream orig = System.out;

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, false, "UTF-8"));
        // 这里不会打印
        for (int i = 0; i < 2; i++) {
            System.out.println("FunTester" + i);
        }

        System.setOut(orig);
        // 打印重置前输出内容
        output("收集到的输出内容:" + os.toString("UTF-8"));

        InputStream is = System.in;
        System.setIn(new ByteArrayInputStream("FunTester\nFunTester\n".getBytes()));
        Scanner scanner = new Scanner(System.in);
        // 输出所有内容
        output(scanner.nextLine());
        output(scanner.nextLine());
        try {
            // 这里由于已经没有内容了,所以会报错
            System.out.println(scanner.nextLine());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        System.setIn(is);
        scanner = new Scanner(System.in);
        // 这里会一直等待用户输入内容
        System.out.println("输入内容:" + scanner.nextLine());
    }
```

实际上，注入和替换是一种用于分离单元测试依赖关系的常用方法，这对于仅关注代码非常有用。有很多高级和复杂的方法可以执行此操作，但是在这里，我们只想解释一种简单的方法，即`mock`以便可以专注于代码。

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