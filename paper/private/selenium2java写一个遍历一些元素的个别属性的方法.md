# selenium2java写一个遍历一些元素的个别属性的方法
本人在学习selenium2java的时候，遇到一个找到所有人的成绩的问题，url规则很简单，就自己写了一个遍历的方法。分享出来，供大家参考。


```
for(int i=100;i<150;i++){
			driver.get(url + i);
			if (exists(driver, By.xpath("html/body/div[1]/div/div[1]/p[1]/span[1]"))) {
				String time = getTextByXpath(driver, "html/body/div[1]/div/div[1]/p[1]/span[1]");
				saveToFile(i+smile+time, false);
				output(i+smile+time);
			}else{
				saveToFile(i+sad+"无效成绩！", false);
				output(i+sad+"无效成绩！");
			}
	
		}
```

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>