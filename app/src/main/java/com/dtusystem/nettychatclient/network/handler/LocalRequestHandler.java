package com.dtusystem.nettychatclient.network.handler;

import android.util.Log;

import com.dtusystem.nettychatclient.network.PromiseContainer;
import com.dtusystem.nettychatclient.network.message.HeartBeat;
import com.dtusystem.nettychatclient.network.message.Message;
import com.dtusystem.nettychatclient.network.utils.NetworkMsg;
import com.dtusystem.nettychatclient.network.utils.RequestWrapper;

import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Promise;

/**
 * 本地请求的处理器（需要处理本地的请求，远程的响应）
 */
@ChannelHandler.Sharable
public class LocalRequestHandler extends ChannelDuplexHandler {

    private final PromiseContainer promiseContainer = PromiseContainer.INSTANCE;

    private final AtomicInteger idGenerator = new AtomicInteger(0);

    private final NetworkMsg heartBeat = new NetworkMsg(0, new HeartBeat());


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                Log.d(LocalRequestHandler.class.toString(), "writer is idle");
                heartBeat.setId(idGenerator.incrementAndGet());
                ctx.writeAndFlush(heartBeat);
            } else {
                ctx.fireUserEventTriggered(evt);
            }
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof RequestWrapper) {
            RequestWrapper requestWrapper = (RequestWrapper) msg;
            int id = idGenerator.incrementAndGet();
            if (requestWrapper.getPromiseWrapper().getPromise() != null) {
                promiseContainer.addPromise(id, requestWrapper.getPromiseWrapper());
            }
            super.write(ctx, new NetworkMsg(id, requestWrapper.getRequest()), promise);
        } else {
            Log.e(LocalRequestHandler.class.toString(), "the msg is null or unknown class");
            throw new IllegalArgumentException("the msg is null or unknown class");
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof NetworkMsg) {
            NetworkMsg networkMsg = (NetworkMsg) msg;

            Log.d(LocalRequestHandler.class.toString(), networkMsg.getMessage().toString());

            if (networkMsg.getMessage() instanceof HeartBeat) {
                ReferenceCountUtil.release(msg);
                return;
            }

            int id = networkMsg.getId();
            Promise<Message> promise = promiseContainer.removePromise(id).getPromise();
            if (promise != null) {
                promise.setSuccess(networkMsg.getMessage());
                ReferenceCountUtil.release(msg);
            } else {
                ctx.fireChannelRead(msg);
            }
        } else {
            throw new IllegalArgumentException("the msg is null or unknown class");
        }
    }
}
