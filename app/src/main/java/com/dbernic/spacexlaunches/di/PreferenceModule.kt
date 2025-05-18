package com.dbernic.spacexlaunches.di

import android.content.Context
import com.dbernic.spacexlaunches.model.datasources.local.PreferencesDataSource
import com.dbernic.spacexlaunches.model.datasources.local.PreferencesDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Singleton
    @Provides
    fun providePreferencesDataSource(
        @ApplicationContext context: Context
    ): PreferencesDataSource = PreferencesDataSourceImpl(context)
}