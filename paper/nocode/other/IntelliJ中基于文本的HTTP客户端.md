# IntelliJ中基于文本的HTTP客户端



`IntelliJ`提供了一个纯基于文本的**HTTP客户端**。尽管一开始听起来可能很奇怪，但事实证明这是一个非常有用的功能。

# 入门

首先，我们需要创建一个名称以`.http`或`.rest`结尾的文件。例如`FunTester.http`。

要发出简单的GET请求，我们必须在新创建的文件中写下该请求。

例如：

`GET  https://api.muxiaoguo.cn/api/dujitang`

`IntelliJ`现在在该行旁边添加了一个小的**Run-Icon**，它可以执行请求。

![](http://pic.automancloud.com/WX20200922-162438.png)

* 响应结果：

```shell
GET https://api.muxiaoguo.cn/api/dujitang

HTTP/1.1 200 OK
Server: nginx
Date: Tue, 22 Sep 2020 08:17:55 GMT
Content-Type: text/html; charset=utf-8
Transfer-Encoding: chunked
Connection: keep-alive
Vary: Accept-Encoding
Expires: Thu, 19 Nov 1981 08:52:00 GMT
Cache-Control: no-store, no-cache, must-revalidate
Pragma: no-cache
Strict-Transport-Security: max-age=31536000

{"code":"200","msg":"success","data":{"comment":"你以为有钱人很快乐吗?他们的快乐你根本想象不到！"}}

Response code: 200 (OK); Time: 146ms; Content length: 76 bytes

Cannot preserve cookies, cookie storage file is included in ignored list:
> /Users/fv/Documents/workspace/fun/.idea/httpRequests/http-client.cookies

```

* 如果要添加`JSON`参数的请求头，只需添加`Content-Type`标头和请求正文：


```JSON
GET  https://api.muxiaoguo.cn/api/dujitang
cookie: PHPSESSID=e78ldgop6jub72kp636vqcsj6l
user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36
Content-Type: application/json
 
{
  "aa": "FunTester",
  "ss": "ok"
}
```

* 同一文件中的多个请求需要使用**###**分隔。例如：

```json
GET  https://api.muxiaoguo.cn/api/dujitang
cookie: PHPSESSID=e78ldgop6jub72kp636vqcsj6l
user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36

###
 
GET  https://api.muxiaoguo.cn/api/dujitang
cookie: PHPSESSID=e78ldgop6jub72kp636vqcsj6l
user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36
Content-Type: application/json
 
{
  "aa": "FunTester",
  "ss": "ok"
}
```

# 使用变量

使用`{{..}}`语法，我们可以向请求中添加变量。也许我们想针对不同的环境发出相同的请求。为此，我们可以使用`host`变量更新请求：

`GET http://{{host}}/products`

接下来，我们需要定义`{{host}}`变量。为此，我们创建一个`http-client.env.json`文件并添加以下内容：


```json
{
  "development": {
    "host": "http://localhost:8080"
  },
  "production": {
    "host": "http://my-cool-api.com"
  }
}
```

这定义了两个环境：`dev`和`online`。两种环境都使用不同的值定义`host`变量。

运行请求时，我们现在可以选择所需的环境：

![](http://pic.automancloud.com/WX20200922-162532.png)

# 团队共享

基于文本的简单请求定义使您可以轻松地与团队共享。您甚至可以将请求文件检入版本控制系统。当然，您不希望签入执行请求可能需要的密码或API密钥。`IntelliJ`通过单独的私有环境文件（http-client.private.env.json）支持此功能。与前面的环境示例一样，我们可以使用此文件来定义变量。

例如：


```json
{
  "dev": {
    "api-key": "S3DKLJ56698CR3T"
  }
}

```

为了确保安全性，我们可以从版本控制系统中明确排除此文件。

----
公众号**FunTester**首发，原创分享爱好者，腾讯云和掘金社区首页推荐，知乎七级原创作者，欢迎关注、交流，禁止第三方擅自转载。

FunTester热文精选
=

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)
- [测试开发工程师工作技巧](https://mp.weixin.qq.com/s/TvrUCisja5Zbq-NIwy_2fQ)
- [Selenium4 IDE，它终于来了](https://mp.weixin.qq.com/s/XNotlZvFpmBmBQy1pYifOw)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [为什么测试覆盖率如此重要](https://mp.weixin.qq.com/s/0evyuiU2kdXDgMDnDKjORg)
- [为什么测试覆盖率如此重要](https://mp.weixin.qq.com/s/0evyuiU2kdXDgMDnDKjORg)
- [吐个槽，非测误入。](https://mp.weixin.qq.com/s/BBFzUZVFMmU7a6qfLKas2w)
- [自动化测试框架](https://mp.weixin.qq.com/s/vu6p_rQd3vFKDYu8JDJ0Rg)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDnHxttBoq6jhgic4jJF8icbAMdOvlR0xXUX9a3tupYYib3ibYyIHicNtefS3Jo7yefLKlQWgLK7bCgCLA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)