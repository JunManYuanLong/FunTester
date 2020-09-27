# 如何在Mac OS上使用UiAutomator快速调试类

本人最近在Mac OS上使用UiAutomator快速调试类的时候发现跟Windows环境下使用有很大的区别，对于我这个Mac OS小白来说有很多坑要填，今天终于修改完毕，分享代码，供大家参考。主要区别就是在执行命令的时候需要把命令前面加上执行全路径。还有一个就是斜杠的问题，统一改过来就可以了。

遇到的报错情况：

下面这个是没有配置全路径时的报错信息：

` Cannot run program "android": error=2, No such file or directory`
下面这个是路径错误时的报错信息：

`Cannot run program "/Users/dahaohaozai/android-sdk-macosx/toos/android": error=2, No such file or directory`
下面是调试类的代码：


```
package source;
 
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
 
/**
 * @author ··-·尘
 * @E-mail:Fhaohaizi@163.com
 * @version 创建时间：2017年8月18日 上午10:53:24
 * @alter 修改时间： 2017年10月23日10:19:34 类说明：测试调试用例
 */
public class UiAutomatorHelper extends Common {
 
	private static String android_id = "1";// androidId，写好不用传参
	private static String jar_name = "";// jar名字
	private static String test_class = "";// 包名.类名
	private static String test_name = "";// 用例名
	// private static String devices = Common.NEXUS5DEVICESID;//自定义设备ID
	private static String workspace_path;// 工作空间不需要配置，自动获取工作空间目录
 
	public UiAutomatorHelper() {// 如果类有带参构造方法，必须把隐藏的空参构造方法写出来
		output("欢迎使用自定义调试类！");
	}
 
	/**
	 * 构造方法
	 * 
	 * @param jarName
	 *            jar包的名字
	 * @param testClass
	 *            类名
	 * @param testName
	 *            方法名Ï
	 */
	public UiAutomatorHelper(String jarName, String testClass, String testName) {
		output("欢迎使用自定义调试类！");
		workspace_path = getWorkSpase();
		jar_name = jarName;
		test_class = testClass;
		test_name = testName;
		// Common.getInstance().setMobileInputMethodToUtf();//设置输入法为utf7
		runUiautomator();
		// Common.getInstance().setMobileInputMethodToQQ();//设置输入法为QQ输入法
		output(Common.LINE + "---FINISH DEBUG----" + Common.LINE);// 结束
	}
 
	// 运行步骤
	private void runUiautomator() {
		creatBuildXml();
		modfileBuild();
		buildWithAnt();
		pushTestJar(workspace_path + "/bin/" + jar_name + ".jar");
		runTest(jar_name, test_class + "#" + test_name);
	}
 
	// 创建build.xml
	public void creatBuildXml() {
		// System.out.println"android create uitest-project -n " + jar_name + " -t " +
		// android_id + " -p " + workspace_path);
		execCmd(ANDROID_PATH + "android create uitest-project -n " + jar_name + " -t " + android_id + " -p "
				+ workspace_path);
	}
 
	// 修改build
	public void modfileBuild() {
		StringBuffer stringBuffer = new StringBuffer();// 创建并实例化stringbuffer
		try {
			File file = new File("build.xml");
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));// 通过文件字节输入流创建并实例化输出字符流（流转换）
				BufferedReader bufferedReader = new BufferedReader(read);// 创建并实例化BufferedReader，用来接收字符流
				String lineTxt = null;// 用来接收readline的结果
				while ((lineTxt = bufferedReader.readLine()) != null) {// 循环读取处理内容
					if (lineTxt.matches(".*help.*")) {// 正则匹配
						lineTxt = lineTxt.replaceAll("help", "build");// 替换help为build
					}
					stringBuffer = stringBuffer.append(lineTxt + "\t\n");// stringbuffer接收修改后的内容
				}
				bufferedReader.close();// 关闭流，有依赖关系所以先关闭
				read.close();// 关闭流
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
 
	// ant 执行build
	public void buildWithAnt() {
		execCmd(ANT_PATH + "ant");
	}
 
	/**
	 *  把jar包push到手机上
	 * @param localPath jar包的绝对路径
	 */
	public void pushTestJar(String localPath) {
		String pushCmd = ADB_PATH + "adb push " + localPath + " /data/local/tmp/";
		execCmd(pushCmd);
	}
 
	/**
	 * 运行用例方法
	 * 
	 * @param jarName
	 *            jar包名字
	 * @param testName
	 *            运行方法名字
	 */
	public void runTest(String jarName, String testName) {
		String runCmd = ADB_PATH + "adb shell uiautomator runtest ";
		String testCmd = jarName + ".jar " + "--nohup -c " + testName;
		execCmd(runCmd + testCmd);
	}
 
	// 获取工作空间
	public String getWorkSpase() {
		File directory = new File("");// 创建并实例化file对象
		String abPath = directory.getAbsolutePath();// 获取绝对路径
		return abPath;
	}
 
	// 执行cmd命令
	public void execCmd(String cmd) {
		try {
			Process p = Runtime.getRuntime().exec(cmd);// 通过runtime类执行cmd命令
			// 正确输出流
			InputStream input = p.getInputStream();// 创建并实例化输入字节流
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));// 先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String line = "";
			while ((line = reader.readLine()) != null) {// 循环读取
				System.out.println(line);// 输出
				saveToFile(line, "runlog.log", false);// 保存，false表示不覆盖
			}
			reader.close();// 此处reader依赖于input，应先关闭
			input.close();
			// 错误输出流
			InputStream errorInput = p.getErrorStream();// 创建并实例化输入字节流
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorInput));// 先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String eline = "";
			while ((eline = errorReader.readLine()) != null) {// 循环读取
				System.out.println(eline);// 输出
				saveToFile(eline, "runlog.log", false);// 保存，false表示不覆盖
			}
			errorReader.close();// 此处有依赖关系，先关闭errorReader
			errorInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	// 覆盖写入文件
	public void writerText(String path, String content) {
		File dirFile = new File(path);
		if (!dirFile.exists()) {// 如果不存在，新建
			dirFile.mkdir();
		}
		try {
			// 这里加入true 可以不覆盖原有TXT文件内容，续写
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(path));// 通过文件输出流来用bufferedwrite接收写入
			bw1.write(content);// 将内容写到文件中
			bw1.flush();// 强制输出缓冲区内容
			bw1.close();// 关闭流
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	// 写入文档，注释见writerText方法
	public void saveToFile(String text, String path, boolean isClose) {
		File file = new File("runlog.log");
		BufferedWriter bf = null;
		try {
			FileOutputStream outputStream = new FileOutputStream(file, true);
			OutputStreamWriter outWriter = new OutputStreamWriter(outputStream);
			bf = new BufferedWriter(outWriter);
			bf.append(text);// 添加内容
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