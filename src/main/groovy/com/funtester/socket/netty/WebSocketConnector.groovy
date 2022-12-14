package com.funtester.socket.netty

import com.funtester.frame.SourceCode
import com.funtester.frame.execute.ThreadPoolUtil
import groovy.util.logging.Log4j2
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.DefaultHttpHeaders
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.handler.codec.http.websocketx.WebSocketVersion
import io.netty.handler.stream.ChunkedWriteHandler

@Log4j2
class WebSocketConnector extends SourceCode {

    static Bootstrap bootstrap = new Bootstrap()

    // 事件循环线程池
    static EventLoopGroup group = new NioEventLoopGroup(128, ThreadPoolUtil.getFactory("N"))

    /**
     * 前缀类型,ws和wss
     */
    static String prefix = "ws://"

    // 服务器ip
    String host

    // 服务器通信端口
    int port

    /**
     * 路径
     */
    String path

    /**
     * 网络通道
     */
    Channel channel;

    /**
     * WebSocket协议类型的模拟客户端连接器构造方法
     *
     * @param serverIp
     * @param serverSocketPort
     * @param group
     */
    WebSocketConnector(String host, int port) {
        this.host = host;
        this.port = port;
        this.group = group;
    }


    void doConnect() {
        try {
            String URL = prefix + this.host + ":" + this.port + path == null ? EMPTY : path;
            URI uri = new URI(URL);
            final WebSocketIoHandler handler = new WebSocketIoHandler(WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加一个http的编解码器
                            pipeline.addLast(new HttpClientCodec());
                            // 添加一个用于支持大数据流的支持
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 添加一个聚合器，这个聚合器主要是将HttpMessage聚合成FullHttpRequest/Response
                            pipeline.addLast(new HttpObjectAggregator(1024 * 64));
                            if (path != null) pipeline.addLast(new WebSocketServerProtocolHandler(path));
                            pipeline.addLast(handler);
                        }
                    });
            try {
                synchronized (bootstrap) {
                    final ChannelFuture future = bootstrap.connect(this.host, this.port).sync();
                    this.channel = future.channel();
                }
            } catch (Exception e) {
                log.error("连接服务失败", e);
            }
        } catch (Exception e) {
            log.error("连接服务失败", e);
        }

    }

    void disConnect() {
        this.channel.close();
    }

}