package com.example.testappcompose.core.extension

import kotlin.String

/**
 * Capitalize the first word of a string
 */
fun String.sentenceCase(): String = lowercase().replaceFirstChar(Char::uppercase)

/**
 * Capitalize the each word of a string
 */
fun String.titleCase(): String = lowercase().split(" ").joinToString(separator = " ") {
    it.replaceFirstChar(Char::uppercase)
}
