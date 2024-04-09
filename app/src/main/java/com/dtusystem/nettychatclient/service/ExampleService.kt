package com.dtusystem.nettychatclient.service

import com.dtusystem.nettychatclient.network.message.Formula
import com.dtusystem.nettychatclient.network.message.Response
import io.netty.util.concurrent.Promise


/**
 * 如果使用的函数不是挂起函数，需要返回值是 io.netty.util.concurrent.Promise.Promise<Response<T>>；
 * 如果使用的函数不是挂起函数，需要返回值是 io.netty.util.concurrent.Promise.Promise<T>；
 * 如果使用的函数是挂起函数，返回值可以是 Response<T>
 * 如果使用的函数是挂起函数，返回值可以是 T
 * */
interface ExampleService {

    fun issueFormula(formula: Formula): Promise<Response<String>>

    suspend fun issueFormula2(formula: Formula): Response<String>

    fun issueFormula3(formula: Formula): Promise<String>

    suspend fun issueFormula4(formula: Formula): String

}