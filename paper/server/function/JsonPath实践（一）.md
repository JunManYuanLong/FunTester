# JsonPath实践（一）

最近团队开发了一个平台，功能界面类似`postman`，用例都还是单接口的用例，就是可以绑定一个用户的登录状态和一些常量。验证功能主要分为两类：1、系统验证（包括请求异常、HTTP状态码、通用响应结构验证）；2、功能验证（包括业务code、文本内容等）。都是通过字符串解析和正则匹配来完成的。

接下来的二期有一个目标就是丰富验证功能和多用例串联起来，这里了解到了一个`jsonpath`的工具，经过简单尝试，效果非常理想。因为每一个用例都对应这一个请求，执行完都会返回一个`JSonobject`的对象（这里是框架封装好的）。如果使用`JSonpath`的标记语言能够完成`json`信息的提取，那么就可以完美解决这个需求。

下面分享一下官方的`API`的实践。

## 引入jar包


```Groovy
    compile group: 'com.jayway.jsonpath', name: 'json-path', version: '2.4.0'
```
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

## `jsonpath`的两种写法


`JsonPath`表达式始终以与`XPath`表达式与`XML`文档结合使用的方式解析`JSON`结构数据。`JsonPath`中的根对象或者数组用**$**表示。

`JsonPath`表达式可以使用点符号

`$.store.book[0].title`

或括号符号

`$['store']['book'][0]['title']`

## API

基本的是一个`read()`方法：


```Java
   /**
     * Creates a new JsonPath and applies it to the provided Json object
     *
     * @param json     a json object
     * @param jsonPath the json path
     * @param filters  filters to be applied to the filter place holders  [?] in the path
     * @param <T>      expected return type
     * @return list of objects matched by the given path
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T read(Object json, String jsonPath, Predicate... filters) {
        return parse(json).read(jsonPath, filters);
    }
```

* 对于测试来讲，只需要两个参数即可，足够满足所有使用场景了，而且后面的参数对象对于`fastjson`兼容性不咋好，部分高级语法不支持这个。


## 获取所有图书的作者列表

`jsonpath`：`$.store.book[*].author`

代码：

```Java
        Object read = JsonPath.read(json, "$.store.book[*].author");
        output(read);
```

这里可以完全自定义返回对象类型，`jsonpath`会自动帮用户完成对象转换，跟下面的这个写法是完全等效的。


```Java
 List read = JsonPath.read(json, "$.store.book[*].author");
        output(read);

```

输出：


```shell
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> ["Nigel Rees","Evelyn Waugh","Herman Melville","J. R. R. Tolkien"]

```

可以看到得到了无论使用`object`或者`list`得到的结果都是一样的，但是如果使用的不当的对象类型，就会报下面的错误：


```Java
Exception in thread "main" java.lang.ClassCastException: net.minidev.json.JSONArray cannot be cast to java.lang.String
	at com.fun.ztest.groovy.JsonPathTest.main(JsonPathTest.java:50)
```

## 获取所有作者

`JSonpath`：`$..author`
`$.store..price`

代码省略，输出：


```shell
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> ["Nigel Rees","Evelyn Waugh","Herman Melville","J. R. R. Tolkien"]

```


```shell
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> [19.95,8.95,12.99,8.99,22.99]
```

可以看到可以不指定具体的`key`也可以获取固定层级的某个`key`的`value`。

## 获取节点下所有信息

1. `JSonpath`：`$.store.*`
2. `JSonpath`：`$.ss.*`

代码省略，输出：



```Java
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> [{"color":"red","price":19.95},[{"author":"Nigel Rees","price":8.95,"category":"reference","title":"Sayings of the Century"},{"author":"Evelyn Waugh","price":12.99,"category":"fiction","title":"Sword of Honour"},{"author":"Herman Melville","price":8.99,"isbn":"0-553-21311-3","category":"fiction","title":"Moby Dick"},{"author":"J. R. R. Tolkien","price":22.99,"isbn":"0-395-19395-8","category":"fiction","title":"The Lord of the Rings"}]]
```

```Java
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> [32,32,4,23]
```

下期讲讲如果处理`json`数组，欢迎继续关注！

--- 
* 公众号**FunTester**首发，更多原创文章：[FunTester430+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [如何mock固定QPS的接口](https://mp.weixin.qq.com/s/yogj9Fni0KJkyQuKuDYlbA)
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [电子书网站爬虫实践](https://mp.weixin.qq.com/s/KGW0dIS5NTLgxyhSjxDiOw)
- [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)