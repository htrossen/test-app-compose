package com.example.testappcompose.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testappcompose.R
import com.example.testappcompose.common.BackButton
import com.example.testappcompose.common.GridView
import com.example.testappcompose.common.LoadingState
import com.example.testappcompose.common.ProblemState

@Composable
fun FavoritesPage(
    navToCocktailDetails: (String) -> Unit,
    navBack: () -> Unit
) {
    val viewModel: FavoritesViewModel = hiltViewModel()

    val viewState by viewModel.viewState

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary),
                backgroundColor = MaterialTheme.colorScheme.primary,
                title = {
                    Text(
                        text = stringResource(id = R.string.favorites,),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    BackButton(onBack = navBack)
                }
            )
        }
    ) { paddingValues ->

        AnimatedContent(
            targetState = viewState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "Favorites Page"
        ) { state ->
            when (state) {
                is FavoritesViewState.Loading -> LoadingState(
                    modifier = Modifier.fillMaxSize(),
                )
                is FavoritesViewState.Empty -> ProblemState(
                    modifier = Modifier.fillMaxSize(),
                    netDiagnostics = "No favorites.",
                    imageUrl = "https://media.istockphoto.com/id/465431628/photo/collection-of-glasses-used-for-alcoholic-drinks-on-white-backdrop.jpg?s=612x612&w=0&k=20&c=vMSxtSwB-ph47H8xshDfLN4tv9ipXRZ6MxOYRAZbiAM=",
                    primaryTextResId = R.string.empty_fav_text_primary,
                    secondaryTextResId = R.string.empty_fav_text_secondary,
                )
                is FavoritesViewState.Loaded -> {
                    GridView(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(paddingValues),
                        imageModifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .size(200.dp),
                        components = state.cocktails,
                        clickAction = { navToCocktailDetails(it) }
                    )
                }
            }
        }
    }
}
