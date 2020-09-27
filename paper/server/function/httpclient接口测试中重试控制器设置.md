# httpclient接口测试中重试控制器设置



本人在使用httpclient做接口测试的过程中，之前并没有考虑到请求失败自动重试的情况，但有时又需要在发生某些错误的时候重试，比如超时，比如响应频繁被拒绝等等，在看过官方的示例后，自己写了一个自动重试的控制器。分享代码，供大家参考。

下面是获取控制器的方法：


```
    /**
     * 获取重试控制器
     *
     * @return
     */
    private static HttpRequestRetryHandler getHttpRequestRetryHandler() {
        return new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                logger.warn("请求发生错误！", exception);
                if (executionCount > HttpClientConstant.TRY_TIMES) return false;
                if (exception instanceof NoHttpResponseException) {
                    logger.warn("没有响应异常");
                    sleep(1);
                    return true;
                } else if (exception instanceof ConnectTimeoutException) {
                    logger.warn("连接超时，重试");
                    sleep(5);
                    return true;
                } else if (exception instanceof SSLHandshakeException) {
                    logger.warn("本地证书异常");
                    return false;
                } else if (exception instanceof InterruptedIOException) {
                    logger.warn("IO中断异常");
                    sleep(1);
                    return true;
                } else if (exception instanceof UnknownHostException) {
                    logger.warn("找不到服务器异常");
                    return false;
                } else if (exception instanceof SSLException) {
                    logger.warn("SSL异常");
                    return false;
                } else if (exception instanceof HttpHostConnectException) {
                    logger.warn("主机连接异常");
                    return false;
                } else if (exception instanceof SocketException) {
                    logger.warn("socket异常");
                    return false;
                } else {
                    logger.warn("未记录的请求异常：{}", exception.getClass());
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，则重试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    sleep(2);
                    return true;
                }
                return false;
            }
        };
    }
```
   
这样超时时间和重试次数来作为判断接口请求失败的依据了。下面是控制器设置方法：


```
	 /**
     * 通过连接池获取https协议请求对象
     * <p>
     * 增加默认的请求控制器，和请求配置，连接控制器，取消了cookiestore，单独解析响应set-cookie和发送请求的header，适配多用户同时在线的情况
     * </p>
     *
     * @return
     */
    private static CloseableHttpClient getCloseableHttpsClients() {
        // 创建自定义的httpsclient对象
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).setRetryHandler(httpRequestRetryHandler).setDefaultRequestConfig(requestConfig).build();
//         CloseableHttpClient client = HttpClients.createDefault();//非连接池创建
        return client;
    }

```

### 技术类文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
8. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
9. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
10. [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
11. [httpclient如何处理302重定向](https://mp.weixin.qq.com/s/vg354AjPKhIZsnSu4GZjZg)
12. [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
13. [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)


### 非技术文章精选
1. [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
2. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
3. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
4. [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
5. [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
6. [测试之《代码不朽》脑图](https://mp.weixin.qq.com/s/2aGLK3knUiiSoex-kmi0GA)
7. [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
8. [优秀软件开发人员的态度](https://mp.weixin.qq.com/s/0uEEeFaR27aTlyp-sm61bA)
9. [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)
10. [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
11. [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
12. [自动化测试解决了什么问题](https://mp.weixin.qq.com/s/96k2I_OBHayliYGs2xo6OA)
