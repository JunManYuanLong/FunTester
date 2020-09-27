# 爬虫实践--CBA历年比赛数据

闲来无聊，刚好有个朋友来问爬虫的事情，说起来了CBA这两年的比赛数据，做个分析，再来个大数据啥的。来了兴趣，果然搞起来，下面分享一下爬虫的思路。

## 1、选取数据源
这里我并不懂CBA，数据源选的是国内某门户网站的CBA专栏，下面会放链接地址，有兴趣的可以去看看。

## 2、分析数据
经过查看页面元素，发现页面是后台渲染，没办法通过接口直接获取数据。下面就要分析页面元素，看到所有的数据都是存在表格里面的，这下就简单了很多。

## 3、确定思路
思路比较简单，通过正则把所有行数据都提取出来，过滤掉无用的修饰信息，得到的就是想要的数据。此处我把每行的列符合替换成了“,”方便用csv记录数据。

经过过滤之后的数据如下：

```
球队,第一节,第二节,第三节,第四节,总比分
广州,33,37,36,27,133
北控,23,18,17,34,92
2019-01-1619:35:00轮次：31场序309开始比赛　　比赛已结束
首发,球员,出场时间,两分球,三分球,罚球,进攻,篮板,助攻,失误,抢断,犯规,盖帽,得分
,张永鹏,25.8,7-9,0-0,1-1,4,8,3,0,0,1,0,15
,鞠明欣,19.1,2-4,1-2,0-0,2,5,2,2,0,1,0,7
,西热力江,25.5,1-1,4-8,0-0,1,2,4,1,3,1,0,14
,郭凯,15.5,2-2,0-0,0-0,2,3,0,2,0,2,0,4
,凯尔·弗格,38.1,5-9,5-9,11-11,0,10,12,2,2,4,0,36
,姚天一,12.3,0-1,1-4,0-0,0,1,5,0,0,0,0,3
,科里·杰弗森,24.0,4-4,2-4,3-4,0,6,0,1,0,1,1,17
,陈盈骏,22.6,1-1,2-7,1-1,0,2,4,2,1,2,0,9
,司坤,19.0,2-2,0-2,0-0,0,5,1,0,1,4,0,4
,孙鸣阳,20.6,2-3,0-0,3-3,1,4,1,2,3,4,0,7
,谷玥灼,7.4,1-1,1-2,0-0,0,0,2,0,0,0,0,5
,郑准,10.1,3-4,2-3,0-0,0,2,0,0,0,1,0,12
,总计,240.0,30-41(73.2%),18-41(43.9%),19-20(95.0%),10,48,34,12,10,21,1,133
首发,球员,出场时间,两分球,三分球,罚球,进攻,篮板,助攻,失误,抢断,犯规,盖帽,得分
,于梁,20.8,1-3,0-1,0-0,0,0,2,0,1,5,0,2
,于澍龙,17.9,0-1,1-3,0-0,0,2,1,2,0,1,0,3
,许梦君,46.2,1-3,5-12,0-0,1,6,2,1,0,3,0,17
,托马斯·罗宾逊,43.4,9-20,0-2,9-14,3,11,5,2,1,3,1,27
,杨敬敏,16.0,3-4,0-3,0-0,0,0,0,2,0,1,0,6
,孙贺男,2.8,0-0,0-0,0-0,0,0,0,1,0,1,0,0
,刘大鹏,28.0,1-1,3-5,0-0,1,4,3,2,2,3,0,11
,张铭浩,8.5,0-0,0-0,1-2,0,0,0,0,1,1,0,1
,张帆,27.5,5-7,1-3,0-0,0,1,6,4,1,2,0,13
,王征,23.3,3-3,0-0,6-8,0,2,0,0,1,1,1,12
,常亚松,5.6,0-0,0-1,0-0,0,1,0,1,2,0,0,0
,总计,240.0,23-42(54.8%),10-30(33.3%),16-24(66.7%),5,27,19,15,9,21,2,92
```

下面分享自己代码：


```
package com.fun

import com.fun.frame.Save
import com.fun.frame.httpclient.FanLibrary
import com.fun.utils.Regex
import com.fun.utils.WriteRead

class sd extends FanLibrary {

    public static void main(String[] args) {
        int i = 1
        def total = []
        range(300, 381).forEach {x ->
            total.addAll test(x)
        }
        Save.saveStringList(total, "total4.csv")
        testOver()
    }


    static def test(int i) {
        if (new File(LONG_Path + "${i}.csv").exists()) return WriteRead.readTxtFileByLine(LONG_Path + "${i}.csv")
        String url = "http://cbadata.sports.sohu.com/game/content/2017/${i}"

        def get = getHttpGet(url)

        def response = getHttpResponse(get)


        def string = response.getString("content").replaceAll("\\s", EMPTY)
//        output(string)
        def all = Regex.regexAll(string, "<tr.*?<\\/tr>")
        def list = []
        all.forEach {x ->
            def info = x.replaceAll("</*?tr.*?>", EMPTY).replaceAll("</t(d|h)>", ",")
            info = info.replaceAll("<.*?>", EMPTY)

            info = info.charAt(info.length() - 1) == ',' ? info.substring(0, info.length() - 1) : info
            if (info.startsWith("总计")) info = "," + info
            list << info
            output(info)

        }
        Save.saveStringList(list, "${i}.csv")
        return list
    }

}
```

有兴趣的，可以后台回复“大爷来玩啊”获取本人微信号，咱们私聊。

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
- [JUnit中用于Selenium测试的中实践](https://mp.weixin.qq.com/s/KG4sltQMCfH2MGXkRdtnwA)

## 非技术文章精选

- [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [自动化和手动测试，保持平衡！](https://mp.weixin.qq.com/s/mMr_4C98W_FOkks2i2TiCg)
- [自动化测试生命周期](https://mp.weixin.qq.com/s/SH-vb2RagYQ3sfCY8QM5ew)
- [如何在DevOps引入自动化测试](https://mp.weixin.qq.com/s/MclK3VvMN1dsiXXJO8g7ig)

## 大咖风采

- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
- [JVM的对象和堆](https://mp.weixin.qq.com/s/iNDpTz3gBK3By_bvUnrWOA)

![长按关注](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)