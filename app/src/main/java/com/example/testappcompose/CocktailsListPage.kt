package com.example.testappcompose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testappcompose.common.BackButton
import com.example.testappcompose.common.ErrorState
import com.example.testappcompose.common.GridView
import com.example.testappcompose.common.LoadingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailsPage(
    searchName: String?,
    navToCocktailDetails: (String) -> Unit,
    navBack: () -> Unit
) {
    val viewModel: CocktailsViewModel = hiltViewModel()

    val viewState by viewModel.viewState

    LaunchedEffect(Unit) {
        searchName?.let {
            viewModel.loadData(searchName)
        } ?: run {
            viewModel.viewState.value = CocktailsViewState.Error("searchName null.")
        }
    }

    AnimatedContent(
        targetState = viewState,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "Cocktails List Page"
    ) { state ->
        when (state) {
            is CocktailsViewState.Loading -> LoadingState(
                modifier = Modifier.fillMaxSize(),
                navBack = navBack
            )
            is CocktailsViewState.Error -> ErrorState(
                modifier = Modifier.fillMaxSize(),
                netDiagnostics = state.netDiagnostic,
                navBack = navBack
            )
            is CocktailsViewState.Loaded -> {
                Scaffold(
                    topBar = {
                        Row(
                            modifier = Modifier
                                .background(color = MaterialTheme.colorScheme.surface)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BackButton(onBack = navBack)

                            Text(
                                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
                                text = stringResource(
                                    id = R.string.type_cocktails,
                                    searchName.orEmpty()
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                ) { paddingValues ->
                    GridView(
                        modifier = Modifier.padding(paddingValues),
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
