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
class CocktailsListViewModel @Inject constructor(
    private val cocktailService: CocktailService
) : ViewModel() {

    private var _viewState = MutableStateFlow<ViewState<List<CarouselItem>>>(ViewState.Uninitialized)
    val viewState: StateFlow<ViewState<List<CarouselItem>>> = _viewState

    fun loadData(searchName: String, nonAlcoholic: Boolean) {
        _viewState.update { it.errorToUninitialized() }

        loadDataIfNeeded(searchName, nonAlcoholic)
    }

    private fun loadDataIfNeeded(searchName: String, nonAlcoholic: Boolean) {
        if (_viewState.value !is ViewState.Uninitialized) return

        _viewState.update { ViewState.Loading }

        viewModelScope.launch {
            if (nonAlcoholic) {
                cocktailService.getNonAlcoholic().onSuccess {
                    _viewState.tryEmit(
                        if (it.isNotEmpty()) {
                            ViewState.Loaded(data = it)
                        } else {
                            ViewState.Error("Non-Alcoholic list was empty.")
                        }
                    )
                }.onFailure {
                    _viewState.tryEmit(ViewState.Error(it.netDiagnostics()))
                }
            } else {
                cocktailService.getCocktailsBySearchName(searchName).onSuccess {
                    _viewState.tryEmit(
                        if (it.isNotEmpty()) {
                            ViewState.Loaded(data = it)
                        } else {
                            ViewState.Error("Cocktails list for $searchName was empty.")
                        }
                    )
                }.onFailure {
                    _viewState.tryEmit(ViewState.Error(it.netDiagnostics()))
                }
            }
        }
    }
}
