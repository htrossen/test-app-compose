package com.example.testappcompose.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libraries.core.extension.netDiagnostics
import com.libraries.core.service.CocktailService
import com.libraries.ui.ViewState
import com.libraries.ui.components.CarouselItem
import com.libraries.ui.errorToUninitialized
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

    private fun loadDataIfNeeded(ingredientName: String) {
        if (_viewState.value !is ViewState.Uninitialized) return

        _viewState.update { ViewState.Loading }

        // On Success -- can live without if call fails
        var abv: String? = null
        var description: String? = null

        // On Success -- if call fails or is empty show error screen
        var cocktails: List<CarouselItem>? = null

        // On Error
        var diagnostics: String? = null
        viewModelScope.launch {
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
                        cocktails = it
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
}

data class IngredientDetailData(
    val name: String,
    val image: String,
    val abv: String?,
    val description: String?,
    val cocktails: List<CarouselItem>
)
