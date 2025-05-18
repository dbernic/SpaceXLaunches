package com.dbernic.spacexlaunches.model.datasources.local

interface PreferencesDataSource {

    fun getFavorites(): String

    fun saveFavorites(favorites: String)
}