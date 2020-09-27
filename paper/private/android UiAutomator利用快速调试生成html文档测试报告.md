# android UiAutomator利用快速调试生成html文档测试报告
本人在使用android UiAutomator的过程中，想到另外一种生成测试报告的方案，就是使用html文件生成测试报告，经过中午的学习html文档的知识，终于成功了，现在分享出来，供大家参考。

先发一下测试报告截图：
![](/blog/pic/20170621185025619.png)

下面是测试代码，跟上一篇excel文档的差不太多，只是用list替换了map来写入报告。


```
package teststudent;  
  
import java.io.IOException;
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;
 
public class ClassReport extends Library{
    public static void main(String[] args) throws IOException{
    	String jarname = "Demo";//设置生成jar包的名字
    	boolean key = false;//为true时生成excel测试报告，为false生成html测试报告
        //这里把所有的测试方法放到了一个list集合中，可以灵活控制需要运行的用例  
        List<String> MethodList = new ArrayList<String>();  
        MethodList.add("test001LearnCornerAddPost");  
        MethodList.add("test002RechargeByWechat");  
        MethodList.add("test003RechargeByAlipay");  
        MethodList.add("test004BuyCourseByWechat");  
        MethodList.add("test005AddAndDeleteAdress");  
        MethodList.add("test006Chatroom");  
        MethodList.add("test007ShareCourseToWechat");  
        MethodList.add("test008ShareCourseToFriendCircle");  
        MethodList.add("test009ShareCourseToQQ");  
        MethodList.add("test010ShareCourseToQzone");  
        MethodList.add("test011AlterPassword");  
        MethodList.add("test012AlterUserName");  
        MethodList.add("test013BuyCourseByAlipay");  
        List<String[]> sheet = new ArrayList<String[]>();//新建list，用于存放每个测试用例的测试结果
        if (key) {
        	String[] title = {"编号", "用例名", "运行状态", "错误信息", "错误行Library", "错误行Special", "错误行Case", "开始时间", "结束时间"};  
             sheet.add(title);//把标题行先加入表信息
             }
        new RunHelper(jarname, "1");//新编译jar包并push到手机上
        setMobileInputMethodToUtf();//设置手机输入法为UTF-7
        for(int i = 0;i < MethodList.size(); i++){//遍历运行所有方法  
        	String[] result = execCmdAndReturnResult(jarname, "teststudent.Case", MethodList.get(i), i);//运行测试用例  
            sheet.add(result);//将此次用例的测试结果放入list中  
            }
        if (key) {
        	Map<Integer, List<String[]>> report = new HashMap<Integer, List<String[]>>();//新建map，用于存放多张表格数据
        	 report.put(1, sheet);//把第一个表格的测试数据放入要写入到map里  
        	 Excel.writeXlsx(report);//把测试报告写入excel表格中
        	 } else {
        		 WriteHtml.createWebReport(sheet);
        		 }
        setMobileInputMethodToQQ();//设置手机输入法为QQ输入法  
        }
}  
```


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>