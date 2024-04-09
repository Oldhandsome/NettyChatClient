package com.dtusystem.nettychatclient.network.message

class Response<T>(
    val success: Boolean,
    val reason: String,
    val data: T?
) : Message() {
    override fun getMessageCode(): Int {
        return RESPONSE
    }
}

