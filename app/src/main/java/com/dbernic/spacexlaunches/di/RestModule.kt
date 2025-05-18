package com.dbernic.spacexlaunches.di

import com.dbernic.spacexlaunches.model.datasources.rest.HttpInterface
import com.dbernic.spacexlaunches.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RestModule {

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().apply {
            setLenient()
            setPrettyPrinting()
        }.create()
    }

    @Provides
    fun provideConvertorFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {

        return OkHttpClient.Builder().apply {
            readTimeout(60, TimeUnit.SECONDS)
            connectTimeout(60, TimeUnit.SECONDS)
            addInterceptor(httpLoggingInterceptor)
        }.build()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(
        okHttpClient: OkHttpClient,
        gsonFactory: GsonConverterFactory,
    ): Retrofit.Builder {
        return Retrofit.Builder().apply {
            client(okHttpClient)
            addConverterFactory(gsonFactory)
        }
    }

    @Provides
    fun provideHttpInterface(retrofitBuilder: Retrofit.Builder) : HttpInterface {
        return retrofitBuilder
            .baseUrl(Constants.SERVER)
            .build()
            .create(HttpInterface::class.java)
    }
}