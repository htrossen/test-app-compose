package com.example.testappcompose.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testappcompose.common.CarouselItem
import com.example.testappcompose.repo.PersonalizationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val personalizationRepo: PersonalizationRepo
) : ViewModel() {
    val viewState = mutableStateOf<FavoritesViewState>(FavoritesViewState.Loading)

    init {
        viewModelScope.launch {
            personalizationRepo.getFavorites().collect {
                if (it.isEmpty()) {
                    viewState.value = FavoritesViewState.Empty
                } else {
                    viewState.value = FavoritesViewState.Loaded(
                        it.map { favorite ->
                            CarouselItem(favorite.id, favorite.imageUrl, favorite.name)
                        }
                    )
                }
            }
        }
    }
}
sealed class FavoritesViewState {
    object Loading : FavoritesViewState()
    object Empty : FavoritesViewState()
    data class Loaded(
        val cocktails: List<CarouselItem>
    ) : FavoritesViewState()
}
