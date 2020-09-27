# 利用UiAutomator写一个首页刷新的稳定性测试脚本
本人在做Android APP稳定性测试的过程中，需要测试在不断刷新首页内容的场景下的稳定运行和性能数据的收集。最终根据UiAutomator+多线程解决了这个问题。思路如下：先用UiAutomator编写好运行脚本，然后在使用快速调试的时候把调试命令输出出来，然后在测试脚本中运行这个调试命令即可，当然还需要多线程来辅助记录log和性能数据。

多线程类代码如下：


```
package monkeytest;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import source.Common;
import source.PerformanceThread;
 
public class HomeRefresh {
	public static String ADB_PATH = Common.ADB_PATH;;
	public static String package_name = Common.HEAD_PAGEKAGE;
 
	public static void main(String[] args) {
//		String timess = args[0];
//		int times = Common.getInstance().changeStringToInt(timess);
		for (int i = 0; i < 3; i++) {
			Date start = Common.getInstance().getDate();
			Logcat logcat = new Logcat();// 新建记录log线程
			PerformanceThread performanceThread = new PerformanceThread("homerefresh", package_name);// 性能监控
			performanceThread.start();
			logcat.start();
			String command = "adb shell uiautomator runtest demo.jar --nohup -c happyjuzi.AppCase#testHomeRefresh";
			execCmdAdb(command, "homerefresh" + getNow() + ".log");
			logcat.stopLoacat();
			performanceThread.stopRecord();
			Date end = Common.getInstance().getDate();
			Common.getInstance().outputTimeDiffer(start, end);
		}
	}
 
	/**
	 * 执行adb命令
	 * 
	 * @param cmd
	 *            命令内容
	 * @param fileName
	 *            输入文件路径
	 */
	public static void execCmdAdb(String cmd, String fileName) {
		System.out.println("执行命令：" + cmd);
		String OSname = System.getProperty("os.name");
		Common.getInstance().saveToFile(Common.getInstance().getNow() + "开始！" + Common.LINE, fileName);// 保存
		try {
			Process p = null;
			if (OSname.contains("Mac")) {
				p = Runtime.getRuntime().exec(ADB_PATH + cmd);
			} else {
				p = Runtime.getRuntime().exec("cmd /c " + cmd);
			}
			// 正确输出流
			InputStream input = p.getInputStream();// 创建并实例化输入字节流
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));// 先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String line = "";
			while ((line = reader.readLine()) != null) {// 循环读取
				if (line.startsWith("//   ")) {
					continue;
				}
				if (line.startsWith("//")) {
					Common.getInstance().saveToFile(line, fileName);// 保存
				}
				if (line.contains("Exception")) {
					Common.getInstance().saveToFile("error-homerefresh" + getNow(), "error-homerefresh"+getNow()+".log");
				}
			}
			reader.close();// 此处reader依赖于input，应先关闭
			input.close();
			// 错误输出流
			InputStream errorInput = p.getErrorStream();// 创建并实例化输入字节流
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorInput));// 先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String eline = "";
			while ((eline = errorReader.readLine()) != null) {// 循环读取
				Common.getInstance().saveToFile(eline, fileName);// 保存
 
			}
			errorReader.close();// 此处有依赖关系，先关闭errorReader
			errorInput.close();
		} catch (IOException e) {
			Common.getInstance().output("执行" + cmd + "失败！");
			e.printStackTrace();
		}
		Common.getInstance().saveToFile(Common.LINE + Common.getInstance().getNow() + "结束！", fileName);// 保存
	}
 
	/**
	 * 获取当前时间
	 * 
	 * @return 返回当前时间，只有日期和小时和分数没有年份和秒数
	 */
	private static String getNow() {
		Date time = new Date();
		SimpleDateFormat now = new SimpleDateFormat("MM-dd-HH-mm");
		String c = now.format(time);
		return c;
	}
 
	public void output(String text) {
		System.out.println(text);
	}
 
	public void output(Object... object) {
		if (object.length == 1) {
			output(object[0].toString());
			return;
		}
		for (int i = 0; i < object.length; i++) {
			System.out.println("第" + (i + 1) + "个：" + object[i]);
		}
	}
}
```
运行代码如下，因为比较简单，就只写了方法部分的代码。

```
	public void testHeadHomeRefresh() {
		for (int i = 0; i < 20; i++) {
			startHead();
			waitForUiObjectByResourceId("com.ss.android.article.news:id/b_y");
			sleep(1000);
			for (int k = 0; k < 15; k++) {
				swipeDown();
				sleep(1500);
			}
			stopHead();
		}
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
8. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
9. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
10. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
11. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
12. 

## [公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>