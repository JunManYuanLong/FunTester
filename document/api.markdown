# 接口测试

> FunTester，一个就知道瞎掰呼的核弹派测试人员，量变引起的质变。

从事专门的服务端测试两年时间，纯语言脚本测试，工作语言Java和Groovy。在经历过1年左右的实践，逐步完善了自己的测试框架（包含接口功能、自动化、性能）。框架基础依然是java语言，基于httpclient4.5进行的封装，还拓展了其他功能，如mysql存储数据，HTML报告，alterover分级通知等。下面是我在工作经历中遇到的比较典型的难点解决方案。


## 接口测试
接口功能和自动化过程中遇到的坑，大多是基础的功能实现Demo和测试痛点的解决方案。

- [使用springboot+mybatis数据库存储服务化](https://mp.weixin.qq.com/s/N_5tHW1JJLZlxCaDI2PvyQ)
- [alertover推送api的java httpclient实现实例](https://mp.weixin.qq.com/s/DJXCBEG3SbybfbT6blO1jA)
- [接口自动化通用验证类](https://mp.weixin.qq.com/s/fP1clCKkLREfg6POKV5n1A)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
- [使用httpclient实现图灵机器人web api调用实例](https://mp.weixin.qq.com/s/dYyxvAhwSmJkNI8N9lYQfg)
- [groovy如何使用java接口测试框架发送http请求](https://mp.weixin.qq.com/s/KF5lzMT-E2IBOkp_UjuC4g)
- [httpclient调用京东万象数字营销频道新闻api实例](https://mp.weixin.qq.com/s/kSqgSbPci-q2pfsdcU5Ekw)
- [httpclient遇到socket closed解决办法](https://mp.weixin.qq.com/s/mDRC7mssKmnvcI6StQWIBQ)
- [httpclient4.5如何确保资源释放](https://mp.weixin.qq.com/s/373Lx1bv0vi-pIBgWNzC9Q)
- [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [利用alertover发送获取响应失败的通知消息](https://mp.weixin.qq.com/s/w6y2UkgL3J20mAxc8fq0tA)
- [使用httpclient中EntityUtils类解析entity遇到socket closed错误的原因](https://mp.weixin.qq.com/s/RJnuOa2K6aRCElJafkFeug)
- [httpclient接口测试中重试控制器设置](https://mp.weixin.qq.com/s/hknNdq_ybQ1MoXh_dI3JVA)
- [拼接GET请求的参数](https://mp.weixin.qq.com/s/EGw_97scexH_3m2Uc8Ye5A)
- [httpclient上传文件方法的封装](https://mp.weixin.qq.com/s/HIrwl5ullvEmn_UuyLKkRg)
- [接口批量上传文件的实例](https://mp.weixin.qq.com/s/wZwkWchXXC6iddX1oVEnZQ)
- [httpclient发送https协议请求以及javax.net.ssl.SSLHandshakeException解决办法](https://mp.weixin.qq.com/s/uSHhKRrL2f9USKpSykkpkQ)
- [API测试基础](https://mp.weixin.qq.com/s/bkbUEa9CF21xMYSlhPcULw)
- [拷贝HttpRequestBase对象](https://mp.weixin.qq.com/s/kxB1c0GmSF5OAM15UQJU2Q)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [如何统一接口测试的功能、自动化和性能测试用例](https://mp.weixin.qq.com/s/1xqtXNVw7BdUa03nVcsMTg)
- [如何选择API测试工具](https://mp.weixin.qq.com/s/m2TNJDiqAAWYV9L6UP-29w)
- [初学者的API测试技巧](https://mp.weixin.qq.com/s/_uk6dw5Q7CfS-gXGH-TZEQ)

## 接口性能!
接口性能是做接口性能测试过程中使用的测试策略以及Groovy脚本实现，以及性能测试工具的实现，如timewatch和netdata汉化。
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/7VG7gHx7FUvsuNtBTJpjWA)
- [一个时间计数器timewatch辅助性能测试](https://mp.weixin.qq.com/s/-YZ04n2kyfO0q2QaKHX_0Q)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [Mac+httpclient高并发配置实例](https://mp.weixin.qq.com/s/r4a-vGz0pxeZBPPH3phujw)
- [单点登录性能测试方案](https://mp.weixin.qq.com/s/sv8FnvIq44dFEq63LpOD2A)
- [如何对单行多次update接口进行压测](https://mp.weixin.qq.com/s/Ly1Y4iPGgL6FNRsbOTv0sg)
- [如何对消息队列做性能测试](https://mp.weixin.qq.com/s/MNt22aW3Op9VQ5OoMzPwBw)
- [如何对修改密码接口进行压测](https://mp.weixin.qq.com/s/9CL_6-uZOlAh7oeo7NOpag)
- [如何对多行单次update接口进行压测](https://mp.weixin.qq.com/s/Fsqw7vlw6K9EKa_XJwGIgQ)
- [如何获取JVM堆转储文件](https://mp.weixin.qq.com/s/qCg7nsXVvT1q-9yquQOfWA)
- [性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)
- [性能测试中标记每个请求](https://mp.weixin.qq.com/s/PokvzoLdVf_y9inlVXHJHQ)
- [如何对N个接口按比例压测](https://mp.weixin.qq.com/s/GZxbH4GjDkk4BLqnUj1_kw)
- [如何性能测试中进行业务验证](https://mp.weixin.qq.com/s/OEvRy1bS2Yq_w1kGiidmng)
- [性能测试中记录每一个耗时请求](https://mp.weixin.qq.com/s/VXcp4uIMm8mRgqe8fVhuCQ)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [线程安全类在性能测试中应用](https://mp.weixin.qq.com/s/0-Y63wXqIugVC8RiKldHvg)
- [利用微基准测试修正压测结果](https://mp.weixin.qq.com/s/dmO33qhOBrTByw_NshS-uA)

## 测试方案
- [如何对消息队列做性能测试](https://mp.weixin.qq.com/s/MNt22aW3Op9VQ5OoMzPwBw)
- [如何对修改密码接口进行压测](https://mp.weixin.qq.com/s/9CL_6-uZOlAh7oeo7NOpag)
- [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
- [如何测试非固定型概率算法P=p(1+0.1*N)](https://mp.weixin.qq.com/s/sgg8v-Bi-_sUDJXwuTCMGg)
- [性能测试中标记每个请求](https://mp.weixin.qq.com/s/PokvzoLdVf_y9inlVXHJHQ)
- [如何对N个接口按比例压测](https://mp.weixin.qq.com/s/GZxbH4GjDkk4BLqnUj1_kw)


![](http://pic.automancloud.com/0_Fotor.jpg)

![](http://pic.automancloud.com/42387498274.jpeg)
