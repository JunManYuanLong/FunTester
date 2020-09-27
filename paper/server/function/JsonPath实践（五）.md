# JsonPath实践（五）

书接上文和上上文：

- [JsonPath实践（一）](https://mp.weixin.qq.com/s/Cq0_v_ptbGd4f5y8HIsq7w)
- [JsonPath实践（二）](https://mp.weixin.qq.com/s/w_iJTiuQahIw6U00CJVJZg)
- [JsonPath实践（三）](https://mp.weixin.qq.com/s/58A3k0T6dbOkBJ5nRYKDqA)
- [JsonPath实践（四）](https://mp.weixin.qq.com/s/8ER61qrkMj8bdBpyuq9r6w)

今天分享的内容是`JSonpath`过滤数据的`API`。这部分`API`分成两类：一类是运算符，例如：`==`、`>`、`=~`这些，一类是方法或者函数，例如：`in`、`nin`、`anyof`等等。

第一类实在没啥可分享的写法都是按照语言使用习惯，然后之前的文章也都介绍过了，下面主要分享一下方法函数的使用。

# json数据

在原来的数据基础上增加了`page`和`pages`两个字段。

```Java
        JSONObject json = JSON.parseObject("{" +
                "    \"store\": {" +
                "        \"book\": [" +
                "            {" +
                "                \"category\": \"reference\"," +
                "                \"author\": \"Nigel Rees\"," +
                "                \"title\": \"Sayings of the Century\"," +
                "                \"page\": \"D\"," +
                "                \"pages\": [\"S\",\"X\",\"G\"]," +
                "                \"price\": 8.95" +
                "            }," +
                "            {" +
                "                \"category\": \"fiction\"," +
                "                \"author\": \"Evelyn Waugh\"," +
                "                \"title\": \"Sword of Honour\"," +
                "                \"page\": \"A\"," +
                "                \"pages\": [\"A\",\"B\"]," +
                "                \"price\": 12.99" +
                "            }," +
                "            {" +
                "                \"category\": \"fiction\"," +
                "                \"author\": \"Herman Melville\"," +
                "                \"title\": \"Moby Dick\"," +
                "                \"isbn\": \"0-553-21311-3\"," +
                "                \"page\": \"B\"," +
                "                \"pages\": [\"E\",\"F\"]," +
                "                \"price\": 8.99" +
                "            }," +
                "            {" +
                "                \"category\": \"fiction\"," +
                "                \"author\": \"J. R. R. Tolkien\"," +
                "                \"title\": \"The Lord of the Rings\"," +
                "                \"isbn\": \"0-395-19395-8\"," +
                "                \"page\": \"C\"," +
                "                \"pages\": [\"C\",\"D\"]," +
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

## 字段值在某个数组中

这里的数组的写法跟语言一样。

`jsonpath`：`$.store.book[?(@.page in ['A','C'])]`

代码：

```Java
        Object read = JsonPath.read(json, "$.store.book[?(@.page in ['A','C'])]");
        output(JSONArray.parseArray(read.toString()));
```

等效写法继续省略……

控制台输出：


```shell
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "pages":[
＞  ② . . . "A",
＞  ② . . . "B"
＞  ① . ],
＞  ① . "author":"Evelyn Waugh",
＞  ① . "price":12.99,
＞  ① . "page":"A",
＞  ① . "category":"fiction",
＞  ① . "title":"Sword of Honour"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "pages":[
＞  ② . . . "C",
＞  ② . . . "D"
＞  ① . ],
＞  ① . "author":"J. R. R. Tolkien",
＞  ① . "price":22.99,
＞  ① . "isbn":"0-395-19395-8",
＞  ① . "page":"C",
＞  ① . "category":"fiction",
＞  ① . "title":"The Lord of the Rings"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~

Process finished with exit code 0

```

## 字段值不在某个数组中

这个跟上面的一模一样，只是函数变成了`nin`，应该是`no in`的缩写吧。

`jsonpath`：`$.store.book[?(@.page nin ['A','C'])]`

代码：

```Java
        Object read = JsonPath.read(json, "$.store.book[?(@.page nin ['A','C'])]");
        output(JSONArray.parseArray(read.toString()));
```

等效写法继续省略……

控制台输出：


```shell
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "pages":[
＞  ② . . . "S",
＞  ② . . . "X"，
＞  ② . . . "G"
＞  ① . ],
＞  ① . "author":"Nigel Rees",
＞  ① . "price":8.95,
＞  ① . "page":"D",
＞  ① . "category":"reference",
＞  ① . "title":"Sayings of the Century"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "pages":[
＞  ② . . . "E",
＞  ② . . . "F"
＞  ① . ],
＞  ① . "author":"Herman Melville",
＞  ① . "price":8.99,
＞  ① . "isbn":"0-553-21311-3",
＞  ① . "page":"B",
＞  ① . "category":"fiction",
＞  ① . "title":"Moby Dick"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~

Process finished with exit code 0


```

## 子集

这个校验的是数组之间的关系，`value`的值必需是数组才行，如果不是，会返回空值，但不会报错。

`jsonpath`：`$.store.book[?(@.pages subsetof ['A','B','C'])]`

代码：

```Java
        Object read = JsonPath.read(json, "$.store.book[?(@.pages subsetof ['A','B','C'])]");
        output(JSONArray.parseArray(read.toString()));
```

等效写法继续省略……

控制台输出：



```shell
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "pages":[
＞  ② . . . "A",
＞  ② . . . "B"
＞  ① . ],
＞  ① . "author":"Evelyn Waugh",
＞  ① . "price":12.99,
＞  ① . "page":"A",
＞  ① . "category":"fiction",
＞  ① . "title":"Sword of Honour"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~

Process finished with exit code 0

```

## 数组之间的校验

国外的月亮也不一定是真的圆，这个据说在19年初被支持了，但是最后一个发行版本是在17年，已经三年没有发行版本了。我改天研究一下，自己弄个最新的版本版本吧。


## 属性值数量验证

`size`可以验证数组长度也可以验证字符串长度。

`jsonpath`：`$.store.book[?(@.pages size 3)]`

字符串长度：

`jsonpath`：`$.store.book[?(@.author size 16)]`


代码：

```Java
        Object read = JsonPath.read(json, "$.store.book[?(@.pages size 3)]");
        output(JSONArray.parseArray(read.toString()));
```


```Java
        Object read = JsonPath.read(json, "$.store.book[?(@.author size 16)]");
        output(JSONArray.parseArray(read.toString()));
```

等效写法继续省略……

控制台输出：


```shell
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "pages":[
＞  ② . . . "S",
＞  ② . . . "X",
＞  ② . . . "G"
＞  ① . ],
＞  ① . "author":"Nigel Rees",
＞  ① . "price":8.95,
＞  ① . "page":"D",
＞  ① . "category":"reference",
＞  ① . "title":"Sayings of the Century"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~

Process finished with exit code 0

```


```shell

INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~
＞  {
＞  ① . "pages":[
＞  ② . . . "C",
＞  ② . . . "D"
＞  ① . ],
＞  ① . "author":"J. R. R. Tolkien",
＞  ① . "price":22.99,
＞  ① . "isbn":"0-395-19395-8",
＞  ① . "page":"C",
＞  ① . "category":"fiction",
＞  ① . "title":"The Lord of the Rings"
＞  }
~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~ JSON ~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~~☢~

Process finished with exit code 0

```

## 为空判断

事实证明这也是一个坑，不过可以使用`size 0`这个`API`替换一下。


--- 
* 公众号**FunTester**首发，更多原创文章：[450+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

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