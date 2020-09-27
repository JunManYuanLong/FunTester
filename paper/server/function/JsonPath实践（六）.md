# JsonPath实践（六）


## 书接上文和上上文：

- [JsonPath实践（一）](https://mp.weixin.qq.com/s/Cq0_v_ptbGd4f5y8HIsq7w)
- [JsonPath实践（二）](https://mp.weixin.qq.com/s/w_iJTiuQahIw6U00CJVJZg)
- [JsonPath实践（三）](https://mp.weixin.qq.com/s/58A3k0T6dbOkBJ5nRYKDqA)
- [JsonPath实践（四）](https://mp.weixin.qq.com/s/8ER61qrkMj8bdBpyuq9r6w)
- [JsonPath实践（五）](https://mp.weixin.qq.com/s/knVLW960WXnckGLstdrOVQ)

之前分享了`jsonpath`的部分`API`使用，基本已经把基础的内容讲完了，今天分享一下`JsonPath API`中的函数的使用方法，其实之前讲到的一些`json数组`的过滤中已经用到了一些`函数`，大概是因为功能不一样吧，这里将的函数都是处理`json数组`的，而不是过滤数组的条件。

# json数据

在原来的数据基础上增加了部分字段和部分节点。


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

# 获取数组最小值

`jsonpath`：`$.ss.min()`

代码：

```Java
        Object read = JsonPath.read(json, "$.ss.min()");
        output(read);
```

等效写法继续省略……

控制台输出：


```Java
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 4.0

Process finished with exit code 0

```

* 这里需要注意，该方法返回值是一个`double`的数值，测试中用的`int`整型，但是结果返回的是`4.0`。

# 获取数组的最大值


`jsonpath`：`$.ss.max()`

代码：

```Java
        Object read = JsonPath.read(json, "$.ss.max()");
        output(read);
```

等效写法继续省略……

控制台输出：


```Java
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 32.0

Process finished with exit code 0

```

* 同样的，`max`函数返回的也是`double`浮点型数据。

# 获取数组的平均值

`jsonpath`：`$.ss.avg()`

代码：

```Java
        Object read = JsonPath.read(json, "$.ss.avg()");
        output(read);
```

等效写法继续省略……

控制台输出：


```Java
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 22.75

Process finished with exit code 0
```

* 同样的，`max`函数返回的也是`double`浮点型数据。

# 获取数组的标准差

`jsonpath`：`$.ss.stddev()`

代码：

```Java
        Object read = JsonPath.read(json, "$.ss.stddev()");
        output(read);
```

等效写法继续省略……

控制台输出：


```Java
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 11.431863365173676

Process finished with exit code 0

```

* 同样的，`stddev`函数返回的也是`double`浮点型数据。

# 获取数组的长度

* 对于`json数组`适用。

`jsonpath`：`$.ss.length()`

`jsonpath`：`$.store.book.length()`

代码：

```Java
        Object read = JsonPath.read(json, "$.ss.length()");
        output(read);
```
```Java
        Object read = JsonPath.read(json, "$.store.book.length()");
        output(read);
```
等效写法继续省略……

控制台输出：


```Java
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 4

Process finished with exit code 0

```

# 求数组的和

`jsonpath`：`$.ss.sum()`

代码：

```Java
        Object read = JsonPath.read(json, "$.ss.sum()");
        output(read);
```

等效写法继续省略……

控制台输出：


```Java
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 91.0

Process finished with exit code 0

```

* 同样的，`sun`函数返回的也是`double`浮点型数据。


自此，`JsonPath API`系列已经更完了，我在积极准备`JsonPath util`的内容，使用`Groovy`的[Groovy重载操作符（终极版）](https://mp.weixin.qq.com/s/4oYGJ2B2Y1AqxsIj8v5nZA)功能，敬请期待。

--- 
* 公众号**FunTester**首发，更多原创文章：[450+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [开源测试服务](https://mp.weixin.qq.com/s/ZOs0cp_vt6_iiundHaKk4g)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)
- [测试开发工程师工作技巧](https://mp.weixin.qq.com/s/TvrUCisja5Zbq-NIwy_2fQ)
- [Fiddler Everywhere工具答疑](https://mp.weixin.qq.com/s/2peWMJ-rgDlVjs3STNeS1Q)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)