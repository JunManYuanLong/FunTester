# 利用UiAutomator和httpclient写自动发表笑话到微信朋友圈功能
本人在学习了httpclient之后，一直在想如何把获取到的信息发送到微信朋友圈，今天经过不断尝试，终于完成了这个功能。自动发笑话到朋友圈。中间使用到了图灵机器人的api和alertover的推送api，之前都已经发过了，这里就先不写了。分享代码，供大家参考。

下面这个是主要的代码：

```
public class Wechat extends WechatUse{
	public static String title = "abc";//设置标题
	public static void main(String[] args) throws ClientProtocolException, JSONException, IOException {
		ApiRobot apiRobot = new ApiRobot();//创建并实例化类，用于获取内容
		String info = apiRobot.getReplyFromRobot("讲个笑话");//获取具体笑话的内容
		System.out.println(info);//输出笑话
		PushMessage message = new PushMessage(title, info);//创建并实例化信息
		message.send(message.getNexusReceiverId());//将信息发送到指定设备
		setMobileInputMethodToUtf();//设置输入法utf-7
		new UiAutomatorHelper("Demo", "wechat.Wechat", "testLv", "1", NEXUS5DEVICESID);//运行用例
		setMobileInputMethodToQQ();//设置输入法QQ
	}
	public void testLv() throws UiObjectNotFoundException, InterruptedException, IOException, RemoteException {
		String info = test003getInfoFromAlertover(title);//获取推送到手机上的笑话
		test001PostTextInFriendCircle(info);//将笑话发到朋友圈
	}
}
```
下面是两个用例的代码：

```
	public String test003getInfoFromAlertover(String title) throws UiObjectNotFoundException, IOException, InterruptedException {
		stopAlertover();
		startAlertover();
		swipeUp();
		waitForTextAndClick(title);
		String info = getTextByResourceId("com.alertover.app:id/tv_content");
		stopAlertover();
		return info;
	}
```

```
	public void test001PostTextInFriendCircle(String text) throws InterruptedException, IOException, UiObjectNotFoundException {
		stopWechat();
		startWechat();
		waitForTextAndClick("发现");
		waitForTextAndClick("朋友圈");
		if (getUiObjectByText("朋友圈").exists()) {
			waitForTextAndClick("朋友圈");
			}
		longclickUiObectByDesc("更多功能按钮");
		writeText(text);
		waitForTextAndClick("发送");
		stopWechat();
	}
```
* 基本功能先这样，后期在实现复杂的功能，关于交互这点，我放弃使用了参数化，理论上来说参数化也能实现。

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>