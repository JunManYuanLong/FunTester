# MacOS如何修改MPplayX修改快进时间（MPlayerX的隐藏设置）
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

近期我开始用上了传说级的播放器MPlayX，但是在MacOS系统下好像能设置项并不多，特别是快进时间这一项，默认一次性快进10秒，有点长了。在网上搜了一些解决方案并不好用，只能去官网看文档了，幸好被我找到了。话不多说，直接放命令：

```
fv@localhost:~/Library/Preferences$ defaults write org.niltsh.MPlayerX SeekStepTimeL -float -5
fv@localhost:~/Library/Preferences$ defaults write org.niltsh.MPlayerX SeekStepTimeR -float 5

```
想必大家都能看懂吧，命令只是$后面的内容，官网给出的格式如下：


```
cd ~/Library/Preferences
defaults write org.niltsh.MPlayerX Name -type Value

```
附上官网地址：[MPlayerX的隐藏设置](http://blog.mplayerx.org/blog/2013/02/01/hidden-settings/)

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>