package com.example.testappcompose

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testappcompose.common.CarouselItem
import com.example.testappcompose.core.extension.netDiagnostics
import com.example.testappcompose.core.service.TestService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailsViewModel @Inject constructor(
    private val testService: TestService
) : ViewModel() {

    val viewState = mutableStateOf<CocktailsViewState>(CocktailsViewState.Loading)

    fun loadData(ingredientName: String) = viewModelScope.launch {
        testService.getCocktailsByIngredient(ingredientName).onSuccess {
            if (it.isNotEmpty()) {
                viewState.value = CocktailsViewState.Loaded(cocktails = it)
            } else {
                viewState.value = CocktailsViewState.Error("Cocktails list for $ingredientName was empty.")
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
