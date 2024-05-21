package com.example.testappcompose.screens

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testappcompose.R
import com.example.testappcompose.common.FloatingBackButton
import com.example.testappcompose.common.GlideImageWrapper
import com.example.testappcompose.common.LoadingState
import com.example.testappcompose.common.ProblemState

@Composable
fun CocktailDetailPage(
    cocktailId: String?,
    navBack: () -> Unit
) {
    val viewModel: CocktailDetailViewModel = hiltViewModel()

    val viewState by viewModel.viewState

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        cocktailId?.let {
            viewModel.loadData(cocktailId)
        } ?: run {
            viewModel.viewState.value = ViewState.Error("cocktailId null.")
        }
    }

    Box {
        AnimatedContent(
            targetState = viewState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "Cocktail Detail Page"
        ) { state ->
            when (state) {
                is ViewState.Loading -> LoadingState(modifier = Modifier.fillMaxSize())
                is ViewState.Empty -> {} // NO-OP
                is ViewState.Error -> ProblemState(
                    modifier = Modifier.fillMaxSize(),
                    netDiagnostics = state.netDiagnostic,
                    retry = if (cocktailId != null) {
                        { viewModel.loadData(cocktailId) }
                    } else null
                )
                is ViewState.Loaded -> {
                    val cocktail = state.data
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

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
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

                            // Favorite
                            val favorite by viewModel.favorite
                            Icon(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        cocktailId?.let {
                                            viewModel.saveOrRemoveAsFavorite(it, cocktail)
                                        }
                                    }
                                    .padding(8.dp),
                                painter = painterResource(
                                    id = if (favorite) {
                                        R.drawable.favorite_filled
                                    } else R.drawable.favorite_unfilled
                                ),
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = stringResource(
                                    id = if (favorite) {
                                        R.string.remove_from_favorites
                                    } else R.string.add_to_favorites
                                )
                            )

                            // Share
                            Icon(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        cocktailId?.let {
                                            val title = context.getString(
                                                R.string.share_title,
                                                cocktail.name
                                            )
                                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                                putExtra(Intent.EXTRA_TITLE, title)
                                                putExtra(
                                                    Intent.EXTRA_TEXT,
                                                    context.getString(
                                                        R.string.share_ingredients_instructions,
                                                        title,
                                                        cocktail.ingredients.joinToString("\n") {
                                                            "${it.amount} ${it.name}"
                                                        },
                                                        cocktail.instructions
                                                    )
                                                )
                                                type = "text/plain"
                                            }
                                            val shareIntent = Intent.createChooser(
                                                sendIntent,
                                                context.getString(
                                                    R.string.share_recipe
                                                )
                                            )
                                            ContextCompat.startActivity(context, shareIntent, null)
                                        }
                                    }
                                    .padding(8.dp),
                                painter = painterResource(id = R.drawable.share),
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = stringResource(id = R.string.share_recipe)
                            )
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.primary)

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
        FloatingBackButton(onBack = navBack)
    }
}
