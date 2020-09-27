# Python使用plotly生成本地文件教程


本人在学习使用Python和plotly处理数据的过程中，发现了官网教程和网上一些教程无法正常使用的情况，可能是因为更新导致的，所以我在尝试成功之后想自己写一个教程，便有了下面的文章。如果错误的地方，还请大神指正。我的Python版本是2.7，Mac机器。Python的IDE是pycharm。本教程主要还是依据官网教程+翻译实践。

首先是安装plotly，这个比较简单，网上其他教程也没什么错误，提供一个方法，使用pip安装。


```
$ pip install plotly 
or 
$ sudo pip install plotly 
or update
$ pip install plotly --upgrade
```
其次你得有个自己的账号，plotly官网里面Python的地址如下：
[plotly官网传送门](https://plot.ly/python/)
然后进入交互模式进行如下操作：


```
import plotly 
plotly.tools.set_credentials_file(username='DemoAccount', api_key='lr1c37zw81')
```
这一步需要设置你的用户名和api私钥。如果还没有的请移步官网个人中心获取私钥。
[plotly官网传送门](https://plot.ly/settings/api)

下面是我的个人中心截图：
![](/blog/pic/20171207175202197.png)
然后，需要去本机确认是否成功，文件地址如下：

`~/.plotly/.credentials`

我的文件内容如下：

![](/blog/pic/20171207175424688.png)

这些完成之后，就可以轻松地开始代码操作了。

如果你使用不是本地，而是在线的话，那么你还得再交互模式下设置几个地方，下面就放一下官网的内容：


```
import plotly 
plotly.tools.set_config_file(world_readable=False, sharing='private')
```

```
import plotly 
plotly.tools.set_config_file(plotly_domain='https://plotly.your-company.com', plotly_streaming_domain='stream-plotly.your-company.com')
```
具体的内容大家移步官网，自己翻译一下内容吧，这些都是隐私和权限的内容。
下面是我的Python代码：

```
#!/usr/bin/python
# coding=utf-8
 
import plotly.plotly
import random
from plotly.graph_objs import *
import plotly.graph_objs as abc#必须有个
 
listx = [];
for i in range(20):
    listx.append(i)
print listx
listxx = listx
listy = [];
for i in range(20):
    listy.append(random.randint(12, 20))
print listy
listyy = [];
for i in range(20):
    listyy.append(random.randint(12, 20))
print listy
data_1 = abc.Scatter(
    x=listx,
    y=listy
)
date_2 = abc.Scatter(
    x=listxx,
    y=listyy
)
data1 = Data([data_1, date_2])
plotly.offline.plot(data1)
```
下面是官网的测试代码：

```
import plotly.plotly as py
from plotly.graph_objs import *
 
trace0 = Scatter(
    x=[1, 2, 3, 4],
    y=[10, 15, 13, 17]
)
trace1 = Scatter(
    x=[1, 2, 3, 4],
    y=[16, 5, 11, 9]
)
data = Data([trace0, trace1])
 
py.plot(data, filename = 'basic-line')
```
大家可以尝试一下官网的代码，我一直报错，不能通过，必须是在下面这行代码时才能使用 as代码。
`import plotly.graph_objs as abc#必须`
下面放一张我本地的图标，我并没有直接在代码里保存图片，只是放了一张截图。

![](http://pic.automancloud.com/20171207180725532.png)


### 往期文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
8. [测试之JVM命令脑图](https://mp.weixin.qq.com/s/qprqyv0j3SCvGw1HMjbaMQ)
9. [将json数据格式化输出到控制台](https://mp.weixin.qq.com/s/2IPwvh-33Ov2jBh0_L8shA)
10. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
11. [“双花”BUG的测试分享](https://mp.weixin.qq.com/s/0dsBsssNfg-seJ_tu9zFaQ)
12. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
