# UiAutomator提示不支持 diamond 运算符错误解决办法
本人在使用UiAutomator写测试脚本的过程中遇到一个问题，提示错误信息如下：


```
    [javac] /Users/dahaohaozai/eclipse-workspace/com.apptest/src/source/Common.java:253: 错误: -source 1.5 中不支持 diamond 运算符
    [javac] 		List<String> result = new ArrayList<>();
    [javac] 		                                    ^
    [javac]   (请使用 -source 7 或更高版本以启用 diamond 运算符)
```
网上查了一些文章，好像这个写法是1.7以后的新写法，IDE默认的1.5不支持这个写法，所以才报错。按照方法检查了自己的java compiler的版本还有项目的java compiler的版本依然没有解决问题。所以还是老老实实改代码吧。

```
			List<String> list = new ArrayList<String>();
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>