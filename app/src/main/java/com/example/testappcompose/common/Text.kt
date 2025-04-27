package com.example.testappcompose.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun TextHeadlineLarge(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign? = null
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        fontWeight = FontWeight.Bold,
        color = color,
        style = MaterialTheme.typography.headlineLarge,
    )
}

@Composable
fun TextHeadlineSmall(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign? = null
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        color = color,
        style = MaterialTheme.typography.headlineSmall,
    )
}

@Composable
fun TextTitleMedium(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign? = null,
    textDecoration: TextDecoration? = null,
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        textDecoration = textDecoration,
        color = color,
        style = MaterialTheme.typography.titleMedium,
    )
}

@Composable
fun TextBodyLarge(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        fontWeight = FontWeight.Bold,
        color = color,
        style = MaterialTheme.typography.bodyLarge,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis

    )
}

@Composable
fun TextBodyMedium(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign? = null,
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        color = color,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
fun TextBodySmall(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign? = null,
    fontWeight: FontWeight = FontWeight.Bold,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        color = color,
        fontWeight = fontWeight,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodySmall,
    )
}
