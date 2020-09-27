# Mac执行shell脚本提示command not found的一种解决方案
本人在学习使用Mac OS的过程中，想写一个简单的安装Android apk的脚本，发现根据网上教程走完之后，总会提示一个错误。但是我检查了很多遍权限和Android包括adb环境变量配置，依然没有找到原因，后来无意间用xcode打开了一次sh脚本，发现里面时另外一种编码格式下的内容。突然想到了，我sh脚本文件是用Mac自带的文本编辑器创建的，可能是编码的问题。重新再xcode里面编辑了之后，一切完美运行。

下面是错误代码：


```
./aaaa.sh: line 1: {rtf1ansiansicpg936cocoartf1504cocoasubrtf830: command not found
./aaaa.sh: line 2: syntax error near unexpected token `}'
./aaaa.sh: line 2: `{\fonttbl\f0\fswiss\fcharset0 Helvetica;}'
```

* 请注意第一行大括号里面开头的rtf，这就是问题的关键，不像Windows那样直接修改后缀名就可以的，新建的并不是文本格式的文档。

解决办法就是用xcode或者vs code等编辑器重新编辑sh脚本。

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>