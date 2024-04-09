package com.dtusystem.nettychatclient.repository

import com.dtusystem.nettychatclient.network.message.Formula
import com.dtusystem.nettychatclient.network.message.Response
import com.dtusystem.nettychatclient.network.message.Technology
import com.dtusystem.nettychatclient.service.ExampleService
import dagger.hilt.android.scopes.ViewModelScoped
import io.netty.util.concurrent.Promise
import javax.inject.Inject

@ViewModelScoped
class ExampleRepository @Inject constructor(
    private val exampleService: ExampleService,
) {
    fun issueFormula(formula: Formula): Promise<Response<*>> {
        return exampleService.issueFormula(formula)
    }

    fun issueTechnology(technology: Technology): Promise<Response<*>> {
        return exampleService.issueTechnology(technology)
    }

    suspend fun issueFormula2(formula: Formula): Response<*> = exampleService.issueFormula2(formula)

}