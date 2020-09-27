# java用递归筛选法求N以内的孪生质数（孪生素数）--附冒泡排序和插入排序练习
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

本人最近读完一本书《质数的孤独》，里面讲到孪生质数，就想查一下孪生质数的分布情况。其中主要用到了计算质数（素数）的方法，搜了一下，排名前几的都是用for循环来做的，感觉略微麻烦了一些，在比较一些还是觉得用递归筛选法来解决这个问题。

新建List<Integer>，然后从第0位开始，如果后面的能被这个数整除，则从数组中移除改元素，以此类推，最后留下的就是质数（素数）。代码如下：

```
static void get(List<Integer> list, int tt) {
        int num = list.get(tt);
        for (int i = tt + 1; i < list.size(); i++) {
            if (list.get(i) % num == 0) list.remove(i--);
        }
        if (list.size() > ++tt) get(list, tt);
    }
```
然后再去做相邻元素差求得孪生质数（孪生素数），贴一下求10000以内孪生质数（孪生素数）全部的代码：
```
List<Integer> list = new ArrayList<>();
        for (int i = 2; i < 10000; i+=2) {
            list.add(i);
        }
        get(list, 0);
        for (int i = 0; i < list.size() - 1; i++) {
            Integer integer = list.get(i);
            Integer integer1 = list.get(i + 1);
            if (integer1 - integer == 2) outputData(TEST_ERROR_CODE, "孪生质数:", integer + TAB + TAB + integer1);
        }
```
最后附上一份冒泡排序和插入排序的练习代码：

```
   public static void ff(int[] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = i; j > 0; j--) {
                if (data[j] < data[j - 1]) {
                    int num = data[j];
                    data[j] = data[j - 1];
                    data[j - 1] = num;
                }
            }
        }
        output(changeArraysToList(data));
    }
 
    public static void ff1(int[] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length - i - 1; j++) {
                if (data[j] > data[j + 1]) {
                    int num = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = num;
                }
            }
        }
        output(changeArraysToList(data));
    }
```

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

## [公众号地图](https://mp.weixin.qq.com/s/36RbP20beZ8oWJ9nLAxG3g) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>