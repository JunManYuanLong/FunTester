# Charles报错Failed to install helper解决方案

一直在使用`Charles`，本来打算这等[未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)成熟以后就替换掉`Charles`，没想到它提前故障了。

报错内容如下：

```
Automatic macOS Proxy Configuration

Failed to install helper: The operation couldn't be completed. (CFErrorDomainLaunchd error 9.)
```

![](http://pic.automancloud.com/WX20200811-140948.png)

在操作本机代理的时候会提示需要设置本机代理

![](http://pic.automancloud.com/WX20200811-140932.png)


选择了`Grant Privileges`选择之后会提示。

刚开始还以为是因为权限的问题，或者本机代理默认设置的原因，后来发现都不是。再联想近期所在的配置更改，突然想到我禁止过一个自动配置的启动程序，找到后发现正是`Charles`的自动代理配置功能。

软件内截图如下：

![](http://pic.automancloud.com/WX20200811-101622@2x.png)

配置项路径：`/Library/LaunchDaemons`
配置项名称：`com.xk72.charles.ProxyHelper.plist`

找到原文件后，内容如下：

```HTML
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
	<key>Label</key>
	<string>com.xk72.charles.ProxyHelper</string>
	<key>MachServices</key>
	<dict>
		<key>com.xk72.charles.ProxyHelper</key>
		<true/>
	</dict>
	<key>Program</key>
	<string>/Library/PrivilegedHelperTools/com.xk72.charles.ProxyHelper</string>
	<key>ProgramArguments</key>
	<array>
		<string>/Library/PrivilegedHelperTools/com.xk72.charles.ProxyHelper</string>
	</array>
</dict>
</plist>

```

终于还是被我找到了，这是配置`proxy`的程序。

启动项改成如下配置即可：

![](http://pic.automancloud.com/WX20200811-141310@2x.png)

重启`Charles`，一切**OK**！

--- 
* 公众号**FunTester**首发，更多原创文章：[FunTester440+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [开源测试服务](https://mp.weixin.qq.com/s/ZOs0cp_vt6_iiundHaKk4g)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [电子书网站爬虫实践](https://mp.weixin.qq.com/s/KGW0dIS5NTLgxyhSjxDiOw)
- [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)