package com.example.testappcompose.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.testappcompose.R

@Composable
fun BackButton(
    modifier: Modifier = Modifier.statusBarsPadding().padding(top = 8.dp, start = 8.dp),
    backIconResource: Int = R.drawable.arrow_back,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    onBack: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    Icon(
        painter = painterResource(backIconResource),
        contentDescription = "Navigate Up",
        tint = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .focusRequester(focusRequester)
            .then(modifier)
            .background(color = backgroundColor, shape = CircleShape)
            .padding(8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onBack() }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}
