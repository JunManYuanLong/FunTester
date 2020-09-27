# java使用poi写入excel文档的一种解决方案
本人在学习使用selenium和UiAutomator的时候，学习了一excel文档的操作，前两天写了一个读取excel的方案，今天看了一下写入excel的，暂时用的Map<Integer,List<String[]>>作为写入源。现在分享出来，希望能对你有所帮助。


```
//写入xlsx文档
    public static void writeXlsx(String filename, Map<Integer,List<String[]>> map) {
		String fileType = filename.substring(filename.lastIndexOf(".") + 1, filename.length());//提取文件名后缀
        try {
        	if (!fileType.equals("xlsx")) {//判断文件名是否正确
        		output("文件名错误!");
        		}
            XSSFWorkbook wb = new XSSFWorkbook();//新建工作区
            for(int sheetnum=0;sheetnum<map.size();sheetnum++){//遍历表格
                XSSFSheet sheet = wb.createSheet("第"+(sheetnum+1)+"个表格");
                List<String[]> list = map.get(sheetnum+1);//取出需要写入的表格内容，这里需要+1才行
                for(int i=0;i<list.size();i++){//遍历行
                    XSSFRow row = sheet.createRow(i);//新建行
                    String[] str = list.get(i);//取出需要写入的行信息
                    for(int j=0;j<str.length;j++){//遍历写入行单元格
                        XSSFCell cell = row.createCell(j);//创建单元格
                        cell.setCellValue(str[j]);//写入单元格数据
                    }
                }
            }
            FileOutputStream outputStream = new FileOutputStream(filename);//新建输出流
            wb.write(outputStream);//写入文件数据
            outputStream.close();//关闭输出流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```
这里依然借鉴了前人的思路，自己根据需求优化了一些代码，把方法重新写了一遍。

使用Excel主要为了生成测试报告用的，在实际始终当中，效果一般般，Excel的普通格式比较简单，比如设置颜色和文字格式等等，但是设计合并单元格和插入信息等格式就复杂多了，后期已经放弃。

[一起来~FunTester](https://gitee.com/fanapi/tester/blob/okay/readme.markdown)

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>