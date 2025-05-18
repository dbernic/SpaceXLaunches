package com.dbernic.spacexlaunches.model.repositories

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import com.dbernic.spacexlaunches.model.datasources.local.PreferencesDataSourceImpl
import com.dbernic.spacexlaunches.model.datasources.rest.HttpInterface
import com.dbernic.spacexlaunches.model.entities.rest.LaunchData
import com.dbernic.spacexlaunches.model.entities.ui.LaunchDetailsUi
import com.dbernic.spacexlaunches.model.entities.ui.LaunchItemUI
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LaunchesRepositoryImpl(
    private val httpDataSource: HttpInterface,
    private val preferencesDataSource: PreferencesDataSourceImpl,
): LaunchesRepository {
    private val gson = Gson()
    private val launches = arrayListOf<LaunchData>()
    private val favorites = arrayListOf<String>()

    private val _isFavorites = MutableStateFlow(false)
    val showFavorites = _isFavorites.asStateFlow()

    private val _showItems = MutableStateFlow<List<LaunchItemUI>>(mutableListOf())
    val showItems: StateFlow<List<LaunchItemUI>> = _showItems


    override suspend fun fetchData() {
        val response = httpDataSource.getLaunches()
        response.body()?.let {
            launches.clear()
            launches.addAll(it)
        }

        val favoritesStr = preferencesDataSource.getFavorites()
        if (favoritesStr.isNotEmpty()) {
            try {
                val type = object : TypeToken<List<String>>() {}.type
                favorites.clear()
                favorites.addAll(gson.fromJson(favoritesStr, type))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        calcShowItems()
    }

    override fun showFavorites(show: Boolean) {
        _isFavorites.value = show
        calcShowItems()
    }

    override fun setFavorite(id: String) {
        favorites.add(id)
        preferencesDataSource.saveFavorites(gson.toJson(favorites))
        calcShowItems()
    }

    override fun unSetFavorite(id: String) {
        favorites.remove(id)
        preferencesDataSource.saveFavorites(gson.toJson(favorites))
        calcShowItems()
    }

    override fun switchFavorite(id: String) {
        if (favorites.contains(id)) unSetFavorite(id) else setFavorite(id)
    }

    override suspend fun getDetails(id: String): LaunchDetailsUi? {
        launches.find { it.id == id }?.let { launch ->
            val rocketResult = httpDataSource.getRocket(launch.rocket).body()?.name?:"Confidential"
            var mass = 0
            launch.payloads.forEach {
                val massPayload = httpDataSource.getPayload(it).body()?.mass?:0
                mass += massPayload
            }
            val isFavorite = favorites.contains(launch.id)

            return launch.toDetailedUI(rocketResult, mass, isFavorite)
        }
        return null
    }

    override fun getShowItemsFlow(): StateFlow<List<LaunchItemUI>> {
        return showItems
    }

    override fun getShowFavoritesFlow(): StateFlow<Boolean> {
        return showFavorites
    }

    private fun calcShowItems() {
        val list = launches.map {
            val isFavorite = favorites.contains(it.id)
            it.toLaunchItemUI(isFavorite)
        }

        _showItems.value = if (showFavorites.value) list.filter { favorites.contains(it.id) } else list
    }


}