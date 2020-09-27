# window系统权限不足导致gradle构建失败的解决办法
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

本人在使用window搭建环境的过程中遇到了一个gradle构建失败的的问题，困扰了很久，具体构建报错提示如何：

`gradle location is incorrect`

在搜索了这个错误之后，尝试了很多方案，修改gradle路径的，添加gradle环境变量，添加GRADLE_HOME的，自定义本地仓库，移动gradle软件到工程目录的，均无法解决问题。看来这个问题比较少见吧。只能自己研究研究了。

打开详细日志：
`Could not create parent directory for lock file C:\Program Files (x86)\gradle-4.6\repository\wrapper\dists\gradle-4.10-bin\bg6py687nqv2mbe6e1hdtk57h\gradle-4.10-bin.zip.lck`

看到这个我突然想到了window系统权限，默认的是没有操作C盘的权限的，我立马换到了D盘，于是乎，一切正常了。可能用是MacOS习惯了，突然切到window的忘掉了很久排查问题的方法。

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>