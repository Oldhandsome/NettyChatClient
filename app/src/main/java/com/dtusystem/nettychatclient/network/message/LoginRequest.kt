package com.dtusystem.nettychatclient.network.message

data class LoginRequest(
    val username: String,
    val password: String
) : Message() {
    override fun getMessageCode(): Int {
        return Message.LOGIN_REQUEST
    }
}