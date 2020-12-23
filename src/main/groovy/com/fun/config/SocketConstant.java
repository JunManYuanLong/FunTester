package com.fun.config;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * socket测试相关的配置
 */
@SuppressFBWarnings("MS_SHOULD_BE_FINAL")
public class SocketConstant {

    /* WebSocket独享配置     */
    public static int MAX_WATI_TIMES = 3;

    /* 共享配置    */
    public static int WAIT_INTERVAL = 3;

    public static int MAX_MSG_SIZE = 20;

    /*Socket.IO独享配置*/
    public static int MAX_RETRY = 3;

    public static int RETRY_DELAY = 1000;

    public static int TIMEOUT = 1000;

    public static String[] transports = {"websocket"};


}
