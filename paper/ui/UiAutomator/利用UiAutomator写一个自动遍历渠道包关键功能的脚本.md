# 利用UiAutomator写一个自动遍历渠道包关键功能的脚本
本人在做自动化测试的过程中，出现了一个需求。原因是，在发出去的渠道包里面，偶然一次有两个渠道包微博登录失败的bug，所以想着利用UiAutomator写了一个自动遍历每个渠道包的登录方式的脚本。经过尝试第一版终于完成，分享代码和思路，供大家参考。

思路：把所有渠道包放在一个apk的文件夹里面。写好代码打包成jar包，先push到手机中，然后再导出一个jar包，再这个包里用命令执行UiAutomator脚本。输出结果并保存日志在当前目录下。

下面是放在电脑上的jar包程序入口所在的类的代码：

```
package happyjuzi;
 
import java.io.File;
import source.Common;
 
public class Script extends Common {
	public static void main(String[] args) {
		Script script = new Script();
		script.testDemo();
	}
 
	public static Script getInstance() {
		return new Script();
	}
 
	public void testDemo() {
		String home = getWorkSpase();//获取当前路径
		output(home);
		File file = new File(home + "/apk");
		// File file = new File("/Users/dahaohaozai/Desktop" + "/apk");
		File[] file2 = file.listFiles();
		for (int i = 0; i < file2.length; i++) {
			File apk = file2[i];
			String path = apk.getAbsolutePath();
			for (int k = 0; k < 4; k++) {
				output(apk.getName(), i + 1);
				execCmd(ADB_PATH + "adb uninstall com.happyjuzi.apps.juzi");
				execCmd(ADB_PATH + "adb install " + path);
				execCmd(ADB_PATH + "adb shell uiautomator runtest demo.jar --nohup -c happyjuzi.AppTest#testTest" + k);
			}
		}
	}
}
```

下面是自己调试类的代码，暂且把需要运行的方法直接写在这里了，如果你也要写脚本，不建议这么做。

```
package happyjuzi;
 
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import com.android.uiautomator.core.UiObjectNotFoundException;
import android.os.RemoteException;
import source.UiAutomatorHelper;
 
@SuppressWarnings("deprecation")
public class AppTest extends AppCase {
	public static String jarName, testClass, testName, androidId;
 
	public static void main(String[] args)
			throws ClassNotFoundException, SQLException, InterruptedException, IOException, ParseException {
		jarName = "demo";
		testClass = "happyjuzi.AppTest";
		testName = "testTest0";
		// Script.getInstance().testDemo();
		// PerformanceThread.getInstance().start();//启动线程
		new UiAutomatorHelper(jarName, testClass, testName);// 调试用例
		// PerformanceThread.key = false;//结束线程
	}
 
	public void testTest0() throws InterruptedException, IOException, UiObjectNotFoundException, RemoteException {
		startJuziApp();
		skipGuideage();
		login(0);
		checkIsLogin();
	}
 
	public void testTest1() throws InterruptedException, IOException, UiObjectNotFoundException, RemoteException {
		startJuziApp();
		skipGuideage();
		login(1);
		checkIsLogin();
	}
 
	public void testTest2() throws InterruptedException, IOException, UiObjectNotFoundException, RemoteException {
		startJuziApp();
		skipGuideage();
		login(2);
		checkIsLogin();
	}
 
	public void testTest3() throws InterruptedException, IOException, UiObjectNotFoundException, RemoteException {
		startJuziApp();
		skipGuideage();
		login(3);
		checkIsLogin();
	}
}
```
下面是这三个封装方法的代码：


```
/**
	 * 跳过引导页
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws UiObjectNotFoundException
	 */
	public void skipGuideage() throws IOException, InterruptedException, UiObjectNotFoundException {
		startJuziApp();
		waitForUiObjectByResourceId("com.happyjuzi.apps.juzi:id/btn_skip");
		swipeLeft();
		swipeLeft();
		waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/btn_start");
		sleep(5000);
		if (getUiObjectByResourceId("com.happyjuzi.apps.juzi:id/close").exists()) {
			waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/close");
		}
	}
 
	/**
	 * 登录
	 * 
	 * @param key
	 *            选择登录方式
	 * @throws UiObjectNotFoundException
	 */
	public void login(int key) throws UiObjectNotFoundException {
		switch (key) {
		case 0:
			waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/btn_profile");
			waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/avatar_default_view");
			waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/wx_view");
			waitForUiObjectByResourceId("com.happyjuzi.apps.juzi:id/protrait_item_main");
			break;
		case 1:
			waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/btn_profile");
			waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/avatar_default_view");
			waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/qq_view");
			sleep(5000);
			if (getUiObjectByResourceId("com.tencent.mobileqq:id/name").exists()) {
				clickPiont(500, 1820);
			}
			waitForUiObjectByResourceId("com.happyjuzi.apps.juzi:id/protrait_item_main");
			break;
		case 2:
			waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/btn_profile");
			waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/avatar_default_view");
			waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/sina_view");
			sleep(5000);
			if (getUiObjectByResourceId("com.sina.weibo:id/bnLogin").exists()) {
				waitForResourceIdAndClick("com.sina.weibo:id/bnLogin");
			}
			waitForUiObjectByResourceId("com.happyjuzi.apps.juzi:id/protrait_item_main");
			break;
		case 3:
			waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/btn_profile");
			waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/avatar_default_view");
			waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/phone_view");
			writeTextByResourceId("com.happyjuzi.apps.juzi:id/phone_num_view", "******");
			waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/next_view");
			writeTextByResourceId("com.happyjuzi.apps.juzi:id/pwd_view", "*****");
			waitForResourceIdAndClick("com.happyjuzi.apps.juzi:id/next_view");
			waitForUiObjectByResourceId("com.happyjuzi.apps.juzi:id/protrait_item_main");
			break;
		default:
			break;
		}
	}
 
	/**
	 * 检查是否登录成功
	 */
	public void checkIsLogin() {
		if (getUiObjectByText("点击头像登录").exists()) {
			outpu("登录失败！");
		} else {
			output("登录成功！");
		}
	}
```
> 导出jar包的时候，如果是Mac运行jar包，一定要修改里面ADB_PATH地址，不然会报错。具体导出jar包文件的办法，博客里上一篇帖子就是。

### 技术类文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
8. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
9. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
10. [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
11. [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)
12. [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
13. [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)

### 非技术文章精选
1. [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
2. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
3. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
4. [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
5. [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
6. [测试之《代码不朽》脑图](https://mp.weixin.qq.com/s/2aGLK3knUiiSoex-kmi0GA)

# [点击查看公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ) 

 


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>