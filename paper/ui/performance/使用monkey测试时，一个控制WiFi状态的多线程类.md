# 使用monkey测试时，一个控制WiFi状态的多线程类


添加WiFi测试APP下载地址:

[传送门](https://gitee.com/fanapi/tester/tree/master/long)

本人在使用monkey进行手机APP性能测试的时候，经常会遇到WiFi被关闭，飞行模式被打开的问题，虽然monkey也要进行无网测试，但在无人值守使用monkey测试的时候，还是需要网络状态稳定一些，经常不断尝试，终于找到了解决办法。

思路如下，写了一个APP，专门用来切换网络状态，只是用来切换网络状态而已。然后需求是每分钟检查一次WiFi状态是否跟预期一致，每十分钟切换一次预期状态，已达到交叉测试的效果。使用adb shell ifconfig wlan0拿到当前的网络状态，通过执行adb shell am命令来切换WiFi状态。分享代码如下：


```
package monkeytest;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import source.Common;
 
public class WifiSwitch extends Thread {
	public static boolean WIFIKEY = true;// 线程开关，默认开
	public static boolean WIFISTATUS = true;// WiFi状态开关，默认开
 
	@Override
	public void run() {
		while (WIFIKEY) {
			for (int i = 0; i < 10; i++) {
				if (WIFIKEY) {
					break;
				}
				Common.getInstance().sleep(60 * 1000);
				keepWifiONorOFF(WIFISTATUS);
			}
			WIFISTATUS = !WIFISTATUS;// 反转WiFi状态
		}
	}
 
	/**
	 * 结束线程方法
	 */
	public void stopWifiSwitch() {
		WIFIKEY = false;
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
 
}
```




> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>