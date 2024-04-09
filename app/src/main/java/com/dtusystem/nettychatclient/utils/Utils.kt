package com.dtusystem.nettychatclient.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
private val timeFormat1: SimpleDateFormat = SimpleDateFormat().apply {
    this.timeZone = TimeZone.getTimeZone("GMT+8")
    this.applyPattern("yyyy-MM-dd HH:mm:ss")
}

/**
 * 格式化时间戳
 * */
fun formatTimeStamp(timeStamp: Long): String {
    return timeFormat1.format(timeStamp)
}