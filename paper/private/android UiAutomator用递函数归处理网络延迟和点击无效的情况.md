# android UiAutomator用递函数归处理网络延迟和点击无效的情况

本人在使用android UiAutomator1做测试的时候，遇到了H5页面由于网络延迟过大，在操作等待结束后依然没有显示控件，但是UiAutomator点击操作却已经执行了，导致页面无法跳转，以至于用例失败。有些时候是因为接口响应时间太长导致点击了一个控件页面并没有如期出现相应的变化导致用例失败。自己在封装方法中添加了几行while循环，效果很不错，不过在了解到递归函数之后，决定用递归函数处理这类问题，原因是：递归处理起来太简单了。分享代码，供大家参考。


```
	//点击咨询
	public void clickConsult() throws UiObjectNotFoundException {
		waitForTextAndClick("咨询");
		if (getUiObjectByText("咨询").exists()) {
			clickConsult();//递归
		}
	}
```

```
	//点击班级群
	public void clickCourseGroup() throws UiObjectNotFoundException {
		waitForTextAndClick("班级群");
		if (getUiObjectByText("班级群").exists()) {
			clickCourseGroup();//递归
		}
	}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>