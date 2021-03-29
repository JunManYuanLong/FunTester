package com.funtester.socket;

/**
 * 基于Socket.IO的Client封装对象
 */
public class ScoketIOFunClient {
//public class ScoketIOFunClient extends SourceCode implements Serializable {

//    private static final long serialVersionUID = -7229704711068396512L;
//
//    private static Logger logger = LogManager.getLogger(ScoketIOFunClient.class);
//
//    public static ThreadLocal<IO.Options> options = new ThreadLocal() {
//
//        /**
//         * 通用配置,初始化连接选项的方法,默认采取重置
//         *
//         * @return
//         */
//        @Override
//        public IO.Options initialValue() {
//            IO.Options options = new IO.Options();
//            options.transports = SocketConstant.transports;
//            //失败重试次数
//            options.reconnectionAttempts = SocketConstant.MAX_RETRY;
//            //失败重连的时间间隔
//            options.reconnectionDelay = SocketConstant.RETRY_DELAY;
//            //连接超时时间(ms)
//            options.timeout = SocketConstant.TIMEOUT;
//            return options;
//        }
//
//    };
//
//    /**
//     * 所有的客户端
//     */
//    public static Vector<ScoketIOFunClient> clients = new Vector<>();
//
//    /**
//     * 记录的消息
//     */
//    public LinkedList<String> msgs = new LinkedList<>();
//
//    /**
//     * 客户端名称
//     */
//    private String cname;
//
//    /**
//     * 连接的URL
//     */
//    private String url;
//
//    /**
//     * Socket对象
//     */
//    public Socket socket;
//
//    /**
//     * 监听事件记录,此处使用量很小,故而不考虑线程安全
//     */
//    public Set<String> events = new HashSet<>();
//
//
//    private ScoketIOFunClient(String url, Socket socket) {
//        this.url = url;
//        this.socket = socket;
//        clients.add(this);
//    }
//
//    /**
//     * 获取socketClient实例
//     *
//     * @param url
//     * @param cname
//     * @return
//     */
//    public static ScoketIOFunClient getInstance(String url, String cname) {
//        logger.info("Socket 连接: {},客户端名称: {}", url, cname);
//        ScoketIOFunClient client = null;
//        try {
//            client = new ScoketIOFunClient(url, IO.socket(url, options.get()));
//            client.setCname(cname);
//        } catch (URISyntaxException e) {
//            FailException.fail(e);
//        }
//        return client;
//    }
//
//    /**
//     * 注册通用的事件监听,需要脚本自己注册改监听
//     * {@link io.socket.client.Socket}
//     */
//    public void init() {
//        this.socket.on(Socket.EVENT_CONNECTING, objects -> {
//            logger.info("{} 正在连接...信息:{}", cname, initMsg(objects));
//        });
//        events.add(Socket.EVENT_CONNECTING);
//        this.socket.on(Socket.EVENT_ERROR, objects -> {
//            logger.info("{} 收到错误信息:{}", cname, initMsg(objects));
//        });
//        events.add(Socket.EVENT_ERROR);
//        this.socket.on(Socket.EVENT_CONNECT_TIMEOUT, objects -> {
//            logger.info("{} 连接超时!,url:{},信息:{}", cname, url, initMsg(objects));
//        });
//        events.add(Socket.EVENT_CONNECT_TIMEOUT);
//        this.socket.on(Socket.EVENT_CONNECT_ERROR, objects -> {
//            logger.info("{} 连接错误,信息:{}", cname, initMsg(objects));
//        });
//        events.add(Socket.EVENT_CONNECT_ERROR);
//        this.socket.on(Socket.EVENT_PING, objects -> {
//            logger.info("{} ping消息:{}", cname, initMsg(objects));
//        });
//        events.add(Socket.EVENT_PING);
//        this.socket.on(Socket.EVENT_PONG, objects -> {
//            logger.info("{} ping消息:{}", cname, initMsg(objects));
//        });
//        events.add(Socket.EVENT_PONG);
//        /*此处统一的message做记录*/
//        this.socket.on(Socket.EVENT_MESSAGE, objects -> {
//            String msg = initMsg(objects);
//            saveMsg(msg);
//            logger.info("{} 收到 {} 事件,信息:{}", cname, Socket.EVENT_MESSAGE, msg);
//        });
//    }
//
//    /**
//     * 开始建立socket连接
//     */
//    public void connect() {
//        logger.info("{} 开始连接...", cname);
//        this.socket.connect();
//        int a = 0;
//        while (true) {
//            this.socket.connect();
//            if (this.socket.connected()) break;
//            if ((a++ > SocketConstant.MAX_RETRY)) FailException.fail(cname + "连接重试失败!");
//            SourceCode.sleep(SocketConstant.WAIT_INTERVAL);
//        }
//        logger.info("{} 连接成功!", cname);
//    }
//
//    /**
//     * 添加监听事件
//     *
//     * @param event
//     * @param fn
//     */
//    public void addEventListener(String event, Emitter.Listener fn) {
//        events.add(event);
//        this.socket.on(event, fn);
//    }
//
//    /**
//     * 发送消息,暂不重载
//     *
//     * @param event
//     * @param objects
//     */
//    public void send(String event, Object... objects) {
//        events.add(event);
//        this.socket.emit(event, objects);
//    }
//
//    /**
//     * 关闭SocketClient
//     */
//    public void close() {
//        logger.info("{} socket链接关闭!", cname);
//        this.socket.close();
//    }
//
//    /**
//     * 初始化收到的信息
//     *
//     * @param objects
//     * @return
//     */
//    public static String initMsg(Object... objects) {
//        if (ArrayUtils.isEmpty(objects)) return EMPTY;
//        return Arrays.toString(objects);
//    }
//
//    /**
//     * 该方法用于性能测试中,clone多线程对象
//     *
//     * @return
//     */
//    @Override
//    public ScoketIOFunClient clone() {
//        return getInstance(this.url, this.cname + StringUtil.getString(4));
//    }
//
//    /**
//     * 设置cname,多用于性能测试clone()之后
//     *
//     * @param cname
//     */
//    public void setCname(String cname) {
//        this.cname = cname;
//    }
//
//    public String getCname() {
//        return cname;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    /**
//     * 保存收到的信息,只保留最近的{@link SocketConstant}条
//     *
//     * @param msg
//     */
//    public void saveMsg(String msg) {
//        synchronized (msgs) {
//            if (msgs.size() > SocketConstant.MAX_MSG_SIZE) msgs.remove();
//            msgs.add(msg);
//        }
//    }
//
//    /**
//     * 关闭所有socketclient
//     */
//    public static void closeAll() {
//        clients.forEach(x ->
//                {
//                    if (x != null && x.socket.connected()) x.close();
//                }
//        );
//        clients.clear();
//        logger.info("关闭所有Socket客户端!");
//    }


}
