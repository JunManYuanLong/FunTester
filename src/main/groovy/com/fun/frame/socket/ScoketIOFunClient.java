package com.fun.frame.socket;

import com.fun.base.exception.FailException;
import com.fun.frame.SourceCode;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;

public class ScoketIOFunClient extends SourceCode {

    private static Logger logger = LoggerFactory.getLogger(ScoketIOFunClient.class);

    public static IO.Options options = initOptions();

    public LinkedList<String> msgs = new LinkedList<>();

    String cname;

    String url;

    Socket socket;

    public ScoketIOFunClient(String url, Socket socket) {
        this.url = url;
        this.socket = socket;
    }

    public static ScoketIOFunClient getInstance(String url, String cname) {
        ScoketIOFunClient client = null;
        try {
            client = new ScoketIOFunClient(url, IO.socket(url, options));
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
        options.transports = new String[]{"websocket"};
        //失败重试次数
        options.reconnectionAttempts = 5;
        //失败重连的时间间隔
        options.reconnectionDelay = 1000;
        //连接超时时间(ms)
        options.timeout = 500;
        return options;
    }

    /**
     * 注册通用的事件监听
     * {@link io.socket.client.Socket}
     */
    public void init() {
        socket.on(Socket.EVENT_CONNECTING, objects -> {
            logger.info("{} 正在连接...信息:{}", cname, initMsg(objects));
        });
        socket.on(Socket.EVENT_ERROR, objects -> {
            logger.info("{} 收到错误信息:{}", cname, initMsg(objects));
        });
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, objects -> {
            logger.info("{} 连接超时!,url:{},信息:{}", cname, url, initMsg(objects));
        });
        socket.on(Socket.EVENT_CONNECT_ERROR, objects -> {
            logger.info("{} 连接错误,信息:{}", cname, initMsg(objects));
        });
        socket.on(Socket.EVENT_MESSAGE, objects -> {
            logger.info("{} 收到消息事件,信息:{}", cname, initMsg(objects));
        });
    }




    public void addEventListener(String event, Emitter.Listener fn) {
        socket.on(event, fn);
    }

    private String initMsg(Object... objects) {
        if (objects == null || objects.length == 0) return EMPTY;
        return Arrays.toString(objects);
    }


}
