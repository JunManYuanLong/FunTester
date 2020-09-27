# android UiAutomator利用am命令启动和结束进程
最近在学习UiAutomator的时候看到UiAutomator可以利用am命令启动和结束进程，自己练习了一下，感觉效果挺不错的。分享一下经验。

```
//启动QQ
Runtime.getRuntime().exec("am start -n com.tencent.mobileqq/.activity.SplashActivity");

//关闭QQ,如果运行中想结束得加上waitfor();
Runtime.getRuntime().exec("am force-stop com.tencent.mobileqq").waitFor();
```
至于如何获取报名和活动路径，可以直接从手机log日志里面找到，很方便，网上教程很多，这里就不写了。

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>