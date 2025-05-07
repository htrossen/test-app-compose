package com.libraries.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.model.GlideUrl

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GlideImageWrapper(
    modifier: Modifier,
    url: String,
    contentScale: ContentScale = ContentScale.FillBounds,
    contentDescription: String? = null, // Use default when not important for A11Y
    imageAlignment: Alignment = Alignment.Center,
) {
    GlideImage(
        modifier = modifier,
        model = GlideUrl(url),
        alignment = imageAlignment,
        contentDescription = contentDescription,
        contentScale = contentScale,
        transition = CrossFade,
        loading = placeholder(ColorPainter(MaterialTheme.colorScheme.surface)),
        failure = placeholder(ColorPainter(MaterialTheme.colorScheme.surface)),
    )
}
