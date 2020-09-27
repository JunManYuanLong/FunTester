# java和python程序使用文件通信时，不同系统分行符导致打印文件内容不全的原因分析
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
本人在做接口测试的过程中，需要增加一个微信通知的功能，因为测试工程是用java写的，而微信通知是用python写的，这就需要在java程序和python程序进行通信，本文采用了用文件读写的方式进行。在此过程中遇到一个bug，在java端测试程序将所要通知的信息（有分行）保存成文件后，在python程序读取信息的过程中，打印文档内容时只能打印最后一行的内容，但是将内容发送到微信平台上，却收到了完整的消息内容。

下面是我的java保存的文档的信息：


```
2018-04-11 11-48-08
	2018-04-11 11-48-09
/Users/Vicky/Documents/workspace/fission
```
下面用python读取打印的内容：


```
2018-04-11 11-48-08
 
---------------
	2018-04-11 11-48-09
 
---------------
/Users/Vicky/Documents/workspace/fission
---------------
---------------
/Users/Vicky/Documents/workspace/fission
```
下面是我读取文件时候的代码：


```
 with open(wx + f) as msg:  # 读取信息文件
                n = ""
                for i in msg:
                    print i
                    print "---------------"
                    i = i.strip("\n")
                    n = n + i
                print "---------------"
                print n
```
下面是我java程序保存推送信息的方法：
`saveMessage(getNow() + LINE + TAB + getNow() + LINE + WorkSpace);`
下面是两个常亮：

```
public static String LINE = "\r\n";
	public static String TAB = "\t";
```

可以看出最后一次打印的n只有最后一行。经过查找资料排查原因，终于找到了问题所在。原来我在保存文本的时候分行符用的是“\r\n”的形式，之前一直在Windows平台运行，一直没出过问题，这次在调试通知程序的时候却一直打印不了完整的文件内容，但是通知内容却是完整的。

下面复制一下相关知识点：

\r 和 \n 都是以前的那种打字机传承来的。
\r 代表回车，也就是打印头归位，回到某一行的开头。
\n代表换行，就是走纸，下一行。
linux只用\n换行。

win下用\r\n表示换行。

\r真正实现了其回车的功能（回到行开头，相当于清空了此行）

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>