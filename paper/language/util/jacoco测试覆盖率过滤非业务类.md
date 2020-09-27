# jacoco测试覆盖率过滤非业务类


之前在做[接口测试代码覆盖率（jacoco）方案](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)的时候，漏了一些东西，这篇文章补一下。做使用`jacoco`做接口代码覆盖率测试的过程中，遇到一个问题：测试报告里面信息太多，很杂乱没有针对性，很多都是`config`和`bean`以及适配器的类，绝大部分没有业务代码，统计出来的覆盖率受影响比较大，不够准确。

这里就引入了如何过滤`jacoco`代码覆盖率测试报告的问题，经过查阅资料，大概的方案分两种：一是在`jacoco`配置中过滤，二是在`class`文件夹中删除掉无用的`class`文件。

经过一些考量，决定采用第一种方案，原因如下：

1. 第二种方案不可逆，如果在统计完某一个`moudel`的覆盖率，之后在统计其他`moudel`的时候就无法直接实现；
2. 第二种处理起来比较麻烦，规则需要脚本实现。
3. 与现有框架不好结合，没办法在报告的框架中方便快捷实现这个功能。

第一种方案主要修改`build`文件的配置:


```闲了

<?xml version="1.0" ?>
<project name="studentpad-middle-toc" basedir="/home/jmsmanager/report/studentpad-middle-toc"
    xmlns:jacoco="antlib:org.jacoco.ant"
    xmlns:sonar="antlib:org.sonar.ant" default="all">
    <!--项目名-->
    <property name="projectName" value="studentpad-middle-toc"/>
    <!--Jacoco的安装路径-->
    <property name="jacocoantPath" value="/home/jmsmanager/jacoco/lib/jacocoant.jar"/>
    <!--生成覆盖率报告的路径-->
    <property name="reportfolderPath" value="${basedir}/report/"/>
    <!--远程服务的ip地址，如有多个，可设置多个，name需修改-->
    <property name="server_ip" value="127.0.0.1"/>

    <!--待测程序.class文件路径-->
    <property name="waterommpClasspath" value="/xdfapp/${projectName}/webapps/ROOT/WEB-INF/classes/com/noriental/moudle"/>

    <!--待测程序源码文件路径-->
    <property name="mcmSrcpath" value="${basedir}/source/${projectName}/workspace/src/main/java"/>
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
        <jacoco:dump address="${server_ip}" reset="true" destfile="${basedir}/jacoco.exec" port="12345" append="false"/>
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

                <group name="学生中间层代码覆盖率">
                    <classfiles>
                        <fileset dir="${waterommpClasspath}">
				<exclude name="**/vo/*.class"/>
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
    <target name="all" />

</project>
```
主要在exclude这个标签里面过滤，如果大块排除，也可以在指定classfilepath的时候过滤。

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester十八张原创专辑](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**，合作请联系`Fhaohaizi@163.com`。

### 热文精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [删除List中null的N种方法--最后放大招](https://mp.weixin.qq.com/s/4mfskN781dybyL59dbSbeQ)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [Selenium并行测试基础](https://mp.weixin.qq.com/s/OfXipd7YtqL2AdGAQ5cIMw)
- [Selenium并行测试最佳实践](https://mp.weixin.qq.com/s/-RsQZaT5pH8DHPvm0L8Hjw)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)