package com.libraries.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.libraries.ui.R

@Composable
fun CarouselItemComponent(
    modifier: Modifier,
    imageModifier: Modifier,
    component: CarouselItem,
    clickAction: (String) -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .clickable(onClick = { clickAction(component.id) })
        ) {
            GlideImageWrapper(
                modifier = imageModifier,
                url = component.imageUrl,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.primary)
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.padding(start = 16.dp).weight(1f),
                ) {
                    TextBodyLarge(
                        text = component.title,
                        maxLines = 2,
                    )
                    component.subtitle?.let { subtitle ->
                        TextBodySmall(
                            text = subtitle,
                            fontWeight = FontWeight.Light,
                            maxLines = 2,
                        )
                    }
                }

                Icon(
                    modifier = Modifier.padding(end = 16.dp),
                    painter = painterResource(id = R.drawable.chevron_right),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        }
    }
}

data class CarouselItem(
    val id: String,
    val imageUrl: String,
    val title: String,
    val subtitle: String? = null,
)
