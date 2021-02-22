package com.funtester.config;


/**
 * socket测试相关的配置
 */
public class SocketConstant {

    /* WebSocket独享配置     */

    /**
     * 最大等待次数,超过次数*时间就是连接失败
     */
    public static int MAX_WATI_TIMES = 3;

    /* 共享配置    */

    /**
     * 默认连接间隔
     */
    public static int WAIT_INTERVAL = 3;

    /**
     * 默认最大的保存响应消息的数量
     */
    public static int MAX_MSG_SIZE = 200;

    /*Socket.IO独享配置*/

    /**
     * Socket.IO独享配置
     */
    public static int MAX_RETRY = 3;

    /**
     * 重试延迟
     */
    public static int RETRY_DELAY = 1000;

    /**
     * 请求超时
     */
    public static int TIMEOUT = 10000;

    public static String[] transports = {"websocket"};


}
