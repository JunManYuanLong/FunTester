# android UiAutomator自己写了一个简单测试框架
本人在做UiAutomator自动化测试的时候，偶然跟前辈聊起来现在做的事情和实现的功能，聊着聊着好像自己已经写了一个测试框架的大概。受宠若惊，在整理之后，特来分享，里面的代码我基本上都已经分享过了，所以这次主要两张图片和一些文字为主。若有不足，请不吝赐教。

下面这张图我是画了一个整体的框架个各类之间的关系和作用。
![](/blog/pic/20170801092645560.png)

下面这个接受建议换了一种方式。
![](/blog/pic/20170801092645560.png)

下面是一些自己的一些笔记。

> library 封装基本方法，wait，click，get…
> base 封装最小功能点
> special 封装功能模块及组合功能点
> case 用例编写（断言、log、截图、自定义信息）
> report 运行用例，生成报告文件夹（包括html或Excel测试报告、运行截图、log日志）
> Excel 用于读取数据、保存测试结果、生成测试报告，用例管理。
> html 用于生成测试报告（js美化，截图）
> mysql 辅助初始化测试数据
> map<interlist<String>> 用于进行用例管理（分组，排序、关联账号），依赖性测试，失败用例再次运行
> runhelper 生成测试jar包并初始化手机测试环境

<br></br>
> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)

<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>