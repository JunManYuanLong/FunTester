# 如何优雅地屏蔽掉Google搜索结果中视频、新闻、图片等结果
本人在使用Google搜索的时候，经常发现一些相关性的YouTube视频还有Google自家产品的搜索结果出现，在使用了脚本优化之后，十分不美观。在看了Adblock Plus的自定义过滤器文档之后，终于有了突破。

下面分享一下过程，希望能有所帮助。

下面是Google搜索“我的”一次的结果：
![](/blog/pic/20181127112450795.png)

下面是页面结构：

![](/blog/pic/20181127112706817.png)

经过观察，决定屏蔽id=“acid_src”的元素就好了，下面是元素屏蔽规则例子：


```
<div class="textad">
Cheapest tofu, only here and now!
</div>
<div id="sponsorad">
Really cheap tofu, click here!
</div>
<textad>
Only here you get the best tofu!
</textad>
```

> [上面代码中的第一则广告是在一个 class 属性为“textad”的 div 容器内。过滤规则 ##.textad 。 这里的 ## 表明这是一条元素隐藏规则，剩下的就是定义需要隐藏元素的选择器，同样的，您可以通过他们的 id 属性来隐藏 ###sponsorad 会隐藏第二个广告。您不需要指定元素的名称， 过滤规则 ##textad 同样也可以。您也可以仅指定要阻挡的元素名称来隐藏，例如：{4} 可以隐藏第三则广告。](https://adblockplus.org/zh_CN/filters)

下面是搜索结果：

![](/blog/pic/20181127113220917.png)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>