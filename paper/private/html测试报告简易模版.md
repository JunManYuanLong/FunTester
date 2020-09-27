# html测试报告简易模版
本人在做测试的过程中，经常性需要把测试结果输出成html文件，然后方便其他人在线查看。因此我把之前的生成html文档的类进行的简化，能够兼容其他不同测试类型的测试报告。分享代码，供大家参考。

其中在设置宽度地方大家可以删除掉宽度的设置代码，让浏览器自适应，有一个缺点：如果里面的内容过长可能导致表格的美观程度大大降低。


```
package source;
 
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
 
public class WriteHtml extends SourceCode {
	private static WriteHtml writeHtml = new WriteHtml();
 
	public static WriteHtml getInstance() {
		return writeHtml;
	}
 
	private WriteHtml() {
		output("欢迎使用html文档生成功能！");
	}
 
	public void createWebReport(List<String[]> runresult, String path) {
		// 这个是页面前面固定的信息
		String starttext = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>" + "</head>"
				+ "<h1 style='text-align:center'>juziyule api testreport</h1><p style='text-align:center'>"
				+ "report_time:" + getNow() + "</p>"
				+ "<table align=\"center\"  border='1' style='table-layout:fixed;font-size:14px;'>" + "<thead>"
				+ "<tr><td width='30px'>num</td>" + "<td width='120px'>api_name</td>" + "<td width='100px'>total</td>"
				+ "<td width='100px'>fail_num</td>" + "<td width='250px'>fail_case</td>"
				+ "<td width='250px'>fail_result</td>"
				// +"<td width='160px'>备用列</td>"
				+ "</tr></thead><tbody>";
		// 这里是页面后面固定的信息
		String endtext = "</tbody></table></body></html>";
		File file = new File(path);// 新建一个html文档
		if (file.exists()) {// 判断是否存在，不存在先创建
			file.delete();// 先删除，以免混淆
			try {
				file.createNewFile();
			} catch (IOException e) {
				output("创建文件失败！", e);
			}
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				output("创建文件失败！", e);
			}
		}
		// 将运行信息输出到html文档中
		boolean isClose = false;// 写入时用到
		BufferedWriter bf;
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file, true);
		} catch (FileNotFoundException e) {
			output("文件不存在！", e);
		}
		OutputStreamWriter outWriter = new OutputStreamWriter(outputStream);
		StringBuffer sheet = new StringBuffer();
		for (int i = 0; i < runresult.size(); i++) {
			sheet.append("<tr>");
			String[] result = runresult.get(i);// 获取用例运行信息
			for (int j = 0; j < result.length; j++) {
				String addtext = "<td style='word-wrap:break-word;word-break:break-all;background-color:paleturquoise'>"
						+ result[j] + "</td>";
				sheet.append(addtext);
			}
			sheet.append("</tr>");
		}
		bf = new BufferedWriter(outWriter);// 写入
		try {
			bf.append(starttext);
			// 写入页面前面的信息
			bf.append(sheet);// 写入报告信息
			bf.append(endtext);// 写入页面后面信息
			bf.flush();
			if (isClose) {
				bf.close();
				outWriter.close();
				outputStream.close();
			}
		} catch (IOException e) {
			output("生成html文件失败！", e);
		}
	}
 
	// 获取当前时间
	public String getNow() {
		Date time = new Date();
		SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String c = now.format(time);
		return c;
	}
 
	// 明显输出
	public void output(String text) {
		System.out.println(text);
	}
 
	public void output(int num) {
		System.out.println(num);
	}
}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>