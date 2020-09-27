# python用递归筛选法求N以内的孪生质数（孪生素数）--附冒泡排序和插入排序练习
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
本人最近读完一本书《质数的孤独》，里面讲到孪生质数，就想查一下孪生质数的分布情况。其中主要用到了计算质数（素数）的方法，搜了一下，排名前几的都是用for循环来做的，感觉略微麻烦了一些，在比较一些还是觉得用递归筛选法来解决这个问题。

新建List，然后从第0位开始，如果后面的能被这个数整除，则从数组中移除改元素，以此类推，最后留下的就是质数（素数）。

python版本与java版本不同，java可以在遍历list的时候删除该元素，可以对循环变量i进行i--的操作，防止以后的get(i)方法报错，python不支持这个操作只能是拿到被删除的元素，然后在遍历结束以后再去删除

代码如下：

```
#!/usr/bin/python3
 
class Test():
    def __init__(self):
        print ("fan")
    def get(self,list,st):
        n = list[st]
        a = []
        for i in range(st+1,len(list)):
            if list[i] % n == 0:
                a.append(list[i])
        for x in a:
            list.remove(x)
        if len(list) > st+1:
            self.get(list,st+1)
if __name__ == "__main__":
    test = Test()
    list = [i for i in range(2,5000)]
    test.get(list,0)
    for i in range(len(list)-1):
        a = list[i]
        b = list[i+1]
        if b-a==2:
            print ("孪生质数："+str(a)+"----"+str(b))
```
> 这里备注一下：python为了防止内存溢出，限制了递归的深度，所以直接求10000以内的还不行，会报错：

`RecursionError: maximum recursion depth exceeded in comparison`

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

### 非技术文章精选
1. [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
2. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
3. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
4. [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
5. [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
6. [测试之《代码不朽》脑图](https://mp.weixin.qq.com/s/2aGLK3knUiiSoex-kmi0GA)

# [点击查看公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ)



最后附上一份冒泡排序和插入排序的练习代码：

```
    def fan(self,list):
        for i in range(len(list)):
            for j in range(i,0,-1):
                a = list[j]
                b = list[j-1]
                if b > a :
                    c = a
                    list[j]=b
                    list[j-1]=c
    def fan2(self,list):
        for i in range(len(list)):
            for j in range(len(list) - i - 1):
                a = list[j]
                b = list[j+1]
                if a > b :
                    c = a
                    list[j] = b
                    list[j + 1] = c
```

> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)
<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---
<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>