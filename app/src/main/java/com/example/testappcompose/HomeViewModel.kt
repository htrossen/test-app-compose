package com.example.testappcompose

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.testappcompose.common.CarouselItem

class HomeViewModel : ViewModel() {

    val viewState = mutableStateOf<HomeViewState>(HomeViewState.Loading)

    init {
        val imageUrlBase = "https://www.thecocktaildb.com/images/ingredients/%s.png"
        val liquors = listOf("Vodka", "Gin", "Tequila", "Rum", "Bourbon", "Whiskey")
        viewState.value = HomeViewState.Loaded(
            liquors = liquors.map { CarouselItem(it, imageUrlBase.format(it), it) }
        )
    }
}

sealed class HomeViewState {
    object Loading : HomeViewState()
    data class Loaded(
        val liquors: List<CarouselItem>
    ) : HomeViewState()
}
