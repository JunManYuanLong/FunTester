# selenium2java爬虫示例

本人在使用图灵机器人的过程中，需要丰富一下机器人知识库里面的笑话、段子等内容，就得去网上爬一些内容下来，经过尝试终于成功了，效果一般般，主要原因是添加的知识条目审核不通过，还有就是爬虫次数限制，暂时放弃了，以后打算用接口做爬虫，selenium爬起来很容易出错，浏览器加载太慢了，一旦次数太多很耗时。分享一下代码，供大家参考。


```
package wepractice;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import selenium.Library;
import selenium.Excel;
 
public class NeiHanjokes extends Library{
	public static void main(String[] args) {
		Library library = new Library();
		List<String[]> sheet = new ArrayList<String[]>();//新建list，用于存放每个测试用例的测试结果
		Map<Integer, List<String[]>> dateJoke = new HashMap<Integer, List<String[]>>();
 
		driver.get("http://neihanshequ.com/");
		String home = driver.getWindowHandle();
		library.findElementByXpathAndClick(".//*[@id='detail-list']/li[1]/div/div[2]/a/div/h1/p");
		Set<String> handles = driver.getWindowHandles();
		for(String handle : handles){
			if (!handle.equals(home)) {
				driver.switchTo().window(handle);
			}
		}
		for (int i = 0; i < 15; i++) {
			library.output(i);
			String joke = library.getTextByXpath("html/body/div[3]/div[1]/div/ul/li[1]/div/div[2]/a/div/h1/p");
	    	String[]  jokes = new String[1];
	    	jokes[0] = joke;
	    	sheet.add(jokes);
	    	library.findElementByIdAndClick("prevGroupLink");
		}
    	
    	dateJoke.put(1, sheet);
    	Excel excel = new Excel();
    	excel.writeXlsx(dateJoke);
    	driver.close();//关闭窗口
    	for(String handle : handles){//切换窗口
			if (handle.equals(home)) {
				driver.switchTo().window(handle);
			}
		}
    	driver.quit();//退出浏览器
	}
}
```

### 技术类文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
8. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
9. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
10. [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
11. [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)

### 非技术文章精选
1. [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
2. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
3. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
4. [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)


## [公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect) 


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>