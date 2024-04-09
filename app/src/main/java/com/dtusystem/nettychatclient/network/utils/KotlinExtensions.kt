package com.dtusystem.nettychatclient.network.utils

import com.dtusystem.nettychatclient.network.exception.MessageException
import com.dtusystem.nettychatclient.network.message.Message
import com.dtusystem.nettychatclient.network.message.Response
import io.netty.util.concurrent.Promise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.intrinsics.intercepted
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


suspend fun Promise<Message>.awaitForSuspend(): Message {
    return suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            this@awaitForSuspend.cancel(true)
        }
        this.addListener { future ->
            val message = future.get()
            if(message == null) {
                continuation.resumeWithException(NullPointerException("Response from server is null!!!"))
                return@addListener
            }
            val response = message as Response<*>
            if (future.isSuccess && response.success) {
                continuation.resume(response)
                return@addListener
            }else{
                continuation.resumeWithException(MessageException(response.reason))
            }
        }
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


