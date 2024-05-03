package com.example.testappcompose.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.bumptech.glide.load.model.GlideUrl
import com.example.testappcompose.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun GlideImageWrapper(
    modifier: Modifier,
    url: String,
    contentDescription: String? = null // Use default when not important for A11Y
) {
    GlideImage(
        modifier = modifier,
        imageModel = { GlideUrl(url) },
        previewPlaceholder = painterResource(id = R.drawable.placeholder),
        imageOptions = ImageOptions(
            contentDescription = contentDescription,
        ),
        failure = { painterResource(id = R.drawable.placeholder) }
    )
}
