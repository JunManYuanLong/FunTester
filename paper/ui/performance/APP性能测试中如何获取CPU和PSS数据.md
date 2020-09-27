# APP性能测试中如何获取CPU和PSS数据
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

本人在最近手机APP性能数据的过程中，又重新看了一些Android的内存相关知识，对之前写过的一篇APP性能的线程类的方法做了优化，总得来说，就是增加了PSS数据和增加了数据获取之后的数据整理工作。

获取PSS的方法原理是通过adb shell dumpsys命令获取到的，之前放弃了这个方法，因为内存数据太细分了，后来发现细分的更准确。这里没有统计Native Heap和Dalvik Heap，感觉统计数据的话并没有多大的必要。对这块也不是非常了解如果有不对的地方，还请指正。

方法如下：


```
	/**
	 * 获取应用信息 利用Android系统dumpsys命令获取
	 * 命令能统计到java虚拟的堆内存和栈内存的使用情况
	 * 
	 * @return 返回内存占用
	 */
	public int getMemResult() {
		String cmd1 = Common.ADB_PATH + "adb shell dumpsys meminfo " + package_name;
		int mem_result = 0;
		try {
			Process p = Runtime.getRuntime().exec(cmd1);// 通过runtime类执行cmd命令
			// 正确输出流
			InputStream input = p.getInputStream();// 创建并实例化输入字节流
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));// 先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String line = "";
			while ((line = reader.readLine()) != null) {// 循环读取
				if (line.startsWith("        TOTAL")) {
					mem_result = getMemInfo(line);
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
			}
			errorReader.close();// 此处有依赖关系，先关闭errorReader
			errorInput.close();
		} catch (IOException e) {
			output("执行" + cmd1 + "失败！", e);
		}
		return mem_result;
	}
```
下面是增加的统计方法，主要是在每次新建进程的时候都会记录一个mark，统计方法写在结束线程的方法里：

```
public void stopRecord() {
		AppLocalMySql.getInstance().ClearUpPerformaceData(mark);//整理数据
		PerformanceThread.key = false;//结束线程
	}
```

```
/**
	 * 整理一次性能数据收集
	 * 
	 * @param mark
	 *            统计mark
	 */
	public void ClearUpPerformaceData(int mark) {
		getConnection();
		int cpu = 0, pss = 0, rss = 0, vss = 0, total = 0;
		String device = null, packages = null, test_name = null;
		try {
			if (!connection.isClosed()) {
				outputSuccess();
				Statement statement = connection.createStatement();
				String sql = "SELECT AVG(cpu),AVG(pss),AVG(rss),AVG(vss),COUNT(*),device,package,test_name FROM app_result WHERE mark = "
						+ mark;
				ResultSet resultSet = statement.executeQuery(sql);
				while (resultSet.next()) {
					cpu = resultSet.getInt(1);
					pss = resultSet.getInt(2);
					rss = resultSet.getInt(3);
					vss = resultSet.getInt(4);
					total = resultSet.getInt(5);
					device = resultSet.getString("device");
					packages = resultSet.getString("package");
					test_name = resultSet.getString("test_name");
				}
				String sql2 = "INSERT INTO app_report (mark,test_name,package,device,cpu,pss,rss,vss,total) VALUES ("
						+ mark + ",'" + test_name + "','" + packages + "','" + device + "'," + cpu + "," + pss + ","
						+ rss + "," + vss + "," + total + ")";
				statement.executeUpdate(sql2);
				statement.close();
				connection.close();
			}
		} catch (SQLException e) {
			output("统计方法出错！", e);
		}
	}
```

多线程实现：

```
package source;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
/**
 * @author ··-·尘
 * @E-mail:Fhaohaizi@163.com
 * @version 创建时间：2017年8月23日 下午2:56:22
 * @alter 修改时间：2017年8月24日 10:02:19 类说明：测试内存和cpu信息
 */
public class PerformanceThread extends Thread {
	public int mark = Common.getInstance().getMark();
	public static boolean key = true;
	public static String device_id;
	public static String package_name;
	public static String test_name;
	// 创建实例
	private static PerformanceThread performanceTest;
 
	@Override
	public void run() {
		while (key) {// 循环执行该方法
			try {
				sleep(15 * 1000);// 线程休眠
			} catch (InterruptedException e) {
				e.printStackTrace();// 打印异常
			}
			getCpuAndMemDate();// 执行手机数据的方法
		}
	}
 
	public static PerformanceThread getInstance() {
		if (performanceTest == null) {
			performanceTest = new PerformanceThread();
		}
		return performanceTest;
	}
 
	public PerformanceThread() {
		getDevicesId();
		PerformanceThread.package_name = Common.PAGEKAGE;
		output("欢迎使用手机性能监控线程！");
	}
 
	public PerformanceThread(String package_name) {
		getDevicesId();
		PerformanceThread.package_name = package_name;
		output("欢迎使用手机性能监控线程！");
	}
	
	public PerformanceThread(String testname, String package_name) {
		getDevicesId();
		PerformanceThread.package_name = package_name;
		PerformanceThread.test_name = testname;
		output("欢迎使用手机性能监控线程！");
	}
	
	/**
	 * 结束线程方法
	 */
	public void stopRecord() {
		AppLocalMySql.getInstance().ClearUpPerformaceData(mark);
		PerformanceThread.key = false;
	}
	/**
	 * 获取devicesID
	 */
	public void getDevicesId() {
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
				if (line.contains("device")) {
					PerformanceThread.device_id = line.replaceAll("device", "");
				}
				// System.out.println(line);// 输出
			}
			reader.close();// 此处reader依赖于input，应先关闭
			input.close();
		} catch (IOException e) {
			output("执行" + cmd + "失败！");
			e.printStackTrace();
		}
	}
 
	/**
	 * 获取cpu和mem数据，此方法用的是top命令
	 */
	public void getCpuAndMemDate() {
		int pss = getMemResult();// 获取内存数据
		int[] result = getPerformanceResult();// 获取数据
		int cpu_result = result[0];// 获取cpu
		int vss = result[1];// 获取虚拟内存数据
		int rss = result[2];// 获取物理内存数据
		output("CPU:" + cpu_result, "VSS:" + vss, "RSS:" + rss, "PSS:" + pss);
		AppLocalMySql.getInstance().saveMemAndCpuResult(mark, package_name, device_id, cpu_result, vss, rss, pss,
				test_name);// 写入数据库
	}
 
	/**
	 * 获取统计结果 利用Linux系统top命令 -m表示个数，-n表示获取次数此处写为1
	 * 
	 * @return 返回int数组，包含rss，vss，cpu百分比
	 */
	public int[] getPerformanceResult() {
		String cmd = Common.ADB_PATH + "adb shell top -m 8 -n 1";
		int cpu_result = 0, vss = 0, rss = 0;
		try {
			Process p = Runtime.getRuntime().exec(cmd);// 通过runtime类执行cmd命令
			// 正确输出流
			InputStream input = p.getInputStream();// 创建并实例化输入字节流
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));// 先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String line = "";
			while ((line = reader.readLine()) != null) {// 循环读取
				if (line.contains(package_name)) {// 获取行
					cpu_result = getCpuDate(line);
					vss = getVss(line);
					rss = getRss(line);
				}
			}
			reader.close();// 此处reader依赖于input，应先关闭
			input.close();// 关闭流
			// 错误输出流
			InputStream errorInput = p.getErrorStream();// 创建并实例化输入字节流
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorInput));// 先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String eline = "";
			while ((eline = errorReader.readLine()) != null) {// 循环读取
				System.out.println(eline);// 输出
			}
			errorReader.close();// 此处有依赖关系，先关闭errorReader
			errorInput.close();// 关闭流
		} catch (IOException e) {
			output("执行" + cmd + "失败！");
			e.printStackTrace();
		}
		int[] result = { cpu_result, vss, rss };// 新建数组保存数据
		return result;// 返回数组
	}
 
	/**
	 * 获取应用信息 利用Android系统dumpsys命令获取 命令能统计到java虚拟的堆内存和栈内存的使用情况
	 * 
	 * @return 返回内存占用
	 */
	public int getMemResult() {
		String cmd1 = Common.ADB_PATH + "adb shell dumpsys meminfo " + package_name;
		int mem_result = 0;
		try {
			Process p = Runtime.getRuntime().exec(cmd1);// 通过runtime类执行cmd命令
			// 正确输出流
			InputStream input = p.getInputStream();// 创建并实例化输入字节流
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));// 先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String line = "";
			while ((line = reader.readLine()) != null) {// 循环读取
				if (line.startsWith("        TOTAL")) {
					mem_result = getMemInfo(line);
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
			}
			errorReader.close();// 此处有依赖关系，先关闭errorReader
			errorInput.close();
		} catch (IOException e) {
			output("执行" + cmd1 + "失败！", e);
		}
		return mem_result;
	}
 
	/**
	 * 获取内存信息
	 * 
	 * @param info
	 *            截取到的APP信息
	 * @return 返回内存蚕蛹
	 */
	public int getMemInfo(String info) {
		int result = 0;
		Pattern r = Pattern.compile(" [0-9]+ ");
		Matcher m = r.matcher(info);
		if (m.find()) {
			// output(m.group());
			result = changeStringToInt(m.group().trim());
		}
		return result;
	}
 
	/**
	 * 获取cpu运行信息
	 * 
	 * @param info
	 *            截取到的APP信息
	 * @return 返回CPU占用率，采取double数据格式
	 */
	public double getCpuInfo(String info) {
		String percent = info.substring(0, info.indexOf("%"));
		double result = changeStringToDouble(percent.trim());
		return result;
	}
 
	/**
	 * 获取cpu运行信息
	 * 
	 * @param info
	 *            截取到的APP信息
	 * @return 返回CPU占用率，采取int数据格式
	 */
	public int getCpuDate(String info) {
		Pattern pattern = Pattern.compile(" [0-9]*%");
		Matcher matcher = pattern.matcher(info);
		matcher.find();
		String date = matcher.group().trim();
		return changeStringToInt(date.substring(0, date.length() - 1));
	}
 
	public int getVss(String info) {
		Pattern pattern = Pattern.compile(" [0-9]+K");
		Matcher matcher = pattern.matcher(info);
		matcher.find();
		String date = matcher.group().trim();
		return changeStringToInt(date.substring(0, date.length() - 1));
	}
 
	public int getRss(String info) {
		Pattern pattern = Pattern.compile("K +[0-9]+K");
		Matcher matcher = pattern.matcher(info);
		matcher.find();
		String date = matcher.group().substring(1, matcher.group().length() - 1);
		return changeStringToInt(date.trim());
	}
 
	// 把string类型转化为double
	public Double changeStringToDouble(String text) {
		// return Integer.parseInt(text);
		return Double.valueOf(text);
	}
 
	// 把string类型转化为int
	public int changeStringToInt(String text) {
		// return Integer.parseInt(text);
		return Integer.valueOf(text);
	}
 
	public void output(String text) {// 明显输出
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
8. [测试之JVM命令脑图](https://mp.weixin.qq.com/s/qprqyv0j3SCvGw1HMjbaMQ)
9. [将json数据格式化输出到控制台](https://mp.weixin.qq.com/s/2IPwvh-33Ov2jBh0_L8shA)
10. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
11. [“双花”BUG的测试分享](https://mp.weixin.qq.com/s/0dsBsssNfg-seJ_tu9zFaQ)

## [公众号地图](https://mp.weixin.qq.com/s/36RbP20beZ8oWJ9nLAxG3g) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)



> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)


<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---


<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>