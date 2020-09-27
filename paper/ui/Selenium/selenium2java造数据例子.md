# selenium2java造数据例子

本人在学习selenium2java的时候，有个功能，需要测试分页，每页20条数据，一个个添加太麻烦了，关键每条都得添加十几个数据，干脆自己写了一个方法，自动添加一条自己的高中成绩。分享如下：


```
//添加高中成绩
	public static void addRecord(WebDriver driver, int num) throws InterruptedException {
		findElementByIdAndClick(driver, "btn-user");//点击个人中心
		findElementByIdAndClick(driver, "btn-uc-record");//点击高中成绩
		for(int n = 0;n<num;n++){
			findElementByIdAndClick(driver, "btnAddRecord");//点击添加成绩
			findElementByIdAndClick(driver, "button-toggle-semester_id");//点击学期
			findElementByXpathAndClick(driver, ".//*[@id='dropdown-semester_id']/li["+getRandomInt(5)+"]/a");//选择学期
			findElementByIdAndClick(driver, "button-toggle-exam_id");//点击考试
			findElementByXpathAndClick(driver, ".//*[@id='dropdown-exam_id']/li["+getRandomInt(7)+"]/a");//点击老师类型
			findElementByIdAndClick(driver, "button-toggle-year");//点击年份
			findElementByXpathAndClick(driver, ".//*[@id='dropdown-year']/li["+getRandomInt(5)+"]/a");//选择年份
			/*此处循环填写各科成绩，理科物综合
			 * 语数英 id123，物化生456，政史地789，理综10，文综11
			 */
			for(int i =1;i<7;i++){
				findElementByIdAndClearSendkeys(driver, "input-score"+i, getRandomInt(100));
				findElementByIdAndClearSendkeys(driver, "input-total_score"+i, 100);
				}
			findElementByIdAndClearSendkeys(driver, "input-additional_score", getRandomInt(10));//政策加分
			findElementByIdAndClearSendkeys(driver, "input-ranking_province", getRandomInt(10000));//省排名
			findElementByIdAndClearSendkeys(driver, "input-ranking_city", getRandomInt(1000));//市排名
			findElementByIdAndClearSendkeys(driver, "input-ranking_district", getRandomInt(500));//区排名
			findElementByIdAndClearSendkeys(driver, "input-ranking_school", getRandomInt(200));//学校排名
			findElementByIdAndClearSendkeys(driver, "input-ranking_class", getRandomInt(50));//班级排名
			findElementByIdAndClick(driver, "btnSave");//点击保存
			sleep(1);
			findElementByXpathAndClick(driver, "html/body/div[3]/div[7]/div/button");//点击确定保存
			sleep(1);
			findElementByXpathAndClick(driver, "html/body/div[3]/div[7]/div/button");//点击保存成功的确定
			}
		}
```
下面是我自定义生成随机数字的方法

```
//获取随机数
	public static int getRandomInt(int num) {
		return new Random().nextInt(num)+1;
	}
```

本人最近读完一本书《质数的孤独》，里面讲到孪生质数，就想查一下孪生质数的分布情况。其中主要用到了计算质数（素数）的方法，搜了一下，排名前几的都是用for循环来做的，感觉略微麻烦了一些，在比较一些还是觉得用递归筛选法来解决这个问题。

新建List<Integer>，然后从第0位开始，如果后面的能被这个数整除，则从数组中移除改元素，以此类推，最后留下的就是质数（素数）。代码如下：
```
static void get(List<Integer> list, int tt) {
        int num = list.get(tt);
        for (int i = tt + 1; i < list.size(); i++) {
            if (list.get(i) % num == 0) list.remove(i--);
        }
        if (list.size() > ++tt) get(list, tt);
    }
```
然后再去做相邻元素差求得孪生质数（孪生素数），贴一下求10000以内孪生质数（孪生素数）全部的代码：
```
List<Integer> list = new ArrayList<>();
        for (int i = 2; i < 10000; i+=2) {
            list.add(i);
        }
        get(list, 0);
        for (int i = 0; i < list.size() - 1; i++) {
            Integer integer = list.get(i);
            Integer integer1 = list.get(i + 1);
            if (integer1 - integer == 2) outputData(TEST_ERROR_CODE, "孪生质数:", integer + TAB + TAB + integer1);
        }
```
最后附上一份冒泡排序和插入排序的练习代码：
```
   public static void ff(int[] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = i; j > 0; j--) {
                if (data[j] < data[j - 1]) {
                    int num = data[j];
                    data[j] = data[j - 1];
                    data[j - 1] = num;
                }
            }
        }
        output(changeArraysToList(data));
    }

    public static void ff1(int[] data) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length - i - 1; j++) {
                if (data[j] > data[j + 1]) {
                    int num = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = num;
                }
            }
        }
        output(changeArraysToList(data));
    }
```

> groovy是一种基于JVM的动态语言，我觉得最大的优势有两点，第一：于java兼容性非常好，大部分时候吧groovy的文件后缀改成java直接可以用，反之亦然。java的绝大部分库，groovy都是可以直接拿来就用的。这还带来了另外一个优点，学习成本低，非常低，直接上手没问题，可以慢慢学习groovy不同于Java的语法；第二：编译器支持变得更好，现在用的intellij的ide，总体来说已经比较好的支持groovy语言了，写起代码来也是比较顺滑了，各种基于groovy的框架工具也比较溜，特别是Gradle构建工具，比Maven爽很多。----此段文字为了撑字数强加的，与内容无关。


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
13. [Tcloud 云测平台--集大成者](https://mp.weixin.qq.com/s/29sEO39_NyDiJr-kY5ufdw)


### 非技术文章精选
1. [为什么选择软件测试作为职业道路?](https://mp.weixin.qq.com/s/o83wYvFUvy17kBPLDO609A)
2. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
3. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
4. [自动化测试的障碍](https://mp.weixin.qq.com/s/ZIV7uJp7DzVoKhWOh6lvRg)
5. [自动化测试的问题所在](https://mp.weixin.qq.com/s/BhvD7BnkBU8hDBsGUWok6g)
6. [测试之《代码不朽》脑图](https://mp.weixin.qq.com/s/2aGLK3knUiiSoex-kmi0GA)
7. [成为优秀自动化测试工程师的7个步骤](https://mp.weixin.qq.com/s/wdw1l4AZnPpdPBZZueCcnw)
8. [优秀软件开发人员的态度](https://mp.weixin.qq.com/s/0uEEeFaR27aTlyp-sm61bA)
9. [如何正确执行功能API测试](https://mp.weixin.qq.com/s/aeGx5O_jK_iTD9KUtylWmA)

# [点击查看公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>