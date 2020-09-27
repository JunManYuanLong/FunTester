# httpclient 多线程爬虫实例

本人最近在研究安全测试的过程中，偶然发现某站一个漏洞，在获取资源的时候竟然不需要校验，原来设定的用户每天获取资源的次数限制就没了。赶紧想到用爬虫多爬一些数据，但是奈何数据量太大了，所以想到用多线程来爬虫。经过尝试终于完成了，脚本写得比较粗糙，因为没真想爬完。预计10万数据量，10个线程，每个线程爬1万，每次爬100个数据（竟然是 get 接口，有 url 长度限制）。

分享代码，供大家参考。

```
package practise;
 
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.client.methods.HttpGet;
import net.sf.json.JSONObject;
import source.ApiLibrary;
 
public class LoginDz extends ApiLibrary {
 
	public static void main(String[] args) {
		LoginDz loginDz = new LoginDz();
		loginDz.excuteTreads();
		testOver();
	}
 
	public JSONObject getTi(int[] code, String name) {
		JSONObject response = null;
		String url = "***********";
		JSONObject args = new JSONObject();
		// args.put("ID_List", getTiId(884969));
		args.put("ID_List", getTiId(code));
		HttpGet httpGet = getHttpGet(url, args);
		response = getHttpResponseEntityByJson(httpGet);
		// output(response.toString());
		String text = response.toString();
		if (!text.equals("{\"success_response\":[]}"))
			logLog("name", response.toString());
		output(response);
		return response;
	}
 
 
	public String getTiId(int... id) {
		StringBuffer result = new StringBuffer();
		int length = id.length;
		for (int i = 0; i < length; i++) {
			String abc = "filter[where][origDocID][inq]=" + id[i] + "&";
			result.append(abc);
		}
		return result.toString();
	}
 
	/**
	 * 执行多线程任务
	 */
	public void excuteTreads() {
		int threads = 10;
		ExecutorService executorService = Executors.newFixedThreadPool(threads);
		CountDownLatch countDownLatch = new CountDownLatch(threads);
		Date start = new Date();
		for (int i = 0; i < threads; i++) {
			executorService.execute(new More(countDownLatch, i));
		}
		try {
			countDownLatch.await();
			executorService.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Date end = new Date();
		outputTimeDiffer(start, end);
	}
 
	/**
	 * 多线程类
	 */
	class More implements Runnable {
		public CountDownLatch countDownLatch;
		public int num;
 
		public More(CountDownLatch countDownLatch, int num) {
			this.countDownLatch = countDownLatch;
			this.num = num;
		}
 
		@Override
		public void run() {
			int bound = num * 10000;
 
			try {
				for (int i = bound; i < bound + 10000; i += 100) {
					int[] ids = new int[100];
					for (int k = 0; k < 100; k++) {
						ids[i] = i + k;
						getTi(ids, bound + "");
					}
				}
			} finally {
				countDownLatch.countDown();
			}
		}
 
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
12. [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
13. [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)


### 非技术文章精选
1. [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
2. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
3. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
4. [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
5. [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
6. [测试之《代码不朽》脑图](https://mp.weixin.qq.com/s/2aGLK3knUiiSoex-kmi0GA)
7. [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
8. [优秀软件开发人员的态度](https://mp.weixin.qq.com/s/0uEEeFaR27aTlyp-sm61bA)
9. [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
