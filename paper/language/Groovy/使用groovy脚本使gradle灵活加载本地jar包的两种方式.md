# 使用groovy脚本使gradle灵活加载本地jar包的两种方式
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
本人在使用Jenkins做测试项目的可持续集成过程中，构建工具用的gradle，但由于一些jar包是并私有仓库给用，暂时没有搭建计划。这就导致了我构建项目的时候需要的jar的地址往往是不一样的，而且服务器和本地的版本可能也有所差别，经常其他同学提交代码时候把build.gradle文件一并提交了，倒是仓库文件比较乱。为了解决这个问题，看了一些资料再研究了一点点gradle的使用后总结了两种方法。


第一种思路：把每个人的项目依赖的jar包地址给固定了，然后用判断当前用户是哪个，再去给complie files参数赋值。比较笨，但是比较容易理解，由于框架的jar包和一些固定的jar包版本不怎么发生变化，维护成本较低。也是我这个菜鸟想到的第一个办法，虽然已经不用了，还是记录一下比较好

第二种思路：每次去局域网服务器下载jar包，比对版本，如果一样则下载到项目的文件夹里，再去给complie files参数赋值。这个比较简单，而且能够做到jar包版本更新的时候自动同步（服务端的jar有Jenkins生成）。暂时想到的比较好的办法。

分享一下代码，供大家参考：

```
buildscript {
    ext {
        springBootVersion = '1.5.13.RELEASE'
    }
    repositories {
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
    }
}
 
apply plugin: 'java'
apply plugin: 'idea'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'
 
group = 'com.fission.test'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = 1.8
 
repositories {
    mavenLocal()
    mavenCentral()
}
def jarversion = "api_test_najm-1.0-SNAPSHOT.jar"
def path = getPath2(jarversion)
 
def getPath() {
    def local;
    File file1 = new File("/Users/Vicky/Documents/workspace/api_test_najm/build/libs/api_test_najm-1.0-SNAPSHOT.jar")
    File file2 = new File("/go/jar/api_test_najm-1.0-SNAPSHOT.jar")
    if (file1.exists()) {
        local = file1.getAbsoluteFile()
    } else if (file2.exists()) {
        local = file2.getAbsoluteFile()
    }
    println local
    return local
}
 
def getPath2(String v) {
    def jarpath = new File("").getAbsolutePath() + "/long/" + v
    if (new File(jarpath).exists()) return jarpath
    def url = new URL("http://**.***.**.**:****/go/jar/" + v)
    def out = new FileOutputStream(jarpath)
    out << url.newInputStream()
    return jarpath
}
 
 
dependencies {
    runtime('org.springframework.boot:spring-boot-devtools')
    compile('org.springframework.boot:spring-boot-starter-web')
    testCompile('org.springframework.boot:spring-boot-starter-test')
    compile "org.springframework.boot:spring-boot-starter-thymeleaf"
    compile group: 'org.apache.logging.log4j', name: 'log4j-api', version: '2.11.0'
    compile group: 'org.apache.logging.log4j', name: 'log4j-core', version: '2.11.0'
    compile group: 'org.slf4j', name: 'slf4j-log4j12', version: '1.8.0-beta2'
    compile files(path)
}
```

由于之前接触过groovy语言，写起了也非常顺手，先简单写个例子。以后有机会继续分享gradle自定义脚本任务和Jenkins集成的实践经验。

## 往期文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
8. [将json数据格式化输出到控制台](https://mp.weixin.qq.com/s/2IPwvh-33Ov2jBh0_L8shA)
9. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
10. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
11. [Mac+httpclient高并发配置实例](https://mp.weixin.qq.com/s/r4a-vGz0pxeZBPPH3phujw)
12. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)

## [公众号地图](https://mp.weixin.qq.com/s/36RbP20beZ8oWJ9nLAxG3g) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)



> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)


<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>