# 一个记录手机log的多线程类

本人在做自动化测试的时候，需要单独一个线程来记录手机的log，经过研究确定了终止logcat的方案之后，终于完成了这个类的1.0版本，分享出来供大家参考。


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
这里需要注意一点logcat类的使用方法，不能一直让这个线程运行，不然log文件会很大，每次读写log文件的时候会越来越消耗资源。可以自己在logcat类里面做一个循环也可以在使用logcat线程的时候做循环，我用的后者，因为我的其他线程也是写在测试脚本的循环当中。

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>