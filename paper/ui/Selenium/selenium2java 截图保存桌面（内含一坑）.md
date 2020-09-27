# selenium2java 截图保存桌面（内含一坑）
本人在学习完UiAutomator，继续selenium2java的时候，想把UiAutomator的一些方法搬到selenium2java里面来，期间遇到截图保存的一个坑，就是图片命名。由于window系统不允许“:”在文件名中出现，导致截图失败。

```
//截图命名为当前时间保存桌面
public static void takeScreenshotByNow(WebDriver driver) throws IOException {
File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
String file = "C:\\Users\\user\\Desktop\\"+getNow()+".png";
FileUtils.copyFile(srcFile,new File(file));
}
```

下面是getnow()的方法，坑就在这里了，我用汉字替换了“:”。


```
//获取当前时间
public static String getNow() {
Date time = new Date();
SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd HH点mm分ss秒");
String c = now.format(time);
return(c);
}
```
顺便写一个截图保存桌面的自定义名字的方法


```
//截图重命名保存至桌面
public static void takeScreenshotByName(WebDriver driver, String name) throws IOException {
File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
String file = "C:\\Users\\user\\Desktop\\"+name+".png";
FileUtils.copyFile(srcFile,new File(file));
}
```

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

## [公众号地图](https://mp.weixin.qq.com/s/36RbP20beZ8oWJ9nLAxG3g) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>