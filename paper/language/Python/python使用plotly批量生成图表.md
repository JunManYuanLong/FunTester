# python使用plotly批量生成图表


本人在使用groovy爬取了全国3000+城市的历史天气之后，需要把每个城市的历史天气都绘制一张Time Series表格，用来反映各地的最高温最低温温差的变化曲线。这里遇到了一个问题，每次plotly绘制完图标总会调起系统浏览器打开呈现，一旦我批量生成N多张表格时，电脑就会卡死了。在使用中文作为文件名的时候遇到了一个错误，这个错误刚好能巧妙解决这个问题。在不同编码格式的字符拼接时文件路径时，会报错，报错内容如下：

`'ascii' codec can't encode characters in position 69-70: ordinal not in range(128)`

然后程序停止运行，但是文件已经生成了。在做了异常处理后，刚好能满足需求。关于python2.7的编码问题，并不是很了解为什么出这个错。

python部分的代码如下：


```
#!/usr/bin/python
# coding=utf-8
 
from first.date import DatePlot
import os
from second.MysqlFission import MysqlFission
import shutil
import time
 
 
class Fission:
    x = []
    y = []
    z = []
    d = []
    def __init__(self):
        print "欢迎使用fission类!"
    # def __init__(self,x,y,z,d):
    # def __init__(self,name):
    # self.name = name
    # print "欢迎使用fission类!"
    def getData(self, name):
        size = 0;
        with open("/Users/Vicky/Documents/workspace/source_api/long/" + name + ".log") as apidata:
            for i in apidata:
                data = i.split("\r\n")[0].split("|")[0]
                low = i.split("\r\n")[0].split("|")[1]
                high = i.split("\r\n")[0].split("|")[2]
                diff = int(high) - int(low)
                self.x.append(data)
                self.y.append(low)
                self.z.append(high)
                self.d.append(diff)
                size += 1;
 
    def getDataMarkLine(self, name):
        with open("/Users/Vicky/Documents/workspace/source_api/long/" + name + ".log") as apidata:
            for i in apidata:
                data = i.split("\r\n")[0].split("|")
                day = data[0]
                time = float(data[1])
                self.x.append(day)
                self.y.append(time)
        return [self.x, self.y]
 
 
if __name__ == "__main__":
    names = []
    for name in names:
        name = u"三沙"
        sql = MysqlFission()
        sql.getWeather(name)
        fission = Fission()
        fission.x = []
        fission.y = []
        fission.z = []
        fission.d = []
        fission.getData(name)
        try:
            DatePlot.MakePlotTwo(fission.x, name, high=fission.y, low=fission.z, diff=fission.d)
        except BaseException:
            print 2
        shutil.copyfile(name + ".html", "/Users/Vicky/Desktop/w/" + name + ".html")
        os.remove(name + ".html")
        time.sleep(5)
```
下面是北京市的效果图：
![](http://pic.automancloud.com/20180913143124136.png)

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)

## 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [功能测试与非功能测试](https://mp.weixin.qq.com/s/oJ6PJs1zO0LOQSTRF6M6WA)

## 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
- [JVM的对象和堆](https://mp.weixin.qq.com/s/iNDpTz3gBK3By_bvUnrWOA)