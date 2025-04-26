package com.example.testappcompose.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testappcompose.common.CarouselItem
import com.example.testappcompose.core.extension.netDiagnostics
import com.example.testappcompose.core.service.CocktailService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientDetailViewModel @Inject constructor(
    private val cocktailService: CocktailService
) : ViewModel() {

    private var _viewState = MutableStateFlow<ViewState<IngredientDetailData>>(ViewState.Uninitialized)
    val viewState: StateFlow<ViewState<IngredientDetailData>> = _viewState

    fun loadData(ingredientName: String) {
        _viewState.update { it.errorToUninitialized() }

        loadDataIfNeeded(ingredientName)
    }

    private fun loadDataIfNeeded(ingredientName: String) = viewModelScope.launch {
        if (_viewState.value !is ViewState.Uninitialized) return@launch

        _viewState.update { ViewState.Loading }

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
            _viewState.tryEmit(
                ViewState.Loaded(
                    IngredientDetailData(
                        name = ingredientName,
                        image = "https://www.thecocktaildb.com/images/ingredients/$ingredientName.png",
                        abv = abv,
                        description = description,
                        cocktails = it
                    )
                )
            )
        } ?: run {
            _viewState.tryEmit(ViewState.Error(diagnostics.orEmpty()))
        }
    }
}

data class IngredientDetailData(
    val name: String,
    val image: String,
    val abv: String?,
    val description: String?,
    val cocktails: List<CarouselItem>
)
