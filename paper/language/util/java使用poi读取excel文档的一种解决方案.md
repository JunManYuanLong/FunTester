# java使用poi读取excel文档的一种解决方案
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

本人在学习使用java的过程中，需要验证一下excel表格里面的数据是否与数据库中的数据相等。由于数据太多，故想着用java读取excel数据再去数据库验证。上网看了一下资料自己写了一个读取excel文档的方法，验证数据库的方法暂时还没写，自娱自乐，只能抽时间了。现在把读取excel的方法分享出来。



```
	//读取excel文档，除第一行为标题外内容为数字
	public static List<List<Map<String, String>>> readExcel(File filepath) throws Exception{
		/*首先判断文件是否存在
		 * 在判断文件类型，xls还是xlsx
		 */
		if (!filepath.exists()) {
			output("文件不存在！");
			}
		String  filename = filepath.toString();//转化为string类型
		String fileType = filename.substring(filename.lastIndexOf(".") + 1, filename.length());//提取文件名后缀
		InputStream is = null;
		Workbook wb = null;
	    try {
	    	is = new FileInputStream(filepath);
	        if (fileType.equals("xls")) {
	        	wb = new HSSFWorkbook(is);
	        	} else if (fileType.equals("xlsx")) {
	        		wb = new XSSFWorkbook(is);
	        		} else {
	        			output("文件名错误!");
	        			}
	        //新建集合，考虑到要用value值去查询数据库，所以value设置为string类型
	        List<List<Map<String, String>>> result = new ArrayList<List<Map<String,String>>>();
	        int sheetSize = wb.getNumberOfSheets();//获取表格的个数
	        for (int i = 0; i < sheetSize; i++) {//遍历所有表格
	        	Sheet sheet = wb.getSheetAt(i);
	        	List<Map<String, String>> sheetList = new ArrayList<Map<String, String>>();
	        	List<String> titles = new ArrayList<String>();//放置所有的标题
	            int rowSize = sheet.getLastRowNum() + 1;//此处getLastRowNum()方法获取的行数从0开始，故要+1
	            for (int j = 0; j < rowSize; j++) {//遍历所有行
	            	Row row = sheet.getRow(j);
	            	if (row == null) {//略过空行
	            		continue;
	            		}
	                int cellSize = row.getLastCellNum();//获取列数
	                if (j == 0) {//第一行是标题行
	                	for (int k = 0; k < cellSize; k++) {//添加到标题集合中
	                		Cell cell = row.getCell(k);
	                		titles.add(cell.toString());
	                		}
	                	} else {//其他行是数据行，为数字
	                		Map<String, String> rowMap = new HashMap<String, String>();//保存一行的数据
	                		for (int k = 0; k < titles.size(); k++) {//遍历保存此行数据
	                			Cell cell = row.getCell(k);
	                			String key = titles.get(k);
	                			String value = null;
	                			if (cell != null) {
	                				/*这里因为读取excel数据默认值是double类型的，但我的数据都是整数，为了方便先进行一次转换
	                				 * 先判断数据类型，然后先转换然后在复制给value
	                				 * 数值类型是0，字符串类型是1，公式型是2，空值是3，布尔值4，错误5
	                				 */
	                				if (row.getCell(k).getCellType() == 0) {
	                					value =(int) row.getCell(k).getNumericCellValue()+"";
	                					}else {
	                						value = cell.toString();//转换成string赋值给value
	                						}
	                				}
	                			rowMap.put(key, value);//把数据存入map集合
	                			}
	                		sheetList.add(rowMap);//把存好行的数据存入表格的集合中
	                		}
	                }
	            result.add(sheetList);//把表格的数据存到excel的集合中
	            }
	        return result;
	        } catch (FileNotFoundException e) {
	        	throw e;
	        	} finally {
	        		if (is != null) {
	        			is.close();
	        			}
	        		}
	    }
```
方法的思路是从网上看来的，中间把代码敲了一遍，发现很多地方不太对，不知道是不是因为年份久远的原因。这个方法我做了一些自己的优化，输入参数那个地方我改成了file类型，判断了一下文件是否存在。在读取行数据的时候先转换了一下格式。中文的注释，我也自己重新写了一遍。希望能对你有所帮助。

[一起来~FunTester](https://gitee.com/fanapi/tester/blob/okay/readme.markdown)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>