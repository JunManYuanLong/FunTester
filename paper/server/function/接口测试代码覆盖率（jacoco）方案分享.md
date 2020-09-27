# 接口测试代码覆盖率（jacoco）方案分享

在做接口测试过程中，为了达到量化接口测试用例效果的目的，引入了代码覆盖率作为重要指标，在查阅相关文档和资料通过实践之后，大概得到了一个方案。如图：

![](http://pic.automancloud.com/WechatIMG12_Fotor3243243.jpg)

> 备注：该方案略微复杂了一些，原因在于服务JVM所在的服务器和Jenkins构建服务器与测试脚本所在服务器分别在三台服务器上。

Jenkins机器：服务对应的Jenkins机器

测试项目Jenkins：服务对应的测试项目Jenkins机器

第一次画流程图，有点乱，这里在介绍一下思路，通过服务所在的Tomcat容器的启动脚本（JAVA_OPTS参数）引入jacoco配置，然后启动服务，通过ant配置build.xml读取exec的信息编译并生成报告（html格式）。最后使用web服务来查看相关报告。

最终页面展示情况如下：
![](http://pic.automancloud.com/jacocof5ds4f94sa9.png)
![](http://pic.automancloud.com/jacoco324523452445.png)
![](http://pic.automancloud.com/jacoco5d4gag.png)

### jacoco配置

```
jacoco配置
jacoco配置需要在jar项目启动参数里面添加如下信息：

 -javaagent:/home/jmsmanager/jacoco/lib/jacocoagent.jar=includes=com.noriental.*,output=tcpserver,address=127.0.0.1,port=12345

javaagent配置：jacocoagent.jar所在目录

includes配置：所包含的包路径

output配置：输出类型，默认tcpserver

address配置：服务ip，本机IP选12.7.0.0.1

port配置：端口，任选
```

### ant配置build.xml文件

```
<?xml version="1.0" ?>
<project name="user-center" basedir="/home/jmsmanager/report/user-center"
    xmlns:jacoco="antlib:org.jacoco.ant"
    xmlns:sonar="antlib:org.sonar.ant" default="all">
    <!--项目名-->
    <property name="projectName" value="user-center"/>
    <!--Jacoco的安装路径-->
    <property name="jacocoantPath" value="/home/jmsmanager/jacoco/lib/jacocoant.jar"/>
    <!--生成覆盖率报告的路径-->
    <property name="reportfolderPath" value="${basedir}/report/"/>
    <!--远程服务的ip地址，如有多个，可设置多个，name需修改-->
    <property name="server_ip" value="127.0.0.1"/>

    <!--待测程序.class文件路径-->
    <property name="waterommpClasspath" value="/home/jmsmanager/report/${projectName}/source/BOOT-INF/classes/com/noriental/center/moudle/"/>

    <!--待测程序源码文件路径-->
    <property name="mcmSrcpath" value="${basedir}/source/src/main/java"/>
    <!--Jacoco所在目录-->
    <taskdef uri="antlib:org.jacoco.ant" resource="org/jacoco/ant/antlib.xml">
        <classpath path="${jacocoantPath}" />
    </taskdef>

    <!--merge task，当有多个待测程序时，生成报告前需将所有.exec文件merge成一个-->
    <target name="merge" depends="dump">
        <jacoco:merge destfile="jacoco.exec">
            <fileset dir="${basedir}" includes="*.exec"/>
        </jacoco:merge>
    </target>
	<!--dump任务:
           根据前面配置的ip地址，和端口号，访问目标服务，并生成.exec文件。-->

    <target name="dump">
        <!-- reset="true"是指在dump完成之后，重置jvm中的覆盖率数据为空。append="true"是指dump出来的exec文件为增量方式 -->
        <jacoco:dump address="${server_ip}" reset="false" destfile="${basedir}/jacoco.exec" port="12347" append="true"/>
    </target>


    <!--report任务:
               根据前面配置的源代码路径和.class文件路径，
      根据dump后，生成的.exec文件，生成最终的html覆盖率报告。-->
    <target name="report">
        <delete dir="${reportfolderPath}" />
        <mkdir dir="${reportfolderPath}" />

        <jacoco:report>
            <executiondata>
                <file file="${basedir}/jacoco.exec" />

            </executiondata>

            <structure name="JaCoCo Report">

                <group name="用户中心覆盖率报告">
                    <classfiles>
                        <fileset dir="${waterommpClasspath}">
* [ ]                                 <exclude name="**/request/*.class"/>
                                <exclude name="**/response/*.class"/>
                        </fileset>
                    </classfiles>
		    <sourcefiles encoding="UTF-8">
                    	<fileset dir="${mcmSrcpath}">
                  	</fileset>
                    </sourcefiles>
                </group>
            </structure>

            <html destdir="${reportfolderPath}" encoding="utf-8" />
            <csv destfile="${reportfolderPath}/report.csv" />
            <xml destfile="${reportfolderPath}/report.xml" />
        </jacoco:report>
    </target>
</project>
```
因为报告在服务所运行的机器上，而且设计的机器比较多，所以把测试报告集中放在某一台机器上统一提供查看。

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