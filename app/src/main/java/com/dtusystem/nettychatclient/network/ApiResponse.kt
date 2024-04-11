package com.dtusystem.nettychatclient.network

import com.dtusystem.nettychatclient.network.exception.MessageException
import com.dtusystem.nettychatclient.network.message.Response

//fun <T> Response<T>.getOrNull(): T? = if (this.success) {
//    this.data
//} else null
//
//fun <T> Response<T>.getOrThrow(): T? = if (this.success) {
//    this.data
//} else {
//    throw MessageException(this.reason)
//}