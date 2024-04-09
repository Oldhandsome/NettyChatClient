package com.dtusystem.nettychatclient.repository

import com.dtusystem.nettychatclient.network.message.Formula
import com.dtusystem.nettychatclient.network.message.Response
import com.dtusystem.nettychatclient.service.ExampleService
import dagger.hilt.android.scopes.ViewModelScoped
import io.netty.util.concurrent.Promise
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@ViewModelScoped
class ExampleRepository @Inject constructor(
    private val exampleService: ExampleService,
) {
    fun issueFormula(formula: Formula): Promise<Response<String>> {
        return exampleService.issueFormula(formula)
    }

    suspend fun issueFormula2(formula: Formula): Response<String> =
        exampleService.issueFormula2(formula)

    fun issueFormula3(formula: Formula): Promise<String> {
        return exampleService.issueFormula3(formula)
    }

    suspend fun issueFormula4(coroutineContext: CoroutineContext, formula: Formula): String =
        withContext(coroutineContext) {
            exampleService.issueFormula4(formula)
        }
}