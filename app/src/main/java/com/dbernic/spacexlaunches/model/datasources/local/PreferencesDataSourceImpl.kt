package com.dbernic.spacexlaunches.model.datasources.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
): PreferencesDataSource {

    private val FAVORITES = "favorites"

    private var pref: SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    private fun setValue(key: String, value: String) {
        pref.edit().putString(key, value).apply()
    }

    private fun getStringValue(key: String): String? {
        return pref.getString(key, null)
    }

    private fun getStringValue(key: String, default: String): String {
        return pref.getString(key, null)?: default
    }

    private fun getStringOrEmpty(key: String): String {
        return pref.getString(key, null) ?: ""
    }

    override fun getFavorites(): String = getStringOrEmpty(FAVORITES)

    override fun saveFavorites(favorites: String) {
        setValue(FAVORITES, favorites)
    }

}