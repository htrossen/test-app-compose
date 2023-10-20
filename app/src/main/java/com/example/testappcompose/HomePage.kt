package com.example.testappcompose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testappcompose.common.GlideImageWrapper
import com.example.testappcompose.common.HorizontalCarousel
import com.example.testappcompose.common.LoadingState

@Composable
fun HomePage(
    navToIngredientDetails: (String) -> Unit
) {
    val viewModel: HomeViewModel = viewModel()

    val viewState by viewModel.viewState

    val homeImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSutMvXbMT3BPFL24wuSe2cQZeM6SYk1D8Pobj2seXkUKNob1Y0RtbzN7tA_i7bjNDCA90&usqp=CAU"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlideImageWrapper(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .padding(16.dp)
                .clip(RoundedCornerShape(6.dp)),
            url = homeImageUrl)
        Text(
            text = stringResource(id = R.string.find_next_cocktail),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge,
        )
        Divider(
            modifier = Modifier.padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )

        AnimatedContent(
            targetState = viewState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "Ingredient Carousel"
        ) { state ->
            when (state) {
                is HomeViewState.Loading -> LoadingState(modifier = Modifier.fillMaxSize())
                is HomeViewState.Loaded -> {
                    Column {
                        Text(
                            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
                            text = stringResource(id = R.string.search_by_liquor),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        HorizontalCarousel(
                            imageModifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(vertical = 16.dp)
                                .size(200.dp),
                            components = state.liquors,
                            clickAction = { navToIngredientDetails(it) }
                        )
                    }
                }
            }
        }
    }
}
