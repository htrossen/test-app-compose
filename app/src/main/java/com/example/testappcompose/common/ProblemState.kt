package com.example.testappcompose.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.testappcompose.BuildConfig
import com.example.testappcompose.R

// TODO: Add retry button

@Composable
fun ProblemState(
    modifier: Modifier,
    netDiagnostics: String,
    imageUrl: String = "https://img.freepik.com/premium-photo/spilled-cocktail-overturned-glass_762026-79695.jpg",
    primaryTextResId: Int = R.string.error_text_primary,
    secondaryTextResId: Int = R.string.error_text_secondary,
    navBack: (() -> Unit)? = null
) {
    var showAlertDialog by remember { mutableStateOf(false) }

    Box {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GlideImageWrapper(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 12f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .clickable(enabled = BuildConfig.DEBUG) { showAlertDialog = true },
                url = imageUrl
            )
            Text(
                text = stringResource(id = primaryTextResId),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = secondaryTextResId),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        navBack?.let {
            FloatingBackButton(onBack = it)
        }
    }

    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = { showAlertDialog = false },
            confirmButton = { },
            title = {
                Text(
                    text = "Error Diagnostics",
                    style = MaterialTheme.typography.headlineSmall,
                )
            },
            text = {
                Text(
                    text = netDiagnostics,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        )
    }
}
