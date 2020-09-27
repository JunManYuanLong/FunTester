# android UiAutomator使用多线程监控手机内存和cpu使用并存入数据库中
本人在使用UiAutomator做自动化测试的时候，需要对对手机在运行用例或者执行monkey期间的cpu和内存使用情况做统计。想了一个方案，使用多线程，执行adb shell top命令，然后处理得到的信息，写入数据库中。经过尝试终于成功了，效果还不错，分享出来，供大家参考。

下图为数据库中的数据。
![](/blog/pic/20170823193053269.png)


备注：代码里包含了使用adb shell dumpsys获取信息的方法，不过在dumpsys cpuinfo的时候遇到一些问题，统计的是一个很长的时间段的，后来就放弃了这种方法，重新写了getCpuAndMemDate()方法。

明天来了补上注释。（已补）

下面是统计信息和存入数据库的类。

```
package source;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
* @author ··-·尘
* @E-mail:Fhaohaizi@163.com
* @version 创建时间：2017年8月23日 下午2:56:22
* @alter 修改时间：2017年8月24日 10:02:19
* 类说明：测试内存和cpu信息
*/
public class PerformanceTest extends Thread{
	public static boolean key = true;
	public int test_mark = (int) (System.currentTimeMillis()/1000);
	//创建实例
	private static PerformanceTest performanceTest = new PerformanceTest();
	//公共访问方法
	public static PerformanceTest getInstance() {
		return performanceTest;
	}
	//私有构造方法
	private PerformanceTest() {
		output("欢迎使用手机性能监控线程！");
	}
	//用于调试监控方法，可删除
//	public static void main(String[] args) throws SQLException {
//		PerformanceTest.getInstance().getCpuAndMemDate();
//	}
	//重写run方法
	@Override
	public void run() {
		while(key) {//循环执行该方法
			try {
				sleep(15 * 1000);//线程休眠
				} catch (InterruptedException e1) {
					e1.printStackTrace();//打印异常
					}
			try {
//				getCpuAndMemResult();//该方法由于dumpsys数据不准确暂时放弃
				getCpuAndMemDate();//执行监控方法
				} catch (SQLException e) {
					e.printStackTrace();//打印异常
					}
			}
		}
	//获取cpu和mem数据
	public void getCpuAndMemDate() throws SQLException {
		int[] result = getPerformanceResult();//获取数据
		int cpu_result = result[0];//获取cpu
		int vss = result[1];//获取虚拟内存数据
		int rss = result[2];//获取物理内存数据
		AppLocalMySql.getInstance().saveMemAndCpuResult(test_mark, cpu_result, vss, rss);//写入数据库
	}
	//获取cpu和mem数据
	public void getCpuAndMemResult() throws SQLException {
		double cpu_result = getCpuResult();//获取cpu数据
		int mem_result = getMemResult();//获取内存数据
		AppLocalMySql.getInstance().saveMemAndCpuResult(test_mark, cpu_result, mem_result);//写入数据库
		}
	//获取统计结果
	public int[] getPerformanceResult() {
		String cmd = "adb shell top -m 10 -n 1";//执行adb命令-m表示条数，-n循环获取次数，此处写为1
		//初始化
		int cpu_result = 0;
		int vss = 0;
		int rss = 0;
		try {
			Process p = Runtime.getRuntime().exec(cmd);//通过runtime类执行cmd命令
			// 正确输出流
			InputStream input = p.getInputStream();//创建并实例化输入字节流
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));//先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String line = "";
			while ((line = reader.readLine()) != null) {//循环读取
				if (line.contains("com.gaotu100.superclass")) {//获取行
//					output(line);//输出行
					//获取数据
					cpu_result = getCpuDate(line);
					vss = getVss(line);
					rss = getRss(line);
					}
				}
			reader.close();//此处reader依赖于input，应先关闭
			input.close();//关闭流
			// 错误输出流
			InputStream errorInput = p.getErrorStream();//创建并实例化输入字节流
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorInput));//先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String eline = "";
			while ((eline = errorReader.readLine()) != null) {//循环读取
				System.out.println(eline);//输出
				}
			errorReader.close();//此处有依赖关系，先关闭errorReader
			errorInput.close();//关闭流
			} catch (IOException e) {
				output("执行" + cmd + "失败！");
				e.printStackTrace();
				}
		int[] result = {cpu_result, vss, rss};//新建数组保存数据
		return result;//返回数组
	}
	public int getMemResult() {
		String cmd1 = "adb shell dumpsys meminfo com.gaotu100.superclass";
		int mem_result = 0;
		try {
			Process p = Runtime.getRuntime().exec(cmd1);//通过runtime类执行cmd命令
			// 正确输出流
			InputStream input = p.getInputStream();//创建并实例化输入字节流
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));//先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String line = "";
			while ((line = reader.readLine()) != null) {//循环读取
				if (line.startsWith("        TOTAL")) {
					mem_result = getMemInfo(line);
				}
					}
			reader.close();//此处reader依赖于input，应先关闭
			input.close();
			// 错误输出流
			InputStream errorInput = p.getErrorStream();//创建并实例化输入字节流
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorInput));//先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String eline = "";
			while ((eline = errorReader.readLine()) != null) {//循环读取
				System.out.println(eline);//输出
				}
			errorReader.close();//此处有依赖关系，先关闭errorReader
			errorInput.close();
			} catch (IOException e) {
				output("执行" + cmd1 + "失败！");
				e.printStackTrace();
				}
		return mem_result;
		}
	
	//获取cpu统计结果
	public double getCpuResult() {
		String cmd = "adb shell dumpsys cpuinfo";
		double cpu_result = 0.0;
		try {
			Process p = Runtime.getRuntime().exec(cmd);//通过runtime类执行cmd命令
			// 正确输出流
			InputStream input = p.getInputStream();//创建并实例化输入字节流
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));//先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String line = "";
			while ((line = reader.readLine()) != null) {//循环读取
				if (line.contains("com.gaotu100.superclass")) {
					cpu_result += getCpuInfo(line);
					}
				}
			reader.close();//此处reader依赖于input，应先关闭
			input.close();
			// 错误输出流
			InputStream errorInput = p.getErrorStream();//创建并实例化输入字节流
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorInput));//先通过inputstreamreader进行流转化，在实例化bufferedreader，接收内容
			String eline = "";
			while ((eline = errorReader.readLine()) != null) {//循环读取
				System.out.println(eline);//输出
				}
			errorReader.close();//此处有依赖关系，先关闭errorReader
			errorInput.close();
			} catch (IOException e) {
				output("执行" + cmd + "失败！");
				e.printStackTrace();
				}
		return cpu_result;
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
	//获取cpu运行信息
	public int getCpuDate(String info) {
		Pattern pattern = Pattern.compile(" [0-9]*%");
		Matcher matcher = pattern.matcher(info);
		if (matcher.find()) {
			output(matcher.group());
		}
		String date = matcher.group().trim();
		output(date.substring(0, date.length() -1 ));
		return changeStringToInt(date.substring(0, date.length() -1 ));
	}
	
	public int getVss(String info) {
		Pattern pattern = Pattern.compile(" [0-9]+K");
		Matcher matcher = pattern.matcher(info);
		if (matcher.find()) {
			output(matcher.group());
		}
		String date = matcher.group().trim();
//		output(date.substring(0, date.length() -1));
		return changeStringToInt(date.substring(0, date.length() -1));
	}
	public int getRss(String info) {
		Pattern pattern = Pattern.compile("K +[0-9]+K");
		Matcher matcher = pattern.matcher(info);
		if (matcher.find()) {
			output(matcher.group());
		}
		String date = matcher.group().substring(1, matcher.group().length() - 1);
//		output(date.trim());
		return changeStringToInt(date.trim());
	}
	
	//把string类型转化为double
	public Double changeStringToDouble (String text) {
//		return Integer.parseInt(text);
		return Double.valueOf(text);
	}
	//把string类型转化为int
	public int changeStringToInt (String text) {
//		return Integer.parseInt(text);
		return Integer.valueOf(text);
	}
	public void output(String text) {//明显输出
		System.out.println(text);
	}
	public void output(double num) {//明显输出
		System.out.println(num);
	}
	public void output(int num) {//方法重载
		System.out.println("===="+num+"====");
	}	
		
	}
```
下面是在调试代码中如何使用多线程的方法：


```
jarName = "Demostudent";testClass="student.Student";testName="testLittle";
		PerformanceTest.getInstance().start();//启动线程
		new UiAutomatorHelper(jarName, testClass, testName);//调试用例
		PerformanceTest.key = false;//结束线程
```

另外一种调用方法：

```
	public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException, IOException, ParseException {
		jarName = "Demostudent";testClass="student.Student";testName="testTest";
		PerformanceTest performanceTest = new PerformanceTest();
		performanceTest.start();
		new UiAutomatorHelper(jarName, testClass, testName);
		performanceTest.key = false;
	}
```
下面是如何将数据保存到数据库中的方法：


```
public void saveMemAndCpuResult(int test_mark, double cpu_result, int mem_result) throws SQLException {
		if (mem_result == 0) {//过滤掉无用数据
			return;
		}
		getConnection();
		if (!connection.isClosed()) {
			outputSuccess();
			Statement statement = connection.createStatement();
			String sql = "INSERT INTO result (test_mark,cpu_result,mem_result) VALUES (" + test_mark +"," + cpu_result + "," + mem_result + ");";
//			output(sql);
			statement.executeUpdate(sql);
		}
	}
	public void saveMemAndCpuResult(int test_mark, int cpu_result, int vss, int rss) throws SQLException {
		if (vss * rss == 0) {//过滤掉无用数据
			return;
		}
		getConnection();
		if (!connection.isClosed()) {
			outputSuccess();
			Statement statement = connection.createStatement();
			String sql = "INSERT INTO result (test_mark,cpu_result,vss,rss) VALUES (" + test_mark +"," + cpu_result + "," + vss + "," + rss + ");";
//			output(sql);
			statement.executeUpdate(sql);
		}
	}
```

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>