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

@Log4j2
class WebSocketConnector {
//class WebSocketConnector extends SimpleChannelInboundHandler<Object> {

    static Bootstrap bootstrap = new Bootstrap()

    // 事件循环线程池
    static EventLoopGroup group = new NioEventLoopGroup(128, ThreadPoolUtil.getFactory("N"))

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
     * 路径
     */
    String path = "/test"

    /**
     * 网络通道
     */
    Channel channel

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

    }


    void doConnect() {
        try {
            String URL = prefix + this.host + ":" + this.port + path;
            URI uri = new URI(URL);
            final WebSocketIoHandler handler = new WebSocketIoHandler(WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
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
                            pipeline.addLast(new HttpObjectAggregator(1024 * 64))
                            pipeline.addLast(handler)
                        }
                    })
            try {
                synchronized (bootstrap) {
                    final ChannelFuture future = bootstrap.connect(this.host, this.port).sync()
                    this.channel = future.channel()
                    clients.add(channel)
                }
            } catch (Exception e) {
                log.error("连接服务失败", e)
            }
            handler.handshakeFuture().get()
            log.info(324)
        } catch (Exception e) {
            log.error("连接服务失败", e)
        }
    }

    void disConnect() {
        this.channel.close()
    }

//    @Override
//    void handlerAdded(ChannelHandlerContext ctx) {
//        handshakeFuture = ctx.newPromise()
//    }
//
//    @Override
//    void channelActive(ChannelHandlerContext ctx) {
//        handShaker.handshake(ctx.channel());
//    }
//
//    @Override
//    void channelInactive(ChannelHandlerContext ctx) {
//        ctx.close()
//        try {
//            super.channelInactive(ctx)
//        } catch (Exception e) {
//            log.error("channelInactive 异常.", e)
//        }
//        log.warn("WebSocket链路与服务器连接已断开.")
//    }
//
//    @Override
//    void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        println 3
//        Channel ch = ctx.channel()
//        if (!handShaker.isHandshakeComplete()) {
//            try {
//                handShaker.finishHandshake(ch, (FullHttpResponse) msg)
//                handshakeFuture.setSuccess()
//                log.info("WebSocket握手成功，可以传输数据了.")
//                // 数据一定要封装成WebSocketFrame才能发达
//                String data = "Hello"
//                WebSocketFrame frame = new TextWebSocketFrame(data)
//                ch.writeAndFlush(frame)
//            } catch (WebSocketHandshakeException e) {
//                log.warn("WebSocket Client failed to connect", e)
//                handshakeFuture.setFailure(e)
//            }
//            return
//        }
//
//        if (msg instanceof FullHttpResponse) {
//            FullHttpResponse response = (FullHttpResponse) msg
//            throw new IllegalStateException(
//                    "Unexpected FullHttpResponse (getStatus=" + response.status() +
//                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')')
//        }
//
//        WebSocketFrame frame = (WebSocketFrame) msg
//        if (frame instanceof TextWebSocketFrame) {
//            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame
//            String s = textFrame.text()
//            log.info("WebSocket Client received message: " + s)
//        } else if (frame instanceof PongWebSocketFrame) {
//            log.info("WebSocket Client received pong")
//        } else if (frame instanceof CloseWebSocketFrame) {
//            log.info("WebSocket Client received closing")
//            ch.close()
//        }
//    }
//
//    @Override
//    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        log.error("WebSocket链路由于发生异常,与服务器连接已断开.", cause)
//        if (!handshakeFuture.isDone()) {
//            handshakeFuture.setFailure(cause)
//        }
//        ctx.close()
//        super.exceptionCaught(ctx, cause)
//    }
//
//    @Override
//    void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent) {
//            IdleStateEvent event = (IdleStateEvent) evt
//            // 如果写通道处于空闲状态,就发送心跳命令
//            if (IdleState.WRITER_IDLE == event.state() || IdleState.READER_IDLE == event.state()) {
//                // 发送心跳数据
//                def channel = ctx.channel()
//                channel.writeAndFlush(new TextWebSocketFrame("dsf"))
//            }
//        } else {
//            super.userEventTriggered(ctx, evt)
//        }
//    }
}