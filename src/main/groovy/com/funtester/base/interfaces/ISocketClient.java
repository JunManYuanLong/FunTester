package com.funtester.base.interfaces;

import com.alibaba.fastjson.JSONObject;
import com.funtester.socket.ScoketIOFunClient;
import com.funtester.socket.WebSocketFunClient;

import java.util.List;

/**
 * 对于基类base拓展Socket功能,暂时分成WebSocket和Socket.IO
 * {@link ScoketIOFunClient}
 * {@link WebSocketFunClient}
 */
public interface ISocketClient {

    /**
     * 连接
     */
    void connect();

    /**
     * 初始化
     */
    void init();

    /**
     * 发送消息
     *
     * @param mgs
     */
    void send(JSONObject mgs);

    /**
     * 发送消息
     *
     * @param mgs
     */
    void send(String mgs);

    /**
     * 关闭
     */
    void close();

    /**
     * 克隆对象,性能测试中需要
     */
    ISocketClient clone();

    /**
     * 是否已连接
     *
     * @return
     */
    boolean isConnect();

    /**
     * 获取记录的消息,用于验证响应,请注意需要返回副本
     *
     * @return
     */
    List<String> getMsgs();

    /**
     * 用于保存收到的信息,不同于Client的saveMsg,此方法需要将对象存储的消息全都存到long_path目录下,是否需要清空Client对象中的msgs信息,需要视情况而定.
     */
    void savaMsg();


}
