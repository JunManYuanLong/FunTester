# 利用alertover发送获取响应失败的通知消息



本人在做接口自动化时候，因为服务器不稳定造成可能的用例失败，但这个失败表象只是在获取响应实体的json对象时为空，在后期排查问题时可能造成困扰，所以特意加了一个获取响应失败的通知，目的就是即使了解到服务器异常。暂时用的是免费的alertover，用了很久，简单可靠是它的优点，后续会加入微信提醒。分享代码，供大家参考。

下面是获取响应实体的json对象的方法（可忽略某一些封装方法）：

```
    /**
     * 获取响应实体
     * <p>会自动设置cookie，但是需要各个项目再自行实现cookie管理</p>
     * <p>该方法只会处理文本信息，对于文件处理可以调用两个过期的方法解决</p>
     *
     * @param request 请求对象
     * @return 返回json类型的对象
     */
    public static JSONObject getHttpResponse(HttpRequestBase request) {
        if (!isRightRequest(request)) return new JSONObject();
        beforeRequest(request);
        JSONObject res = new JSONObject();
        RequestInfo requestInfo = new RequestInfo(request);
        if (HEADER_KEY) output("===========request header===========", Arrays.asList(request.getAllHeaders()));
        long start = Time.getTimeStamp();
        try (CloseableHttpResponse response = ClientManage.httpsClient.execute(request)) {
            long end = Time.getTimeStamp();
            long elapsed_time = end - start;
            if (HEADER_KEY) output("===========response header===========", Arrays.asList(response.getAllHeaders()));
            int status = getStatus(response, res);
            JSONObject setCookies = afterResponse(response);
            String content = getContent(response);
            int data_size = content.length();
            res.putAll(getJsonResponse(content, setCookies));
            int code = iBase == null ? -2 : iBase.checkCode(res, requestInfo);
            if (!iBase.isRight(res))
                new AlertOver("响应状态码错误：" + status, "状态码错误：" + status, requestInfo.getUrl(), requestInfo).sendSystemMessage();
            MySqlTest.saveApiTestDate(requestInfo, data_size, elapsed_time, status, getMark(), code, LOCAL_IP, COMPUTER_USER_NAME);
        } catch (Exception e) {
            logger.warn("获取请求相应失败！", e);
            if (!SysInit.isBlack(requestInfo.getHost()))
                new AlertOver("接口请求失败", requestInfo.toString(), requestInfo.getUrl(), requestInfo).sendSystemMessage();
        } finally {
            HEADER_KEY = false;
            if (!SysInit.isBlack(requestInfo.getHost())) {
                if (requests.size() > 9) requests.removeFirst();
                boolean add = requests.add(request);
            }
        }
        return res;
    }
```

下面是alertover类的代码，比较简单：

```
package com.fun.utils.message;

import com.fun.frame.httpclient.FanLibrary;
import com.fun.base.bean.RequestInfo;
import com.fun.base.interfaces.IMessage;
import com.fun.db.mysql.MySqlTest;
import com.fun.config.SysInit;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlertOver extends FanLibrary implements IMessage {

    private static Logger logger = LoggerFactory.getLogger(AlertOver.class);

    String title;

    String content;

    String murl;

    RequestInfo requestInfo;

    private static String system = "s-7e93ec02-1308-480c-bc11-a7260c14";//系统异常

    private static String function = "s-7e3b7ea5-b4b0-4479-a0e3-bce6c830";//功能异常

    private static String business = "s-466a191a-cbb8-4164-b8be-9779bb88";//业务异常

    private static String remind = "s-f49ac5bc-008b-4b11-890e-6715ef89";//提醒推送

    private static String code = "s-490d0fc6-35cc-4430-9f87-09cdeb05";//程序异常

    private static final String testGroup = "g-4eefc0ad-19af-4b1c-9d0b-ef87be15";

    public AlertOver() {
        this("test title", "test content!");
    }

    public AlertOver(String title, String content) {
        this.title = title;
        this.content = content + LINE + "发送源：" + COMPUTER_USER_NAME;
    }

    public AlertOver(String title, String content, String url) {
        this(title, content);
        this.murl = url;
    }

    public AlertOver(String title, String content, String url, RequestInfo requestInfo) {
        this(title, content);
        this.murl = url;
        this.requestInfo = requestInfo;
    }

    /**
     * 发送系统异常
     */
    public void sendSystemMessage() {
        if (SysInit.isBlack(murl)) return;
        sendMessage(system);
        MySqlTest.saveAlertOverMessage(requestInfo, "system", title, LOCAL_IP, COMPUTER_USER_NAME);
        logger.info("发送系统错误提醒，title：{}，ip：{}，computer：{}", title, LOCAL_IP, COMPUTER_USER_NAME);
    }

    /**
     * 发送功能异常
     */
    public void sendFunctionMessage() {
        sendMessage(function);
    }

    /**
     * 发送业务异常
     */
    public void sendBusinessMessage() {
        sendMessage(business);
    }

    /**
     * 发送程序异常
     */
    public void sendCodeMessage() {
        sendMessage(code);
    }

    /**
     * 提醒推送
     */
    public void sendRemindMessage() {
        sendMessage(remind);
    }

    /**
     * 发送消息
     *
     * @return
     */
    public void sendMessage(String source) {
        if (SysInit.isBlack(murl)) return;
        String url = "https://api.alertover.com/v1/alert";
        String receiver = testGroup;//测试组ID
        JSONObject jsonObject = new JSONObject();// 新建json数组
        jsonObject.put("frame", source);// 添加发送源id
        jsonObject.put("receiver", receiver);// 添加接收组id
        jsonObject.put("content", content);// 发送内容
        jsonObject.put("title", title);// 发送标题
        jsonObject.put("url", murl);// 发送标题
        jsonObject.put("sound", "pianobar");// 发送声音
        logger.debug("消息详情：{}", jsonObject.toString());
        HttpPost httpPost = getHttpPost(url, jsonObject);
        取消发送
        getHttpResponse(httpPost);
    }
}

```

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

