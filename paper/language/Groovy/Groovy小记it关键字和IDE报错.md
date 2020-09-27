# Groovy小记it关键字和IDE报错

在工作中经常写`Groovy`脚本，由于`Groovy`与`Java`的亲戚关系，所以也就直接在**Intellij IDEA**工具上写了，一是因为方便，二是也找不到其他工具了。

但是在使用的过程中总会遇到一些兼容性的问题。**Intellij IDEA**对于`Groovy`的支持并不完美，大概也是`Groovy`语言特性过多的原因。我下载了一个`Groovy`检查的插件，依然还有漏网之鱼。下面分享一些大大小小的**坑**，以方便后来之人。

## IDE报错

在使用`Groovy`语法特性的时候，经常会遇到标红和报错，但是**Intellij IDEA**依然会允许程序运行。但是在运行中可能会遇到错误，还得返回来检查，甚至有时候`IDE`还不会提示这个错误。所以我在写`Groovy`脚本时候，之前是更多偏重于用`Java`的语法。最近改变了自己的习惯之后，发现有一部分`IDE`报错其实没有问题的。例如下面的两个例子：

### 获取属性

`Groovy`获取属性有三种写法。


```Groovy
user.name
user["name"]
user.getName()
```

对的，你没有看错，第一个写法和第三个写法在`Groovy`里面包含了不同的含义，因为`Groovy`正常情况下没有`getName()`这个方法。至于第二种写法算是特殊的语法。

上面三个语法不仅是针对`User`对象，对于`JSonobject`依然适用，我在使用`JsonPath`工具实践的时候也经常用到。有兴趣可以参考以下文章：

- [JsonPath实践（一）](https://mp.weixin.qq.com/s/Cq0_v_ptbGd4f5y8HIsq7w)
- [JsonPath实践（二）](https://mp.weixin.qq.com/s/w_iJTiuQahIw6U00CJVJZg)
- [JsonPath实践（三）](https://mp.weixin.qq.com/s/58A3k0T6dbOkBJ5nRYKDqA)
- [JsonPath实践（四）](https://mp.weixin.qq.com/s/8ER61qrkMj8bdBpyuq9r6w)
- [JsonPath实践（五）](https://mp.weixin.qq.com/s/knVLW960WXnckGLstdrOVQ)
- [JsonPath实践（六）](https://mp.weixin.qq.com/s/ckBCK3t1w68FLBhaw5a7Jw)
- [JsonPath工具类封装](https://mp.weixin.qq.com/s/KyuCuG5fVEExxBdGJO2LdA)
- [JsonPath工具类单元测试](https://mp.weixin.qq.com/s/1YtUWGk_sTjn9bHwAeT0Ew)

但是第二种写法在**Intellij IDEA**里面就会被标红，提示出错。

### 泛型方法调用

这是是我在做性能测试的过程中遇到的，我在`ThreadBase<T>`定义了一个对象`    public T t;`，最开始的想法是用来处理线程私有数据对象的，但是后来发现其实直接新建类比较合适，就用的不多了。

有兴趣的可以参考以前的文章：

- [性能框架多线程基类和执行类--视频讲解](https://mp.weixin.qq.com/s/8Dh-5XfvX8Fm4IqmzbtY6Q)
- [定时和定量压测模式实现--视频讲解](https://mp.weixin.qq.com/s/l_4wCjVM1fAVRHgEPrcrwg)
- [基于HTTP请求的多线程实现类--视频讲解](https://mp.weixin.qq.com/s/8SG1xtzq8ArY84Bxm_SNow)

先发一个第一种写法，也是我现在最常用的写法。

```Groovy
    static class TT extends ThreadLimitTimesCount {

        Mirro m

        int aid = 0

        public TT(Mirro mirro, int times) {
            super()
            this.m = mirro
            this.times = times
        }

        @Override
        protected void doing() throws Exception {
            def pre = m.createPre()
            if (aid == 0) {
                aid = pre.getJSONObject("data").getIntValue("activity_id")
            }
            m.delPre(aid)
        }
    }
```

再发一下之前的写法。


```Groovy
    static class TT extends ThreadLimitTimesCount {

        int aid = 0

        public TT(Mirro mirro, int times) {
            super()
            this.t = mirro
            this.times = times
        }

        @Override
        protected void doing() throws Exception {
            def pre = m.createPre()
            if (aid == 0) {
                aid = pre.getJSONObject("data").getIntValue("activity_id")
            }
            t.delPre(aid)
        }
    }
```

已经取消了`Mirro`这个属性，但是在最后会有报错，如图：

![](http://pic.automancloud.com/WX20200912-150635.png)

这种情况还会出现在`def`修饰的对象调用方法获取属性的时候，这个时候就需要**微微一笑**，**由他去吧**。

## it关键字

在`Groovy`语言中循环中，`it`关键非常有用，比如遍历一个`String`数组或者集合对象的话，直接用`it`就可以代表遍历到的`String`对象。举个例子：


```Groovy
        5.times {
            output(it)
        }
```

控制台输出：


```shell
INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 0
INFO-> 1
INFO-> 2
INFO-> 3
INFO-> 4

Process finished with exit code 0
```

今天遇到一个双层遍历的问题，突然发现不知道在双层遍历的情况下`it`关键字是否可以通用，也就是在外层遍历时候使用`it`，然后在内层遍历时候也用`it`，两个`it`表示不同的遍历对象。下面我们验证一下：


```Java
        3.times {
            output(it * 10)
            3.upto(5) {
                output(it)
            }
        }
```

控制台输出：


```Groovy

INFO-> 当前用户：fv，IP：10.60.192.21，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.6
INFO-> 0
INFO-> 3
INFO-> 4
INFO-> 5
INFO-> 10
INFO-> 3
INFO-> 4
INFO-> 5
INFO-> 20
INFO-> 3
INFO-> 4
INFO-> 5

Process finished with exit code 0

```

果然完美解决我的疑惑，不得不钦佩`Groovy`的设计者。

----
**公众号[FunTester](https://mp.weixin.qq.com/s/s7ZmCNBYy3j-71JFbtgneg)首发，原创分享爱好者，腾讯云和掘金社区首页推荐，知乎七级原创作者，欢迎关注、交流，禁止第三方擅自转载。**

FunTester热文精选
=

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [2020年Tester自我提升](https://mp.weixin.qq.com/s/vuhUp85_6Sbg6ReAN3TTSQ)
- [未来的神器fiddler Everywhere](https://mp.weixin.qq.com/s/-BSuHR6RPkdv8R-iy47MLQ)
- [测试开发工程师工作技巧](https://mp.weixin.qq.com/s/TvrUCisja5Zbq-NIwy_2fQ)
- [Selenium4 IDE，它终于来了](https://mp.weixin.qq.com/s/XNotlZvFpmBmBQy1pYifOw)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [什么阻碍手动测试发挥价值](https://mp.weixin.qq.com/s/t0VAVyA3ywQsHzaqzSILOw)
- [压测中测量异步写入接口的延迟](https://mp.weixin.qq.com/s/odvK1iYgg4eRVtOOPbq15w)
- [“双花”BUG的测试分享](https://mp.weixin.qq.com/s/0dsBsssNfg-seJ_tu9zFaQ)


![](https://mmbiz.qpic.cn/mmbiz_png/13eN86FKXzDnHxttBoq6jhgic4jJF8icbAMdOvlR0xXUX9a3tupYYib3ibYyIHicNtefS3Jo7yefLKlQWgLK7bCgCLA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)