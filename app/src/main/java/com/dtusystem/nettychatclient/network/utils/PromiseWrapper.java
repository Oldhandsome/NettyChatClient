package com.dtusystem.nettychatclient.network.utils;

import java.lang.reflect.Type;

import io.netty.util.concurrent.Promise;

public class PromiseWrapper<T> {

    /**
     * 返回值类型
     */
    private Type returnType;

    /**
     * Promise
     */
    private Promise<T> promise;

    public PromiseWrapper(Type returnType, Promise<T> promise) {
        this.returnType = returnType;
        this.promise = promise;
    }

    public Type getReturnType() {
        return returnType;
    }

    public Promise<T> getPromise() {
        return promise;
    }
}
