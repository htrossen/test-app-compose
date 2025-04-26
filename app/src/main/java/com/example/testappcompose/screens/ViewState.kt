package com.example.testappcompose.screens

sealed class ViewState<out T> {
    data object Uninitialized : ViewState<Nothing>()
    data object Loading : ViewState<Nothing>()
    data object Empty : ViewState<Nothing>()
    data class Loaded<T>(val data: T) : ViewState<T>()
    data class Error(val netDiagnostic: String) : ViewState<Nothing>()
}

fun <T> ViewState<T>.errorToUninitialized() =
    if (this is ViewState.Error) {
        ViewState.Uninitialized
    } else this
