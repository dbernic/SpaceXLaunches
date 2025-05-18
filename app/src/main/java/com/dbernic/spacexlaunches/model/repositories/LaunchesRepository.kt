package com.dbernic.spacexlaunches.model.repositories

import com.dbernic.spacexlaunches.model.entities.ui.LaunchDetailsUi
import com.dbernic.spacexlaunches.model.entities.ui.LaunchItemUI
import kotlinx.coroutines.flow.StateFlow

interface LaunchesRepository {

    suspend fun fetchData()

    fun showFavorites(show: Boolean)

    fun setFavorite(id: String)

    fun unSetFavorite(id: String)

    fun switchFavorite(id: String)

    suspend fun getDetails(id: String): LaunchDetailsUi?

    fun getShowItemsFlow(): StateFlow<List<LaunchItemUI>>

    fun getShowFavoritesFlow(): StateFlow<Boolean>
}