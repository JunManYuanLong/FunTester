# android uiautomator一个画心形图案的方法--代码的浪漫

本人在uiautomator学习math方法的时候，突发奇想想画一个心形的图案，试了几次终于成功了。分享出来，请大神指正。其中主要用到了数学上心的极坐标方程式，然后通过math类的一些方法进行转化，使用swipe方法在手机屏幕上滑动。手机屏幕坐标点（x,y）与数学上的坐标系有些差异，需要转换。

```
    public void heart(int x, int y, int r) {//画心形的方法
        double d = (double) (Math.PI / 30);
        double[] angle = new double[61];//设置角度差
        for (int i = 0; i < 61; i++) {
            angle[i] = i * d;
        }
//建立一个角度差double数组
        double[] ox = new double[61];
        for (int i = 0; i < 61; i++) {
            ox[i] = r * (2 * Math.cos(angle[i]) - Math.cos(2 * angle[i]));
        }
//计算x坐标
        double[] oy = new double[61];
        for (int i = 0; i < 61; i++) {
            oy[i] = r * (2 * Math.sin(angle[i]) - Math.sin(2 * angle[i]));
        }
//计算y坐标
        Point[] heart = new Point[61];
        for (int i = 0; i < 61; i++) {
            heart[i] = new Point();
            heart[i].x = (int) oy[i] + x;
            heart[i].y = -(int) ox[i] + y;
        }
//简历一个点数组，这里坐标一定要转化一下，不然是倒着的心形
        getUiDevice().swipe(heart, 2);
    }
```

x和y表示心中心坐标，r是半径，都是参数。这个方法和画圆类似，不过改了一下数学方程，旋转了一下坐标。


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





> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>