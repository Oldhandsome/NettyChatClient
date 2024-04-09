package com.dtusystem.nettychatclient.network.utils

import io.netty.util.concurrent.Future
import io.netty.util.concurrent.GenericFutureListener
import io.netty.util.concurrent.Promise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.intrinsics.intercepted
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


suspend fun <T : Any> Promise<T>.awaitForSuspend(): T {
    return suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            this@awaitForSuspend.cancel(true)
        }
        this.addListener(object : GenericFutureListener<Future<in T>> {
            override fun operationComplete(future: Future<in T>) {
                if (future.isSuccess) {
                    val result = future.get()
                    if (result == null)
                        continuation.resumeWithException(NullPointerException("Response from server is null!!!"))
                    else
                        continuation.resume(result as T)
                } else
                    continuation.resumeWithException(future.cause())
            }
        })
    }
}

internal suspend fun Exception.suspendAndThrow(): Nothing {
    suspendCoroutineUninterceptedOrReturn<Nothing> { continuation ->
        Dispatchers.Default.dispatch(continuation.context, Runnable {
            continuation.intercepted().resumeWithException(this@suspendAndThrow)
        })
        COROUTINE_SUSPENDED
    }
}


