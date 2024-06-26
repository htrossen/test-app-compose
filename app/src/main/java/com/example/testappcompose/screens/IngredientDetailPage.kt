package com.example.testappcompose.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testappcompose.R
import com.example.testappcompose.common.FloatingBackButton
import com.example.testappcompose.common.GlideImageWrapper
import com.example.testappcompose.common.HorizontalCarousel
import com.example.testappcompose.common.LoadingState
import com.example.testappcompose.common.ProblemState
import com.example.testappcompose.core.extension.clickableWithPressedListener

@Composable
fun IngredientDetailPage(
    ingredientName: String?,
    navToCocktailDetails: (String) -> Unit,
    navToViewAll: () -> Unit,
    navBack: () -> Unit
) {
    val viewModel: IngredientDetailViewModel = hiltViewModel()

    val viewState by viewModel.viewState

    LaunchedEffect(Unit) {
        ingredientName?.let {
            viewModel.loadData(ingredientName)
        } ?: run {
            viewModel.viewState.value = ViewState.Error("ingredientName null.")
        }
    }

    Box {
        AnimatedContent(
            targetState = viewState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "Ingredient Detail Page"
        ) { state ->
            when (state) {
                is ViewState.Loading -> LoadingState(modifier = Modifier.fillMaxSize())
                is ViewState.Empty -> {} // NO-OP
                is ViewState.Error -> ProblemState(
                    modifier = Modifier.fillMaxSize(),
                    netDiagnostics = state.netDiagnostic,
                    retry = if (ingredientName != null) {
                        { viewModel.loadData(ingredientName) }
                    } else null
                )
                is ViewState.Loaded -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
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
                                Text(
                                    text = state.data.name,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.headlineLarge
                                )
                                state.data.abv?.let { abv ->
                                    Text(
                                        text = "$abv ABV",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.primary)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(
                                    id = R.string.type_cocktails,
                                    ingredientName.orEmpty()
                                ),
                                style = MaterialTheme.typography.headlineSmall,
                            )

                            if (state.data.cocktails.size > 10) {
                                var pressed by remember { mutableStateOf(false) }
                                val color = if (pressed) {
                                    MaterialTheme.colorScheme.tertiary
                                } else MaterialTheme.colorScheme.primary

                                Text(
                                    modifier = Modifier
                                        .clickableWithPressedListener(
                                            pressChanged = { pressed = it },
                                            onClick = navToViewAll
                                        ),
                                    text = stringResource(id = R.string.view_all),
                                    color = color,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineSmall,
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
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = stringResource(id = R.string.what_is, state.data.name),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall,
                            )

                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = description,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
        }
        FloatingBackButton(onBack = navBack)
    }
}
