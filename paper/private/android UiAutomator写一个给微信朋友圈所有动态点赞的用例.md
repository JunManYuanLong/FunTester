# android UiAutomator写一个给微信朋友圈所有动态点赞的用例
本人在是呀UiAutomator的过程中，突发奇想，写一个自动给朋友圈点赞的用例，经过尝试，终于成功，效果不错。这个方法用的是for循环，也可以用while循环，加一条判断如果本页全部点赞就跳出来这样的判断即可。分享代码，供大家参考。


```
	public void test003LikeAllStatusInFriendCircle () throws InterruptedException, IOException, UiObjectNotFoundException {
		stopWechat();//关闭微信
		startWechat();//启动微信
		enterFriendCircle();//进入朋友圈
		for(int k = 0;k < 5;k++){//循环5次
			UiObject one = getUiScrollabe();//获取朋友圈动态的可滚动控件
			int num = one.getChildCount();//获取当前页面朋友圈动态条数
			for(int i =0;i < num-1;i++){//循环，此处-1因为点赞可能会导致最后一跳动态在前几条点赞后不在当前页面，后面scrollforward方法已经处理
				UiObject two = one.getChild(new UiSelector().className("android.widget.FrameLayout").index(i));//获取朋友圈动态控件
				UiObject three = two.getChild(new UiSelector().resourceId("com.tencent.mm:id/cw7"));//获取已经点赞的空间
				if (three.exists() && three.getText().trim().contains("尘")) {//如果存在且包含当前帐号就跳过
					continue;
					}
				if (!two.getChild(new UiSelector().description("评论")).exists()) {//如果评论按钮不存在，跳过
					continue;
					}
				two.getChild(new UiSelector().description("评论")).click();//点击评论按钮
				if (getUiObjectByText("取消").exists()) {//如果是取消按钮，则跳过，防止在滚动和循环的时候出错
					swipeLeft();//滑动是弹框消失
					continue;
					}
				waitForTextAndClick("赞");//点击赞
				}
			getUiScrollabe().scrollForward(50);//50步长向下滚动屏幕
			}
		stopWechat();//关闭微信
	}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>