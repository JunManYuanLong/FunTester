# groovy在使用java接口框架时，不兼容问题汇总
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
本人在使用java做了一个基础的接口测试框架，然后用groovy做用例编写的过程中，发现了一些java框架和groovy不兼容的情况。在此汇总一下：

* 文件操作

遇到的问题：java框架中有自动保存日志的功能，会创建一个log文件，然后把内容写到文件中。在使用groovy测试时，保存日志一直在创建文件报错，最终放弃了由groovy创建文件的方案。
具体报错的方法：

```
/**
	 * 写入文本信息，会自动新建文件
	 *
	 * @param file file对象，必须是存在的路径
	 * @param text 写入的内容，如果file存在，续写
	 */
	public static void writeText(File file, String text) {
		if (!file.exists())
			try {
				file.createNewFile();//报错
			} catch (IOException e) {
				e.printStackTrace();
			}
		try {
			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter bw1 = new BufferedWriter(fileWriter);
			bw1.write(text);// 将内容写到文件中
			bw1.flush();// 强制输出缓冲区内容
			bw1.close();// 关闭流
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
```
尝试过mkdir()方法也会报错。

* 强制类型转换

遇到问题：在封装sleep方法时，本意想用整数和小数来控制时长，groovy一直提示无法强制转化，最后只能放弃这个方法，该做int类型传参，具体方法如下：

```
/**
	 * 休眠
	 *
	 * @param second 秒，可以是小数
	 */
	public static void sleep(Number second) {
		try {
			Thread.sleep((long) ((double) second * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>