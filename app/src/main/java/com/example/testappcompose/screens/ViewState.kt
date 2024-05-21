package com.example.testappcompose.screens

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    object Empty : ViewState<Nothing>()
    data class Loaded<T>(val data: T) : ViewState<T>()
    data class Error(val netDiagnostic: String) : ViewState<Nothing>()
}
