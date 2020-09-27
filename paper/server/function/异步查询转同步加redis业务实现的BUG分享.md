# 异步查询转同步加redis业务实现的BUG分享

在最近的性能测试中，某一个查询接口指标不通过，开发做了N次优化，最终的优化方案如下：异步查询然后转同步，再加上redis缓存。此为背景。

在测试过程中发现一个BUG：同样的请求在第一次查询结果是OK的，但是第二次查询（理论上讲得到的缓存数据）缺失了某些字段。

后端服务的测试代码如下，代码内容作了简化，留下了关键的部分，`doSomething(dataMap);`为简化方法，其中`teacherPadAsyncService.doExcuteLikeSateAsync()`、`teacherPadAsyncService.doExcuteAccuracyAsync()`、`teacherPadAsyncService.doExcuteTeacherTagAsync`这三个是异步方法：


```Java
 @Override
    public void doExecute(Map<String, Object> dataMap) {
        String cache = defaultRedisUtil.getString(RedisKeyConfig.COURSE_PKG_DETAIL_KEY + id);
        if (StringUtils.isNotBlank(cache)) {
            dataMap = JSON.parseObject(cache, Map.class);
            return;
        }
        doSomething(dataMap);
        CountDownLatch countDownLatch = new CountDownLatch(3);
        String traceKey = TraceKeyHolder.getTraceKey();
        teacherPadAsyncService.doExcuteLikeSateAsync(dataMap, coursePackage.getId(),ResourceTypeEnum.COURSE_PACKAGE.value, currentUser.getSystemId(), countDownLatch, traceKey);
        teacherPadAsyncService.doExcuteAccuracyAsync(dataMap, coursePackage.getId(), countDownLatch, traceKey);
        teacherPadAsyncService.doExcuteTeacherTagAsync(dataMap, coursePackage, countDownLatch, traceKey);
        doSomething(dataMap);
        defaultRedisUtil.setString(RedisKeyConfig.COURSE_PKG_DETAIL_KEY + id, JSON.toJSONString(dataMap), RedisKeyConfig.COURSE_PKG_DETAIL_EXPIRE_TIME);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("异步处理线程异常", e);
        }
    }
```

`teacherPadAsyncService.doExcuteLikeSateAsync()`这个方法是异步查询点赞状态，会在`dataMap`里面添加一个字段`state`，但是在第二次请求的时候有可能发现这个字段缺失，这只是其中一个BUG。原因在于往`redis`里面放置信息的时机不对，大概是由于写代码太着急，正确的做法应该是在异步转同步以后再去操作`redis`。下面是改之后的代码：

```Java
 @Override
    public void doExecute(Map<String, Object> dataMap) {
        String cache = defaultRedisUtil.getString(RedisKeyConfig.COURSE_PKG_DETAIL_KEY + id);
        if (StringUtils.isNotBlank(cache)) {
            dataMap = JSON.parseObject(cache, Map.class);
            return;
        }
        doSomething(dataMap);
        CountDownLatch countDownLatch = new CountDownLatch(3);
        String traceKey = TraceKeyHolder.getTraceKey();
        teacherPadAsyncService.doExcuteLikeSateAsync(dataMap, coursePackage.getId(),ResourceTypeEnum.COURSE_PACKAGE.value, currentUser.getSystemId(), countDownLatch, traceKey);
        teacherPadAsyncService.doExcuteAccuracyAsync(dataMap, coursePackage.getId(), countDownLatch, traceKey);
        teacherPadAsyncService.doExcuteTeacherTagAsync(dataMap, coursePackage, countDownLatch, traceKey);
        doSomething(dataMap);
        try {
            countDownLatch.await();
                    defaultRedisUtil.setString(RedisKeyConfig.COURSE_PKG_DETAIL_KEY + id, JSON.toJSONString(dataMap), RedisKeyConfig.COURSE_PKG_DETAIL_EXPIRE_TIME);
        } catch (InterruptedException e) {
            logger.error("异步处理线程异常", e);
        }
    }
```

BUG的原因也比较简单，由于第一次查询的时候`redis`里面内容时空的，所以走了数据库查询，查询到结果后，放到`redis`里面，但是在存`redis`时候，异步的查询任务并没有完成，导致第一次请求得多的响应是对的，但是`redis`里面存放的却是错误的。在缓存有效期内，查询的结果都将是错误的。

当然这个实现方法的BUG不止这一个，这里不列举了，有机会再分享。


---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)
- [Selenium 4.0 Alpha更新日志](https://mp.weixin.qq.com/s/tU7sm-pcbpRNwDU9D3OVTQ)
- [Selenium 4.0 Alpha更新实践](https://mp.weixin.qq.com/s/yT9wpO5o5aWBUus494TIHw)
- [如何统一接口测试的功能、自动化和性能测试用例](https://mp.weixin.qq.com/s/1xqtXNVw7BdUa03nVcsMTg)

## 非技术文章精选

- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)
- [API测试基础](https://mp.weixin.qq.com/s/bkbUEa9CF21xMYSlhPcULw)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)