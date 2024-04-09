package com.dtusystem.nettychatclient.network.handler;

import android.util.Log;

import com.dtusystem.nettychatclient.network.message.HeartBeat;
import com.dtusystem.nettychatclient.network.message.Message;

import java.util.concurrent.ConcurrentHashMap;
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

    private final AtomicInteger idGenerator = new AtomicInteger(0);

    private final NetworkMsg heartBeat = new NetworkMsg(0, new HeartBeat());
    private final ConcurrentHashMap<Integer, Promise<Message>> promiseMapper = new ConcurrentHashMap<>();

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
        if (msg instanceof RequestAndPromise) {
            RequestAndPromise requestAndPromise = (RequestAndPromise) msg;
            int id = idGenerator.incrementAndGet();
            System.out.println("1 promise id "  + id);
            if (requestAndPromise.getPromise() != null) {
                promiseMapper.put(id, requestAndPromise.getPromise());
            }
            super.write(ctx, new NetworkMsg(id, requestAndPromise.getRequest()), promise);
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
            Promise<Message> promise = promiseMapper.remove(id);
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
