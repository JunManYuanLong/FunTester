# java和groovy混编的Maven项目如何用intellij打包执行jar包
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

由于自己之前使用的是Gradle构建工具，最近切到Maven有些不太适应，特别是在java和groovy混编时，在打包jar包的过程中出现问题，困扰了很久，在网上查了一些资料，都是引入build插件即可，但是插件有的是eclipse的插件，进过分析和摸索终于得到了一个再intellij使用Maven打包混编项目的jar包的方法。

首先把项目的java文件夹改成groovy，如图：

![](/blog/pic/afdsfdsfadf32423.png)

然后就是配置pom文件，分依赖和build两部分：


```
<dependency>
            <groupId>org.codehaus.groovy</groupId>
            <artifactId>groovy-all</artifactId>
            <version>2.4.8</version>
        </dependency>


    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.6.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>2.6</version>
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>lib/</classpathPrefix>
                            <mainClass>com.okayqa.studentapd.T2</mainClass> <!-- 运行jar的main class  -->
                        </manifest>
                        <!-- 添加本地的jar -->
                        <manifestEntries>
                            <!-- 这个>lib/class-util-1.0.jar 路径是已经被打包到target/lib里的,多个包用空格隔开就可以了 -->
                            <Class-Path>/Users/fv/Documents/workspace/fun/build/libs/fun-1.0.jar</Class-Path>
                        </manifestEntries>
                    </archive>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.codehaus.gmaven</groupId>
                <artifactId>gmaven-plugin</artifactId>
                <version>1.2</version>
                <configuration>
                    <providerSelection>1.7</providerSelection>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>org.codehaus.gmaven.runtime</groupId>
                        <artifactId>gmaven-runtime-1.7</artifactId>
                        <version>1.2</version>
                        <exclusions>
                            <exclusion>
                                <groupId>org.codehaus.groovy</groupId>
                                <artifactId>groovy-all</artifactId>
                            </exclusion>
                        </exclusions>
                    </dependency>
                    <dependency>
                        <groupId>org.codehaus.groovy</groupId>
                        <artifactId>groovy-all</artifactId>
                        <version>1.7.0</version>
                    </dependency>
                </dependencies>
                <executions>
                    <execution>
                        <goals>
                            <goal>generateStubs</goal>
                            <goal>compile</goal>
                            <goal>generateTestStubs</goal>
                            <goal>testCompile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

* 中间有一个打包本机jar的设置没删除，如果本地jar包路径配置的地方报红，检查没问题的话可以不管，不影响打包编译。


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>