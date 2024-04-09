package com.dtusystem.nettychatclient.network.message

data class Formula(
    val valveA: Int,
    val valveB: Int,
    val valveC: Int,
    val proportionA: Int,
    val proportionB: Int,
    val proportionC: Int
): Message() {
    override fun getMessageCode(): Int {
        return Message.FORMULA
    }
}