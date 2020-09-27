# 如何对N个接口按比例压测

随着微服务盛行，公司的服务端项目也越来越多。单一的接口性能测试并不能准确反映某个服务的总体处理能力，在服务功能划分比较清晰的架构下，对于某一服务的总体性能测试也相对变得简单。下面分享一个对于某个模块对应的服务的N个接口按照固定比例（来源于线上监控）进行性能测试，基于自己写的[性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)。

场景：该服务3个接口，比例为1：2：3。

这里为了保证请求不被线程共享，我使用了自己的重写的request深度拷贝的方法[拷贝HttpRequestBase对象](https://mp.weixin.qq.com/s/kxB1c0GmSF5OAM15UQJU2Q)，这里一定要去做处理，不然线程共享会导致mark请求标记失败，一定要多注意一下`Serializable`接口的实现，不然会导致拷贝`MarkRequest`对象拷贝失败，request标记会混乱，还有一种办法就是重写`MarkRequest`的`clone()`方法也行，如果是使用Groovy语言，建议选择后者。


```java
    public static void main(String[] args) {
        def argsUtil = new ArgsUtil(args)
        def thread = argsUtil.getIntOrdefault(0, 2)
        def times = argsUtil.getIntOrdefault(1, 5)
        def split = argsUtil.getStringOrdefault(3, "1:2:3").split(":")

        def base = getBase()
        def flow = new Flow(base)
        flow.kSearch("测试")
        def request1 = FanLibrary.getLastRequest()
        flow.getPlatformK(12)
        def request2 = FanLibrary.getLastRequest()
        flow.getPaperType()
        def request3 = FanLibrary.getLastRequest()


        MarkRequest mark = new MarkRequest() {

            private static final long serialVersionUID = -2751325651625435073L;

            String m;

            @Override
            public String mark(HttpRequestBase request) {
                request.removeHeaders("requestid");
                m = m == null ? RString.getStringWithoutNum(4) : m
                String value = "fun_" + m + CONNECTOR + Time.getTimeStamp();
                request.addHeader("requestid", value);
                return value;
            }

        };


        def requests = []
        split[0].times {
            requests << new RequestThreadTime(request1, times)
        }
        split[1].times {
            requests << new RequestThreadTime(request2, times)
        }
        split[2].times {
            requests << new RequestThreadTime(request3, times)
        }
        List<HttpRequestBase> res = []
        thread.times {
            res << requests
        }

        new Concurrent(res, "对于模块**按照比例${split}压测线程数${thread}次数${times}").start()

        allOver();
    }
```

---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [Selenium 4.0 Alpha更新日志](https://mp.weixin.qq.com/s/tU7sm-pcbpRNwDU9D3OVTQ)
- [Selenium 4.0 Alpha更新实践](https://mp.weixin.qq.com/s/yT9wpO5o5aWBUus494TIHw)
- [如何统一接口测试的功能、自动化和性能测试用例](https://mp.weixin.qq.com/s/1xqtXNVw7BdUa03nVcsMTg)

## 非技术文章精选

- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)
- [API测试基础](https://mp.weixin.qq.com/s/bkbUEa9CF21xMYSlhPcULw)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)