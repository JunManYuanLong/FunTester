# android UiAutomator如何把运行结果写到html文档里

此文写作时间较早，为UiAutomator1实践，现已放弃UiAutomator1，各位看个思路即可。

昨天研究了一下如何生成html文件的测试报告，但没有发出来写html文件的代码，经过整理之后，觉得差不多了。发出来供大家参考。


```
	public static void createWebReport(List<String[]> runresult) throws IOException {
		//这个是页面前面固定的信息
		String starttext = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>"
				+"</head><body><h1 style='text-align:center'>android UiAutomator测试报告</h1><p style='text-align:center'>"+getNow()+"测试报告"+"</p>"
						+"<table  border='1' style='table-layout:fixed;font-size:14px;'>"
				+"<thead>"
						+"<tr><td width='30px'>编号</td>"
				+"<td width='120px'>用例名</td>"
						+"<td width='70px'>运行状态</td>"
				+"<td width='250px'>错误信息</td>"
						+"<td width='160px'>错误行Library</td>"
				+"<td width='160px'>错误行Special</td>"
						+"<td width='160px'>错误行Case</td><td width='100px'>开始时间</td>"
				+"<td width='100px'>结束时间</td><td width='100px'>备用列</td></tr>"
						+"</thead><tbody>";
		//这里是页面后面固定的信息
		String endtext = "</tbody></table></body></html>";
		File file = new File("C:\\Users\\fankaiqiang\\Desktop\\888\\"+getNow()+".html");//新建一个html文档
		if (!file.exists()) {//判断是否存在，不存在先创建
			file.createNewFile();
			}
		//将运行信息输出到html文档中
		boolean isClose = false;//写入时用到
		BufferedWriter bf;
		FileOutputStream outputStream = new FileOutputStream(file, true);
		OutputStreamWriter outWriter = new OutputStreamWriter(outputStream);
		String sheet = "";
		for (int i = 0; i < runresult.size(); i++) {
			sheet = sheet + "<tr>";//添加行
			String[] result = runresult.get(i);//获取用例运行信息
			for (int j = 0; j < result.length; j++) {
				if (result[2] == "运行成功") {//运行成功，就用绿色
					String addtext = "<td style='word-wrap:break-word;word-break:break-all;background-color:paleturquoise'>" + result[j]+ "</td>";
					sheet = sheet + addtext;
					} else if (result[2] == "断言错误") {//断言失败，就用黄色
						String addtext = "<td style='word-wrap:break-word;word-break:break-all;background-color:yellow;'>" + result[j]+ "</td>";
						sheet = sheet + addtext;
						} else if (result[2] == "运行错误") {//运行失败就用红色
							String addtext = "<td style='word-wrap:break-word;word-break:break-all;background-color:red;'>" + result[j]+ "</td>";
							sheet = sheet + addtext;
							} else {
								output("运行信息错误！");
								}
				}
			sheet = sheet + "</tr>";//结束行
			}
		bf = new BufferedWriter(outWriter);//写入
		bf.append(starttext);//写入页面前面的信息
		bf.append(sheet);//写入报告信息
		bf.append(endtext);//写入页面后面信息
		bf.flush();
		if (isClose) {
			bf.close();
			}
		}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>