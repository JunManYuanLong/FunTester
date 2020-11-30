package com.fun.frame.socket;

import com.alibaba.fastjson.JSONObject;
import com.fun.base.bean.AbstractBean;
import com.fun.base.exception.FailException;
import com.fun.base.exception.ParamException;
import com.fun.config.Constant;
import com.fun.config.SocketConstant;
import com.fun.frame.SourceCode;
import com.fun.utils.RString;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Vector;

/**
 * socket客户端代码,限于WebSocket协议的测试
 */
@SuppressFBWarnings({"CN_IMPLEMENTS_CLONE_BUT_NOT_CLONEABLE", "DM_DEFAULT_ENCODING", "MS_SHOULD_BE_FINAL"})
public class WebSocketFunClient extends WebSocketClient {

    private static Logger logger = LoggerFactory.getLogger(WebSocketFunClient.class);

    public static Vector<WebSocketFunClient> socketClients = new Vector<>();

    /**
     * 存储收到的消息
     */
    public LinkedList<String> msgs = new LinkedList<>();

    /**
     * 连接的url
     */
    private String url;

    /**
     * 客户端名称
     */
    private String cname;

    private WebSocketFunClient(String url, String cname) throws URISyntaxException {
        super(new URI(url));
        this.cname = cname;
        this.url = url;
        socketClients.add(this);
    }

    /**
     * 获取socketclient实例
     *
     * @param url
     * @return
     */
    public static WebSocketFunClient getInstance(String url) {
        return getInstance(url, Constant.DEFAULT_STRING + RString.getString(4));
    }

    /**
     * 获取socketclient实例
     *
     * @param url
     * @param cname
     * @return
     */
    public static WebSocketFunClient getInstance(String url, String cname) {
        WebSocketFunClient client = null;
        try {
            client = new WebSocketFunClient(url, cname);
        } catch (URISyntaxException e) {
            ParamException.fail(cname + "创建socket client 失败! 原因:" + e.getMessage());
        }
        return client;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        logger.info("{} 正在建立socket连接...", cname);
        handshakedata.iterateHttpFields().forEachRemaining(x -> logger.info("握手信息key: {} ,value: {}", x, handshakedata.getFieldValue(x)));
    }

    /**
     * 收到消息时候调用的方法
     *
     * @param message
     */
    @Override
    public void onMessage(String message) {
        saveMsg(message);
        logger.info("{}收到: {}", cname, message);
    }

    /**
     * 关闭
     *
     * @param code   关闭code码,详情查看 {@link org.java_websocket.framing.CloseFrame}
     * @param reason 关闭原因
     * @param remote
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.info("{} socket 连接关闭,URL: {} ,code码:{},原因:{},是否由远程服务关闭:{}", cname, url, code, reason, remote);
    }

    /**
     * 关闭socketclient
     */
    @Override
    public void close() {
        logger.warn("{}:socket连接关闭!", cname);
        super.close();
    }

    /**
     * 出错时候调用
     *
     * @param e
     */
    @Override
    public void onError(Exception e) {
        logger.error("{} socket异常,URL: {}", cname, url, e);
    }

    /**
     * 发送消息
     *
     * @param text
     */
    @Override
    public void send(String text) {
        logger.debug("{} 发送:{}", cname, text);
        super.send(text);
    }

    /**
     * 简历socket连接
     */
    @Override
    public void connect() {
        logger.info("{} 开始连接...", cname);
        super.connect();
        int a = 0;
        while (true) {
            if (this.getReadyState() == ReadyState.OPEN) break;
            if ((a++ > SocketConstant.MAX_WATI_TIMES)) FailException.fail(cname + "连接重试失败!");
            SourceCode.sleep(SocketConstant.WAIT_INTERVAL);
        }
        logger.info("{} 连接成功!", cname);
    }

    /**
     * 发送非默认编码格式的文字
     *
     * @param text
     * @param charset
     */
    public void send(String text, Charset charset) {
        send(new String(text.getBytes(), charset));
    }

    /**
     * 发送json信息
     *
     * @param json
     */
    public void send(JSONObject json) {
        send(json.toJSONString());
    }

    /**
     * 发送bean
     *
     * @param bean
     */
    public void send(AbstractBean bean) {
        send(bean.toString());
    }

    /**
     * 该方法用于性能测试中,clone多线程对象
     *
     * @return
     */
    @Override
    public WebSocketFunClient clone() {
        return getInstance(this.url, this.cname + RString.getString(4));
    }


    /**
     * 重置连接
     */
    @Override
    public void reconnect() {
        logger.info("{}重置连接并尝试重新连接!", cname);
        super.reconnect();
    }

    /**
     * 设置cname,多用于性能测试clone()之后
     *
     * @param cname
     */
    public void setCname(String cname) {
        this.cname = cname;
    }

    /**
     * 关闭所有socketclient
     */
    public static void closeAll() {
        socketClients.forEach(x ->
                {
                    if (x != null && !x.isClosed()) x.close();
                }
        );
        logger.info("关闭所有Socket客户端!");
    }

    /**
     * 保存收到的信息,只保留最近的{@link SocketConstant}条
     *
     * @param msg
     */
    public void saveMsg(String msg) {
        synchronized (msgs) {
            if (msgs.size() > SocketConstant.MAX_MSG_SIZE) msgs.remove();
            msgs.add(msg);
        }
    }


}
