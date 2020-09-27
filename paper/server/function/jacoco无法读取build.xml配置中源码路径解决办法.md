# jacoco无法读取build.xml配置中源码路径解决办法


本人在使用jacoco做用例的代码覆盖率的时候遇到一个问题，就是按照文档配置的build.xml中的class文件路径和源码文件路径，但是在第一次尝试成功之后，我为了区分各个项目源码，做文件路径做了修改，就一直不成功了，经过N次的错误尝试，终于发现了文档中缺失的部分，就是class文件路径并没有严格的要求，包括java启动参数里面的include参数也没有严格的要求，但是特么源码路径就必需得配置到com包上一级路径，一般来说也就是main/java这一层，着实尴尬不已。分享一下我到 build.xml配置文件。

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
                                <exclude name="**/request/*.class"/>
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
---
* **郑重声明**：文章禁止第三方（腾讯云除外）转载、发表，事情原委[测试窝，首页抄我七篇原创还拉黑，你们的良心不会痛吗？](https://mp.weixin.qq.com/s/ke5avkknkDMCLMAOGT7wiQ)
