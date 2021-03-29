package com.funtester.utils.message;

import com.funtester.base.bean.RequestInfo;
import com.funtester.base.interfaces.IMessage;
import com.funtester.httpclient.FunLibrary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AlertOver extends FunLibrary implements IMessage {

    private static Logger logger = LogManager.getLogger(AlertOver.class);

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
//        if (SysInit.isBlack(murl)) return;
//        sendMessage(system);
//        MySqlTest.saveAlertOverMessage(requestInfo, "system", title, LOCAL_IP, COMPUTER_USER_NAME);
//        logger.info("发送系统错误提醒，title：{}，ip：{}，computer：{}", title, LOCAL_IP, COMPUTER_USER_NAME);
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
//        if (SysInit.isBlack(murl)) return;
//        String url = "https://api.alertover.com/v1/alert";
//        String receiver = testGroup;//测试组ID
//        JSONObject jsonObject = new JSONObject();// 新建json数组
//        jsonObject.put("frame", source);// 添加发送源id
//        jsonObject.put("receiver", receiver);// 添加接收组id
//        jsonObject.put("content", content);// 发送内容
//        jsonObject.put("title", title);// 发送标题
//        jsonObject.put("url", murl);// 发送标题
//        jsonObject.put("sound", "pianobar");// 发送声音
//        logger.debug("消息详情：{}", jsonObject.toString());
//        HttpPost httpPost = getHttpPost(url, jsonObject);
        /*取消发送*/
//        getHttpResponse(httpPost);
    }


}