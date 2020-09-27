# httpclient 接口测试多线程并发类封装（新）
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
2018年08月07日更新内容如下：1.把请求方法从apilibrary提取出来；2.不在使用vector作为接口请求时间的直接存储对象，而是在每个线程中新建一个arraylist来存储，防止多线程请求存储数据时，可能导致延迟影响测试结果的问题。3.增加了ops简单计算方法;4.使用long类型存储请求时间替换之前的double方案，减少测试过程中多少的计算量，并修改响应的方法和存储对象类型。


```
package com.fission.source.source;
 
import com.fission.source.httpclient.ApiLibrary;
import com.fission.source.httpclient.ClientManage;
import com.fission.source.until.Save;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
 
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
public class Concurrent extends ApiLibrary {
	public HttpRequestBase request;//请求
	public int threads;//线程数，多请求等于请求数
	public int times;//线程请求执行次数
	public List<HttpRequestBase> requests;//多个请求
 
	/**
	 * 用于记录每次多线程执行的响应时间
	 */
	public static Vector<Long> allTimes = new Vector<>();
 
	/**
	 * 多请求单线程多次请求方法
	 *
	 * @param requests 请求列表
	 * @param times    单个请求的请求次数
	 */
	public Concurrent(List<HttpRequestBase> requests, int times) {
		this.requests = requests;
		this.times = times;
		this.threads = requests.size();
	}
 
	/**
	 * 单请求多线程多次任务构造方法
	 *
	 * @param request 被执行的请求
	 * @param threads 线程数
	 * @param times   每个线程运行的次数
	 */
	public Concurrent(HttpRequestBase request, int threads, int times) {
		this.request = request;
		this.threads = threads;
		this.times = times;
	}
 
	/**
	 * 执行多线程任务
	 */
	public void excuteTreads() {
		ExecutorService executorService = Executors.newFixedThreadPool(threads);//新建定长线程池
		CountDownLatch countDownLatch = new CountDownLatch(threads);//新建一个CountDownLatch处理线程关闭，长度等于线程池长度
		long start = Time.getTimeStamp();
		for (int i = 0; i < threads; i++) {
			executorService.execute(new More(countDownLatch, getRequest(i)));
		}
		try {
			countDownLatch.await();
			executorService.shutdown();
		} catch (InterruptedException e) {
			output("线程池关闭失败！", e);
		}
		long end = Time.getTimeStamp();
		output("总计" + threads + "个线程，总计" + (threads * times) + "次请求，共用时：" + Time.getTimeDiffer(start, end) + "秒！");
	}
 
	/**
	 * 保存请求时间到本地文件
	 */
	public static void saveRequestTimes() {
		saveRequestTimes("long");
	}
 
	/**
	 * 保存请求时间到本地文件
	 */
	public static void saveRequestTimes(String name) {
		List<Long> list = new ArrayList<>();
		Collections.copy(list, allTimes);
		Save.saveLongList(list, "long");
	}
 
	/**
	 * 获取请求
	 *
	 * @param i 个
	 * @return
	 */
	private HttpRequestBase getRequest(int i) {
		if (requests == null)
			return request;
		return requests.get(i);
	}
 
	/**
	 * 计算结果
	 *
	 * @param name
	 */
	public static void countOPS(int name) {
		List<String> strings = WriteRead.readTxtFileByLine(LONG_Path + name + FILE_TYPE_LOG);
		int size = strings.size();
		output("数据量：" + size);
		double sum = 0;
		for (int i = 0; i < size; i++) {
			double time = changeStringToInt(strings.get(i));
			if (time > 2000) continue;
			sum += time;
		}
		output("时间总和：" + sum);
		double v = 1000 / sum * size * name;
		output("QPS：" + v);
	}
 
	/**
	 * 多次执行某个请求，但是不记录日志，记录方法用 loglong
	 * <p>此方法只适应与单个请求的重复请求，对于有业务联系的请求暂时不能适配</p>
	 *
	 * @param request 请求
	 * @param times   次数
	 */
	public static void executeRequest(HttpRequestBase request, int times) {
		List<Long> testTimes = new ArrayList<>();
		ApiLibrary.beforeRequest(request);
		long start1 = Time.getTimeStamp();
		for (int i = 0; i < times; i++) {
			double elapsed_time = 0.0;
			CloseableHttpResponse response = null;// 创建响应对象
			long start = Time.getTimeStamp();
			try {
				response = ClientManage.httpsClient.execute(request);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			long end = Time.getTimeStamp();// 记录结束时间
			long time = end - start;
			try {
				if (response != null) {
					response.close();
				}
			} catch (Exception e2) {
				output("响应关闭失败！", e2);
			}
			testTimes.add(time);
		}
		long end1 = Time.getTimeStamp();
		output("执行了" + times + "次请求，花费时间" + Time.getTimeDiffer(start1, end1) + "秒！");
		allTimes.addAll(testTimes);
	}
 
	/**
	 * 多线程类
	 */
	class More implements Runnable {
		private CountDownLatch countDownLatch;
		private HttpRequestBase request;
 
		public More(CountDownLatch countDownLatch, HttpRequestBase request) {
			this.countDownLatch = countDownLatch;
			this.request = request;
		}
 
		@Override
		public void run() {
			try {
				executeRequest(request, times);
			} finally {
				countDownLatch.countDown();
			}
		}
	}
}
```
--------------------分割线-----------------------

之前写过一个httpclient多线程并发类的代码，今天分享的新版主要增加了对多个不同的请求进行并发测试的功能，具体获取请求对象的方法在其他文章分享过了这里就不再贴代码了（包括cookies和header设置以及并发类的使用）。分享代码供大家参考。

```
package com.fission.source.source;
 
import org.apache.http.client.methods.HttpRequestBase;
 
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
public class Concurrent extends ApiLibrary {
	public HttpRequestBase request;
	public int threads;
	public int times;
	public List<HttpRequestBase> requests;
 
	/**
	 * 多请求单线程多次请求方法
	 *
	 * @param requests 请求列表
	 * @param times    单个请求的请求次数
	 */
	public Concurrent(List<HttpRequestBase> requests, int times) {
		this.requests = requests;
		this.times = times;
		this.threads = requests.size();
	}
 
	/**
	 * 单请求多线程多次任务构造方法
	 *
	 * @param request 被执行的请求
	 * @param threads 线程数
	 * @param times   每个线程运行的次数
	 */
	public Concurrent(HttpRequestBase request, int threads, int times) {
		this.request = request;
		this.threads = threads;
		this.times = times;
	}
 
	/**
	 * 执行多线程任务
	 */
	public void excuteTreads() {
		ExecutorService executorService = Executors.newFixedThreadPool(threads);
		CountDownLatch countDownLatch = new CountDownLatch(threads);
		Date start = new Date();
		for (int i = 0; i < threads; i++) {
			executorService.execute(new More(countDownLatch, getRequest(i)));
		}
		try {
			countDownLatch.await();
			executorService.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Date end = new Date();
		output("总计" + threads + "个线程，总计" + (threads * times) + "次请求，共用时：" + getTimeDiffer(start, end) + "秒！");
		saveRequestTimes();
	}
 
	/**
	 * 保存请求时间到本地文件
	 */
	public static void saveRequestTimes() {
		StringBuffer buffer = new StringBuffer();
		int length = testTimes.size();
		for (int i = 0; i < length; i++) {
			if (i == 0) {
				buffer.append(testTimes.get(0));
				continue;
			}
			buffer.append(LINE + testTimes.get(i));
		}
		logLong(buffer.toString());
	}
 
	/**
	 * 获取请求
	 *
	 * @param i 个
	 * @return
	 */
	private HttpRequestBase getRequest(int i) {
		if (requests == null)
			return request;
		return requests.get(i);
	}
 
	/**
	 * 多线程类
	 */
	class More implements Runnable {
		private CountDownLatch countDownLatch;
		private HttpRequestBase request;
 
		public More(CountDownLatch countDownLatch, HttpRequestBase request) {
			this.countDownLatch = countDownLatch;
			this.request = request;
		}
 
		@Override
		public void run() {
			try {
				executeRequest(request, times);
			} finally {
				countDownLatch.countDown();
			}
		}
 
	}
}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>