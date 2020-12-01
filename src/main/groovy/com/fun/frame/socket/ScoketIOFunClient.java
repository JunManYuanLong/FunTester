package com.fun.frame.socket;

import com.fun.base.exception.FailException;
import com.fun.config.SocketConstant;
import com.fun.frame.SourceCode;
import com.fun.utils.RString;
import io.netty.util.internal.ConcurrentSet;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;

public class ScoketIOFunClient extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(ScoketIOFunClient.class);

    public static IO.Options options = initOptions();

    public static Vector<ScoketIOFunClient> clients = new Vector<>();

    public LinkedList<String> msgs = new LinkedList<>();

    private String cname;

    private String url;

    public Socket socket;

    public ConcurrentSet<String> events = new ConcurrentSet<>();


    private ScoketIOFunClient(String url, Socket socket) {
        this.url = url;
        this.socket = socket;
        clients.add(this);
    }

    public static ScoketIOFunClient getInstance(String url, String cname) {
        ScoketIOFunClient client = null;
        try {
            client = new ScoketIOFunClient(url, IO.socket(url, options));
            client.setCname(cname);
        } catch (URISyntaxException e) {
            FailException.fail();
        }
        return client;
    }


    /**
     * 初始化连接选项的方法,默认采取重置
     *
     * @return
     */
    public static IO.Options initOptions() {
        IO.Options options = new IO.Options();
        options.transports = SocketConstant.transports;
        //失败重试次数
        options.reconnectionAttempts = SocketConstant.MAX_RETRY;
        //失败重连的时间间隔
        options.reconnectionDelay = SocketConstant.RETRY_DELAY;
        //连接超时时间(ms)
        options.timeout = SocketConstant.TIMEOUT;
        return options;
    }

    /**
     * 注册通用的事件监听
     * {@link io.socket.client.Socket}
     */
    public void init() {
        this.socket.on(Socket.EVENT_CONNECTING, objects -> {
            logger.info("{} 正在连接...信息:{}", cname, initMsg(objects));
        });
        events.add(Socket.EVENT_CONNECTING);
        this.socket.on(Socket.EVENT_ERROR, objects -> {
            logger.info("{} 收到错误信息:{}", cname, initMsg(objects));
        });
        events.add(Socket.EVENT_ERROR);
        this.socket.on(Socket.EVENT_CONNECT_TIMEOUT, objects -> {
            logger.info("{} 连接超时!,url:{},信息:{}", cname, url, initMsg(objects));
        });
        events.add(Socket.EVENT_CONNECT_TIMEOUT);
        this.socket.on(Socket.EVENT_CONNECT_ERROR, objects -> {
            logger.info("{} 连接错误,信息:{}", cname, initMsg(objects));
        });
        events.add(Socket.EVENT_CONNECT_ERROR);
        /*此处统一的message做记录*/
        this.socket.on(Socket.EVENT_MESSAGE, objects -> {
            String msg = initMsg(objects);
            saveMsg(msg);
            logger.info("{} 收到消息事件,信息:{}", cname, msg);
        });
    }

    /**
     * 开始建立socket连接
     */
    public void connect() {
        this.socket.connect();
        logger.info("{} 开始连接...", cname);
        this.socket.connect();
        int a = 0;
        while (true) {
            if (this.socket.connected()) break;
            if ((a++ > SocketConstant.MAX_RETRY)) FailException.fail(cname + "连接重试失败!");
            SourceCode.sleep(SocketConstant.WAIT_INTERVAL);
        }
        logger.info("{} 连接成功!", cname);
    }

    /**
     * 添加监听事件
     *
     * @param event
     * @param fn
     */
    public void addEventListener(String event, Emitter.Listener fn) {
        events.add(event);
        this.socket.on(event, fn);
    }

    public void send(String event, Object... objects) {
        events.add(event);
        this.socket.emit(event, objects);
    }

    public void close() {
        logger.info("{} socket链接关闭!", cname);
        clients.remove(this);
        this.socket.close();
    }

    /**
     * 初始化收到的信息
     *
     * @param objects
     * @return
     */
    public static String initMsg(Object... objects) {
        if (ArrayUtils.isEmpty(objects)) return EMPTY;
        return Arrays.toString(objects);
    }

    @Override
    public ScoketIOFunClient clone() {
        return getInstance(this.url, this.cname + RString.getString(4));
    }

    /**
     * 设置cname,多用于性能测试clone()之后
     *
     * @param cname
     */
    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCname() {
        return cname;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    /**
     * 关闭所有socketclient
     */
    public static void closeAll() {
        clients.forEach(x ->
                {
                    if (x != null && x.socket.connected()) x.close();
                }
        );
        clients.clear();
        logger.info("关闭所有Socket客户端!");
    }


}
