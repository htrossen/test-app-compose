package com.libraries.core.extension

import retrofit2.HttpException

fun Throwable.netDiagnostics(): String {
    val sb = StringBuilder()

    when (this) {
        is HttpException -> {
            sb.appendLine(this::class.java.canonicalName)
            sb.appendLine("HTTP ${code()}")
            sb.appendLine(message())
        }
        is Exception -> {
            sb.appendLine(this::class.java.canonicalName)
            sb.appendLine(message)
        }
        else -> sb.appendLine(toString())
    }
    return sb.toString()
}
