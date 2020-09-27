# java利用for循环输出正三角新解

本人在重新学习Java的过程中，遇到一个作业，就是用循环输出正三角，在完成作业之余，查看了很多网上的答案，用了好几次for循环，基本思路就是先拼接前面的空格，在去拼接后面的“*”符号。感觉有点捉急，自己想了一想新的办法来更少的循环来得到答案。我利用一个if-else判断，以三角形顶点作为临界点，然后根据输出行数不同，拼接不同数量的空格和“*”，分享代码，供大家参考。（一共两个例子，一个是正向的三角形，一个是等边三角形）



```
for (int i = 0; i < 10; i++) {
			for (int k = 0; k < 9 + i; k++) {
				if (k < 10 - i) {
					System.out.print("  ");
				} else {
					System.out.print("* ");
				}
			}
			System.out.println("\n\t");
		}
```
输出结果如下：
![](/blog/pic/20180105134736372.png)

下面是输出等边三角形的代码：


```
for (int i = 0; i < 10; i++) {
			int n = 1;
			for (int k = 0; k < 9 + i; k++) {
				if (k < 10 - i) {
					System.out.print("  ");
				} else if (n % 2 == 1) {
					n++;
					System.out.print(" *  ");
				} else {
					n++;
				}
			}
			System.out.println("\n\t");
		}
```
下面是执行结果图：

![](/blog/pic/20180105203504706.png)



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

## [公众号地图](https://mp.weixin.qq.com/s/36RbP20beZ8oWJ9nLAxG3g) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>