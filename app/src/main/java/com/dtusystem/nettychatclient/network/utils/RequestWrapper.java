package com.dtusystem.nettychatclient.network.utils;

import com.dtusystem.nettychatclient.network.message.Message;

public class RequestWrapper<T> {

    /**
     * 当前的请求
     * */
    private Message request;

    /**
     * 封装的响应
     * */
    private PromiseWrapper<T> promiseWrapper;


    public RequestWrapper(Message request, PromiseWrapper<T> promiseWrapper) {
        this.request = request;
        this.promiseWrapper = promiseWrapper;
    }

    public Message getRequest() {
        return request;
    }

    public PromiseWrapper<T> getPromiseWrapper() {
        return promiseWrapper;
    }
}
