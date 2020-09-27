# 利用Python+plotly制作接口请求时间的violin图表
本人在做接口测试的过程中，每次请求接口时都会自己计算一个请求时间存在数据库里，时间一长积累了很多数据，在学习Python+plotly进行数据可视化后，终于对接口请求时间这个数据进行了处理，制作了violin图表，效果还不错。分享一下代码，供大家参考。

下面是我自己的测试方法：


```
#!/usr/bin/python
# coding=utf-8
 
import plotly.plotly
import pandas as pd
import plotly.figure_factory as ff
import second.mysql
 
if __name__ == "__main__":
    a = second.mysql.Mysql()
    b = a.getApiTimes('/article/list/userfeed', '/article/detail', '/article/info', '/advertise/api/list', '/common/menu')
    df = pd.DataFrame(dict(Score=b[0], Group=b[1]))  # 合并数据
    fig = ff.create_violin(df, data_header='Score', group_header='Group',
        height=700, width=1200,title='接口请求时间')
    plotly.offline.plot(fig)
```
下面是mysql里面的getapitimes()方法：


```
    def getApiTimes(self, *params):
        num = str(params.__len__())
        print "接口数：" + num
        conn = self.conn
        # 获取链接
        cur = conn.cursor()
        data = []
        size = []
        for api in params:
            cur.execute("SELECT * FROM api_result WHERE api_name = \"" + api + "\"")
            dfs = cur.fetchall()
            # 排除异常数据
            for row in dfs:
                if row[7] < 1:
                    data.append(row[7])
                    size.append(api)
        cur.close()
        conn.commit()
        conn.close()
        return [data, size]
```
下面是几个接口的效果图：

![](http://pic.automancloud.com/20171216135927061.png)

这下再去看接口请求的响应时间，简直太直接了。用来做PPT和报告再好不过了。

### 技术类文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
8. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
9. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
10. [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
11. [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)
12. [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
13. [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)


### 非技术文章精选
1. [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
2. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
3. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
4. [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
5. [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
6. [测试之《代码不朽》脑图](https://mp.weixin.qq.com/s/2aGLK3knUiiSoex-kmi0GA)
7. [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
8. [优秀软件开发人员的态度](https://mp.weixin.qq.com/s/0uEEeFaR27aTlyp-sm61bA)
9. [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)

