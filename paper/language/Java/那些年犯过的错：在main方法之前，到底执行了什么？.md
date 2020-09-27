# 那些年犯过的错：在main方法之前，到底执行了什么？

本人在做接口测试的时候，需要用一个公共类来把所有的执行的代码，然后这个公共类有hsot和hosttype等属性来区分各个测试环境，然后在去不同的地方取用例和请求接口。在给这些属性复制的时候，我是通过不同测试环境新建不同的配置文件，然后执行每个环境的时候让只加载需要测试的环境的配置文件来实现管理测试环境的。中间遇到了一些坑，主要就是对java代码执行循序，特别是在main方法之前的代码执行顺序了解不深入导致的，中间有多个继承关系也有点扰乱了思路。下面分享一下自己这个错误的复现步骤。

首先放一下一个单独的类的代码执行顺序，下面是代码：

```
package practice;
 
public class Cbc {
	public static Cbc cbc = new Cbc();
 
	public static void main(String[] args) {
		System.out.println("进入程序入口了！");
	}
 
	public Cbc() {
		System.out.println("我是Cbc构造方法！");
	}
 
	static {
		System.out.println("我是Cbc静态代码块！");
	}
}
```
下面是执行结果：

> 我是Cbc构造方法！
> 我是Cbc静态代码块！
> 进入程序入口了！

这个就比较简单了，先执行静态变量赋值，然后执行静态代码块，然后再去执行main方法。
下面是多个继承的情况，Cba继承于Bbc，Bbc继承于Abc，三个类的代码都是相似的，分享如下：
```
package practice;
 
public class Cbc extends Bbc{
	public static Cbc cbc = new Cbc();
 
	public static void main(String[] args) {
		System.out.println("进入程序入口了！");
	}
 
	public Cbc() {
		System.out.println("我是Cbc构造方法！");
	}
 
	static {
		System.out.println("我是Cbc静态代码块！");
	}
}
 
class Bbc extends Abc {
	public static Bbc bbc = new Bbc();
 
	public Bbc() {
		System.out.println("我是Bbc构造方法！");
	}
 
	static {
		System.out.println("我是Bbc静态代码块！");
	}
}
 
class Abc {
	public static Abc abc = new Abc();
 
	public Abc() {
		System.out.println("我是Abc构造方法！");
	}
 
	static {
		System.out.println("我是Abc静态代码块！");
	}
}
```
下面是执行结果：

>我是Abc构造方法！
>我是Abc静态代码块！
>我是Abc构造方法！
>我是Bbc构造方法！
>我是Bbc静态代码块！
>我是Abc构造方法！
>我是Bbc构造方法！
>我是Cbc构造方法！
>我是Cbc静态代码块！
>进入程序入口了！

可以看出来，先执行Abc，再去执行Bbc，再去执行Cbc。
知道了这个逻辑，就可以做一些事情，比如Abc有一个int对象num的值是1，是公用默认的，但是我想再某一个特殊（Cbc）情况下使用num值是2，那么我可以Bbc里面对num重新赋值，使得我在使用Cbc这个情况下时候，num值是2，而在其他情况时，num的值依然是1。

下面是测试代码：


```
public class Cbc extends Bbc {
	public static void main(String[] args) {
		System.out.println("进入程序入口了！");
		System.out.println("num的值：" + num);
	}
}
 
class Bbc extends Abc {
	static {
		num = 2;
	}
}
 
class Abc {
	public static int num = 1;
 
}
```

下面是执行结果：

>进入程序入口了！
>num的值：2

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



<br></br>
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>

