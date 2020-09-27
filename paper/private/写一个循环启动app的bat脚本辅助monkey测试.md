# 写一个循环启动app的bat脚本辅助monkey测试
本人在使用monkey测试app的时候，感觉那个启动acitivity的参数的设置好像不咋好用，具体原因不是太清楚，可能是我设置的间隔时间的问题。大概设置的比例30%-40%的样子，但是很少能看到重启app的，在学了一些bat的知识后，想得了用bat也能做，之前想的是用java来做。下面分享一下bat代码，供大家参考。


```
rem 程序说明
cls
Title [循环启动app]
::begin-----------------------------------
@echo off
:backhere
echo 休眠1分钟！
Wscript sleep.vbs
echo 休眠结束，启动app
call 打开课堂app-nexus.bat
echo 启动app成功！
goto backhere 
::end-----------------------------------
```
其中sleep.vbs的内容一句话：

`WScript.sleep 60000`

单位是毫秒。关掉窗口即可结束。


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>