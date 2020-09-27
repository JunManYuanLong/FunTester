# selenium2java利用mysq解决向浏览器插入cookies时token过期问题
本人在学习selenium2java中通过浏览器插入cookies模拟用户登录的时候，发现一个问题，就是token值过期的问题，后来学习了selenium2java连接数据库后找到了一个更好的解决方案。每次插入cookies的时候总是从数据库拿到最新的token，这样就完美解决了过期的问题。

这个是我登录后从浏览器拿到的cookies：

>[Automatic_login=18436035355%7Ce3ceb5881a0a1fdaad01296d7554868d%7CStudent; expires=星期二, 21 三月 2017 01:59:55 CST; path=/;
 domain=www.dz101.com, Hm_lvt_52b97b391587eb6d3e582caa097d6f91=1489471192; expires=星期三, 14 三月 2018 01:59:56 CST; path=/; 
 domain=.dz101.com, MyName=18436035355; expires=星期二, 21 三月 2017 01:59:54 CST; path=/; 
 domain=www.dz101.com, User_token_Session=f24f16d472b222271e6dcf27077231b9; expires=星期二, 21 三月 2017 01:59:54 CST; path=/; 
 domain=www.dz101.com, User_identity_Session=1; expires=星期二, 21 三月 2017 01:59:54 CST; path=/; 
 domain=www.dz101.com, PHPSESSID=1s2uvdrj33d72qvj2qlqojhsl7; path=/; domain=www.dz101.com, Hm_lpvt_52b97b391587eb6d3e582caa097d6f91=1489471196;
 path=/; domain=.dz101.com]

经过分析和尝试发现，其实只有插入MyName和User_token_Session这两项就可以了。
 
下面是我成功插入后的cookies：


>[Hm_lvt_52b97b391587eb6d3e582caa097d6f91=1489472871; expires=星期三, 14 三月 2018 02:27:53 CST; path=/; 
domain=.dz101.com, MyName=18436035355; path=/; 
domain=www.dz101.com, User_token_Session=f24f16d472b222271e6dcf27077231b9; path=/; 
domain=www.dz101.com, PHPSESSID=uahgb7ll1405h0p5jhloipt7a2; path=/; 
domain=www.dz101.com, Hm_lpvt_52b97b391587eb6d3e582caa097d6f91=1489472873; path=/; domain=.dz101.com]

下面是我写的代码

```
//向浏览器添加cookies
public static void addCookies(WebDriver driver, String mobile) throws ClassNotFoundException, SQLException, IOException {
Cookie a = new Cookie("MyName", mobile);
Cookie b = new Cookie("User_token_Session", MySql.getNewToken(mobile));
driver.manage().addCookie(a);
driver.manage().addCookie(b);
driver.navigate().refresh();
//查看浏览器cookies
// Set<Cookie> cooies = driver.manage().getCookies();
// System.out.println(cooies);
}
```
下面是getNewToken(String mobile))方法：


```
    public static String getNewToken(String mobile) throws ClassNotFoundException, SQLException, IOException {
// 加载驱动程序
        Class.forName(driver);
// 连接数据库
        Connection conn = DriverManager.getConnection(url, user, password);
        if (!conn.isClosed())
            System.out.println("Succeeded connecting to the Database!");
//statement用来执行SQL语句
        Statement statement = conn.createStatement();
// 要执行的SQL语句
        String sql = "select * from users where mobile = " + mobile;
        output(sql);
// 结果集
        ResultSet rs = statement.executeQuery(sql);
        System.out.println("查询结果如下所示:");
        String id = null;
        while (rs.next()) {
            // 选择列数据
            id = rs.getString("id");
            // 输出结果
            System.out.println(rs.getString("id") + "\t" + id);
        }
        rs.close();
        String sql2 = "select * from users_token where uid = " + id + " ORDER BY create_time DESC LIMIT 1";
        ResultSet rs2 = statement.executeQuery(sql2);
        String token = null;
        System.out.println("查询结果如下所示:");
        while (rs2.next()) {
            token = rs2.getString(token);
            output(token);
            saveToFile(getNow() + token, "runlog.log", false);
        }
        conn.close();
        return token;
    }
```

## 往期文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
8. [将json数据格式化输出到控制台](https://mp.weixin.qq.com/s/2IPwvh-33Ov2jBh0_L8shA)
9. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
10. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
11. [Mac+httpclient高并发配置实例](https://mp.weixin.qq.com/s/r4a-vGz0pxeZBPPH3phujw)
12. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)

## [公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>