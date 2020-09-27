# selenium2java 遇到有三个窗口用例的处理办法


本人在学习selenium2java的时候遇到一个用例，执行完竟然有三个窗口，使用handles的办法教程只写了两个窗口的解决办法，后来我把handles全都输出出来后发现其实相当于三个窗口，再进去第三个窗口的时候，多判断一下就好了，斜面分享一下自己的经验。



```
//登录下载精品资源
    public static void downloadResources(WebDriver driver, boolean key) throws InterruptedException, AWTException {
        if (key) {
            loginWithTeacher(driver);
        } else {
            loginWithStudent(driver);
        }
        findElementByTextAndClick(driver, "精品资源");
        String homehandle = driver.getWindowHandle();
        findElementByXpathAndClick(driver, "html/body/div[3]/ul/li[4]/a");
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (handle.equals(homehandle) == false) {
                driver.switchTo().window(handle);
                output("切换到精品资源页面了！");
                Thread.sleep(2000);
                findElementByXpathAndClick(driver, "html/body/div[5]/div[2]/div[1]/div[1]/dl/dd/a[4]");
                findElementByXpathAndClick(driver, "html/body/div[5]/div[2]/div[1]/div[2]/dl/dd/a[3]");
                findElementByXpathAndClick(driver, "html/body/div[5]/div[2]/div[1]/div[4]/dl/dd/a[3]");
                Thread.sleep(2000);
                String news = driver.getWindowHandle();
                findElementByTextAndClick(driver, "点击下载");
                Set<String> handlenews = driver.getWindowHandles();
                for (String newsss : handlenews) {
                    if (newsss.equals(news) == false && newsss.equals(homehandle) == false) {
                        driver.switchTo().window(newsss);
                        output("切换到资源页面了！");
                        findElementByXpathAndClick(driver, "html/body/div[3]/div[1]/div[2]/table/tbody/tr[3]/td[5]/a");
                        Thread.sleep(500);
                        driver.switchTo().alert().accept();
                        driver.close();
                    }
                }
                for (String newsss : handlenews) {
                    if (newsss.equals(news)) {
                        driver.switchTo().window(newsss);
                        output("回到精品资源页面了！");
                        driver.close();
                    }
                }
            }
        }
        for (String handle : handles) {
            if (handle.equals(homehandle)) {
                driver.switchTo().window(handle);
                output("回到首页了！");
                Thread.sleep(2000);
                driver.quit();
            }
        }
    }
```

## 往期文章精选

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

## [公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>