package com.example.testappcompose.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testappcompose.common.CarouselItem
import com.example.testappcompose.core.repo.PersonalizationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val personalizationRepo: PersonalizationRepo
) : ViewModel() {
    private var _viewState = MutableStateFlow<ViewState<List<CarouselItem>>>(ViewState.Loading)
    val viewState: StateFlow<ViewState<List<CarouselItem>>> = _viewState

    init {
        viewModelScope.launch {
            personalizationRepo.getFavorites().collect {
                _viewState.tryEmit(
                    if (it.isEmpty()) {
                        ViewState.Empty
                    } else {
                        ViewState.Loaded(
                            it.map { favorite ->
                                CarouselItem(favorite.id, favorite.imageUrl, favorite.name)
                            }
                        )
                    }
                )
            }
        }
    }
}
