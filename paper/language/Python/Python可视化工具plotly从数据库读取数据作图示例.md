# Python可视化工具plotly从数据库读取数据作图示例

本人在学习使用Python数据可视化工具plotly的过程中，实际的需求是将数据库中的数据展示出来，经过尝试终于完成了第一步，把数据库某列数据取出来，然后再在本地生成html文件。下面分享一下代码，供大家参考。


```
#!/usr/bin/python
# coding=utf-8
 
import pymysql
import plotly.plotly
from plotly.graph_objs import *
import plotly.graph_objs as abc  # 必须
 
host = "192.168.10.111"
user = "*****"
passwd = "*****"
db = "api_test"
port = 3306
charset = "utf8"
# 新建链接
conn = pymysql.connect(
    host=host,
    port=port,
    user=user,
    passwd=passwd,
    db=db,
    charset=charset,
)
# 获取链接
cur = conn.cursor()
# 执行sql
re = cur.execute("SELECT * FROM api_result WHERE api_name = \"/article/list/userfeed\"")
# 返回结果
dfs = cur.fetchall()
# 存放查询结果
sss = []
# 排除异常数据
for row in dfs:
    if row[7] < 1:
        sss.append(row[7])
# 关闭链接
cur.close()
conn.commit()
conn.close()
# 获取结果长度
length = sss.__len__()
# 新建x轴数组
listx = []
for i in range(length):
    listx.append(i)
data_1 = abc.Scatter(
    x=listx,
    y=sss,
    name='test1',  # 名称
    mode='markers',
    # 格式
    marker=dict(
        size=10,  # 点的大小
        color="rgba(255,47,167,.9)",  # 点的颜色
        line=dict(  # 点外围环的属性
            width=2,  # 环的宽度
            color='rgb(2,2,2)'  # 环的颜色
        )
    )
)
data1 = Data([data_1])
plotly.offline.plot(data1)
```

中间根据官网提供的教程对图标进行了美化，主要是修改了点的颜色和外环的属性。

下面是效果图：
![](http://pic.automancloud.com/20171214095206667.png)


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

# [点击查看公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>