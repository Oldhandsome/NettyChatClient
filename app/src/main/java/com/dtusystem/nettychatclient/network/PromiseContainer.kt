package com.dtusystem.nettychatclient.network

import com.dtusystem.nettychatclient.network.utils.PromiseWrapper
import java.util.concurrent.ConcurrentHashMap

object PromiseContainer {
    private val promiseWrapperMappers = ConcurrentHashMap<Int, PromiseWrapper<*>>();

    fun addPromise(id: Int, promiseWrapper: PromiseWrapper<*>) {
        promiseWrapperMappers.put(id, promiseWrapper)
    }

    fun getPromise(id: Int): PromiseWrapper<*>? {
        return promiseWrapperMappers.get(id)
    }

    fun removePromise(id: Int): PromiseWrapper<*>? {
        return promiseWrapperMappers.remove(id)
    }

    fun clearPromise() {
        promiseWrapperMappers.clear();
    }
}