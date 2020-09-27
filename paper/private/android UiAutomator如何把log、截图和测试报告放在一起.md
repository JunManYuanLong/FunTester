# android UiAutomator如何把log、截图和测试报告放在一起
本人在使用android UiAutomator做测试的时候，在断言之前都会截图留证，方便以后查找。随着执行的次数增多，截图也越来越多，log文件也变成了上万行，如果找起来非常不方便。故而想了一个把每一次运行的相关信息放到一个文件夹中，这样方便查找截图和log。经过尝试终于成功了。分享代码如下，供大家参考。


```
package student;  
  
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;
 
public class ClassReport extends Library{
	//VERSION_KEY为false时，测试环境；为true时，正式环境
	public static final boolean version = false;
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, SQLException, ParseException {
    	String jarname = "Dream22";//设置生成jar包的名字
    	String logPath = "C:\\Users\\fankaiqiang\\workspace\\superclass-app\\reportlog.log";//运行log地址
    	String htmlPath = "C:\\Users\\fankaiqiang\\Desktop\\888\\UiAutomator.html";//测试报告
    	String backgroundPath = "C:\\Users\\fankaiqiang\\Desktop\\888\\222.jpg";//背景图地址
    	String time = Excel.getNow();//获取时间，也是测试报告文件夹的名称
    	String reportPath = creatNewFile(time);
    	/*删除已有的log文件和测试报告文件
    	 * 之所以放在开始因为java写入文件后，这些文件是被占用的无法删除
    	 */
    	File oldfile1 = new File(htmlPath);
        File oldfile2 = new File(logPath);
		oldfile1.delete();
		oldfile2.delete();
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
        MethodList.add("test014SubmitLectureExercise");
        MethodList.add("test015ConsultGaotuTeacher");
        MethodList.add("test016ChatroomByCourse");
        MethodList.add("test017PlayCourseVideo");
        MethodList.add("test018PlayLectureVideo");
        MethodList.add("test019SalePrice");
        MethodList.add("test020LectureLive");
        
        //准备必要数据
        if (version) {
        	output();
        	} else {
        		MySql.updateLiveTime();//更新直播公开课直播时间
        		MySql.DeleteUserLectureExercise();//删除作业提交记录
//        		MySql.UpdateLectureByUserid();//更新作业状态可提交
        		}
//        fail();//用语单独执行准备数据的方法
        List<String[]> sheet = new ArrayList<String[]>();//新建list，用于存放每个测试用例的测试结果
        if (key) {
        	String[] title = {"编号", "用例名", "运行状态", "错误信息", "错误行Library", "错误行Special", "错误行Case", "开始时间", "结束时间"};
        	sheet.add(title);//把标题行先加入表信息
             }
        new RunHelper(jarname, "1");//新编译jar包并push到手机上
        setMobileInputMethodToUtf();//设置手机输入法为UTF-7
        for(int i = 0;i < MethodList.size(); i++){//遍历运行所有方法 
        	String[] result = execCmdAndReturnResult(jarname, "student.Case", MethodList.get(i), i);//运行测试用例  
            sheet.add(result);//将此次用例的测试结果放入list中  
            }
        if (key) {
        	Map<Integer, List<String[]>> report = new HashMap<Integer, List<String[]>>();//新建map，用于存放多张表格数据
        	 report.put(1, sheet);//把第一个表格的测试数据放入要写入到map里  
        	 Excel.writeXlsx(report);//把测试报告写入excel表格中
        	 } else {
        		 WriteHtml.createWebReport(sheet);
        		 }
        copyFile(htmlPath, reportPath+"\\UiAutomator.html");//复制测试报告
        copyFile(logPath, reportPath+"\\reportlog.log");//复制运行log
        copyFile(backgroundPath, reportPath+"\\222.jpg");//复制背景图
        execCmd("adb -s 06dbd10c0ae4e3af pull /mnt/sdcard/123/ C:\\Users\\fankaiqiang\\Desktop\\888\\testreport"+time);//获取运行截图
        execCmdAndReturnResult(jarname, "student.Case", "lastCase", 100);//删除手机运行截图
        
    	
        
        
        setMobileInputMethodToQQ();//设置手机输入法为QQ输入法  
        }
    
}  
```

lastcase用例是用来清除手机上面的截图的代码如下：

```
public void deleteScreenShot() {//删除文件
		File file = new File("/mnt/sdcard/123/");
		if (file.exists()) {
			File[] files = file.listFiles();  
		    for (int i = 0; i < files.length; i++) {  
		    	files[i].delete();
		    }
		    file.delete();
		} else {
			System.out.println("文件夹不存在！");
		}
		
	}
```
中间有一个坑，尝试了很多次没有解决，本来打算删除log和测试报告放在结束，可是用delete()和renameto()，都不好用，而且不报错，经过查资料，觉得可能是文件被占用了，故在一开始就把原来的log文件和测试报告情况。避免产生混淆。效果如下，关于文件名还在优化。123是测试截图。222是背景图，UiAutomator是html的测试报告。因为使用html做的，所以excel的测试报告没有生成，所以这里看不到。

![](/blog/pic/20170718190636372.png)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>