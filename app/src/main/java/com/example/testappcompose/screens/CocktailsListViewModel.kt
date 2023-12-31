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
class CocktailsViewModel @Inject constructor(
    private val cocktailService: CocktailService
) : ViewModel() {

    val viewState = mutableStateOf<CocktailsViewState>(CocktailsViewState.Loading)

    fun loadData(searchName: String) = viewModelScope.launch {
        cocktailService.getCocktailsBySearchName(searchName).onSuccess {
            if (it.isNotEmpty()) {
                viewState.value = CocktailsViewState.Loaded(cocktails = it)
            } else {
                viewState.value =
                    CocktailsViewState.Error("Cocktails list for $searchName was empty.")
            }
        }.onFailure {
            viewState.value = CocktailsViewState.Error(it.netDiagnostics())
        }
    }
}
sealed class CocktailsViewState {
    object Loading : CocktailsViewState()
    data class Error(val netDiagnostic: String) : CocktailsViewState()
    data class Loaded(
        val cocktails: List<CarouselItem>
    ) : CocktailsViewState()
}
