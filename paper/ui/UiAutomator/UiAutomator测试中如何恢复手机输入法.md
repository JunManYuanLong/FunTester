# UiAutomator测试中如何恢复手机输入法


本人在使用UiAutomator测试的时候，需要用到utf7输入法，每次执行之前都会切换到utf7输入法，然后每次执行结束之后再切换到正常输入法，由于测试机器比较多，所以写了一个自动切换到其他任意输入法的方法。分享代码，供大家参考。


```
	/**
	 * 修改手机输入法为utf7
	 */
	public void setMobileInputMethodToUtf() {
		execCmdAdb("adb shell settings put secure default_input_method jp.jun_nama.test.utf7ime/.Utf7ImeService");
	}
 
	/**
	 * 设置其他输入法
	 */
	public void setMobileInputMethodToOthers() {
		String name;
		List<String> lists = execCmdAndReturnResult("adb shell ime list -s");
		for (int i = 0; i < lists.size(); i++) {
			if (!lists.get(i).contains("utf7ime")) {
				name = lists.get(i);
				execCmdAdb("adb shell settings put secure default_input_method " + name);
			}
		}
	}
```
其中execCmdAdb和execCmdAndReturnResult方法如下：


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

```
/**
	 * 执行adb命令，返回信息
	 * 
	 * @param cmd
	 *            命令内容
	 */
	public List<String> execCmdAndReturnResult(String cmd) {
		output("正在执行：" + cmd);
		List<String> result = new ArrayList<String>();
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
			while ((line = reader.readLine()) != null) {
				// output(line);
				result.add(line);
			}
			reader.close();// 此处reader依赖于input，应先关闭
			input.close();
		} catch (IOException e) {
			output("执行" + cmd + "失败！");
			e.printStackTrace();
		}
		return result;
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