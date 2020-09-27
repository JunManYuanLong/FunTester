# 统计APP启动时间和进入首页时间的多线程类

本人在做APP性能测试的时候，需要统计一下APP启动时间和进入首页的时间，之前采取的方案是图片做对比，后来采取了录屏，效果都不是很理想，在参考了网上关于手机log分析手机启动activity的教程，自己写了一个多线程类通过不停地启动关闭APP，同时分析log中关于activity的lauch时间得到需要的数据。测试了一下效果很不错，分享代码，供大家参考。

```
package monkeytest;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import source.AppLocalMySql;
import source.Common;
 
public class LauchTime extends Thread {
	public static String ADB_PATH = Common.ADB_PATH;;
	public static String package_name = Common.PAGEKAGE;
	public static String test_name = "normal";
	public static Pattern pattern = Pattern.compile("\\+.+?ms");
	public static boolean LauchKey = false;
 
	public static void main(String[] args) {
		// String timess = args[0];
		// int times = Common.getInstance().changeStringToInt(timess);
		execCmdAdb("adb logcat -c");
		Common.getInstance().sleep(2000);
		LauchTime lauchTime = new LauchTime();
		lauchTime.start();// 启动logcat统计线程
		StartApp startApp = new StartApp();// 获取startAPP实例
		for (int i = 0; i < 5; i++) {
			startApp.startJuziApp();// 启动APP
			Common.getInstance().sleep(9000);
			startApp.stopJuziApp();// 关闭APP
			Common.getInstance().sleep(1000);
		}
		lauchTime.stopLauch();// 结束统计
	}
 
	@Override
	public void run() {
		execCmdAdb("adb logcat");
	}
 
	/**
	 * 停止logcat线程
	 */
	public void stopLauch() {
		LauchTime.LauchKey = true;
	}
 
	/**
	 * 执行adb命令
	 * 
	 * @param cmd
	 *            命令内容
	 * @param fileName
	 *            输入文件路径
	 */
	private static void execCmdAdb(String cmd) {
		System.out.println("正在执行：" + cmd);
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
				if (LauchKey) {
					p.destroy();// 结束线程
					reader.close();// 此处reader依赖于input，应先关闭
					input.close();
					return;
				}
				//截取到log信息，分别统计两个activity的时间
				if (line.contains("Displayed")) {
					if (line.contains("SplashActivity")) {
						double time = getLauchTime(line);
						AppLocalMySql.getInstance().saveLauchTime(test_name, package_name, "SplashActivity", time);
					}
					if (line.contains("HomeActivity")) {
						double time = getLauchTime(line);
						AppLocalMySql.getInstance().saveLauchTime(test_name, package_name, "HomeActivity", time);
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
				output(eline);// 输出
			}
			errorReader.close();// 此处有依赖关系，先关闭errorReader
			errorInput.close();
		} catch (
 
		IOException e) {
			Common.getInstance().output("执行" + cmd + "失败！");
			e.printStackTrace();
		}
	}
 
	/**
	 * 获取启动时间
	 * 
	 * @param line
	 *            截取到的log信息
	 * @return 返回double时间，单位s，默认0.00
	 */
	public static double getLauchTime(String line) {
		Matcher matcher = pattern.matcher(line);
		if (matcher.find()) {
			line = matcher.group(0);
			line = line.substring(1, line.length() - 2);
			line = line.replace("s", ".");
			if (!line.contains(".")) {
				line = "0." + line;
			}
			double time = Common.getInstance().changeStringToDouble(line);
			return time;
		}
		return 0.00;
	}
 
	public static void output(String text) {
		System.out.println(text);
	}
 
	public static void output(Object... object) {
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
中间startAPP类用到的方法如下：

```
/**
	 * 启动橘子APP
	 */
	public void startJuziApp() {
		if (Monkey.package_name.contains("happyjuzi")) {
			execCmdAdb("adb shell am start -n com.happyjuzi.apps.juzi/.SplashActivity");
		} else if (Monkey.package_name.contains("article.news")) {
			execCmdAdb("adb shell am start -n com.ss.android.article.news/.activity.SplashBadgeActivity");
		}
	}
 
	public void stopJuziApp() {
		if (Monkey.package_name.contains("happyjuzi")) {
			execCmdAdb("adb shell am force-stop com.happyjuzi.apps.juzi");
		} else if (Monkey.package_name.contains("article.news")) {
			execCmdAdb("adb shell force-stop com.ss.android.article.news");
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
8. [测试之JVM命令脑图](https://mp.weixin.qq.com/s/qprqyv0j3SCvGw1HMjbaMQ)
9. [将json数据格式化输出到控制台](https://mp.weixin.qq.com/s/2IPwvh-33Ov2jBh0_L8shA)
10. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
11. [“双花”BUG的测试分享](https://mp.weixin.qq.com/s/0dsBsssNfg-seJ_tu9zFaQ)

## [公众号地图](https://mp.weixin.qq.com/s/36RbP20beZ8oWJ9nLAxG3g) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)



> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>