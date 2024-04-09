package com.dtusystem.nettychatclient.network.message

data class Technology(
    val fanPressure: Int,
    val sectorPressure: Int,
    val muzzlePressure: Int,
) : Message() {
    override fun getMessageCode(): Int {
        return Message.TECHNOLOGY
    }
}