# 如何对单行多次update接口进行压测

> update某一个字段值和原来值相同时性能比不同时更高。

在对服务端进行测试的时候，经常会遇到这类情况：单个接口的功能就是修改数据库中某一条数据某个字段的值。在对这类接口进行压测的过程中，遇到一个难点，如何每次都设置不同的值，当然可以通过获取一类的随机数的形式规避掉重复的概率，但是在特定场景下依然无法解决，比如字段值范围偏小。

在工作中遇到一个典型的案例就是对于用户性别的修改，用户性别属性在数据库中以0-3的数字表示无，男，女，保密。在接口参数中也只有四个选择变量值。

这这种情况下，使用刚才的方案采取随机数的情况就难以实现，因为重复的概率很大。

下面是我当时采取的测试方案（仅供参考）：

并发策略依然采用之前的方案：一个线程绑定一个用户，不断发起请求。

解决传参重复：每个线程绑定一个integer类对象，然后每次把对象的值当做性别的值放到请求参数里面去。完成请求之后，执行“++”操作，然后再执行对4取模然后赋值本身。伪代码如下：

```
int i;
//循环开始
doRequest(i);
i++;
i=i%4;
//循环结束
//单线程执行完成
```
通过这样的方法即可解决请求参数可能跟原有值重复问题，如果还需要对每一次结果进行验证，可继续在接口方法中进行拓展。
下面是我用Java写的一个demo，如果是Groovy写脚本的话会更简单一些。
```
package com.fun;

import com.fun.base.constaint.ThreadBase;
import com.fun.frame.SourceCode;
import com.fun.frame.excute.Concurrent;
import com.fun.frame.httpclient.FanLibrary;

import java.util.ArrayList;
import java.util.List;

public class G extends SourceCode {

    public static void main(String[] args) {
        int threadNum = 100;
        int times = 1000;
        List<ThreadBase> threadTask = new ArrayList<>();
        for (int i = 0; i < threadNum; i++) {
            ThreadBase<Integer> threadBase = new ThreadBase<Integer>(i, times) {
                User user;

                @Override
                protected void before() {
                    user = new User(t);
                }

                @Override
                protected void doing() throws Exception {
                    user.doRequest(t++);
//                    user.check();//校验
                    t = t % 4;
                }

                @Override
                protected void after() {

                }
            };
            threadTask.add(threadBase);
        }
        Concurrent concurrent = new Concurrent(threadTask);
        concurrent.start();
        FanLibrary.testOver();
    }


}

class User {

    public User(int i) {

    }

    public void doRequest(int i) {
        System.out.println("完成！" + i);
    }

    public boolean check() {
        return true;
    }
}
```

然后脚本写完之后还可以通过把线程数和请求数进行参数化来让使用更加灵活。

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
