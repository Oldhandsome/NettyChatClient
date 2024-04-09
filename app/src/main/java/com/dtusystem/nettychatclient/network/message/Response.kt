package com.dtusystem.nettychatclient.network.message

class Response(
    val success: Boolean,
    val reason: String
) : Message() {
    override fun getMessageCode(): Int {
        return RESPONSE
    }



}