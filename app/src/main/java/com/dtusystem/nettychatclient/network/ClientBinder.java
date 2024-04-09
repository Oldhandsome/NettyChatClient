package com.dtusystem.nettychatclient.network;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.dtusystem.nettychatclient.network.handler.EventHandler;
import com.dtusystem.nettychatclient.network.handler.LocalRequestHandler;
import com.dtusystem.nettychatclient.network.handler.RemoteRequestHandlers;
import com.dtusystem.nettychatclient.network.utils.KotlinExtensionsKt;
import com.dtusystem.nettychatclient.network.utils.RequestWrapper;
import com.dtusystem.nettychatclient.network.utils.PromiseWrapper;
import com.dtusystem.nettychatclient.network.handler.remote.RemoteRequestHandler;
import com.dtusystem.nettychatclient.network.message.Message;
import com.dtusystem.nettychatclient.network.protocol.MessageCodecSharable;
import com.dtusystem.nettychatclient.network.protocol.ProtocolLengthFieldDecoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.net.ConnectException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import kotlin.coroutines.Continuation;
import timber.log.Timber;

public class ClientBinder {

    /**
     * 写超时的最大时间
     */
    private final int heartBeatInterval = 3;

    private final String ip;

    private final int port;

    /**
     * 记录日志
     */
    private final LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
    private final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    private final RemoteRequestHandlers remoteRequestHandlers = new RemoteRequestHandlers();    /**
     * 重连的回调函数
     */
    private final EventHandler.ReconnectionCallback callback = this::startConnect;
    private final Bootstrap bootstrap;
    private ChannelFuture channelFuture;
    private Channel channel;
    public ClientBinder(String ip, int port) {
        this.ip = ip;
        this.port = port;
        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class)
                .group(eventLoopGroup)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ProtocolLengthFieldDecoder())
                                .addLast(loggingHandler)
                                .addLast(new MessageCodecSharable())
                                .addLast(new IdleStateHandler(heartBeatInterval * 3, heartBeatInterval, 0))
                                .addLast("eventHandler", new EventHandler())
                                .addLast("localRequestHandler", new LocalRequestHandler())
                                .addLast("remoteRequestHandlers", new RemoteRequestHandlers());
                    }
                });
    }

    public boolean isActive() {
        if (channel == null) return false;
        return channel.isActive();
    }

    public void startConnect() {
        if (!isActive()) {
            channelFuture = bootstrap.connect(ip, port).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        channel = future.sync().channel();
                        ((EventHandler) channel.pipeline().get("eventHandler")).setCallback(callback);
                    } else {
                        EventLoop loop = future.channel().eventLoop();
                        loop.schedule(new Runnable() {
                            @Override
                            public void run() {
                                startConnect();
                            }
                        }, 5, TimeUnit.SECONDS);
                    }
                }
            });

        }
    }

    public void stopConnect() {
        if (isActive()) {
            try {
                ((EventHandler) channel.pipeline().get("eventHandler")).setCallback(null);
                channel.close().sync();
            } catch (InterruptedException e) {
                Timber.e(e);
            }
        } else {
            if (channelFuture != null) {
                try {
                    channelFuture.cancel(true);
                } catch (CancellationException e) {
                    Timber.e(e);
                }
            }
        }
    }

    public <T> T create(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new InvocationHandler() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        int length = parameterTypes.length;
                        Promise<Object> promise = new DefaultPromise<>(eventLoopGroup.next());
                        if (length > 0 && parameterTypes[length - 1] == Continuation.class) {
                            Continuation continuation = (Continuation) args[length - 1];
                            if (isActive()) {
                                Parameter parameterOfContinuation = method.getParameters()[length - 1];
                                ParameterizedType parameterizedType = (ParameterizedType) parameterOfContinuation.getParameterizedType();
                                WildcardType wildcardType = (WildcardType) parameterizedType.getActualTypeArguments()[0];
                                Type returnType = wildcardType.getLowerBounds()[0];
                                PromiseWrapper promiseWrapper = new PromiseWrapper(returnType, promise);
                                channel.writeAndFlush(new RequestWrapper((Message) args[0], promiseWrapper));
                                return KotlinExtensionsKt.awaitForSuspend(promise, continuation);
                            }
                            ConnectException exception = new ConnectException("can not connect to the server");
                            return KotlinExtensionsKt.suspendAndThrow(exception, continuation);
                        }
                        if(method.getReturnType() == Promise.class){
                            Type returnType = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
                            if (isActive()) {
                                PromiseWrapper promiseWrapper = new PromiseWrapper(returnType, promise);
                                channel.writeAndFlush(new RequestWrapper((Message) args[0], promiseWrapper));
                            } else
                                promise.setFailure(new ConnectException("can not connect to the server"));
                            return promise;
                        }
                        throw new RuntimeException("Please use `Promise` class or use suspend function!!!");
                    }
                }
        );
    }

    public void addRemoteRequestHandler(RemoteRequestHandler remoteRequestHandler) {
        remoteRequestHandlers.addRemoteRequestHandler(remoteRequestHandler);
    }

    public void removeRemoteRequestHandler(RemoteRequestHandler remoteRequestHandler) {
        remoteRequestHandlers.removeRemoteRequestHandle(remoteRequestHandler);
    }


}
