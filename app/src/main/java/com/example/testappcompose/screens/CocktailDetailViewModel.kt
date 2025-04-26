package com.example.testappcompose.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testappcompose.core.data.Favorite
import com.example.testappcompose.core.extension.netDiagnostics
import com.example.testappcompose.core.model.Cocktail
import com.example.testappcompose.core.repo.PersonalizationRepo
import com.example.testappcompose.core.service.CocktailService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailDetailViewModel @Inject constructor(
    private val cocktailService: CocktailService,
    private val personalizationRepo: PersonalizationRepo
) : ViewModel() {

    private var _viewState = MutableStateFlow<ViewState<Cocktail>>(ViewState.Loading)
    val viewState: StateFlow<ViewState<Cocktail>> = _viewState

    val favorite = mutableStateOf(false)

    fun loadData(id: String) {
        viewModelScope.launch {
            cocktailService.getCocktailById(id).onSuccess {
                _viewState.tryEmit(
                    if (it != null) {
                        ViewState.Loaded(data = it)
                    } else {
                        ViewState.Error("Cocktail info for $id was null.")
                    }
                )
            }.onFailure {
                _viewState.tryEmit(ViewState.Error(it.netDiagnostics()))
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
