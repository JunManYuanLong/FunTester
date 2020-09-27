# ArrayList浅、深拷贝

[原文地址](https://www.javacodegeeks.com/2020/04/arraylist-clone-arraylist-deep-copy-and-shallow-copy.html)

# 简介

`ArrayList`深拷贝和浅拷贝。`ArrayList`的`clone()`方法用于创建`list`的浅表副本。在新列表中，仅复制对象引用。如果我们在第一个`ArrayList`中更改对象状态，则更改后的对象状态也将反映在克隆的`ArrayList`中。

# 浅拷贝示例

使用`clone()`将`String`列表复制到新列表的Demo。


```Java
package com.fun;

import java.util.ArrayList;

public class AR  {
    
    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        list.add("four");
        list.add("give");

        System.out.println("原始对象 : " + list);

        ArrayList clonedLis = (ArrayList) list.clone();

        System.out.println("拷贝对象 : " + clonedLis);

    }


}

```

控制台输出：


```shell
INFO-> 当前用户：fv，IP：192.168.0.103，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.4
原始对象 : [one, two, three, four, give]
拷贝对象 : [one, two, three, four, give]

Process finished with exit code 0
```

# 浅拷贝之引用对象

示例程序将`ArrayList`与自定义对象进行浅表复制。克隆列表之后，修改原始对象。


```Java
package com.fun;


import java.util.ArrayList;

public class AR {

    public static void main(String[] args) {

        ArrayList<Student> list = new ArrayList<>();
        list.add(new Student(18, "fun"));
        list.add(new Student(20, "tester"));
        ArrayList<Student> clonedList = (ArrayList) list.clone();
        Student student = clonedList.get(1);
        student.name = "FunTester";
        System.out.println("原始对象 : " + clonedList);
        System.out.println("拷贝对象 : " + list);
    }


}

class Student {

    public int id;

    public String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "学生信息:id=" + id + ", name=" + name + "]";
    }


}
```

控制台输出：

```shell
INFO-> 当前用户：fv，IP：192.168.0.103，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.4
原始对象 : [学生信息:id=18, name=fun], 学生信息:id=20, name=FunTester]]
拷贝对象 : [学生信息:id=18, name=fun], 学生信息:id=20, name=FunTester]]

Process finished with exit code 0
```

* 可以看出来浅拷贝是引用拷贝。

# 深度复制示例

下面的程序创建对象的深层副本。对克隆列表的修改不会影响原始列表。


```Java
package com.fun;


import com.fun.frame.httpclient.FanLibrary;

import java.util.ArrayList;

public class AR extends FanLibrary {

    public static void main(String[] args) {
        ArrayList<Student> list = new ArrayList<>();
        list.add(new Student(100, "fun"));
        list.add(new Student(101, "tester"));
        ArrayList<Student> clonedList = new ArrayList<>();
        for (Student student : list) {
            clonedList.add(student.clone());
        }
        Student student = clonedList.get(1);
        student.name = "FunTester";
        System.out.println("Cloned list : " + clonedList);
        System.out.println("Original list : " + list);

    }


}


class Student {

    public int id;

    public String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "学生信息:id=" + id + ", name=" + name + "]";
    }

    @Override
    public Student clone() {
        return new Student(this.id, this.name);
    }


}
```

控制台输出：

```shell
INFO-> 当前用户：fv，IP：192.168.0.103，工作目录：/Users/fv/Documents/workspace/fun/,系统编码格式:UTF-8,系统Mac OS X版本:10.15.4
Cloned list : [学生信息:id=100, name=fun], 学生信息:id=101, name=FunTester]]
Original list : [学生信息:id=100, name=fun], 学生信息:id=101, name=tester]]

Process finished with exit code 0
```

---
参考文章：
- [从JVM堆内存分析验证深浅拷贝](https://mp.weixin.qq.com/s/SdYDnoau1rjjvPC2SUymBg)
- [拷贝HttpRequestBase对象](https://mp.weixin.qq.com/s/kxB1c0GmSF5OAM15UQJU2Q)

---
* **郑重声明**：“FunTester”首发，欢迎关注交流，禁止第三方转载。更多原创文章：**[FunTester原创合集](https://mp.weixin.qq.com/s/Le-tpC79pIpacHXGOkkYWw)**。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [性能测试中图形化输出测试数据](https://mp.weixin.qq.com/s/EMvpYIsszdwBJFPIxztTvA)
- [JMeter吞吐量误差分析](https://mp.weixin.qq.com/s/jHKmFNrLmjpihnoigNNCSg)
- [JMeter如何模拟不同的网络速度](https://mp.weixin.qq.com/s/1FCwNN2htfTGF6ItdkcCzw)
- [手机号验证码登录性能测试](https://mp.weixin.qq.com/s/i-j8fJAdcsJ7v8XPOnPDAw)
- [接口测试视频专题](https://mp.weixin.qq.com/s/4mKpW3QiVRee3kcVOSraog)

## 无代码文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [好书推荐《Java性能权威指南》](https://mp.weixin.qq.com/s/YWd5Yx6n7887g1lMLTcsWQ)
- [自动化新手要避免的坑（上）](https://mp.weixin.qq.com/s/MjcX40heTRhEgCFhInoqYQ)
- [自动化新手要避免的坑（下）](https://mp.weixin.qq.com/s/azDUo1IO5JgkJIS9n1CMRg)
- [如何成为全栈自动化工程师](https://mp.weixin.qq.com/s/j2rQ3COFhg939KLrgKr_bg)
- [简化测试用例](https://mp.weixin.qq.com/s/BhwfDqhN9yoa3Iul_Eu5TA)
- [自动化策略六步走](https://mp.weixin.qq.com/s/He69k8iCKhTKD1j-yV6M5g)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCxr0Sa2MXpNKicZE024zJm73r4hrjticMMYViagtaSXxwsyhmRmOrdXPXfS5zB2ILHtaqNSoWGRwa8Q/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)