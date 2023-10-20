package com.example.testappcompose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testappcompose.common.BackButton
import com.example.testappcompose.common.ErrorState
import com.example.testappcompose.common.GlideImageWrapper
import com.example.testappcompose.common.LoadingState

@Composable
fun CocktailDetailPage(
    cocktailId: String?,
    navBack: () -> Unit
) {
    val viewModel: CocktailDetailViewModel = hiltViewModel()

    val viewState by viewModel.viewState

    LaunchedEffect(Unit) {
        cocktailId?.let {
            viewModel.loadData(cocktailId)
        } ?: run {
            viewModel.viewState.value = CocktailDetailsViewState.Error("cocktailId null.")
        }
    }

    Box {
        AnimatedContent(
            targetState = viewState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "Cocktail Detail Page"
        ) { state ->
            when (state) {
                is CocktailDetailsViewState.Loading -> LoadingState(modifier = Modifier.fillMaxSize())
                is CocktailDetailsViewState.Error -> ErrorState(
                    modifier = Modifier.fillMaxSize(),
                    netDiagnostics = state.netDiagnostic
                )
                is CocktailDetailsViewState.Loaded -> {
                    val cocktail = state.cocktail
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            GlideImageWrapper(
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                url = cocktail.image
                            )
                        }

                        Column {
                            Text(
                                text = cocktail.name,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                text = stringResource(id = R.string.glass_type, cocktail.glass),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.primary)

                        Text(
                            text = stringResource(id = R.string.ingredients),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall,
                        )

                        cocktail.ingredients.forEach { ingredient ->
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "${ingredient.amount} ",
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                                Text(
                                    text = ingredient.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }

                        Text(
                            text = stringResource(id = R.string.instructions),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall,
                        )

                        Text(
                            text = cocktail.instructions,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
        BackButton(onBack = navBack)
    }
}
