# python plotly制作接口响应耗时的时间序列表（Time Series）


本人在做工作中，要对某一个接口的响应耗时进行一个长期的统计，由于之前的数据全都写在了数据库中，统计了半年多的数据。在学习了plotly的Time Series 时间序列图标之后，绘制了一张接口响应耗时的图标，分享代码，供大家参考。

下面是从数据库读取数据的java代码：


```
 JSONObject data = new JSONObject();
        ResultSet resultSet = MySqlTest.excuteQuerySql("SELECT DATE(create_time),AVG(elapsed_time) *1000 FROM request_record WHERE api_name in(\"/service/user/v3/login/mobile/v5\",\"/service/user/v3/login/mobile/v4\") GROUP BY DATE(create_time) ORDER BY DATE(create_time);");
        while (resultSet.next()) {
            String name = resultSet.getString(1);
            double time = resultSet.getDouble(2);
            data.put(name, time);
        }
        resultSet.close();
        Save.saveJsonList(data, "apitime");
```
下面是读取文件的方法和生成表格的调用类的代码：


```
#!/usr/bin/python
# coding=utf-8
 
from first.date import DatePlot
 
 
class Fission:
    x = []
    y = []
    z = []
 
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
 
    def getDataMarkLine(self, name):
        with open("/Users/Vicky/Documents/workspace/api_test_najm/long/" + name + ".log") as apidata:
            for i in apidata:
                data = i.split("\r\n")[0].split("|")
                day = data[0]
                time = float(data[1])
                self.x.append(day)
                self.y.append(time)
        return [self.x, self.y]
 
 
if __name__ == "__main__":
    fission = Fission()
    a = fission.getDataMarkLine("apitime")
    DatePlot.MakePlot(a[0], a[1], "time")
```
下面是生成时间序列表的封装类（我保留了多条折现的方法以及注释了显示规定日期间隔数据的方法）：


```
#!/usr/bin/python
# coding=utf-8
 
import plotly.graph_objs as drive
import plotly.plotly
 
 
class DatePlot:
    def __init__(self):
        print "时间表格！"
 
    @staticmethod
    def MakePlot(x, y, titile):
        a = drive.Scatter(
            x=x,
            y=y,
            name="SSSSS",
            line=dict(color='#17BECF'),
            opacity=1
        )
 
        b = drive.Scatter(
            x=["2016-02-20", "2016-02-21", "2016-02-23"],
            y=[28.04, 20, 33],
            name="AAAAA",
            line=dict(color='#7F7F7F'),
            opacity=0.8
        )
 
        data = [a]
 
        layout = dict(
            title=titile,
            # xaxis=dict(
            #     range=['2018-07-01', '2018-07-20'])
        )
        fig = dict(data=data, layout=layout)
        plotly.offline.plot(fig, filename=titile + ".html")
```
下面是效果图：

![](http://pic.automancloud.com/20180816140458859.png)


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

## 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
- [JVM的对象和堆](https://mp.weixin.qq.com/s/iNDpTz3gBK3By_bvUnrWOA)