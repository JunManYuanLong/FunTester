# android UiAutomator运行用例并获取运行信息的方法
本人在使用调试类做UiAutomator测试的时候，一直想着把UiAutomator自己的测试报告里的内容进行过滤，筛选出自己需要的信息，经过不断尝试和改进，终于完成。分享出来供大家参考。我是用string数组来存放用例信息的，也可以用map或许更好一点，暂时还没尝试。

```
public static String[] execCmdAndReturnResult(String jarName,String testclass, String testName, int orderno) {
		int status = 0;//此参数用于提取运行code
		String runCmd = "adb shell uiautomator runtest ";//调试命令的前半部分
		String testCmd = jarName + ".jar " + "--nohup -c " + testclass + "#" + testName;//调试命令的后半部分
		System.out.println("----runTest:  " + runCmd + testCmd);//输出调试命令
		String runresult = "";//运行结果
		String runinfo = "";//运行信息
		String errorlineinliabrary = "";//在library类的错误行
		String errorlineinspecial = "";//在special类的错误行
		String errorlineincase = "";//在case类的错误行
		Date time1 = new Date();//开始时间
		SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置时间格式
		String starttime = now.format(time1);//转换时间格式
		String cmd = runCmd + testCmd;//拼接调试命令
		System.out.println("----execCmd:  " + cmd);//输出调试命令
		try {
			Process p = Runtime.getRuntime().exec("cmd /c " + cmd);//借助runtime类执行调试命令
			// 正确输出流
			InputStream input = p.getInputStream();//正确输出流，新建输入流
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));//将字节输入流转化为reader字符输入流，并用bufferedreader接收数据
			String line = "";//用来接收输入流字符流的每一行
			while ((line = reader.readLine()) != null) {//每次读取一行赋值给line
				System.out.println(line);//输出此行
				saveToFile(line, "reportlog.log", false);//保存此行到log里面
				if (line.startsWith("INSTRUMENTATION_STATUS_CODE:")) {//获取运行报告的code码
					status ++;//此处因为每一个报告都有不只一个code，我们只要最后一个
					if (status == 2) {//当status=2时，就是我们要的code
						System.out.println(getCode(line));//输出此行的code
						if (getCode(line).equalsIgnoreCase("-1")) {//如果-1，则运行错误
							runresult = "运行错误";
							} else if (getCode(line).equalsIgnoreCase("-2")) {//如果-2则是断言错误
								runresult = "断言错误";
							} else {
								runresult = "运行成功";//此处就不判断其他情况了，直接表示运行成功
								}
						}
					}
				if (line.startsWith("INSTRUMENTATION_STATUS: stack=")) {//获取错误和异常
					runinfo = line.substring(30, line.length());//截取我们需要的错误和异常
					}
				if (line.startsWith("	at student.Special.")) {//获取错误在special类发生的行
					errorlineinspecial = line.substring(line.indexOf(".")+1, line.length());//截取错误发生行
					}
				if (line.startsWith("	at student.Case.")) {//获取错误在case类发生的行
					errorlineincase = line.substring(line.indexOf(".")+1, line.length());//截取错误发生行
					}
				if (line.startsWith("	at student.Library.")) {//获取错误在library类发生的行
					errorlineinliabrary = line.substring(line.indexOf(".")+1, line.length());//截取错误发生行
					}
				}
			reader.close();//此处有依赖关系，先关闭reader
			input.close();//关闭字节输入流
			// 错误输出流
			InputStream errorInput = p.getErrorStream();//获取错误流，新建输出流
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorInput));////将字节输入流转化为reader字符输入流，并用bufferedreader接收数据
			String eline = "";////用来接收输入流字符流的每一行
			while ((eline = errorReader.readLine()) != null) {//每次读取一行赋值给line
				System.out.println(eline);//输出此行
				saveToFile(line, "runlog.log", false);//保存此行到log里面
			}
			errorReader.close();//此处有依赖关系，先关闭errorReader
			errorInput.close();//关闭输入字节流
			} catch (IOException e) {
				e.printStackTrace();//抛出异常
				}
		Date time = new Date();//获取结束时间
		String endtime = now.format(time);//转化时间格式
		String[] result = new String[9];//新建数组存放运行信息
		result[0] = (orderno + 1) + "";//表示运行的用例的顺序
		result[1] = testName;//用例名
		result[2] = runresult;//运行结果
		result[3] = runinfo;//运行信息
		result[4] = errorlineinliabrary;//在library类的错误行
		result[5] = errorlineinspecial;//在special类的错误行
		result[6] = errorlineincase;//在case类的错误行
		result[7] = starttime;//开始时间
		result[8] = endtime;//结束时间
		return result;//返回运行信息
	}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>