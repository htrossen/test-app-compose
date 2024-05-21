package com.example.testappcompose.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testappcompose.common.CarouselItem
import com.example.testappcompose.core.extension.netDiagnostics
import com.example.testappcompose.core.service.CocktailService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailsListViewModel @Inject constructor(
    private val cocktailService: CocktailService
) : ViewModel() {

    val viewState = mutableStateOf<ViewState<List<CarouselItem>>>(ViewState.Loading)

    fun loadData(searchName: String) = viewModelScope.launch {
        cocktailService.getCocktailsBySearchName(searchName).onSuccess {
            if (it.isNotEmpty()) {
                viewState.value = ViewState.Loaded(data = it)
            } else {
                viewState.value =
                    ViewState.Error("Cocktails list for $searchName was empty.")
            }
        }.onFailure {
            viewState.value = ViewState.Error(it.netDiagnostics())
        }
    }
}
