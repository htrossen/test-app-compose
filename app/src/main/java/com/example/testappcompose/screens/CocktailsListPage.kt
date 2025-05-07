package com.example.testappcompose.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testappcompose.R
import com.libraries.ui.ViewState
import com.libraries.ui.components.BackButton
import com.libraries.ui.components.GridView
import com.libraries.ui.components.LoadingState
import com.libraries.ui.components.ProblemState
import com.libraries.ui.components.TextHeadlineSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailsPage(
    searchName: String,
    nonAlcoholic: Boolean = false,
    navToCocktailDetails: (String) -> Unit,
    navBack: () -> Unit
) {
    val viewModel: CocktailsListViewModel = hiltViewModel()

    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData(searchName, nonAlcoholic)
    }

    AnimatedContent(
        targetState = viewState,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "Cocktails List Page"
    ) { state ->
        when (state) {
            ViewState.Uninitialized, ViewState.Loading -> LoadingState(
                modifier = Modifier.fillMaxSize(),
                navBack = navBack
            )
            is ViewState.Empty -> {} // NO-OP
            is ViewState.Error -> ProblemState(
                modifier = Modifier.fillMaxSize(),
                netDiagnostics = state.netDiagnostic,
                navBack = navBack,
                retry = { viewModel.loadData(searchName, nonAlcoholic) }
            )
            is ViewState.Loaded -> {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier
                                .background(color = MaterialTheme.colorScheme.primary),
                            colors = TopAppBarDefaults.topAppBarColors().copy(
                                containerColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                TextHeadlineSmall(
                                    text = if (nonAlcoholic) {
                                        stringResource(id = R.string.mocktails)
                                    } else {
                                        stringResource(
                                            id = R.string.type_cocktails,
                                            searchName
                                        )
                                    },
                                )
                            },
                            navigationIcon = {
                                BackButton(onBack = navBack)
                            }
                        )
                    }
                ) { paddingValues ->
                    GridView(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(paddingValues),
                        imageModifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .size(200.dp),
                        components = state.data,
                        clickAction = { navToCocktailDetails(it) }
                    )
                }
            }
        }
    }
}
