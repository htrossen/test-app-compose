package com.example.testappcompose.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.testappcompose.common.CarouselItem

class HomeViewModel : ViewModel() {

    val viewState = mutableStateOf<ViewState<List<CarouselItem>>>(ViewState.Loading)

    init {
        val imageUrlBase = "https://www.thecocktaildb.com/images/ingredients/%s.png"
        val liquors = listOf("Vodka", "Gin", "Tequila", "Rum", "Bourbon", "Whiskey")
        viewState.value = ViewState.Loaded(
            liquors.map { CarouselItem(it, imageUrlBase.format(it), it) }
        )
    }
}
