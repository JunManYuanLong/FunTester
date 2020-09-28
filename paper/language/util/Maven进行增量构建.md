# Maven进行增量构建



如果要开始任何新的基于`Java`的项目，则`gradle`应该是第一选择，但是某些场景或者某些方面，`Maven`依然有着不错的优势。在编译构建项目时，就会需要一些插件来提供不同的功能支持。

`Maven Java`编译器插件对增量编译提供了不错的支持，但它无法处理一些极端情况，例如：

* 源文件夹中文件更改时触发编译。
* 不更改代码时跳过单元测试。

在大多数情况下，为了处理已删除文件的情况，必须运行`mvn clean install`，这意味着将编译完整代码并执行单元测试。 

偶然发现有一个插件可以解决这个两个问题：
 
* 更改代码后触发对应的文件编译并触发完整版本构建。
* 在不更改代码的情况下跳过单元测试执行。
 
这两个功能都可以帮助大大减少编译时间，因为在大多数情况下，只有很少的模块被更改并且可以使用以前的生成输出。您可以通过启用此插件来快速构建。

## 如何使用插件

该插件是在预清理阶段添加的，将以下条目添加到`pom.xml`并使用`mvn pre-clean install`。


```XML
				<plugin>
                <groupId>mavenplugin</groupId>
                <artifactId>compilerplugin</artifactId>
                <version>1.0-SNAPSHOT</version>
                <executions>
                    <execution>
                        <id>pre-clean</id>
                        <phase>pre-clean</phase>
                        <goals>
                            <goal>inc</goal>
                        </goals>
                    </execution>
                </executions> 
            </plugin>
```

---
* **郑重声明**：公众号“FunTester”首发，欢迎关注交流，禁止第三方转载。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [压测中测量异步写入接口的延迟](https://mp.weixin.qq.com/s/odvK1iYgg4eRVtOOPbq15w)
- [多种登录方式定量性能测试方案](https://mp.weixin.qq.com/s/WuZ2h2rr0rNBgEvQVioacA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [多项目登录互踢测试用例](https://mp.weixin.qq.com/s/Nn_CUy_j7j6bUwHSkO0pCQ)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [选择手动测试还是自动化测试？](https://mp.weixin.qq.com/s/4haRrfSIp5Plgm_GN98lRA)
- [自动化测试项目为何失败](https://mp.weixin.qq.com/s/KFJXuLjjs1hii47C1BH8PA)
- [简化测试用例](https://mp.weixin.qq.com/s/BhwfDqhN9yoa3Iul_Eu5TA)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzBiaBZzt2rchWvBn0pztDTcYwUrHyWvCCIxiaHORQ1xe1vID42zWVicABw6dHibFChrlbFqVR5vO96eVQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)