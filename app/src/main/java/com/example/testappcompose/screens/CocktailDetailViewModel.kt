package com.example.testappcompose.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.libraries.core.database.Favorite
import com.libraries.core.extension.netDiagnostics
import com.libraries.core.model.Cocktail
import com.libraries.core.repo.PersonalizationRepo
import com.libraries.core.service.CocktailService
import com.libraries.ui.ViewState
import com.libraries.ui.errorToUninitialized
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailDetailViewModel @Inject constructor(
    private val cocktailService: CocktailService,
    private val personalizationRepo: PersonalizationRepo
) : ViewModel() {

    private var _viewState = MutableStateFlow<ViewState<Cocktail>>(ViewState.Uninitialized)
    val viewState: StateFlow<ViewState<Cocktail>> = _viewState

    val favorite = mutableStateOf(false)

    fun loadData(id: String) {
        _viewState.update { it.errorToUninitialized() }

        loadDataIfNeeded(id)
    }

    private fun loadDataIfNeeded(id: String) {
        if (_viewState.value !is ViewState.Uninitialized) return

        _viewState.update { ViewState.Loading }

        viewModelScope.launch {
            cocktailService.getCocktailById(id).onSuccess {
                _viewState.tryEmit(
                    ViewState.Loaded(data = it)
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
