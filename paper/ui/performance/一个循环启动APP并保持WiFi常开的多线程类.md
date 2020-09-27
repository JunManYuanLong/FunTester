# 一个循环启动APP并保持WiFi常开的多线程类

添加WiFi测试APP下载地址

[传送门](https://gitee.com/fanapi/tester/tree/master/long)

本人在使用monkey进行测试的时候，发现monkey参数里面--pct-appswitch参数并不好用，随机性比较大，所以想自己来控制启动APP的次数和间隔。之前通过批处理命令bat和shell脚本都实现过，但是现在要做成一个jar的工具包，只能写在一个多线程类里，随便把校验WiFi开关状态的方法也写在了一起。分享代码，供大家参考。


```
package monkeytest;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import source.Common;
 
public class StartApp extends Thread {
	public boolean MKEY = true;//线程开关
	public static boolean WIFISTATUS = true;// WiFi状态开关，默认开
 
	@Override
	public void run() {
		while (MKEY) {
			Common.getInstance().sleep(60 * 1000);
			keepWifiONorOFF(WIFISTATUS);
			startJuziApp();
		}
	}
 
	/**
	 * 启动橘子APP
	 */
	public void startJuziApp() {
		if (Monkey.package_name.contains("happyjuzi")) {
			execCmdAdb("adb shell am start -n com.happyjuzi.apps.juzi/.SplashActivity");
		} else if (Monkey.package_name.contains("article.news")) {
			execCmdAdb("adb shell am start -n com.ss.android.article.news/.activity.SplashBadgeActivity");// 今日头条
		}
	}
 
	/**
	 * 保持WiFi状态的方法
	 * 
	 * @param status
	 *            当前WiFi的期望状态
	 */
	public void keepWifiONorOFF(boolean status) {
		if (status & wifiIsOn()) {// 判断WiFi状态是否跟预期状态一致
			closeOrOpenWifi();
		}
	}
 
	/**
	 * wifi是否打开
	 * 
	 * @return 开打true，没打开false
	 */
	private boolean wifiIsOn() {
		String cmd = "adb shell ifconfig wlan0";
		System.out.println("执行：" + cmd);
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
				if (line.contains("RUNNING")) {
					return true;
				}
			}
			reader.close();// 此处reader依赖于input，应先关闭
			input.close();
		} catch (IOException e) {
			Common.getInstance().output("执行" + cmd + "失败！");
			e.printStackTrace();
		}
		return false;
	}
 
	/**
	 * 开关WiFi
	 */
	public void closeOrOpenWifi() {
		try {
			Runtime.getRuntime().exec(Common.ADB_PATH + "adb shell am start -n run.wifibutton/.WifiButtonActivity")
					.waitFor();
		} catch (InterruptedException | IOException e) {
			Common.getInstance().output("WIFI开关异常！", e);
		}
	}
 
	/**
	 * 结束线程方法
	 */
	public void stopThread() {
		this.MKEY = false;
	}
 
	/**
	 * 执行cmd命令
	 * 
	 * @param cmd
	 *            命令
	 */
	public static void execCmdAdb(String cmd) {
		System.out.println("正在执行：" + cmd);
		String OSname = System.getProperty("os.name");
		try {
			if (OSname.contains("Mac")) {
				Runtime.getRuntime().exec(Common.ADB_PATH + cmd);
			} else {
				Runtime.getRuntime().exec("cmd /c " + cmd);
			}
		} catch (IOException e) {
			System.out.println("执行" + cmd + "失败！");
			e.printStackTrace();
		}
	}
}
```

### 技术类文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
8. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
9. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
10. [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
11. [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)
12. [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)

### 非技术文章精选
1. [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
2. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
3. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
4. [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
5. [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
6. [测试之《代码不朽》脑图](https://mp.weixin.qq.com/s/2aGLK3knUiiSoex-kmi0GA)

# [点击查看公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>