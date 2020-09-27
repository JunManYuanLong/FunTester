# android uiautomator自定义监听示例--一个弹出权限设置的监听
本人在学习android uiautomator时遇到一个问题，有时候应用安装使用过程中遇到突然弹出应用权限的安全警告导致运行中断的情况，在学习了监听之后自己写了一个监听权限设置弹出框的的监听，分享出来，请大神指正。


```
UiDevice.getInstance().registerWatcher("x1",new UiWatcher(){
        UiObject warrning = new UiObject(new UiSelector().text("安全警告"));
        @Override
        public boolean checkForCondition(){
        System.out.println("the watcher is begin !");
        if (warrning.exists()){
        UiObject noremind = new UiObject(new UiSelector().text("不再提醒"));
        try {
noremind.click();
} catch (UiObjectNotFoundException e1) {
e1.printStackTrace();
}
        UiObject allow = new UiObject(new UiSelector().text("允许"));
        try {
allow.click();
} catch (UiObjectNotFoundException e2) {
e2.printStackTrace();
}
        System.out.println("it is allow");
        return true;}
        System.out.println("it is refuse");
        return false;}}); 
UiObject ss = new UiObject(new UiSelector().text("扫啊扫"));
ss.click();
getUiDevice().runWatchers();//此行为强制运行监听，正常使用请删除
```

### 往期文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
8. [测试之JVM命令脑图](https://mp.weixin.qq.com/s/qprqyv0j3SCvGw1HMjbaMQ)
9. [将json数据格式化输出到控制台](https://mp.weixin.qq.com/s/2IPwvh-33Ov2jBh0_L8shA)
10. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
11. [“双花”BUG的测试分享](https://mp.weixin.qq.com/s/0dsBsssNfg-seJ_tu9zFaQ)
12. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
13. [Mac+httpclient高并发配置实例](https://mp.weixin.qq.com/s/r4a-vGz0pxeZBPPH3phujw)

## [公众号地图](https://mp.weixin.qq.com/s/36RbP20beZ8oWJ9nLAxG3g) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>