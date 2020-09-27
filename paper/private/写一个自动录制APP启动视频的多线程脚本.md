# 写一个自动录制APP启动视频的多线程脚本
本人在做APP稳定性测试的过程中，需要统计一下APP启动时间和启动时广告的样式，以免复审一下广告。所以在执行启动时间另起了一个多线程，分享代码，供大家参考。


```
package monkeytest;
 
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
 
import source.Common;
 
public class ScreenRecord extends Thread {
	@Override
	public void run() {
		Common.getInstance().sleep(300);
		execCmdAdb("adb shell screenrecord --time-limit 10 --size 960*640 --bit-rate 2000000 /sdcard/123/" + getNow()
				+ ".mp4");
	}
 
	private static void execCmdAdb(String cmd) {
		System.out.println(cmd);
		String OSname = System.getProperty("os.name");
		try {
			if (OSname.contains("Mac")) {
				Runtime.getRuntime().exec(Common.ADB_PATH + cmd);
			} else {
				Runtime.getRuntime().exec("cmd /c " + cmd);
			}
		} catch (IOException e) {
			Common.getInstance().output("执行" + cmd + "失败！");
			e.printStackTrace();
		}
	}
 
	/**
	 * 获取当前时间
	 * 
	 * @return 返回当前时间，只有日期和小时和分数没有年份和秒数
	 */
	private static String getNow() {
		Date time = new Date();
		SimpleDateFormat now = new SimpleDateFormat("MM-dd-HH-mm");
		String c = now.format(time);
		return c;
	}
}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>