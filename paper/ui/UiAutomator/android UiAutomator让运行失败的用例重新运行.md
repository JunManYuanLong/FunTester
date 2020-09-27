# android UiAutomator让运行失败的用例重新运行


本人在使用android UiAutomator做测试的时候，发现经常会因为页面加载太慢或者网络延迟比较大又或者出现意外情况导致用例失败，但是在检查的时候又能运行成功，提出了一个让失败的用例重新运行的需求，经过尝试终于成功了，使用excel作为测试报告的类型，html的类似，下面分享一下运行的代码，供大家参考。



```
List<String[]> firstsheet = new ArrayList<String[]>();//新建list，用于存放每个测试用例的测试结果
        String[] title = {"编号", "用例名", "运行状态", "错误信息", "错误行Library", "错误行Special", "错误行Case", "开始时间", "结束时间"};
        firstsheet.add(title);//把标题行先加入表信息
        new RunHelper(jarname, "1");//新编译jar包并push到手机上
        setMobileInputMethodToUtf();//设置手机输入法为UTF-7
        for(int i = 0;i < MethodList.size(); i++){//遍历运行所有方法  
        	String[] result = execCmdAndReturnResult(jarname, "student.Case", MethodList.get(i), i);//运行测试用例  
        	firstsheet.add(result);//将此次用例的测试结果放入list中  
            }
        List<String[]> secondsheet = new ArrayList<String[]>();//新建list，用于存放第二次的测试结果
        secondsheet.add(title);//把标题加入表中
        //遍历第一遍测试结果
        for(int s =0;s < firstsheet.size();s++){
        	String[] result = firstsheet.get(s);//遍历每一个用例的运行结果
        	if (!result[2].equals("运行成功")) {//获取运行未成功的用例集
				String[] second = execCmdAndReturnResult(jarname, "student.Case", result[1], s);//重新运行未成功用例
				secondsheet.add(second);//把第二次运行的结果加入了第二张表中
			}
        }
        Map<Integer, List<String[]>> report = new HashMap<Integer, List<String[]>>();//新建map，用于存放多张表格数据
        report.put(1, firstsheet);//把第一个表格的测试数据放入要写入到map里 
        report.put(2, secondsheet);//把第二个表格的测试数据放入要写入的map里
        Excel.writeXlsx(report);//把测试报告写入excel表格中
```

这次的注释写得有点乱，大家将就看看，希望能对你有所帮助。

### 往期文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
8. [如何测试概率型业务接口](https://mp.weixin.qq.com/s/kUVffhjae3eYivrGqo6ZMg)
9. [httpclient处理多用户同时在线](https://mp.weixin.qq.com/s/Nuc30Fwy6-Qyr-Pc65t1_g)
10. [成为杰出Java开发人员的10个步骤](https://mp.weixin.qq.com/s/UCNOTSzzvTXwiUX6xpVlyA)
11. [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
12. 

## [公众号地图](https://mp.weixin.qq.com/s/CJJ2g-RqzfBsbCCYKKp5pQ) ☢️ [一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)



> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>