# android UiAutomator写一个自动化工具循环造数据的例子
本人在学习android UiAutomator的时候，大神提到自动化可以用来造数据，根据作业功能写了一个自动提交作业的例子，顺便练习了swith-case的使用，以及自己自定义方法的运用，特别是最后两个waitForTextAndClick()，等待元素出现并点击，如果用Configurator.getInstance().setActionAcknowledgmentTimeout(50);把操作等待时间降低，就能大大提升执行的速度，现在把分享出来，如有错误还请指正。


```
for(int i=0;i<110;i++){
// Configurator.getInstance().setActionAcknowledgmentTimeout(50);
outputNotable("第"+(i+1)+"次开始！");
//拍照
getUiObjectByResourceId("com.dianzhi.student:id/iv").clickAndWaitForNewWindow();
getUiObjectByResourceId("com.dianzhi.student:id/openCamera_dialog").clickAndWaitForNewWindow();
getUiObjectByResourceId("com.oppo.camera:id/shutter_button").clickAndWaitForNewWindow();
getUiObjectByResourceId("com.oppo.camera:id/btn_done").clickAndWaitForNewWindow();
sleep(1000);
int x = UiDevice.getInstance().getDisplayWidth();
int y = UiDevice.getInstance().getDisplayHeight();
clickPiont(x-180, y-90);
waitForUiObject("提交");
//选择年级和学科
int ss = new Random().nextInt(3);
switch (ss) {
case 0:
getUiObjectByText("高中").clickAndWaitForNewWindow();
break;
case 1:
getUiObjectByText("初中").clickAndWaitForNewWindow();
break;
case 2:
getUiObjectByText("小学").clickAndWaitForNewWindow();
break;
default:
break;
}
int sss = new Random().nextInt(3);
switch (sss) {
case 0:
getUiObjectByText("语文").clickAndWaitForNewWindow();
break;
case 1:
getUiObjectByText("数学").clickAndWaitForNewWindow();
break;
case 2:
getUiObjectByText("英语").clickAndWaitForNewWindow();
break;
default:
break;
}
getUiObjectByResourceId("com.dianzhi.student:id/et").setText(Utf7ImeHelper.e("我是测试！"));
getUiObjectByText("提交").clickAndWaitForNewWindow();
waitForTextAndClick("知道了");
UiDevice.getInstance().pressBack();
waitForTextAndClick("作业辅导");
 
}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>