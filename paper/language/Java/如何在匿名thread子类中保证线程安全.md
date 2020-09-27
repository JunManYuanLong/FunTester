# 如何在匿名thread子类中保证线程安全

在做性能测试的过程中，我写了两个虚拟类`ThreadLimitTimeCount`和`ThreadLimitTimesCount`做框架，通过对线程的标记来完成超时请求的记录。旧方法如下：


```Java
 @Override
    protected void after() {
        requestMark.addAll(marks);
        marks = new ArrayList<>();
        GCThread.stop();
        synchronized (this.getClass()) {
            if (countDownLatch.getCount() == 0 && requestMark.size() != 0) {
                Save.saveStringList(requestMark, MARK_Path.replace(LONG_Path, EMPTY) + Time.getDate().replace(SPACE_1, CONNECTOR));
                requestMark = new Vector<>();
            }
        }
    }
```

其中我用了`synchronized`关键字同步，但是在匿名类的单元测试中出现一个BUG，匿名类中没有实现`clone()`方法，也不能直接使用深拷贝方法，导致无法直接复制对象，所以我创建了多个功能相同的匿名线程类。问题来了，在代码执行过程中，偶然会出现记录`markrequest`的文档中出现空内容的形式。

我查询了一些资料，感觉问题出现在`synchronized (this.getClass())`这个问题了，因为我打印`this.getClass()`给我的是当前测试类的类名，感觉原因就是匿名类的问题，匿名类相当于多个实现类，`synchronized (this.getClass())`无法保证多各类对象同时访问这个方法的线程安全。最终，我选择了另外一种方式，就是单独写一个线程安全的`save()`方法，这样就可以保证所有访问保存方法的线程的安全，将清空记录列表的功能也放在了这个线程安全的方法里了。


```Java
    /**
     * 同步save数据,用于匿名类多线程保存测试数据
     *
     * @param data
     * @param name
     */
    public static void saveStringListSync(Collection<String> data, String name) {
        synchronized (Save.class) {
            if (data.isEmpty()) return;
            saveStringList(data, name);
        }
    }
```

原来虚拟类的方法就变成了如下的样子：


```Java
            if (countDownLatch.getCount() == 0 && requestMark.size() != 0) {
                Save.saveStringListSync(requestMark, MARK_Path.replace(LONG_Path, EMPTY) + Time.getDate().replace(SPACE_1, CONNECTOR));
            }
```


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