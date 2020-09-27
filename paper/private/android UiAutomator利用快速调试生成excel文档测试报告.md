# android UiAutomator利用快速调试生成excel文档测试报告
本人在使用UiAutomator做测试的时候，更偏向于使用快速调试类来进行，方便好用，但是生成报告一直是个心病，昨天想到一个方案，今天终于成功，通过快速调试类，先进行编译和push，然后通过执行cmd命令逐个执行测试用。对控制台输出的信息进行处理，分类存储，执行完用例，一起写在一个excel文档。这个办法还有一个优点，就是可以随意运行任意两个或者更多的测试用例，相比UiAutomatorHelper只能运行单个用例，也算是一点点提高。

这里还可以利用list集合储存的信息将错误的用例再运行一次，另存一个sheet里面，变成第二次执行错误用例的测试报告。由于时间紧凑，我就没写这块的代码，哪位写好了，可以发我一下。

先发一下生成用例的效果图：

![](/blog/pic/20170617165630378.png)

看起来还不错，下面分享一下自己的代码。


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
* runhelper类只是在UiAutomatorhelper基础上注释掉运行用例的方法即可。



> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>