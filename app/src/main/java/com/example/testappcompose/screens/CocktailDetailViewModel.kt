package com.example.testappcompose.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testappcompose.core.data.Favorite
import com.example.testappcompose.core.extension.netDiagnostics
import com.example.testappcompose.core.model.Cocktail
import com.example.testappcompose.core.service.CocktailService
import com.example.testappcompose.repo.PersonalizationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailDetailViewModel @Inject constructor(
    private val cocktailService: CocktailService,
    private val personalizationRepo: PersonalizationRepo
) : ViewModel() {

    val viewState = mutableStateOf<CocktailDetailsViewState>(CocktailDetailsViewState.Loading)

    val favorite = mutableStateOf(false)

    fun loadData(id: String) {
        viewModelScope.launch {
            cocktailService.getCocktailById(id).onSuccess {
                if (it != null) {
                    viewState.value = CocktailDetailsViewState.Loaded(cocktail = it)
                } else {
                    viewState.value =
                        CocktailDetailsViewState.Error("Cocktail info for $id was null.")
                }
            }.onFailure {
                viewState.value = CocktailDetailsViewState.Error(it.netDiagnostics())
            }
        }
        viewModelScope.launch {
            favorite.value = personalizationRepo.isFavorites(id)
        }
    }

    fun saveOrRemoveAsFavorite(id: String, cocktail: Cocktail) {
        viewModelScope.launch {
            if (favorite.value) {
                personalizationRepo.deleteFavorite(id)
            } else {
                personalizationRepo.saveFavorites(
                    Favorite(id = id, name = cocktail.name, imageUrl = cocktail.image)
                )
            }
        }
        favorite.value = !favorite.value
    }
}
sealed class CocktailDetailsViewState {
    object Loading : CocktailDetailsViewState()
    data class Error(val netDiagnostic: String) : CocktailDetailsViewState()
    data class Loaded(
        val cocktail: Cocktail
    ) : CocktailDetailsViewState()
}
