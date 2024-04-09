package com.dtusystem.nettychatclient.service

import com.dtusystem.nettychatclient.network.message.Formula
import com.dtusystem.nettychatclient.network.message.Response
import com.dtusystem.nettychatclient.network.message.Technology
import io.netty.util.concurrent.Promise


/**
 * 如果使用的函数不是挂起函数，需要返回值是io.netty.util.concurrent.Promise.Promise<T>；
 * 如果使用的函数是挂起函数，返回值可以是定义的对象类
 * */
interface ExampleService {

    fun issueFormula(formula: Formula): Promise<Response<*>>

    fun issueTechnology(technology: Technology): Promise<Response<*>>

    suspend fun issueFormula2(formula: Formula): Response<*>
}