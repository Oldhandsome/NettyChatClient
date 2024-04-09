package com.dtusystem.nettychatclient.network.handler;

import android.util.Log;


import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import timber.log.Timber;

/**
 * 处理事件的处理器：读超时事件、异常事件、断开连接事件、连接事件
 * */
@ChannelHandler.Sharable
public class EventHandler extends ChannelDuplexHandler {

    private ReconnectionCallback callback;

    public ReconnectionCallback getCallback() {
        return callback;
    }

    public void setCallback(ReconnectionCallback callback) {
        this.callback = callback;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                Log.d(EventHandler.class.toString(), "reader is idle");
                ctx.close().sync();
                if (callback != null)
                    callback.reconnect();
            } else {
                ctx.fireUserEventTriggered(evt);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Log.d(EventHandler.class.toString(), "channel is active!!!");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.d(EventHandler.class.toString(), "channel is inactive!!!");
        ctx.close().sync();
        if (callback != null)
            callback.reconnect();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Timber.e(cause);
    }

    public interface ReconnectionCallback {
        void reconnect();
    }
}
