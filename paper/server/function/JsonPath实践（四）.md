# JsonPath实践（四）

书接上文和上上文以及上上上文：

- [JsonPath实践（一）](https://mp.weixin.qq.com/s/Cq0_v_ptbGd4f5y8HIsq7w)
- [JsonPath实践（二）](https://mp.weixin.qq.com/s/w_iJTiuQahIw6U00CJVJZg)
- [JsonPath实践（三）](https://mp.weixin.qq.com/s/58A3k0T6dbOkBJ5nRYKDqA)

本期继续将如何处理`json`数组，主要内容是通过正则过滤`json`数组中的数据，以及通过正则校验`json`节点值。

`JSonpath`中的正则语法是通用的，但是使用方法跟`Groovy`非常类似。有兴趣的同学参考：[Java和Groovy正则使用](https://mp.weixin.qq.com/s/DT3BKE3ZcCKf6TLzGc5wbg)。使用`=~`这个标记语法表示正则匹配，然后用前后两个`/`符号表示正则的内容，这一点跟`Groovy`一模一样，还有多了一种忽略大小写的语法，就是在正则语句后面的`/`加上`i`这个字母（暂时没发现其他字母的标记功能）。

## json数据

首先看官方给的`json`数据的`Demo`（我做了一点点修改）：

```Java
JSONObject json = JSON.parseObject("{" +
                "    \"store\": {" +
                "        \"book\": [" +
                "            {" +
                "                \"category\": \"reference\"," +
                "                \"author\": \"Nigel Rees\"," +
                "                \"title\": \"Sayings of the Century\"," +
                "                \"price\": 8.95" +
                "            }," +
                "            {" +
                "                \"category\": \"fiction\"," +
                "                \"author\": \"Evelyn Waugh\"," +
                "                \"title\": \"Sword of Honour\"," +
                "                \"price\": 12.99" +
                "            }," +
                "            {" +
                "                \"category\": \"fiction\"," +
                "                \"author\": \"Herman Melville\"," +
                "                \"title\": \"Moby Dick\"," +
                "                \"isbn\": \"0-553-21311-3\"," +
                "                \"price\": 8.99" +
                "            }," +
                "            {" +
                "                \"category\": \"fiction\"," +
                "                \"author\": \"J. R. R. Tolkien\"," +
                "                \"title\": \"The Lord of the Rings\"," +
                "                \"isbn\": \"0-395-19395-8\"," +
                "                \"price\": 22.99" +
                "            }" +
                "        ]," +
                "        \"bicycle\": {" +
                "            \"color\": \"red\"," +
                "            \"price\": 19.95" +
                "        }" +
                "    }," +
                "    \"expensive\": 10," +
                "    \"ss\": [32,32,4,23]" +
                "}");
```


## 通过正则过滤数组


`jsonpath`：`$..book[?(@.author =~ /.*Rees/)]`

或者使用路径表示：

`jsonpath`：`$.store.book[?(@.author =~ /.*Rees/)]`

* 这里表示倒数第一个对象

代码：

```Java
        Object read = JsonPath.read(json, "$.store.book[?(@.author =~ /.*Rees/)]");
        output(JSONArray.parseArray(read.toString()));
```

等效写法继续省略……

控制台输出：


```shell
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "author":"Nigel Rees",
＞  ① . "price":8.95,
＞  ① . "category":"reference",
＞  ① . "title":"Sayings of the Century"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~

Process finished with exit code 0

```

## 忽略大小写

`jsonpath`：`$..book[?(@.author =~ /.*REES/)]`

或者使用忽略大小写语法：

`jsonpath`：`$.store.book[?(@.author =~ /.*REES/i)]`

* 这里表示倒数第一个对象

代码：

```Java
        Object read = JsonPath.read(json, "$.store.book[?(@.author =~ /.*REES/)]");
        output(JSONArray.parseArray(read.toString()).isEmpty());
```

```Java
        Object read = JsonPath.read(json, "$.store.book[?(@.author =~ /.*Rees/i)]");
        output(JSONArray.parseArray(read.toString()));
```

等效写法继续省略……

控制台输出：

```shell
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> true

Process finished with exit code 0

```

```shell
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "author":"Nigel Rees",
＞  ① . "price":8.95,
＞  ① . "category":"reference",
＞  ① . "title":"Sayings of the Century"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~

Process finished with exit code 0

```

## 正则验证节点值

* 这个不支持，哈哈，我会封装一下，让它支持。

--- 
* 公众号**FunTester**首发，更多原创文章：[440+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [开源测试服务](https://mp.weixin.qq.com/s/ZOs0cp_vt6_iiundHaKk4g)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)
- [测试开发工程师工作技巧](https://mp.weixin.qq.com/s/TvrUCisja5Zbq-NIwy_2fQ)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)