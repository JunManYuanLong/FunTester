# java网格输出的类--练习stream


在做测试的时候，经常需要把一些信息输出到控制台，但是格式上比较乱，想弄成一个类似SQL客户端的那个输出格式，在参考了一些资料后自己写了一个简单的控制台网格输出的类，分享代码供大家参考。

使用方法：暂时支持了map和list两种类型的数据展示，并没有提供header功能。


```
public static void main(String[] args) {

        List<String> ss0 = Arrays.asList("234", "432", "54");
        List<String> ss3 = Arrays.asList("234", "432", "54", "54", "54");
        List<String> ss1 = Arrays.asList("6546", "7675");
        Map<String, String> sss = new HashMap<>();
        sss.put(getNanoMark() + EMPTY, "fdsf");
        sss.put(getNanoMark() + EMPTY, "fdsfdsaff");
        sss.put(getNanoMark() + EMPTY, "fdsf");
        sss.put(getNanoMark() + EMPTY, "fdsfafdsf");
        sss.put(getNanoMark() + EMPTY, "fdsf");
        sss.put(getMark() + EMPTY, "fdsf");
        show(sss);
        List<List<String>> rows = Arrays.asList(ss1, ss3, ss0);
        show(rows);
        JSONObject json = new JSONObject();
        json.put("3234", 32432);
        json.put("323dsa4", 32432);
        json.put("3fdsa234", 32432);
        json.put("323fdsf4", 32432);
        json.put("32d34", 32432);
        json.put("32fdsafdf34", 32432);
        show(json);
    }
```
效果展示：
![](/blog/pic/QQ20190619-163255.png)
类代码如下（下一步优化其他数据类型和header以及边栏支持）：


```
package com.fun.utils;

import com.fun.frame.SourceCode;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ConsoleTable extends SourceCode {

    List<Integer> rowLength = new ArrayList<>();

    public static void show(Map map) {
        new ConsoleTable(map);
    }

    public static void show(List<List<String>> rows) {
        new ConsoleTable(rows);
    }

    /**
     * 输出map
     *
     * @param map
     */
    private ConsoleTable(Map map) {
        Set set = map.keySet();
        int asInt0 = set.stream().mapToInt(key -> key.toString().length()).max().getAsInt();
        rowLength.add(asInt0 + 2);
        List<String> values = new ArrayList<>();
        set.forEach(key -> values.add(map.get(key).toString()));
        int asInt1 = values.stream().mapToInt(value -> value.length()).max().getAsInt();
        rowLength.add(asInt1 + 2);
        StringBuffer stringBuffer = new StringBuffer(LINE + getHeader());
        map.forEach((k, v) -> {
            stringBuffer.append(getCel(0, k.toString()));
            stringBuffer.append(getCel(1, v.toString()));
        });
        output(stringBuffer.append(LINE + getHeader()).toString());
    }

    /**
     * 输出list
     *
     * @param rows
     */
    private ConsoleTable(List<List<String>> rows) {
        for (int i = 0; i < rows.size(); i++) {
            List<String> line = rows.get(i);
            for (int j = 0; j < line.size(); j++) {
                String s = line.get(j);
                if (rowLength.size() <= j) rowLength.add(0);
                if (rowLength.get(j) < s.length()) rowLength.set(j, s.length());
            }
        }
        rowLength = rowLength.stream().map(n -> n + 2).collect(Collectors.toList());
        StringBuffer stringBuffer = new StringBuffer(LINE + getHeader());
        for (int i = 0; i < rows.size(); i++) {
            List<String> line = rows.get(i);
            for (int j = 0; j < rowLength.size(); j++) {
                stringBuffer.append(getCel(j, j < line.size() ? line.get(j) : EMPTY));
            }
        }
        output(stringBuffer.append(LINE + getHeader()).toString());
    }


    /**
     * 获取每一格的string
     *
     * @param colum   列
     * @param content 格内容
     * @return
     */
    public String getCel(int colum, String content) {
        Integer integer = rowLength.get(colum);
        int i = integer - content.length();
        return (colum == 0 ? LINE + PART : PART) + getManyString(SPACE_1, i / 2) + content + getManyString(SPACE_1, i - i / 2) + (rowLength.size() - colum == 1 ? PART : EMPTY);
    }

    /**
     * 获取头尾行
     *
     * @return
     */
    private String getHeader() {
        List<String> collect = rowLength.stream().map(size -> getManyString("-", size)).collect(Collectors.toList());
        return "+" + StringUtils.join(collect.toArray(), "+") + "+";
    }


}
```

这里使用了一些stream的用法，虽然比较生疏，但是效果很不错，stream语法很强大，使得编码效率急速提升。在groovy语言使用java的stream各种方法时，还是遇到了不少的坑，目前主要还是符号兼容的问题比较多，所以尽量还是java stream的自己的语法比较好。


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
- [手动测试存在的重要原因](https://mp.weixin.qq.com/s/mW5vryoJIkeskZLkBPFe0Q)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [功能测试与非功能测试](https://mp.weixin.qq.com/s/oJ6PJs1zO0LOQSTRF6M6WA)
- [自动化和手动测试，保持平衡！](https://mp.weixin.qq.com/s/mMr_4C98W_FOkks2i2TiCg)
- [自动化测试生命周期](https://mp.weixin.qq.com/s/SH-vb2RagYQ3sfCY8QM5ew)

## 大咖风采
- [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)
- [Android App 测试工具及知识大集合](https://mp.weixin.qq.com/s/Xk9rCW8whXOTAQuCfhZqTg)
- [4399AT UI自动化CI与CD](https://mp.weixin.qq.com/s/cVwg8ddnScWPX4uldsJ0fA)
- [Android App常规测试内容](https://mp.weixin.qq.com/s/tweeoS5wTqK3k7R2TVuDXA)
- [JVM的对象和堆](https://mp.weixin.qq.com/s/iNDpTz3gBK3By_bvUnrWOA)
- [JVM虚拟机面试大全](https://mp.weixin.qq.com/s/WPll-3ZvYrS7J7Cl8MuzhA)

![长按关注](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzBEASPySoVdOFmP12QUIWAQms664L0b82nic8BRIlufg0QibzXNnoibZp8yqhU9Pv0hXjKtqrGof8kMA/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)