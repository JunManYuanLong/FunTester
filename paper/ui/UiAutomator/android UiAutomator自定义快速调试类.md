# android UiAutomator自定义快速调试类

本人在使用UiAutomator的过程中，一直用快速调试类来做测试，发现其中很多地方都需要根据不同的需求做修改，今天特意花了点时间总体修改一遍，更加灵活了，又写了很多中文注释。分享出来，供大家参考。


```
package student;
 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
 
public class UiAutomatorHelper {
 
	private static String android_id = "1";//androidId，写好不用传参
	private static String jar_name = "";//jar名字
	private static String test_class = "";//包名.类名
	private static String test_name = "";//用例名
	private static String devices = UseOften.NEXUS5DEVICESID;//自定义设备ID
	private static String workspace_path;// 工作空间不需要配置，自动获取工作空间目录
	public UiAutomatorHelper() {//如果类有带参构造方法，必须把隐藏的空参构造方法写出来
		Library.getInstance().output("欢迎使用自定义调试类！");
	}
	public UiAutomatorHelper(String jarName, String testClass, String testName) {
		workspace_path = getWorkSpase();
		jar_name = jarName;
		test_class = testClass;
		test_name = testName;
		UseOften.getInstance().setMobileInputMethodToUtf();//设置输入法为utf7
		runUiautomator();
		UseOften.getInstance().setMobileInputMethodToQQ();//设置输入法为QQ输入法
		System.out.println(UseOften.LINE+"---FINISH DEBUG----"+UseOften.LINE);//结束
	}
	// 运行步骤
	private void runUiautomator() {
		creatBuildXml();
		modfileBuild();
		buildWithAnt();
		pushTestJar(workspace_path + "\\bin\\" + jar_name + ".jar");
		runTest(jar_name, test_class + "#" + test_name);
	}
 
	//创建build.xml
	public void creatBuildXml() {
		execCmd("cmd /c android create uitest-project -n " + jar_name + " -t " + android_id + " -p " + "\""
				+ workspace_path + "\"");
	}
	//修改build
	public void modfileBuild() {
		StringBuffer stringBuffer = new StringBuffer();//创建并实例化stringbuffer
		try {
			File file = new File("build.xml");
			if (file.isFile() && file.exists()) { //判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));//通过文件字节输入流创建并实例化输出字符流（流转换）
				BufferedReader bufferedReader = new BufferedReader(read);//创建并实例化BufferedReader，用来接收字符流
				String lineTxt = null;//用来接收readline的结果
				while ((lineTxt = bufferedReader.readLine()) != null) {//循环读取处理内容
					if (lineTxt.matches(".*help.*")) {//正则匹配
						lineTxt = lineTxt.replaceAll("help", "build");//替换help为build
					}
					stringBuffer = stringBuffer.append(lineTxt + "\t\n");//stringbuffer接收修改后的内容
				}
				bufferedReader.close();//关闭流，有依赖关系所以先关闭
				read.close();//关闭流
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		// 修改后写回去
		writerText("build.xml", new String(stringBuffer));
	}
	//ant 执行build
	public void buildWithAnt() {
		execCmd("cmd /c ant");
	}
	//把jar包push到手机上
	public void pushTestJar(String localPath) {
		localPath = "\"" + localPath + "\"";
		String pushCmd = "adb -s "+devices+" push " + localPath + " /data/local/tmp/";
		execCmd(pushCmd);
	}
	//运行用例方法
	public void runTest(String jarName, String testName) {
		String runCmd = "adb -s "+devices+" shell uiautomator runtest ";//此处-s表示nexus机器
		String testCmd = jarName + ".jar " + "--nohup -c " + testName;
		execCmd(runCmd + testCmd);
	}
	//获取工作空间
	public String getWorkSpase() {
		File directory = new File("");//创建并实例化file对象
		String abPath = directory.getAbsolutePath();//获取绝对路径
		return abPath;
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
			e.printStackTrace();
		}
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
	//写入文档，注释见writerText方法
	public void saveToFile(String text, String path, boolean isClose) {
		File file = new File("runlog.log");
		BufferedWriter bf = null;
		try {
			FileOutputStream outputStream = new FileOutputStream(file, true);
			OutputStreamWriter outWriter = new OutputStreamWriter(outputStream);
			bf = new BufferedWriter(outWriter);
			bf.append(text);//添加内容
			bf.newLine();
			bf.flush();
			if (isClose) {
				bf.close();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
8. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
9. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
10. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
11. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
12. [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
13. [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)
14. [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)

## [公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>