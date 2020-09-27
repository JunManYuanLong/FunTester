# httpclient遇到socket closed解决办法

本人在做接口功能自动化测试的过程中遇到一个一个问题，如果请求过于频繁后，总会报一个java.net.SocketException: socket closed异常，在研究完代码之后发现了一个问题，在请求结束之后我做一个释放释放链接的方法。
很早之前写的这个了，没想到访问量还挺高，为了不误人子弟特意来补充：本人用的httpclient包是4.5.5，releaseConection（）方法已经弃用了。用现在的PoolingHttpClientConnectionManager来管理连接池，不需要这个方法。

异常想信息如下：
`java.net.SocketException: socket closed`
下面是我的错误代码：

`request.releaseConnection();//此处容易造成socket close`
我想了一下，查阅了一些资料，这个releaseconnection()的方法，是释放该链接之后并不关闭，这样这个链接就可以重复使用了。官方的文档中表达如下：

`This is a crucial step to keep things flowing. We must tell HttpClient that we are done with the connection and that it can now be reused. Without doing this HttpClient will wait indefinitely for a connection to free up so that it can be reused.`

翻译过来的意思大概是，这个是资源流动利用的关键。必须告诉httpclient，这个链接释放掉可以被重复使用。使用这个方法的好处就是，不需要等待有一个空闲的httpclient才能执行下一个链接。

 
我代码出现这个问题的原因是在链接释放后，服务端主动关闭了这个链接。我又查了一些资料，印证了自己的猜想，因为这个方法实在连接池使用的过程中释放链接的办法，连接池管理器就会关闭这个链接重复让别的请求使用。至此，问题找到了。

## 往期文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
8. [将json数据格式化输出到控制台](https://mp.weixin.qq.com/s/2IPwvh-33Ov2jBh0_L8shA)
9. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
10. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
11. [Mac+httpclient高并发配置实例](https://mp.weixin.qq.com/s/r4a-vGz0pxeZBPPH3phujw)
12. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
