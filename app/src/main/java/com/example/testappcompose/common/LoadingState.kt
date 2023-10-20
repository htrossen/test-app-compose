package com.example.testappcompose.common

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingState(
    modifier: Modifier,
    navBack: (() -> Unit)? = null
) {
    Box(modifier = modifier) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
        navBack?.let {
            BackButton(onBack = it)
        }
    }
}
