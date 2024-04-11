package com.dtusystem.nettychatclient.network.message

import com.dtusystem.nettychatclient.network.exception.MessageException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 本地请求的所有响应（通用形式）
 * */
data class Response<T>(
    val success: Boolean,
    val reason: String,
    val data: T?
) : Message() {
    override fun getMessageCode(): Int {
        return RESPONSE
    }

    companion object {
        fun isResponseType(type: Type): Boolean {
            return type is ParameterizedType && type.rawType === Response::class.java
        }
    }

    fun getOrNull(): T? {
        return if (this.success) {
            this.data
        } else null
    }

    fun getOrThrow(): T? {
        if (this.success)
            return this.data
        throw MessageException(this.reason)
    }
}

