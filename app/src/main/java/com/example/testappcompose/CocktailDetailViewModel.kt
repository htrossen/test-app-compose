package com.example.testappcompose

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testappcompose.core.extension.netDiagnostics
import com.example.testappcompose.core.model.Cocktail
import com.example.testappcompose.core.service.TestService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailDetailViewModel @Inject constructor(
    private val testService: TestService
) : ViewModel() {

    val viewState = mutableStateOf<CocktailDetailsViewState>(CocktailDetailsViewState.Loading)

    fun loadData(id: String) = viewModelScope.launch {
        testService.getCocktailById(id).onSuccess {
            if (it != null) {
                viewState.value = CocktailDetailsViewState.Loaded(cocktail = it)
            } else {
                viewState.value = CocktailDetailsViewState.Error("Cocktail info for $id was null.")
            }
        }.onFailure {
            viewState.value = CocktailDetailsViewState.Error(it.netDiagnostics())
        }
    }
}
sealed class CocktailDetailsViewState {
    object Loading : CocktailDetailsViewState()
    data class Error(val netDiagnostic: String) : CocktailDetailsViewState()
    data class Loaded(
        val cocktail: Cocktail
    ) : CocktailDetailsViewState()
}
