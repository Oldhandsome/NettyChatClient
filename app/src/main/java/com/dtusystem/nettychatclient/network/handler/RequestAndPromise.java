package com.dtusystem.nettychatclient.network.handler;

import com.dtusystem.nettychatclient.network.message.Message;

import io.netty.util.concurrent.Promise;

public class RequestAndPromise {
    private Message request;
    private Promise<Message> promise;

    public RequestAndPromise(Message request, Promise<Message> promise) {
        this.promise = promise;
        this.request = request;
    }

    public Message getRequest() {
        return request;
    }

    public void setRequest(Message request) {
        this.request = request;
    }

    public Promise<Message> getPromise() {
        return promise;
    }

    public void setPromise(Promise<Message> promise) {
        this.promise = promise;
    }
}
