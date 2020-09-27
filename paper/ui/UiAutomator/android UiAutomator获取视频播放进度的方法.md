# android UiAutomator获取视频播放进度的方法
本人在使用android UiAutomator做测试的时候，有时候需要统计视频播进度，然后去断言上传的进度数据正确与否。具体的思路就是根据进度条的颜色区分，我选的红色，然后去计算各个点的数值，然后计算进度的百分比。

这是app的界面进度条的截图

![](/blog/pic/20170907125142081.png)

下面是我两次获取到的数据。
![](/blog/pic/20170907125858613.png)

下面是我的代码：


```
package student;
 
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import com.android.uiautomator.core.UiObjectNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.RemoteException;
import source.UiAutomatorHelper;
/**
* @author ··-·尘
* @E-mail:Fhaohaizi@163.com
* @version 创建时间：2017年8月18日 上午10:53:24
* @alter 修改时间：2017年9月7日 12:55:36
* 类说明：测试调试用例
*/
@SuppressWarnings("deprecation")
public class Student extends StudentCase{
	public static String jarName,testClass,testName,androidId;
	public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException, IOException, ParseException {
		jarName = "DemoStudent";testClass="student.Student";testName="testTest";
//		PerformanceThread.getInstance().start();//启动线程
		new UiAutomatorHelper(jarName, testClass, testName);//调试用例
//		PerformanceThread.key = false;//结束线程
	}
	public void testTest() throws InterruptedException, IOException, UiObjectNotFoundException, RemoteException {
		clickPiont(500, 500);//点击屏幕，显示播放进度条
		sleep(100);//休眠等待截图
		Bitmap bitmap = getBitmapByResourceId("com.genshuixue.student:id/view_video_coursede_control_seekbar");//获取播放空间的bitmap实例
		double percent = getVideoProgress(bitmap);
	}
}
```
下面是获取bitmap的方法

```
	//截取某个控件的图像
	public Bitmap getBitmapByResourceId(String id) throws UiObjectNotFoundException {
		Rect rect = getUiObjectByResourceId(id).getVisibleBounds();//获取控件的rect对象
		String path = screenShot("test");//截图
		Bitmap bitmap = BitmapFactory.decodeFile(path);//创建并实例化bitmap对象
		bitmap = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height());//截取bitmap实例
		return bitmap;
	}
```
下面是获取进度的方法：

```
	//获取视频播放进度条
	public double getVideoProgress(Bitmap bitmap) {
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		List<Integer> date = new ArrayList<Integer>();
		for (int i = 0;i < width; i++) {
			int color = bitmap.getPixel(i, height / 2);
			int red = Color.red(color);
//			output(red);
			date.add(red);
			}
		int date1 = 0,date2 = 0,date3 = 0,date4 = 0;
		int status1 = 0,status2 = 0;
		for (int i = 1;i < date.size() - 1;i++) {
			
			if (date.get(i) == 255 && status1 == 0) {
				status1++;
				date1 = i;
			}
			if (date.get(i) == 238 && status2 == 0) {
				status2++;
				date2 = i;
			}
			if (date.get(i + 1) < 238 && date.get(i) == 238) {
				date3 = i;
			}
			if (date.get(i + 1) < 165 && date.get(i) == 165) {
				date4 = i;
			}
		}
//		output(date1, date2, date3, date4);
//		output((date2 + date3 - date1 * 2.00)/(date4 - date1)/2);
		return (date2 + date3 - date1 * 2.00)/(date4 - date1)/2;
	}
```


### 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
- [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)



### 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
- [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
- [测试之《代码不朽》脑图](https://mp.weixin.qq.com/s/2aGLK3knUiiSoex-kmi0GA)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [优秀软件开发人员的态度](https://mp.weixin.qq.com/s/0uEEeFaR27aTlyp-sm61bA)
- [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [自动化测试解决了什么问题](https://mp.weixin.qq.com/s/96k2I_OBHayliYGs2xo6OA)
- [17种软件测试人员常用的高效技能-上](https://mp.weixin.qq.com/s/vrM_LxQMgTSdJxaPnD_CqQ)
- [17种软件测试人员常用的高效技能-下](https://mp.weixin.qq.com/s/uyWdVm74TYKb62eIRKL7nQ)

# [点击查看公众号地图](https://mp.weixin.qq.com/s/l_zkWzQL65OIQOjKIvdG-Q)