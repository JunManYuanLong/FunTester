# 如何获取手机性能测试数据FPS
本人在做APP性能测试的过程中，为了测试APP在各个场景下的流畅度，需要收集手在各个运行场景下的fps数据，经常查资料，使用的是adb shell命令：

`adb shell dumpsys gfxinfo 包名`
分享代码，供大家参考。
测试方法：

```
        Fps fps = new Fps();
			fps.start();
			//do something
			fps.stopFps();
			fps.join();
```
多线程类的代码：

```
package monkeytest;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import source.AppLocalMySql;
import source.Common;
 
public class Fps extends Thread {
	private boolean KEY = true;
	private static String testName = "normal";
	private int mark = Common.mark;
 
	@Override
	public void run() {
		while (KEY) {
			execCmdAdb("adb shell dumpsys gfxinfo com.happyjuzi.apps.juzi");
			Common.getInstance().sleep(5000);
		}
	}
 
	public int getMatcher(String text, Pattern pattern) {
		Pattern newattern = Pattern.compile(pattern.toString().trim());
		Matcher matcher = newattern.matcher(text);
		List<String> numbers = new ArrayList<>();
		while (matcher.find()) {
			String number = matcher.group(0);
			numbers.add(number);
		}
		double result = 0;
		for (int i = 0; i < numbers.size(); i++) {
			double num = Common.getInstance().changeStringToDouble(numbers.get(i));
			result += num;
		}
		int total = (int) result + new Random().nextInt(2);
		return total;
	}
 
	/**
	 * 结束线程
	 */
	public void stopFps() {
		this.KEY = false;
	}
 
	/**
	 * 执行cmd命令
	 * 
	 * @param cmd
	 *            命令
	 */
	public void execCmdAdb(String cmd) {
		Pattern pattern = Pattern.compile("	([0-9]{1,2}+\\.[0-9]{2})");
		output("正在执行：" + cmd);
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
				if (line.startsWith("	")) {
					Matcher matcher = pattern.matcher(line);
					if (matcher.find()) {
						AppLocalMySql.getInstance().saveFps(testName, mark);
						getMatcher(line, pattern);// 输出
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
				System.out.println(eline);// 输出
				Common.getInstance().saveToFile(eline, "runlog.log");// 保存，false表示不覆盖
			}
			errorReader.close();// 此处有依赖关系，先关闭errorReader
			errorInput.close();
		} catch (IOException e) {
			output("执行" + cmd + "失败！");
			e.printStackTrace();
		}
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

### 往期文章精选

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

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>