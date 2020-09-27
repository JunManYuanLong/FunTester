# eclipse如何导出可用命令行传参执行的jar包
本人在做接口测试框架开发的过程中，需要给其他组员写一些小工具，研究了一下如何用eclipse导出jar包的问题，发现网上一些教程写得很有问题，把能执行的jar包和普通的jar包弄混淆了，所以自己特意写一个如何导出可执行的jar包的教程。

本教程用Mac，eclipse，iterm2。以一个计算用户token的方法。

下面是我的代码，写个简单的例子。功能是输入uid返回用户当前的token。


```
package source;
 
public class Practise extends ApiLibrary {
	public static void main(String[] args) {
		Practise practise = new Practise();
		practise.testDemo(args[0]);
	}
 
	public void testDemo(String uid) {
		String token = getToken(uid);
		output(token);
	}
}
```
下面是第一步：
选择项目右击，选择export。如图：
![](/blog/pic/20171103131351421.jpeg)
第二步：

选择runable jar file，点击next。如图：

![](/blog/pic/20171103131531246.png)
第三步：

选择运行的类，选择导出路径，点击finish。如图：
![](/blog/pic/20171103131820554.png)
打开iterm2，切换到jar包所在文件夹。执行命令 java -jar (文件名)，如图：

![](/blog/pic/20171103132033329.png)

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>