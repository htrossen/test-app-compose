package com.example.testappcompose.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testappcompose.R
import com.libraries.ui.ViewState
import com.libraries.ui.components.FloatingBackButton
import com.libraries.ui.components.GlideImageWrapper
import com.libraries.ui.components.HorizontalCarousel
import com.libraries.ui.components.LoadingState
import com.libraries.ui.components.ProblemState
import com.libraries.ui.components.TextBodySmall
import com.libraries.ui.components.TextHeadlineLarge
import com.libraries.ui.components.TextHeadlineSmall

@Composable
fun IngredientDetailPage(
    ingredientName: String,
    navToCocktailDetails: (String) -> Unit,
    navToViewAll: () -> Unit,
    navBack: () -> Unit
) {
    val viewModel: IngredientDetailViewModel = hiltViewModel()

    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData(ingredientName)
    }

    Box {
        AnimatedContent(
            targetState = viewState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "Ingredient Detail Page"
        ) { state ->
            when (state) {
                ViewState.Uninitialized, ViewState.Loading -> LoadingState(modifier = Modifier.fillMaxSize())
                is ViewState.Empty -> {} // NO-OP
                is ViewState.Error -> ProblemState(
                    modifier = Modifier.fillMaxSize(),
                    netDiagnostics = state.netDiagnostic,
                    retry = { viewModel.loadData(ingredientName) }
                )
                is ViewState.Loaded -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .statusBarsPadding()
                            .navigationBarsPadding()
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            GlideImageWrapper(
                                modifier = Modifier
                                    .size(200.dp),
                                url = state.data.image
                            )

                            Column(modifier = Modifier.weight(.5f)) {
                                TextHeadlineLarge(
                                    text = state.data.name,
                                )
                                state.data.abv?.let { abv ->
                                    TextHeadlineSmall(
                                        text = "$abv ABV",
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.primary)

                        val labelModifier = if (state.data.cocktails.size > 10) {
                            Modifier
                                .clip(shape = RoundedCornerShape(12.dp))
                                .clickable { navToViewAll() }
                        } else Modifier

                        Row(
                            modifier = labelModifier
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextHeadlineSmall(
                                text = stringResource(
                                    id = R.string.type_cocktails,
                                    ingredientName
                                ),
                            )

                            if (state.data.cocktails.size > 10) {
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow_forward),
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    contentDescription = stringResource(id = R.string.view_all)
                                )
                            }
                        }

                        HorizontalCarousel(
                            imageModifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .size(200.dp),
                            components = state.data.cocktails.take(10),
                            clickAction = { navToCocktailDetails(it) }
                        )

                        state.data.description?.let { description ->
                            TextHeadlineSmall(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = stringResource(id = R.string.what_is, state.data.name),
                                textAlign = TextAlign.Center,
                            )

                            TextBodySmall(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = description,
                            )
                        }
                    }
                }
            }
        }
        FloatingBackButton(onBack = navBack)
    }
}
