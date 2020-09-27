# httpclient如何处理302重定向

在使用httpclient做接口测试的时候，遇到了一个重定向的接口，由于框架原因导致的必需得重定向到另外一个域名的接口完成功能。在之前未遇到这个的情况，经过修改请求方法解决了这个问题。大致思路是：如果发现是HTTP code是302，就会去header数组里面找location的字段，把字段的结果放到响应体里面，我的响应体是json格式的。

* 中间还需要修改一部分的httpclient连接池和requestconfig的配置。

代码如下：

```
    /**
     * 获取响应实体
     * <p>会自动设置cookie，但是需要各个项目再自行实现cookie管理</p>
     * <p>该方法只会处理文本信息，对于文件处理可以调用两个过期的方法解决</p>
     *
     * @param request 请求对象
     * @return 返回json类型的对象
     */
    public static JSONObject getHttpResponse(HttpRequestBase request) {
        if (!isRightRequest(request)) return new JSONObject();
        beforeRequest(request);
        JSONObject res = new JSONObject();
        RequestInfo requestInfo = new RequestInfo(request);
        if (HEADER_KEY) output("===========request header===========", Arrays.asList(request.getAllHeaders()));
        long start = Time.getTimeStamp();
        try (CloseableHttpResponse response = ClientManage.httpsClient.execute(request)) {
            long end = Time.getTimeStamp();
            long elapsed_time = end - start;
            if (HEADER_KEY) output("===========response header===========", Arrays.asList(response.getAllHeaders()));
            int status = getStatus(response, res);
            JSONObject setCookies = afterResponse(response);
            String content = getContent(response);
            int data_size = content.length();
            res.putAll(getJsonResponse(content, setCookies));
            int code = iBase == null ? -2 : iBase.checkCode(res, requestInfo);
//            if (!iBase.isRight(res))
//                new AlertOver("响应状态码错误：" + status, "状态码错误：" + status, requestInfo.getUrl(), requestInfo).sendSystemMessage();
            MySqlTest.saveApiTestDate(requestInfo, data_size, elapsed_time, status, getMark(), code, LOCAL_IP, COMPUTER_USER_NAME);
        } catch (Exception e) {
            logger.warn("获取请求相应失败！", e);
            if (!SysInit.isBlack(requestInfo.getHost()))
                new AlertOver("接口请求失败", requestInfo.toString(), requestInfo.getUrl(), requestInfo).sendSystemMessage();
        } finally {
            HEADER_KEY = false;
            if (!SysInit.isBlack(requestInfo.getHost())) {
                if (requests.size() > 9) requests.removeFirst();
                boolean add = requests.add(request);
            }
        }
        return res;
    }
    
    
        /**
     * 获取响应状态，处理重定向的url
     *
     * @param response
     * @param res
     * @return
     */
    public static int getStatus(CloseableHttpResponse response, JSONObject res) {
        int status = response.getStatusLine().getStatusCode();
        if (status != HttpStatus.SC_OK) logger.warn("响应状态码错误：{}", status);
        if (status == HttpStatus.SC_MOVED_TEMPORARILY)
            res.put("location", response.getFirstHeader("Location").getValue());
        return status;
    }
``` 

下面是配置：


```
    /**
     * 获取请求超时控制器
     * <p>
     * cookieSpec:即cookie策略。参数为cookiespecs的一些字段。作用：
     * 1、如果网站header中有set-cookie字段时，采用默认方式可能会被cookie reject，无法写入cookie。将此属性设置成CookieSpecs.STANDARD_STRICT可避免此情况。
     * 2、如果要想忽略cookie访问，则将此属性设置成CookieSpecs.IGNORE_COOKIES。
     * </p>
     *
     * @return
     */
    private static RequestConfig getRequestConfig() {
        return RequestConfig.custom().setConnectionRequestTimeout(HttpClientConstant.CONNECT_REQUEST_TIMEOUT).setConnectTimeout(HttpClientConstant.CONNECT_TIMEOUT).setSocketTimeout(HttpClientConstant.SOCKET_TIMEOUT).setCookieSpec(CookieSpecs.IGNORE_COOKIES).setRedirectsEnabled(false).build();
    }
```

> requestconfig既可以在创建httpclient连接池的时候设置，也可以在对HTTPrequestbase进行设置，这里我采取了第一种方式。

在学习selenium2java的时候，在写收货地址相关用例的时候碰到了下拉框，刚好练习了一下select的使用，现在分享，供大家参考。


```
	//删除添加收货地址
	public static void deleteAndAddUserAdress(WebDriver driver) throws InterruptedException {
		clickUser(driver);
		findElementByTextAndClick(driver, "个人信息");
		findElementByTextAndClick(driver, "收货地址");
		clickDeleteAdress(driver);
		sleep(0);
		clickSure(driver);
		AddAddress(driver);
		String name = getTextByXpath(driver, ".//*[@id='main']/div[2]/div/div/div[1]/p[1]");
		assertTrue("添加收获地址失败！", name.equals("收货人:测试收货人"));
	}
```
下面是具体的添加收货地址的方法：

```
//添加收货地址
	public static void AddAdress(WebDriver driver) {
		findElementByIdAndClick(driver, "add-address-btn");//点击添加地址
		findElementByXpathAndClearSendkeys(driver, ".//*[@id='LAY_layuipro1a']/div/div[1]/table/tbody/tr[1]/td[2]/div/input", "测试收货人");//添加收货人
		findElementByXpathAndClearSendkeys(driver, ".//*[@id='LAY_layuipro1a']/div/div[1]/table/tbody/tr[2]/td[2]/div/input", "13120454218");//输入手机号
		//选择省市县，以及详细地址
		Select province = new Select(findElementByid(driver, "province-select"));
		province.selectByIndex(1);
		Select city = new Select(findElementByid(driver, "city-select"));
		city.selectByIndex(1);			
		Select area = new Select(findElementByid(driver, "area-select"));
		area.selectByIndex(1);
		findElementByClassnameAndClearSendkeys(driver, "textarea", "我是测试地址。");
		clickSave(driver);
	}
```

### 往期文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
8. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
9. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
10. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
11. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
12. [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
13. [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)

