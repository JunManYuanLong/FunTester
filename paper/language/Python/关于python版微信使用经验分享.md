# 关于python版微信使用经验分享
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

本人最近在使用python版的微信做了一些模拟操作，使用的wxpy封装好的框架api，聊天机器人接入的是图灵，其他的暂时还没有功能的接入计划。

在实施的过程中遇到了很多问题，大部分是微信api过时导致了，因为本身调用的是web微信的api，我看到git上的项目已经最近的更新时间是2017年，中间很多次微信的api调整都没更新到，比如添加好友，比如被@检测等等。

今天分享一下自己的使用经验和一些自己的封装方法：

* 朋友列表和群列表，因为会自动同步最近活跃的群，所以如果新添加的群需要重新获取一下groups。api提供了一个search的搜索方法，使用起来稍微麻烦一些，我换了一种思路，直接下标获取好友和群。方法如下：

```
def fslist():
    fs= bot.friends()
    a = 0
    for i in fs:
        print a
        a +=1
        print i
def gslist():
    gs= bot.groups()
    a = 0
    for i in gs:
        print a
        a +=1
        print i
```
直接可以通过元素索引角标直接获取元素。
* 撤回消息。这个我只是对原来的recall()方法做了简单封装，这里也见识了python的简洁性。主要是在调试模式中使用。

```
def re(*msg):
    if not msg  == ():
        ms=bot.messages.search(msg)[-1]
        ms.recall()
    else:
        ms=bot.messages[-1]
        ms.recall()
    print ms
```
* 发送图片文件，主要也是在调试模式中使用。

```
def sendpic(user,name):
    user.send_image('/Users/Vicky/Downloads/'+name)
    print user
    print "发送了图片"+name
```
* 聊天机器人对话，本人接的图灵的，接入方法请参照[python微信+requests+图灵机器人制作聊天机器人](/blog/article/利用python wxpy和requests写一个自动应答微信机器人实例.html)，我这里只写一个封装过后的方法，方便调用。


```
def getTulingReplay(msg,friend):
    userid = ""
    if friend in fs:
        userid = fs.index(friend)
    if friend in gs:
        userid = gs.index(friend)
    j = dict(
            reqType=0,
            perception=dict(
                inputText=dict(
                    text=msg
                    )
                ),
            userInfo=dict(
                apiKey="***",
                userId=userid
                )
            )
    r = requests.post("http://openapi.tuling123.com/openapi/api/v2",json=j)
    info = json.loads(r.text)["results"]
    c = info[0]["values"]
    d = c.keys()
    m = c[d[0]]
    print m
    friend.send(m)
```
* 保存聊天记录。这个原需求是想整理一下群聊天记录，还有那些撤回的，现在做分析用。只存本地，尚未清洗数据到数据库。只有文本信息。

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>