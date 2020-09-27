# java如何将json数据格式化输出到控制台

在做接口测试的过程中，大多数数据交互都使用json格式，但是在控制台输出json的效果远不如浏览器插件实现的漂亮。在找了一些资料后，决定自己写一个格式化输出json信息到控制台的方法，经过一些尝试已经完成。分享如下：

```
    /**
     * 输出json
     *
     * @param jsonObject json格式响应实体
     */
    public static JSONObject output(JSONObject jsonObject) {
        if (MapUtils.isEmpty(jsonObject)) {
            output("json 对象是空的！");
            return jsonObject;
        }
        String start = SourceCode.getManyString(SPACE_1, 4);
        String jsonStr = jsonObject.toString();// 先将json对象转化为string对象
        jsonStr = jsonStr.replaceAll("\\\\/", OR);
        int level = 0;// 用户标记层级
        StringBuffer jsonResultStr = new StringBuffer("＞  ");// 新建stringbuffer对象，用户接收转化好的string字符串
        for (int i = 0; i < jsonStr.length(); i++) {// 循环遍历每一个字符
            char piece = jsonStr.charAt(i);// 获取当前字符
            // 如果上一个字符是断行，则在本行开始按照level数值添加标记符，排除第一行
            if (i != 0 && '\n' == jsonResultStr.charAt(jsonResultStr.length() - 1)) {
                for (int k = 0; k < level; k++) {
                    jsonResultStr.append(start);
                }
            }
            switch (piece) {
                case ',':
                    // 如果是“,”，则断行
                    char last = jsonStr.charAt(i - 1);
                    if ("\"0123456789le]}".contains(last + EMPTY)) {
                        jsonResultStr.append(piece + LINE);
                    } else {
                        jsonResultStr.append(piece);
                    }
                    break;
                case '{':
                case '[':
                    // 如果字符是{或者[，则断行，level加1
                    jsonResultStr.append(piece + LINE);
                    level++;
                    break;
                case '}':
                case ']':
                    // 如果是}或者]，则断行，level减1
                    jsonResultStr.append(LINE);
                    level--;
                    for (int k = 0; k < level; k++) {
                        jsonResultStr.append(start);
                    }
                    jsonResultStr.append(piece);
                    break;
                default:
                    jsonResultStr.append(piece);
                    break;
            }
        }
        output(LINE + "↘ ↘ ↘ ↘ ↘ ↘ ↘ ↘ json ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙" + LINE + jsonResultStr.toString().replaceAll(LINE, LINE + "＞  ") + LINE + "↘ ↘ ↘ ↘ ↘ ↘ ↘ ↘ json ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙ ↙");
        return jsonObject;
    }
    
```

更新了输出显示效果：

![](/blog/pic/2018081617301199.png)


备注：在json数据中存在“,”这种情况以及value非string（数字和null或者科学技术法时）可能会存在一些显示异常，以及value值本身是一个json数据的字符串时候也会把value当做json来处理。总体来说够用，效果比较满意。

[一起来~FunTester](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483866&idx=3&sn=2ef9d9bdcc49b5e52fcb3b6f35396a5e&chksm=fd4a8cecca3d05fafee68d4a9f9024ffc950cb66809d28f0ec3f8ee1ce280349f27d5352314c&scene=21#wechat_redirect)

### 往期文章精选

1. [java一行代码打印心形](https://mp.weixin.qq.com/s/QPSryoSbViVURpSa9QXtpg)
2. [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
3. [接口测试代码覆盖率（jacoco）方案分享](https://mp.weixin.qq.com/s/D73Sq6NLjeRKN8aCpGLOjQ)
4. [性能测试框架](https://mp.weixin.qq.com/s/3_09j7-5ex35u30HQRyWug)
5. [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
6. [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
7. [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
8. [测试之JVM命令脑图](https://mp.weixin.qq.com/s/qprqyv0j3SCvGw1HMjbaMQ)

## [公众号地图](http://mp.weixin.qq.com/s?__biz=MzU4MTE2NDEyMQ==&mid=2247483929&idx=2&sn=5f720d91eea288869bd2242e27412262&chksm=fd4a8f2fca3d063951370261e99097122338848741480768dfb6be4e33dff641daa40afa52d0&scene=21#wechat_redirect)


> 欢迎有兴趣的一起交流：群号:340964272

![](/blog/pic/201712120951590031.png)


<a href="/blog/home.html">返回首页</a><a href="/blog/交个朋友.html"  style="float:right;">交个朋友</a>
---


<script src="/blog/js/bubbly.js"></script>
<script src="/blog/js/article.js"></script>