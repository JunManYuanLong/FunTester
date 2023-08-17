package com.funtester.socket.netty

import com.funtester.frame.execute.ThreadPoolUtil
import groovy.util.logging.Log4j2
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.DefaultHttpHeaders
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory
import io.netty.handler.codec.http.websocketx.WebSocketVersion
import io.netty.handler.stream.ChunkedWriteHandler
import io.netty.util.concurrent.GlobalEventExecutor

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

@Log4j2
class WebSocketConnector {

    static Bootstrap bootstrap = new Bootstrap()

    static ReentrantLock createLock = new ReentrantLock()

    // 事件循环线程池
    static EventLoopGroup group = new NioEventLoopGroup(ThreadPoolUtil.getFactory("N"))

    static {
        bootstrap.group(group).channel(NioSocketChannel.class)
    }

    /**
     * 用于记录和管理所有客户端的channel
     */
    private ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE)

    WebSocketClientHandshaker handShaker

    ChannelPromise handshakeFuture

    /**
     * 前缀类型,ws和wss
     */
    static String prefix = "ws://"

    // 服务器ip
    String host

    URI uri

    // 服务器通信端口
    int port = 12345

    /**
     * 网络通道
     */
    Channel channel

    WebSocketIoHandler handler

    /**
     * WebSocket协议类型的模拟客户端连接器构造方法
     *
     * @param serverIp
     * @param serverSocketPort
     * @param group
     */
    WebSocketConnector(String host) {
        this.host = host
        String URL = prefix + this.host + ":" + this.port + "/test";
        uri = new URI(URL);
        handler = new WebSocketIoHandler(WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));
        bootstrap.option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline()
                        // 添加一个http的编解码器
                        pipeline.addLast(new HttpClientCodec())
                        // 添加一个用于支持大数据流的支持
                        pipeline.addLast(new ChunkedWriteHandler())
                        // 添加一个聚合器，这个聚合器主要是将HttpMessage聚合成FullHttpRequest/Response
                        pipeline.addLast(new HttpObjectAggregator(1024 * 1024))
                        pipeline.addLast(handler)
                    }
                })
    }


    void connect() {
        try {
            def lock = createLock.tryLock(10, TimeUnit.SECONDS)
            if (lock) {
                try {
                    final ChannelFuture future = bootstrap.connect(this.host, this.port).sync()
                    this.channel = future.channel()
                    clients.add(channel)
                } catch (e) {
                    log.error("创建channel失败", e)
                } finally {
                    createLock.unlock()
                }
            } else {
                log.error("创建channel失败,获取锁超时")
            }
        } catch (Exception e) {
            log.error("连接服务失败", e)
        } finally {
            this.handshakeFuture = handler.handshakeFuture()
        }
    }

    void close() {
        this.channel.close()
    }

}