# Python2.7使用plotly绘制本地散点图和折线图实例


本人在学习使用Python和plotly处理数据时，经过两个小时艰难试错，终于完成了散点图和折线图的实例。在使用过程中遇到一个大坑，因为官方给出的案例是用在线存储的，所以需要安装jupyter（也就是ipython）才能使用notebook来处理生成的文件，一开始我没太懂iplot和plot之间的差异，导致浪费了很多时间。

`重要提示：最新的jupyter不支持Python3.2及以下版本。`
![](/blog/pic/20171208181728920.jpeg)

最后我只能继续采用本地文件的形式来解决这个问题了。下面放出我的测试代码，被注释掉的是官方给出的代码以及离线存储的代码。应该是最新版的Python的方案。


```
#!/usr/bin/python
# coding=utf-8
 
import plotly.plotly
import random
from plotly.graph_objs import *
import plotly.graph_objs as abc  # 必须
import numpy as np
 
 
def sayHello():
    N=100
    xx = [];
    for i in range(20):
        xx.append(i)
    y0 = [];
    for i in range(20):
        y0.append(random.randint(0, 10))
    y1 = [];
    for i in range(20):
        y1.append(random.randint(10, 20))
    y2 = [];
    for i in range(20):
        y2.append(random.randint(20, 30))
    #xx = np.linspace(0, 1, N)
    #y0 = np.random.randn(N) + 5
    #y1 = np.random.randn(N)
    #y2 = np.random.randn(N) - 5
    data_1 = abc.Scatter(
        x=xx,
        y=y0,
        name='test1',
        mode='markers'
    )
    date_2 = abc.Scatter(
        x=xx,
        y=y1,
        name='test2',
        mode="lines"
    )
    date_3 = abc.Scatter(
        x=xx,
        y=y2,
        name='test3',
        mode="lines+markers"
    )
    '''
    N = 1000
    random_x = np.random.randn(N)
    random_y = np.random.randn(N)
    # Create a trace
    trace = abc.Scatter(
        x=random_x,
        y=random_y,
        mode='markers'
    )
    data1 = [trace]
    '''
    data1 = Data([data_1, date_2,date_3])
    plotly.offline.plot(data1)
    #plotly.offline.iplot(data1,filename='test01')
 
 
if __name__ == "__main__":
    sayHello()
```

下面是我最终结果的截图：

![](/blog/pic/20171208182042824.png)


## 往期文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
8. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
9. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
10. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
11. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)

## [公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>