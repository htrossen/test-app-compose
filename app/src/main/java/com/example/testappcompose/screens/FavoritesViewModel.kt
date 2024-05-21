package com.example.testappcompose.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testappcompose.common.CarouselItem
import com.example.testappcompose.core.repo.PersonalizationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val personalizationRepo: PersonalizationRepo
) : ViewModel() {
    val viewState = mutableStateOf<ViewState<List<CarouselItem>>>(ViewState.Loading)

    init {
        viewModelScope.launch {
            personalizationRepo.getFavorites().collect {
                if (it.isEmpty()) {
                    viewState.value = ViewState.Empty
                } else {
                    viewState.value = ViewState.Loaded(
                        it.map { favorite ->
                            CarouselItem(favorite.id, favorite.imageUrl, favorite.name)
                        }
                    )
                }
            }
        }
    }
}
