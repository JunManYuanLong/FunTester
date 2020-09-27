# 使用Groovy进行Bash（shell）操作

[原文地址](https://www.javacodegeeks.com/2014/11/using-groovy-for-bash-shell-operations.html)

最近，我需要创建一个Groovy脚本来删除Linux机器中的某些目录。原因如下：
1. 我们有一台服务器来执行预定的作业。从一个数据库到另一个数据库的ETL之类的工作，从文件到数据库的工作，等等。服务器激活客户端，这些客户端位于我们要对其执行操作的机器中。大多数（几乎所有）作业都是用普通脚本编写的。
2. CI流程的一部分是将WAR部署到专用服务器中。然后，我们有了一个脚本，该脚本除其他外还使用软链接将“ webapps”定向到新创建的目录。该部署每小时进行一次，这将很快填满专用服务器。
因此，我需要创建一个脚本来检查正确位置中的所有目录并删除旧目录。我决定保留最新的4个目录。当前是脚本中的可变数字。如果我想要/需要，可以将其作为输入参数。但是我决定从简单开始。

我决定做的很简单：

在已知位置列出所有前缀为webapp的目录

按时间，降序对它们进行排序，从第四个索引开始执行删除操作。


```
def numberOfDirectoriesToKeep = 4
def webappsDir = new File('/usr/local/tomcat/tomcat_aps')
def webDirectories = webappsDir.listFiles().grep(~/.*webapps_.*/)
def numberOfWeappsDirectories = webDirectories.size();
 
if (numberOfWeappsDirectories >= numberOfDirectoriesToKeep) {
  webDirectories.sort{it.lastModified() }.reverse()[numberOfDirectoriesToKeep..numberOfWeappsDirectories-1].each {
    logger.info("Deleteing ${it}");
    // here we'll delete the file. First try was doing a Java/groovy command of deleting directories
  }
} else {
  logger.info("Too few web directories")
}
```
**没用！！！文件未删除。**
碰巧代理程序以与运行tomcat的用户不同的身份运行。该代理无权删除目录。

我的解决方案是使用运行shell命令sudo。

长话短说，这是完整的脚本：


```
import org.slf4j.Logger
import com.my.ProcessingJobResult
 
def Logger logger = jobLogger
//ProcessingJobResult is proprietary 
def ProcessingJobResult result = jobResult
 
try {
    logger.info("Deleting old webapps from CI - START")
    def numberOfDirectoriesToKeep = 4 // Can be externalized to input parameter
    def webappsDir = new File('/usr/local/tomcat/tomcat_aps')
    def webDirectories = webappsDir.listFiles().grep(~/.*webapps_.*/)
    def numberOfWeappsDirectories = webDirectories.size();
 
    if (numberOfWeappsDirectories >= numberOfDirectoriesToKeep) {
        webDirectories.sort{it.lastModified() }.reverse()[numberOfDirectoriesToKeep..numberOfWeappsDirectories-1].each {
            logger.info("Deleteing ${it}");
            def deleteCommand = "sudo -u tomcat rm -rf " + it.toString();
            deleteCommand.execute();
        }
    } else {
        logger.info("Too few web directories")
    }
    result.status = Boolean.TRUE
    result.resultDescription = "Deleting old webapps from CI ended"
    logger.info("Deleting old webapps from CI - DONE")
} catch (Exception e) {
    logger.error(e.message, e)
    result.status = Boolean.FALSE
    result.resultError = e.message
}
 
return result
```

顺便说一句，有一个较小的索引错误，由于我们总是有更多目录，所以我决定不修复。



### 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
- [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
- [python plotly处理接口性能测试数据方法封装](https://mp.weixin.qq.com/s/NxVdvYlD7PheNCv8AMYqhg)
- [单点登录性能测试方案](https://mp.weixin.qq.com/s/sv8FnvIq44dFEq63LpOD2A)



### 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
- [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
- [测试之《代码不朽》脑图](https://mp.weixin.qq.com/s/2aGLK3knUiiSoex-kmi0GA)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [优秀软件开发人员的态度](https://mp.weixin.qq.com/s/0uEEeFaR27aTlyp-sm61bA)
- [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [自动化测试解决了什么问题](https://mp.weixin.qq.com/s/96k2I_OBHayliYGs2xo6OA)
- [17种软件测试人员常用的高效技能-上](https://mp.weixin.qq.com/s/vrM_LxQMgTSdJxaPnD_CqQ)
- [17种软件测试人员常用的高效技能-下](https://mp.weixin.qq.com/s/uyWdVm74TYKb62eIRKL7nQ)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)

### 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)



# [点击查看公众号地图](https://mp.weixin.qq.com/s/l_zkWzQL65OIQOjKIvdG-Q)