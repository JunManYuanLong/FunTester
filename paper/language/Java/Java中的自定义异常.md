# Java中的自定义异常


在测试脚本的编写中会需要使用自定义异常，通常可以很容易地用**Java**创建自定义异常类。它可以是已检查或未检查的异常。下面将演示一个简单的示例来检查Java中自定义异常的创建。

* 如何创建自定义异常类
* 引发自定义Java异常
* 捕获自定义异常
* 检查输出

# Java自定义异常

下面的类是创建自定义异常的简单**Java**类。

```Java
package com.fun.base.exception;

public class FailException extends Exception {

    private static final long serialVersionUID = -7041169491254546905L;

    public FailException() {
        super("FunTester");
    }

    protected FailException(String message) {
        super(message);
    }


}

```

创建异常的步骤：

* 创建一个`Java`类
* 扩展`Exception`类
* 调用`super()`

**Exception**类中还存在其他构造函数。这是创建自定义异常的基本示例。这是最常用的方式。

## 触发自定义异常

在上面的示例中，我们创建了一个自定义异常**FailException**。现在，让我们在**Java**代码示例中抛出此异常。

```Java

    public static void fail(String message) {
        throw new FailException(message);
    }

```

引发异常的步骤：

* 创建异常**FailException**的实例
* 使用**throw**关键字引发异常
* 使用**throws**关键字声明方法中的异常

# Demo

```Java
    /**
     * 根据解析好的content，转化json对象
     *
     * @param content
     * @return
     */
    private static JSONObject getJsonResponse(String content, JSONObject cookies) throws FailException {
        JSONObject jsonObject = new JSONObject();
        try {
            if (StringUtils.isEmpty(content)) FailException.fail("响应为空!");
            jsonObject = JSONObject.parseObject(content);
        } catch (JSONException e) {
            jsonObject = getJson("content=" + content, "code=" + TEST_ERROR_CODE);
            logger.warn("响应体非json格式，已经自动转换成json格式！");
        } finally {
            if (cookies != null && !cookies.isEmpty()) jsonObject.put(HttpClientConstant.COOKIE, cookies);
            return jsonObject;
        }
    }

```


--- 
* **郑重声明**：公众号“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester十八张原创专辑](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**，合作请联系`Fhaohaizi@163.com`。

### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [Selenium并行测试最佳实践](https://mp.weixin.qq.com/s/-RsQZaT5pH8DHPvm0L8Hjw)
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)
- [有关UI测试计划](https://mp.weixin.qq.com/s/D0fMXwJF754a7Mr5ARY5tQ)
- [软件测试外包](https://mp.weixin.qq.com/s/sYQfb2PiQptcT0o_lLpBqQ)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)