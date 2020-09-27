# httpclient爬虫爬取电影信息和下载地址实例（编码格式转化）


本次更新主要解决了老旧页面下载链接可能是迅雷和ftp格式的，还有就是去重，因为每一页有一个推荐列表，里面也会有相应的详情链接，还有兼容了另外的页面格式，更新了两个方法：

```
public static void spider(int pa) {
        List<String> page = getPage(pa);
        String[] abc = "http://www.***.net/ys/20170620/37704.htm, http://www.***.net/ys/20170727/38028.htm, http://www.***.net/ys/20170810/38113.htm, http://www.***.net/ys/20170703/37769.htm, http://www.***.net/ys/20170615/37680.htm, http://www.***.net/ys/20170615/37678.htm, http://www.***.net/ys/20170727/38027.htm, http://www.***.net/ys/20170802/38060.htm, http://www.***.net/ys/20170515/37385.htm, http://www.***.net/ys/20170725/38001.htm, http://www.***.net/ys/20170608/37614.htm, http://www.***.net/ys/20170802/38059.htm, http://www.***.net/ys/20170629/37742.htm, http://www.***.net/ys/20170512/37323.htm, http://www.***.net/ys/20170426/37219.htm, http://www.***.net/ys/20170727/38026.htm, http://www.***.net/ys/20170730/38046.htm, http://www.***.net/ys/20170804/38082.htm, http://www.***.net/ys/20170714/37848.htm, http://www.***.net/ys/20180819/40982.htm, http://www.***.net/ys/20180819/40981.htm, http://www.***.net/ys/20180818/40980.htm, http://www.***.net/ys/20180818/40979.htm, http://www.***.net/ys/20180818/40978.htm, http://www.***.net/ys/20180818/40977.htm, http://www.***.net/ys/20180817/40975.htm, http://www.***.net/ys/20180817/40974.htm".split(", ");
        List<String> list = Arrays.asList(abc);
        page.removeAll(list);
        output(page.size());
        Set<String> truelist = new HashSet<>();
        page.forEach(l -> truelist.add(l));
        truelist.forEach(p -> {
            try {
                getMovieInfo(p);
                sleep(getRandomInt(3) + 3);
            } catch (Exception e) {
                output(p);
            }
        });
    }
 
    public static void spider(String text) {
        List<String> page = getPage(text);
        Set<String> truelist = new HashSet<>();
        page.forEach(l -> truelist.add(l));
        truelist.forEach(p -> {
            try {
                getMovieInfo(p);
                sleep(getRandomInt(3));
            } catch (Exception e) {
                output(p);
            }
        });
    }
 
    public static List<String> getPage(int page) {
        String url = "http://www.***.net/ys/index_" + page + ".htm";
        if (page == 1) url = "http://www.***.net/ys/";
        output(url);
        HttpGet httpGet = getHttpGet(url);
        JSONObject response = getHttpResponse(httpGet);
        String content = response.getString("content");
        byte[] bytes = content.getBytes(UTF_8);
        String all = new String(bytes, UTF_8);
        List<String> list = regexAll(all, "http://www.***.net/ys/\\d+/\\d+.htm");
        return list;
    }
 
    public static List<String> getPage(String page) {
        String content = page;
        byte[] bytes = content.getBytes(UTF_8);
        String all = new String(bytes, UTF_8);
        List<String> list = regexAll(all, "http://www.***.net/ys/\\d+/\\d+.htm");
        return list;
    }
 
    public static boolean getMovieInfo(int day, int index) {
//        String url = "http://www.***.net/ys/20180819/40981.htm";
        String url = "http://www.***.net/ys/" + day + "/" + index + ".htm";
        getMovieInfo(url);
        return true;
    }
 
    public static boolean getMovieInfo(String url) {
        HttpGet httpGet = getHttpGet(url);
        JSONObject response = getHttpResponse(httpGet);
        String s = response.getString("content");
        if (s.contains("您查询的内容不存在，请返回首页重新搜索")) return false;
        byte[] bytes = s.getBytes(UTF_8);
        String all = new String(bytes, UTF_8);
        String name = EMPTY, tname = EMPTY, year = EMPTY, language = EMPTY, date = EMPTY, score = EMPTY, length = EMPTY, author = EMPTY;
        if (all.contains("◎")) {
            int i = all.indexOf("◎");
            int i1 = all.indexOf("<hr");
            String info = s.substring(i, i1);
            name = getInfo(info, "片　　名　");
            tname = getInfo(info, "译　　名　");
            year = getInfo(info, "年　　代　");
            language = getInfo(info, "语　　言　");
            date = getInfo(info, "上映日期　");
            score = getInfo(info, "豆瓣评分　");
            length = getInfo(info, "片　　长　");
            author = getInfo(info, "导　　演　");
        } else {
            name = getInfo(all, "<title>");
            if (name.contains("_")) name = name.substring(0, name.indexOf("_"));
            length = getInfo(all, "片长: ");
            date = getInfo(all, "上映日期: ");
            author = getInfo(all, "导演: ");
            language = getInfo(all, "语言: ");
        }
        List<String> magnets = regexAll(all, "magnet:.+?>");
        List<String> ed2ks = regexAll(all, "ed2k:.+?>");
        if (ed2ks.size() == 0) ed2ks = regexAll(all, "ftp://.+?>");
        if (ed2ks.size() == 0) ed2ks = regexAll(all, "thunder://.+?>");
        List<String> pans = regexAll(all, "http(s)*://pan.baidu.com/.+?</td>");
        String sql = "INSERT INTO movie (name,tname,year,language,date,score,length,author,magnet,ed2k,pan) VALUES(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\");";
        sql = String.format(sql, name, tname, year, language, date, score, length, author, magnets.toString().replace("\"", EMPTY), ed2ks.toString().replace("\"", EMPTY), pans.toString().replace("\"", EMPTY));
        if (ed2ks.size() != 0) MySqlTest.sendWork(sql);
        output(magnets.toString().length(), ed2ks.toString().length(), pans.toString().length());
        output(sql);
        return true;
    }
 
    public static String getInfo(String text, String start) {
        String value = EMPTY;
        List<String> nameinfo = regexAll(text, start + ".+?<");
        if (nameinfo.size() > 0) value = nameinfo.get(0).replace(start, EMPTY).replace("<", EMPTY);
        return value;
    }
```
----------------------------------分割线-------------------------------------------------

本人使用httpclient爬虫过程中，想爬取关注的一个电影网站的下载地址。在经过尝试之后，终于成功爬取了几百部热门电影的信息和下载地址（电驴和磁力链接）。中间遇到了编码格式，正则匹配不一致，还有重复链接过滤等问题，也都一一搞定。附上代码，供大家参考。

关键信息隐藏，思路供大家参考：先去访问列表页，拿到详情页的链接，去重之后去访问详情页，拿到相关信息和下载地址，存储到数据库中。


```
public class MyTest extends ApiLibrary {
    public static void main(String[] args) {
        DEFAULT_CHARSET = GB2312;
        for (int i = 0; i < 10; i++) {
            spider(1);
        }
 
        testOver();
    }
 
    public static void spider(int pa) {
        String[] abc = "http://www.***.net/ys/20170620/37704.htm, http://www.***.net/ys/20170727/38028.htm, http://www.***.net/ys/20170810/38113.htm, http://www.***.net/ys/20170703/37769.htm, http://www.***.net/ys/20170615/37680.htm, http://www.***.net/ys/20170615/37678.htm, http://www.***.net/ys/20170727/38027.htm, http://www.***.net/ys/20170802/38060.htm, http://www.***.net/ys/20170515/37385.htm, http://www.***.net/ys/20170725/38001.htm, http://www.***.net/ys/20170608/37614.htm, http://www.***.net/ys/20170802/38059.htm, http://www.***.net/ys/20170629/37742.htm, http://www.***.net/ys/20170512/37323.htm, http://www.***.net/ys/20170426/37219.htm, http://www.***.net/ys/20170727/38026.htm, http://www.***.net/ys/20170730/38046.htm, http://www.***.net/ys/20170804/38082.htm, http://www.***.net/ys/20170714/37848.htm, http://www.***.net/ys/20180819/40982.htm, http://www.***.net/ys/20180819/40981.htm, http://www.***.net/ys/20180818/40980.htm, http://www.***.net/ys/20180818/40979.htm, http://www.***.net/ys/20180818/40978.htm, http://www.***.net/ys/20180818/40977.htm, http://www.***.net/ys/20180817/40975.htm, http://www.***.net/ys/20180817/40974.htm".split(", ");
        List<String> list = Arrays.asList(abc);
        page.removeAll(list);
        Set<String> truelist = new HashSet<>();
        page.forEach(l -> truelist.add(l));
        truelist.forEach(p -> {
            try {
                getMovieInfo(p);
                sleep(getRandomInt(3));
            } catch (Exception e) {
                output(p);
            }
        });
    }
 
    public static List<String> getPage(int page) {
        String url = "http://www.***.net/ys/index_" + page + ".htm";
        if (page == 1) url = "http://www.***.net/ys/";
        output(url);
        HttpGet httpGet = getHttpGet(url);
        JSONObject response = getHttpResponse(httpGet);
        String content = response.getString("content");
        output(content);
        byte[] bytes = content.getBytes(UTF_8);
        String all = new String(bytes, UTF_8);
        List<String> list = regexAll(all, "http://www.***.net/ys/\\d+/\\d+.htm");
        return list;
    }
 
    public static boolean getMovieInfo(int day, int index) {
//        String url = "http://www.***.net/ys/20180819/40981.htm";
        String url = "http://www.***.net/ys/" + day + "/" + index + ".htm";
        HttpGet httpGet = getHttpGet(url);
        JSONObject response = getHttpResponse(httpGet);
        String s = response.getString("content");
        if (s.contains("您查询的内容不存在，请返回首页重新搜索")) return false;
        byte[] bytes = s.getBytes(UTF_8);
        String all = new String(bytes, UTF_8);
        int i = all.indexOf("◎");
        int i1 = all.indexOf("<hr");
        String info = s.substring(i, i1);
        String name = getInfo(info, "片　　名　");
        String tname = getInfo(info, "译　　名　");
        String year = getInfo(info, "年　　代　");
        String language = getInfo(info, "语　　言　");
        String date = getInfo(info, "上映日期　");
        String score = getInfo(info, "豆瓣评分　");
        String length = getInfo(info, "片　　长　");
        String author = getInfo(info, "导　　演　");
        List<String> magnets = regexAll(all, "magnet:.+?>");
        List<String> ed2ks = regexAll(all, "ed2k:.+?>");
        List<String> pans = regexAll(all, "http(s)*://pan.baidu.com/.+?</td>");
        String sql = "INSERT INTO movie (name,tname,year,language,date,score,length,author,magnet,ed2k,pan) VALUES(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\");";
        sql = String.format(sql, name, tname, year, language, date, score, length, author, magnets.toString().replace("\"", EMPTY), ed2ks.toString().replace("\"", EMPTY), pans.toString().replace("\"", EMPTY));
        MySqlTest.sendWork(sql);
        return true;
    }
 
    public static boolean getMovieInfo(String url) {
        HttpGet httpGet = getHttpGet(url);
        JSONObject response = getHttpResponse(httpGet);
        String s = response.getString("content");
        if (s.contains("您查询的内容不存在，请返回首页重新搜索")) return false;
        byte[] bytes = s.getBytes(UTF_8);
        String all = new String(bytes, UTF_8);
        int i = all.indexOf("◎");
        int i1 = all.indexOf("<hr");
        String info = s.substring(i, i1);
        String name = getInfo(info, "片　　名　");
        String tname = getInfo(info, "译　　名　");
        String year = getInfo(info, "年　　代　");
        String language = getInfo(info, "语　　言　");
        String date = getInfo(info, "上映日期　");
        String score = getInfo(info, "豆瓣评分　");
        String length = getInfo(info, "片　　长　");
        String author = getInfo(info, "导　　演　");
        List<String> magnets = regexAll(all, "magnet:.+?>");
        List<String> ed2ks = regexAll(all, "ed2k:.+?>");
        List<String> pans = regexAll(all, "http(s)*://pan.baidu.com/.+?</td>");
        String sql = "INSERT INTO movie (name,tname,year,language,date,score,length,author,magnet,ed2k,pan) VALUES(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\");";
        sql = String.format(sql, name, tname, year, language, date, score, length, author, magnets.toString().replace("\"", EMPTY), ed2ks.toString().replace("\"", EMPTY), pans.toString().replace("\"", EMPTY));
        MySqlTest.sendWork(sql);
        output(magnets.toString().length(), ed2ks.toString().length(), pans.toString().length());
        output(sql);
        return true;
    }
 
    public static String getInfo(String text, String start) {
        String value = EMPTY;
        List<String> nameinfo = regexAll(text, start + ".+?<");
        if (nameinfo.size() > 0) value = nameinfo.get(0).replace(start, EMPTY).replace("<", EMPTY);
        return value;
    }
 
}
```
下面是数据库存储的截图：
![](/blog/pic/20180821115800959.png)


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

# [点击查看公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)


<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>