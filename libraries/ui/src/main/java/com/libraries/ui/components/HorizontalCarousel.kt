package com.libraries.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalCarousel(
    imageModifier: Modifier,
    components: List<CarouselItem>,
    clickAction: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        state = rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        components.forEach { component ->
            item {
                CarouselItemComponent(
                    modifier = Modifier
                        .width(192.dp)
                        .heightIn(min = 80.dp),
                    imageModifier = imageModifier,
                    component = component,
                    clickAction = clickAction
                )
            }
        }
    }
}
