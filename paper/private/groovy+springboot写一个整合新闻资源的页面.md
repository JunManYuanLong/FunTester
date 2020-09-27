# groovy+springboot写一个整合新闻资源的页面
本人在使用spring boot写测试服务的过程中，遇到了一些需要将数据展示给测试人员的问题，在简单学习了html知识之后，终于写到了一个表格版的数据展示方案。顺便用爬虫爬取了几个新闻门户的最新新闻做个页面，分享一下。

本次包括爬虫和spring boot里面的代码，全部采用了groovy。

[传送门](http://blog.fv1314.xyz/spider/it)

爬虫脚本：

```
package com.faner.spider
 
 
import com.fun.httpclient.FanLibrary
import com.fun.utils.Regex
import net.sf.json.JSONObject
 
class News extends FanLibrary {
    static JSONObject getItNews() {
        def response = getHttpResponse(getHttpGet("https://***/")).getString("content")
        def all = Regex.regexAll(response, "<li>\\s+<str.*\\s+.*\\s+.*</li>")
        def object = new JSONObject()
        all.each { news ->
            news = news.replaceAll("\\s", EMPTY)
            if (!news.contains("广告")) {
                def url = Regex.regexAll(news, "https:.*htm")[0]
                def title = Regex.regexAll(news, "blank\".*>*.*>.*</a>")[0]
                title = title.substring(7).replaceAll("<.+?>", EMPTY)
                object.put(title, url)
            }
        }
        return object;
    }
    static JSONObject getSina() {
        def object = new JSONObject()
        def response = getHttpResponse(getHttpGet("https://***/")).getString("content")
        def all = Regex.regexAll(response, "<a target=\"_blank\" href=\"https://.*?</a>")
        all.each { line ->
            def url = Regex.getRegex(line, "<a target=\"_blank\" href=\".*?\"")
            def title = Regex.getRegex(line, ">.*?<")
            if (!url.contains("weibo")) {
                object.put(title,url)
            }
        }
        return object;
    }
}
```
下面是接口方法：


```
@RequestMapping("/it")
    @ResponseBody
    String getNews() {
        def news = News.getItNews()
        def sina = News.getSina()
        news.putAll(sina)
        List<List<Object>> list = new ArrayList<>()
        List<Object> title = Arrays.asList("新闻标题", "新闻链接")
        list.add(title)
        def keys = news.keys()
        keys.each { key ->
            def s = new String(key.toString().getBytes(), UTF_8)
            def ss = new String(news.get(key).toString().getBytes(), UTF_8)
            ss = "<a href=\"" + ss + "\">传送门</a>"
            list.add(Arrays.asList(s, ss))
        }
        return WriteHtml.createWebReport(list, "新闻接口").toString()
    }
```
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>