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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.testappcompose.BuildConfig

// TODO: Add retry button

@Composable
fun ErrorState(
    modifier: Modifier,
    netDiagnostics: String,
    navBack: (() -> Unit)? = null
) {
    var showAlertDialog by remember { mutableStateOf(false) }

    val spilledDrinkUrl = "https://img.freepik.com/premium-photo/spilled-cocktail-overturned-glass_762026-79695.jpg"

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
                url = spilledDrinkUrl
            )
            Text(
                text = "Oops, looks like we had a little too much fun!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "Please bare with us while we clean up our mess.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        navBack?.let {
            BackButton(onBack = it)
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
