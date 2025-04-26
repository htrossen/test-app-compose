package com.example.testappcompose.screens

import androidx.lifecycle.ViewModel
import com.example.testappcompose.common.CarouselItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private var _viewState = MutableStateFlow<ViewState<List<CarouselItem>>>(ViewState.Loading)
    val viewState: StateFlow<ViewState<List<CarouselItem>>> = _viewState

    init {
        val imageUrlBase = "https://www.thecocktaildb.com/images/ingredients/%s.png"
        val liquors = listOf("Vodka", "Gin", "Tequila", "Rum", "Bourbon", "Whiskey")
        _viewState.tryEmit(
            ViewState.Loaded(
                liquors.map { CarouselItem(it, imageUrlBase.format(it), it) }
            )
        )
    }
}
