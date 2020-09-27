# 基于wxpy的微信机器人来通知测试异常的例子
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
本人在学习使用wxpy的过程中，因为测试框架是java的，所以采用了文件来进行两个程序之间的通信。思路如下：首先测试异常会保存在一个文件夹下，以log形式保存，然后写一个wxpy的机器人，定时去扫描这个文件夹，如果发现了log，就及时通知到一个微信群里，（后期根据log中的等级不同，分别通知不同的人和群）。

下面分享一下机器人的代码：


```
bot = Bot(cache_path=True)
times = 0
wx = "/Users/Vicky/Documents/workspace/fission/long/wx/"
while (True):
    file = os.listdir(wx)
    for f in file:
        if os.path.exists(wx + f):
            if f.startswith("."):  # 排除隐藏系统文件,针对Mac
                continue
            times = 0
            with open(wx + f) as msg:  # 读取信息文件
                n = ""
                for i in msg:
                    i = i.strip("\n")
                    n = n + i
            test = bot.groups()[0]
            test.send(n.decode("utf-8"))
            os.remove(wx + f)
        time.sleep(2)#发送消息间隔,防止被封
    times += 1
    if (times > 10):#防止微信机器人自动下线
        friend = bot.friends().search(u"点点横点尘")[0]
        friend.send(time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())))
        times = 0
    print times
    time.sleep(30)#扫描间隔,防止被封
```
本人持续运行24小时，这个例子用了缓存模式，短时间内掉线重登是可以不用验证的，暂时看来是比较稳定可用的。

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>