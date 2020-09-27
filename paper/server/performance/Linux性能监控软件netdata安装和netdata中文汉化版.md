# Linux性能监控软件netdata安装和netdata中文汉化版


成果展示页面：[netdata中文汉化版展示页面](http://blog.fv1314.xyz:19999)

这是我fork仓库的地址：[netdata中文版](https://github.com/Fhaohaizi/netdata)

由于本人水平有限，很多地方还得依赖大神的帮助：[请不吝赐教](http://blog.fv1314.xyz/blog/%E4%BA%A4%E4%B8%AA%E6%9C%8B%E5%8F%8B.html)

新版的netdata中文汉化安装教程如下：

1.依赖安装（同原版）

2.netdata安装（请移步本人GitHub上fork仓库地址下载安装包）

3.执行脚本（netdata-installer-zh.sh）

4.正常来说netdata服务会自动重启（刷新浏览器缓存即可查看汉化效果）

如图：
![](http://pic.automancloud.com/20190125160129621.png)


-------
-------

本人在团队内推广netdata监控Linux服务器性能的过程中，遇到最大的问题就是汉化，因为netdata至今依然没有推出中文版本。所以只能自己做一些简单的汉化工作，幸好作者提供了这么一个功能。我先做了一点尝试，首先说一下安装过程，由于比较简单就概述一下。

1.安装依赖，第一行安装基本的部分，不包括mysql / mariadb, postgres, named, hardware sensors and SNMP. 第二条是完整安装所有依赖。

```
curl -Ss 'https://raw.githubusercontent.com/firehol/netdata-demo-site/master/install-required-packages.sh' >/tmp/kickstart.sh && bash /tmp/kickstart.sh -i netdata
curl -Ss 'https://raw.githubusercontent.com/firehol/netdata-demo-site/master/install-required-packages.sh' >/tmp/kickstart.sh && bash /tmp/kickstart.sh -i netdata-all
```
2.安装netdata


```
# 下载
git clone https://github.com/firehol/netdata.git --depth=1
cd netdata
 
# 安装
./netdata-installer.sh
```
3.启动netdata服务


```
service netdata start
```
然后就能访问http://ip:19999/，就能看到netdata的web页面。如下图
![](http://pic.automancloud.com/20190125160129621.png)




> 欢迎有兴趣的一起交流：群号:340964272

![](http://pic.automancloud.com/201712120951590031.png)

 ---
* **郑重声明**：文章首发于公众号“FunTester”，欢迎关注交流，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [压测中测量异步写入接口的延迟](https://mp.weixin.qq.com/s/odvK1iYgg4eRVtOOPbq15w)
- [多种登录方式定量性能测试方案](https://mp.weixin.qq.com/s/WuZ2h2rr0rNBgEvQVioacA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [多项目登录互踢测试用例](https://mp.weixin.qq.com/s/Nn_CUy_j7j6bUwHSkO0pCQ)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [选择手动测试还是自动化测试？](https://mp.weixin.qq.com/s/4haRrfSIp5Plgm_GN98lRA)
- [自动化测试项目为何失败](https://mp.weixin.qq.com/s/KFJXuLjjs1hii47C1BH8PA)
- [简化测试用例](https://mp.weixin.qq.com/s/BhwfDqhN9yoa3Iul_Eu5TA)