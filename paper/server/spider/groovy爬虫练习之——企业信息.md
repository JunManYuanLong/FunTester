# groovy爬虫练习之——企业信息



> 获取数据的方法依然采取了regex正则匹配的方法，请求框架采用了java，爬虫语言是groovy，本地拼接好sql语句，发送到mysql服务端，完成存储。

代码如下：

```
package com.fan
 
import com.fantest.httpclient.FanLibrary
import com.fantest.mysql.MySqlTest
import com.fantest.utils.Regex
import net.sf.json.JSONObject
 
class Company extends FanLibrary {
    static void main(String[] args) {
        for (def i in 1..1060) {
            getPage(i)
//                getInfo("/eportal/ui?pageId=307900&t=toDetail&ZSBH=D311056737")
        }
        testOver()
    }
 
    static getPage(int page) {
        def url = "http://www.***.gov.cn/eportal/ui?pageId=307900"
        def params = new JSONObject()
        params.put("filter_LIKE_QYMC", EMPTY)
        params.put("filter_LIKE_YYZZZCH", EMPTY)
        params.put("filter_LIKE_ZSBH", EMPTY)
        params.put("filter_LIKE_XXDZ", EMPTY)
        params.put("currentPage", page)
        params.put("pageSize", 15)
        params.put("OrderByField", EMPTY)
        params.put("OrderByDesc", EMPTY)
        def response = getHttpResponse(getHttpPost(url, params))
        def s = response.getString("content")
        def all = Regex.regexAll(s, "<td s.*?浏览")
        for (int i = 1; i < all.size(); i++) {
            def get = all.get(i)
            def regex = Regex.getRegex(get, "href=\".*?\"").replace("amp;", EMPTY)
            getInfo(regex)
            sleep(3)
        }
        return response;
    }
 
    static getInfo(String url) {
        try {
            url = "http://www.***.gov.cn" + url;
            def response = getHttpResponse(getHttpGet(url))
            def content = response.getString("content")
            def all = Regex.regexAll(content, "<td class=\"label\".*?\n.*\n.*\n.*\n.*\n.*")
            def name = all.get(0).replaceAll("<.*?>", EMPTY).replaceAll("(\n| )", EMPTY).split("：")[1]
            def adress = all.get(1).replaceAll("<.*?>", EMPTY).replaceAll("(\n| )", EMPTY).split("：")[1]
            def money = all.get(2).replaceAll("<.*?>", EMPTY).replaceAll("(\n| )", EMPTY).split("：")[1]
            def sid = all.get(3).replaceAll("<.*?>", EMPTY).replaceAll("(\n| )", EMPTY).split("：")[1]
            def type = all.get(4).replaceAll("<.*?>", EMPTY).replaceAll("(\n| )", EMPTY).split("：")[1]
            def man = all.get(5).replaceAll("<.*?>", EMPTY).replaceAll("(\n| )", EMPTY).split("：")[1]
            def paper = all.get(6).replaceAll("<.*?>", EMPTY).replaceAll("(\n| )", EMPTY).split("：")[1]
            def level = all.get(7).replaceAll("<.*?>", EMPTY).replaceAll("(\n| )", EMPTY).split("：")[1]
            def gov = all.get(8).replaceAll("<.*?>", EMPTY).replaceAll("(\n| )", EMPTY).split("：")[1]
            def time = all.get(9).replaceAll("<.*?>", EMPTY).replaceAll("(\n| )", EMPTY).split("：")[1]
            def start = time.split("~")[0]
            def end = time.split("~")[1]
            String sql = "INSERT INTO company (name,adress,money,sid,type,man,paper,level,gov,start,end) VALUES (\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\");"
            sql = String.format(sql, name, adress, money, sid, type, man, paper, level, gov, start, end)
            output(sql)
            MySqlTest.sendWork(sql)
        }
        catch (Exception e) {
            output(e)
        }
    }
}
```
第一页的网页结构如下：
![](/blog/pic/20181022101846430.jpeg)
第二页详情页结构如下:
![](/blog/pic/2018102210203567.png)

![20181022101846430.jpeg](https://upload-images.jianshu.io/upload_images/9276255-9cd3e77a5950d82d.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

第二页详情页结构如下:
![2018102210203567.png](https://upload-images.jianshu.io/upload_images/9276255-6f7938c2541eb1ff.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

regex是我自己简单封装的正则匹配的类，代码可以去我码云上面看看

[邀请链接](https://gitee.com/fanapi/tester/invite_link?invite=d3f63470c4601ec1308551fa9ffcd3361c47559ac8870251b7993663152babc59a5d3a335119c07c)

### 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
- [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
- [python plotly处理接口性能测试数据方法封装](https://mp.weixin.qq.com/s/NxVdvYlD7PheNCv8AMYqhg)
- [单点登录性能测试方案](https://mp.weixin.qq.com/s/sv8FnvIq44dFEq63LpOD2A)



### 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
- [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
- [测试之《代码不朽》脑图](https://mp.weixin.qq.com/s/2aGLK3knUiiSoex-kmi0GA)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [优秀软件开发人员的态度](https://mp.weixin.qq.com/s/0uEEeFaR27aTlyp-sm61bA)
- [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [自动化测试解决了什么问题](https://mp.weixin.qq.com/s/96k2I_OBHayliYGs2xo6OA)
- [17种软件测试人员常用的高效技能-上](https://mp.weixin.qq.com/s/vrM_LxQMgTSdJxaPnD_CqQ)
- [17种软件测试人员常用的高效技能-下](https://mp.weixin.qq.com/s/uyWdVm74TYKb62eIRKL7nQ)

### 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)


