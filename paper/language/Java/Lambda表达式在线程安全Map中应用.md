# Lambda表达式在线程安全Map中应用

[原文地址](https://dzone.com/articles/lambdas-for-concurrent-maps)

`java.util.concurrent`包含两个线程安全的`Map`，即`ConcurrentHashMap`类和`ConcurrentSkipListMap`类。这两个类都是线程安全的和高性能的。但是由于读取修改写入竞争条件，因此使用它们容易出错。`Lambda`表达式帮助我们优雅地避免了这些竞争条件。

# 错误Demo

当我们从`ConcurrentHashMap`中读取元素，修改该元素并将该元素写回到`Map`中时，多线程操作就会发生竞争，请参考：[原子操作组合与线程安全](https://mp.weixin.qq.com/s/XB5LXucAF5Bo8EkfLZYRmw)。如以下示例所示：

```Java
package com.fun;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

public class TestFun {

    public void update(ConcurrentHashMap<Integer, Integer> map) {
        Integer result = map.get(1);
        if (result == null) {
            map.put(1, 1);
        } else {
            map.put(1, result + 1);
        }
    }

    @Test
    public void testUpdate() throws InterruptedException {
        final ConcurrentHashMap<Integer, Integer> map =
                new ConcurrentHashMap<Integer, Integer>();
        Thread first = new Thread(() -> {
            update(map);
            update(map);
            update(map);
            update(map);
            update(map);
        });
        Thread second = new Thread(() -> {
            update(map);
            update(map);
            update(map);
            update(map);
            update(map);
        });
        Thread third = new Thread(() -> {
            update(map);
            update(map);
            update(map);
            update(map);
            update(map);
        });
        first.start();
        second.start();
        third.start();
        first.join();
        second.join();
        third.join();
        assertEquals(15, map.get(1).intValue());
    }


}
```

在这里，我们实现了每个按键计数器。在**update**方法中，如果不存在映射，则将计数初始化为**1**，否则将计数加**1**。为了重现竞争条件，我们从三个不同的线程更新了`ConcurrentHashMap`。在线程都停止之后，我们检查该值是否跟方法的调用次数一致。

# 控制台输出

* 这里效果不明显，可以增加线程更容易复现这个BUG。

```shell
java.lang.AssertionError: 
Expected :15
Actual   :10
<Click to see difference>

	at org.junit.Assert.fail(Assert.java:88)
	at org.junit.Assert.failNotEquals(Assert.java:834)
	at org.junit.Assert.assertEquals(Assert.java:645)
	at org.junit.Assert.assertEquals(Assert.java:631)
	at com.fun.TestFun.testUpdate(TestFun.java:51)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:497)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
	at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:33)
	at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:230)
	at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:58)


Process finished with exit code 255

```


# 利用Lambda表达式避免读取、修改、写入竞争条件

为了避免这种竞争情况，我们需要一种方法来执行所有三个操作，即**读取**，**修改**和**写入**单个原子方法调用。该方法`compute`使用`lambda`表达式来做到这一点：


```Java
public void update(
    ConcurrentHashMap<Integer,Integer>  map ) {
    map.compute(1, (key, value) -> {
        if (value == null) {
            return 1;
         } 
            return value + 1;
    });
}
```


现在，**读取**，**修改**和**写入**操作以一种原子方法发生，并且竞争消失了。

# Lambdas需要是纯净的

`ConcurrentHashMap`中的`lambda`表达式应该在节点的同步锁下执行。因此，其他线程不得调用此`ConcurrentHashMap`对应节点实例的其他写入操作。

对于的`compute`的源码如下：


```Java
    public V compute(K key,
                     BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (key == null || remappingFunction == null)
            throw new NullPointerException();
        int h = spread(key.hashCode());
        V val = null;
        int delta = 0;
        int binCount = 0;
        for (Node<K,V>[] tab = table;;) {
            Node<K,V> f; int n, i, fh;
            if (tab == null || (n = tab.length) == 0)
                tab = initTable();
            else if ((f = tabAt(tab, i = (n - 1) & h)) == null) {
                Node<K,V> r = new ReservationNode<K,V>();
                synchronized (r) {
                    if (casTabAt(tab, i, null, r)) {
                        binCount = 1;
                        Node<K,V> node = null;
                        try {
                            if ((val = remappingFunction.apply(key, null)) != null) {
                                delta = 1;
                                node = new Node<K,V>(h, key, val, null);
                            }
                        } finally {
                            setTabAt(tab, i, node);
                        }
                    }
                }
                if (binCount != 0)
                    break;
            }
            else if ((fh = f.hash) == MOVED)
                tab = helpTransfer(tab, f);
            else {
                synchronized (f) {
                    if (tabAt(tab, i) == f) {
                        if (fh >= 0) {
                            binCount = 1;
                            for (Node<K,V> e = f, pred = null;; ++binCount) {
                                K ek;
                                if (e.hash == h &&
                                    ((ek = e.key) == key ||
                                     (ek != null && key.equals(ek)))) {
                                    val = remappingFunction.apply(key, e.val);
                                    if (val != null)
                                        e.val = val;
                                    else {
                                        delta = -1;
                                        Node<K,V> en = e.next;
                                        if (pred != null)
                                            pred.next = en;
                                        else
                                            setTabAt(tab, i, en);
                                    }
                                    break;
                                }
                                pred = e;
                                if ((e = e.next) == null) {
                                    val = remappingFunction.apply(key, null);
                                    if (val != null) {
                                        delta = 1;
                                        pred.next =
                                            new Node<K,V>(h, key, val, null);
                                    }
                                    break;
                                }
                            }
                        }
                        else if (f instanceof TreeBin) {
                            binCount = 1;
                            TreeBin<K,V> t = (TreeBin<K,V>)f;
                            TreeNode<K,V> r, p;
                            if ((r = t.root) != null)
                                p = r.findTreeNode(h, key, null);
                            else
                                p = null;
                            V pv = (p == null) ? null : p.val;
                            val = remappingFunction.apply(key, pv);
                            if (val != null) {
                                if (p != null)
                                    p.val = val;
                                else {
                                    delta = 1;
                                    t.putTreeVal(h, key, val);
                                }
                            }
                            else if (p != null) {
                                delta = -1;
                                if (t.removeTreeNode(p))
                                    setTabAt(tab, i, untreeify(t.first));
                            }
                        }
                    }
                }
                if (binCount != 0) {
                    if (binCount >= TREEIFY_THRESHOLD)
                        treeifyBin(tab, i);
                    break;
                }
            }
        }
        if (delta != 0)
            addCount((long)delta, binCount);
        return val;
    }
```

可以看到其中多次用的了**synchronized**关键词完成线程同步，每次锁住对节点对象，操作完成之后释放锁。

--- 
* **郑重声明**：公众号“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester十八张原创专辑](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**，合作请联系`Fhaohaizi@163.com`。

### 热文精选

- [接口功能测试专辑](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1321895538945638401&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [性能测试专题](https://mp.weixin.qq.com/mp/appmsgalbum?action=getalbum&album_id=1319027448301961218&__biz=MzU4MTE2NDEyMQ==#wechat_redirect)
- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
- [如何mock固定QPS的接口](https://mp.weixin.qq.com/s/yogj9Fni0KJkyQuKuDYlbA)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [Selenium并行测试最佳实践](https://mp.weixin.qq.com/s/-RsQZaT5pH8DHPvm0L8Hjw)
- [如何维护自动化测试](https://mp.weixin.qq.com/s/4eh4AN_MiatMSkoCMtY3UA)
- [有关UI测试计划](https://mp.weixin.qq.com/s/D0fMXwJF754a7Mr5ARY5tQ)

![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)