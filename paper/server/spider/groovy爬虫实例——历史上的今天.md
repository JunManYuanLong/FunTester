# groovy爬虫实例——历史上的今天

最近做了一个历史上今天的爬虫程序，跟历史天气数据源一致，数据量比较小，几十秒就爬完了。中间遇到一些问题，一起分享出来供大家参考。本项目源码和相关数据已经上传到了github，有兴趣的朋友可以去看看，会不定期更新。

[git传送门](https://github.com/Fhaohaizi/fan)

1. get请求发送sql语句不能过长：我是做爬虫里面把sql拼好，发送到数据库存储服务上，之前一直用的get请求，由于这次内容较多，超过了最大长度限制，导致报错。故改为post请求，且兼容了get请求方式。

2. 不明确的数据类型：某个年份的某一天事件不唯一的话，json格式的value是array，如果唯一则是一个json。在处理这个数据的时候才去了正则匹配。总结起来，在提取相关接口数据的时候，正则最好用。

3. 拼接月份的时候有点复杂，直接写了一个省事儿的方法，如果各位有简单好用的，望不吝赐教。


```
static void main(String[] args) {
        DEFAULT_CHARSET = GBK;
 
        for (int i in 1..12) {
            for (int j in 1..31) {
                if (i == 2 && (j == 30 || j == 31)) continue
                if ((i in [4, 6, 9, 11]) && j == 31) continue
                def month = i > 9 ? i + EMPTY : "0" + i;
                def day = j > 9 ? j + EMPTY : "0" + j;
                def date = month + "-" + day
                getInfo(date)
            }
        }
        testOver()
    }
    static getInfo(String date) {
        def url = "http://tools.***.com/his/" + date.replace("-", EMPTY) + "_c.js"
        def all = FanRequest.isGet()
                .setUri(url)
                .getResponse()
                .getString("content")
                .substring(8)
                .replace(";", EMPTY)
                .replaceAll("(&nbsp)+", EMPTY)
                .replaceAll("\\t", EMPTY)
                .replace("##", EMPTY)
                .replaceAll(SPACE_1, EMPTY)
        def json = JSONObject.fromObject(all)
        def keys = json.keySet()
        keys.each { key ->
            def s = json.get(key).toString()
            def all1 = Regex.regexAll(s, "\\{\"title.+?\\}")
            for (int i in 0..all1.size() - 1) {
                def info = all1.get(i)
                def inf = JSONObject.fromObject(info.toString())
                def title = inf.getString("title")
                def keyword = inf.getString("keyword")
                def content = inf.getString("content")
                def alt = inf.getString("alt")
                String sql = "INSERT INTO today_histroy (date,title,keyword,content,alt) VALUES (\"%s\",\"%s\",\"%s\",\"%s\",\"%s\");"
                sql = String.format(sql, key + "-" + date, title, keyword, content.replace("　　", EMPTY), alt)
                MySqlTest.sendWork(sql)
            }
        }
    }
```


### 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [单点登录性能测试方案](https://mp.weixin.qq.com/s/sv8FnvIq44dFEq63LpOD2A)

### 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
- [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)

### 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
- [JVM的对象和堆](https://mp.weixin.qq.com/s/iNDpTz3gBK3By_bvUnrWOA)
