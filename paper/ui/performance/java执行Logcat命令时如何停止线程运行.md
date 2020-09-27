# java执行Logcat命令时如何停止线程运行
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

本人在使用UiAutomator的时候，想多写一个自动收集手机log的方法，使用runtime类执行了adb logcat的方法，但是一直找不到好的方法结束这个线程，网上说有kill pid的，但是这个操作起来略微麻烦了。自己也想了一个destroy线程的方法，一直不好用。提示错误信息如下：

```
Exception in thread "main" java.lang.NoSuchMethodError
	at java.lang.Thread.destroy(Thread.java:990)
	at monkeytest.Monkey.main(Monkey.java:15)
```
原因是因为：destroy()方法会摧毁线程，但是runtime使用中新建了一个子线程，所以才会报错。
后来自己想了一个办法，在去读input流的时候，多加一个参数来写一个stop的方法。下面是logcat的类代码，分享出来，供大家参考。其中一个destroy的方法得直接去结束process线程，如果子线程在运行的时候，直接调用destroy方法，就会报上面的那个错误。这个是自己总结的，如有错误还请指正。


```
package monkeytest;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import source.Common;
 
public class Logcat extends Thread {
	private static boolean LogKey = false;
 
	@Override
	public void run() {
		execCmdAdb("adb logcat -c");
		execCmdAdb("adb logcat", "logcat.log", "happyjuzi", true);
 
	}
		
	/**
	 * 执行adb命令
	 * 
	 * @param cmd
	 *            命令内容
	 * @param fileName
	 *            输入文件路径
	 * @param filter
	 *            过滤词汇
	 * @param key
	 *            是否包含过滤词汇
	 */
	private void execCmdAdb(String cmd, String fileName, String filter, boolean key) {
		System.out.println(cmd);
		String OSname = System.getProperty("os.name");
		try {
			Process p = null;
			if (OSname.contains("Mac")) {
				p = Runtime.getRuntime().exec(Common.ADB_PATH + cmd);
			} else {
				p = Runtime.getRuntime().exec("cmd /c " + cmd);
			}
			// 正确输出流
			InputStream input = p.getInputStream();// 创建并实例化输入字节流
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));// 先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String line = "";
			while ((line = reader.readLine()) != null) {// 循环读取
				if (LogKey) {
					p.destroy();//结束线程
					reader.close();// 此处reader依赖于input，应先关闭
					input.close();
					return;
				}
				// System.out.println(line);// 输出
				if (key) {
					if (line.contains(filter)) {
						Common.getInstance().saveToFile(line, fileName, false);// 保存
					}
				} else {
					if (!line.contains(filter)) {
						Common.getInstance().saveToFile(line, fileName, false);// 保存
					}
				}
			}
			reader.close();// 此处reader依赖于input，应先关闭
			input.close();
			// 错误输出流
			InputStream errorInput = p.getErrorStream();// 创建并实例化输入字节流
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorInput));// 先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String eline = "";
			while ((eline = errorReader.readLine()) != null) {// 循环读取
				if (LogKey) {
					p.destroy();
					errorReader.close();// 此处有依赖关系，先关闭errorReader
					errorInput.close();
					return;
				}
				if (key) {
					if (eline.contains(filter)) {
						Common.getInstance().saveToFile(line, fileName, false);// 保存
					}
				} else {
					if (!eline.contains(filter)) {
						Common.getInstance().saveToFile(line, fileName, false);// 保存
					}
				}
			}
			errorReader.close();// 此处有依赖关系，先关闭errorReader
			errorInput.close();
		} catch (IOException e) {
			Common.getInstance().output("执行" + cmd + "失败！");
			e.printStackTrace();
		}
	}
 
	private void execCmdAdb(String cmd) {
		System.out.println(cmd);
		String OSname = System.getProperty("os.name");
		try {
			if (OSname.contains("Mac")) {
				Runtime.getRuntime().exec(Common.ADB_PATH + cmd);
			} else {
				Runtime.getRuntime().exec("cmd /c " + cmd);
			}
		} catch (IOException e) {
			Common.getInstance().output("执行" + cmd + "失败！");
			e.printStackTrace();
		}
	}
 
	public void stopLoacat() {
		Logcat.LogKey = true;
	}
}
```
多线程实现：


```
package monkeytest;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
 
import source.Common;
 
public class Logcat extends Thread {
	private static boolean LogKey = false;
 
	@Override
	public void run() {
		execCmdAdb("adb logcat -c");
		execCmdAdb("adb logcat", "logcat" + getNow() + ".log");
	}
 
	/**
	 * 执行adb命令
	 * 
	 * @param cmd
	 *            命令内容
	 * @param fileName
	 *            输入文件路径
	 * @param filter
	 *            过滤词汇
	 * @param key
	 *            是否包含过滤词汇
	 */
	private void execCmdAdb(String cmd, String fileName) {
		System.out.println(cmd);
		String OSname = System.getProperty("os.name");
		try {
			Process p = null;
			if (OSname.contains("Mac")) {
				p = Runtime.getRuntime().exec(Common.ADB_PATH + cmd);
			} else {
				p = Runtime.getRuntime().exec("cmd /c " + cmd);
			}
			// 正确输出流
			InputStream input = p.getInputStream();// 创建并实例化输入字节流
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));// 先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String line = "";
			while ((line = reader.readLine()) != null) {// 循环读取
				if (LogKey) {
					p.destroy();// 结束线程
					reader.close();// 此处reader依赖于input，应先关闭
					input.close();
					return;
				}
				// System.out.println(line);// 输出
				if (line.contains("happyjuzi")) {
					Common.getInstance().saveToFile(line, fileName, false);// 保存
				}
			}
			reader.close();// 此处reader依赖于input，应先关闭
			input.close();
			// 错误输出流
			InputStream errorInput = p.getErrorStream();// 创建并实例化输入字节流
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorInput));// 先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String eline = "";
			while ((eline = errorReader.readLine()) != null) {// 循环读取
				if (LogKey) {
					p.destroy();
					errorReader.close();// 此处有依赖关系，先关闭errorReader
					errorInput.close();
					return;
				}
				if (eline.contains("happyjuzi")) {
					Common.getInstance().saveToFile(line, fileName, false);// 保存
				}
			}
			errorReader.close();// 此处有依赖关系，先关闭errorReader
			errorInput.close();
		} catch (IOException e) {
			Common.getInstance().output("执行" + cmd + "失败！");
			e.printStackTrace();
		}
	}
 
	private void execCmdAdb(String cmd) {
		System.out.println(cmd);
		String OSname = System.getProperty("os.name");
		try {
			if (OSname.contains("Mac")) {
				Runtime.getRuntime().exec(Common.ADB_PATH + cmd);
			} else {
				Runtime.getRuntime().exec("cmd /c " + cmd);
			}
		} catch (IOException e) {
			Common.getInstance().output("执行" + cmd + "失败！");
			e.printStackTrace();
		}
	}
 
	/**
	 * 停止logcat线程
	 */
	public void stopLoacat() {
		Logcat.LogKey = true;
	}
 
	/**
	 * 获取当前时间
	 * 
	 * @return 返回当前时间，只有日期和小时和分数没有年份和秒数
	 */
	private String getNow() {
		Date time = new Date();
		SimpleDateFormat now = new SimpleDateFormat("MMdd-HHmm");
		String c = now.format(time);
		return c;
	}
}

```
* 这里需要注意一点logcat类的使用方法，不能一直让这个线程运行，不然log文件会很大，每次读写log文件的时候会越来越消耗资源。可以自己在logcat类里面做一个循环也可以在使用logcat线程的时候做循环，我用的后者，因为我的其他线程也是写在测试脚本的循环当中。

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

## [公众号地图](https://mp.weixin.qq.com/s/36RbP20beZ8oWJ9nLAxG3g) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)




> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)


<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---


<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>