package com.dtusystem.nettychatclient.network.handler;


import android.annotation.SuppressLint;
import android.util.Log;

import com.dtusystem.nettychatclient.network.handler.remote.RemoteRequestHandler;
import com.dtusystem.nettychatclient.network.message.Message;
import com.dtusystem.nettychatclient.network.utils.NetworkMsg;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


@ChannelHandler.Sharable
public class RemoteRequestHandlers extends SimpleChannelInboundHandler<NetworkMsg> {

    private final ConcurrentMap<Integer, Object> remoteRequestHandlers = new ConcurrentHashMap<>();

    public void addRemoteRequestHandler(RemoteRequestHandler requestHandler) {
        List<Integer> codes = requestHandler.getMessageCodes();
        for (Integer code : codes) {
            Object object = remoteRequestHandlers.get(code);
            if (object == null) {
                remoteRequestHandlers.put(code, requestHandler);
                continue;
            }
            if (object instanceof RemoteRequestHandler && object != requestHandler) {
                remoteRequestHandlers.put(code, Arrays.asList(object, requestHandler));
                continue;
            }
            if (object instanceof List) {
                List<RemoteRequestHandler> handlerList = (List<RemoteRequestHandler>) object;
                handlerList.add(requestHandler);
            }
        }
    }

    public void removeRemoteRequestHandle(RemoteRequestHandler requestHandler) {
        List<Integer> codes = requestHandler.getMessageCodes();
        for (Integer code : codes) {
            Object object = remoteRequestHandlers.get(code);
            if (object == null) continue;
            if (object instanceof RemoteRequestHandler && requestHandler == object) {
                remoteRequestHandlers.remove(code);
                continue;
            }
            if (object instanceof List) {
                List<RemoteRequestHandler> requestHandlers = (List<RemoteRequestHandler>) object;
                requestHandlers.remove(requestHandler);
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private Object invoke(int code, RemoteRequestHandler remoteRequestHandler, Message request) {
        Class<? extends Message> messageClass = Message.Companion.getMessageClass(code);
        if (messageClass != null) {
            String simpleName = messageClass.getSimpleName();
            String methodName = String.format("handle%s", simpleName);
            try {
                Method method = remoteRequestHandler.getClass().getDeclaredMethod(methodName, messageClass);
                return method.invoke(remoteRequestHandler, request);
            } catch (NoSuchMethodException e) {
                String error = String.format("can not find the method named '%s' in class %s!!!", methodName, remoteRequestHandler.getClass());
                Log.e(RemoteRequestHandlers.class.getSimpleName(), error);
                throw new RuntimeException(error);
            } catch (InvocationTargetException | IllegalAccessException e) {
                Log.e(RemoteRequestHandlers.class.getSimpleName(), e.toString());
                throw new RuntimeException(e);
            }
        }
        String error = String.format("can not find the class which the number %d points to!!!", code);
        throw new RuntimeException(error);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NetworkMsg networkMsg) {
        int id = networkMsg.getId();
        int code = networkMsg.getMessage().getMessageCode();
        Object object = remoteRequestHandlers.get(code);
        if (object != null) {
            // 如果是一个普通的对象
            if (object instanceof RemoteRequestHandler) {
                RemoteRequestHandler handler = (RemoteRequestHandler) object;
                Object result = invoke(code, handler, networkMsg.getMessage());
                if (result != null) ctx.writeAndFlush(new NetworkMsg(id, (Message) result));
                return;
            }
            // 否则就是一个列表
            for (RemoteRequestHandler handler : (List<RemoteRequestHandler>) object) {
                Object result = invoke(code, handler, networkMsg.getMessage());
                if (result != null) ctx.writeAndFlush(new NetworkMsg(id, (Message) result));
            }
        }
    }
}
