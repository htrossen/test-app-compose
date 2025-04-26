package com.example.testappcompose.core.net

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetModule {
    private const val APPLICATION_JSON = "application/json; charset=UTF8"

    private const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"

    @Provides
    @Singleton
    @Named("testOkHttpClient")
    fun provideOkHttpClient(
        interceptors: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient = OkHttpClient.Builder().apply {
        interceptors.forEach(::addInterceptor)
        readTimeout(30, TimeUnit.SECONDS)
    }.build()

    @Suppress("JSON_FORMAT_REDUNDANT")
    @Provides
    @Singleton
    @Named("testRetrofit")
    fun provideRetrofit(
        @Named("testOkHttpClient") okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(
            Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }.asConverterFactory(APPLICATION_JSON.toMediaType())
        )
        .build()

    @Provides
    @Singleton
    fun provideTestApi(@Named("testRetrofit") retrofit: Retrofit): CocktailApi =
        retrofit.create(CocktailApi::class.java)
}
