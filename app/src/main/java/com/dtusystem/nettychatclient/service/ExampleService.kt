package com.dtusystem.nettychatclient.service

import com.dtusystem.nettychatclient.network.message.Formula
import com.dtusystem.nettychatclient.network.message.Response
import com.dtusystem.nettychatclient.network.message.Technology
import io.netty.util.concurrent.Promise

interface ExampleService {

    fun issueFormula(formula: Formula): Promise<Response>

    fun issueTechnology(technology: Technology): Promise<Response>

    suspend fun issueFormula2(formula: Formula): Response
}