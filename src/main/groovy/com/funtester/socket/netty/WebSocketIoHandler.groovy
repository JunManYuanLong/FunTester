package com.funtester.socket.netty

import groovy.util.logging.Log4j2
import io.netty.channel.*
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.handler.codec.http.FullHttpResponse
import io.netty.handler.codec.http.websocketx.*
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import io.netty.util.concurrent.GlobalEventExecutor

/**
 * WebSocket协议类型的模拟客户端IO处理器类
 */
@Log4j2
class WebSocketIoHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * 用于记录和管理所有客户端的channel
     */
    private ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE)

    private final WebSocketClientHandshaker handShaker

    Closure closure

    private ChannelPromise handshakeFuture

    WebSocketIoHandler(WebSocketClientHandshaker handShaker) {
        this.handShaker = handShaker
    }

    ChannelFuture handshakeFuture() {
        return handshakeFuture
    }

    @Override
    void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise()
    }

    @Override
    void channelActive(ChannelHandlerContext ctx) {
        handShaker.handshake(ctx.channel());
    }

    @Override
    void channelInactive(ChannelHandlerContext ctx) {
        ctx.close()
        try {
            super.channelInactive(ctx)
        } catch (Exception e) {
            log.error("channelInactive 异常.", e)
        }
        log.warn("WebSocket链路与服务器连接已断开.")
    }

    @Override
    void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel()
        if (!handShaker.isHandshakeComplete()) {
            try {
                handShaker.finishHandshake(ch, (FullHttpResponse) msg)
                handshakeFuture.setSuccess()
            } catch (WebSocketHandshakeException e) {
                log.warn("WebSocket Client failed to connect", e)
                handshakeFuture.setFailure(e)
            }
            return
        }

        WebSocketFrame frame = (WebSocketFrame) msg
        if (frame instanceof TextWebSocketFrame) {
            if (closure != null) {
                TextWebSocketFrame textFrame = (TextWebSocketFrame) frame
                closure(textFrame.text())
            }
        } else if (frame instanceof CloseWebSocketFrame) {
            log.info("WebSocket Client closing")
            ch.close()
        }
    }

    @Override
    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("WebSocket链路由于发生异常,与服务器连接已断开.", cause)
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause)
        }
        ctx.close()
        super.exceptionCaught(ctx, cause)
    }

    @Override
    void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt
            // 如果写通道处于空闲状态,就发送心跳命令
            if (IdleState.WRITER_IDLE == event.state() || IdleState.READER_IDLE == event.state()) {
                // 发送心跳数据
                def channel = ctx.channel()
                channel.writeAndFlush(new TextWebSocketFrame("dsf"))
            }
        } else {
            super.userEventTriggered(ctx, evt)
        }
    }
}