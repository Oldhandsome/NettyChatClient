package com.dtusystem.nettychatclient.network

@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {
    data class Ok<T>(
        val data: T,
    ) : ApiResponse<T>()

    data class Error<T>(
        val throwable: Throwable,
    ) : ApiResponse<T>()
}

fun <T> ApiResponse<T>.getOrNull(): T? = when (this) {
    is ApiResponse.Ok<*> -> data as T
    is ApiResponse.Error<*> -> null
}

fun <T> ApiResponse<T>.getOrThrow(): T = when (this) {
    is ApiResponse.Ok<*> -> data as T
    is ApiResponse.Error<*> -> throw throwable
}