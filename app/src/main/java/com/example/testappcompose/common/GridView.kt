package com.example.testappcompose.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GridView(
    modifier: Modifier,
    imageModifier: Modifier,
    components: List<CarouselItem>,
    clickAction: (String) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        components.forEach { component ->
            item {
                CarouselItem(
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
