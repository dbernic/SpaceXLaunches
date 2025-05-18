package com.dbernic.spacexlaunches.di

import com.dbernic.spacexlaunches.model.datasources.local.PreferencesDataSourceImpl
import com.dbernic.spacexlaunches.model.datasources.rest.HttpInterface
import com.dbernic.spacexlaunches.model.repositories.LaunchesRepository
import com.dbernic.spacexlaunches.model.repositories.LaunchesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideLaunchesRepository(
        httpInterface: HttpInterface,
        preferencesDataSource: PreferencesDataSourceImpl
    ): LaunchesRepository = LaunchesRepositoryImpl(
        httpInterface, preferencesDataSource
    )

}