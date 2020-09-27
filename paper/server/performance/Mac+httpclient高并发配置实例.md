# Mac+httpclient高并发配置实例


本人近期在做服务端的压力测试，在准备测试脚本的过程中遇到了一些配置方面的问题，在经过查阅资料和对比结果后总结了在Mac电脑配置和httpclient配置上的经验。分享出来供大家参考。（备注：Science Internet），以下部分解决方案源于Google搜索结果和httpclient官方文档。

首先在Mac上的上遇到问题是大量端口处于TIME_WAIT状态，这里先不说代码层面的，具体的配置如下：

下面是修改Mac可用最大连接数和使用端口的相关参数：

```
sudo sysctl -w kern.maxfiles=1048600     最大连接数
sudo sysctl -w kern.maxfilesperproc=1048576  单进程最大连接数
```
注意单进程与单线程不一样。下面是设置端口的：

```
net.inet.ip.portrange.first: 10240   起始值 
 
net.inet.ip.portrange.last: 65535   结束值
```
还有一个关于临时端口的设置：


```
sysctl -w net.inet.ip.portrange.hifirst = 16384 
sysctl -w net.inet.ip.portrange.first = 16384
```
这个配置允许使用49151个端口，与上面那个设置略有冲突但不影响，有兴趣童鞋可以去原贴看看。[传送门](https://raby.sh/osx-where-are-my-time_wait.html)

下面是httpclient设置：

目前我一共查阅了几种方案，一一尝试了一下，说一下结果：

第一种设置request请求头的，具体代码如下：

```
method.setRequestHeader("Connection", "close");  
```

还有一个设置kipeAlive属性的，这里就不发代码了，因为我看到这个方法的代码里面用的还是SimpleHttpConnectionManager连接池管理类，而我用的4.5已经不推荐这个了，现在用的是PoolingHttpClientConnectionManager类，所以抱有一丝疑惑。在实际的使用中，并没有发现对连接池性能有特别明显的提升。故而放弃了这个方法。

第二种是在设置request.releaseConnection()方法。

`method.releaseConnection()`

这个方法跟上一个也出出现在httpclient较早的版本里面的，但是并未说不推荐了，这个方法资料比较少。实际使用效果来说会导致一部分的SocketException，比如超时，比如关闭之类的。故而直接放弃掉了，在普通接口测试中并未比较区别。有兴趣的童鞋可以尝试一下，希望能告知答案。

第三种是自己定时执行链接的回收，用的是httpclient官方给出的方法写了一个多线程类：


```
public class GCThread extends Thread {
	private static boolean flag = true;
 
	@Override
	public void run() {
		while (flag) {
			SourceCode.sleep(1);
			ApiLibrary.recyclingConnection();
		}
	}
 
	public static void stopThread() {
		flag = false;
	}
}
```
设置完上面这些剩下的就是httpclient的相关设置了，本人总结了一下，大概两个方面：连接池设置和超时设置。

连接池设置：

```
		此处解释下MaxtTotal和DefaultMaxPerRoute的区别：
		1、MaxtTotal是整个池子的大小；
		2、DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分；比如：
		MaxtTotal=400 DefaultMaxPerRoute=200
		只连接到某个网站时，到这个主机的并发最多只有200；而不是400；
		每个主机的并发最多只有200；即加起来是400（但不能超过400）；所以起作用的设置是DefaultMaxPerRoute。*/
		connManager.setMaxTotal(MAX_TOTAL_CONNECTION);
		connManager.setDefaultMaxPerRoute(MAX_PER_ROUTE_CONNECTION);
```

超时设置：
`RequestConfig.custom().setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).setCookieSpec(CookieSpecs.STANDARD).build();`    
附上还httpclient官方文档地址：[传送门](http://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/impl/conn/PoolingHttpClientConnectionManager.html)

我用的httpclient4.5.5的包，本机iMac，并发超过20k，暂时未见到本机的瓶颈。希望能对各位有点帮助。

### 往期文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
8. [测试之JVM命令脑图](https://mp.weixin.qq.com/s/qprqyv0j3SCvGw1HMjbaMQ)
9. [将json数据格式化输出到控制台](https://mp.weixin.qq.com/s/2IPwvh-33Ov2jBh0_L8shA)
10. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
11. [“双花”BUG的测试分享](https://mp.weixin.qq.com/s/0dsBsssNfg-seJ_tu9zFaQ)
12. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
13. [Mac+httpclient高并发配置实例](https://mp.weixin.qq.com/s/r4a-vGz0pxeZBPPH3phujw)

## [公众号地图](https://mp.weixin.qq.com/s/36RbP20beZ8oWJ9nLAxG3g) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)