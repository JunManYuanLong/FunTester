# httpclient爬虫爬取汉字拼音等信息（关键信息未隐去）


下面是使用httpclient爬虫爬取某个网站的汉字相关信息的实践代码，中间遇到了一些字符格式的问题。之前被同事见过用html解析类来抓取页面信息，而不是像我现在用正则，经常尝试，效果并不好，毕竟页面放爬虫还是非常好做的。在本次实践中，就遇到了相关的难点，所以还是才去了正则提取的方式。分享代码，供大家参考。关键信息并未隐去。

```
public static void main(String[] args) throws SQLException {
        DEFAULT_CHARSET = GB2312;
        List<String> list = WriteRead.readTxtFileByLine(LONG_Path + "word.log");
        list.forEach(py -> {
            getPYAndWord(py);
        });
        testOver();
    }
 
    public static void getPYAndWord(String py) {
        output(py);
        String url = "http://zd.diyifanwen.com/zidian/py/" + py + ".htm";
        HttpGet httpGet = getHttpGet(url);
        JSONObject response = getHttpResponse(httpGet);
//            output(response);
        String content = response.getString("content");
        String all = new String(content.getBytes(UTF_8), UTF_8);
        List<String> regexAll = new ArrayList<>();
        List<String> alllist = regexAll(all, "http://zd.d.*?>[\\u4e00-\\u9FFF]<");
        output(alllist.size());
        alllist.forEach(line -> {
            String murl = regexAll(line, "http://zd.diyifanwen.com/zidian/\\w/\\d+.htm").get(0);
            String mword = regexAll(line, ">[\\u4e00-\\u9fa5]<").get(0);
            regexAll.add(mword);
            output(murl, mword);
            String sql = "INSERT INTO chinese_dictionary_word (word,url) VALUES (\"%s\",\"%s\");";
            sql = String.format(sql, mword.replaceAll("<|>", EMPTY), murl);
            output(sql);
            MySqlTest.sendWork(sql);
        });
        String str = regexAll.toString().replaceAll("<|>|\\[|\\]", EMPTY);
        String sql = "INSERT INTO chinese_dictionary_py_word (py,words) VALUES (\"%s\",\"%s\");";
        sql = String.format(sql, py, str);
        output(sql);
        MySqlTest.sendWork(sql);
        sleep(2);
    }
 
    /**获取拼音列表
     * @return
     */
    public static String getPY() {
        String url = "http://zd.diyifanwen.com/zidian/py/";
        HttpGet httpGet = getHttpGet(url);
        JSONObject response = getHttpResponse(httpGet);
//        output(response);
        String content = response.getString("content");
        byte[] bytes = content.getBytes(UTF_8);
        String all = new String(bytes, UTF_8);
        Log.log("content", all);
        return all;
    }
 
    /**获取所有首字母和拼音
     * @param all
     */
    public static void getAllPY(String all) {
        List<String> list = regexAll(all, "<dt class=\"pyTitle\">拼音首字母\\w+</dt>" + LINE + ".+/dd>");
            list.forEach(s -> {
                int num = s.indexOf("拼音首字母");
                String first = s.substring(num + 5, num + 6);
                List<String> list1 = regexAll(s, "http://zd.diyifanwen.com/zidian/py/\\w+.htm");
                list1.forEach(str -> {
                    int one = str.indexOf("/py/");
                    int two = str.lastIndexOf(".");
                    String second = str.substring(one + 4, two);
                    String sql = "INSERT INTO chinese_dictionary_py (first_word,all_word) VALUES (\"%s\",\"%s\");";
                    String sqlEnd = String.format(sql, first, second);
                    MySqlTest.sendWork(sqlEnd);
                });
            });
    }
 
    /**检查拼音是否全部获取到
     * @param all
     */
    public static void checkPY(String all) {
        List<String> list = regexAll(all, "zidian/py/\\w+.htm");
        list.forEach(str -> {
            int one = str.indexOf("/py/");
            int two = str.lastIndexOf(".");
            String second = str.substring(one + 4, two);
            output(second);
            String sql = "SELECT * FROM chinese_dictionary_py WHERE all_word = \"%s\";";
            String sq = String.format(sql, second);
            ResultSet resultSet = MySqlTest.excuteQuerySql(sq);
            try {
                if (!resultSet.next()) output(sq);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
 
    /**从数据库中查找当前获取的拼音并存储到文件中
     * @throws SQLException
     */
    public static void getAllPY() throws SQLException {
        List<String> word = new ArrayList<>();
        ResultSet resultSet = MySqlTest.excuteQuerySql("SELECT all_word FROM chinese_dictionary_py;");
        while (resultSet.next()) {
            String string = resultSet.getString(1);
            word.add(string);
        }
        Save.saveStringList(word, "word");
    }
```

结果如图：

![](/blog/pic/20180827161359440.png)
![](/blog/pic/20180827161425591.png)

对于汉字具体的释义内容并未爬取，连接进行了保存。



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

