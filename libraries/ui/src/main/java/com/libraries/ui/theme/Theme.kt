package com.libraries.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BlueLight,
    secondary = BlueDark,
    tertiary = BlueDarkest,
    surface = Gray900,
    background = BlueDarkest,
    onPrimary = Gray,
    onSecondary = Gray,
    onTertiary = Gray,
    onBackground = Gray,
    onSurface = Gray,
)

private val LightColorScheme = lightColorScheme(
    primary = Burgundy600,
    secondary = Burgundy200,
    tertiary = Tan,
    surface = Color.White,
    background = Tan,
    onPrimary = Gray900,
    onSecondary = Gray900,
    onTertiary = Gray900,
    onBackground = Gray900,
    onSurface = Gray900,
)

@Composable
fun TestAppComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
