package com.fun.base.interfaces;

import com.alibaba.fastjson.JSONObject;

/**
 * 对于基类base拓展Socket功能,暂时分成WebSocket和Socket.IO
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
     * 克隆
     */
    void clone();


}
