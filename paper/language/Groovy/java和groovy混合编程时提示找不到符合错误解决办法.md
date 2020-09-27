# java和groovy混合编程时提示找不到符合错误解决办法
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

本人在使用java和groovy混合编程时，发现一个问题，当java和groovy相互调用的过程中在本机执行没有任何问题，但当弄到Jenkins上之后总是报错，本机使用gradle执行build的task的时候，也是报错，信息如下：

```
 错误: 找不到符号
import com.fission.alpha.base.Util;
```
在经过同事指正之后找到了解决的办法，就是把文件名改成groovy，然后gradle添加groovy的编译插件，这样编译就会先编译groovy代码，然后再去编译java代码就不会造成这样的错误了。
修改完之后的文件结构如下：
![](/blog/pic/20181019141137474.png)
build.gradle文件修改内容如下：

```
apply plugin: 'java'
apply plugin: 'idea'
apply plugin: 'groovy'
```

[一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)

### 往期文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
8. [测试之JVM命令脑图](https://mp.weixin.qq.com/s/qprqyv0j3SCvGw1HMjbaMQ)

#### [公众号地图](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483929&idx=2&sn=5f720d91eea288869bd2242e27412262&chksm=fd4a8f2fca3d063951370261e99097122338848741480768dfb6be4e33dff641daa40afa52d0&scene=21#wechat_redirect)



> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>