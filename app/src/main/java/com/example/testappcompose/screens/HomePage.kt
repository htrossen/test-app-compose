package com.example.testappcompose.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testappcompose.R
import com.example.testappcompose.common.GlideImageWrapper
import com.example.testappcompose.common.HorizontalCarousel
import com.example.testappcompose.common.LoadingState
import com.example.testappcompose.core.extension.titleCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    navToSearchResults: (String) -> Unit,
    navToFavorites: () -> Unit,
    navToIngredientDetails: (String) -> Unit
) {
    val viewModel: HomeViewModel = viewModel()

    val viewState by viewModel.viewState.collectAsState()

    val homeImageUrl = "https://img.freepik.com/free-photo/fresh-cocktails-with-ice-lemon-lime-fruits-generative-ai_188544-12370.jpg?size=626&ext=jpg&ga=GA1.1.386372595.1697932800&semt=sph"
    Scaffold(
        topBar = {
            var searchText by remember { mutableStateOf(TextFieldValue()) }
            var searchBarFocused by remember { mutableStateOf(false) }
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current

            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary),
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, end = 12.dp)
                            .border(1.dp, MaterialTheme.colorScheme.background, CircleShape)
                            .focusRequester(focusRequester),
                        textStyle = MaterialTheme.typography.labelLarge,
                        value = searchText,
                        onValueChange = { newText ->
                            searchText = newText
                            searchBarFocused = true
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                tint = MaterialTheme.colorScheme.background,
                                contentDescription = stringResource(id = R.string.search)
                            )
                        },
                        trailingIcon = if (searchText.text.isNotEmpty()) {
                            {
                                IconButton(
                                    onClick = {
                                        searchText = TextFieldValue("")
                                        searchBarFocused = false
                                        focusManager.clearFocus()
                                    }
                                ) {
                                    Icon(
                                        painterResource(R.drawable.close),
                                        tint = MaterialTheme.colorScheme.background,
                                        contentDescription = stringResource(R.string.clear_search_cd)
                                    )
                                }
                            }
                        } else null,
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.search_placeholder),
                                color = MaterialTheme.colorScheme.background.copy(.8f),
                                style = MaterialTheme.typography.labelLarge,
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus()
                                val text = searchText.text
                                searchText = TextFieldValue("")
                                if (text.isNotBlank()) {
                                    navToSearchResults(text.trim().titleCase())
                                }
                            }
                        ),
                        singleLine = true,
                        shape = CircleShape,
                        colors = TextFieldDefaults.colors().copy(
                            focusedContainerColor = MaterialTheme.colorScheme.primary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = MaterialTheme.colorScheme.background,
                            unfocusedTextColor = MaterialTheme.colorScheme.background,
                            cursorColor = MaterialTheme.colorScheme.background,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )
                },
                actions = {
                    IconButton(
                        modifier = Modifier
                            .padding(end = 4.dp),
                        onClick = { navToFavorites() },
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            painter = painterResource(id = R.drawable.favorite_filled),
                            tint = MaterialTheme.colorScheme.background,
                            contentDescription = stringResource(id = R.string.favorites)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImageWrapper(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
                    .clip(RoundedCornerShape(6.dp)),
                url = homeImageUrl
            )
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.find_next_cocktail),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge,
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )

            AnimatedContent(
                targetState = viewState,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "Ingredient Carousel"
            ) { state ->
                when (state) {
                    ViewState.Uninitialized, ViewState.Loading -> LoadingState(modifier = Modifier.fillMaxSize())
                    is ViewState.Empty -> {} // NO-OP
                    is ViewState.Error -> {} // NO-OP
                    is ViewState.Loaded -> {
                        Column {
                            Text(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    bottom = 16.dp,
                                    end = 16.dp
                                ),
                                text = stringResource(id = R.string.search_by_liquor),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            HorizontalCarousel(
                                imageModifier = Modifier
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(16.dp)
                                    .size(175.dp),
                                components = state.data,
                                clickAction = { navToIngredientDetails(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}
