# JsonPath实践（二）

书接上文：[JsonPath实践（一）](https://mp.weixin.qq.com/s/Cq0_v_ptbGd4f5y8HIsq7w)

本期聊一下如何使用`JSonpath`标记语法处理，`json`对象中的数组主要内容是提取数组中对象和对象集合。

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

## 获取数组中的有序对象

`jsonpath`：`$.store.book[2]`

* 这里注意索引是从*0*开始的。

`jsonpath`：`$.store.book[-1]`

* 这里表示倒数第一个对象

代码：

```Java
        Object read = JsonPath.read(json, "$.store.book[2]");
        output(JSON.parseObject(read.toString()));
```

等效写法：

```Java
        JSONObject read = JsonPath.read(json, "$.store.book[2]");
        output(read);

```

控制台输出：


```shell
INFO-> 当前用户：fv，IP：10.60.131.54，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
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

Process finished with exit code 0

```


## 获取数组中的有序对象切片

* 这里的语法类似于`Python`中对于数组的处理。

`jsonpath`：`$.store.book[:2]`

* 倒数截取

`jsonpath`：`$.store.book[-2:]`


代码：

```Java
        Object read = JsonPath.read(json, "$.store.book[2]");
        output(JSON.parseObject(read.toString()));
```

等效写法省略……


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
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "author":"Evelyn Waugh",
＞  ① . "price":12.99,
＞  ① . "category":"fiction",
＞  ① . "title":"Sword of Honour"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~

Process finished with exit code 0

```

* 倒数截取


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