# 如何使用logcat记录所有页面的加载时间
本人学习使用APP性能测试的过程中，需要统计页面的启动时间，因为自己写了一个logcat的监控线程，所以想把所有的activity的启动时间都记录下来，留作参考。经过尝试，总算是完成了，分享代码供大家参考。

下面是logcat的执行方法：

```
/**
	 * 执行adb logcat命令此方法已过滤掉I级别日常不做记录
	 * 
	 * @param cmd
	 *            命令内容
	 * @param fileName
	 *            输入文件路径
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
				//收集启动时间
				LauchTime.getActivityTimes(line);
				if (line.startsWith("I/") || line.contains(" I ")) {
					continue;
				}
				if (line.contains("happyjuzi")) {
					Common.getInstance().saveToFile(line, fileName);// 保存
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
					Common.getInstance().saveToFile(line, fileName);// 保存
				}
			}
			errorReader.close();// 此处有依赖关系，先关闭errorReader
			errorInput.close();
		} catch (IOException e) {
			Common.getInstance().output("执行" + cmd + "失败！");
			e.printStackTrace();
		}
	}
```
下面是getActivityTimes（）方法：

```
/**
	 * 通过log获取activity启动用时
	 * 
	 * @param line
	 *            每条log
	 */
	public static void getActivityTimes(String line) {
		if (line.contains("Displayed") && line.contains("juzi")) {
			String activity = getActivityName(line);
			double time = getLauchTime(line);
			output(activity, time);
			AppLocalMySql.getInstance().saveLauchTime(test_name, device, package_name, activity, time);
		}
	}
 
	/**
	 * 获取启动时间
	 * 
	 * @param line
	 *            截取到的log信息
	 * @return 返回double时间，单位s，默认0.00
	 */
	private static double getLauchTime(String line) {
		Matcher matcher = pattern_time.matcher(line);
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
 
	/**
	 * 获取activity名
	 * 
	 * @param line
	 *            截取到的log信息
	 * @return 返回activity名
	 */
	private static String getActivityName(String line) {
		Matcher matcher = pattern_name.matcher(line);
		if (matcher.find()) {
			line = matcher.group(0);
			line = line.substring(0, line.length() - 1);
		}
		return line;
	}
```

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>