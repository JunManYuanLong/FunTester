# java测试框架如何执行groovy脚本文件


本人在写基于httpclient的测试框架时，用到了groovy脚本作为测试用例的脚本语言，自然就需要java执行上传的测试脚本，在看过实例之后，自己进行了封装，总体来说跟java反射执行java方法类似。但又有一些不兼容的情况，部分已经写了博客做记录了，以后会陆续更新。分享代码，供大家参考。

其中一个比较大的区别时，在获取groovy类加载器的时候必须是非静态的。


```
package com.fission.source.source;
 
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.codehaus.groovy.tools.GroovyClass;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
 
public class ExcuteGroovy extends SourceCode {
	private String path;
	private String name;
	private String[] args;//参数，暂时不支持参数
	private List<String> files = new ArrayList<>();//所有脚本
	private GroovyClassLoader loader = new GroovyClassLoader(getClass().getClassLoader());
	private GroovyObject groovyObject;//groovy对象
	private Class<?> groovyClass;//执行类
 
 
	public ExcuteGroovy(String path, String name) {
		this.path = path;
		this.name = name;
		getGroovyObject();
	}
 
	/**
	 * 执行一个类的所有方法
	 */
	public void excuteAllMethod(String path) {
		getAllFile(path);
		if (files == null)
			return;
		files.forEach((file) -> new ExcuteGroovy(file, "").excuteMethodByPath());
	}
 
	/**
	 * 执行某个类的方法，需要做过滤
	 *
	 * @return
	 */
	public void excuteMethodByName() {
		if (new File(path).isDirectory()) {
			output("文件类型错误!");
		}
		try {
			groovyObject.invokeMethod(name, null);
		} catch (Exception e) {
			output("执行" + name + "失败!", e);
		}
 
	}
 
	/**
	 * 根据path执行相关方法
	 */
	public void excuteMethodByPath() {
		Method[] methods = groovyClass.getDeclaredMethods();//获取类方法，此处方法比较多，需过滤
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.contains("test") || methodName.equals("main")) {
				groovyObject.invokeMethod(methodName, null);
			}
		}
	}
 
	/**
	 * 获取groovy对象和执行类
	 */
	public void getGroovyObject() {
		try {
			groovyClass = loader.parseClass(new File(path));//创建类
		} catch (IOException e) {
			output(e);
		}
		try {
			groovyObject = (GroovyObject) groovyClass.newInstance();//创建类对象
		} catch (InstantiationException e) {
			output(e);
		} catch (IllegalAccessException e) {
			output(e);
		}
	}
 
	/**
	 * 获取文件下所有的groovy脚本,不支持递归查询
	 *
	 * @return
	 */
	public List<String> getAllFile(String path) {
		File file = new File(path);
		if (file.isFile()) {
			files.add(path);
			return files;
		}
		File[] files1 = file.listFiles();
		int size = files1.length;
		for (int i = 0; i < size; i++) {
			String name = files1[i].getAbsolutePath();
			if (name.endsWith(".groovy"))
				files.add(name);
		}
		return files;
	}
}
```
在获取groovy脚本的时候，并未用到递归，以后随着需求增加应该会增加递归。

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)

## 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)

## 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
- [JVM的对象和堆](https://mp.weixin.qq.com/s/iNDpTz3gBK3By_bvUnrWOA)