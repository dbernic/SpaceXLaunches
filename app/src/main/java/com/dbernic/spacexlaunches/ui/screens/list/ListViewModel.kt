package com.dbernic.spacexlaunches.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbernic.spacexlaunches.model.repositories.LaunchesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val launchesRepository: LaunchesRepository
): ViewModel() {

    val showItemsFlow = launchesRepository.getShowItemsFlow()
    val isShowFavoritesFlow = launchesRepository.getShowFavoritesFlow()

    fun fetchItems() {
        viewModelScope.launch {
            try {
                launchesRepository.fetchData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun switchFavorite(id: String) {
        launchesRepository.switchFavorite(id)
    }

    fun toggleFavorites() {
        launchesRepository.showFavorites(!isShowFavoritesFlow.value)
    }
}