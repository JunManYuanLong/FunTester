# python微信+requests+图灵机器人制作聊天机器人
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
在使用过python微信之后，对于其功能和图灵机器人做了一下整合，这样在用户消息不包含关键字的时候，默认用户处于聊天模式，可以进行一些轻松的对话，比如讲讲笑话，看看新闻，查查天气一类的操作，在加上接入第三方翻译接口，实现更加丰富的功能。这里分享一下接入图灵聊天机器人的代码：


```
#!/usr/bin/python
# coding=utf-8
 
from wxpy import *
import os
import time
import requests
import json
 
bot = Bot(cache_path=True)
fs = bot.friends()
gs = bot.groups()
@bot.register(Friend, TEXT)
def auto_reply_friend(msg):
    print msg
    m = msg.text
    friend = msg.sender
    if "@" not in m:
        j = dict(
            reqType=0,
            perception=dict(
                inputText=dict(
                    text=m
                    )
                ),
            userInfo=dict(
                apiKey="***********",
                userId=fs.index(friend)
                )
            )
        r = requests.post("http://openapi.tuling123.com/openapi/api/v2",json=j)
        info = json.loads(r.text)["results"]
        for i in range(len(info)):
            c = info[i]["values"]
            d = c.keys()
            m = c[d[0]]
            friend.send(m)
        return
    try:
        r = requests.post("http://10.10.32.155:8081/uname/"+m)
        b = json.loads(r.text)["data"][u"用户token："]
        friend.send(b)
    except BaseException:
        friend.send(u"你输入的账号不存在！")
embed()
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>