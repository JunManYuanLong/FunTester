# 如何计算double类型的百分比

本人在接口测试的过程中，遇到一个需求，统计接口请求成功的百分比，以便后期做统计，因为我的请求次数和成功次数都是以int类型存储的，百分比要以double类型两位小数存储，在网上到了一些办法，很多都是转的string类型，然后截取或者使用固定格式去转化，个人觉得并不可取，看起来会比较繁琐。自己想了一些，决定用强制类型转换来处理这个问题，下面是自己写的方法。分享出来供大家参考。

```
/**
	 * 获取一个百分比，两位小数
	 * 
	 * @param total
	 *            总数
	 * @param piece
	 *            成功数
	 * @return 百分比
	 */
	public double getPercent(int total, int piece) {
		if (total == 0) {
			return 0.00;
		}
		if (total == piece) {
			return 100.00;
		}
		int s = (int) (piece * (1.0) / total * 10000);
		double result = s * 1.0 / 100;
		return result;
	}
```

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>