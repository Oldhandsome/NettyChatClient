package com.dtusystem.nettychatclient.network.message

abstract class Message {
    companion object {
        const val HEART = 1
        const val FORMULA = 2
        const val TECHNOLOGY = 3
        const val LOGIN_REQUEST = 5
        const val RESPONSE = 6

        private val messageClassMapper = mapOf(
            HEART to HeartBeat::class.java,
            FORMULA to Formula::class.java,
            TECHNOLOGY to Technology::class.java,
            LOGIN_REQUEST to LoginRequest::class.java,
            RESPONSE to Response::class.java
        )

        fun getMessageClass(code: Int): Class<out Message>? {
            return messageClassMapper.get(code);
        }

    }

    abstract fun getMessageCode(): Int
}