package com.fun.frame.socket;

import com.alibaba.fastjson.JSONObject;
import com.fun.base.bean.AbstractBean;
import com.fun.base.exception.ParamException;
import com.fun.utils.RString;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Vector;

/**
 * socket客户端代码
 */
@SuppressFBWarnings({"CN_IMPLEMENTS_CLONE_BUT_NOT_CLONEABLE", "DM_DEFAULT_ENCODING", "MS_SHOULD_BE_FINAL"})
public class SocketClient extends WebSocketClient implements Serializable {

    private static final long serialVersionUID = 1306796619468953402L;

    private static Logger logger = LoggerFactory.getLogger(SocketClient.class);


    public static Vector<SocketClient> socketClients = new Vector<>();

    private String name;

    private SocketClient(URI uri) {
        this(uri, Thread.currentThread().getName());
    }

    private SocketClient(URI uri, String name) {
        super(uri);
        this.name = name;
        socketClients.add(this);
    }

    public static SocketClient getInstance(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            ParamException.fail("创建socket client 失败! 原因:" + e.getMessage());
        }
        return new SocketClient(uri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        logger.info("开始建立socket连接...");
        handshakedata.iterateHttpFields().forEachRemaining(x -> logger.info("握手信息key: {} ,value: {}", x, handshakedata.getFieldValue(x)));
    }

    /**
     * 收到消息时候调用的方法
     *
     * @param message
     */
    @Override
    public void onMessage(String message) {
        logger.info("收到: {}", message);
    }

    /**
     * 关闭,存疑.线程结束会自动关闭.不可调用websocketclient中的clone()方法
     *
     * @param code   关闭code码,详情查看 {@link org.java_websocket.framing.CloseFrame}
     * @param reason 关闭原因
     * @param remote
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.info("socket 连接关闭...;code码:{},原因:{},是否由远程服务关闭:{}", code, reason, remote);
        try {
            Socket socket = getSocket();
            if (!socket.isClosed()) socket.close();
        } catch (IOException e) {
            logger.error("socket连接关闭失败!", e);
        }finally {
            socketClients.remove(this);
        }
    }

    @Override
    public void onError(Exception e) {
        logger.error("socket异常!", e);
    }

    @Override
    public void send(String text) {
        logger.debug("发送:{}", text);
        super.send(text);
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
    public SocketClient clone() {
        return new SocketClient(this.uri,this.name+ RString.getString(1));
    }


}
