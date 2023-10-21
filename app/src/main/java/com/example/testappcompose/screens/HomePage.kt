package com.example.testappcompose.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.testappcompose.core.extension.clickableWithPressedListener
import com.example.testappcompose.core.extension.titleCase

@Composable
fun HomePage(
    navToSearchResults: (String) -> Unit,
    navToFavorites: () -> Unit,
    navToIngredientDetails: (String) -> Unit
) {
    val viewModel: HomeViewModel = viewModel()

    val viewState by viewModel.viewState

    val homeImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSutMvXbMT3BPFL24wuSe2cQZeM6SYk1D8Pobj2seXkUKNob1Y0RtbzN7tA_i7bjNDCA90&usqp=CAU"
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
                backgroundColor = MaterialTheme.colorScheme.primary,
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
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.search_placeholder),
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
                        shape = CircleShape,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.background,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )
                },
                actions = {
                    if (searchBarFocused) {
                        var pressed by remember { mutableStateOf(false) }
                        val color = if (pressed) {
                            MaterialTheme.colorScheme.onPrimary
                        } else MaterialTheme.colorScheme.background

                        Text(
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clickableWithPressedListener(
                                    pressChanged = { pressed = it },
                                    onClick = {
                                        searchText = TextFieldValue("")
                                        searchBarFocused = false
                                        focusManager.clearFocus()
                                    }
                                ),
                            text = stringResource(id = R.string.cancel),
                            color = color,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    } else {
                        Icon(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { navToFavorites() }
                                .padding(8.dp),
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
}
