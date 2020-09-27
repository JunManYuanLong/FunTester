# 如何给所有连接设备更新apk版本

本人在测试Android APP的过程中，遇到一个需求：更新所有连接设备的apk版本。因为之前做的脚本都是针对单个连接设备的，所以adb -s参数一直没用，都是动态获取的，所以这次还特意写了一个新的方法。分享代码供大家参考。这里并没有使用多线程来完成这个事情，有兴趣的朋友可以出自己尝试一下多线程搞定这个问题。

下面是我的测试代码：


```
package practise;
 
import java.util.List;
import source.Common;
 
public class PractiseApp extends Common {
	public static void main(String[] args) {
		String versioin = "3.0";
		List<String> devices = Common.getInstance().getAllDevices();
		for (int i = 0; i < devices.size(); i++) {
			Common.getInstance().execCmdAdb("adb -s " + devices.get(i) + " uninstall " + PAGEKAGE);
			Common.getInstance().execCmdAdb("adb -s " + devices.get(i)
					+ " install -r /Users/dahaohaozai/Desktop/apk/version/" + versioin + ".apk");
		}
	}
}
```
下面是getalldevices()方法：

```
/**
	 * 获取所有当前设备id
	 */
	public List<String> getAllDevices() {
		List<String> devices = new ArrayList<>();
		String cmd = "adb devices";
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
				if (line.contains("device") && !line.contains("devices")) {
					devices.add(line.replaceAll("device", "").trim());
				}
			}
			reader.close();// 此处reader依赖于input，应先关闭
			input.close();
		} catch (IOException e) {
			output("执行" + cmd + "失败！", e);
		}
		return devices;
	}
```
下面是execmdadb()的方法：

```
/**
	 * 执行cmd命令
	 * 
	 * @param cmd
	 *            命令
	 */
	public void execCmdAdb(String cmd) {
		output("正在执行：" + cmd);
		String OSname = System.getProperty("os.name");
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
				System.out.println(line);// 输出
				saveToFile(line, "runlog.log");// 保存，false表示不覆盖
			}
			reader.close();// 此处reader依赖于input，应先关闭
			input.close();
			// 错误输出流
			InputStream errorInput = p.getErrorStream();// 创建并实例化输入字节流
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorInput));// 先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String eline = "";
			while ((eline = errorReader.readLine()) != null) {// 循环读取
				System.out.println(eline);// 输出
				saveToFile(eline, "runlog.log");// 保存，false表示不覆盖
			}
			errorReader.close();// 此处有依赖关系，先关闭errorReader
			errorInput.close();
		} catch (IOException e) {
			output("执行" + cmd + "失败！");
			e.printStackTrace();
		}
	}
```

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>