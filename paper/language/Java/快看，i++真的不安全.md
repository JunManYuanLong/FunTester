# 快看，i++真的不安全
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---

上期文章讲到“i++;”本身是一个线程不安全的操作，原因是操作不是原子性的，存在取值和赋值的两个过程，但是究竟怎么会不安全呢？本期借助一个“vmlens”的项目来演示为何会发生线程不安全的情况。文末是vmlens简介。

测试代码：

```
public class TestCounter {
	private volatile int i = 0;
	@Interleave
	public void increment() {
	 i++;	
	}
	@Test
	public void testUpdate() throws InterruptedException	{
		Thread first = new Thread( () ->   {increment();} ) ;
		Thread second = new Thread( () ->   {increment();} ) ;
		first.start();
		second.start();
		first.join();
		second.join();
		
	}	
	@After
	public void checkResult() {
		assertEquals( 2 , i );
	}	
}
```

重要的是pom.xml文件配置：

```
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.vmlens</groupId>
  <artifactId>examples</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>examples</name>
  <url>http://maven.apache.org</url>

 <pluginRepositories>
  <pluginRepository>
    <id>vmlens</id>
    <url>http://vmlens.com/download</url>
  </pluginRepository>
</pluginRepositories>


  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
   		<dependency>
			<groupId>com.vmlens</groupId>
			<artifactId>annotation</artifactId>
			<version>1.0.2</version>
			<scope>test</scope>
		</dependency>
  
  
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.8.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
   <build>
    <pluginManagement>
    <plugins>
    
     
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.5.1</version>
        <configuration>
          <source>1.8</source>
          <target>1.8</target>
        </configuration>
      </plugin>
    </plugins>
 </pluginManagement>
 
  <plugins>
  			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-surefire-plugin</artifactId>
				<version>3.0.0-M3</version>
				<configuration>
					<includes>
						<include>none</include>
					</includes>
				</configuration>
			</plugin>
 	<plugin>
				<groupId>com.vmlens</groupId>
				<artifactId>interleave</artifactId>
				<version>1.0.4</version>
				<!-- start regression test -->
				<configuration>
					<trimStackTrace>false</trimStackTrace>
									<includes>
		<include>com.vmlens.examples.doNotCombine.TestCounter</include>
					</includes>
				</configuration>
           </plugin>
      </plugins>
  </build>
  </project>

```

接下来是vmlens的报告：

![](/blog/pic/vmlens14984651231.png)

从图中我们可以看出在两个线程同时执行“i++;”的时候，两个线程都先后读取到了i的值“0”，然后先后完成了计算“i+1”，最后又先后给i赋值“1”，导致测试用例执行失败。

介绍一下这个插件：
“vmlens”是一款测试java多线程的工具。
1. 需要测试多个线程访问相同内存位置或监视器的应用程序的所有部分。vmlens显示多个线程访问相同内存位置或监视器的所有位置。
2. vmlens插入等待，在测试期间通知指令并重新运行测试，直到测试所有线程交错。这与数据竞争和死锁的自动检测一起导致系统和可重复的测试。
3. 通过查看多个线程以何种方式访问​​相同状态，您可以减少共享状态的数量。
4. 较少共享状态意味着需要较少的同步监视器。

下面是作者托马斯原文：

```
Hello!

Do you love to write bug-free software? Me too!

It always bothers me when I can not test something. That's why I created vmlens, a tool to test multithreaded java.

Now 4 years and countless tests later vmlens enables you to test multi-threaded java systematic and reproducible. And like vmlens now let my completely test vmlens, vmlens let you test the multithreaded part of your application

Enjoy writing concurrent software secured by tests.

Cheers, Thomas
```

作者本人照片：
![](/blog/pic/thomas.png)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)


<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---


<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>