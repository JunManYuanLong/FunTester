# Java压缩/解压缩字符串

[原文地址](https://www.javacodegeeks.com/2020/07/java-compress-decompress-string-data.html)

`Java`使用压缩库为常规压缩提供了`Deflater`类。它还提供了`DeflaterOutputStream`，它使用`Deflater`类通过压缩（压缩）数据流，然后将压缩后的数据写入另一个输出流来过滤数据流。有等效的`Inflater`和`InflaterOutputStream`类来处理解压。

## 压缩

这是一个如何使用`DeflatorOutputStream`压缩字节数组的示例。

```Java
    /**
     * 压缩字符串,默认梳utf-8
     *
     * @param text
     * @return
     */
    public static String zipBase64(String text) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(out)) {
                deflaterOutputStream.write(text.getBytes(Constant.UTF_8));
            }
            return new String(DecodeEncode.base64Encode(out.toByteArray()));
        } catch (IOException e) {
            logger.error("压缩文本失败:{}", text, e);
        }
        return EMPTY;
    }
```

让我们测试一下：


```Java
    public static void main(String[] args) throws IOException {

        String text = DecodeEncode.zipBase64("5615616119688refdaf888888888888888865555555555555511111111111111111111111119999999999999999999999999999999911111111111111111111333333333333333333");
        output(text);

        String s = DecodeEncode.unzipBase64(text);
        output(s);

        output(text.length() + TAB + s.length());
    }

```

控制台输出：


```shell
INFO-> eJwzNTM0NTM0MzS0NLOwKEpNS0lMs0ADZqYowBAXsCQAsOkxxgAALV0fBw==
INFO-> 5615616119688refdaf888888888888888865555555555555511111111111111111111111119999999999999999999999999999999911111111111111111111333333333333333333
INFO-> 60	145
```


## 解压


```Java
    /**
     * 解压字符串,默认utf-8
     *
     * @param text
     * @return
     */
    public static String unzipBase64(String text) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            try (OutputStream outputStream = new InflaterOutputStream(os)) {
                outputStream.write(DecodeEncode.base64Byte(text));
            }
            return new String(os.toByteArray(), Constant.UTF_8);
        } catch (IOException e) {
            logger.error("解压文本失败:{}", text, e);
        }
        return EMPTY;
    }
```

让我们测试一下：


```Java
    public static void main(String[] args) throws IOException {
        String text = "eJwzNTM0NTM0MzS0NLOwKEpNS0lMs0ADZqYowBAXsCQAsOkxxgAALV0fBw==";
        String s = DecodeEncode.unzipBase64(text);
        output(s);
        output(text.length() + TAB + s.length());
    }
```

控制台输出：


```shell

INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 5615616119688refdaf888888888888888865555555555555511111111111111111111111119999999999999999999999999999999911111111111111111111333333333333333333
INFO-> 60	145
```

## 测试用例

用的是`spock`测试框架，这里用来验证一下，压缩后的字符串和压缩前的长短。

参考文章：
- [Maven和Gradle中配置单元测试框架Spock](https://mp.weixin.qq.com/s/kL5keijAAZwmq_DO1NDBtw)
- [Groovy单元测试框架spock基础功能Demo](https://mp.weixin.qq.com/s/fQCyIyeQANbu2YP2ML6_8Q)
- [Groovy单元测试框架spock数据驱动Demo](https://mp.weixin.qq.com/s/uCAB7Mxt1JZW229aKp-uVQ)
- [单元测试框架spock和Mockito应用](https://mp.weixin.qq.com/s/s21Lts1UnG9HwOEVvgj-uw)
- [人生苦短？试试Groovy进行单元测试](https://mp.weixin.qq.com/s/ahyP-YQTzigeq_5N8byC4g)

```Groovy

     def "测试加密解密"() {

        expect:
        name.length() > DecodeEncode.zipBase64(name).length()

        where:
        name << ["00000000000000000000000000000000000000000000000000000",
                "51666666666666666666666666666666666666666666666666666",
                "(&%^&%*&%(^(^(*&^*(&^(*&^(*^(*&%^%^\$^%##@#!#@!~~#@",
                "发大房东放大反动发动机吧就产国产过高冬季佛冬季风戳分床三佛",
                 "gkjdgjdgjlfdjgldkgjfdsafoiwehoirehtoiewho"]
    }

```

测试结果：


```shell
Condition not satisfied:

name.length() > DecodeEncode.zipBase64(name).length()
|    |        | |            |         |     |
|    50       | |            |         |     64
|             | |            |         (&%^&%*&%(^(^(*&^*(&^(*&^(*^(*&%^%^$^%##@#!#@!~~#@
|             | |            eJwdxVsNADAMAkAtC4M0qKgSpFT7HrmPKzGixcplxaV/+cUwOwQaC71m0AcDsQqi
|             | class com.fun.utils.DecodeEncode
|             false
(&%^&%*&%(^(^(*&^*(&^(*&^(*^(*&%^%^$^%##@#!#@!~~#@


	at com.FunTester.spock.pratice.ZIP.测试加密解密(ZIP.groovy:29)


Condition not satisfied:

name.length() > DecodeEncode.zipBase64(name).length()
|    |        | |            |         |     |
|    29       | |            |         |     120
|             | |            |         发大房东放大反动发动机吧就产国产过高冬季佛冬季风戳分床三佛
|             | |            eJwlyEEKgCAURdHdmwllQYPAZgYKFRRNRDA3898nd5HQ5HK40CNWxzJTWHh6qqE7KI/6leclYnA4L4oOJtW+uSnbDLHjsJTMj2J7ljekQFQU2vo/xR8+gg==
|             | class com.fun.utils.DecodeEncode
|             false
发大房东放大反动发动机吧就产国产过高冬季佛冬季风戳分床三佛


	at com.FunTester.spock.pratice.ZIP.测试加密解密(ZIP.groovy:29)


Condition not satisfied:

name.length() > DecodeEncode.zipBase64(name).length()
|    |        | |            |         |     |
|    41       | |            |         |     60
|             | |            |         gkjdgjdgjlfdjgldkgjfdsafoiwehoirehtoiewho
|             | |            eJwVwgkKACAIBMC3BqvrBUIFfj8ahhngX4pgIRmKs7R9xNq32G2XsX5gTRDw
|             | class com.fun.utils.DecodeEncode
|             false
gkjdgjdgjlfdjgldkgjfdsafoiwehoirehtoiewho


	at com.FunTester.spock.pratice.ZIP.测试加密解密(ZIP.groovy:29)


```

其中两个没通过，感觉这个压缩针对存数字的效果会比较好，或者把处理成`byte[]`会比较好用。网上看一些资料，主要还是用来压缩文件的，有的看着效果还不错，不过让我想起来一个梗：压缩完的文件大小大于压缩前。

--- 
* 公众号**FunTester**首发，更多原创文章：[FunTester430+原创文章](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)，欢迎关注、交流，禁止第三方擅自转载。

#### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/7VG7gHx7FUvsuNtBTJpjWA)
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)
- [有关UI测试计划](https://mp.weixin.qq.com/s/D0fMXwJF754a7Mr5ARY5tQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [测试用例设计——一切测试的基础](https://mp.weixin.qq.com/s/0_ubnlhp2jk-jxHxJ95E9g)

![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzCcsLRmf6VicSKFPfvMT8p7eg7iaBGgPxmbNxHsBcOic2rcw1TCvS1PTGC6WkRFXA7yoqr2bVlrEQqlA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)