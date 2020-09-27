# android UiAutomator生成测试报告的基本方法封装
上次发过了UiAutomator的基本方法封装，由于我使用调试类做的测试报告，所以一些方法得单独进行封装，下面就是我生成报告部分封装的测试方法。

```
package source;
 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class Common {
	public static final String LINE = "\r\n";
	public static final String TAB = "\t";
	public static final String MX3DEVICESID = "353ACJJA4L8A";
	public static final String NEXUS5DEVICESID = "06dbd10c0ae4e3af";
	public static Common useOften = new Common();
	public static Common getInstance() {
		return useOften;
	}
	public void outputBegin() {//输出开始
		System.out.println(getNow()+"..-. ...- 开始！");
	}
	public void outputNow() {//输出当前时间
		System.out.println(getNow());
	}
	public void outputOver() {//输出结束
		System.out.println(getNow()+"..-. ...- 结束！");
	}
	public void output(String text) {//明显输出
		System.out.println(text);
	}
	public void output(String ...text) {//方法重载
		for (int i = 0; i < text.length; i++) {
			System.out.println("第"+ (i+1) + "个："+ text[i]);
		}
	}
	public void output(double num) {//明显输出
		System.out.println(num);
	}
	public void output(double ...num) {//方法重载
		for (int i = 0; i < num.length; i++) {
			System.out.println("第"+ (i+1) + "个："+ num[i]);
		}
	}
	public void output(int num) {//方法重载
		System.out.println(num);
	}
	public void output(int ...num) {//方法重载
		for (int i = 0; i < num.length; i++) {
			System.out.println("第"+ (i+1) + "个："+ num[i]);
		}
	}
	//输出时间差
	public void outputTimeDiffer(Date start, Date end) {
		long time = end.getTime() - start.getTime();
		double differ = (double)time/1000;
		output("总计用时"+differ+"秒！");
	}
	//匹配短信验证码
	public String findCode(String message) {
		Pattern r = Pattern.compile("[0-9]{6}");
		Matcher m = r.matcher(message);
		if (m.find()) {
			output("匹配成功");
			for(int i=0;i<=m.groupCount();i++){
				System.out.println(m.group(i));
				}
			}
		return m.group(0);
		}
	//获取内存信息
	public int getMemInfo(String info) {
		int result = 0;
		Pattern r = Pattern.compile(" [0-9]+ ");
		Matcher m = r.matcher(info);
		if (m.find()) {
			System.out.println(m.group());
			result = changeStringToInt(m.group().trim());
			}
		return result;
		}
	//获取cpu运行信息
	public double getCpuInfo(String info) {
		String percent = info.substring(0, info.indexOf("%"));
		double result = changeStringToDouble(percent.trim());
		return result;
	}
	//把string类型转化为int
	public int changeStringToInt (String text) {
//		return Integer.parseInt(text);
		return Integer.valueOf(text);
	}
	//把string类型转化为double
	public Double changeStringToDouble (String text) {
//		return Integer.parseInt(text);
		return Double.valueOf(text);
	}
	//执行cmd命令
	public void execCmd(String cmd) {
		try {
			Process p = Runtime.getRuntime().exec(cmd);//通过runtime类执行cmd命令
			// 正确输出流
			InputStream input = p.getInputStream();//创建并实例化输入字节流
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));//先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String line = "";
			while ((line = reader.readLine()) != null) {//循环读取
					System.out.println(line);//输出
					saveToFile(line, "runlog.log", false);//保存，false表示不覆盖
					}
			reader.close();//此处reader依赖于input，应先关闭
			input.close();
			// 错误输出流
			InputStream errorInput = p.getErrorStream();//创建并实例化输入字节流
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorInput));//先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String eline = "";
			while ((eline = errorReader.readLine()) != null) {//循环读取
				System.out.println(eline);//输出
				saveToFile(eline, "runlog.log", false);//保存，false表示不覆盖
			}
			errorReader.close();//此处有依赖关系，先关闭errorReader
			errorInput.close();
		} catch (IOException e) {
			output("执行" + cmd + "失败！");
			e.printStackTrace();
		}
	}
	public String[] execCmdAndReturnResult(String jarName,String testClass, String testName, int orderno) {
		int status = 0;//此参数用于提取运行code
		String runCmd = "adb shell uiautomator runtest ";//调试命令的前半部分
		String testCmd = jarName + ".jar " + "--nohup -c " + testClass + "#" + testName;//调试命令的后半部分
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
	//此方法仅用于运行已完成的方法，且指定设备为nexus
	public void runTest(String jarName, String testClass, String testName) throws Exception {
		setMobileInputMethodToUtf();
		String runCmd = "adb -s " + NEXUS5DEVICESID + " shell uiautomator runtest ";//调试命令的前半部分
		String testCmd = jarName + ".jar " + "--nohup -c " + testClass + "#" + testName;//调试命令的后半部分
//		System.out.println("----runTest:  " + runCmd + testCmd);//输出调试命令
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
				saveToFile(line, "runlog.log", false);//保存此行到log里面
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
				throw new Exception("执行cmd命令出错！");
				}
		setMobileInputMethodToQQ();
	}
	//保存到log文件中	
	public void saveToFile(String text, String path, boolean isClose) throws IOException {
		File file = new File(path);
		BufferedWriter bf = null;
		FileOutputStream outputStream = new FileOutputStream(file, true);
		OutputStreamWriter outWriter = new OutputStreamWriter(outputStream);
		bf = new BufferedWriter(outWriter);
		bf.append(text);
		bf.newLine();
		bf.flush();
		if (isClose) {
			bf.close();
			}
		outWriter.close();//此处有依赖关系，先关闭outWriter
		outputStream.close();
		}
	//覆盖写入文件
	public void writerText(String path, String content) {
		File dirFile = new File(path);
		if (!dirFile.exists()) {//如果不存在，新建
			dirFile.mkdir();
		}
		try {
			//这里加入true 可以不覆盖原有TXT文件内容，续写
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(path));//通过文件输出流来用bufferedwrite接收写入
			bw1.write(content);//将内容写到文件中
			bw1.flush();//强制输出缓冲区内容
			bw1.close();//关闭流
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//修改nexus手机输入法为utf7
	public void setMobileInputMethodToUtf() {
		execCmd("adb -s 06dbd10c0ae4e3af shell settings put secure default_input_method jp.jun_nama.test.utf7ime/.Utf7ImeService");
		}
	//设置nexus为QQ输入法
	public void setMobileInputMethodToQQ() {
		execCmd("adb -s 06dbd10c0ae4e3af shell settings put secure default_input_method com.tencent.qqpinyin/.QQPYInputMethodService");
		}
	//获取测试用例名
	public String getTest(String text) {
		return text.substring(29, text.length());
	}
	//获取运行状态码
	public String getCode(String text) {
		return text.substring(29, text.length());
	}
	//获取随机数
	public int getRandomInt(int num) {
		return new Random().nextInt(num);
		}
	//复制文件
	public void copyFile(String oldPath, String newPath) {
		System.out.println("源文件路径：" + oldPath);
		System.out.println("目标文件路径：" + newPath);
		try {
			int bytesum = 0;//这个用来统计需要写入byte数组的长度
			int byteread = 0;//这个用来接收read()方法的返回值
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath);//读入原文件，实例化输入流
				FileOutputStream fs = new FileOutputStream(newPath);//实例化输出流
				byte[] buffer = new byte[10000];
				while ((byteread = inStream.read(buffer)) != -1) {//如何读取到文件末尾
					bytesum += byteread;//字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);//此方法第一个参数是byte数组，第二次参数是开始位置，第三个参数是长度
					}
				fs.flush();//强制缓存输出
				fs.close();//关闭输出流
				inStream.close();//关闭输入流
				}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
//		File oldfile2 = new File(oldPath);
//		oldfile2.delete();
	}
	public String getDevicesId(String devices) {
		if (devices.equals("MX3")) {
			return MX3DEVICESID;
			} else if (devices.equals("nexus5")) {
				return NEXUS5DEVICESID;
				} else {
					return NEXUS5DEVICESID;
					}
	}
	//新建测试报告目录
	public String creatNewFile(String time) {
		File file = new File("C:\\Users\\fankaiqiang\\Desktop\\888\\"+"testreport"+time);
		if (!file.exists()) {
			file.mkdir();
			}
		return file.toString();
	}
	//获取当前工作路径
	public String getWorkSpase() {
		File directory = new File("");
		String abPath = directory.getAbsolutePath();
		return abPath;
	}
	//执行cmd命令，控制台信息编码方式GBK
	public void execCmdByGBK(String cmd) throws IOException {
		System.out.println(cmd);
		try {  
            Process p = Runtime.getRuntime().exec("cmd /c " + cmd);
            InputStreamReader inputStreamReader = new InputStreamReader(p.getInputStream(), Charset.forName("GBK"));
            BufferedReader br = new BufferedReader(inputStreamReader);  
            String line=null;  
            while((line = br.readLine()) != null){  
            	System.out.println(line); 
            	}
            br.close();//此处有依赖，先关闭br
            inputStreamReader.close();
            } catch (IOException e) {
            	output("执行" + cmd + "失败！");
            	e.printStackTrace(); 
            	}
        }
	//设置nexus亮度250
	public void setScreenLightTo250() {
		execCmd("adb -s "+NEXUS5DEVICESID+" shell settings put system screen_brightness 250");
	}
	//设置nexus屏幕亮度150
	public void setScreenLightTo150() {
		execCmd("adb -s "+NEXUS5DEVICESID+" shell settings put system screen_brightness 150");
	}
	//删除字符
	public String deleteCharFromString(String delChar, String text) {
		return text.replace(delChar, "");
	}
	public String getNow() {//获取当前时间
		Date time = new Date();
		SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String c = now.format(time);
		return c;
		}
	public void deleteScreenShot() {//删除截图文件夹
		File file = new File("/mnt/sdcard/123/");
		if (file.exists()) {//如果file存在
			File[] files = file.listFiles();//获取文件夹下文件列表  
		    for (int i = 0; i < files.length; i++) {//遍历删除
		    	files[i].delete();
		    }
		    file.delete();//最后删除文件夹，如果不存在直接删除文件夹
		} else {
			output("文件夹不存在！");
		}
		
	}
 
}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>