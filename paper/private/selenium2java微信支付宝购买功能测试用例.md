# selenium2java微信支付宝购买功能测试用例
本人在学习使用selenium2java的过程中，遇到了测试支付宝和微信购买课程的用例，写起来略微复杂了一些，因为考虑到重复测试，得先修改用户订单状态，还得考虑用户已经买过、下单未付款、第一次购买等情况，中间用到了if-else if -else的判断语法，还得切换窗口。下面分享出来，供大家参考。



```
//购买班课
	public static void BuyCourseWithoutPay(WebDriver driver) throws InterruptedException, ClassNotFoundException, SQLException {
		MySql.alterUserOrdersStatus();//修改用户订单状态
		clickCourseMore(driver);
		selectGradeAndSubject(driver, "初一", "数学");
		findElementByXpathAndClick(driver, "html/body/div[2]/div[3]/div[1]/ul/li[1]");//选择课程
		clickApply(driver);
		/*如果已经购买过尚未支付，会跳转到订单页面，点击支付，选择支付宝和微信，验证跳转页面title和弹框
		 * 注意切换窗口，支付宝会出现先窗口
		 * 如果没有购买过或者已经过期订单，会跳转到提交订单页面
		 */
		if (exists(driver, By.id("commitOrder"))) {//如果发现立即支付按钮，则去支付页面
			clickCommitOrder(driver);//点击订单页面立即支付
			findElementByClassNameAndClick(driver, "alipay");//选择支付宝支付
			String homehandle = driver.getWindowHandle();//获取页面handle
			findElementByIdAndClick(driver, "payment_btn");//点击立即支付
			Set<String> handles = driver.getWindowHandles();//获取当前handles
			for(String handle : handles){
				if (handle.equals(homehandle) == false) {
					driver.switchTo().window(handle);//切换到支付宝页面
					assertEquals("跳转支付宝失败！", "支付宝 - 网上支付 安全快速！", driver.getTitle());
					driver.close();//关闭支付宝窗口
					}
				}
			for(String handle : handles){
				if (handle.equals(homehandle)) {
					driver.switchTo().window(handle);//切换到原来的窗口
					findElementByClassNameAndClick(driver, "layui-layer-setwin");//关闭支付支付弹框
					sleep(0);
					findElementByClassNameAndClick(driver, "wxpay");//点击微信支付
					findElementByIdAndClick(driver, "payment_btn");//点击立即支付
					assertEquals("微信支付弹框二维码失败！", "微信扫描二维码以完成支付", getTextByXpath(driver, ".//*[@id='LAY_layuipro1']/div/p"));
					}
				}
			}else if (exists(driver, By.className("address"))) {//如果发现有收货地址，先删除
				moveToElementByClassName(driver, "address");//鼠标移动到收货地址上
				clickDeleteAdress(driver);
				sleep(0);
				clickSure(driver);
				sleep(0);
				}else {//添加收货地址
					AddAddress(driver);
					sleep(0);
					clickCommitOrder(driver);
					findElementByClassNameAndClick(driver, "alipay");//选择支付宝支付
					String homehandle = driver.getWindowHandle();
					findElementByIdAndClick(driver, "payment_btn");//点击立即支付						
					Set<String> handles = driver.getWindowHandles();
					for(String handle : handles){
						if (handle.equals(homehandle) == false) {
							driver.switchTo().window(handle);//切换到支付宝页面
							assertEquals("跳转支付宝失败！", "支付宝 - 网上支付 安全快速！", driver.getTitle());
							driver.close();
							}
						if (handle.equals(handle)) {
							driver.switchTo().window(handle);
							findElementByClassNameAndClick(driver, "layui-layer-setwin");//关闭支付支付弹框
							findElementByClassNameAndClick(driver, "wxpay");//点击微信支付
							findElementByIdAndClick(driver, "payment_btn");//点击立即支付
							assertEquals("微信支付弹框二维码失败！", "微信扫描二维码以完成支付", getTextByXpath(driver, ".//*[@id='LAY_layuipro1']/div/p"));
							}
						}
					}
	}
```
其中一些自定义方法如下：

```
	//选择年级和科目
	public static void selectGradeAndSubject(WebDriver driver, String grade, String subject) {
		findElementByTextAndClick(driver, grade);
		findElementByTextAndClick(driver, subject);
	}
```

```
	//修改用户购买订单
	public static void alterUserOrdersStatus() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		Connection connection = DriverManager.getConnection(url, user, password);
		if (!connection.isClosed()) {
			Statement statement = connection.createStatement();
			String sql = "UPDATE orders set order_status = 5 WHERE user_id = "+ user_id + " and order_status = 2";
			statement.executeUpdate(sql);
			output("修改用户购买订单成功！");
			}
		connection.close();
		}
```

```
	//鼠标悬停
	public static void moveToElementById(WebDriver driver, String id) {
		Actions actions = new Actions(driver);
		actions.moveToElement(findElementByid(driver, id));
	}
	public static void moveToElementByClassName(WebDriver driver, String name) {
		Actions actions = new Actions(driver);
		actions.moveToElement(findElementByClassName(driver, name));
	}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>