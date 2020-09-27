# 获取HTTP请求对象--测试框架视频讲解


之前有了个序[FunTester测试框架视频讲解（序）](https://mp.weixin.qq.com/s/CJrHAAniDMyr5oDXYHpPcQ)，反响不错，今天来讲讲测试框架中另外一个核心的类`FanLibrary`中获取`HttpRequestBase`对象的方法。

主要分为`HTTPget`和`HTTPpost`，参数也分`json`和`form`形式，还有文件传输等等。下期讲讲发送请求和解析响应结果。

- [点击查看原文观看视频](https://mp.weixin.qq.com/s/hG89sGf96GcPb2hGnludsw)

**gitee地址：https://gitee.com/fanapi/tester**

代码如下：

```Java
    /**
     * 方法已重载，获取get对象
     * <p>方法重载，主要区别参数，会自动进行urlencode操作</p>
     *
     * @param url  表示请求地址
     * @param args 表示传入数据
     * @return 返回get对象
     */
    public static HttpGet getHttpGet(String url, JSONObject args) {
        if (args == null || args.size() == 0) return getHttpGet(url);
        String uri = url + changeJsonToArguments(args);
        return getHttpGet(uri.replace(" ", ""));
    }

    /**
     * 方法已重载，获取get对象
     * <p>方法重载，主要区别参数，会自动进行urlencode操作</p>
     *
     * @param url 表示请求地址
     * @return 返回get对象
     */
    public static HttpGet getHttpGet(String url) {
        return new HttpGet(url);
    }

    /**
     * 获取post对象，以form表单提交数据
     * <p>方法重载，文字信息form表单提交，文件信息二进制流提交，具体参照文件上传的方法主食，post请求可以不需要参数，暂时不支持其他参数类型，如果是公参需要在url里面展示，需要传一个json对象，一般默认args为get公参，params为post请求参数</p>
     *
     * @param url    请求地址
     * @param params 请求数据，form表单形式设置请求实体
     * @return 返回post对象
     */
    public static HttpPost getHttpPost(String url, JSONObject params) {
        HttpPost httpPost = getHttpPost(url);
        setFormHttpEntity(httpPost, params);
        httpPost.addHeader(HttpClientConstant.ContentType_FORM);
        return httpPost;
    }

    /**
     * 获取httppost对象，没有参数设置
     * <p>方法重载，文字信息form表单提交，文件信息二进制流提交，具体参照文件上传的方法主食，post请求可以不需要参数，暂时不支持其他参数类型，如果是公参需要在url里面展示，需要传一个json对象，一般默认args为get公参，params为post请求参数</p>
     *
     * @param url
     * @return
     */
    public static HttpPost getHttpPost(String url) {
        return new HttpPost(url.replace(" ", ""));
    }

    /**
     * 获取httppost对象，json格式对象，传参时手动tostring
     * <p>新重载方法，适应post请求json传参，估计utf-8编码格式</p>
     *
     * @param url
     * @param params
     * @return
     */
    public static HttpPost getHttpPost(String url, String params) {
        HttpPost httpPost = getHttpPost(url);
        httpPost.setEntity(new StringEntity(params, DEFAULT_CHARSET.toString()));
        httpPost.addHeader(HttpClientConstant.ContentType_JSON);
        return httpPost;
    }

    /**
     * * 获取httppost对象，json格式对象，传参时手动tostring
     * <p>新重载方法，适应post请求json传参</p>
     *
     * @param url
     * @param args
     * @return
     */
    public static HttpPost getHttpPost(String url, JSONObject args, String params) {
        return getHttpPost(url + changeJsonToArguments(args), params);
    }

    /**
     * 获取 httppost 请求对象
     * <p>方法重载，文字信息form表单提交，文件信息二进制流提交，具体参照文件上传的方法主食，post请求可以不需要参数，暂时不支持其他参数类型，如果是公参需要在url里面展示，需要传一个json对象，一般默认args为get公参，params为post请求参数</p>
     *
     * @param url    请求地址
     * @param args   请求地址参数
     * @param params 请求参数
     * @return
     */
    public static HttpPost getHttpPost(String url, JSONObject args, JSONObject params) {
        return getHttpPost(url + changeJsonToArguments(args), params);
    }


    /**
     * 获取 httpPost 对象
     * <p>方法重载，文字信息form表单提交，文件信息二进制流提交，具体参照文件上传的方法主食，post请求可以不需要参数，暂时不支持其他参数类型，如果是公参需要在url里面展示，需要传一个json对象，一般默认args为get公参，params为post请求参数</p>
     *
     * @param url    请求地址
     * @param args   请求通用参数
     * @param params 请求参数，其中二进制流必须是 file
     * @param file   文件
     * @return
     */
    public static HttpPost getHttpPost(String url, JSONObject args, JSONObject params, File file) {
        return getHttpPost(url + changeJsonToArguments(args), params, file);
    }

    /**
     * 获取 httpPost 对象
     * <p>方法重载，文字信息form表单提交，文件信息二进制流提交，具体参照文件上传的方法主食，post请求可以不需要参数，暂时不支持其他参数类型，如果是公参需要在url里面展示，需要传一个json对象，一般默认args为get公参，params为post请求参数</p>
     *
     * @param url    请求地址
     * @param params 请求参数，其中二进制流必须是 file
     * @param file   文件
     * @return
     */
    public static HttpPost getHttpPost(String url, JSONObject params, File file) {
        HttpPost httpPost = getHttpPost(url);
        setMultipartEntityEntity(httpPost, params, file);
        return httpPost;
    }

    /**
     * 设置二进制流实体，params 里面参数值为 file
     *
     * @param httpPost httpPsot 请求
     * @param params   请求参数
     * @param file     文件
     */
    private static void setMultipartEntityEntity(HttpPost httpPost, JSONObject params, File file) {
        logger.debug("上传文件名：{}", file.getAbsolutePath());
        String fileName = file.getName();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            logger.warn("读取文件失败！", e);
        }
        Iterator<String> keys = params.keySet().iterator();// 遍历 params 参数和值
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();// 新建MultipartEntityBuilder对象
        while (keys.hasNext()) {
            String key = keys.next();
            String value = params.getString(key);
            if (value.equals("file")) {
                builder.addBinaryBody(key, inputStream, ContentType.create(HttpClientConstant.CONTENTTYPE_MULTIPART_FORM), fileName);// 设置流参数
            } else {
                StringBody body = new StringBody(value, ContentType.create(HttpClientConstant.CONTENTTYPE_TEXT, DEFAULT_CHARSET));// 设置普通参数
                builder.addPart(key, body);
            }
        }
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
    }
```

---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第三版](https://mp.weixin.qq.com/s/Mk3PoH7oJX7baFmbeLtl_w)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [压测中测量异步写入接口的延迟](https://mp.weixin.qq.com/s/odvK1iYgg4eRVtOOPbq15w)
- [多种登录方式定量性能测试方案](https://mp.weixin.qq.com/s/WuZ2h2rr0rNBgEvQVioacA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [左移测试](https://mp.weixin.qq.com/s/8zXkWV4ils17hUqlXIpXSw)
- [选择手动测试还是自动化测试？](https://mp.weixin.qq.com/s/4haRrfSIp5Plgm_GN98lRA)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDkiawpL3o8umv1EgHOc2OE1H8DtTMQSXWTOgFYPMSGtoX2BZlricBBJun4hMGUOJd7uibe68zQecRFw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)![](https://mmbiz.qpic.cn/mmbiz_gif/13eN86FKXzCPsneTRDBzskVY9GpIhbl6e3JpwysPqAbM7Z80J1EZrIYpTO7YSD40Cp9hOicibdV3GIbVTcEapgqA/640?wx_fmt=gif&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)