# java一行代码打印心形


之前看到Python一行代码就可以在控制台打印心形，感觉非常神奇，昨天突然想起来其实java应该也是可以的，在stream加持执行下，分享代码如下：

```
 IntStream.range(-15, 15).map(y -> -y).forEach(y -> IntStream.range(-30, 30).forEach(x -> System.out.print(Math.pow(Math.pow((x * 0.05), 2) + Math.pow((y * 0.1), 2) - 1, 3) - Math.pow(x * 0.05, 2) * Math.pow(y * 0.1, 3) <= 0 ? "love".charAt(Math.abs((y - x) % 4)) : " " + (x == 29 ? "\n" : ""))));
```

![](http://pic.automancloud.com/QQ20190716-103607@2x.png)

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载，更多原创文章：**[FunTester原创专辑](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**。

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
