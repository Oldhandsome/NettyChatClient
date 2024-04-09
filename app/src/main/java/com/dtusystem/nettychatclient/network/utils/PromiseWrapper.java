package com.dtusystem.nettychatclient.network.utils;

import com.dtusystem.nettychatclient.network.message.Message;

import java.lang.reflect.Type;

import io.netty.util.concurrent.Promise;

public class PromiseWrapper {

    /**
     * 返回值类型
     * */
    private Type returnType;

    /**
     * Promise
     * */
    private Promise<Message> promise;

    public PromiseWrapper(Type returnType, Promise<Message> promise) {
        this.returnType = returnType;
        this.promise = promise;
    }

    public Type getReturnType() {
        return returnType;
    }

    public Promise<Message> getPromise() {
        return promise;
    }
}
