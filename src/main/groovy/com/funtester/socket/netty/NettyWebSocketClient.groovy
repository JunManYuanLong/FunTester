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
import io.netty.handler.codec.http.websocketx.*
import io.netty.handler.stream.ChunkedWriteHandler
import io.netty.util.concurrent.GlobalEventExecutor

@Log4j2
class NettyWebSocketClient {

    static Bootstrap bootstrap = new Bootstrap()

    /**
     * 处理事件的线程池
     */
    static EventLoopGroup group = new NioEventLoopGroup(ThreadPoolUtil.getFactory("N"))

    static {
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
    }

    /**
     * 用于记录和管理所有客户端的channel
     */
    static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE)


    ChannelPromise handshakeFuture

    URI uri

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
    NettyWebSocketClient(String url, Closure closure = null) {
        this.uri = new URI(URL)
        handler = new WebSocketIoHandler(WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()))
        if (closure != null) handler.closure = closure
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline()
                pipeline.addLast(new HttpClientCodec())
                pipeline.addLast(new ChunkedWriteHandler())
                pipeline.addLast(new HttpObjectAggregator(1024 * 1024))
                pipeline.addLast(handler)
            }
        })
    }


    /**
     * 连接
     */
    void connect() {
        try {
            try {
                ChannelFuture future = bootstrap.connect(uri.getHost(), uri.getPort()).sync()
                this.channel = future.channel()
                clients.add(channel)
            } catch (e) {
                log.error("创建channel失败", e)
            }
        } catch (Exception e) {
            log.error("连接服务失败", e)
        } finally {
            this.handshakeFuture = handler.handshakeFuture()
        }
    }

    /**
     * 发送文本消息
     */
    ChannelFuture sendText(String msg) {
        channel.writeAndFlush(new TextWebSocketFrame(msg))
    }

    /**
     * 发送ping消息
     */
    ChannelFuture ping() {
        channel.writeAndFlush(new PingWebSocketFrame())
    }

    /**
     * 关闭
     */
    void close() {
        group.shutdownGracefully()
    }

}