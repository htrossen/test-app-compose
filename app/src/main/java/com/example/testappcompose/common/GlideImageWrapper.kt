package com.example.testappcompose.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.bumptech.glide.load.model.GlideUrl
import com.skydoves.landscapist.glide.GlideImage
import com.example.testappcompose.R

@Composable
fun GlideImageWrapper(
    modifier: Modifier,
    url: String
) {
    GlideImage(
        modifier = modifier,
        imageModel = GlideUrl(url),
        placeHolder = painterResource(id = R.drawable.placeholder),
        contentDescription = null,
        error = painterResource(id = R.drawable.placeholder)
    )
}
