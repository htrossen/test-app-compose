package com.example.testappcompose

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
class IngredientDetailViewModel @Inject constructor(
    private val cocktailService: CocktailService
) : ViewModel() {

    val viewState = mutableStateOf<IngredientDetailViewState>(IngredientDetailViewState.Loading)

    fun loadData(ingredientName: String) = viewModelScope.launch {
        // On Success -- can live without if call fails
        var abv: String? = null
        var description: String? = null

        // On Success -- if call fails or is empty show error screen
        var cocktails: List<CarouselItem>? = null

        // On Error
        var diagnostics: String? = null
        viewModelScope.launch {
            launch {
                cocktailService.getCocktailIngredient(ingredientName).onSuccess {
                    abv = it.abv
                    description = it.description
                }.onFailure {
                    diagnostics += it.netDiagnostics()
                }
            }
            launch {
                cocktailService.getCocktailsBySearchName(ingredientName).onSuccess {
                    if (it.isNotEmpty()) {
                        cocktails = it
                    } else diagnostics += "Cocktails list for $ingredientName was empty."
                }.onFailure {
                    diagnostics += it.netDiagnostics()
                }
            }
        }.join()

        cocktails?.let {
            viewState.value = IngredientDetailViewState.Loaded(
                name = ingredientName,
                image = "https://www.thecocktaildb.com/images/ingredients/$ingredientName.png",
                abv = abv,
                description = description,
                cocktails = it
            )
        } ?: run {
            viewState.value = IngredientDetailViewState.Error(diagnostics.orEmpty())
        }
    }
}

sealed class IngredientDetailViewState {
    object Loading : IngredientDetailViewState()
    data class Error(val netDiagnostic: String) : IngredientDetailViewState()
    data class Loaded(
        val name: String,
        val image: String,
        val abv: String?,
        val description: String?,
        val cocktails: List<CarouselItem>
    ) : IngredientDetailViewState()
}
