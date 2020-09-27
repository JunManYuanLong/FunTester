# JsonPath实践（三）

书接上文和上上文：
- [JsonPath实践（一）](https://mp.weixin.qq.com/s/Cq0_v_ptbGd4f5y8HIsq7w)
- [JsonPath实践（二）](https://mp.weixin.qq.com/s/w_iJTiuQahIw6U00CJVJZg)

本期讲一下获取数组时增加过滤条件，这里用到的语法稍微复杂一点点。主要的过滤条件有几类：**属性是否存在**、**属性值比较**、**属性值与属性值**和**数组长度求值**。

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

## 验证属性是否存在

`jsonpath`：`$..book[?(@.isbn)]`


代码：

```Java
        Object read = JsonPath.read(json, "$..book[?(@.isbn)]");
        output(JSONArray.parseArray(read.toString()));
```

等效写法省略……


控制台输出：


```shell
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "author":"Herman Melville",
＞  ① . "price":8.99,
＞  ① . "isbn":"0-553-21311-3",
＞  ① . "category":"fiction",
＞  ① . "title":"Moby Dick"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "author":"J. R. R. Tolkien",
＞  ① . "price":22.99,
＞  ① . "isbn":"0-395-19395-8",
＞  ① . "category":"fiction",
＞  ① . "title":"The Lord of the Rings"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~

Process finished with exit code 0

```

* 暂时没有找到提供验证属性不存在的`API`，不过这个可以通过另外的方式实现，例如：**属性值比较**和**属性值正则匹配**等等，后面会讲到。


## 属性值比较

字符串比较：

`jsonpath`：`$..book[?(@.isbn == '0-395-19395-8')]`

数值比较：

`jsonpath`：`$..book[?(@.price > 20)]`

* 这里语法支持不同数据类型的自动化转换的，跟其他脚本语言一样。`JSonpath`还支持更多的**值标胶**写法，这个以后单独写篇文章讲一讲。

代码：

```Java
        Object read = JsonPath.read(json, "$..book[?(@.price > 20)]");
        output(JSONArray.parseArray(read.toString()));
```

等效写法继续省略……


控制台输出：


```shell
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "author":"J. R. R. Tolkien",
＞  ① . "price":22.99,
＞  ① . "isbn":"0-395-19395-8",
＞  ① . "category":"fiction",
＞  ① . "title":"The Lord of the Rings"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~

Process finished with exit code 0

```

## 属性值与属性值

这个比较简单，涉及到一个`JSonpath`语法的嵌套问题。

`jsonpath`：`$..book[?(@.price > $['expensive'])]`

* 这里语法的嵌套基本是个套娃，不过个人还是不建议使用套娃，毕竟标记语法当然是越简单越好，非常强调可读性。

代码：

```Java
        Object read = JsonPath.read(json, "$..book[?(@.price > $['expensive'])]");
        output(JSONArray.parseArray(read.toString()));
```

等效写法继续省略……


控制台输出：


```shell

INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "author":"Evelyn Waugh",
＞  ① . "price":12.99,
＞  ① . "category":"fiction",
＞  ① . "title":"Sword of Honour"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "author":"J. R. R. Tolkien",
＞  ① . "price":22.99,
＞  ① . "isbn":"0-395-19395-8",
＞  ① . "category":"fiction",
＞  ① . "title":"The Lord of the Rings"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~

Process finished with exit code 0

```

## 数组长度求值

这个就更简单了，求数组长度的一个`API`。

`jsonpath`：`$..book.length()`

* 这里有一个*坑*，如果把`length()`方法用到对数组过滤或者指定数组对象之后，会变成求该对象属性个数或者过滤后数组的长度的功能了，返回结果是个数值类型的数组。

代码：

```Java
        Object read = JsonPath.read(json, "$..book.length()");
        output(read);
```

等效写法继续省略……


控制台输出：


```shell

INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> [4]

Process finished with exit code 0

```

--- 
* 公众号**FunTester**首发，更多原创文章：[FunTester440+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [开源测试服务](https://mp.weixin.qq.com/s/ZOs0cp_vt6_iiundHaKk4g)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [电子书网站爬虫实践](https://mp.weixin.qq.com/s/KGW0dIS5NTLgxyhSjxDiOw)
- [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)