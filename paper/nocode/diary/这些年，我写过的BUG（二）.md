# 这些年，我写过的BUG（二）

> BUG是最好的学习素材。


最近的**BUG**都不疼不痒，基本秒修复。昨天遇到一个大坑，修复了好几个小时。这是一个事务挂起导致数据库连接未释放，然后导致获取数据库连接失败的**BUG**。

## 场景

运行测试用例集（包含多个测试用例），处理逻辑如下：1、首先去并发处理用例参数，例如关联用户的登录状态（这个比较麻烦，请参考旧文内容：[我的开发日记（十五）](https://mp.weixin.qq.com/s/bwkvz2t6YItQD0O_BIxpHQ)中的*分布式锁*的实现）；2、把用例组装成多线程任务，丢到线程池去执行；3、异步等待所有用例执行完成，处理数据，异步写入数据库。

## BUG代码


```Java
    /**
     * 获取用户登录凭据,map缓存
     *
     * @param id
     * @param map
     * @return
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
    public String getCertificate(int id, ConcurrentHashMap<Integer, String> map) {
        if (map.contains(id)) return map.get(id);
        Object o = UserLock.get(id);
        synchronized (o) {
            if (map.contains(id)) return map.get(id);
            logger.warn("非缓存读取用户数据{}", id);
            TestUserCheckBean user = testUserMapper.findUser(id);
            if (user == null) UserStatusException.fail("用户不存在,ID:" + id);
            String create_time = user.getCreate_time();
            long create = Time.getTimestamp(create_time);
            long now = Time.getTimeStamp();
            if (now - create < OkayConstant.CERTIFICATE_TIMEOUT && user.getStatus() == UserState.OK.getCode()) {
                map.put(id, user.getCertificate());
                return user.getCertificate();
            }
            boolean b = UserUtil.checkUserLoginStatus(user);
            if (!b) {
                updateUserStatus(user);
                if (user.getStatus() != UserState.OK.getCode()) UserStatusException.fail("用户不可用,ID:" + id);
            } else {
                testUserMapper.updateUserStatus(user);
            }
            map.put(id, user.getCertificate());
            return user.getCertificate();
        }
    }

```


## BUG分析

这里犯了两个错误：

### 判断key方法错误

应该使用`map.containsKey(id)`来判断，而不是`map.contains(id)`，可以看一下`map.contains(id)`的源码：


```Java

    /**
     * Legacy method testing if some key maps into the specified value
     * in this table.  This method is identical in functionality to
     * {@link #containsValue(Object)}, and exists solely to ensure
     * full compatibility with class {@link java.util.Hashtable},
     * which supported this method prior to introduction of the
     * Java Collections framework.
     *
     * @param  value a value to search for
     * @return {@code true} if and only if some key maps to the
     *         {@code value} argument in this table as
     *         determined by the {@code equals} method;
     *         {@code false} otherwise
     * @throws NullPointerException if the specified value is null
     */
    public boolean contains(Object value) {
        return containsValue(value);
    }
```

其实`map.contains(id)`查的**value**而不是**key**，导致很多多余的查询和其他操作。

### 事务传播行为

具体知识点参考旧文：[我的开发日记（三）](https://mp.weixin.qq.com/s/a-I0agh6nWp8RLlcmbgf5w)中对于**事务隔离级别**和**事务传播行为**的记录。

这里的`REQUIRES_NEW`表示*REQUIRES_NEW ：创建一个新的事务，如果当前存在事务，则把当前事务挂起。*

每一个事务都会占用一个连接，然后会把之前的事务挂起等待，这样就导致会占用很多数据库连接而不释放。再加上本身有很多读写数据库的操作，所以导致了下面的报错：


```Java

2020-07-29 10:27:50 ERROR com.okay.family.service.impl.CaseCollectionServiceImpl:287 [] [Thread-176] 处理用例参数发生错误!
org.springframework.transaction.CannotCreateTransactionException: Could not open JDBC Connection for transaction; nested exception is java.sql.SQLTransientConnectionException: HikariPool-1 - Connection is not available, request timed out after 30006ms.
	at org.springframework.jdbc.datasource.DataSourceTransactionManager.doBegin(DataSourceTransactionManager.java:308)
	at org.springframework.transaction.support.AbstractPlatformTransactionManager.startTransaction(AbstractPlatformTransactionManager.java:400)
	at org.springframework.transaction.support.AbstractPlatformTransactionManager.getTransaction(AbstractPlatformTransactionManager.java:373)
	at org.springframework.transaction.interceptor.TransactionAspectSupport.createTransactionIfNecessary(TransactionAspectSupport.java:572)
	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:360)
	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:118)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
	at org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:212)
	at com.sun.proxy.$Proxy82.handleParams(Unknown Source)
	at com.okay.family.service.impl.CaseCollectionServiceImpl.lambda$null$4(CaseCollectionServiceImpl.java:282)
```

## 解决办法

### 调整事务传播行为

删除`REQUIRES_NEW`设置，恢复默认值。

### 设置超时时间

数据库连接池获取连接超时时间设置：

`spring.datasource.hikari.connection-timeout=3000`


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