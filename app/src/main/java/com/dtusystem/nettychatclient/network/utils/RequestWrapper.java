package com.dtusystem.nettychatclient.network.utils;

import com.dtusystem.nettychatclient.network.message.Message;

public class RequestWrapper {

    /**
     * 当前的请求
     * */
    private Message request;

    /**
     * 封装的响应
     * */
    private PromiseWrapper promiseWrapper;


    public RequestWrapper(Message request, PromiseWrapper promiseWrapper) {
        this.request = request;
        this.promiseWrapper = promiseWrapper;
    }

    public Message getRequest() {
        return request;
    }

    public PromiseWrapper getPromiseWrapper() {
        return promiseWrapper;
    }
}
