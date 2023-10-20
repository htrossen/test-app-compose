package com.example.testappcompose.core.service

import com.example.testappcompose.core.extension.netDiagnostics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface Service {

    suspend fun <T> apiCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        call: suspend () -> T,
    ): Result<T> =
        with(dispatcher) {
            runCatching { call() }
                .onFailure {
                    // TODO: Use real logger
                    val netDiagnostics = it.netDiagnostics()
                    println("Api call failed: $netDiagnostics\n$it")
                }
        }
}
