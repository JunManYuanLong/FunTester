# python plotly处理接口性能测试数据方法封装（Violin图和折线图）


本人最近接到一个服务器性能测试需求，在做完测试准备执行完测试用例之后，在处理测试数据的时候使用的python图形化工具plotly，之前写过一些脚本都是实现教程的代码，借此机会正好进行了方法的封装。分享代码，供大家参考。

下面是python读取文件的类代码：


```
#!/usr/bin/python
# coding=utf-8
 
from second import Violin as vv
 
 
class Fission:
    z = []
    x = []
 
    def __init__(self):
        print "欢迎使用fission类!"
 
    def getData(self, name):
        size = 0;
        with open("/Users/Vicky/Documents/workspace/api_test/long/" + name + ".log") as apidata:
            for i in apidata:
                data = i.split("\r\n")[0]
                time = float(data)
                if time > 2: continue
                self.z.append(data)
                size += 1;
        length = size;
        for i in range(length):
            self.x.append(name + "线程")
 
 
if __name__ == "__main__":
    vvv = vv.Violin()
    fission = Fission()
    for i in range(10,23,1):
        aa = str((i + 1) * 10)
        fission.getData(aa)
    vvv.makeViolin(fission.z, fission.x, "接口性能测试", "110-230")
```
下面是生成voilin图标的方法封装：

```
#!/usr/bin/python
# coding=utf-8
 
import plotly.plotly
import pandas as pd
import plotly.figure_factory as ff
 
 
class Violin:
    def __init__(self):
        print "violin图表生成类！"
 
    def makeViolin(self, score, group, title, name):
        data = pd.DataFrame(dict(Score=score, Group=group))
        fig = ff.create_violin(data, data_header='Score', group_header='Group', height=700, width=1200, title=title)
        plotly.offline.plot(fig, filename=name + ".html")
```
下面是生成的图标的截图：

![](http://pic.automancloud.com/20180601124713906.png)

下面是QPS的图截图：

![](http://pic.automancloud.com/20180601124813351.png)


### 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)



### 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
- [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [17种软件测试人员常用的高效技能-上](https://mp.weixin.qq.com/s/vrM_LxQMgTSdJxaPnD_CqQ)
- [17种软件测试人员常用的高效技能-下](https://mp.weixin.qq.com/s/uyWdVm74TYKb62eIRKL7nQ)

### 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)


# [点击查看公众号地图](https://mp.weixin.qq.com/s/l_zkWzQL65OIQOjKIvdG-Q) 