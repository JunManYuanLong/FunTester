# 多线程并发时，list 集合add 方法数据丢失原因解答
最近我在使用 httpclient 多线程并发测试的时候，因为数据量比较大，所以放弃了数据库存储，写在本地文件中，方案是在执行的过程中把执行的数据写到一个 list 集合里面，然后执行完之后把所有的数据写在一个 log 文件当中，然后用 Python+plotly 进行数据处理。但是在使用过程中，发现得到的数据总是不够，有的竟然是 null。而且每次都这样，特别在是高并发的情况下会发生更多错误。

经过查阅相关资料，发现是 list 集合本身特性导致的，list 对象线程不安全。故而需要用线程安全的集合Vector 来执行数据的存储。想明白了方案就简单多了，放一下代码，各位参考。


```
public static Vector<Double> testTimes = new Vector<>();

int length = testTimes.size();
		for (int i = 0; i < length; i++) {
			if (testTimes.get(i) == null) {
				continue;
			}
			if (i == 0) {
				buffer.append(testTimes.get(0));
				continue;
			}
			buffer.append(LINE + testTimes.get(i));
		}
		logLong(WorkPath + "long.log", buffer.toString());
		
		public static void logLong(String path, String content) {
		File dirFile = new File(path);
		if (dirFile.exists()) {
			dirFile.delete();
		}
		try {
			dirFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileWriter fileWriter = new FileWriter(path, true);
			BufferedWriter bw1 = new BufferedWriter(fileWriter);
			bw1.write(content);// 将内容写到文件中
			bw1.flush();// 强制输出缓冲区内容
			bw1.close();// 关闭流
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>