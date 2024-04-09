package com.dtusystem.nettychatclient.network.handler.remote;

import java.util.List;

/**
 * 远程请求的处理器基类，需要根据报文的类型不同分发给不同的对象
 * 处理函数：handle+待处理的类名 handleFormula
 */
public abstract class RemoteRequestHandler {
    /**
     * 获得能够处理的 请求 类型
     */
    public abstract List<Integer> getMessageCodes();

}