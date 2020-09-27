# 如何对多行单次update接口进行压测

> 每一行的数据值允许update一次，百万级测试数据。

上次聊到如何对单行多次update进行压测，主要是为了解决单线程中请求参数如何每次都跟上次不一样这个难点。

本文讲的是多行单次update进行压测，就是大量数据需要在压测过程中update，但是每条数据只能update一次，在请求参数中必需保证，所有线程的请求参数必需都不一样，而且得跟已有的数据保持一致。

这里用到了之前讲到的线程安全的只是，思路如下：构建一个线程安全的队列，在压测前把数据读到这个队列中，然后压测开始后每次请求都先从这个队列中取参数。
还有一个思路，在压测前为每一个线程构建一个的队列（互不相同），这里可以非安全，压测过程中每一个线程从自己的队列中读取参数。

伪代码如下：

```
Queue q = Date.init();
//某一个线程开始循环开始
doRequest(q.take());//需要控制q的大小，也可以再新启线程不断往q添加数据
//单线程执行完成
```

其中，从队列中取值的方法根据用例设计不同而不同，也可以用poll()，防止阻塞。

分享一下我的代码：


```
public static LinkedBlockingQueue<String> msgs = addmsg();


    public static LinkedBlockingQueue<String> addmsg() {
        String absolutePath = new File("").getAbsolutePath();
        List<String> strings = WriteRead.readTxtFileByLine(absolutePath + "/queue");
        LinkedBlockingQueue<String> ss = new LinkedBlockingQueue<>();
        ss.addAll(strings);
        logger.info("重新读取队列值");
        return ss;
    }

public int deleteQ() throws InterruptedException {
        if (msgs.size() == 0) {
            logger.info("队列为空了");
            msgs = addmsg();
        }
        String absolutePath = new File("").getAbsolutePath();
        List<String> strings = WriteRead.readTxtFileByLine(absolutePath + "/dubbo");

        new Concurrent(new ThreadBase(SourceCode.changeStringToInt(strings.get(0))) {
            @Override
            protected void before() {

            }

            @Override
            protected void doing() throws Exception {
                String msg = msgs.poll(100, TimeUnit.MILLISECONDS);
                logger.info("msg:{}", msg);
                DeleteQueueRequest deleteQueueRequest0 = new DeleteQueueRequest();
                deleteQueueRequest0.setMsg(msg);
                deleteQueueRequest0.setTaskTypeEnum(TaskTypeEnum.PUBLISH_PROMU);
                CommonResponse<String> queue3 = commonDelayQueueService.deleteQueue(deleteQueueRequest0);
                logger.info("deleteQueue2 {}", JsonUtil.obj2Json(queue3));
            }

            @Override
            protected void after() {

            }
        }, SourceCode.changeStringToInt(strings.get(1))).start();

        return 0;
    }
```

这里分享了第一种思路的实现，由于测试数据数量巨大，所以不担心发生阻塞或者拿不到对象的情况。

## 技术类文章精选

- [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
- [五行代码构建静态博客](https://mp.weixin.qq.com/s/hZnimJOg5OqxRSDyFvuiiQ)
- [基于java的直线型接口测试框架初探](https://mp.weixin.qq.com/s/xhg4exdb1G18-nG5E7exkQ)

## 非技术文章精选
- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [未来10年软件测试的新趋势-上](https://mp.weixin.qq.com/s/9XgpIfXQRuKg1Pap-tfqYQ)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)

## 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
- [JVM的对象和堆](https://mp.weixin.qq.com/s/iNDpTz3gBK3By_bvUnrWOA)