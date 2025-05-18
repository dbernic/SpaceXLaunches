package com.dbernic.spacexlaunches.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dbernic.spacexlaunches.model.entities.ui.LaunchDetailsUi
import com.dbernic.spacexlaunches.model.repositories.LaunchesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val launchesRepository: LaunchesRepository
): ViewModel() {

    private val _details = MutableStateFlow<LaunchDetailsUi?>(null)
    val details = _details.asStateFlow()

    fun getDetails(id: String) {
        viewModelScope.launch {
            try {
                val result = launchesRepository.getDetails(id)
                _details.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun switchFavorite(id: String) {
        launchesRepository.switchFavorite(id)
        getDetails(id)
    }
}